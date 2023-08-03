package org.hertsstack.codegen;

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
}
