package com.tomoyane.herts.hertshttp.validator;

import com.tomoyane.herts.hertscommon.context.HertsCoreType;
import com.tomoyane.herts.hertscommon.service.HertsCoreService;
import com.tomoyane.herts.hertscommon.util.RpcValidateUtil;

import java.util.List;

public class HertsHttpValidator extends RpcValidateUtil {
    public static boolean isAllHttpType(List<HertsCoreService> coreServices) {
        for (HertsCoreService coreService : coreServices) {
            if (coreService.getHertsCoreType() != HertsCoreType.Http) {
                return false;
            }
        }
        return true;
    }
}
