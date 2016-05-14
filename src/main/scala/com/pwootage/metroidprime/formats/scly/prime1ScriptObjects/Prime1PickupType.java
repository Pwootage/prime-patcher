package com.pwootage.metroidprime.formats.scly.prime1ScriptObjects;

public enum Prime1PickupType {
    PowerBeam(0x00),
    IceBeam(0x01),
    WaveBeam(0x02),
    PlasmaBeam(0x03),
    Missile(0x04),
    Bombs(0x06),
    PowerBomb(0x07),
    Flamethrower(0x08),
    ThermalVisor(0x09),
    ChargeBeam(0x0A),
    SuperMissile(0x0B),
    GrappleBeam(0x0C),
    XRay(0x0D),
    IceSpreader(0x0E),
    SpaceJump(0x0f),
    MorphBall(0x10),
    BoostBall(0x12),
    SpiderBall(0x13),
    PowerSuit(0x14),
    GravitySuit(0x15),
    VariaSuit(0x16),
    PhasonSuit(0x17),
    EnergyTank(0x18),
    HealthRefill(0x1A),
    Wavebuster(0x1C),

    ArtifactTruth(0x1D),
    ArtifactStrength(0x1E),
    ArtifactElder(0x1F),
    ArtifactWild(0x20),
    ArtifactLifegiver(0x21),
    ArtifactWarrior(0x22),
    ArtifactChozo(0x23),
    ArtifactNature(0x24),
    ArtifactSun(0x25),
    ArtifactWorld(0x26),
    ArtifactSprit(0x27),
    ArtifactNewborn(0x28),
    ;

    public final byte id;

    Prime1PickupType(int id) {
        if (id < 0 || id > 255) throw new IllegalArgumentException();
        this.id = (byte) id;
    }

    public static Prime1PickupType fromID(int id) {
        if (id < 0 || id > 255) throw new IllegalArgumentException();
        return fromID((byte) id);
    }

    public static Prime1PickupType fromID(byte id) {
        for (Prime1PickupType typ : Prime1PickupType.values()) {
            if (typ.id == id) return typ;
        }
        return null;
    }
}
