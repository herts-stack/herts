package org.hertsstack.codegen;

import java.util.Map;

/**
 * Code type resolver.
 *
 * @author Herts Contributer
 */
interface TypeResolver {

    /**
     * Get mapping Java and Typescript.
     *
     * @return Mapiing information
     */
    Map<JavaType, TypescriptType> getMapping();

    /**
     * Convert type from Java to Typescript.
     *
     * @param javaType JavaType
     * @return TypescriptType
     */
    TypescriptType convertType(JavaType javaType);

    /**
     * Convert type from Typescript to Java.
     *
     * @param typescriptType TypescriptType
     * @return JavaType
     */
    JavaType convertType(TypescriptType typescriptType);

    /**
     * Find Typescript type by java package name.
     *
     * @param packageName Package name
     * @return TypescriptType
     */
    TypescriptType findType(String packageName);
}
