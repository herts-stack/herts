package org.hertsstack.codegen;

enum JavaType {
    Object(java.lang.Object.class.getName()),
    ObjectArray("[L" + java.lang.Object.class.getName() + ";"),
    ByteClass(java.lang.Byte.class.getName()),
    ByteClassArray("[L" + java.lang.Byte.class.getName() + ";"),
    Byte(byte.class.getName()),
    ByteArray("[L" + byte.class.getName() + ";"),
    ShortClass(java.lang.Short.class.getName()),
    ShortClassArray("[L" + java.lang.Short.class.getName() + ";"),
    Short(short.class.getName()),
    ShortArray("[L" + short.class.getName() + ";"),
    IntClass(java.lang.Integer.class.getName()),
    IntClassArray("[L" + java.lang.Integer.class.getName() + ";"),
    Int(int.class.getName()),
    IntArray("[L" + int.class.getName() + ";"),
    LongClass(java.lang.Long.class.getName()),
    LongClassArray("[L" + java.lang.Long.class.getName() + ";"),
    Long(long.class.getName()),
    LongArray("[L" + long.class.getName() + ";"),
    FloatClass(java.lang.Float.class.getName()),
    FloatClassArray("[L" + java.lang.Float.class.getName() + ";"),
    Float(float.class.getName()),
    FloatArray("[L" + float.class.getName() + ";"),
    DoubleClass(java.lang.Double.class.getName()),
    DoubleClassArray("[L" + java.lang.Double.class.getName() + ";"),
    Double(double.class.getName()),
    DoubleArray("[L" + double.class.getName() + ";"),
    BooleanClass(java.lang.Boolean.class.getName()),
    BooleanClassArray("[L" + java.lang.Boolean.class.getName() + ";"),
    Boolean(boolean.class.getName()),
    BooleanArray("[L" + boolean.class.getName() + ";"),
    CharacterClass(java.lang.Character.class.getName()),
    CharacterClassArray("[L" + java.lang.Character.class.getName() + ";"),
    Character(char.class.getName()),
    CharacterArray("[L" + char.class.getName() + ";"),
    String(java.lang.String.class.getName()),
    StringArray("[L" + java.lang.String.class.getName() + ";"),
    BigDecimal(java.math.BigDecimal.class.getName()),
    BigDecimalArray("[L" + java.math.BigDecimal.class.getName() + ";"),
    BigInteger(java.math.BigInteger.class.getName()),
    BigIntegerArray("[L" + java.math.BigInteger.class.getName() + ";"),
    Date(java.util.Date.class.getName()),
    DateArray("[L" + java.util.Date.class.getName() + ";"),
    UUID(java.util.UUID.class.getName()),
    UUIDArray("[L" + java.util.UUID.class.getName() + ";"),
    Enum(java.lang.Enum.class.getName()),
    ArrayList(java.util.ArrayList.class.getName()),
    List(java.util.List.class.getName()),
    Map(java.util.Map.class.getName()),
    HashMap(java.util.HashMap.class.getName()),
    Set(java.util.Set.class.getName()),
    HashSet(java.util.HashSet.class.getName());

    private final String data;

    JavaType(final String data) {
        this.data = data;
    }

    public boolean isCollection(JavaType javaType) {
        if (javaType == ArrayList || javaType == List ||
                javaType == Map || javaType == HashMap ||
                javaType == Set || javaType == HashSet) {
            return true;
        }
        return false;
    }

    public java.lang.String getData() {
        return data;
    }

    public static JavaType findType(String javaTypeStr) {
        for (JavaType javaType : JavaType.values()) {
            if (javaType.getData().equals(javaTypeStr)) {
                return javaType;
            }
        }
        return null;
    }
}
