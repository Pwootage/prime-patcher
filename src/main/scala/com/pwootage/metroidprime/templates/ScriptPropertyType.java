package com.pwootage.metroidprime.templates;

public enum ScriptPropertyType {
    BOOL("bool"),
    BYTE("byte"),
    SHORT("short"),
    LONG("long"),
    FLOAT("float"),
    STRING("string"),
    VECTOR3F("vector3f"),
    COLOR("color"),
    FILE("file"),
    CHARACTER("character"),
    MAYASPLINE("MayaSpline"),
    ENUM("enum"),
    BITFIELD("bitfield"),
    STRUCT("struct"),
    ARRAY("array");

    public final String identifier;

    ScriptPropertyType(String identifier) {
        this.identifier = identifier;
    }

    public static ScriptPropertyType fromIdentifier(String identifier) {
        return valueOf(identifier.toUpperCase());
    }
}
