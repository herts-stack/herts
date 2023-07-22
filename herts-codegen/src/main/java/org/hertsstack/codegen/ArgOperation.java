package org.hertsstack.codegen;

import org.hertsstack.core.context.HertsType;

public class ArgOperation {
    public enum Lang {
        Typescript
    }

    public static HertsType convertHertsType(String hertsType) {
        if (hertsType == null || hertsType.isEmpty()) {
            return null;
        }
        for (HertsType h : HertsType.values()) {
            if (h.name().equals(hertsType)) {
                return h;
            }
        }
        return null;
    }

    public static Lang convertLang(String lang) {
        if (lang == null || lang.isEmpty()) {
            return null;
        }
        for (Lang l : Lang.values()) {
            if (l.name().equals(lang)) {
                return l;
            }
        }
        return null;
    }

}
