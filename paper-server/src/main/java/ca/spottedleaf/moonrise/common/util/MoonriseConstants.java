package ca.spottedleaf.moonrise.common.util;

import ca.spottedleaf.moonrise.common.PlatformHooks;

public final class MoonriseConstants {

    public static final int MAX_VIEW_DISTANCE = Integer.getInteger(PlatformHooks.get().getBrand() + ".MaxViewDistance", 32);

    private MoonriseConstants() {}

}
