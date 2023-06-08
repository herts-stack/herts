package org.herts.core.util;

import org.herts.core.service.HertsReceiver;
import org.herts.core.service.HertsService;
import org.herts.core.context.HertsType;
import org.herts.core.exception.HertsServiceNotFoundException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Herts service validation utility
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsServiceValidateUtil {
    protected static final String voidReturnName = "void";

    /**
     * Check registered hert type
     *
     * @param coreTypes herts types
     * @return Result
     */
    public static boolean isSameHertsCoreType(List<HertsType> coreTypes) {
        return coreTypes.isEmpty() ||
                coreTypes.stream().allMatch(coreTypes.get(0)::equals);
    }

    /**
     * Check registered service
     *
     * @param services HertsService list
     * @return Result
     */
    public static String validateRegisteredServices(List<HertsService> services) {
        List<String> serviceNames = new ArrayList<>();
        Method[] methods = null;
        for (HertsService hertsRpcService : services) {
            String serviceName = hertsRpcService.getClass().getName();
            serviceNames.add(serviceName);

            Class<?> thisClass;
            try {
                thisClass = Class.forName(serviceName);
                methods = thisClass.getDeclaredMethods();
            } catch (ClassNotFoundException ignore) {
                throw new HertsServiceNotFoundException("Unknown class name. Allowed class is " + serviceName);
            }
        }
        if (methods == null) {
            throw new HertsServiceNotFoundException("Definition method is not found");
        }
        return validateMethods(methods, serviceNames);
    }

    /**
     * Check method condition
     *
     * @param classes Class of List
     * @return Result
     */
    public static String validateMethod(List<Class<?>> classes) {
        List<String> serviceNames = new ArrayList<>();
        Method[] methods = null;
        for (Class<?> c : classes) {
            try {
                String serviceName = c.getName();
                serviceNames.add(serviceName);
                methods = c.getDeclaredMethods();
            } catch (Exception ex) {
                throw new HertsServiceNotFoundException("Unknown class name.");
            }
        }
        if (methods == null) {
            throw new HertsServiceNotFoundException("Definition method is not found");
        }
        return validateMethods(methods, serviceNames);
    }

    /**
     * Check method condition
     *
     * @param methods      Method of List
     * @param serviceNames Herts service name
     * @return Result
     */
    public static String validateMethods(Method[] methods, List<String> serviceNames) {
        List<String> methodNames = new ArrayList<>();
        for (Method method : methods) {
            methodNames.add(method.getName());
        }
        if (CollectionUtil.findDuplicates(methodNames).size() > 0) {
            return "Method name is duplicated. Herts supports uniq method name only.";
        }

        Set<String> dupServiceNames = CollectionUtil.findDuplicates(serviceNames);
        if (dupServiceNames.size() > 0) {
            return "HertsService name is duplicated.";
        }
        return "";
    }

    /**
     * Check streaming rpc method
     *
     * @param classTypes Class of List
     * @return Result
     */
    public static boolean isStreamingRpc(List<Class<?>> classTypes) {
        for (Class<?> c : classTypes) {
            try {
                Method[] methods = c.getDeclaredMethods();
                for (Method method : methods) {
                    if (method.getParameterTypes().length != 1) {
                        return false;
                    }
                    if (!method.getParameterTypes()[0].getName().equals("io.grpc.stub.StreamObserver")) {
                        return false;
                    }
                }
            } catch (Exception ex) {
                throw new HertsServiceNotFoundException("Unknown class name.");
            }
        }
        return true;
    }

    /**
     * Check streaming rpc precondition
     *
     * @param services Herts service list
     * @return Result
     */
    public static boolean isValidStreamingRpc(List<HertsService> services) {
        for (HertsService service : services) {
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
     *
     * @param serviceTypes Herts service type list
     * @return Result
     */
    public static boolean isAllHttpType(List<HertsType> serviceTypes) {
        for (HertsType hertsType : serviceTypes) {
            if (hertsType != HertsType.Http) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check http type
     *
     * @param coreServices Herts service list
     * @return Result
     */
    public static boolean isAllHttpTypeByService(List<HertsService> coreServices) {
        for (HertsService coreService : coreServices) {
            if (coreService.getHertsType() != HertsType.Http) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check method type of all services.
     * If all method return type is void, return true
     *
     * @param hertsServices HertsService list
     * @return Result
     */
    public static boolean isAllReturnVoid(List<HertsService> hertsServices) {
        for (HertsService hertsService : hertsServices) {
            for (Method method : hertsService.getClass().getDeclaredMethods()) {
                String methodReturnType = method.getReturnType().getName();
                if (!methodReturnType.equals(voidReturnName)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Check method type of all receiver services.
     * If all method return type is void, return true
     *
     * @param hertsReceivers HertsReceiver list
     * @return Result
     */
    public static boolean isAllReturnVoidBy(List<HertsReceiver> hertsReceivers) {
        for (HertsReceiver herts : hertsReceivers) {
            for (Method method : herts.getClass().getDeclaredMethods()) {
                String methodReturnType = method.getReturnType().getName();
                if (!methodReturnType.equals(voidReturnName)) {
                    return false;
                }
            }
        }
        return true;
    }
}
