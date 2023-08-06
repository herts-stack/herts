package org.hertsstack.codegen;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

class TypescriptCodeGenClient extends TypescriptBase {
    private final String serviceName;
    private final TypescriptFileName fileName;
    private final String outDir;

    public TypescriptCodeGenClient(String serviceName, TypeResolver typeResolver, String outDir) {
        super(typeResolver);
        this.serviceName = serviceName;
        this.outDir = outDir;
        this.fileName = new TypescriptFileName(this.serviceName);
    }

    public void genClient(Method[] methods) {
        System.out.println("Typescript file name = " + this.fileName.getClientFileName());
        System.out.println("Generating...");

        String templateFileName = this.fileName.getClientTsFileName() + ".vm";
        String template = TypescriptDefault.CLIENT_CLASS;
        CodeGenUtil.writeFile(CodeGenUtil.getFullPath(this.outDir, templateFileName), template);

        List<String> reqModelNames = new ArrayList<>();
        List<String> resModelNames = new ArrayList<>();
        List<String> customModelNames = new ArrayList<>();
        List<TypescriptDefault.MethodInfo> methodInfos = new ArrayList<>();
        for (Method method : methods) {
            String capitalizeMethodName = CodeGenUtil.capitalizeFirstLetter(method.getName());
            String reqClassName = capitalizeMethodName + "Request";
            String resClassName = capitalizeMethodName + "Response";

            reqModelNames.add(reqClassName);
            resModelNames.add(resClassName);

            JavaType javaType = JavaType.findType(method.getReturnType().getName());
            String typescriptType = getTypescriptTypeStr(javaType, method.getReturnType());
            if (this.typeResolver.findType(typescriptType) == null) {
                customModelNames.add(typescriptType);
            }

            methodInfos.add(new TypescriptDefault.MethodInfo(
                    method.getName(),
                    method.getParameterTypes().length > 0,
                    reqClassName,
                    resClassName,
                    typescriptType
            ));
        }

        TypescriptDefault.ClientClassInfo clientClassInfo = new TypescriptDefault.ClientClassInfo(
                "$",
                this.serviceName,
                this.serviceName + "Client",
                reqModelNames,
                this.fileName.getRequestTsFileName(),
                resModelNames,
                this.fileName.getResponseTsFileName(),
                customModelNames,
                this.fileName.getStructureTsFileName(),
                methodInfos
        );

        try {
            StringWriter sw = new StringWriter();
            VelocityContext context = clientClassInfo.getVelocityContext();
            Template tem = Velocity.getTemplate(templateFileName);
            tem.merge(context, sw);

            CodeGenUtil.writeFile(CodeGenUtil.getFullPath(this.outDir, this.fileName.getClientFileName()), sw.toString());
        } catch (Exception ex) {
            System.out.println("Failed to create " + this.fileName.getClientFileName() + " file");
            ex.printStackTrace();
        } finally {
            try {
                Files.delete(Path.of(CodeGenUtil.getFullPath(this.outDir, templateFileName)));
            } catch (Exception ignore) {
            }
        }
    }
}
