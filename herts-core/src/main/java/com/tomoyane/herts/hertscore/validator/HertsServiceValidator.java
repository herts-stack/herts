package com.tomoyane.herts.hertscore.validator;

import com.tomoyane.herts.hertscommon.context.HertsCoreType;

import java.util.List;

public class HertsServiceValidator {

    public static boolean isSameType(List<HertsCoreType> coreTypes) {
        return coreTypes.isEmpty() ||
                coreTypes.stream().allMatch(coreTypes.get(0)::equals);
    }
}
