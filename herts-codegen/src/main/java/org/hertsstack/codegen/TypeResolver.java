package org.hertsstack.codegen;

import java.util.Map;

interface TypeResolver {
    Map<JavaType, TypescriptType> getMapping();
    TypescriptType convertType(JavaType javaType);
    JavaType convertType(TypescriptType typescriptType);
    TypescriptType findType(String packageName);
}
