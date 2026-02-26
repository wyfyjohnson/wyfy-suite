# Expression Language Semantics

## Status

Work in progress. Syntax is explicitly deferred — this document tracks semantic decisions only.
Do not implement syntax until this document reaches a stable state.

---

## Settled Semantics

### Import System
- `import <nodeName>` — hostname only, no path, no namespacing
- Must appear at the top of the file before any other statements
- Imports expose the target node's available capabilities to the current flake
- Same syntax regardless of whether the target is local or cross-dimension
- Cross-dimension requires Dimensional Link Block on both nodes — hardware concern, not language concern
- Import failure (node not found, out of range, no link block) = compiler error with specific message

### Node Naming
- Node name is set once when the multiblock is first named
- Node name = flake filename = import identifier — always identical, always in sync
- Names are permanent — no rename operation exists by design

### Tiering
- Tier constraints are enforced at runtime by the hardware, not at parse time
- Writing a higher-tier argument is valid syntax — running it on insufficient hardware is a runtime error
- Error message must specify what tier is required vs what is installed

### Atomicity
- Failed jobs produce no partial output
- Either the full job completes or nothing changes

### Error Handling
- All errors surface in the terminal GUI
- Errors must be specific: name the missing module, the required tier, the unreachable node
- Silent failures are never acceptable

---

## Unsettled Semantics

### Reactivity
- Should expressions be job-based (run once when triggered) or daemon-like (watch for conditions)?
- Or both, with different declaration syntax?

### Recipe Definition
- How are crafting recipes declared inline in a flake?
- Can a recipe reference another node's storage as an ingredient source?

### Conditionals and Control Flow
- Does the language support if/else or pattern matching?
- How does error recovery work mid-pipeline?

### Chaining
- Confirmed: operations can be chained sequentially (grind then smelt in one expression)
- How is ordering enforced — implicit top-to-bottom, or explicit dependency declaration?

### Storage Queries
- What does querying an imported node's storage look like semantically?
- Can you filter by item tag, count, NBT?

---

## Non-Goals

- The language is not Turing-complete by design
- No general-purpose loops or recursion
- Expressiveness for automation is the goal, not a scripting language
