package org.bukkit.entity;

import static org.junit.jupiter.api.Assertions.*;
import net.minecraft.world.entity.EntityPose;
import org.junit.jupiter.api.Test;

public class EntityPoseTest {

    @Test
    public void testBukkitToMinecraft() {
        for (Pose pose : Pose.values()) {
            assertNotNull(EntityPose.values()[pose.ordinal()], pose.name());
        }
    }

    @Test
    public void testMinecraftToBukkit() {
        for (EntityPose entityPose : EntityPose.values()) {
            assertNotNull(Pose.values()[entityPose.ordinal()], entityPose.name());
        }
    }
}
