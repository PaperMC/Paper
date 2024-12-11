package org.bukkit.craftbukkit.block;

import java.util.Collections;
import java.util.Optional;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.component.DataComponentPredicate;
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

    public CraftContainer(World world, T tileEntity) {
        super(world, tileEntity);
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
        Optional<? extends Component> customName = this.getSnapshot().lockKey.predicate().components().asPatch().get(DataComponents.CUSTOM_NAME);

        return (customName != null) ? customName.map(CraftChatMessage::fromComponent).orElse("") : "";
    }

    @Override
    public void setLock(String key) {
        if (key == null) {
            this.getSnapshot().lockKey = LockCode.NO_LOCK;
        } else {
            DataComponentPredicate predicate = DataComponentPredicate.builder().expect(DataComponents.CUSTOM_NAME, CraftChatMessage.fromStringOrNull(key)).build();
            this.getSnapshot().lockKey = new LockCode(new ItemPredicate(Optional.empty(), MinMaxBounds.Ints.ANY, predicate, Collections.emptyMap()));
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
    public String getCustomName() {
        T container = this.getSnapshot();
        return container.name != null ? CraftChatMessage.fromComponent(container.getCustomName()) : null;
    }

    @Override
    public void setCustomName(String name) {
        this.getSnapshot().name = CraftChatMessage.fromStringOrNull(name);
    }

    @Override
    public void applyTo(T container) {
        super.applyTo(container);

        if (this.getSnapshot().name == null) {
            container.name = null;
        }
    }

    @Override
    public abstract CraftContainer<T> copy();

    @Override
    public abstract CraftContainer<T> copy(Location location);
}
