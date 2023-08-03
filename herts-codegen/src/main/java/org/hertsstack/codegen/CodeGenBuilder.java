package org.hertsstack.codegen;

import org.hertsstack.core.annotation.HertsHttp;
import org.hertsstack.core.context.HertsType;
import org.hertsstack.core.exception.CodeGenException;

import java.util.ArrayList;
import java.util.List;

public class CodeGenBuilder {
    private final List<Class<?>> hertsServices = new ArrayList<>();
    private CodeGenLang lang = null;

    public static CodeGenBuilder builder() {
        return new CodeGenBuilder();
    }

    public CodeGenBuilder hertsService(Class<?> interfaceClass) {
        this.hertsServices.add(interfaceClass);
        return this;
    }

    public CodeGenBuilder lang(CodeGenLang lang) {
        this.lang = lang;
        return this;
    }

    public CodeGenEngine build() {
        if (this.hertsServices.size() == 0) {
            throw new CodeGenException("Please set HertsService");
        }
        if (this.lang == null) {
            throw new CodeGenException("Please set Lang");
        }

        for (Class<?> c : this.hertsServices) {
            try {
                HertsHttp annotation = c.getAnnotation(HertsHttp.class);
                if (annotation.value() != HertsType.Http) {
                    throw new CodeGenException("Code generation supports " + HertsType.Http + " only");
                }
            } catch (Exception ex) {
                throw new CodeGenException("Code generation supports " + HertsType.Http + " only");
            }
        }
        return new CodeGenEngineImpl(this.hertsServices, this.lang);
    }
}
