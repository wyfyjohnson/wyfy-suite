# Project Vision — IvaldiOS Mod Suite

## Overview

A suite of Minecraft mods built around a declarative, expression-driven compute system. The player writes configuration expressions to define what their machines do, rather than manually operating them. Inspired by NixOS's philosophy of describing desired state and letting the system resolve how to achieve it. Targeting deep Create integration from the ground up. Built to run on Forge, NeoForge, and Fabric — platform abstraction is handled at the library level so content mods are written once.

The suite is composed of **LibWyfy** (the shared library) and multiple content mods that depend on it. This structure allows each content mod to be smaller in scope while the library carries all shared infrastructure — similar to how McJty structures the RFTools ecosystem.

---

## LibWyfy — The Library Mod

The foundation everything else builds on. No gameplay content of its own — purely infrastructure for dependent mods.

### Platform Abstraction
- Targets Forge, NeoForge, and Fabric
- All platform-specific code lives in the library — content mods are written once and run on all three
- Create integration handled per-platform within the library, transparent to content mods

### Multiblock Framework
- Structure validation and formation system
- Module registration API so content mods can expose new hardware capabilities
- Module-to-language binding — registered modules expose functions and arguments to the expression language
- Flake storage, loading, and linking across multiple compute nodes
- Expression compiler and evaluator (see Language section)

### Power Infrastructure
- Active job power spiking above idle baseline
- Energy storage API with insertable battery item support for capacity expansion
- Create SU/RPM bridge — bidirectional SU interface block that exports or imports depending on the node's current power state

### Storage Infrastructure
- Drive/disk item system for capacity scaling (no enormous storage blocks)

### Item and Fluid Handling
- Generic interface blocks compatible with Create chutes, funnels, and tunnels
- Capacity scaling through insertable upgrade items per module
- Filter support for item and fluid interfaces

### GUI Framework
- Terminal text editor component (reusable across all mods in the suite)
- In-world schematic preview system when using builder
- Compiler error display in terminal
- Ponder integration — in-game animated scene support for all content mods in the suite. Ponder is pulled in as a standalone library dependency so Create is not required to use it. All major mechanics get dedicated Ponder scenes. Content mods register their own scenes through LibWyfy's Ponder API.

### Recipe and Crafting Infrastructure
- Flake-defined recipes — crafting jobs declared in expressions rather than pattern grids
- Recipe registration API for content mods

---

## The Expression Language

The language a player writes to define what their compute node does. Design goals: approachable, looks like a blend of Nix and Python, structured but readable. Syntax to be finalized after all semantics are established.

### Core Concepts

**Flake (name TBD)** — A saved expression file that lives within the multiblock structure itself. The file, the flake, the named node, and the physical multiblock are all paired under one name — set once when the structure is named and never changed. All flakes are stored in a shared mod directory on disk, mimicking how Create handles schematics — browsable, backupable, and portable across worlds. The terminal always shows the current flake and it is always editable in-place.

**Modules** — Hardware blocks attached to the multiblock. Each module installed on a structure exposes new functions and arguments in the language. Missing hardware = compiler error, not silent failure.

**Named Nodes** — Each compute node the player builds is given a name when placed. That name is permanent — it ties together the physical structure, the flake file on disk, and the hostname used in imports. One flake can reference and manage multiple named nodes, similar to how a NixOS flake manages multiple host configurations.

### Imports

Importing another node is done by hostname only. No complex path syntax, no namespacing — just the name of the target node.

```
import multiBlock_0
```

This single line exposes whatever `multiBlock_0` has available — its storage, its processing capabilities, anything its modules provide — to the current node's flake. The two structures handle the handshake; the player just uses the name.

**Distance and Dimensions**

Within a dimension, imports work up to a set block range. No special hardware required — the structures communicate locally as long as they are within range.

Cross-dimension imports require a Dimensional Link Block installed on both structures — one acts as the transmitter, the other as the receiver. This block is expensive to craft and represents a meaningful progression gate. Once installed on both ends, the import works identically to a local import. The syntax never changes — `import multiBlock_0` is `import multiBlock_0` whether the target is 20 blocks away or in the Nether. The hardware handles the rest transparently.

### Reference Workflow — Starter Base to Mega Base

The player returns from mining and deposits raw resources into their storage/processing node (`multiBlock_0`). The node's flake handles ore processing automatically — pulling from the input chest, grinding, smelting, and storing the outputs.

The player then walks to their builder node (`multiBlock_1`). They open the terminal. If a flake is already written, they run it. If not, they write it now — the terminal is always editable. The flake imports `multiBlock_0` to access stored processed materials, then instructs the builder module to place the schematic for the mega base, consuming items from storage as it builds.

```
# Example — placeholder syntax, final syntax TBD
import multiBlock_0

build {
  schematic = "mega_base"
  materials = multiBlock_0.storage
}
```

### Tiering Through Arguments

Tiers are enforced at runtime by the hardware, not by locking the player out of writing expressions. A tier 1 grinder module only validates `intensity = 1`. You can write `intensity = 3` — you just can't run it until the hardware supports it. The terminal tells you exactly why.

### Capacity Scaling

Capacity for fluids, power buffers, and storage is expanded through items inserted into module slots — not by making the multiblock larger. The structure stays a reasonable size. Complexity scales through items.

---

## IvaldiOS — Primary Content Mod (Name Tentative)

The first content mod. Self-sufficient — does not require any external tech mod to function.

### The Compute Node Multiblock

One machine type that serves many roles depending on installed modules and its active flake. A player can run one node that handles everything, or distribute several specialized nodes throughout their base. Both are valid playstyles.

Roles a single node can fill:

- **Ore processing** — grind, catalyze, smelt, produce byproducts
- **Storage network** — AE2/Refined Storage style item and fluid storage via drive items
- **Base builder** — schematic-driven construction (RFTools Builder / Create Schematicannon style)
- **Base designer** — text editor for building schematics, live in-world preview via GUI
- **Automated crafting** — recipes defined in the flake, executed on demand or reactively
- **Automation hub** — pulling, filtering, routing, and processing items and fluids across the base

### Module Types (Planned)

**Grinder Module** — Exposes ore processing functions. Fluid catalyst slot for output multipliers. Tiered for intensity. Insertable fluid cell items for capacity.

**Smelter Module** — Exposes smelting functions. Can be chained with grinder in a single expression for full pipeline processing.

**Inventory Interface Module** — Exposes pull/push functions. Compatible with Create chutes and funnels natively.

**Fluid Interface Module** — Exposes drain/fill functions. Compatible with Create fluid pipes natively.

**Drive Bay Module** — Storage network node. Accepts drive items for capacity.

**Builder Module** — Exposes schematic placement and construction functions. Consumes items from storage.

**Generator Module** — Power production. Fuel input produces internal energy. Can also accept Create SU as input.

**SU Interface Block** — Bidirectional Create power integration. Exports SU when the node has excess power, imports SU when the node needs more than it generates internally.

**Crafting Module** — Executes recipes defined in the active flake. No pattern grid required.

**Dimensional Link Block** — Installed on two nodes to enable cross-dimension imports. Expensive to craft. Both structures require one — transmitter and receiver. Once installed, cross-dimension imports are syntactically identical to local ones.

### Power Model

Always-on idle draw like AE2. Scales with number of installed modules. Active jobs spike above idle. Power buffer expanded through insertable battery items. Create SU bridge available from the start as an alternative or supplementary power source.

### Create Integration

- Chutes, funnels, and tunnels interact with interface blocks natively — Create handles all item logistics if the player prefers
- Fluid pipes work with fluid interface blocks
- SU interface block handles rotational power in both directions
- RPM and SU mechanics handled properly through LibWyfy infrastructure

---

## Future Content Mods (Planned)

### DraupnirTech — GregTech-Inspired Nuclear Power Generation
A separate content mod using LibWyfy. Heavy power generation focused on complex fuel chains and multiblock reactor designs. Hooks into LibWyfy's power infrastructure, multiblock framework, and Create integration without reimplementing any of it.

Additional mods in the suite follow the same pattern — register into LibWyfy, focus purely on content.

---

## Progression Arc

### Early Game
Craft a basic controller and a couple of starter modules. Write a simple expression to accomplish something — for example, ore processing all the raw ores from a chest the player deposits into. Power it with a basic generator. The terminal and expression language are the centerpiece and should be approachable and intuitive from the first interaction.

### Mid Game
Expand the multiblock with a storage module and drive items. Begin routing with Create. Write flakes that define crafting recipes. Name compute nodes and start importing between them within range. Introduce fluid catalysts for higher ore processing yields. Begin building schematics with the builder module pulling from local storage.

### Later Game
Schematic builder running unattended construction. Complex multi-step processing pipelines in a single expression. Cross-dimension imports via the Dimensional Link Block, linking nodes across the Overworld, Nether, and End. Power network balancing between multiple named compute nodes. Full base automation — the player writes the configuration, the system executes it.

---

## Design Principles

- **Ponder-first documentation** — Every major mechanic has an in-game Ponder scene. No wiki required to understand the basics.
- **Write once, run anywhere** — Forge, NeoForge, and Fabric are all supported. Platform abstraction lives entirely in LibWyfy so content mods never touch platform-specific code.
- **Self-sufficient** — No external tech mod required. Power, storage, processing, and logistics all exist within the suite.
- **One machine, many roles** — The compute node is not a grinder or a storage system. It is a compute node. Modules define what it does.
- **Hardware is the package manager** — You cannot run what you haven't installed. Missing modules produce compiler errors, not silent failures.
- **Name is identity** — The multiblock name, flake file, and node identifier are one and the same. Set once, never changed. No drift between what's on disk and what's in the world.
- **Imports are just hostnames** — `import multiBlock_0` is all the player writes. Local or cross-dimension, the syntax is identical. The hardware handles the complexity.
- **Reproducible builds** — Name your node, write your flake, replicate your setup anywhere with matching hardware.
- **Capacity through items, not blocks** — Upgrade items scale capacity. The structure stays usable.
- **Library-first** — All shared infrastructure lives in LibWyfy. Content mods are thin layers on top.
- **Create is a first-class citizen** — Not an afterthought integration. Create logistics and power work natively.

---

## Open Questions

- Final naming for the internal energy unit
- Final name for IvaldiOS (tentative, not settled)
- Final expression language syntax (deferred until semantics are fully settled)
- Exact multiblock size constraints and module slot counts
- Storage network scope — per-node or networked across multiple nodes via imports?
- Exact block range for local same-dimension imports
