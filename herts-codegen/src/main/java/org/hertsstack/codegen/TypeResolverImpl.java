package org.hertsstack.codegen;

import java.util.HashMap;
import java.util.Map;

class TypeResolverImpl implements TypeResolver {
    private final static Map<JavaType, TypescriptType> MAPPING = new HashMap<>() {
        {
            put(JavaType.Void, TypescriptType.Void);
            put(JavaType.Object, TypescriptType.Any);
            put(JavaType.ByteClass, TypescriptType.Number);
            put(JavaType.ByteClassArray, TypescriptType.ArrayNumber);
            put(JavaType.Byte, TypescriptType.Number);
            put(JavaType.ByteArray, TypescriptType.ArrayNumber);
            put(JavaType.ShortClass, TypescriptType.Number);
            put(JavaType.ShortClassArray, TypescriptType.ArrayNumber);
            put(JavaType.Short, TypescriptType.Number);
            put(JavaType.ShortArray, TypescriptType.ArrayNumber);
            put(JavaType.IntClass, TypescriptType.Number);
            put(JavaType.IntClassArray, TypescriptType.ArrayNumber);
            put(JavaType.Int, TypescriptType.Number);
            put(JavaType.IntArray, TypescriptType.ArrayNumber);
            put(JavaType.LongClass, TypescriptType.Number);
            put(JavaType.LongClassArray, TypescriptType.ArrayNumber);
            put(JavaType.Long, TypescriptType.Number);
            put(JavaType.LongArray, TypescriptType.ArrayNumber);
            put(JavaType.FloatClass, TypescriptType.Number);
            put(JavaType.FloatClassArray, TypescriptType.ArrayNumber);
            put(JavaType.Float, TypescriptType.Number);
            put(JavaType.FloatArray, TypescriptType.ArrayNumber);
            put(JavaType.DoubleClass, TypescriptType.Number);
            put(JavaType.DoubleClassArray, TypescriptType.ArrayNumber);
            put(JavaType.Double, TypescriptType.Number);
            put(JavaType.DoubleArray, TypescriptType.ArrayNumber);
            put(JavaType.BooleanClass, TypescriptType.Boolean);
            put(JavaType.BooleanClassArray, TypescriptType.ArrayBoolean);
            put(JavaType.Boolean, TypescriptType.Boolean);
            put(JavaType.BooleanArray, TypescriptType.ArrayBoolean);
            put(JavaType.CharacterClass, TypescriptType.String);
            put(JavaType.CharacterClassArray, TypescriptType.ArrayString);
            put(JavaType.Character, TypescriptType.String);
            put(JavaType.CharacterArray, TypescriptType.ArrayString);
            put(JavaType.String, TypescriptType.String);
            put(JavaType.StringArray, TypescriptType.ArrayString);
            put(JavaType.BigDecimal, TypescriptType.Number);
            put(JavaType.BigDecimalArray, TypescriptType.ArrayNumber);
            put(JavaType.BigInteger, TypescriptType.Number);
            put(JavaType.BigIntegerArray, TypescriptType.ArrayNumber);
            put(JavaType.Date, TypescriptType.Date);
            put(JavaType.DateArray, TypescriptType.ArrayDate);
            put(JavaType.UUID, TypescriptType.String);
            put(JavaType.UUIDArray, TypescriptType.ArrayString);
            put(JavaType.ArrayList, TypescriptType.Any);
            put(JavaType.List, TypescriptType.Array);
            put(JavaType.HashMap, TypescriptType.Map);
            put(JavaType.Map, TypescriptType.Map);
            put(JavaType.HashSet, TypescriptType.Array);
            put(JavaType.Set, TypescriptType.Array);
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
