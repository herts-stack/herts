package org.hertsstack.codegen;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

class TypescriptCodeGenResponse extends TypescriptBase {
    private final String serviceName;
    private final TypescriptFileName fileName;
    private final String outDir;

    public TypescriptCodeGenResponse(String serviceName, TypeResolver typeResolver, String outDir) {
        super(typeResolver);
        this.serviceName = serviceName;
        this.fileName = new TypescriptFileName(this.serviceName);
        this.outDir = outDir;
    }

    public void generate(Method[] methods) {
        System.out.println("Typescript file name = " + this.fileName.getResponseFileName());
        System.out.println("Generating...");

        String templateFileName = this.fileName.getResponseTsFileName() + ".vm";
        String template = TypescriptDefault.STRUCTURE_RES_MODEL_CLASS;
        CodeGenUtil.writeFile(CodeGenUtil.getFullPath(this.outDir, templateFileName), template);

        List<TypescriptDefault.ImportInfo> importInfos = new ArrayList<>();
        List<TypescriptDefault.ResClassInfo.Response> resClassInfos = new ArrayList<>();
        List<TypescriptDefault.ResClassInfo.Payload> payloadClassInfos = new ArrayList<>();

        for (Method method : methods) {
            String capitalizeMethodName = CodeGenUtil.capitalizeFirstLetter(method.getName());
            String payloadClassName = capitalizeMethodName + "Payload";

            JavaType javaType = JavaType.findType(method.getReturnType().getName());
            String defaultTypescriptType = getTypescriptTypeStr(javaType, method.getReturnType());

            BiFunction<JavaType, Class<?>, String> generateTypescriptType = this::getTypescriptTypeStr;
            String typescriptType = CodeGenUtil.getGeneticTypes(javaType, defaultTypescriptType,
                    new Type[]{method.getGenericReturnType()}, generateTypescriptType);

            addImportSentenceIfNotExist(defaultTypescriptType, importInfos, this.fileName.getStructureTsFileName());

            resClassInfos.add(new TypescriptDefault.ResClassInfo.Response(
                    capitalizeMethodName + CodeGenUtil.getResponseName(),
                    payloadClassName
            ));

            payloadClassInfos.add(new TypescriptDefault.ResClassInfo.Payload(
                    payloadClassName,
                    typescriptType
            ));
        }

        TypescriptDefault.ResClassInfo resClassInfo = new TypescriptDefault.ResClassInfo();
        resClassInfo.setImportInfos(importInfos);
        resClassInfo.setResClassInfos(resClassInfos);
        resClassInfo.setPayloadClassInfos(payloadClassInfos);

        try {
            StringWriter sw = new StringWriter();
            VelocityContext context = resClassInfo.getVelocityContext();
            Template tem = Velocity.getTemplate(templateFileName);
            tem.merge(context, sw);

            CodeGenUtil.writeFile(CodeGenUtil.getFullPath(this.outDir, this.fileName.getResponseFileName()), sw.toString());
        } catch (Exception ex) {
            System.out.println("Failed to create " + this.fileName.getResponseFileName() + " file");
            ex.printStackTrace();
        } finally {
            try {
                Files.delete(Path.of(CodeGenUtil.getFullPath(this.outDir, templateFileName)));
            } catch (Exception ignore) {
            }
        }
    }
}
