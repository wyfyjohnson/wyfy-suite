# Architecture Rules

## LibWyfy vs Content Mod Boundaries

LibWyfy owns:
- Multiblock validation, formation, and module registration API
- Expression language compiler, evaluator, and runtime
- Module-to-language binding system
- Power infrastructure (internal energy unit, idle draw system, Create SU/RPM bridge)
- Storage infrastructure (drive/disk item system, typed buffer API, network query API)
- Item and fluid handling abstractions (interface block base classes, filter system)
- GUI framework (terminal text editor, schematic preview, upgrade slot component, Ponder integration)
- Recipe and crafting infrastructure
- Cross-node import resolution and dimensional link system
- Flake file I/O and directory management

IvaldiOS owns:
- Concrete module block implementations (grinder, smelter, inventory interface, fluid interface, drive bay, builder, generator, SU interface, dimensional link, crafting)
- All textures, models, blockstates
- All item definitions (drive items, battery items, fluid cell items, upgrade cards)
- All recipes (JSON or data-gen)
- All Ponder scene registrations (using LibWyfy's Ponder API)
- Language binding registrations for each module

DraupnirTech owns:
- All nuclear power generation content
- Its own module registrations into LibWyfy
- Its own Ponder scenes
- Does not depend on IvaldiOS — only on LibWyfy

## Platform Abstraction Pattern

All platform-specific code must go through a service interface defined in `common/`.

```
common/
  platform/
    PlatformHelper.java         # Interface
forge/
  platform/
    ForgePlatformHelper.java    # Forge impl
neoforge/
  platform/
    NeoForgePlatformHelper.java
fabric/
  platform/
    FabricPlatformHelper.java
```

Never call Forge/NeoForge/Fabric APIs directly from `common/`. Always go through the helper.

## Create Integration

- Create SU/RPM handling lives in LibWyfy behind a feature flag — graceful no-op if Create is absent
- Check `PlatformHelper.isModLoaded("create")` before registering Create compat
- Create Fabric (`createfabric`) and Create for Forge/NeoForge have different package paths — abstract behind LibWyfy's SU bridge

## Multiblock Module Registration

Modules register into LibWyfy via the module registry during mod init.

```java
// In IvaldiOS mod init
LibWyfyAPI.modules().register(
    new ResourceLocation("ivaldios", "grinder"),
    GrinderModule.INSTANCE
);
```

Each registered module declares:
- What structure blocks it requires
- What language functions/arguments it exposes
- What capability type it provides (PROCESSING, STORAGE, LOGISTICS, POWER, BUILD, LINK)

## Energy

- Internal unit name TBD — use `IEnergy` interface and `EnergyUnit` placeholder throughout until named
- Never import RF/FE energy types into common code
- Create SU conversion factor is configurable, not hardcoded
