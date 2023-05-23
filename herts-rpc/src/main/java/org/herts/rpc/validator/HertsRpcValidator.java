package org.herts.rpc.validator;

import org.herts.common.service.HertsService;
import org.herts.common.util.HertsServiceValidateUtil;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Herts rpc validator
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsRpcValidator extends HertsServiceValidateUtil {
    private static final String ioGrpcStreamPkgName = "io.grpc.stub.StreamObserver";

    public static boolean isAllReturnStreamObserver(List<HertsService> hertsServices) {
        for (HertsService hertsService : hertsServices) {
            for (Method method : hertsService.getClass().getDeclaredMethods()) {
                var methodReturnType = method.getReturnType().getName();
                if (!methodReturnType.equals(ioGrpcStreamPkgName)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean hasReactiveInterface(List<HertsService> hertsServices) {
        for (HertsService service : hertsServices) {
            Type type = service.getClass().getGenericSuperclass();

            if (type instanceof ParameterizedType parameterizedType) {
                Type[] typeArguments = parameterizedType.getActualTypeArguments();
                if (typeArguments.length > 1) {
                    Type typeArgument = typeArguments[1];
                    if (typeArgument instanceof Class<?> genericClass) {
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
