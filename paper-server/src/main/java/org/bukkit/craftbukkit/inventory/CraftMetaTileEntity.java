package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import net.minecraft.server.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;

@DelegateDeserialization(CraftMetaItem.SerializableMeta.class)
public class CraftMetaTileEntity extends CraftMetaItem {
    
    @ItemMetaKey.Specific(ItemMetaKey.Specific.To.NBT)
    static final ItemMetaKey BLOCK_ENTITY_TAG = new ItemMetaKey("BlockEntityTag");
    
    final Material material;
    private final NBTTagCompound blockEntityTag;

    CraftMetaTileEntity(CraftMetaItem meta, Material material) {
        super(meta);
        this.material = material;

        if (!(meta instanceof CraftMetaTileEntity)) {
            blockEntityTag = null;
            return;
        }

        CraftMetaTileEntity te = (CraftMetaTileEntity) meta;
        this.blockEntityTag = te.blockEntityTag;
    }

    CraftMetaTileEntity(NBTTagCompound tag, Material material) {
        super(tag);
        this.material = material;
        
        if (tag.hasKeyOfType(BLOCK_ENTITY_TAG.NBT, 10)) {
            blockEntityTag = tag.getCompound(BLOCK_ENTITY_TAG.NBT);
        } else {
            blockEntityTag = null;
        }
    }

    CraftMetaTileEntity(Map<String, Object> map) {
        super(map);
        material = Material.AIR; // TODO
        
        blockEntityTag = null;
    }

    @Override
    void applyToItem(NBTTagCompound tag) {
        super.applyToItem(tag);
        
        if (blockEntityTag != null) {
            tag.set(BLOCK_ENTITY_TAG.NBT, blockEntityTag);
        }
    }

    @Override
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);
        return builder;
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        if (blockEntityTag != null) {
            hash = 61 * hash + this.blockEntityTag.hashCode();
        }
        return original != hash ? CraftMetaTileEntity.class.hashCode() ^ hash : hash;
    }

    @Override
    public boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaTileEntity) {
            CraftMetaTileEntity that = (CraftMetaTileEntity) meta;

            return Objects.equal(this.blockEntityTag, that.blockEntityTag);
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaTileEntity || blockEntityTag == null);
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && blockEntityTag == null;
    }

    @Override
    boolean applicableTo(Material type) {
        switch(type){            
            case COMMAND:
            case CHEST:
            case TRAPPED_CHEST:
            case FURNACE:
            case HOPPER:
            case MOB_SPAWNER:
            case SIGN:
            case BREWING_STAND_ITEM:
            case JUKEBOX:
            case FLOWER_POT_ITEM:
            case DISPENSER:
            case DROPPER:
                return true;
        }
        return false;
    }
}
