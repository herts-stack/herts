package org.herts.common.util;

import org.herts.common.service.HertsRpcService;
import org.herts.common.context.HertsType;
import org.herts.common.exception.HertsServiceNotFoundException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Herts service validation utility
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsServiceValidateUtil {

    /**
     * Check registered hert type
     * @param coreTypes herts types
     * @return Result
     */
    public static boolean isSameHertsCoreType(List<HertsType> coreTypes) {
        return coreTypes.isEmpty() ||
                coreTypes.stream().allMatch(coreTypes.get(0)::equals);
    }

    /**
     * Check registered service
     * @param services HertsService list
     * @return Result
     */
    public static String validateRegisteredServices(List<HertsRpcService> services) {
        List<String> serviceNames = new ArrayList<>();
        for (HertsRpcService hertsRpcService : services) {
            var serviceName = hertsRpcService.getClass().getName();
            serviceNames.add(serviceName);

            Class<?> thisClass;
            try {
                thisClass = Class.forName(serviceName);
            } catch (ClassNotFoundException ignore) {
                throw new HertsServiceNotFoundException("Unknown class name. Allowed class is " + serviceName);
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

    /**
     * Check streaming rpc precondition
     * @param services Herts service list
     * @return Result
     */
    public static boolean isValidStreamingRpc(List<HertsRpcService> services) {
        for (HertsRpcService service : services) {
            if (service.getHertsType() != HertsType.BidirectionalStreaming && service.getHertsType() != HertsType.ClientStreaming) {
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

    /**
     * Check http type
     * @param coreServices Herts service list
     * @return Result
     */
    public static boolean isAllHttpType(List<HertsRpcService> coreServices) {
        for (HertsRpcService coreService : coreServices) {
            if (coreService.getHertsType() != HertsType.Http) {
                return false;
            }
        }
        return true;
    }
}
