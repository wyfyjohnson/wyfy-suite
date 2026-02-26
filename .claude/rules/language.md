# Expression Language Rules

## Status

Semantics are being designed. Syntax is explicitly deferred.
See @docs/language-semantics.md for current design state.
Do not finalize or implement syntax until semantics are fully settled.

## What the Language Must Support

- `import <nodeName>` — hostname-only, top of file, no path syntax
- Module function calls with named arguments
- Tiered argument validation against installed hardware at runtime
- Chaining operations sequentially within a single expression (e.g. grind then smelt)
- Accessing imported node's storage and capabilities
- Recipe definitions inline in the flake
- Reactive/conditional logic (design TBD — see language-semantics.md)

## Compiler Behavior

- Missing module → compiler error: "grinder: module not installed"
- Hardware tier too low → runtime error: "intensity = 3 requires tier 3 grinder, installed: tier 1"
- Import target not found or out of range → compiler error with node name and specific reason
- No Dimensional Link Block for cross-dimension import → compiler error
- Failed jobs produce NO partial output — atomicity is required
- All errors must be displayed in the terminal GUI, not just logged to console

## Runtime

- The evaluator lives in LibWyfy — `FlakeEvaluator` class
- Module bindings are registered at startup and looked up by the evaluator at runtime
- Flake files stored at `<world>/libwyfy/flakes/<nodeName>.flake` (extension TBD)
- Flake name = node name = filename — enforced on node creation, never changes

## Do Not

- Do not implement syntax before semantics are finalized
- Do not make the language Turing-complete as a goal
- Do not silently ignore unknown functions or arguments — always error
- Do not allow partial job output on failure
