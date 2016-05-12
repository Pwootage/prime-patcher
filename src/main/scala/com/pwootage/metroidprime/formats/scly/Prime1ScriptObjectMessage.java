package com.pwootage.metroidprime.formats.scly;

public enum Prime1ScriptObjectMessage {
    Activate(0x1),
    Unknown_0x02(0x2),
    Close(0x3),
    Deactivate(0x4),
    Decrement(0x5),
    Follow(0x6),
    Increment(0x7),
    Next(0x8),
    Open(0x9),
    Reset(0xA),
    ResetAndStart(0xB),
    SetToMax(0xC),
    SetToZero(0xD),
    Start(0xE),
    Stop(0xF),
    StopAndReset(0x10),
    ToggleActive(0x11),
    Unknown_0x12(0x12),
    Action(0x13),
    Play(0x14),
    Alert(0x15);

    public final byte id;

    Prime1ScriptObjectMessage(int id) {
        if (id < 0 || id > 255) throw new IllegalArgumentException();
        this.id = (byte) id;
    }

    public static Prime1ScriptObjectMessage fromID(int id) {
        if (id < 0 || id > 255) throw new IllegalArgumentException();
        return fromID((byte) id);
    }

    public static Prime1ScriptObjectMessage fromID(byte id) {
        for (Prime1ScriptObjectMessage typ : Prime1ScriptObjectMessage.values()) {
            if (typ.id == id) return typ;
        }
        return null;
    }
}
