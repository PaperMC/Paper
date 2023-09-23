package org.bukkit.block.banner;

import static org.junit.jupiter.api.Assertions.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.EnumBannerPatternType;
import org.bukkit.support.AbstractTestingBase;
import org.junit.jupiter.api.Test;

public class PatternTypeTest extends AbstractTestingBase {

    @Test
    public void testToBukkit() {
        for (EnumBannerPatternType nms : BuiltInRegistries.BANNER_PATTERN) {
            PatternType bukkit = PatternType.getByIdentifier(nms.getHashname());

            assertNotNull(bukkit, "No Bukkit banner for " + nms + " " + nms.getHashname());
        }
    }

    @Test
    public void testToNMS() {
        for (PatternType bukkit : PatternType.values()) {
            EnumBannerPatternType found = null;
            for (EnumBannerPatternType nms : BuiltInRegistries.BANNER_PATTERN) {
                if (bukkit.getIdentifier().equals(nms.getHashname())) {
                    found = nms;
                    break;
                }
            }

            assertNotNull(found, "No NMS banner for " + bukkit + " " + bukkit.getIdentifier());
        }
    }
}
