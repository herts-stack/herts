package org.hertsstack.gateway;

import org.hertsstack.core.exception.ServiceMethodNotfoundException;
import org.hertsstack.core.service.HertsService;
import org.hertsstack.rpcclient.HertsRpcClient;
import org.hertsstack.rpcclient.HertsRpcClientBuilder;
import org.hertsstack.rpcclient.HertsRpcClientIBuilder;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class RpcClient {
    private final ConcurrentMap<String, RpcDefinition> serviceDef;

    private RpcClient(ConcurrentMap<String, RpcDefinition> serviceDef) {
        this.serviceDef = serviceDef;
    }

    public static RpcClient create(String host, boolean isSecure, int port, List<Class<?>> hertsServiceInterfaces) {
        HertsRpcClientIBuilder builder = HertsRpcClientBuilder
                .builder(host, port)
                .secure(isSecure)
                .autoReconnection(true);

        for (Class<?> hertsServiceInterface : hertsServiceInterfaces) {
            builder = builder.registerHertsRpcServiceInterface(hertsServiceInterface);
        }

        ConcurrentMap<String, RpcDefinition> serviceDef = new ConcurrentHashMap<>();
        HertsRpcClient client = builder.connect();
        for (Class<?> hertsServiceInterface : hertsServiceInterfaces) {
            HertsService hertsRpcService = client.createUnknownHertsRpcService(hertsServiceInterface);
            String serviceName = hertsServiceInterface.getSimpleName();
            serviceDef.put(serviceName, RpcDefinition.create(hertsRpcService));
        }

        return new RpcClient(serviceDef);
    }

    public RpcDefinition getRpcDefinition(String serviceName) {
        return serviceDef.get(serviceName);
    }

    public Object callRpc(String serviceName, String methodName, Object... params) throws InvocationTargetException, IllegalAccessException {
        RpcDefinition rpcDefinition = this.serviceDef.get(serviceName);
        if (rpcDefinition == null) {
            throw new ServiceMethodNotfoundException(serviceName + " is not defined");
        }
        return rpcDefinition.callMethod(methodName, params);
    }
}
