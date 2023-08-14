package org.hertsstack.rpcclient;

import io.grpc.Channel;
import io.grpc.ClientInterceptor;
import io.grpc.ConnectivityState;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.hertsstack.core.exception.ChannelIReconnectionException;
import org.hertsstack.core.logger.Logging;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

class ConnectionManager {
    private static final java.util.logging.Logger logger = Logging.getLogger(ConnectionManager.class.getSimpleName());
    private static final int tryThreshold = 100;

    private final AtomicBoolean isReconnecting = new AtomicBoolean(false);
    private ManagedChannelBuilder<?> builder;
    private ManagedChannel channel;
    private GrpcClientOption option;
    private boolean isKilledClient;
    private int retryCnt = 0;

    interface Reconnection {
        void reconnect(Channel channel);
    }

    ConnectionManager(Channel channel, GrpcClientOption option) {
        this.channel = (ManagedChannel) channel;
        this.option = option;

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            this.isKilledClient = true;
            logger.info("Caught sigterm on Herts client");
        }));
    }

    public void reconnectListener(Reconnection reconnection) {
        reconnection.reconnect(this.channel);
    }

    public ManagedChannel connect(String host, int port, boolean isSecureConnection, ClientInterceptor interceptor, boolean isReconnection) {
        this.builder = ManagedChannelBuilder.forAddress(host, port);

        if (!isSecureConnection) {
            this.builder = this.builder.usePlaintext();
        }
        if (interceptor != null) {
            this.builder = this.builder.intercept(interceptor);
        } else {
            this.builder = this.builder.intercept(HertsRpcClientInterceptBuilder.builder(new HertsRpcClientEmptyInterceptor()).build());
        }
        if (this.option.getKeepaliveMilliSec() != null) {
            this.builder = this.builder.keepAliveTime(this.option.getKeepaliveMilliSec(), TimeUnit.MILLISECONDS);
        }
        if (this.option.getKeepaliveTimeoutMilliSec() != null) {
            this.builder = this.builder.keepAliveTimeout(this.option.getKeepaliveTimeoutMilliSec(), TimeUnit.MILLISECONDS);
        }
        if (this.option.getIdleTimeoutMilliSec() != null) {
            this.builder = this.builder.idleTimeout(this.option.getIdleTimeoutMilliSec(), TimeUnit.MILLISECONDS);
        }
        this.builder = this.builder.enableRetry();
        this.channel = this.builder.build();

        if (isReconnection) {
            watchState();
        }
        return this.channel;
    }

    private void watchState() {
        Thread thread = new Thread(() -> {
            while (!isKilledClient) {
                try {
                    checkState();
                    Thread.sleep(1000);
                } catch (InterruptedException ignore) {
                } catch (ChannelIReconnectionException ex) {
                    logger.warning(ex.getMessage());
                    break;
                }
            }
            logger.info("Closed thread for watching grpc state");
        });
        thread.start();
    }

    private void checkState() {
        ConnectivityState currentState = channel.getState(true);
        if (currentState == ConnectivityState.READY) {
            if (this.isReconnecting.get()) {
                logger.info("gRPC channel reconnection is successfully");
            }
            retryCnt = 0;
            this.isReconnecting.set(false);
        } else if (currentState == ConnectivityState.IDLE) {
            // Nothing
        } else if (currentState == ConnectivityState.CONNECTING) {
            // Nothing
        } else if (currentState == ConnectivityState.TRANSIENT_FAILURE) {
            logger.warning("Caught TCP 3-way handshake timing out or a socket on grpc channel");
            this.isReconnecting.set(true);
            reconnectChannel();
        } else if (currentState == ConnectivityState.SHUTDOWN) {
            logger.warning("Caught shutdown on grpc channel");
            this.isReconnecting.set(true);
            reconnectChannel();
        } else {
            logger.warning("Unknown state on grpc channel");
        }
    }

    private boolean shouldChallenge() {
        return !this.isKilledClient && this.retryCnt < tryThreshold;
    }

    private void reconnectChannel() {
        if (shouldChallenge()) {
            this.channel.resetConnectBackoff();
            this.channel = this.builder.build();
            this.retryCnt++;
            logger.info("Trying reconnection for grpc channel. Challenge up to " + tryThreshold + " times. Count: " + this.retryCnt);
        } else {
            this.channel.shutdown();
            throw new ChannelIReconnectionException("Could not reconnect to server. Exceeded reconnection threshold " + this.retryCnt);
        }
    }
}
