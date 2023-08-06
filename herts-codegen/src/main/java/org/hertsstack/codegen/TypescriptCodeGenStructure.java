package org.hertsstack.codegen;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

class TypescriptCodeGenStructure extends TypescriptBase {
    private final String serviceName;
    private final TypescriptFileName fileName;
    private final String outDir;

    public TypescriptCodeGenStructure(String serviceName, TypeResolver typeResolver, String outDir) {
        super(typeResolver);
        this.serviceName = serviceName;
        this.fileName = new TypescriptFileName(this.serviceName);
        this.outDir = outDir;
    }

    private void genNestedModel(List<TypescriptDefault.StructureClassInfo.Structure> structures, Class<?> targetClass)
            throws ClassNotFoundException {

        if (this.cacheGenCode.getGeneratedPackageNames().contains(targetClass.getSimpleName())) {
            return;
        }

        List<String> nestedModelPackages = new ArrayList<>();
        Field[] allFields = targetClass.getDeclaredFields();

        TypescriptDefault.StructureClassInfo.Structure structure = new TypescriptDefault.StructureClassInfo.Structure();
        structure.setName(targetClass.getSimpleName());

        List<TypescriptDefault.ConstructorInfo> constructorInfos = new ArrayList<>();
        for (Field field : allFields) {
            String propertyName = field.getName();
            String propertyType = field.getType().getName();

            if (propertyType.contains("$")) {
                Class<?> nestedClass = Class.forName(field.getType().getName());
                if (CodeGenUtil.isCustomModelClass(nestedClass)) {
                    nestedModelPackages.add(field.getType().getName());
                }

                String[] classSplit = propertyType.split("\\$");
                propertyType = classSplit[classSplit.length-1];

                constructorInfos.add(new TypescriptDefault.ConstructorInfo(propertyName, propertyType));
            } else {
                JavaType type = JavaType.findType(propertyType);
                TypescriptType typescriptType = this.typeResolver.convertType(type);

                constructorInfos.add(new TypescriptDefault.ConstructorInfo(propertyName, typescriptType.getData()));
            }
        }
        structure.setConstructors(constructorInfos);
        structures.add(structure);

        this.cacheGenCode.addGeneratedPackageNames(targetClass.getSimpleName());

        // Recursive generate model
        for (String nestedModelPkg : nestedModelPackages) {
            Class<?> nestedClass = Class.forName(nestedModelPkg);
            genNestedModel(structures, nestedClass);
        }
    }

    private void senCustomPkgModelStr(Method[] methods, List<TypescriptDefault.StructureClassInfo.Structure> structures) {
        for (Method method : methods) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            for (Class<?> paramTypeClass : parameterTypes) {
                if (CodeGenUtil.isCustomModelClass(paramTypeClass)) {
                    try {
                        genNestedModel(structures, paramTypeClass);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static final String REQUEST_HEADER_CLASS_NAME = "RequestHeaders";
    public void genStructureModel(Method[] methods, StringBuilder codeStr) {
        System.out.println("Typescript file name = " + this.fileName.getStructureFileName());
        System.out.println("Generating...");

        String templateFileName = this.fileName.getStructureTsFileName() + ".vm";
        String template = TypescriptDefault.STRUCTURE_CUSTOM_MODEL_CLASS;
        CodeGenUtil.writeFile(CodeGenUtil.getFullPath(this.outDir, templateFileName), template);

        List<TypescriptDefault.StructureClassInfo.Structure> structures = new ArrayList<>();
        boolean hasHeader = true;
        if (!this.cacheGenCode.getGeneratedPackageNames().contains(REQUEST_HEADER_CLASS_NAME)) {
            this.cacheGenCode.addGeneratedPackageNames(REQUEST_HEADER_CLASS_NAME);
            hasHeader = false;
        }
        senCustomPkgModelStr(methods, structures);

        TypescriptDefault.StructureClassInfo classInfo = new TypescriptDefault.StructureClassInfo(structures);
        try {
            StringWriter sw = new StringWriter();
            VelocityContext context = classInfo.getVelocityContext(hasHeader);
            Template tem = Velocity.getTemplate(templateFileName);
            tem.merge(context, sw);

            codeStr.append(sw);
        } catch (Exception ex) {
            System.out.println("Failed to merge " + this.fileName.getStructureFileName() + " file");
            ex.printStackTrace();
        } finally {
            try {
                Files.delete(Path.of(CodeGenUtil.getFullPath(this.outDir, templateFileName)));
            } catch (Exception ignore) {
            }
        }
    }

    public void genResponseModel(Method[] methods) {
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
            String typescriptType = getTypescriptTypeStr(javaType, method.getReturnType());
            if (this.typeResolver.findType(typescriptType) == null) {
                importInfos.add(new TypescriptDefault.ImportInfo(
                        typescriptType,
                        this.fileName.getStructureTsFileName()
                ));
            }

            resClassInfos.add(new TypescriptDefault.ResClassInfo.Response(
                    capitalizeMethodName + "Response",
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

    public void genRequestModel(Method[] methods) {
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
                    Class<?> parameterTypeClass = parameterTypes[i];
                    JavaType javaType = JavaType.findType(parameterTypeClass.getName());
                    String typescriptType = getTypescriptTypeStr(javaType, parameterTypeClass);

                    args.add(new TypescriptDefault.ReqClassInfo.Request.Arg(
                            "arg" + i,
                            typescriptType,
                            "payload" + i,
                            parameterTypes[i].getName()
                    ));
                    if (this.typeResolver.findType(typescriptType) == null) {
                        importInfos.add(new TypescriptDefault.ImportInfo(
                                typescriptType,
                                this.fileName.getStructureTsFileName()
                        ));
                    }
                }
            }

            payloadNames.add(payloadClassName);
            reqClassInfos.add(new TypescriptDefault.ReqClassInfo.Request(
                    capitalizeMethodName + "Request",
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
