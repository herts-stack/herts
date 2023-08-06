package org.hertsstack.codegen;

import org.hertsstack.core.exception.CodeGenException;

import java.util.List;

class CodeGenEngineImpl implements CodeGenEngine {
    private final List<Class<?>> hertsServices;
    private final CodeGenLang lang;

    public CodeGenEngineImpl(List<Class<?>> hertsServices, CodeGenLang lang) {
        this.hertsServices = hertsServices;
        this.lang = lang;
    }

    @Override
    public void generate() {
        generateCode("");
    }

    @Override
    public void generate(String absoluteOutDir) {
        if (!absoluteOutDir.startsWith("/")) {
            throw new RuntimeException("absoluteOutDir parameter is invalid. Please set start /");
        }
        generateCode(absoluteOutDir);
    }

    private void generateCode(String path) {
        CodeGenerator codeGenerator;
        switch (this.lang) {
            case Typescript:
                codeGenerator = new TypescriptHttpClientGenerator();
                break;
            default:
                throw new CodeGenException("Not support code generation language");
        }
        codeGenerator.run(path, this.hertsServices);
    }
}
