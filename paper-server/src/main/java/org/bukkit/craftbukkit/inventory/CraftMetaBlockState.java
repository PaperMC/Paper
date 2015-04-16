package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import net.minecraft.server.BlockJukeBox;
import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntityBanner;
import net.minecraft.server.TileEntityBeacon;
import net.minecraft.server.TileEntityBrewingStand;
import net.minecraft.server.TileEntityChest;
import net.minecraft.server.TileEntityCommand;
import net.minecraft.server.TileEntityDispenser;
import net.minecraft.server.TileEntityDropper;
import net.minecraft.server.TileEntityFurnace;
import net.minecraft.server.TileEntityHopper;
import net.minecraft.server.TileEntityMobSpawner;
import net.minecraft.server.TileEntityNote;
import net.minecraft.server.TileEntitySign;
import net.minecraft.server.TileEntitySkull;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.block.CraftBanner;
import org.bukkit.craftbukkit.block.CraftBeacon;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.block.CraftBrewingStand;
import org.bukkit.craftbukkit.block.CraftChest;
import org.bukkit.craftbukkit.block.CraftCommandBlock;
import org.bukkit.craftbukkit.block.CraftCreatureSpawner;
import org.bukkit.craftbukkit.block.CraftDispenser;
import org.bukkit.craftbukkit.block.CraftDropper;
import org.bukkit.craftbukkit.block.CraftFurnace;
import org.bukkit.craftbukkit.block.CraftHopper;
import org.bukkit.craftbukkit.block.CraftJukebox;
import org.bukkit.craftbukkit.block.CraftNoteBlock;
import org.bukkit.craftbukkit.block.CraftSign;
import org.bukkit.craftbukkit.block.CraftSkull;
import org.bukkit.inventory.meta.BlockStateMeta;

@DelegateDeserialization(CraftMetaItem.SerializableMeta.class)
public class CraftMetaBlockState extends CraftMetaItem implements BlockStateMeta {
    
    @ItemMetaKey.Specific(ItemMetaKey.Specific.To.NBT)
    static final ItemMetaKey BLOCK_ENTITY_TAG = new ItemMetaKey("BlockEntityTag");
    
    final Material material;
    NBTTagCompound blockEntityTag;

    CraftMetaBlockState(CraftMetaItem meta, Material material) {
        super(meta);
        this.material = material;

        if (!(meta instanceof CraftMetaBlockState)
                || ((CraftMetaBlockState) meta).material != material
                || material == Material.SIGN
                || material == Material.COMMAND) {
            blockEntityTag = null;
            return;
        }

        CraftMetaBlockState te = (CraftMetaBlockState) meta;
        this.blockEntityTag = te.blockEntityTag;
    }

    CraftMetaBlockState(NBTTagCompound tag, Material material) {
        super(tag);
        this.material = material;
        
        if (tag.hasKeyOfType(BLOCK_ENTITY_TAG.NBT, 10)) {
            blockEntityTag = tag.getCompound(BLOCK_ENTITY_TAG.NBT);
        } else {
            blockEntityTag = null;
        }
    }

    CraftMetaBlockState(Map<String, Object> map) {
        super(map);
        String matName = SerializableMeta.getString(map, "blockMaterial", true);
        Material m = Material.getMaterial(matName);
        if (m != null) {
            material = m;
        } else {
            material = Material.AIR;
        }
    }

    @Override
    void applyToItem(NBTTagCompound tag) {
        super.applyToItem(tag);
        
        if (blockEntityTag != null) {
            tag.set(BLOCK_ENTITY_TAG.NBT, blockEntityTag);
        }
    }

    @Override
    void deserializeInternal(NBTTagCompound tag) {
        if (tag.hasKeyOfType(BLOCK_ENTITY_TAG.NBT, 10)) {
            blockEntityTag = tag.getCompound(BLOCK_ENTITY_TAG.NBT);
        }
    }

    @Override
    void serializeInternal(final Map<String, NBTBase> internalTags) {
        if (blockEntityTag != null) {
            internalTags.put(BLOCK_ENTITY_TAG.NBT, blockEntityTag);
        }
    }

    @Override
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);
        builder.put("blockMaterial", material.name());
        return builder;
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        if (blockEntityTag != null) {
            hash = 61 * hash + this.blockEntityTag.hashCode();
        }
        return original != hash ? CraftMetaBlockState.class.hashCode() ^ hash : hash;
    }

    @Override
    public boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaBlockState) {
            CraftMetaBlockState that = (CraftMetaBlockState) meta;

            return Objects.equal(this.blockEntityTag, that.blockEntityTag);
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaBlockState || blockEntityTag == null);
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && blockEntityTag == null;
    }

    @Override
    boolean applicableTo(Material type) {
        switch(type){         
            case FURNACE:
            case CHEST:
            case TRAPPED_CHEST:
            case JUKEBOX:
            case DISPENSER:
            case DROPPER:
            case SIGN:
            case MOB_SPAWNER:
            case NOTE_BLOCK:
            case PISTON_BASE:
            case BREWING_STAND_ITEM:
            case ENCHANTMENT_TABLE:
            case COMMAND:
            case BEACON:
            case DAYLIGHT_DETECTOR:
            case DAYLIGHT_DETECTOR_INVERTED:
            case HOPPER:
            case REDSTONE_COMPARATOR:
            case FLOWER_POT_ITEM:
                return true;
        }
        return false;
    }

    @Override
    public boolean hasBlockState() {
        return blockEntityTag != null;
    }

    @Override
    public BlockState getBlockState() {
        TileEntity te = blockEntityTag == null ? null : TileEntity.c(blockEntityTag);

        switch (material) {
        case SIGN:
        case SIGN_POST:
        case WALL_SIGN:
            if (te == null) {
                te = new TileEntitySign();
            }
            return new CraftSign(material, (TileEntitySign) te);
        case CHEST:
        case TRAPPED_CHEST:
            if (te == null) {
                te = new TileEntityChest();
            }
            return new CraftChest(material, (TileEntityChest) te);
        case BURNING_FURNACE:
        case FURNACE:
            if (te == null) {
                te = new TileEntityFurnace();
            }
            return new CraftFurnace(material, (TileEntityFurnace) te);
        case DISPENSER:
            if (te == null) {
                te = new TileEntityDispenser();
            }
            return new CraftDispenser(material, (TileEntityDispenser) te);
        case DROPPER:
            if (te == null) {
                te = new TileEntityDispenser();
            }
            return new CraftDropper(material, (TileEntityDropper) te);
        case HOPPER:
            if (te == null) {
                te = new TileEntityHopper();
            }
            return new CraftHopper(material, (TileEntityHopper) te);
        case MOB_SPAWNER:
            if (te == null) {
                te = new TileEntityMobSpawner();
            }
            return new CraftCreatureSpawner(material, (TileEntityMobSpawner) te);
        case NOTE_BLOCK:
            if (te == null) {
                te = new TileEntityNote();
            }
            return new CraftNoteBlock(material, (TileEntityNote) te);
        case JUKEBOX:
            if (te == null) {
                te = new BlockJukeBox.TileEntityRecordPlayer();
            }
            return new CraftJukebox(material, (BlockJukeBox.TileEntityRecordPlayer) te);
        case BREWING_STAND:
            if (te == null) {
                te = new TileEntityBrewingStand();
            }
            return new CraftBrewingStand(material, (TileEntityBrewingStand) te);
        case SKULL:
            if (te == null) {
                te = new TileEntitySkull();
            }
            return new CraftSkull(material, (TileEntitySkull) te);
        case COMMAND:
            if (te == null) {
                te = new TileEntityCommand();
            }
            return new CraftCommandBlock(material, (TileEntityCommand) te);
        case BEACON:
            if (te == null) {
                te = new TileEntityBeacon();
            }
            return new CraftBeacon(material, (TileEntityBeacon) te);
        case BANNER:
        case WALL_BANNER:
        case STANDING_BANNER:
            if (te == null) {
                te = new TileEntityBanner();
            }
            return new CraftBanner(material, (TileEntityBanner) te);
        default:
            throw new IllegalStateException("Missing blockState for " + material);
        }
    }

    @Override
    public void setBlockState(BlockState blockState) {
        Validate.notNull(blockState, "blockState must not be null");
        TileEntity te = ((CraftBlockState) blockState).getTileEntity();
        Validate.notNull(te, "Invalid blockState");

        boolean valid;
        switch (material) {
        case SIGN:
        case SIGN_POST:
        case WALL_SIGN:
            valid = te instanceof TileEntitySign;
            break;
        case CHEST:
        case TRAPPED_CHEST:
            valid = te instanceof TileEntityChest;
            break;
        case BURNING_FURNACE:
        case FURNACE:
            valid = te instanceof TileEntityFurnace;
            break;
        case DISPENSER:
            valid = te instanceof TileEntityDispenser;
            break;
        case DROPPER:
            valid = te instanceof TileEntityDropper;
            break;
        case HOPPER:
            valid = te instanceof TileEntityHopper;
            break;
        case MOB_SPAWNER:
            valid = te instanceof TileEntityMobSpawner;
            break;
        case NOTE_BLOCK:
            valid = te instanceof TileEntityNote;
            break;
        case JUKEBOX:
            valid = te instanceof BlockJukeBox.TileEntityRecordPlayer;
            break;
        case BREWING_STAND:
            valid = te instanceof TileEntityBrewingStand;
            break;
        case SKULL:
            valid = te instanceof TileEntitySkull;
            break;
        case COMMAND:
            valid = te instanceof TileEntityCommand;
            break;
        case BEACON:
            valid = te instanceof TileEntityBeacon;
            break;
        case BANNER:
        case WALL_BANNER:
        case STANDING_BANNER:
            valid = te instanceof TileEntityBanner;
            break;
        default:
            valid = false;
            break;
        }

        Validate.isTrue(valid, "Invalid blockState for " + material);

        blockEntityTag = new NBTTagCompound();
        te.b(blockEntityTag);
    }
}
