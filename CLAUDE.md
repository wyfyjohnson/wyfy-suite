# wyfy-suite Mod Suite by Wyfy

Declarative, expression-driven Minecraft mod suite inspired by NixOS. The player writes configuration expressions to define machine behavior rather than operating machines manually.

## Teaching Mode

A structured Java curriculum for this project lives in `.claude/TEACHING.md`.

When the developer asks to work on the lesson plan, wants to learn a concept, or says something
like "teach me" or "where are we in the lesson" — read `.claude/TEACHING.md` and follow the
teaching instructions there. Do not summarize the lesson plan back at them. Begin teaching.

The curriculum progresses through 8 phases, each building on the last, all grounded in this
codebase. Do not skip phases unless the student demonstrates they already know the material.

## Project Rules

See `.claude/rules/` for multiblock, energy framework, and other design rules.

## Mods in This Repo

- **LibWyfy** — Shared library. No gameplay content. All platform abstraction, multiblock framework, power/storage infrastructure, expression language runtime, and Ponder integration live here.
- **IvaldiOS** — Primary content mod. Depends on LibWyfy. Registers the compute node multiblock, modules, items, and blocks.
- **DraupnirTech** - A very arduous crafting and power generation mod.

## Project Structure

```
├── CLAUDE.md
├── .claude/
│   ├── rules/               # Full project vision
│   └── teaching.md   # Expression language design (WIP)
├── libwyfy/
│   ├── common/                 # Platform-agnostic code
│   ├── forge/                  # Forge-specific stubs
│   ├── neoforge/               # NeoForge-specific stubs
│   └── fabric/                 # Fabric-specific stubs
└── ivaldios/
    ├── common/
    ├── forge/
    ├── neoforge/
    └── fabric/
```

## Build Commands

```bash
# Build all
./gradlew build

# Build specific platform
./gradlew :libwyfy:forge:build
./gradlew :ivaldios:neoforge:build

# Run tests
./gradlew test

# Run game (NeoForge dev env)
./gradlew :ivaldios:neoforge:runClient
```

## Architecture Rules

- **Platform-specific code NEVER goes in `common/`** — use capability/service abstractions and implement per platform.
- **Content NEVER goes in LibWyfy** — if it has a texture, recipe, or loot table it belongs in a content mod.
- **IvaldiOS registers into LibWyfy's APIs** — it does not reimplement power, multiblock, or storage logic.
- **Expression language runtime lives entirely in LibWyfy** — IvaldiOS only registers module-to-language bindings.

See .claude/rules/architecture.md for detailed rules.

## Key Concepts

- **Compute Node** — The single multiblock type in IvaldiOS. Its behavior is defined by attached modules and its active leaf.
- **Module** — A hardware block attached to the multiblock that exposes new functions/arguments to the expression language.
- **Leaf** — The expression file tied to a compute node. Named identically to the node. Stored in the mod's schematics-style directory. Never renamed.
- **Import** — `import <nodeName>` at the top of a leaf. Hostname-only, no path syntax. Same syntax regardless of distance or dimension.
- **Dimensional Link Block** — Required on both nodes for cross-dimension imports. Hardware handles the complexity; syntax stays identical.

## Naming Conventions

- Classes: `PascalCase`
- Fields/methods: `camelCase`
- Constants: `UPPER_SNAKE_CASE`
- Registry keys: `snake_case`
- Packages: `dev.wyfy.libwyfy.*` / `dev.wyfy.ivaldios.*`

## Do Not

- Do not hardcode platform checks in `common/` — use the platform service layer
- Do not add crafting recipes to LibWyfy
