package com.pwootage.metroidprime.formats.scly;

public enum Prime1ScriptObjectState {
    Active(0x0),
    Arrived(0x1),
    Closed(0x2),
    Entered(0x3),
    Exited(0x4),
    Inactive(0x5),
    Inside(0x6),
    MaxReached(0x7),
    Open(0x8),
    Zero(0x9),
    Attack(0xA),
    Unknown_0x0B(0xB),
    Retreat(0xC),
    Patrol(0xD),
    Dead(0xE),
    CameraPath(0xF),
    CameraTarget(0x10),
    Unknown_0x11(0x11),
    Play(0x12),
    Unknown_x13(0x13),
    DeathRattle(0x14),
    Unknown_0x15(0x15),
    Damage(0x16),
    Unknown_0x17(0x17),
    Unknown_0x18(0x18),
    Modify(0x19),
    ScanDone(0x1C),
    Unknown(0x1E),
    ReflectedDamage(0x1F),
    InheritBounds(0x20);

    public final byte id;

    Prime1ScriptObjectState(int id) {
        if (id < 0 || id > 255) throw new IllegalArgumentException();
        this.id = (byte) id;
    }

    public static Prime1ScriptObjectState fromID(int id) {
        if (id < 0 || id > 255) throw new IllegalArgumentException();
        return fromID((byte) id);
    }

    public static Prime1ScriptObjectState fromID(byte id) {
        for (Prime1ScriptObjectState typ : Prime1ScriptObjectState.values()) {
            if (typ.id == id) return typ;
        }
        return null;
    }
}
