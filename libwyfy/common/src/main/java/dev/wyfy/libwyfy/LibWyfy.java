package dev.wyfy.libwyfy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LibWyfy {

    public static void init() {
        Constants.LOG.info("Initializing {}", Constants.MOD_NAME);

        // MultiblockRegistry.init();
        // LanguageCompiler.init();
        // EnergySystem.init();

        // Register platform services
        // Services.PLATFORM.registerEnergyConversion();

        Constants.LOG.info("{} initialization complete", Constants.MOD_NAME);
    }
}
