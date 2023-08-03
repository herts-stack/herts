package org.hertsstack.codegen;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

class TypescriptCodeGenMain extends TypescriptBase {
    private final String serviceName;
    private final TypescriptFileName fileName;

    public TypescriptCodeGenMain(String serviceName, TypeResolver typeResolver) {
        super(typeResolver);
        this.serviceName = serviceName;
        this.fileName = new TypescriptFileName(this.serviceName);
    }

    public void genMain(Method[] methods) {

        System.out.println("Typescript file name = " + this.fileName.getMainFileName());
        System.out.println("Generating...");

        StringBuilder codeStr = new StringBuilder();
        codeStr.append(CodeGenUtil.GEN_COMMENT);
        codeStr.append("\nimport {RequestHeaders} from './" + this.fileName.getStructureTsFileName() + "'");
        codeStr.append("\nimport {" + this.serviceName + "Client} from './" + this.fileName.getClientTsFileName() + "'");
        for (Method method : methods) {
            String capitalizeMethodName = CodeGenUtil.capitalizeFirstLetter(method.getName());

            codeStr.append("\nimport {" + capitalizeMethodName + "Request} from './" + this.fileName.getRequestTsFileName() + "'");
            codeStr.append("\nimport {" + capitalizeMethodName + "Response} from './" + this.fileName.getResponseTsFileName() + "'");
        }

        codeStr.append("\n\nasync function main() {");
        codeStr.append("\n\tconst client = new " + serviceName + "Client('http://localhost:9999');");
        codeStr.append("\n\tconst header: RequestHeaders = {'Content-Type': 'application/json'};");
        for (Method method : methods) {
            String capitalizeMethodName = CodeGenUtil.capitalizeFirstLetter(method.getName());

            codeStr.append("\n\n");
            if (method.getParameterTypes().length == 0) {
                codeStr.append("\tconst res" + capitalizeMethodName + " = await client." + method.getName() + "(header);");
            } else {
                List<String> params = new ArrayList<>();
                for (Class<?> paramType : method.getParameterTypes()) {
                    params.add("null");
                }
                codeStr.append("\tconst body = " + capitalizeMethodName + "Request.createRequest(" + String.join(", ", params) + ")");
                codeStr.append("\n\tconst res" + capitalizeMethodName + " = await client." + method.getName() + "(header, body);");
            }
            codeStr.append("\n\tconsole.log(res" + capitalizeMethodName + ");");
        }

        codeStr.append("\n}");
        codeStr.append("\n\nmain();");

        CodeGenUtil.writeFile(this.fileName.getMainFileName(), codeStr.toString());
    }
}
