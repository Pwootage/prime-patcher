package com.pwootage.metroidprime.formats.scly;

import com.pwootage.metroidprime.templates.ScriptTemplate;
import com.pwootage.metroidprime.templates.ScriptTemplates;

public enum Prime1ScriptObjectType {
    Actor(0x0, "mp1/Script/Actor.xml"),
    EnemyUnused(0x1),
    Waypoint(0x2, "mp1/Script/Waypoint.xml"),
    DoorArea(0x3, "mp1/Script/DoorArea.xml"),
    Trigger(0x4, "mp1/Script/Trigger.xml"),
    Timer(0x5, "mp1/Script/Timer.xml"),
    Counter(0x6, "mp1/Script/Counter.xml"),
    Effect(0x7, "mp1/Script/Effect.xml"),
    Platform(0x8, "mp1/Script/Platform.xml"),
    Sound(0x9, "mp1/Script/Sound.xml"),
    Generator(0xA, "mp1/Script/Generator.xml"),
    Dock(0xB, "mp1/Script/Dock.xml"),
    Camera(0xC, "mp1/Script/Camera.xml"),
    CameraWaypoint(0xD, "mp1/Script/CameraWaypoint.xml"),
    NewIntroBoss(0xE, "mp1/Script/NewIntroBoss.xml"),
    SpawnPoint(0xF, "mp1/Script/SpawnPoint.xml"),
    CameraHint(0x10, "mp1/Script/CameraHint.xml"),
    Pickup(0x11, "mp1/Script/Pickup.xml"),
    JumpPointUnused(0x12),
    MemoryRelay(0x13, "mp1/Script/MemoryRelay.xml"),
    RandomRelay(0x14, "mp1/Script/RandomRelay.xml"),
    Relay(0x15, "mp1/Script/Relay.xml"),
    Beetle(0x16, "mp1/Script/Beetle.xml"),
    HUDMemo(0x17, "mp1/Script/HUDMemo.xml"),
    CameraFilterKeyframe(0x18, "mp1/Script/CameraFilterKeyframe.xml"),
    CameraBlurKeyframe(0x19, "mp1/Script/CameraBlurKeyframe.xml"),
    DamageableTrigger(0x1A, "mp1/Script/DamageableTrigger.xml"),
    Debris(0x1B, "mp1/Script/Debris.xml"),
    CameraShaker(0x1C, "mp1/Script/CameraShaker.xml"),
    ActorKeyframe(0x1D, "mp1/Script/ActorKeyframe.xml"),
    Water(0x20, "mp1/Script/Water.xml"),
    WarWasp(0x21, "mp1/Script/WarWasp.xml"),
    MapStationUnused(0x22),
    SpacePirate(0x24, "mp1/Script/SpacePirate.xml"),
    FlyingPirate(0x25, "mp1/Script/FlyingPirate.xml"),
    ElitePirate(0x26, "mp1/Script/ElitePirate.xml"),
    MetroidBeta(0x27, "mp1/Script/MetroidBeta.xml"),
    ChozoGhost(0x28, "mp1/Script/ChozoGhost.xml"),
    CoverPoint(0x2A, "mp1/Script/CoverPoint.xml"),
    SpiderBallWaypoint(0x2C, "mp1/Script/SpiderBallWaypoint.xml"),
    BloodFlower(0x2D, "mp1/Script/BloodFlower.xml"),
    FlickerBat(0x2E, "mp1/Script/FlickerBat.xml"),
    PathCamera(0x2F, "mp1/Script/PathCamera.xml"),
    GrapplePoint(0x30, "mp1/Script/GrapplePoint.xml"),
    PuddleSpore(0x31, "mp1/Script/PuddleSpore.xml"),
    DebugCameraWaypoint(0x32, "mp1/Script/DebugCameraWaypoint.xml"),
    SpiderBallAttractionSurface(0x33, "mp1/Script/SpiderBallAttractionSurface.xml"),
    PuddleToadGamma(0x34, "mp1/Script/PuddleToadGamma.xml"),
    DistanceFog(0x35, "mp1/Script/DistanceFog.xml"),
    FireFlea(0x36, "mp1/Script/FireFlea.xml"),
    MetareeAlpha(0x37, "mp1/Script/MetareeAlpha.xml"),
    DockAreaChange(0x38, "mp1/Script/DockAreaChange.xml"),
    ActorRotate(0x39, "mp1/Script/ActorRotate.xml"),
    SpecialFunction(0x3A, "mp1/Script/SpecialFunction.xml"),
    SpankWeed(0x3B, "mp1/Script/SpankWeed.xml"),
    SovaUnused(0x3C),
    Parasite(0x3D, "mp1/Script/Parasite.xml"),
    PlayerHint(0x3E, "mp1/Script/PlayerHint.xml"),
    Ripper(0x3F, "mp1/Script/Ripper.xml"),
    PickupGenerator(0x40, "mp1/Script/PickupGenerator.xml"),
    AIKeyframe(0x41, "mp1/Script/AIKeyframe.xml"),
    PointOfInterest(0x42, "mp1/Script/PointOfInterest.xml"),
    Drone(0x43, "mp1/Script/Drone.xml"),
    MetroidAlpha(0x44, "mp1/Script/MetroidAlpha.xml"),
    DebrisExtended(0x45, "mp1/Script/DebrisExtended.xml"),
    Steam(0x46, "mp1/Script/Steam.xml"),
    Ripple(0x47, "mp1/Script/Ripple.xml"),
    BallTrigger(0x48, "mp1/Script/BallTrigger.xml"),
    TargetingPoint(0x49, "mp1/Script/TargetingPoint.xml"),
    ElectroMagneticPulse(0x4A, "mp1/Script/ElectroMagneticPulse.xml"),
    IceSheegoth(0x4B, "mp1/Script/IceSheegoth.xml"),
    PlayerActor(0x4C, "mp1/Script/PlayerActor.xml"),
    Flaahgra(0x4D, "mp1/Script/Flaahgra.xml"),
    AreaAttributes(0x4E, "mp1/Script/AreaAttributes.xml"),
    FishCloud(0x4F, "mp1/Script/FishCloud.xml"),
    FishCloudModifier(0x50, "mp1/Script/FishCloudModifier.xml"),
    VisorFlare(0x51, "mp1/Script/VisorFlare.xml"),
    WorldTeleporterUnused(0x52),
    VisorGoo(0x53, "mp1/Script/VisorGoo.xml"),
    JellyZap(0x54, "mp1/Script/JellyZap.xml"),
    ControllerAction(0x55, "mp1/Script/ControllerAction.xml"),
    Switch(0x56, "mp1/Script/Switch.xml"),
    PlayerStateChange(0x57, "mp1/Script/PlayerStateChange.xml"),
    Thardus(0x58, "mp1/Script/Thardus.xml"),
    SaveStationUnused(0x59),
    WallCrawlerSwarm(0x5A, "mp1/Script/WallCrawlerSwarm.xml"),
    AIJumpPoint(0x5B, "mp1/Script/AIJumpPoint.xml"),
    FlaahgraTentacle(0x5C, "mp1/Script/FlaahgraTentacle.xml"),
    RoomAcoustics(0x5D, "mp1/Script/RoomAcoustics.xml"),
    ColorModulate(0x5E, "mp1/Script/ColorModulate.xml"),
    ThardusRockProjectile(0x5F, "mp1/Script/ThardusRockProjectile.xml"),
    Midi(0x60, "mp1/Script/Midi.xml"),
    StreamedAudio(0x61, "mp1/Script/StreamedAudio.xml"),
    WorldTeleporter(0x62, "mp1/Script/WorldTeleporter.xml"),
    Repulsor(0x63, "mp1/Script/Repulsor.xml"),
    GunTurret(0x64, "mp1/Script/GunTurret.xml"),
    FogVolume(0x65, "mp1/Script/FogVolume.xml"),
    Babygoth(0x66, "mp1/Script/Babygoth.xml"),
    Eyeball(0x67, "mp1/Script/Eyeball.xml"),
    RadialDamage(0x68, "mp1/Script/RadialDamage.xml"),
    CameraPitchVolume(0x69, "mp1/Script/CameraPitchVolume.xml"),
    EnvFxDensityController(0x6A, "mp1/Script/EnvFxDensityController.xml"),
    Magdolite(0x6B, "mp1/Script/Magdolite.xml"),
    TeamAIMgr(0x6C, "mp1/Script/TeamAIMgr.xml"),
    SnakeWeedSwarm(0x6D, "mp1/Script/SnakeWeedSwarm.xml"),
    ActorContraption(0x6E, "mp1/Script/ActorContraption.xml"),
    Oculus(0x6F, "mp1/Script/Oculus.xml"),
    Geemer(0x70, "mp1/Script/Geemer.xml"),
    SpindleCamera(0x71, "mp1/Script/SpindleCamera.xml"),
    AtomicAlpha(0x72, "mp1/Script/AtomicAlpha.xml"),
    CameraHintTrigger(0x73, "mp1/Script/CameraHintTrigger.xml"),
    RumbleEffect(0x74, "mp1/Script/RumbleEffect.xml"),
    AmbientAI(0x75, "mp1/Script/AmbientAI.xml"),
    AtomicBeta(0x77, "mp1/Script/AtomicBeta.xml"),
    IceZoomer(0x78, "mp1/Script/IceZoomer.xml"),
    Puffer(0x79, "mp1/Script/Puffer.xml"),
    Tryclops(0x7A, "mp1/Script/Tryclops.xml"),
    Ridley(0x7B, "mp1/Script/Ridley.xml"),
    Seedling(0x7C, "mp1/Script/Seedling.xml"),
    ThermalHeatFader(0x7D, "mp1/Script/ThermalHeatFader.xml"),
    Burrower(0x7F, "mp1/Script/Burrower.xml"),
    ScriptBeam(0x81, "mp1/Script/ScriptBeam.xml"),
    WorldLightFader(0x82, "mp1/Script/WorldLightFader.xml"),
    MetroidPrimeStage2(0x83, "mp1/Script/MetroidPrimeStage2.xml"),
    MetroidPrimeStage1(0x84, "mp1/Script/MetroidPrimeStage1.xml"),
    MazeNode(0x85, "mp1/Script/MazeNode.xml"),
    OmegaPirate(0x86, "mp1/Script/OmegaPirate.xml"),
    PhazonPool(0x87, "mp1/Script/PhazonPool.xml"),
    PhazonHealingNodule(0x88, "mp1/Script/PhazonHealingNodule.xml"),
    NewCameraShaker(0x89, "mp1/Script/NewCameraShaker.xml"),
    ShadowProjector(0x8A, "mp1/Script/ShadowProjector.xml"),
    EnergyBall(0x8B, "mp1/Script/EnergyBall.xml");

    public final byte id;
    public final String xmlFile;

    Prime1ScriptObjectType(int id, String xmlFile) {
        this.xmlFile = xmlFile;
        if (id < 0 || id > 255) throw new IllegalArgumentException();
        this.id = (byte) id;
    }

    Prime1ScriptObjectType(int id) {
        this.xmlFile = null;
        if (id < 0 || id > 255) throw new IllegalArgumentException();
        this.id = (byte) id;
    }

    public ScriptTemplate template() {
        if (xmlFile == null) {
            return null;
        }
        return ScriptTemplates.loadTemplate(xmlFile);
    }

    public static Prime1ScriptObjectType fromID(int id) {
        if (id < 0 || id > 255) throw new IllegalArgumentException();
        return fromID((byte) id);
    }

    public static Prime1ScriptObjectType fromID(byte id) {
        for (Prime1ScriptObjectType typ : Prime1ScriptObjectType.values()) {
            if (typ.id == id) return typ;
        }
        return null;
    }
}
