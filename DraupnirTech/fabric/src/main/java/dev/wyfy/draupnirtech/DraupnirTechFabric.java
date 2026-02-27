package dev.wyfy.draupnirtech;

import net.fabricmc.api.ModInitializer;

public class DraupnirTechFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        // Use Fabric to bootstrap the Common mod.
        CommonClass.init();
    }
}
