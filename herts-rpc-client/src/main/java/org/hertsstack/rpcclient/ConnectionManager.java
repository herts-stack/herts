package org.hertsstack.rpcclient;

import io.grpc.Channel;
import io.grpc.ClientInterceptor;
import io.grpc.ConnectivityState;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.hertsstack.core.logger.Logging;

import java.util.concurrent.TimeUnit;

class ConnectionManager {
    private static final java.util.logging.Logger logger = Logging.getLogger(ConnectionManager.class.getSimpleName());

    private ManagedChannelBuilder<?> builder;
    private ManagedChannel channel;
    private GrpcClientOption option;
    private boolean isKilledClient;

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

    public ManagedChannel connect(String host, int port, boolean isSecureConnection, ClientInterceptor interceptor) {
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

        // No support auto reconnection on library
        // automaticReconnect();
        return this.channel;
    }

    private void automaticReconnect() {
        Thread thread = new Thread(() -> {
            logger.info("Thread start");
            while (!isKilledClient) {
                ConnectivityState currentState = channel.getState(true);
                if (currentState == ConnectivityState.READY) {
                    logger.info("Ready");
                    retryCnt = 0;
                } else if (currentState == ConnectivityState.IDLE) {
                    logger.info("Idle");
                } else if (currentState == ConnectivityState.CONNECTING) {
                    logger.info("Connecting");
                } else if (currentState == ConnectivityState.TRANSIENT_FAILURE) {
                    logger.info("Caught TRANSIENT_FAILURE on channel");
                    rebuildChannel();
                    break;
                } else if (currentState == ConnectivityState.SHUTDOWN) {
                    logger.info("Caught SHUTDOWN on channel");
                    rebuildChannel();
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            logger.info("Thread Closed");
        });
        thread.start();
    }

    private int retryCnt = 0;
    private static final int tryThreshold = 5;
    private void rebuildChannel() {
        this.channel.shutdown();
        if (!this.isKilledClient && this.retryCnt < tryThreshold) {
            this.channel = this.builder.build();
            this.retryCnt++;
            logger.info("Trying reconnection for channel. Try: " + this.retryCnt);
            automaticReconnect();
        }
    }
}
