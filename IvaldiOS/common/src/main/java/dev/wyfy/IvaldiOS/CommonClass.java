package dev.wyfy.ivaldios;

import dev.wyfy.ivaldios.platform.Services;

public class CommonClass {

    // The loader specific projects are able to import and use any code from the common project. This allows you to
    // write the majority of your code here and load it from your loader specific projects. This example has some
    // code that gets invoked by the entry point of the loader specific projects.
    public static void init() {
        IvaldiOS.LOG.info(
            "IvaldiOS has been initialized on {}!",
            Services.PLATFORM.getPlatformName()
        );
    }
}
