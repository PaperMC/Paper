package io.papermc.paper.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.datacomponent.PaperDataComponentType;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import java.util.function.UnaryOperator;
import net.kyori.adventure.text.event.HoverEvent;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.Item;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.inventory.CraftItemType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record PaperItemStackTemplate(
    Holder<Item> item,
    int count,
    DataComponentPatch componentPatch
) implements ItemStackTemplate {

    public PaperItemStackTemplate(final Holder<Item> type, final int count) {
        this(type, count, new DataComponentPatch(new Reference2ObjectArrayMap<>()));
    }

    public static ItemStackTemplate fromNMS(final net.minecraft.world.item.ItemStackTemplate template) {
        return new PaperItemStackTemplate(
            template.item(),
            template.count(),
            new DataComponentPatch(new Reference2ObjectArrayMap<>(template.components().map))
        );
    }

    public static net.minecraft.world.item.ItemStackTemplate toNMS(final ItemStackTemplate template) {
        final PaperItemStackTemplate internal = (PaperItemStackTemplate) template;
        return new net.minecraft.world.item.ItemStackTemplate(
            internal.item,
            internal.count,
            new DataComponentPatch(new Reference2ObjectArrayMap<>(internal.componentPatch.map))
        );
    }

    @Override
    public ItemType type() {
        return CraftItemType.minecraftToBukkitNew(this.item.value());
    }

    @Override
    public ItemStackTemplate withCount(final int amount) {
        return new PaperItemStackTemplate(
            this.item,
            amount,
            new DataComponentPatch(new Reference2ObjectArrayMap<>(this.componentPatch.map))
        );
    }

    @Override
    public ItemStackTemplate withType(final ItemType type) {
        return new PaperItemStackTemplate(
            ((CraftItemType<?>) type).getHolder(),
            this.count,
            new DataComponentPatch(new Reference2ObjectArrayMap<>(this.componentPatch.map))
        );
    }

    @Override
    public <T> void setData(final DataComponentType.Valued<T> type, final DataComponentBuilder<T> valueBuilder) {
        this.setDataInternal((PaperDataComponentType.ValuedImpl<T, ?>) type, valueBuilder.build());
    }

    @Override
    public <T> void setData(final DataComponentType.Valued<T> type, final T value) {
        this.setDataInternal((PaperDataComponentType.ValuedImpl<T, ?>) type, value);
    }

    @Override
    public void setData(final DataComponentType.NonValued type) {
        this.setDataInternal((PaperDataComponentType.NonValuedImpl<?, ?>) type, null);
    }

    private <A, V> void setDataInternal(final PaperDataComponentType<A, V> type, final A value) {
        this.componentPatch.map.put(type.getHandle(), type.getAdapter().toVanillaUnchecked(value));
    }

    @Override
    public ItemStack getAsItemStack() {
        return CraftItemStack.asBukkitMirror(toNMS(this).create());
    }

    @Override
    public HoverEvent<HoverEvent.ShowItem> asHoverEvent(final UnaryOperator<HoverEvent.ShowItem> op) {
        return Bukkit.getServer().getItemFactory().asHoverEvent(this, op);
    }

    @Override
    public String translationKey() {
        return this.item.value().getDescriptionId();
    }
}
