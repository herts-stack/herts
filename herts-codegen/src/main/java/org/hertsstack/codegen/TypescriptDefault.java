package org.hertsstack.codegen;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.util.List;

public class TypescriptDefault {

    public static void initVelocity(String outDir) {
        if (outDir != null && !outDir.isEmpty()) {
            Velocity.setProperty("file.resource.loader.path", outDir);
        }
        Velocity.init();
    }

    /**
     * Response class information
     */
    public static class ResClassInfo {
        private List<ImportInfo> importInfos;
        private List<Response> resClassInfos;
        private List<Payload> payloadClassInfos;

        public VelocityContext getVelocityContext() {
            VelocityContext context = new VelocityContext();
            context.put("importInfos", this.importInfos);
            context.put("resClassInfos", this.resClassInfos);
            context.put("payloadClassInfos", this.payloadClassInfos);
            return context;
        }

        public List<ImportInfo> getImportInfos() {
            return importInfos;
        }

        public void setImportInfos(List<ImportInfo> importInfos) {
            this.importInfos = importInfos;
        }

        public List<Response> getResClassInfos() {
            return resClassInfos;
        }

        public void setResClassInfos(List<Response> resClassInfos) {
            this.resClassInfos = resClassInfos;
        }

        public List<Payload> getPayloadClassInfos() {
            return payloadClassInfos;
        }

        public void setPayloadClassInfos(List<Payload> payloadClassInfos) {
            this.payloadClassInfos = payloadClassInfos;
        }

        public static class Response {
            private final String name;
            private final String payloadClassName;

            public Response(String name, String payloadClassName) {
                this.name = name;
                this.payloadClassName = payloadClassName;
            }

            public String getName() {
                return name;
            }

            public String getPayloadClassName() {
                return payloadClassName;
            }
        }

        public static class Payload {
            private final String name;
            private final String valueType;

            public Payload(String name, String valueType) {
                this.name = name;
                this.valueType = valueType;
            }

            public String getName() {
                return name;
            }

            public String getValueType() {
                return valueType;
            }
        }
    }

    /**
     * Request class information
     */
    public static class ReqClassInfo {
        private List<Request> reqClassInfos;
        private List<String> payloadNames;
        private List<ImportInfo> importInfos;

        public VelocityContext getVelocityContext() {
            VelocityContext context = new VelocityContext();
            context.put("importInfos", this.importInfos);
            context.put("reqClassInfos", this.reqClassInfos);
            context.put("payloadNames", this.payloadNames);
            return context;
        }

        public void setReqClassInfos(List<Request> reqClassInfos) {
            this.reqClassInfos = reqClassInfos;
        }

        public void setPayloadNames(List<String> payloadNames) {
            this.payloadNames = payloadNames;
        }

        public List<Request> getReqClassInfos() {
            return reqClassInfos;
        }

        public List<String> getPayloadNames() {
            return payloadNames;
        }

        public List<ImportInfo> getImportInfos() {
            return importInfos;
        }

        public void setImportInfos(List<ImportInfo> importInfos) {
            this.importInfos = importInfos;
        }

        public static class Request {
            private final String name;
            private final String payloadName;
            private final List<Arg> args;

            public Request(String name, String payloadName, List<Arg> args) {
                this.name = name;
                this.payloadName = payloadName;
                this.args = args;
            }

            public String getName() {
                return name;
            }

            public String getPayloadName() {
                return payloadName;
            }

            public List<Arg> getArgs() {
                return args;
            }

            public static class Arg {
                private final String keyName;
                private final String typeName;
                private final String payloadValName;
                private final String classPkg;

                public Arg(String keyName, String typeName, String payloadValName, String classPkg) {
                    this.keyName = keyName;
                    this.typeName = typeName;
                    this.payloadValName = payloadValName;
                    this.classPkg = classPkg;
                }

                public String getKeyName() {
                    return keyName;
                }

                public String getTypeName() {
                    return typeName;
                }

                public String getPayloadValName() {
                    return payloadValName;
                }

                public String getClassPkg() {
                    return classPkg;
                }
            }
        }
    }

    /**
     * Structure class information
     */
    public static class StructureClassInfo {
        private final List<Structure> classInfos;

        public StructureClassInfo(List<Structure> classInfos) {
            this.classInfos = classInfos;
        }

        public List<Structure> getClassInfos() {
            return classInfos;
        }

        public VelocityContext getVelocityContext(boolean hasHeader) {
            VelocityContext context = new VelocityContext();
            context.put("classInfos", this.classInfos);
            context.put("hasHeaderModel", hasHeader);
            return context;
        }

        public static class Structure {
            private String name;
            private List<ConstructorInfo> filedInfos;

            public Structure() {}

            public Structure(String name, List<ConstructorInfo> filedInfos) {
                this.name = name;
                this.filedInfos = filedInfos;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setConstructors(List<ConstructorInfo> filedInfos) {
                this.filedInfos = filedInfos;
            }

            public String getName() {
                return name;
            }

            public List<ConstructorInfo> getFiledInfos() {
                return filedInfos;
            }
        }
    }

    /**
     * Constructor information
     */
    public static class ConstructorInfo {
        private final String keyName;
        private final String typeName;

        public ConstructorInfo(String keyName, String typeName) {
            this.keyName = keyName;
            this.typeName = typeName;
        }

        public String getKeyName() {
            return keyName;
        }

        public String getTypeName() {
            return typeName;
        }
    }

    /**
     * Client class information
     */
    public static class ClientClassInfo {
        private final String dollarSign;
        private final String serviceName;
        private final String clientClassName;
        private final List<String> reqModelNames;
        private final String reqModelFileName;
        private final List<String> resModelNames;
        private final String resModelFileName;
        private final List<String> customModelNames;
        private final String customModelFileName;
        private final List<MethodInfo> methodInfos;

        public ClientClassInfo(String dollarSign, String serviceName, String clientClassName,
                               List<String> reqModelNames, String reqModelFileName, List<String> resModelNames,
                               String resModelFileName, List<String> costomModelNames, String customModelFileName,
                               List<MethodInfo> methodInfos) {
            this.dollarSign = dollarSign;
            this.serviceName = serviceName;
            this.clientClassName = clientClassName;
            this.reqModelNames = reqModelNames;
            this.reqModelFileName = reqModelFileName;
            this.resModelNames = resModelNames;
            this.resModelFileName = resModelFileName;
            this.customModelNames = costomModelNames;
            this.customModelFileName = customModelFileName;
            this.methodInfos = methodInfos;
        }

        public String getDollarSign() {
            return dollarSign;
        }

        public String getServiceName() {
            return serviceName;
        }

        public String getClientClassName() {
            return clientClassName;
        }

        public List<String> getReqModelNames() {
            return reqModelNames;
        }

        public String getReqModelFileName() {
            return reqModelFileName;
        }

        public List<String> getResModelNames() {
            return resModelNames;
        }

        public String getResModelFileName() {
            return resModelFileName;
        }

        public List<String> getCustomModelNames() {
            return customModelNames;
        }

        public String getCustomModelFileName() {
            return customModelFileName;
        }

        public List<MethodInfo> getMethodInfos() {
            return methodInfos;
        }

        public VelocityContext getVelocityContext() {
            VelocityContext context = new VelocityContext();
            context.put("dollarSign", this.dollarSign);
            context.put("serviceName", this.serviceName);
            context.put("reqModelNames", this.reqModelNames);
            context.put("reqModelFileName", this.reqModelFileName);
            context.put("resModelNames", this.resModelNames);
            context.put("resModelFileName", this.resModelFileName);
            context.put("customModelNames", this.customModelNames);
            context.put("customModelFileName", this.customModelFileName);
            context.put("clientClassName", this.clientClassName);
            context.put("methodInfos", this.methodInfos);
            return context;
        }
    }

    /**
     * Method information
     */
    public static class MethodInfo {
        private final String name;
        private final boolean hasBody;
        private final String requestClassName;
        private final String responseClassName;
        private final String returnClassName;

        public MethodInfo(String name, boolean hasBody, String requestClassName,
                          String responseClassName, String returnClassName) {
            this.name = name;
            this.hasBody = hasBody;
            this.requestClassName = requestClassName;
            this.responseClassName = responseClassName;
            this.returnClassName = returnClassName;
        }

        public String getName() {
            return name;
        }

        public boolean isHasBody() {
            return hasBody;
        }

        public String getRequestClassName() {
            return requestClassName;
        }

        public String getResponseClassName() {
            return responseClassName;
        }

        public String getReturnClassName() {
            return returnClassName;
        }
    }

    /**
     * Import sentence information
     */
    public static class ImportInfo {
        private final String name;
        private final String filePath;

        public ImportInfo(String name, String filePath) {
            this.name = name;
            this.filePath = filePath;
        }

        public String getName() {
            return name;
        }

        public String getFilePath() {
            return filePath;
        }
    }

    /**
     * Client typescript class template.
     */
    public static final String CLIENT_CLASS =
            """
            // Don't edit this file because this file is generated by herts codegen.
            import axios, {AxiosError} from 'axios'
            import {RequestHeaders} from './$customModelFileName'
                        
            #foreach($name in $reqModelNames)
            import {$name} from './$reqModelFileName'
            #end
            
            #foreach($name in $resModelNames)
            import {$name} from './$resModelFileName'
            #end
            
            #foreach($name in $customModelNames)
            import {$name} from './$customModelFileName'
            #end

            export class $clientClassName {
                        
                /**
                 * API endpoint information with protocol schema.
                 * @param apiSchema http|https://hoge.com
                 */
                 constructor(private apiSchema: string) {}
                 
                 #foreach($methodInfo in $methodInfos)
                 #if($methodInfo.hasBody)
                 public async $methodInfo.name ( headers: RequestHeaders, body: $methodInfo.requestClassName ): Promise<$methodInfo.returnClassName | null> {
                 #else
                 public async $methodInfo.name ( headers: RequestHeaders ): Promise<$methodInfo.returnClassName | null> {
                    const body = null;
                 #end
                    return await axios.post<$methodInfo.responseClassName>(
                        `$dollarSign{(this.apiSchema)}/api/$serviceName/$methodInfo.name`,
                         body,
                         {
                            headers: headers,
                         })
                         .then(res => {
                            if (res.data.payload === undefined || res.data.payload === null) {
                                return null;
                            }
                            return res.data.payload.value;
                         })
                         .catch((e: AxiosError<any>) => {
                            throw e;
                         })
                    }
                 #end
            }
            """;

    /**
     * Structure typescript class template.
     */
    public static final String STRUCTURE_CUSTOM_MODEL_CLASS =
            """
            #if(!$hasHeaderModel)
            // Don't edit this file because this file is generated by herts codegen.
            export type RequestHeaders = {
            	[x: string]: string | number | boolean;
            }
            #end
            
            #foreach($classInfo in $classInfos)
            export class $classInfo.name {
                constructor(
                #foreach($filed in $classInfo.filedInfos)
                    $filed.keyName : $filed.typeName,
                #end
                ) {
                #foreach($filed in $classInfo.filedInfos)
                    this.$filed.keyName = $filed.keyName
                #end
                }
                #foreach($filed in $classInfo.filedInfos)
                $filed.keyName : $filed.typeName
                #end
            }
            #end
            """;

    /**
     * Request model typescript class template.
     */
    public static final String STRUCTURE_REQ_MODEL_CLASS =
            """
            // Don't edit this file because this file is generated by herts codegen.
            #foreach($importInfo in $importInfos)
            import {$importInfo.name} from './$importInfo.filePath'
            #end

            #foreach($classInfo in $reqClassInfos)
            export class $classInfo.name {
                private constructor(payloads: Array<$classInfo.payloadName>) {
                    this.payloads = payloads;
                }
                payloads: Array<$classInfo.payloadName>;
                public static createRequest(
                #foreach($arg in $classInfo.args)
                    $arg.keyName : $arg.typeName,
                #end
                ) {
                    const payloads = new Array<$classInfo.payloadName>();
                    #foreach($arg in $classInfo.args)
                    const $arg.payloadValName = new $classInfo.payloadName ('$arg.keyName', $arg.keyName, '$arg.classPkg');
                    payloads.push($arg.payloadValName);
                    #end
                    return new $classInfo.name (payloads);
                };
            }
            #end
            
            #foreach($payloadName in $payloadNames)
            export class $payloadName {
                constructor(keyName: string, value: any, classInfo: string) {
                    this.keyName = keyName;
                    this.value = value;
                    this.classInfo = classInfo;
                }
                private keyName: string;
                private value: any;
                private classInfo: string;
            }
            #end
            """;

    /**
     * Response model typescript class template.
     */
    public static final String STRUCTURE_RES_MODEL_CLASS =
            """
            // Don't edit this file because this file is generated by herts codegen.
            #foreach($importInfo in $importInfos)
            import {$importInfo.name} from './$importInfo.filePath'
            #end
            
            #foreach($classInfo in $resClassInfos)
            export class $classInfo.name {
                constructor() {
                    this.payload = new $classInfo.payloadClassName ();
                }
                payload: $classInfo.payloadClassName;
            }
            #end
            
            #foreach($classInfo in $payloadClassInfos)
            export class $classInfo.name {
                constructor() {
                    this.keyName = '';
                    this.value = null;
                    this.classInfo = '';
                }
                private keyName: string;
                value: $classInfo.valueType;
                private classInfo: string;
            }
            #end
            """;

    /**
     * Main typescript class template.
     */
    public static final String MAIN_CLASS =
            """
            """;
}