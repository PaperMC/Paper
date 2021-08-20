package io.papermc.paper.attribute;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.craftbukkit.attribute.CraftAttributeInstance;

import java.util.Collection;

public class UnmodifiableAttributeInstance extends CraftAttributeInstance {

    public UnmodifiableAttributeInstance(AttributeInstance handle, Attribute attribute) {
        super(handle, attribute);
    }

    @Override
    public void setBaseValue(double d) {
        throw new UnsupportedOperationException("Cannot modify default attributes");
    }

    @Override
    public void addModifier(AttributeModifier modifier) {
        throw new UnsupportedOperationException("Cannot modify default attributes");
    }

    @Override
    public void removeModifier(AttributeModifier modifier) {
        throw new UnsupportedOperationException("Cannot modify default attributes");
    }
}
