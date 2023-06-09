package org.herts.rpc;

import org.herts.core.service.HertsService;
import org.herts.core.util.ServiceValidateUtil;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Herts rpc validator
 *
 * @author Herts Contributer
 * @version 1.0.0
 */
class RpcValidator extends ServiceValidateUtil {
    private static final String ioGrpcStreamPkgName = "io.grpc.stub.StreamObserver";

    /**
     * Check method type of all services.
     * If all method return type is Streaming, return true
     *
     * @param hertsServices HertsService list
     * @return Result
     */
    public static boolean isAllReturnStreamObserver(List<HertsService> hertsServices) {
        for (HertsService hertsService : hertsServices) {
            for (Method method : hertsService.getClass().getDeclaredMethods()) {
                String methodReturnType = method.getReturnType().getName();
                if (!methodReturnType.equals(ioGrpcStreamPkgName)) {
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
     * @param hertsServices HertsService list
     * @return Result
     */
    public static boolean isAllReceiverVoid(List<HertsService> hertsServices) {
        for (HertsService service : hertsServices) {
            Type type = service.getClass().getGenericSuperclass();

            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Type[] typeArguments = parameterizedType.getActualTypeArguments();
                if (typeArguments.length > 1) {
                    Type typeArgument = typeArguments[1];
                    if (typeArgument instanceof Class<?>) {
                        Class<?> genericClass = (Class<?>) typeArgument;
                        Method[] methods = genericClass.getDeclaredMethods();
                        for (Method method : methods) {
                            if (!method.getReturnType().getName().equals(voidReturnName)) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
