package org.hertsstack.codegen;

import java.lang.reflect.Method;
import java.util.List;

class TypescriptHttpClientGenerator implements CodeGenerator {
    private final TypeResolver typeResolver;

    public TypescriptHttpClientGenerator() {
        this.typeResolver = new TypeResolverImpl();
    }

    @Override
    public void run(String outDir, List<Class<?>> hertsServices) {
        TypescriptDefault.initVelocity(outDir);
        StringBuilder commonStructureCode = new StringBuilder();

        for (Class<?> hertsService : hertsServices) {
            String serviceName = hertsService.getSimpleName();
            Method[] methods = hertsService.getDeclaredMethods();

            TypescriptCodeGenStructure typescriptCodeGenStructure = new TypescriptCodeGenStructure(serviceName, this.typeResolver, outDir);
            TypescriptCodeGenClient typescriptCodeGenClient = new TypescriptCodeGenClient(serviceName, this.typeResolver, outDir);

            typescriptCodeGenStructure.genStructureModel(methods, commonStructureCode);
            typescriptCodeGenStructure.genRequestModel(methods);
            typescriptCodeGenStructure.genResponseModel(methods);
            typescriptCodeGenClient.genClient(methods);
        }

        CodeGenUtil.writeFile(CodeGenUtil.getFullPath(outDir, TypescriptFileName.tsStructureFileName), commonStructureCode.toString());
    }
}
