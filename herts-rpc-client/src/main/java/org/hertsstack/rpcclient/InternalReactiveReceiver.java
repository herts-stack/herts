package org.hertsstack.rpcclient;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.MethodDescriptor;
import io.grpc.stub.AbstractStub;
import io.grpc.stub.ClientCalls;
import io.grpc.stub.StreamObserver;

import org.hertsstack.core.modelx.InternalRpcMsg;
import org.hertsstack.serializer.MessageJsonParsingException;
import org.hertsstack.core.modelx.HertsMessage;
import org.hertsstack.core.context.HertsType;
import org.hertsstack.core.descriptor.CustomGrpcDescriptor;
import org.hertsstack.serializer.MessageSerializer;
import org.hertsstack.core.service.HertsReceiver;

import java.lang.reflect.Method;

/**
 * Internal reactive receiver
 *
 * @author Herts Contributer
 */
class InternalReactiveReceiver {

    private final HertsReceiver hertsReceiver;
    private final ClientRequestInfo clientConnection;

    private InternalReactiveReceiver(HertsReceiver hertsReceiver, ClientRequestInfo clientConnection) {
        this.hertsReceiver = hertsReceiver;
        this.clientConnection = clientConnection;
    }

    public static InternalReactiveReceiver create(HertsReceiver hertsReceiver, ClientRequestInfo clientConnection) {
        return new InternalReactiveReceiver(hertsReceiver, clientConnection);
    }

    public String getClientId() {
        return this.clientConnection.getClientId();
    }

    /**
     * Create InternalReceiverStub.
     *
     * @param channel Grpc Channel
     * @return InternalReceiverStub
     */
    public InternalReceiverStub newHertsReactiveStreamingService(Channel channel) {
        io.grpc.stub.AbstractStub.StubFactory<InternalReceiverStub> factory = new AbstractStub.StubFactory<>() {
            @Override
            public InternalReceiverStub newStub(Channel channel, CallOptions callOptions) {
                return new InternalReceiverStub(channel, callOptions, hertsReceiver);
            }
        };
        return InternalReceiverStub.newStub(factory, channel).withCallCredentials(this.clientConnection);
    }

    /**
     * InternalReceiverStub class.
     */
    public static class InternalReceiverStub extends io.grpc.stub.AbstractBlockingStub<InternalReceiverStub> {
        private final MessageSerializer serializer = new MessageSerializer();
        private final HertsReceiver hertsReceiver;
        private final Channel channel;
        private final CallOptions callOptions;

        protected InternalReceiverStub(Channel channel, CallOptions callOptions, HertsReceiver hertsReceiver) {
            super(channel, callOptions);
            this.hertsReceiver = hertsReceiver;
            this.channel = channel;
            this.callOptions = callOptions;
        }

        /**
         * Register receiver to server.
         *
         * @param streaming Class
         */
        public void registerReceiver(Class<?> streaming) throws MessageJsonParsingException {
            String serviceName = streaming.getName();
            Method method = streaming.getDeclaredMethods()[0];

            MethodDescriptor<Object, Object> methodDescriptor = CustomGrpcDescriptor
                    .generateStramingMethodDescriptor(HertsType.ServerStreaming, serviceName, method.getName());

            StreamObserver<Object> responseObserver = new InternalReactiveObserver(this.hertsReceiver);

            Object[] methodParameters = new Object[0];
            Class<?>[] parameterTypes = new Class<?>[1];
            parameterTypes[0] = method.getParameterTypes()[0];

            byte[] requestBytes = this.serializer.serialize(new InternalRpcMsg(methodParameters, parameterTypes));

            ClientCalls.asyncServerStreamingCall(this.channel.newCall(methodDescriptor, this.callOptions), requestBytes, responseObserver);
        }

        @Override
        protected InternalReceiverStub build(Channel channel, CallOptions callOptions) {
            return new InternalReceiverStub(channel, callOptions, this.hertsReceiver);
        }
    }
}
