# Multiblock Rules

## Structure

- The controller block is the identity anchor — it holds the node name and flake reference
- Modules are additional blocks for the multiblock
- Capacity scales through items inserted into module slots, not by adding more blocks

## Formation

- Structure validation happens in LibWyfy's multiblock framework
- Formation is triggered by the player naming the controller block — this sets the name until the user decides to change it
- Blocks must be directly adjacent to each other forming any shape

## Naming

- The name given to the structure is available on the player's team network, working with FTB teams and other mods like it. So players can be part of the same team and have access to the named structures.
- Name is shared between: the physical controller block, the flake file on disk, and the import identifier

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
- `Buider` — builder block, allows visualization, a flake will be build instructions, very similar to schematics and the cannon with Create.
- `` — a block that allows this multiblock structure to interact with another multiblock structures, dimensional link
- `` - a block that allows this multiblock structure to interact with another multiblock structure within the same dimension
- `PanelBlock` - a block that adds to the multiblock structure for aesthetics and to extend the multiblock structure  

- Augment machine:
- `BaseTransformer` - a block with a UI and inventory, the model has a different appearance based on the amount of upgrades in it. This block being part of multiblock allows for processing blocks to work at an accelerated rate, must be configured via the coding language being developed. This is where a "flake" like thing be loaded.
## Power mechanic

This should be very close Create's Stress Unit mechanic but in regards to electric-like energy:

```Available WE = Total SU × Conversion Rate
Machine Speed = Base Performance(Tier/Voltage) × RPM
Work Consumption = Operations × Work Cost (time-independent)
Network Load = Idle Load + Active Consumption

// Overstress check (like Create)
if (NetworkLoad > AvailableWE) {
// All machines stop working
allMachines.forEach(machine -> machine.stop());
}

// Network load calculation
NetworkLoad = idleMachines.stream()
.mapToInt(Machine::getIdleLoad)
.sum()
+ activeMachines.stream()
.mapToInt(Machine::getActiveConsumption)
.sum();

// smelting example with Work-based model
int stoneToSmelt = 64;
WE_consumed = 1 x SMELT_COST x 64 = 64 WE
WE_consumed = 64 x SMELT_COST x 1 = 64 WE

// Speed/Time Calculation, time to complete work
float speedMultiplier = machineTier x receivedRPM;
float timeToComplete = baseTime/ speedMultiplier;

A single compute node can have multiple modules of compatible types attached simultaneously.
Conflicting or duplicate modules are caught at formation validation.
