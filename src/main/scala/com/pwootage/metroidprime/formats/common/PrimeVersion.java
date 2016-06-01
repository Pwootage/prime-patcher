package com.pwootage.metroidprime.formats.common;

public enum PrimeVersion {
    PRIME_1("Metroid Prime"),
    PRIME_2("Metroid Prime 2: Echoes");

    public final String prettyName;

    PrimeVersion(String prettyName) {
        this.prettyName = prettyName;
    }
}
