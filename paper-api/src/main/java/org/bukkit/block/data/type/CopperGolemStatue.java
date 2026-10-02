package org.bukkit.block.data.type;

import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Waterlogged;
import org.jspecify.annotations.NullMarked;

/// 'copper\_golem\_pose' indicates the pose the statue stands.
@NullMarked
public interface CopperGolemStatue extends BlockData, Directional, Waterlogged {

    /// Gets the value of the 'copper\_golem\_pose' property.
    ///
    /// @return the 'copper\_golem\_pose' value
    Pose getCopperGolemPose();

    /// Sets the value of the 'copper\_golem\_pose' property.
    ///
    /// @param pose the new 'copper\_golem\_pose' value
    void setCopperGolemPose(Pose pose);

    enum Pose {
        STANDING,
        SITTING,
        RUNNING,
        STAR
    }
}
