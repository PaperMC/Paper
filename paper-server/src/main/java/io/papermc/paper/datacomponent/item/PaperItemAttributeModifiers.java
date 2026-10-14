package io.papermc.paper.datacomponent.item;

import com.google.common.base.Preconditions;
import io.papermc.paper.datacomponent.item.attribute.AttributeModifierDisplay;
import io.papermc.paper.datacomponent.item.attribute.PaperAttributeModifierDisplay;
import io.papermc.paper.util.MCUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.craftbukkit.attribute.CraftAttribute;
import org.bukkit.craftbukkit.attribute.CraftAttributeInstance;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.Unmodifiable;

public record PaperItemAttributeModifiers(
    net.minecraft.world.item.component.ItemAttributeModifiers impl
) implements ItemAttributeModifiers, Handleable<net.minecraft.world.item.component.ItemAttributeModifiers> {

    private static List<Entry> convert(final net.minecraft.world.item.component.ItemAttributeModifiers nmsModifiers) {
        return MCUtil.transformUnmodifiable(nmsModifiers.modifiers(), nms -> new PaperEntry(
            CraftAttribute.minecraftHolderToBukkit(nms.attribute()),
            CraftAttributeInstance.convert(nms.modifier(), nms.slot()),
            PaperAttributeModifierDisplay.fromVanilla(nms.display())
        ));
    }

    @Override
    public net.minecraft.world.item.component.ItemAttributeModifiers getHandle() {
        return this.impl;
    }

    @Override
    public @Unmodifiable List<Entry> modifiers() {
        return convert(this.impl);
    }

    public record PaperEntry(Attribute attribute, AttributeModifier modifier, AttributeModifierDisplay display) implements ItemAttributeModifiers.Entry {
    }

    static final class BuilderImpl implements ItemAttributeModifiers.Builder {

        private final List<net.minecraft.world.item.component.ItemAttributeModifiers.Entry> entries = new ObjectArrayList<>();

        @Override
        public ItemAttributeModifiers.Builder addModifier(final Attribute attribute, final AttributeModifier modifier, final EquipmentSlotGroup equipmentSlotGroup, final AttributeModifierDisplay display) {
            Preconditions.checkArgument(
                this.entries.stream().noneMatch(e ->
                    e.modifier().id().equals(CraftNamespacedKey.toMinecraft(modifier.getKey())) && e.attribute().is(CraftNamespacedKey.toMinecraft(attribute.getKey()))
                ),
                "Cannot add 2 modifiers with identical keys on the same attribute (modifier %s for attribute %s)",
                modifier.getKey(), attribute.getKey()
            );

            this.entries.add(new net.minecraft.world.item.component.ItemAttributeModifiers.Entry(
                CraftAttribute.bukkitToMinecraftHolder(attribute),
                CraftAttributeInstance.convert(modifier),
                CraftEquipmentSlot.getNMSGroup(equipmentSlotGroup),
                PaperAttributeModifierDisplay.toVanilla(display)
            ));
            return this;
        }

        @Override
        public ItemAttributeModifiers build() {
            if (this.entries.isEmpty()) {
                return new PaperItemAttributeModifiers(net.minecraft.world.item.component.ItemAttributeModifiers.EMPTY);
            }

            return new PaperItemAttributeModifiers(new net.minecraft.world.item.component.ItemAttributeModifiers(
                new ObjectArrayList<>(this.entries)
            ));
        }
    }
}
