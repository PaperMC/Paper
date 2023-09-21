package org.bukkit.craftbukkit.attribute;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.AttributeBase;
import org.bukkit.attribute.Attribute;
import org.bukkit.support.AbstractTestingBase;
import org.junit.Assert;
import org.junit.Test;

public class AttributeTest extends AbstractTestingBase {

    @Test
    public void testToBukkit() {
        for (AttributeBase nms : BuiltInRegistries.ATTRIBUTE) {
            Attribute bukkit = CraftAttribute.minecraftToBukkit(nms);

            Assert.assertNotNull(nms.toString(), bukkit);
        }
    }

    @Test
    public void testToNMS() {
        for (Attribute attribute : Attribute.values()) {
            AttributeBase nms = CraftAttribute.bukkitToMinecraft(attribute);

            Assert.assertNotNull(attribute.name(), nms);
        }
    }
}
