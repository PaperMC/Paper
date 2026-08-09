package io.papermc.paper.math;

import org.bukkit.util.NumberConversions;

record AngleImpl(float degrees, boolean relative) implements Angle {
    AngleImpl {
        NumberConversions.checkFinite(degrees, "degrees not finite");
    }
}
