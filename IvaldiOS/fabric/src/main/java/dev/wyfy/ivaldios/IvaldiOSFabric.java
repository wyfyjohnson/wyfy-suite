package dev.wyfy.ivaldios;

import net.fabricmc.api.ModInitializer;

public class ExampleMod implements ModInitializer {

    @Override
    public void onInitialize() {
        // Use Fabric to bootstrap the Common mod.
        CommonClass.init();
    }
}
