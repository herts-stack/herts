package org.hertsstack.codegen;

enum TypescriptType {
    Any("any"),
    Number("number"),
    Boolean("boolean"),
    String("string"),
    Date("Date"),
    ArrayString("Array<string>"),
    ArrayNumber("Array<number>"),
    ArrayBoolean("Array<boolean>"),
    ArrayDate("Array<Date>"),
    Array("Array<$0>"),
    Map("Map<$0, $1>");

    private final String data;

    TypescriptType(final String data) {
        this.data = data;
    }

    public java.lang.String getData() {
        return data;
    }
}
