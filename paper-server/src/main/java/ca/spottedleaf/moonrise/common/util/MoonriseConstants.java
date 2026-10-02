package ca.spottedleaf.moonrise.common.util;

import ca.spottedleaf.concurrentutil.numa.OSNuma;
import ca.spottedleaf.moonrise.common.PlatformHooks;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public final class MoonriseConstants {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final int MAX_VIEW_DISTANCE = Integer.getInteger(PlatformHooks.get().getBrand() + ".MaxViewDistance", 32);
    public static final boolean NUMA_ENABLE;
    static {
        final boolean numaScheduling = Boolean.getBoolean(PlatformHooks.get().getBrand() + ".NumaScheduling");
        if (!numaScheduling) {
            NUMA_ENABLE = false;
            if (OSNuma.getNativeInstance().isAvailable()) {
                LOGGER.info("NUMA enable flag is not set; however there is NUMA interaction support for this OS.");
                LOGGER.info("Detected " + OSNuma.getNativeInstance().getTotalNumaNodes() + " NUMA nodes.");
            }
        } else {
            final OSNuma numa = OSNuma.getNativeInstance();
            if (!numa.isAvailable()) {
                NUMA_ENABLE = false;
                LOGGER.info("NUMA enable flag is set; however there is no NUMA interaction support for this OS.");
            } else {
                NUMA_ENABLE = true;

                final int totalNodes = numa.getTotalNumaNodes();
                LOGGER.info("NUMA enable flag is set. Detected " + totalNodes + " NUMA nodes, " + numa.getTotalCores() + " virtual cores.");
            }
        }
    }

    private MoonriseConstants() {}
}
