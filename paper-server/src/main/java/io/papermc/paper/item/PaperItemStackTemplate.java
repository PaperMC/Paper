package io.papermc.paper.item;

import com.google.common.base.Preconditions;
import io.papermc.paper.datacomponent.DataComponentAdapter;
import io.papermc.paper.datacomponent.DataComponentBuilder;
import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.datacomponent.DataComponentView;
import io.papermc.paper.datacomponent.PaperDataComponentPatchView;
import io.papermc.paper.datacomponent.PaperDataComponentType;
import io.papermc.paper.datacomponent.PaperDataComponentView;
import io.papermc.paper.persistence.ItemPersistentDataViewHolder;
import io.papermc.paper.registry.RegistryElement;
import io.papermc.paper.registry.data.util.Conversions;
import io.papermc.paper.registry.holder.PaperRegistryHolders;
import io.papermc.paper.registry.holder.RegistryHolder;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.CustomData;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.inventory.CraftItemType;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.util.BoundChecker.requireRange;

public final class PaperItemStackTemplate implements ItemStackTemplate, ItemPersistentDataViewHolder, Handleable<net.minecraft.world.item.ItemStackTemplate> {

    private final net.minecraft.world.item.ItemStackTemplate template;
    private final PaperDataComponentPatchView patch;

    public PaperItemStackTemplate(final net.minecraft.world.item.ItemStackTemplate template) {
        this.template = template;
        this.patch = new PaperDataComponentPatchView(template.components());
    }

    public static net.minecraft.world.item.ItemStackTemplate unwrap(final ItemStackTemplate template) {
        return ((PaperItemStackTemplate) template).template;
    }

    @Override
    public net.minecraft.world.item.ItemStackTemplate getHandle() {
        return this.template;
    }

    @Override
    public ItemType type() {
        return CraftItemType.minecraftToBukkitNew(this.template.item().value());
    }

    @Override
    public int amount() {
        return this.template.count();
    }

    @Override
    public PaperDataComponentPatchView patch() {
        return this.patch;
    }

    @Override
    public DataComponentView resolveData() {
        Preconditions.checkState(this.template.item().areComponentsBound(), "The default data components of %s aren't available yet", this.template.item().getRegisteredName());
        return new PaperDataComponentView(this.template);
    }

    @Override
    public CustomData getCustomData() {
        final CustomData customData = this.patch.getValue(DataComponents.CUSTOM_DATA);
        return customData == null ? CustomData.EMPTY : customData;
    }

    @Override
    public ItemStack createItemStack() {
        return CraftItemStack.asBukkitCopy(this.template);
    }

    @Override
    public ItemStackTemplate.Builder toBuilder() {
        return new BuilderImpl(this.template.item(), this.template.count(), this.template.components());
    }

    @Override
    public boolean equals(final @Nullable Object obj) {
        return obj instanceof final PaperItemStackTemplate other && this.template.equals(other.template);
    }

    @Override
    public int hashCode() {
        return this.template.hashCode();
    }

    @Override
    public String toString() {
        return "PaperItemStackTemplate{template=" + this.template + "}";
    }

    public static final class BuilderImpl implements ItemStackTemplate.Builder {

        private final Holder<Item> item;
        private int amount;
        private final DataComponentPatch.Builder patch = DataComponentPatch.builder();

        public BuilderImpl(final Holder<Item> item, final int amount, final DataComponentPatch patch) {
            this.item = item;
            this.amount = amount;
            this.patch.copy(patch);
        }

        @Override
        public ItemStackTemplate.Builder amount(final int amount) {
            this.amount = requireRange(amount, "amount", 1, Item.ABSOLUTE_MAX_STACK_SIZE);
            return this;
        }

        @Override
        public <T> ItemStackTemplate.Builder setData(final DataComponentType.Valued<T> type, final T value) {
            Preconditions.checkArgument(value != null, "value cannot be null");
            final PaperDataComponentType.ValuedImpl<T, ?> paperType = (PaperDataComponentType.ValuedImpl<T, ?>) type;
            PaperDataComponentPatchView.checkNotHolderValued(paperType, "setHolder");
            return this.setDataInternal(paperType, value);
        }

        @Override
        public <T> ItemStackTemplate.Builder setData(final DataComponentType.Valued<T> type, final DataComponentBuilder<T> valueBuilder) {
            return this.setData(type, valueBuilder.build());
        }

        @Override
        public ItemStackTemplate.Builder setData(final DataComponentType.NonValued type) {
            return this.setDataInternal((PaperDataComponentType.NonValuedImpl<?, ?>) type, null);
        }

        private <A, V> ItemStackTemplate.Builder setDataInternal(final PaperDataComponentType<A, V> type, final A value) {
            this.patch.set(type.getHandle(), type.getAdapter().toVanilla(value, type.getHolder()));
            return this;
        }

        @Override
        public <T extends RegistryElement.Buildable<T, E, ?>, E> ItemStackTemplate.Builder setHolder(final DataComponentType.Valued<T> type, final RegistryHolder<T, E> holder) {
            return this.setHolder((PaperDataComponentType.ValuedImpl<T, ?>) type, holder);
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        private <T extends RegistryElement.Buildable<T, E, ?>, E, NMS, M> ItemStackTemplate.Builder setHolder(final PaperDataComponentType.ValuedImpl<T, NMS> type, final RegistryHolder<T, E> holder) {
            Preconditions.checkArgument(holder != null, "holder cannot be null");
            final DataComponentAdapter.HolderValue<NMS, M> holderValue = (DataComponentAdapter.HolderValue<NMS, M>) PaperDataComponentPatchView.requireHolderValue(type);
            Preconditions.checkArgument(holder.registryKey().equals(holderValue.registryKey()), "%s holds a value of %s, not %s", type.getHolder().getRegisteredName(), holderValue.registryKey(), holder.registryKey());
            final Holder<M> nmsHolder = PaperRegistryHolders.convert((RegistryHolder) holder, Conversions.current());
            this.patch.set(type.getHandle(), holderValue.wrap().apply(nmsHolder));
            return this;
        }

        @Override
        public ItemStackTemplate.Builder unsetData(final DataComponentType type) {
            this.patch.remove(PaperDataComponentType.bukkitToMinecraft(type));
            return this;
        }

        @Override
        public ItemStackTemplate.Builder resetData(final DataComponentType type) {
            this.patch.clear(PaperDataComponentType.bukkitToMinecraft(type));
            return this;
        }

        @Override
        public ItemStackTemplate build() {
            return new PaperItemStackTemplate(new net.minecraft.world.item.ItemStackTemplate(this.item, this.amount, this.patch.build()));
        }
    }
}
