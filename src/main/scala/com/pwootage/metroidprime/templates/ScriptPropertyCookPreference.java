package com.pwootage.metroidprime.templates;

public enum ScriptPropertyCookPreference {
    ALWAYS("always"),
    NEVER("never"),
    NOPREF("nopref");

    public final String identifier;

    ScriptPropertyCookPreference(String identifier) {
        this.identifier = identifier;
    }

    public static ScriptPropertyCookPreference fromIdentifier(String identifier) {
        return valueOf(identifier.toUpperCase());
    }
}
