package dev.wyfy.ivaldios;

import net.fabricmc.api.ModInitializer;

public class IvaldiOS implements ModInitializer {

    @Override
    public void onInitialize() {
        // Use Fabric to bootstrap the Common mod.
        CommonClass.init();
    }
}