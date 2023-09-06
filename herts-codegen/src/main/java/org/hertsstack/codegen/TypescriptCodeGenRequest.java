package org.hertsstack.codegen;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.hertsstack.core.annotation.HertsParam;

import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

class TypescriptCodeGenRequest extends TypescriptBase {
    private final String serviceName;
    private final TypescriptFileName fileName;
    private final String outDir;

    public TypescriptCodeGenRequest(String serviceName, TypeResolver typeResolver, String outDir) {
        super(typeResolver);
        this.serviceName = serviceName;
        this.fileName = new TypescriptFileName(this.serviceName);
        this.outDir = outDir;
    }

    public void generate(Method[] methods) {
        System.out.println("Typescript file name = " + this.fileName.getRequestFileName());
        System.out.println("Generating...");

        String templateFileName = this.fileName.getRequestTsFileName() + ".vm";
        String template = TypescriptDefault.STRUCTURE_REQ_MODEL_CLASS;
        CodeGenUtil.writeFile(CodeGenUtil.getFullPath(this.outDir, templateFileName), template);

        List<TypescriptDefault.ReqClassInfo.Request> reqClassInfos = new ArrayList<>();
        List<String> payloadNames = new ArrayList<>();
        List<TypescriptDefault.ImportInfo> importInfos = new ArrayList<>();

        for (Method method : methods) {
            String capitalizeMethodName = CodeGenUtil.capitalizeFirstLetter(method.getName());
            String payloadClassName = capitalizeMethodName + "Payload";
            Class<?>[] parameterTypes = method.getParameterTypes();
            boolean hasParameter = parameterTypes.length > 0;

            List<TypescriptDefault.ReqClassInfo.Request.Arg> args = new ArrayList<>();
            if (hasParameter) {
                for (int i = 0; i < parameterTypes.length; i++) {

                    // Find actual arg name
                    String actualArgName = null;
                    if (method.getParameters()[i].getAnnotations() != null && method.getParameters()[i].getAnnotations().length > 0) {
                        Annotation annotation = method.getParameters()[i].getAnnotations()[0];
                        if (annotation instanceof HertsParam) {
                            HertsParam parameterNameAnnotation = (HertsParam) annotation;
                            actualArgName = parameterNameAnnotation.value();
                        }
                    } else {
                        actualArgName = "arg" + i;
                    }

                    // Find typescript type
                    Class<?> parameterTypeClass = parameterTypes[i];
                    JavaType javaType = JavaType.findType(parameterTypeClass.getName());
                    String defaultTypescriptType = getTypescriptTypeStr(javaType, parameterTypeClass);

                    BiFunction<JavaType, Class<?>, String> generateTypescriptType = this::getTypescriptTypeStr;
                    String typescriptType = CodeGenUtil.getGeneticTypes(javaType, defaultTypescriptType,
                            method.getGenericParameterTypes(), generateTypescriptType);

                    args.add(new TypescriptDefault.ReqClassInfo.Request.Arg(
                            "arg" + i,
                            actualArgName,
                            typescriptType,
                            "payload" + i
                    ));

                    addImportSentenceIfNotExist(defaultTypescriptType, importInfos, this.fileName.getStructureTsFileName());
                }
            }

            payloadNames.add(payloadClassName);
            reqClassInfos.add(new TypescriptDefault.ReqClassInfo.Request(
                    capitalizeMethodName + CodeGenUtil.getRequestName(),
                    payloadClassName,
                    args
            ));
        }

        TypescriptDefault.ReqClassInfo reqClassInfo = new TypescriptDefault.ReqClassInfo();
        reqClassInfo.setReqClassInfos(reqClassInfos);
        reqClassInfo.setPayloadNames(payloadNames);
        reqClassInfo.setImportInfos(importInfos);

        try {
            StringWriter sw = new StringWriter();
            VelocityContext context = reqClassInfo.getVelocityContext();
            Template tem = Velocity.getTemplate(templateFileName);
            tem.merge(context, sw);

            CodeGenUtil.writeFile(CodeGenUtil.getFullPath(this.outDir, this.fileName.getRequestFileName()), sw.toString());
        } catch (Exception ex) {
            System.out.println("Failed to create " + this.fileName.getRequestFileName() + " file");
            ex.printStackTrace();
        } finally {
            try {
                Files.delete(Path.of(CodeGenUtil.getFullPath(this.outDir, templateFileName)));
            } catch (Exception ignore) {
            }
        }
    }
}
