package org.herts.rpc.validator;

import org.herts.common.service.HertsDuplexStreamingService;
import org.herts.common.service.HertsService;
import org.herts.common.util.HertsServiceValidateUtil;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Herts rpc validator
 * @author Herts Contributer
 * @version 1.0.0
 */
public class HertsRpcValidator extends HertsServiceValidateUtil {
    private static final String voidReturnName = "void";
    private static final String ioGrpcStreamPkgName = "io.grpc.stub.StreamObserver";

    public static boolean isAllReturnVoid(List<HertsService> hertsServices) {
        for (HertsService hertsService : hertsServices) {
            for (Method method : hertsService.getClass().getDeclaredMethods()) {
                var methodReturnType = method.getReturnType().getName();
                if (!methodReturnType.equals(voidReturnName)) {
                    return false;
                }
            }
        }
        return true;
    }

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

    public static boolean hasDuplexInterface(List<HertsService> hertsServices) {
        for (HertsService service : hertsServices) {
            HertsDuplexStreamingService<Object, Object> duplexStreamingService;
            try {
                duplexStreamingService = (HertsDuplexStreamingService<Object, Object>) service;
            } catch (Exception ex) {
                return false;
            }

            var receiver = duplexStreamingService.getReceiver();
            if (receiver == null) {
                return false;
            }
            var duplexService = duplexStreamingService.getService();
            if (duplexService == null) {
                return false;
            }
            for (Method method : receiver.getClass().getDeclaredMethods()) {
                var methodReturnType = method.getReturnType().getName();
                if (!methodReturnType.equals(voidReturnName)) {
                    return false;
                }
            }
        }
        return true;
    }
}
