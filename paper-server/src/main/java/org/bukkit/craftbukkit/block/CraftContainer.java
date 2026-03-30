package org.bukkit.craftbukkit.block;

import net.minecraft.advancements.criterion.DataComponentMatchers;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.core.component.DataComponentExactPredicate;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.LockCode;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Container;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.inventory.ItemStack;

public abstract class CraftContainer<T extends BaseContainerBlockEntity> extends CraftBlockEntityState<T> implements Container {

    public CraftContainer(World world, T blockEntity) {
        super(world, blockEntity);
    }

    protected CraftContainer(CraftContainer<T> state, Location location) {
        super(state, location);
    }

    @Override
    public boolean isLocked() {
        return this.getSnapshot().lockKey != LockCode.NO_LOCK;
    }

    @Override
    public String getLock() {
        Component customName = this.getSnapshot().lockKey.predicate().components().exact().asPatch().get(DataComponentMap.EMPTY, DataComponents.CUSTOM_NAME);
        return (customName != null) ? CraftChatMessage.fromComponent(customName) : "";
    }

    @Override
    public void setLock(String key) {
        if (key == null || key.isEmpty()) {
            this.getSnapshot().lockKey = LockCode.NO_LOCK;
        } else {
            this.getSnapshot().lockKey = new LockCode(ItemPredicate.Builder.item().withComponents(
                DataComponentMatchers.Builder.components().exact(
                    DataComponentExactPredicate.builder().expect(
                        DataComponents.CUSTOM_NAME, CraftChatMessage.fromString(key)[0]
                    ).build()
                ).build()
            ).build());
        }
    }

    @Override
    public void setLockItem(ItemStack key) {
        if (key == null) {
            this.getSnapshot().lockKey = LockCode.NO_LOCK;
        } else {
            this.getSnapshot().lockKey = new LockCode(CraftItemStack.asCriterionConditionItem(key));
        }
    }

    @Override
    public net.kyori.adventure.text.Component customName() {
        final T blockEntity = this.getSnapshot();
        return blockEntity.hasCustomName() ? io.papermc.paper.adventure.PaperAdventure.asAdventure(blockEntity.getCustomName()) : null;
    }

    @Override
    public void customName(final net.kyori.adventure.text.Component customName) {
        this.getSnapshot().name = customName != null ? io.papermc.paper.adventure.PaperAdventure.asVanilla(customName) : null;
    }

    @Override
    public String getCustomName() {
        T container = this.getSnapshot();
        return container.name != null ? CraftChatMessage.fromComponent(container.getCustomName()) : null;
    }

    @Override
    public void setCustomName(String name) {
        this.getSnapshot().name = CraftChatMessage.fromStringOrNull(name);
    }

    @Override
    public void applyTo(T blockEntity) {
        super.applyTo(blockEntity);

        if (this.getSnapshot().name == null) {
            blockEntity.name = null;
        }
    }

    @Override
    public abstract CraftContainer<T> copy();

    @Override
    public abstract CraftContainer<T> copy(Location location);
}
