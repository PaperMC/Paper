package ca.spottedleaf.moonrise.common.util;

import ca.spottedleaf.moonrise.common.PlatformHooks;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import oshi.SystemInfo;
import java.util.Locale;

public final class FMA {

    private static final Logger LOGGER = LogUtils.getLogger();

    static {
        final Thread t = new Thread() {
            @Override
            public void run() {
                FMAInit.init();
            }
        };
        t.setPriority(Thread.MIN_PRIORITY);
        t.setName(PlatformHooks.get().getBrand() + " FMA initialiser");
        t.setDaemon(true);
        t.setUncaughtExceptionHandler((final Thread thread, final Throwable thr) -> {
            LOGGER.error("Uncaught exception in thread '" + thread.getName() + "': ", thr);
        });
        t.start();
    }

    public static void init() {}

    public static boolean isFMASupported() {
        return FMAInit.FMA_SUPPORTED;
    }

    private static final class FMAInit {

        public static final boolean FMA_SUPPORTED = detectFma();

        private static void init() {}

        private static boolean detectFma() {
            final String arch = System.getProperty("os.arch", "").toLowerCase(Locale.ROOT);

            final boolean is64 = arch.contains("64");
            final boolean arm = arch.contains("aarch64") || arch.contains("arm64");
            if (arm) {
                // FMA is ARMv8 baseline, Apple Silicon included
                return report(true, "ARMv8 baseline");
            }
            if (!is64) {
                return report(false, "non 64-bit platform");
            }
            if (System.getProperty("os.name", "").toLowerCase(Locale.ROOT).contains("linux")) {
                try {
                    for (final String line : new SystemInfo().getHardware().getProcessor().getFeatureFlags()) {
                        if (line.toLowerCase(Locale.ROOT).matches("(?s).*\\bfma\\b.*")) {
                            LOGGER.debug("Matched FMA feature flag line: " + line);
                            return report(true, "CPU feature flag");
                        }
                    }

                    return report(false, "no CPU feature flag");
                } catch (final Throwable thr) {
                    LOGGER.debug("Error while retrieving platform flags: ", thr);
                    return report(testForFMA(), "performance test, CPU feature flags unavailable");
                }
            }
            // Windows, macOS: measure the actual thing
            return report(testForFMA(), "performance test");
        }

        private static boolean report(final boolean supported, final String reason) {
            LOGGER.info("FMA supported: " + supported + " (" + reason + ")");
            return supported;
        }

        private static boolean testForFMA() {
            // warm into C2
            for (int i = 0; i < 3; i++) {
                timeFma(Sink.X);
                timeMul(Sink.Y);
            }
            final long fma = Math.min(
                timeFma(Sink.X),
                Math.min(
                    timeFma(Sink.X), timeFma(Sink.X)
                )
            );
            final long mul = Math.min(
                timeMul(Sink.Y),
                Math.min(
                    timeMul(Sink.Y), timeMul(Sink.Y)
                )
            );
            // intrinsic fma ~= mul speed; software is 10x+
            return fma < mul * 3;
        }

        private static final double A = 1.000001;
        private static final double B = 0.999999;
        private static final int LOOPS = 200_000;

        private static long timeFma(final double[] s) {
            double x = s[0];
            long t = System.nanoTime();
            for (int i = 0; i < LOOPS; i++) {
                x = Math.fma(A, B, x);
            }
            s[0] = x;
            return System.nanoTime() - t;
        }

        private static long timeMul(final double[] s) {
            double y = s[0];
            long t = System.nanoTime();
            for (int i = 0; i < LOOPS; i++) {
                y = y * A + B;
            }
            s[0] = y;
            return System.nanoTime() - t;
        }

        static final class Sink {
            static final double[] X = { 0.5 };
            static final double[] Y = { 0.5 };
        }
    }

    private FMA() {}
}
