package com.tomoyane.herts.hertscommon.util;

import com.tomoyane.herts.hertscommon.context.HertsCoreType;
import com.tomoyane.herts.hertscommon.exception.HertsRpcNotFoundException;
import com.tomoyane.herts.hertscommon.service.HertsCoreService;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class RpcValidateUtil {
    public static boolean isSameHertsCoreType(List<HertsCoreType> coreTypes) {
        return coreTypes.isEmpty() ||
                coreTypes.stream().allMatch(coreTypes.get(0)::equals);
    }

    public static String validateRegisteredServices(List<HertsCoreService> services) {
        List<String> serviceNames = new ArrayList<>();
        for (HertsCoreService hertsCoreService : services) {
            var serviceName = hertsCoreService.getClass().getName();
            serviceNames.add(serviceName);

            Class<?> thisClass;
            try {
                thisClass = Class.forName(serviceName);
            } catch (ClassNotFoundException ignore) {
                throw new HertsRpcNotFoundException("Unknown class name. Allowed class is " + serviceName);
            }

            Method[] methods = thisClass.getDeclaredMethods();
            List<String> methodNames = new ArrayList<>();
            for (Method method : methods) {
                methodNames.add(method.getName());
            }

            if (CollectionUtil.findDuplicates(methodNames).size() > 0) {
                return "Method name is duplicated. Herts supports uniq method name only. HertsService = " + serviceName;
            }
        }

        var dupServiceNames = CollectionUtil.findDuplicates(serviceNames);
        if (dupServiceNames.size() > 0) {
            return "HertsService name is duplicated.";
        }
        return "";
    }

    public static boolean isValidStreamingRpc(List<HertsCoreService> services) {
        for (HertsCoreService service : services) {
            if (service.getHertsCoreType() != HertsCoreType.BidirectionalStreaming && service.getHertsCoreType() != HertsCoreType.ClientStreaming) {
                continue;
            }
            Class<?> thisClass;
            try {
                thisClass = Class.forName(service.getClass().getName());
            } catch (ClassNotFoundException ignore) {
                continue;
            }

            Method[] methods = thisClass.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getParameterTypes().length != 1) {
                    return false;
                }
                if (!method.getParameterTypes()[0].getName().equals("io.grpc.stub.StreamObserver")) {
                    return false;
                }
            }
        }
        return true;
    }
}
