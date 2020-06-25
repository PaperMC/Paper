package org.bukkit.craftbukkit.attribute;

import com.google.common.base.Preconditions;
import net.minecraft.server.AttributeBase;
import net.minecraft.server.AttributeMapBase;
import net.minecraft.server.IRegistry;
import org.bukkit.Registry;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;

public class CraftAttributeMap implements Attributable {

    private final AttributeMapBase handle;

    public CraftAttributeMap(AttributeMapBase handle) {
        this.handle = handle;
    }

    @Override
    public AttributeInstance getAttribute(Attribute attribute) {
        Preconditions.checkArgument(attribute != null, "attribute");
        net.minecraft.server.AttributeModifiable nms = handle.a(toMinecraft(attribute));

        return (nms == null) ? null : new CraftAttributeInstance(nms, attribute);
    }

    public static AttributeBase toMinecraft(Attribute attribute) {
        return IRegistry.ATTRIBUTE.get(CraftNamespacedKey.toMinecraft(attribute.getKey()));
    }

    public static Attribute fromMinecraft(String nms) {
        return Registry.ATTRIBUTE.get(CraftNamespacedKey.fromString(nms));
    }
}
