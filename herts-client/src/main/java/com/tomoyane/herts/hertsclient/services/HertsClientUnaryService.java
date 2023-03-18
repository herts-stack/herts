package com.tomoyane.herts.hertsclient.services;

import com.tomoyane.herts.hertscommon.exceptions.HertsRpcNotFoundException;
import com.tomoyane.herts.hertscore.core.UnaryServiceCore;
import com.tomoyane.herts.hertscore.service.HertsService;
import io.grpc.CallOptions;
import io.grpc.Channel;

import java.lang.reflect.Method;

public class HertsClientUnaryService extends io.grpc.stub.AbstractBlockingStub<HertsClientUnaryService> implements HertsService {

    private HertsService hertsService;
    private Method[] methods;

    public HertsClientUnaryService(Channel channel, CallOptions callOptions, HertsService hertsService) {
        super(channel, callOptions);
        this.hertsService = hertsService;

        String serviceName = hertsService.getClass().getName();
        Class<?> thisClass;
        try {
            thisClass = Class.forName(serviceName);
        } catch (ClassNotFoundException ignore) {
            throw new HertsRpcNotFoundException("Unknown class name. Allowed class is " + UnaryServiceCore.class.getName());
        }

        this.methods = thisClass.getDeclaredMethods();
    }

    @Override
    protected HertsClientUnaryService build(Channel channel, CallOptions callOptions) {
        return new HertsClientUnaryService(channel, callOptions, this.hertsService);
    }

    public void invoke() {

    }
}
