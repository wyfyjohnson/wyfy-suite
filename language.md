- Imports expose the target node's available capabilities to the current leaf.
- Same syntax regardless of whether the target is local or cross-dimension.
- Cross-dimension requires a Dimensional Link Block on both nodes — this is a hardware
  concern, not a language concern.
- Import failure (node not found, out of range, no link block) = **compiler error** with a
  specific, named message. The leaf will not run.

### Triggers (Reactivity)
- All leaf behavior is **trigger-driven**. A leaf does nothing until a trigger fires.
- A trigger describes a condition: "when redstone signal received", "when storage contains
  X items", "when time of day is Y", etc.
- When the trigger fires, the associated job runs once to completion.
- There are no background daemons, no polling loops, no persistent watchers at the language
  level. Continuous behavior is modeled as a repeating trigger (e.g. "every N seconds"),
  not as a daemon process.
- This keeps the mental model simple: **triggers fire, jobs run, results appear.**

### Lifecycle
- A leaf is **inactive** by default when first loaded.
- A leaf becomes **active** when the node it is attached to is powered (e.g. redstone,
  manual activation from the terminal GUI, or another leaf's output).
- A leaf is **suspended** when the node loses power or resources — it does not error, it
  simply waits.
- A leaf is **torn down** when the multiblock is dismantled. Teardown is clean: no partial
  state, no orphaned jobs.
- Lifecycle transitions surface in the terminal GUI with a plain-language status message.

### Jobs
- A job is a unit of work declared inside a leaf. It runs when its trigger fires.
- Jobs are **atomic**: either the full job completes or nothing changes. No partial output.
- Jobs are **sequential by default**: steps run top-to-bottom in the order they are written.
- Explicit dependency declarations are not required for basic use. Advanced users may
  declare ordering constraints explicitly — this is unsettled (see below).

### Chaining
- Operations chain sequentially within a job (e.g. grind → smelt → store).
- Each step receives the output of the previous step as its input.
- Ordering is **implicit top-to-bottom** unless an explicit dependency is declared.
- If any step in a chain fails, the whole job is rolled back (atomicity applies).

### Schematic-to-Leaf Reproducibility
- A multiblock build can be captured as a **pattern** using an in-world tool (analogous to
  Create's schematic cannon mechanic).
- Capturing a pattern automatically generates a `leaf.vanir` file that describes:
    - The structure layout (block positions, orientations, relative coordinates).
    - Required components (machines, connectors, multiblock blocks).
    - Any automation logic that was active at capture time.
- The generation process is **deterministic and idempotent**: capturing the same structure
  always produces bit-for-bit identical leaf files.
- The generated leaf is portable and can be shared between worlds or players.
- If any block in the structure cannot be represented in the leaf schema (unsupported mod
  blocks, unknown block states), the capture fails with a **compiler error** naming the
  specific block. No partial leaf is written.

### Tiering
- Tier constraints are enforced at runtime by the hardware, not at parse time.
- Writing a higher-tier argument is valid syntax. Running it on insufficient hardware is a
  **runtime error**.
- The error message must name the required tier and the installed tier explicitly.

### Error Handling
- All errors surface in the terminal GUI with plain-language messages.
- Every error must be specific: name the missing module, the required tier, the unreachable
  node, the invalid configuration, or the uncapturable block.
- Silent failures are never acceptable.
- The distinction between **compiler errors** (leaf won't load) and **runtime errors** (leaf
  loaded but failed during execution) must be consistent and explicit in all error messages.

---

## Unsettled Semantics

### Recipe Definition
**Sub-question A — Declaration model:**  
How does a player write a processing or crafting recipe inline in a leaf? Should it look like
naming inputs and outputs, or referencing a named recipe from the mod's registry?

**Sub-question B — Ingredient sourcing:**  
Can a recipe pull ingredients from another node's storage by reference (e.g. "use iron ore
stored in `orequarry`")? If so, does that make the recipe declaration an implicit dependency
on that node being active?

### Conditionals and Control Flow
- Does Vanir support if/else or match-style constructs?
- If so, how does a player write "if I don't have enough iron, skip this job instead of
  erroring"?
- How does error recovery work mid-chain? Options: stop and report, skip the step, retry
  with a delay.

### Advanced Ordering
- For complex multi-node automation, is top-to-bottom ordering always sufficient?
- If not, what does explicit dependency declaration look like semantically?
- This is only relevant for advanced use — simple cases should never need it.

### Storage Queries
- What does reading from an imported node's storage look like semantically?
- Can a player filter by item type, tag, count, or NBT data?
- Can a storage query be used as a trigger condition (e.g. "when `orequarry` has more than
  64 iron ore, run this job")?

---

## Hard Constraints

These are fixed rules the agent must never violate when designing or extending Vanir.

- Vanir is not Turing-complete. No general-purpose loops, recursion, or arbitrary computation.
- No version pinning or lock files. In-world state is the source of truth.
- A leaf is a coordination layer only. It never owns, drives, or configures machines directly.
- Basic automation must be achievable without programming knowledge. Complexity is always
  opt-in.
- The language is domain-specific to Minecraft automation. It is not a general-purpose
  scripting language and should never be designed toward one.
- Vanir's model is inspired by Nix's pure, declarative semantics but must remain grounded
  in what a Minecraft player would find intuitive.
