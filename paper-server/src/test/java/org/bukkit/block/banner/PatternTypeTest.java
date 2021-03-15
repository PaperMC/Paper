package org.bukkit.block.banner;

import junit.framework.Assert;
import net.minecraft.world.level.block.entity.EnumBannerPatternType;
import org.bukkit.support.AbstractTestingBase;
import org.junit.Test;

public class PatternTypeTest extends AbstractTestingBase {

    @Test
    public void testToBukkit() {
        for (EnumBannerPatternType nms : EnumBannerPatternType.values()) {
            PatternType bukkit = PatternType.getByIdentifier(nms.b());

            Assert.assertNotNull("No Bukkit banner for " + nms + " " + nms.b(), bukkit);
        }
    }

    @Test
    public void testToNMS() {
        for (PatternType bukkit : PatternType.values()) {
            EnumBannerPatternType found = null;
            for (EnumBannerPatternType nms : EnumBannerPatternType.values()) {
                if (bukkit.getIdentifier().equals(nms.b())) {
                    found = nms;
                    break;
                }
            }

            Assert.assertNotNull("No NMS banner for " + bukkit + " " + bukkit.getIdentifier(), found);
        }
    }
}
