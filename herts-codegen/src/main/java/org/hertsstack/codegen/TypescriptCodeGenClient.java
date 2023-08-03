package org.hertsstack.codegen;

import java.lang.reflect.Method;

class TypescriptCodeGenClient extends TypescriptBase {
    private final String serviceName;
    private final TypescriptFileName fileName;

    public TypescriptCodeGenClient(String serviceName, TypeResolver typeResolver) {
        super(typeResolver);
        this.serviceName = serviceName;
        this.fileName = new TypescriptFileName(this.serviceName);
    }

    public void genClient(Method[] methods) {
        System.out.println("Typescript file name = " + this.fileName.getClientFileName());
        System.out.println("Generating...");

        StringBuilder codeStr = new StringBuilder();
        codeStr.append(CodeGenUtil.GEN_COMMENT);

        // Import definition
        codeStr.append("\nimport axios, {AxiosError} from 'axios'");
        codeStr.append("\nimport {RequestHeaders} from './" + this.fileName.getStructureTsFileName() + "'");
        for (Method method : methods) {
            String capitalizeMethodName = CodeGenUtil.capitalizeFirstLetter(method.getName());

            codeStr.append("\nimport {" + capitalizeMethodName + "Request} from './" +
                    this.fileName.getRequestTsFileName() + "';");

            codeStr.append("\nimport {" + capitalizeMethodName + "Response} from './" +
                    this.fileName.getResponseTsFileName() + "';");
        }

        // Client Class definition
        codeStr.append("\n\nexport class " + this.serviceName + "Client {");
        codeStr.append("\n\t/**\n" +
                "\t * API endpoint information with protocol schema.\n" +
                "\t * @param apiSchema http|https://hoge.com\n" +
                "\t */");
        codeStr.append("\n\tconstructor(private apiSchema: string) {}");

        // Method definition
        for (Method method : methods) {
            String capitalizeMethodName = CodeGenUtil.capitalizeFirstLetter(method.getName());

            JavaType javaType = JavaType.findType(method.getReturnType().getName());
            String typescriptType = getTypescriptTypeStr(javaType, method.getReturnType());
            if (this.typeResolver.findType(typescriptType) == null) {
                codeStr.insert(CodeGenUtil.GEN_COMMENT.length(), "\n\nimport {" + typescriptType + "} from './" + this.fileName.getStructureTsFileName() + "'");
            }

            if (method.getParameterTypes().length == 0) {
                codeStr.append("\n\tpublic async " + method.getName() + "(headers: RequestHeaders): Promise<" + typescriptType + " | null> {");
                codeStr.append("\n\t\tconst body = null;");
            } else {
                codeStr.append("\n\tpublic async " + method.getName() + "(headers: RequestHeaders, body: " + capitalizeMethodName + "Request): Promise<" + typescriptType +" | null> {");
            }
            codeStr.append("\n\t\treturn await axios.post<" + capitalizeMethodName + "Response>(");
            codeStr.append("\n\t\t\t`${(this.apiSchema)}/api/" + this.serviceName + "/" + method.getName() + "`,");
            codeStr.append("\n\t\t\tbody,");
            codeStr.append("\n\t\t\t{");
            codeStr.append("\n\t\t\t\theaders: headers,");
            codeStr.append("\n\t\t\t})");
            codeStr.append("\n\t\t\t.then(res => {");
            codeStr.append("\n\t\t\t\tif (res.data.payload === undefined || res.data.payload === null) {");
            codeStr.append("\n\t\t\t\t\treturn null;");
            codeStr.append("\n\t\t\t\t}");
            codeStr.append("\n\t\t\t\treturn res.data.payload.value;");
            codeStr.append("\n\t\t\t})");
            codeStr.append("\n\t\t\t.catch((e: AxiosError<any>) => {");
            codeStr.append("\n\t\t\t\tthrow e;");
            codeStr.append("\n\t\t\t})");
            codeStr.append("\n\t}");
            codeStr.append("\n");
        }
        codeStr.append("\n\n}");

        CodeGenUtil.writeFile(this.fileName.getClientFileName(), codeStr.toString());
    }
}
