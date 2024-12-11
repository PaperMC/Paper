package org.bukkit.craftbukkit.attribute;

import com.google.common.base.Preconditions;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;

public class CraftAttributeMap implements Attributable {

    private final AttributeMap handle;

    public CraftAttributeMap(AttributeMap handle) {
        this.handle = handle;
    }

    @Override
    public AttributeInstance getAttribute(Attribute attribute) {
        Preconditions.checkArgument(attribute != null, "attribute");
        net.minecraft.world.entity.ai.attributes.AttributeInstance nms = this.handle.getInstance(CraftAttribute.bukkitToMinecraftHolder(attribute));

        return (nms == null) ? null : new CraftAttributeInstance(nms, attribute);
    }
}
