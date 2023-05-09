package org.herts.rpc.validator;

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
    public static boolean isAllReturnVoid(List<HertsService> hertsServices) {
        for (HertsService hertsService : hertsServices) {
            for (Method method : hertsService.getClass().getDeclaredMethods()) {
                var methodReturnType = method.getReturnType().getName();
                if (!methodReturnType.equals("void")) {
                    return false;
                }
            }
        }
        return true;
    }
}
