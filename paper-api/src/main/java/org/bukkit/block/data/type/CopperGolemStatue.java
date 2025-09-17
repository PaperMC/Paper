package org.bukkit.block.data.type;

import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Waterlogged;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface CopperGolemStatue extends BlockData, Directional, Waterlogged {
    Pose getCopperGolemPose();

    void setCopperGolemPose(Pose pose);

    enum Pose {
        STANDING,
        SITTING,
        RUNNING,
        STAR
    }
}
