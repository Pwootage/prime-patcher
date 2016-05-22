package com.pwootage.metroidprime.formats.common;

import com.pwootage.metroidprime.utils.DataTypeConversion;

public enum PrimeFileType {
    MREA("MREA"),
    MLVL("MLFL"),
    STRG("STRG")
    ;

    public final int fourCC;
    public final String text;

    PrimeFileType(String text) {
        this.text = text;
        this.fourCC = DataTypeConversion.strToIntContainingChars(text);
    }
}
