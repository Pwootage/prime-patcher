package com.pwootage.metroidprime.templates;

public enum ScriptPropertyStructType {
    MULTI("multi"),
    SINGLE("single");

    public final String identifier;

    ScriptPropertyStructType(String identifier) {
        this.identifier = identifier;
    }

    public static ScriptPropertyStructType fromIdentifier(String identifier) {
        return valueOf(identifier.toUpperCase());
    }
}
