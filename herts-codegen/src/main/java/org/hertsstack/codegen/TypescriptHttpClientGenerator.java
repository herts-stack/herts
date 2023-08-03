package org.hertsstack.codegen;

import java.lang.reflect.Method;
import java.util.List;

class TypescriptHttpClientGenerator implements CodeGenerator {
    private final TypeResolver typeResolver;

    public TypescriptHttpClientGenerator() {
        this.typeResolver = new TypeResolverImpl();
    }

    @Override
    public void run(String path, List<Class<?>> hertsServices) {
        StringBuilder commonStructureCode = new StringBuilder();

        for (Class<?> hertsService : hertsServices) {
            String serviceName = hertsService.getSimpleName();
            Method[] methods = hertsService.getDeclaredMethods();

            TypescriptCodeGenStructure typescriptCodeGenStructure = new TypescriptCodeGenStructure(serviceName, this.typeResolver);
            TypescriptCodeGenClient typescriptCodeGenClient = new TypescriptCodeGenClient(serviceName, this.typeResolver);
            TypescriptCodeGenMain typescriptCodeGenMain = new TypescriptCodeGenMain(serviceName, this.typeResolver);

            typescriptCodeGenStructure.genStructureModel(methods, commonStructureCode);
            typescriptCodeGenStructure.genRequestModel(methods);
            typescriptCodeGenStructure.genResponseModel(methods);
            typescriptCodeGenClient.genClient(methods);
            typescriptCodeGenMain.genMain(methods);
        }

        CodeGenUtil.writeFile(TypescriptFileName.tsStructureFileName, commonStructureCode.toString());
    }
}
