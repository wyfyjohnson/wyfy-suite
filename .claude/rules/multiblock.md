# Multiblock Rules

## Structure

- The controller block is the identity anchor — it holds the node name and leaf reference
- Modules are additional blocks for the multiblock
- Capacity scales through items inserted into module slots, not by adding more blocks

## Formation

- Structure validation happens in LibWyfy's multiblock framework
- Formation is triggered by the player naming the controller block — this sets the name until the user decides to change it
- Blocks must be directly adjacent to each other forming any shape

## Naming

- The name given to the structure is available on the player's team network, working with FTB teams and other mods like it. So players can be part of the same team and have access to the named structures.
- Name is shared between: the physical controller block, the leaf file on disk, and the import identifier

## Upgrades

- Each module block has item slots for upgrade/capacity items
- Upgrade items are defined in the content mod (IvaldiOS, DraupnirTech)
- LibWyfy provides the generic upgrade slot container that modules use
- Speed, energy buffer, fluid capacity, and storage capacity are all upgrade-item based — not additional blocks

## Core Machines

Each core machine must have a UI:

- `MechanizedGrinder` — grinder block that processes raw ore, ore blocks, other grindable items like wheat. Uses power and a fluid to further process the items. (ie. wheat, ground with water provides dough)
- `MechanizedFurnace` - smelts ores and items to produce ingots, blocks and alloys 
- `StorageController` — this block will be similar to the drive bay of AE2, where items can be inserted and it will show visually
- `ItemIOBlock` - a block that can push/pull items from an inventory, works with Create packagers, inventory interface/fluid interface
- `BasicGenerator` — Generates energy used by the machine/multiblock
- `BushingBlock` - a block that accepts and sends stress units. When receiving SU, this block generates power to be used for the power mechanics, processing, building, etc.
- `Buider` — builder block, allows visualization, a leaf will be build instructions, very similar to schematics and the cannon with Create.
- `` — a block that allows this multiblock structure to interact with another multiblock structures, dimensional link
- `` - a block that allows this multiblock structure to interact with another multiblock structure within the same dimension
- `PanelBlock` - a block that adds to the multiblock structure for aesthetics and to extend the multiblock structure  
- `MechanicalBushing` - a block

- Augment machine:
- `BaseTransformer` - a block with a UI and inventory, the model has a different appearance based on the amount of upgrades in it. This block being part of multiblock allows for processing blocks to work at an accelerated rate, must be configured via the coding language being developed. This is where a leaf can be loaded.
## Energy Framework — BE (Baryonic Energy)

### Overview

BE (Baryonic Energy) is the internal power unit shared across LibWyfy, IvaldiOS, and DraupnirTech. It is a plasma-based energy — the fourth state of matter. It is not transferred over cables or pipes. It is produced, stored, and consumed entirely within the multiblock structure that generates it. LibWyfy owns all contracts and runtime logic. IvaldiOS and DraupnirTech own all implementations.

---

### Core Identity

- BE is produced by ionizing rotational force (SU from Create) or burning fuel
- Once ionized and stored in a Containment Vessel, plasma is stable — no degradation, no passive drain
- Machines draw freely from stored plasma with no penalty of any kind
- Coherence is a property of the generator only — it never touches stored plasma or machine draw

---

### Formulas

    FluxOutput(i)   = RawInput(i) x SU_TO_BE_RATIO x Coherence(i)
    TotalFlux       = Sum of FluxOutput(i) + VesselDrain
    CoherenceDemand = Sum of FieldMaintenanceCost(i) + Sum of ActiveFluxDraw(i)
    IonizationCost  = Operations x CostPerOp
    TimeToComplete  = BaseTime / (MachineTier x RPM)

---

### Constants

Defined in `libwyfy/Constants.java`. Never redefined in content mods.

| Constant         | Value | Purpose                                 |
|------------------|-------|-----------------------------------------|
| SU_TO_BE_RATIO   | 0.1   | Base SU to BE conversion factor         |
| MIN_MACHINE_RPM  | 32    | Minimum RPM before any flux is produced |
| ENERGY_UNIT_NAME | BE    | Display name shown in UI                |

---

### Coherence

Coherence is the ionization efficiency of a generator. It determines how much BE is extracted from a given SU input. It is computed entirely inside the generator's BlockEntity and never exposed to the network.

#### What Drives Coherence

- RPM level — minimum RPM produces roughly 0.4 base coherence, higher RPM approaches 1.0 asymptotically
- RPM stability — fluctuating input lowers coherence
- Upgrade items — CoherenceCrystal items in the generator's upgrade slots add a direct bonus, capped at 1.0

#### BushingBlock Coherence Formula

    baseCoherence  = 0.4 + clamp((SU - MIN_RPM) / 512.0, 0.0, 1.0) x 0.6
    finalCoherence = clamp(baseCoherence + upgradeBonus, 0.0, 1.0)
    BEOutput       = SU x SU_TO_BE_RATIO x finalCoherence

The 512.0 ceiling is a tuning value — revisit once target SU ranges from Create sources are known.

#### Design Intent

Coherence is a reward, not a punishment. A low-RPM water wheel still produces plasma — just less per SU. Better rotational sources and upgrade items push coherence toward 1.0. No network penalty ever fires as a result of generator coherence.

---

### Network Field States

Three states. Informational only — they drive UI and machine callbacks, never penalties.

| State | Condition | Effect |
|---|---|---|
| STABLE | Supply meets or exceeds demand | All machines run normally |
| LOW | Vessels covering a deficit | Machines run, vessels draining |
| EMPTY | No supply and vessels depleted | Machines pause cleanly, retain progress |

#### EMPTY Behavior

- All machines receive onPlasmaEmpty() — they pause at current progress
- No work is lost, no items are consumed, no timer starts
- When supply is restored, machines receive onPlasmaRestored() and continue from where they stopped
- There is no collapse, no recovery timer, no punishment

---

### Generators

#### BushingBlock

Converts Create rotational force into BE. Coherence is computed from RPM and upgrade items. Primary early-game power source.

- Upgrade slots: 2
- Upgrade: CoherenceCrystalItem adds to conversion bonus
- Output: WorkEngine.ionize(rawSU, finalCoherence)

#### BasicGenerator

Burns fuel to produce BE directly. No Create dependency. Coherence fixed at tier baseline.

- Upgrade slots: 2
- Output: flat BE/tick determined by fuel type and generator tier

#### MechanicalBushing

Inverse of BushingBlock. Consumes BE from the network and outputs SU into a Create rotational network. Allows BE multiblock structures to power Create contraptions.

---

### Containment Vessels

Vessels store surplus plasma and cover deficits automatically each tick. Stored plasma carries no coherence value — it is already stable ionized matter.

- Surplus each tick is automatically charged into vessels
- Deficit is automatically drained from vessels before field state is evaluated
- Capacity scales through FluxCapacitor upgrade items
- Whether ContainmentVessel is a standalone block or a module slot item is an open question

---

### Machine Costs

Defined in each BlockEntity class inside IvaldiOS or DraupnirTech. LibWyfy never hard-codes these values.

| Machine | Maintenance (BE/tick) | Active Draw (BE/tick) | Ionization Cost (BE/op) | Tier |
|---|---|---|-------------------------|---|
| MechanizedGrinder | 2 | 8 | 10                      | T1 — 1.0x |
| MechanizedFurnace | 1 | 6 | 8                       | T1 — 1.0x |
| AlloyForge | 3 | 12 | 16                      | T2 — 2.0x |
| FabricationChamber | 4 | 16 | 24                      | T2 — 2.0x |
| BaseTransformer | 2 | 10 | leaf-defined            | upgrade-scaled |
| StorageController | 1 | 0 | 0                       | passive |

---

### Upgrade Items

Defined in IvaldiOS and DraupnirTech. All implement IUpgradeItem from LibWyfy.

| Item | Method | Effect | Used In |
|---|---|---|---|
| CoherenceCrystalItem | getConversionBonus() | Improves ionization efficiency | BushingBlock, BasicGenerator |
| FluxCapacitorItem | getBufferBonus() | Increases containment vessel capacity | ContainmentVessel, StorageController |
| SpeedCrystalItem | getSpeedBonus() | Adds to machine tier multiplier | All processing machines |
| FluidExpansionItem | getFluidCapBonus() | Increases fluid tank capacity | MechanizedGrinder |

---

### Network Tick Flow

Runs server-side each tick per dimension via BENetworkManager.

    1. BENetworkManager.tickAll(dimension, gameTick)
    2.   BENetwork.tick(gameTick)
    3.     rebuildCache() if dirty or every 4 ticks (staggered per network by name hash)
    4.       cachedFluxOutput = sum of generator.getBEOutput()   coherence already applied inside generator
    5.       cachedDemand     = sum of machine maintenance + active draw
    6.     deficit = max(0, demand - fluxOutput)
    7.     drainVessels(deficit)
    8.     chargeVessels(surplus) if supply exceeds demand
    9.     resolveFieldState(available, demand) — STABLE / LOW / EMPTY
    10.    onFieldStateChanged() if state changed — fire machine callbacks

Client sync payload contains only: fluxOutput, demand, fieldState, name. The full network object is never sent over the wire.

---

### Implementation Boundaries

    LibWyfy owns:
      IBEMachine             — machine contract
      IBEGenerator           — generator contract, coherence interface
      IBEBuffer              — containment vessel contract
      BENetwork              — per-multiblock network runtime
      BENetworkManager       — global per-dimension registry
      WorkEngine             — ionize(), timeToComplete(), ionizationCost()
      IUpgradeItem           — upgrade item contract
      UpgradeSlotContainer   — generic upgrade slot manager

    IvaldiOS owns:
      AbstractMachineBlockEntity    — wires IBEMachine into BlockEntity lifecycle
      AbstractGeneratorBlockEntity  — wires IBEGenerator into BlockEntity lifecycle
      BushingBlockEntity            — coherence computation, Create SU interop
      All concrete machine BlockEntities
      All upgrade item definitions

    DraupnirTech owns:
      Its own AbstractMachineBlockEntity mirroring IvaldiOS pattern
      Its own upgrade items implementing IUpgradeItem
      Never imports IvaldiOS classes directly — LibWyfy contracts only

---

### Open Questions

- Final UI terminology for flux, coherence, and field state labels
- Whether ContainmentVessel is a standalone block or always a module-slot item
- Cross-network plasma transfer — should two named nodes on the same team share a vessel pool or remain fully isolated
- Tier 3 machine cost definitions
- Whether MechanicalBushing requires a minimum vessel charge before it will output SU

