package org.herts.rpcclient.receiver;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.MethodDescriptor;
import io.grpc.stub.ClientCalls;
import io.grpc.stub.StreamObserver;
import org.herts.common.context.HertsClientInfo;
import org.herts.common.context.HertsMsg;
import org.herts.common.context.HertsType;
import org.herts.common.descriptor.HertsGrpcDescriptor;
import org.herts.common.serializer.HertsSerializer;
import org.herts.common.service.HertsReceiver;

import java.lang.reflect.Method;
import java.util.UUID;

public class InternalReceiveStreaming {

    public InternalReceiveStreaming(HertsReceiver hertsReceiver) {
    }

    public InternalReceiverStub newHertsClientStreamingService(Channel channel) {
        io.grpc.stub.AbstractStub.StubFactory<InternalReceiverStub> factory = InternalReceiverStub::new;
        return InternalReceiverStub.newStub(factory, channel);
    }

    public static class InternalReceiverStub extends io.grpc.stub.AbstractBlockingStub<InternalReceiverStub> {
        private final HertsSerializer serializer = new HertsSerializer();

        private Channel channel;
        private CallOptions callOptions;

        protected InternalReceiverStub(Channel channel, CallOptions callOptions) {
            super(channel, callOptions);
            this.channel = channel;
            this.callOptions = callOptions;
        }

        public void registerReceiver(Class<?> streaming) throws JsonProcessingException {
            var serviceName = streaming.getName();
            Method method = streaming.getDeclaredMethods()[0];

            System.out.println(serviceName);
            System.out.println(method.getName());
            MethodDescriptor<Object, Object> methodDescriptor = HertsGrpcDescriptor
                    .generateStramingMethodDescriptor(HertsType.ServerStreaming, serviceName, method.getName());

            var clientInfo = new HertsClientInfo();
            clientInfo.setId(UUID.randomUUID().toString());
            StreamObserver<Object> responseObserver = new ReceiveObserver();

            Object[] methodParameters = new Object[1];
            methodParameters[0] = clientInfo;

            Class<?>[] parameterTypes = new Class<?>[1];
            parameterTypes[0] = method.getParameterTypes()[0];

            byte[] requestBytes = this.serializer.serialize(new HertsMsg(methodParameters, parameterTypes));

            ClientCalls.asyncServerStreamingCall(this.channel.newCall(methodDescriptor, this.callOptions), requestBytes, responseObserver);
        }

        @Override
        protected InternalReceiverStub build(Channel channel, CallOptions callOptions) {
            return new InternalReceiverStub(channel, callOptions);
        }
    }
}
