package io.papermc.paper.attribute;

import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.craftbukkit.attribute.CraftAttribute;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UnmodifiableAttributeMap implements Attributable {

    private final AttributeSupplier handle;

    public UnmodifiableAttributeMap(@NotNull AttributeSupplier handle) {
        this.handle = handle;
    }

    @Override
    public @Nullable AttributeInstance getAttribute(@NotNull Attribute attribute) {
        net.minecraft.core.Holder<net.minecraft.world.entity.ai.attributes.Attribute> nmsAttribute = CraftAttribute.bukkitToMinecraftHolder(attribute);
        if (!this.handle.hasAttribute(nmsAttribute)) {
            return null;
        }
        return new UnmodifiableAttributeInstance(this.handle.getAttributeInstance(nmsAttribute), attribute);
    }

    @Override
    public void registerAttribute(@NotNull Attribute attribute) {
        throw new UnsupportedOperationException("Cannot register new attributes here");
    }
}
