package org.bukkit.entity;

import net.minecraft.world.entity.vehicle.EntityBoat;
import org.bukkit.craftbukkit.entity.CraftBoat;
import org.bukkit.support.AbstractTestingBase;
import org.junit.jupiter.api.Test;

public class BoatTest extends AbstractTestingBase {

    @Test
    public void testTypes() {
        for (EntityBoat.EnumBoatType enumBoatType : EntityBoat.EnumBoatType.values()) {
            CraftBoat.boatTypeFromNms(enumBoatType);
        }

        for (Boat.Type enumBoatType : Boat.Type.values()) {
            CraftBoat.boatTypeToNms(enumBoatType);
        }
    }

    @Test
    public void testStatus() {
        for (EntityBoat.EnumStatus enumStatus : EntityBoat.EnumStatus.values()) {
            CraftBoat.boatStatusFromNms(enumStatus);
        }
    }
}
