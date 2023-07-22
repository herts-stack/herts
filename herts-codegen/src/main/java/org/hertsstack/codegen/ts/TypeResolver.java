package org.hertsstack.codegen.ts;

import java.util.Map;
import org.hertsstack.codegen.JavaType;

public interface TypeResolver {
    Map<JavaType, TypescriptType> getMapping();
    TypescriptType convertType(JavaType javaType);
    JavaType convertType(TypescriptType typescriptType);
}
