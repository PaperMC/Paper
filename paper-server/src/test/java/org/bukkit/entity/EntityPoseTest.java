package org.bukkit.entity;

import static org.junit.jupiter.api.Assertions.*;

import net.minecraft.world.entity.Pose;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;

@Normal
public class EntityPoseTest {

    @Test
    public void testBukkitToMinecraft() {
        for (Pose pose : Pose.values()) {
            assertNotNull(Pose.values()[pose.ordinal()], pose.name());
        }
    }

    @Test
    public void testMinecraftToBukkit() {
        for (Pose entityPose : Pose.values()) {
            assertNotNull(org.bukkit.entity.Pose.values()[entityPose.ordinal()], entityPose.name());
        }
    }
}
