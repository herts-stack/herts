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

class TypescriptCodeGenMain extends TypescriptBase {
    private final String serviceName;
    private final TypescriptFileName fileName;
    private final String outDir;

    public TypescriptCodeGenMain(String serviceName, TypeResolver typeResolver, String outDir) {
        super(typeResolver);
        this.serviceName = serviceName;
        this.fileName = new TypescriptFileName(this.serviceName);
        this.outDir = outDir;
    }

    public void genMain(Method[] methods) {
        System.out.println("Typescript file name = " + this.fileName.getMainFileName());
        System.out.println("Generating...");

        String templateFileName = this.fileName.getRequestTsFileName() + ".vm";
        String template = TypescriptDefault.EXAMPLE_USAGE;
        CodeGenUtil.writeFile(CodeGenUtil.getFullPath(this.outDir, templateFileName), template);

        List<TypescriptDefault.ImportInfo> importInfos = new ArrayList<>();
        List<TypescriptDefault.ExampleUsage.ClientInfo.Method> methodInfos = new ArrayList<>();

        int idx = 1;
        for (Method method : methods) {
            List<String> factoryParamList = new ArrayList<>();
            for (Class<?> p : method.getParameterTypes()) {
                factoryParamList.add("null");

                JavaType javaType = JavaType.findType(p.getName());
                String defaultTypescriptType = getTypescriptTypeStr(javaType, p);

                if (this.typeResolver.findType(defaultTypescriptType) == null) {
                    importInfos.add(new TypescriptDefault.ImportInfo(
                            defaultTypescriptType,
                            this.fileName.getStructureTsFileName()
                    ));
                }
            }

            String capitalizeMethodName = CodeGenUtil.capitalizeFirstLetter(method.getName());
            String reqClassName = capitalizeMethodName + CodeGenUtil.getRequestName();
            String reqClassNameWithFunc = reqClassName + ".createRequest";
            String reqValName = "req" + idx;
            String resValName = "res" + idx;
            methodInfos.add(new TypescriptDefault.ExampleUsage.ClientInfo.Method(
                    "." + method.getName(),
                    reqClassNameWithFunc,
                    reqValName,
                    resValName,
                    String.join(", ", factoryParamList)
            ));
            importInfos.add(new TypescriptDefault.ImportInfo(
                    reqClassName,
                    this.fileName.getRequestTsFileName()
            ));
            idx++;
        }

        String clientName = this.serviceName + "Client";
        importInfos.add(new TypescriptDefault.ImportInfo(
                clientName,
                this.fileName.getClientTsFileName()
        ));
        TypescriptDefault.ExampleUsage usage = new TypescriptDefault.ExampleUsage(
                importInfos,
                new TypescriptDefault.ExampleUsage.ClientInfo(clientName, methodInfos)
        );

        try {
            StringWriter sw = new StringWriter();
            VelocityContext context = usage.getVelocityContext();
            Template tem = Velocity.getTemplate(templateFileName);
            tem.merge(context, sw);

            CodeGenUtil.writeFile(CodeGenUtil.getFullPath(this.outDir, this.fileName.getMainFileName()), sw.toString());
        } catch (Exception ex) {
            System.out.println("Failed to create " + this.fileName.getMainFileName() + " file");
            ex.printStackTrace();
        } finally {
            try {
                Files.delete(Path.of(CodeGenUtil.getFullPath(this.outDir, templateFileName)));
            } catch (Exception ignore) {
            }
        }
    }
}
