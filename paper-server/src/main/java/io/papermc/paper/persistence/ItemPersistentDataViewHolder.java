package io.papermc.paper.persistence;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.component.CustomData;
import org.bukkit.craftbukkit.persistence.CraftPersistentDataTypeRegistry;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface ItemPersistentDataViewHolder extends PersistentDataViewHolder {

    String PDC_CUSTOM_DATA_KEY = "PublicBukkitValues";
    CraftPersistentDataTypeRegistry DATA_TYPE_REGISTRY = new CraftPersistentDataTypeRegistry();

    static PersistentDataContainerView createView(final ItemPersistentDataViewHolder holder) {
        return new PaperPersistentDataContainerView(DATA_TYPE_REGISTRY) {

            @Override
            public int getSize() {
                return holder.getPdcTag().size();
            }

            @Override
            public CompoundTag toTagCompound() {
                return holder.getPdcTag();
            }

            @Override
            public @Nullable Tag getTag(final String key) {
                return holder.getPdcTag().get(key);
            }
        };
    }

    CustomData getCustomData();

    private CompoundTag getPdcTag() {
        // getUnsafe is OK here because we are only ever *reading* the data so immutability is preserved
        //noinspection deprecation
        return this.getCustomData().getUnsafe().getCompoundOrEmpty(PDC_CUSTOM_DATA_KEY);
    }

    @Override
    default PersistentDataContainerView getPersistentDataContainer() {
        return createView(this);
    }
}
