package org.hertsstack.codegen;

import java.util.List;

class TypescriptBase {
    protected final CacheGenCode cacheGenCode;
    protected final TypeResolver typeResolver;

    public TypescriptBase(TypeResolver typeResolver) {
        this.cacheGenCode = CacheGenCode.getInstance();
        this.typeResolver = typeResolver;
    }

    protected String getTypescriptTypeStr(JavaType javaType, Class<?> typeClass) {
        if (javaType == null
                && CodeGenUtil.isCustomModelClass(typeClass)
                && this.cacheGenCode.getGeneratedPackageNames().contains(typeClass.getSimpleName())) {
            return typeClass.getSimpleName();
        } else {
            return this.typeResolver.convertType(javaType).getData();
        }
    }

    protected void addImportSentenceIfNotExist(String tsType, List<TypescriptDefault.ImportInfo> importInfos, String filename) {
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
                        filename
                ));
            }
        }
    }
}
