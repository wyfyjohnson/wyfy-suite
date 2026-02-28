package dev.wyfy.libwyfy;

import net.fabricmc.api.ModInitializer;

public class LibWyfyFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        // Use Fabric to bootstrap the Common mod.
        LibWyfy.init();
    }
}
