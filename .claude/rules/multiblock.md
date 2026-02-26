# Multiblock Rules

## Structure

- Target size: 3x3x3 to 5x5x5 — do not design modules that force larger structures
- The controller block is the identity anchor — it holds the node name and flake reference
- Modules are additional blocks bolted onto the multiblock structure
- Capacity scales through items inserted into module slots, not by adding more blocks

## Formation

- Structure validation happens in LibWyfy's multiblock framework
- Formation is triggered by the player naming the controller block — this sets the name permanently
- Invalid structure → specific error message indicating what is wrong and where
- Partial structures do not activate — all or nothing

## Naming

- The name given at formation is permanent — no rename operation
- Name is shared between: the physical controller block, the flake file on disk, and the import identifier
- LibWyfy enforces this at formation time

## Upgrades

- Each module block has item slots for upgrade/capacity items
- Upgrade items are defined in the content mod (IvaldiOS, DraupnirTech)
- LibWyfy provides the generic upgrade slot container that modules use
- Speed, energy buffer, fluid capacity, and storage capacity are all upgrade-item based — not additional blocks

## Module Capability Types

Each module must declare exactly one capability type:

- `PROCESSING` — grinder, smelter, crafting
- `STORAGE` — drive bay
- `LOGISTICS` — inventory interface, fluid interface
- `POWER` — generator, SU interface
- `BUILD` — builder
- `LINK` — dimensional link

A single compute node can have multiple modules of compatible types attached simultaneously.
Conflicting or duplicate modules are caught at formation validation.
