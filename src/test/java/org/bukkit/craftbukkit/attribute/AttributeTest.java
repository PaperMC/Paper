package org.bukkit.craftbukkit.attribute;

import net.minecraft.server.AttributeBase;
import net.minecraft.server.IRegistry;
import net.minecraft.server.MinecraftKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.support.AbstractTestingBase;
import org.junit.Assert;
import org.junit.Test;

public class AttributeTest extends AbstractTestingBase {

    @Test
    public void testToBukkit() {
        for (MinecraftKey nms : IRegistry.ATTRIBUTE.keySet()) {
            Attribute bukkit = CraftAttributeMap.fromMinecraft(nms.toString());

            Assert.assertNotNull(nms.toString(), bukkit);
        }
    }

    @Test
    public void testToNMS() {
        for (Attribute attribute : Attribute.values()) {
            AttributeBase nms = CraftAttributeMap.toMinecraft(attribute);

            Assert.assertNotNull(attribute.name(), nms);
        }
    }
}
