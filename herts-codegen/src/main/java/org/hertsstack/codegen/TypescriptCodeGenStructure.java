package org.hertsstack.codegen;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

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

    private void setConstructorInfoForMap(String propertyName,
                                          Class<?> keyClass,
                                          Class<?> valueClass,
                                          List<TypescriptDefault.ConstructorInfo> constructorInfos,
                                          List<String> nestedModelPackages) throws ClassNotFoundException {

        // TODO: Unused custom model
        if (CodeGenUtil.isCustomModelClass(keyClass)) {
            nestedModelPackages.add(keyClass.getTypeName());
        }
        if (CodeGenUtil.isCustomModelClass(valueClass)) {
            nestedModelPackages.add(valueClass.getTypeName());
        }

        JavaType keyJavaType = JavaType.findType(keyClass.getName());
        String keyPropertyTypeStr = this.typeResolver.convertType(keyJavaType).getData();

        JavaType valueJavaType = JavaType.findType(valueClass.getName());
        String valuePropertyTypeStr = this.typeResolver.convertType(valueJavaType).getData();

        String propertyTypeStr = TypescriptType.Map.getData()
                .replace("$0", keyPropertyTypeStr)
                .replace("$1", valuePropertyTypeStr);

        constructorInfos.add(new TypescriptDefault.ConstructorInfo(propertyName, propertyTypeStr));
    }

    private void setConstructorInfo(String propertyName,
                                    String propertyType,
                                    Class<?> nestedClass,
                                    List<TypescriptDefault.ConstructorInfo> constructorInfos,
                                    List<String> nestedModelPackages,
                                    boolean isArray,
                                    boolean isCustom) throws ClassNotFoundException {

        if (CodeGenUtil.isCustomModelClass(nestedClass)) {
            nestedModelPackages.add(propertyType);
        }

        String[] classSplit;
        if (propertyType.contains("$")) {
            classSplit = propertyType.split("\\$");
        } else {
            classSplit = propertyType.split("\\.");
        }

        String propertyTypeStr = null;
        if (isArray) {
            propertyTypeStr = TypescriptType.Array.getData().replace("$0", classSplit[classSplit.length - 1]);
        } else if (isCustom) {
            propertyTypeStr = classSplit[classSplit.length - 1];
        } else {
            JavaType type = JavaType.findType( classSplit[classSplit.length - 1]);
            TypescriptType typescriptType = this.typeResolver.convertType(type);
            propertyTypeStr = typescriptType.getData();
        }

        constructorInfos.add(new TypescriptDefault.ConstructorInfo(propertyName, propertyTypeStr));
    }

    private void setEnumInfo(String propertyName,
                             Class<?> enumClass,
                             List<TypescriptDefault.ConstructorInfo> constructorInfos,
                             List<TypescriptDefault.StructureClassInfo.EnumInfo> enumInfos) throws ClassNotFoundException {

        Object[] enumObjects = enumClass.getEnumConstants();
        List<String> enumValues = new ArrayList<>();
        for (Object o : enumObjects) {
            enumValues.add(o.toString());
        }
        String enumName = CodeGenUtil.capitalizeFirstLetter(propertyName);
        enumInfos.add(new TypescriptDefault.StructureClassInfo.EnumInfo(enumName, enumValues));
        constructorInfos.add(new TypescriptDefault.ConstructorInfo(propertyName, enumName));
    }

    private void genNestedModel(List<TypescriptDefault.StructureClassInfo.Structure> structures,
                                Class<?> targetClass,
                                List<TypescriptDefault.StructureClassInfo.EnumInfo> enumInfos) throws ClassNotFoundException {

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
//            System.out.println("Name: " + propertyName);
//            System.out.println("Type: " + propertyType);

            JavaType javaType = JavaType.findType(propertyType);
            TypescriptType typescriptType = this.typeResolver.convertType(javaType);

            Class<?> classObj = null;
            try {
                classObj = Class.forName(propertyType);
                if (classObj.isEnum()) {
                    setEnumInfo(propertyName, classObj, constructorInfos, enumInfos);
                    continue;
                }
            } catch (java.lang.ClassNotFoundException ignore) {
            }

            // Custom Object
            if (javaType == null) {
                setConstructorInfo(propertyName, propertyType, classObj, constructorInfos, nestedModelPackages, false, true);

            // Array
            } else if (typescriptType == TypescriptType.Array) {
                String typeName = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].getTypeName();
                classObj = Class.forName(typeName);
                setConstructorInfo(propertyName, typeName, classObj, constructorInfos, nestedModelPackages, true, false);

            // Map
            } else if (typescriptType == TypescriptType.Map) {
                String keyTypeName = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].getTypeName() ;
                String valueTypeName = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[1].getTypeName();
                setConstructorInfoForMap(propertyName, Class.forName(keyTypeName), Class.forName(valueTypeName), constructorInfos, nestedModelPackages);

            // Default converter
            } else {
                constructorInfos.add(new TypescriptDefault.ConstructorInfo(propertyName, typescriptType.getData()));
            }
        }
        structure.setConstructors(constructorInfos);
        structures.add(structure);

        this.cacheGenCode.addGeneratedPackageNames(targetClass.getSimpleName());

        // Recursive generate model
        for (String nestedModelPkg : nestedModelPackages) {
            Class<?> nestedClass = Class.forName(nestedModelPkg);
            genNestedModel(structures, nestedClass, enumInfos);
        }
    }

    private void senCustomPkgModelStr(Method[] methods,
                                      List<TypescriptDefault.StructureClassInfo.Structure> structures,
                                      List<TypescriptDefault.StructureClassInfo.EnumInfo> enumInfos) {

        for (Method method : methods) {
            Class<?>[] parameterTypes = method.getParameterTypes();

            // Parameter structures
            for (Class<?> paramTypeClass : parameterTypes) {
                if (CodeGenUtil.isCustomModelClass(paramTypeClass)) {
                    try {
                        genNestedModel(structures, paramTypeClass, enumInfos);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }

            // Return structure
            Class<?> returnType = method.getReturnType();
            JavaType javaType = JavaType.findType(returnType.getName());
            if (javaType == null && !returnType.getName().equals("void")) {
                try {
                    genNestedModel(structures, returnType, enumInfos);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static final String REQUEST_HEADER_CLASS_NAME = "RequestHeaders";

    public String genStructureModel(Method[] methods) {
        System.out.println("Typescript file name = " + this.fileName.getStructureFileName());
        System.out.println("Generating...");

        String templateFileName = this.fileName.getStructureTsFileName() + ".vm";
        String template = TypescriptDefault.STRUCTURE_CUSTOM_MODEL_CLASS;
        CodeGenUtil.writeFile(CodeGenUtil.getFullPath(this.outDir, templateFileName), template);

        List<TypescriptDefault.StructureClassInfo.Structure> structures = new ArrayList<>();
        List<TypescriptDefault.StructureClassInfo.EnumInfo> enumInfos = new ArrayList<>();
        boolean hasHeader = true;
        if (!this.cacheGenCode.getGeneratedPackageNames().contains(REQUEST_HEADER_CLASS_NAME)) {
            this.cacheGenCode.addGeneratedPackageNames(REQUEST_HEADER_CLASS_NAME);
            hasHeader = false;
        }
        senCustomPkgModelStr(methods, structures, enumInfos);

        TypescriptDefault.StructureClassInfo classInfo = new TypescriptDefault.StructureClassInfo(structures, enumInfos);
        try {
            StringWriter sw = new StringWriter();
            VelocityContext context = classInfo.getVelocityContext(hasHeader);
            Template tem = Velocity.getTemplate(templateFileName);
            tem.merge(context, sw);

            return sw.toString();
        } catch (Exception ex) {
            System.out.println("Failed to merge " + this.fileName.getStructureFileName() + " file");
            ex.printStackTrace();
        } finally {
            try {
                Files.delete(Path.of(CodeGenUtil.getFullPath(this.outDir, templateFileName)));
            } catch (Exception ignore) {
            }
        }
        return null;
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
            String defaultTypescriptType = getTypescriptTypeStr(javaType, method.getReturnType());

            BiFunction<JavaType, Class<?>, String> generateTypescriptType = this::getTypescriptTypeStr;
            String typescriptType = CodeGenUtil.getGeneticTypes(javaType, defaultTypescriptType,
                    new Type[]{method.getGenericReturnType()}, generateTypescriptType);

            addImportSentenceIfNotExist(defaultTypescriptType, importInfos);

            resClassInfos.add(new TypescriptDefault.ResClassInfo.Response(
                    capitalizeMethodName + "MethodResponse",
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
                    String defaultTypescriptType = getTypescriptTypeStr(javaType, parameterTypeClass);

                    BiFunction<JavaType, Class<?>, String> generateTypescriptType = this::getTypescriptTypeStr;
                    String typescriptType = CodeGenUtil.getGeneticTypes(javaType, defaultTypescriptType,
                            method.getGenericParameterTypes(), generateTypescriptType);

                    args.add(new TypescriptDefault.ReqClassInfo.Request.Arg(
                            "arg" + i,
                            typescriptType,
                            "payload" + i
                    ));

                    addImportSentenceIfNotExist(defaultTypescriptType, importInfos);
                }
            }

            payloadNames.add(payloadClassName);
            reqClassInfos.add(new TypescriptDefault.ReqClassInfo.Request(
                    capitalizeMethodName + "MethodRequest",
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

    private void addImportSentenceIfNotExist(String tsType, List<TypescriptDefault.ImportInfo> importInfos) {
        if (this.typeResolver.findType(tsType) == null) {
            boolean isImported = false;
            for (TypescriptDefault.ImportInfo importInfo : importInfos) {
                if (importInfo.getName().equals(tsType)) {
                    isImported = true;
                    break;
                }
            }
            if (!isImported) {
                importInfos.add(new TypescriptDefault.ImportInfo(
                        tsType,
                        this.fileName.getStructureTsFileName()
                ));
            }
        }
    }
}
