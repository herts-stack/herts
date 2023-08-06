package org.hertsstack.codegen;

enum TypescriptType {
    Any("any"),
    Number("number"),
    Boolean("boolean"),
    String("string"),
    Date("Date"),
    Array("Array"),
    Map("Map");

    private final String data;

    TypescriptType(final String data) {
        this.data = data;
    }

    public java.lang.String getData() {
        return data;
    }
}
