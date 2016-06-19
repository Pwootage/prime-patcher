package com.pwootage.metroidprime.formats.scly;

import com.pwootage.metroidprime.templates.ScriptTemplate;
import com.pwootage.metroidprime.templates.ScriptTemplates;

public enum Prime1ScriptObjectType {
    Actor(0x0, "mp1/script/Actor.xml"),
    EnemyUnused(0x1),
    Waypoint(0x2, "mp1/script/Waypoint.xml"),
    DoorArea(0x3, "mp1/script/DoorArea.xml"),
    Trigger(0x4, "mp1/script/Trigger.xml"),
    Timer(0x5, "mp1/script/Timer.xml"),
    Counter(0x6, "mp1/script/Counter.xml"),
    Effect(0x7, "mp1/script/Effect.xml"),
    Platform(0x8, "mp1/script/Platform.xml"),
    Sound(0x9, "mp1/script/Sound.xml"),
    Generator(0xA, "mp1/script/Generator.xml"),
    Dock(0xB, "mp1/script/Dock.xml"),
    Camera(0xC, "mp1/script/Camera.xml"),
    CameraWaypoint(0xD, "mp1/script/CameraWaypoint.xml"),
    NewIntroBoss(0xE, "mp1/script/NewIntroBoss.xml"),
    SpawnPoint(0xF, "mp1/script/SpawnPoint.xml"),
    CameraHint(0x10, "mp1/script/CameraHint.xml"),
    Pickup(0x11, "mp1/script/Pickup.xml"),
    JumpPointUnused(0x12),
    MemoryRelay(0x13, "mp1/script/MemoryRelay.xml"),
    RandomRelay(0x14, "mp1/script/RandomRelay.xml"),
    Relay(0x15, "mp1/script/Relay.xml"),
    Beetle(0x16, "mp1/script/Beetle.xml"),
    HUDMemo(0x17, "mp1/script/HUDMemo.xml"),
    CameraFilterKeyframe(0x18, "mp1/script/CameraFilterKeyframe.xml"),
    CameraBlurKeyframe(0x19, "mp1/script/CameraBlurKeyframe.xml"),
    DamageableTrigger(0x1A, "mp1/script/DamageableTrigger.xml"),
    Debris(0x1B, "mp1/script/Debris.xml"),
    CameraShaker(0x1C, "mp1/script/CameraShaker.xml"),
    ActorKeyframe(0x1D, "mp1/script/ActorKeyframe.xml"),
    Water(0x20, "mp1/script/Water.xml"),
    WarWasp(0x21, "mp1/script/WarWasp.xml"),
    MapStationUnused(0x22),
    SpacePirate(0x24, "mp1/script/SpacePirate.xml"),
    FlyingPirate(0x25, "mp1/script/FlyingPirate.xml"),
    ElitePirate(0x26, "mp1/script/ElitePirate.xml"),
    MetroidBeta(0x27, "mp1/script/MetroidBeta.xml"),
    ChozoGhost(0x28, "mp1/script/ChozoGhost.xml"),
    CoverPoint(0x2A, "mp1/script/CoverPoint.xml"),
    SpiderBallWaypoint(0x2C, "mp1/script/SpiderBallWaypoint.xml"),
    BloodFlower(0x2D, "mp1/script/BloodFlower.xml"),
    FlickerBat(0x2E, "mp1/script/FlickerBat.xml"),
    PathCamera(0x2F, "mp1/script/PathCamera.xml"),
    GrapplePoint(0x30, "mp1/script/GrapplePoint.xml"),
    PuddleSpore(0x31, "mp1/script/PuddleSpore.xml"),
    DebugCameraWaypoint(0x32, "mp1/script/DebugCameraWaypoint.xml"),
    SpiderBallAttractionSurface(0x33, "mp1/script/SpiderBallAttractionSurface.xml"),
    PuddleToadGamma(0x34, "mp1/script/PuddleToadGamma.xml"),
    DistanceFog(0x35, "mp1/script/DistanceFog.xml"),
    FireFlea(0x36, "mp1/script/FireFlea.xml"),
    MetareeAlpha(0x37, "mp1/script/MetareeAlpha.xml"),
    DockAreaChange(0x38, "mp1/script/DockAreaChange.xml"),
    ActorRotate(0x39, "mp1/script/ActorRotate.xml"),
    SpecialFunction(0x3A, "mp1/script/SpecialFunction.xml"),
    SpankWeed(0x3B, "mp1/script/SpankWeed.xml"),
    SovaUnused(0x3C),
    Parasite(0x3D, "mp1/script/Parasite.xml"),
    PlayerHint(0x3E, "mp1/script/PlayerHint.xml"),
    Ripper(0x3F, "mp1/script/Ripper.xml"),
    PickupGenerator(0x40, "mp1/script/PickupGenerator.xml"),
    AIKeyframe(0x41, "mp1/script/AIKeyframe.xml"),
    PointOfInterest(0x42, "mp1/script/PointOfInterest.xml"),
    Drone(0x43, "mp1/script/Drone.xml"),
    MetroidAlpha(0x44, "mp1/script/MetroidAlpha.xml"),
    DebrisExtended(0x45, "mp1/script/DebrisExtended.xml"),
    Steam(0x46, "mp1/script/Steam.xml"),
    Ripple(0x47, "mp1/script/Ripple.xml"),
    BallTrigger(0x48, "mp1/script/BallTrigger.xml"),
    TargetingPoint(0x49, "mp1/script/TargetingPoint.xml"),
    ElectroMagneticPulse(0x4A, "mp1/script/ElectroMagneticPulse.xml"),
    IceSheegoth(0x4B, "mp1/script/IceSheegoth.xml"),
    PlayerActor(0x4C, "mp1/script/PlayerActor.xml"),
    Flaahgra(0x4D, "mp1/script/Flaahgra.xml"),
    AreaAttributes(0x4E, "mp1/script/AreaAttributes.xml"),
    FishCloud(0x4F, "mp1/script/FishCloud.xml"),
    FishCloudModifier(0x50, "mp1/script/FishCloudModifier.xml"),
    VisorFlare(0x51, "mp1/script/VisorFlare.xml"),
    WorldTeleporterUnused(0x52),
    VisorGoo(0x53, "mp1/script/VisorGoo.xml"),
    JellyZap(0x54, "mp1/script/JellyZap.xml"),
    ControllerAction(0x55, "mp1/script/ControllerAction.xml"),
    Switch(0x56, "mp1/script/Switch.xml"),
    PlayerStateChange(0x57, "mp1/script/PlayerStateChange.xml"),
    Thardus(0x58, "mp1/script/Thardus.xml"),
    SaveStationUnused(0x59),
    WallCrawlerSwarm(0x5A, "mp1/script/WallCrawlerSwarm.xml"),
    AIJumpPoint(0x5B, "mp1/script/AIJumpPoint.xml"),
    FlaahgraTentacle(0x5C, "mp1/script/FlaahgraTentacle.xml"),
    RoomAcoustics(0x5D, "mp1/script/RoomAcoustics.xml"),
    ColorModulate(0x5E, "mp1/script/ColorModulate.xml"),
    ThardusRockProjectile(0x5F, "mp1/script/ThardusRockProjectile.xml"),
    Midi(0x60, "mp1/script/Midi.xml"),
    StreamedAudio(0x61, "mp1/script/StreamedAudio.xml"),
    WorldTeleporter(0x62, "mp1/script/WorldTeleporter.xml"),
    Repulsor(0x63, "mp1/script/Repulsor.xml"),
    GunTurret(0x64, "mp1/script/GunTurret.xml"),
    FogVolume(0x65, "mp1/script/FogVolume.xml"),
    Babygoth(0x66, "mp1/script/Babygoth.xml"),
    Eyeball(0x67, "mp1/script/Eyeball.xml"),
    RadialDamage(0x68, "mp1/script/RadialDamage.xml"),
    CameraPitchVolume(0x69, "mp1/script/CameraPitchVolume.xml"),
    EnvFxDensityController(0x6A, "mp1/script/EnvFxDensityController.xml"),
    Magdolite(0x6B, "mp1/script/Magdolite.xml"),
    TeamAIMgr(0x6C, "mp1/script/TeamAIMgr.xml"),
    SnakeWeedSwarm(0x6D, "mp1/script/SnakeWeedSwarm.xml"),
    ActorContraption(0x6E, "mp1/script/ActorContraption.xml"),
    Oculus(0x6F, "mp1/script/Oculus.xml"),
    Geemer(0x70, "mp1/script/Geemer.xml"),
    SpindleCamera(0x71, "mp1/script/SpindleCamera.xml"),
    AtomicAlpha(0x72, "mp1/script/AtomicAlpha.xml"),
    CameraHintTrigger(0x73, "mp1/script/CameraHintTrigger.xml"),
    RumbleEffect(0x74, "mp1/script/RumbleEffect.xml"),
    AmbientAI(0x75, "mp1/script/AmbientAI.xml"),
    AtomicBeta(0x77, "mp1/script/AtomicBeta.xml"),
    IceZoomer(0x78, "mp1/script/IceZoomer.xml"),
    Puffer(0x79, "mp1/script/Puffer.xml"),
    Tryclops(0x7A, "mp1/script/Tryclops.xml"),
    Ridley(0x7B, "mp1/script/Ridley.xml"),
    Seedling(0x7C, "mp1/script/Seedling.xml"),
    ThermalHeatFader(0x7D, "mp1/script/ThermalHeatFader.xml"),
    Burrower(0x7F, "mp1/script/Burrower.xml"),
    ScriptBeam(0x81, "mp1/script/ScriptBeam.xml"),
    WorldLightFader(0x82, "mp1/script/WorldLightFader.xml"),
    MetroidPrimeStage2(0x83, "mp1/script/MetroidPrimeStage2.xml"),
    MetroidPrimeStage1(0x84, "mp1/script/MetroidPrimeStage1.xml"),
    MazeNode(0x85, "mp1/script/MazeNode.xml"),
    OmegaPirate(0x86, "mp1/script/OmegaPirate.xml"),
    PhazonPool(0x87, "mp1/script/PhazonPool.xml"),
    PhazonHealingNodule(0x88, "mp1/script/PhazonHealingNodule.xml"),
    NewCameraShaker(0x89, "mp1/script/NewCameraShaker.xml"),
    ShadowProjector(0x8A, "mp1/script/ShadowProjector.xml"),
    EnergyBall(0x8B, "mp1/script/EnergyBall.xml");

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
