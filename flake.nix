{
  description = "wyfy-suite — dev environment";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs =
    {
      self,
      nixpkgs,
      flake-utils,
    }:
    flake-utils.lib.eachDefaultSystem (
      system:
      let
        pkgs = import nixpkgs {
          inherit system;
          config.allowUnfreePredicate =
            pkg:
            builtins.elem (pkg.pname or "") [
              "claude-code"
            ];
        };
      in
      {
        devShells.default = pkgs.mkShell {
          buildInputs = with pkgs; [
            claude-code
            claude-monitor
            jdk21
            gradle
            git
          ];

          JAVA_HOME = "${pkgs.jdk21}";

          shellHook = ''
            echo "wyfy-suite dev environment"
          '';
        };
      }
    );
}
