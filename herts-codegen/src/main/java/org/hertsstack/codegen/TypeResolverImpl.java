package org.hertsstack.codegen;

import java.util.HashMap;
import java.util.Map;

class TypeResolverImpl implements TypeResolver {
    private final static Map<JavaType, TypescriptType> MAPPING = new HashMap<>() {
        {
            put(JavaType.Object, TypescriptType.Any);
            put(JavaType.ByteClass, TypescriptType.Number);
            put(JavaType.Byte, TypescriptType.Number);
            put(JavaType.ShortClass, TypescriptType.Number);
            put(JavaType.Short, TypescriptType.Number);
            put(JavaType.IntClass, TypescriptType.Number);
            put(JavaType.Int, TypescriptType.Number);
            put(JavaType.LongClass, TypescriptType.Number);
            put(JavaType.Long, TypescriptType.Number);
            put(JavaType.FloatClass, TypescriptType.Number);
            put(JavaType.Float, TypescriptType.Number);
            put(JavaType.DoubleClass, TypescriptType.Number);
            put(JavaType.Double, TypescriptType.Number);
            put(JavaType.BooleanClass, TypescriptType.Boolean);
            put(JavaType.Boolean, TypescriptType.Boolean);
            put(JavaType.CharacterClass, TypescriptType.String);
            put(JavaType.Character, TypescriptType.String);
            put(JavaType.String, TypescriptType.String);
            put(JavaType.BigDecimal, TypescriptType.Number);
            put(JavaType.BigInteger, TypescriptType.Number);
            put(JavaType.Date, TypescriptType.Date);
            put(JavaType.UUID, TypescriptType.String);
            put(JavaType.ArrayList, TypescriptType.Any);
            put(JavaType.List, TypescriptType.Any);
            put(JavaType.HashMap, TypescriptType.Any);
            put(JavaType.Map, TypescriptType.Any);
            put(JavaType.HashSet, TypescriptType.Any);
            put(JavaType.Set, TypescriptType.Any);
        }
    };

    @Override
    public Map<JavaType, TypescriptType> getMapping() {
        return MAPPING;
    }

    @Override
    public TypescriptType convertType(JavaType javaType) {
        if (javaType == null) {
            return TypescriptType.Any;
        }
        return MAPPING.get(javaType);
    }

    @Override
    public JavaType convertType(TypescriptType typescriptType) {
        for (Map.Entry<JavaType, TypescriptType> entry : MAPPING.entrySet()) {
            if (entry.getValue().equals(typescriptType)) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    public TypescriptType findType(String packageName) {
        for (TypescriptType typescriptType : TypescriptType.values()) {
            if (typescriptType.getData().equals(packageName)) {
                return typescriptType;
            }
        }
        return null;
    }
}
