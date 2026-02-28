package dev.wyfy.libwyfy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {

    public static final String MOD_ID = "libwyfy";
    public static final String MOD_NAME = "LibWyfy";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static final double SU_TO_WE_RATIO = 0.1;
    public static final int MIN_MACHINE_RPM = 32;
    public static final String ENERGY_UNIT_NAME = "WE"; // in essence, efficency/machine speed
}
