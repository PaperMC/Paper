package org.bukkit.entity;

import static org.junit.jupiter.api.Assertions.*;
import net.minecraft.world.entity.EntityPose;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;

@Normal
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
