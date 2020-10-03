package net.minecraft.server;

import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Supplier;
// CraftBukkit start
import org.bukkit.craftbukkit.persistence.CraftPersistentDataContainer;
import org.bukkit.craftbukkit.persistence.CraftPersistentDataTypeRegistry;
import org.bukkit.inventory.InventoryHolder;
// CraftBukkit end

public abstract class TileEntity {

    // CraftBukkit start - data containers
    private static final CraftPersistentDataTypeRegistry DATA_TYPE_REGISTRY = new CraftPersistentDataTypeRegistry();
    public CraftPersistentDataContainer persistentDataContainer;
    // CraftBukkit end
    private static final Logger LOGGER = LogManager.getLogger();
    private final TileEntityTypes<?> tileType;
    @Nullable
    protected World world;
    protected BlockPosition position;
    protected boolean f;
    @Nullable
    private IBlockData c;
    private boolean g;

    public TileEntity(TileEntityTypes<?> tileentitytypes) {
        this.position = BlockPosition.ZERO;
        this.tileType = tileentitytypes;
    }

    @Nullable
    public World getWorld() {
        return this.world;
    }

    public void setLocation(World world, BlockPosition blockposition) {
        this.world = world;
        this.position = blockposition.immutableCopy();
    }

    public boolean hasWorld() {
        return this.world != null;
    }

    public void load(IBlockData iblockdata, NBTTagCompound nbttagcompound) {
        this.position = new BlockPosition(nbttagcompound.getInt("x"), nbttagcompound.getInt("y"), nbttagcompound.getInt("z"));
        // CraftBukkit start - read container
        this.persistentDataContainer = new CraftPersistentDataContainer(DATA_TYPE_REGISTRY);

        NBTTagCompound persistentDataTag = nbttagcompound.getCompound("PublicBukkitValues");
        if (persistentDataTag != null) {
            this.persistentDataContainer.putAll(persistentDataTag);
        }
        // CraftBukkit end
    }

    public NBTTagCompound save(NBTTagCompound nbttagcompound) {
        return this.b(nbttagcompound);
    }

    private NBTTagCompound b(NBTTagCompound nbttagcompound) {
        MinecraftKey minecraftkey = TileEntityTypes.a(this.getTileType());

        if (minecraftkey == null) {
            throw new RuntimeException(this.getClass() + " is missing a mapping! This is a bug!");
        } else {
            nbttagcompound.setString("id", minecraftkey.toString());
            nbttagcompound.setInt("x", this.position.getX());
            nbttagcompound.setInt("y", this.position.getY());
            nbttagcompound.setInt("z", this.position.getZ());
            // CraftBukkit start - store container
            if (this.persistentDataContainer != null && !this.persistentDataContainer.isEmpty()) {
                nbttagcompound.set("PublicBukkitValues", this.persistentDataContainer.toTagCompound());
            }
            // CraftBukkit end
            return nbttagcompound;
        }
    }

    @Nullable
    public static TileEntity create(IBlockData iblockdata, NBTTagCompound nbttagcompound) {
        String s = nbttagcompound.getString("id");

        return (TileEntity) IRegistry.BLOCK_ENTITY_TYPE.getOptional(new MinecraftKey(s)).map((tileentitytypes) -> {
            try {
                return tileentitytypes.a();
            } catch (Throwable throwable) {
                TileEntity.LOGGER.error("Failed to create block entity {}", s, throwable);
                return null;
            }
        }).map((tileentity) -> {
            try {
                tileentity.load(iblockdata, nbttagcompound);
                return tileentity;
            } catch (Throwable throwable) {
                TileEntity.LOGGER.error("Failed to load data for block entity {}", s, throwable);
                return null;
            }
        }).orElseGet(() -> {
            TileEntity.LOGGER.warn("Skipping BlockEntity with id {}", s);
            return null;
        });
    }

    public void update() {
        if (this.world != null) {
            this.c = this.world.getType(this.position);
            this.world.b(this.position, this);
            if (!this.c.isAir()) {
                this.world.updateAdjacentComparators(this.position, this.c.getBlock());
            }
        }

    }

    public BlockPosition getPosition() {
        return this.position;
    }

    public IBlockData getBlock() {
        if (this.c == null) {
            this.c = this.world.getType(this.position);
        }

        return this.c;
    }

    @Nullable
    public PacketPlayOutTileEntityData getUpdatePacket() {
        return null;
    }

    public NBTTagCompound b() {
        return this.b(new NBTTagCompound());
    }

    public boolean isRemoved() {
        return this.f;
    }

    public void al_() {
        this.f = true;
    }

    public void r() {
        this.f = false;
    }

    public boolean setProperty(int i, int j) {
        return false;
    }

    public void invalidateBlockCache() {
        this.c = null;
    }

    public void a(CrashReportSystemDetails crashreportsystemdetails) {
        crashreportsystemdetails.a("Name", () -> {
            return IRegistry.BLOCK_ENTITY_TYPE.getKey(this.getTileType()) + " // " + this.getClass().getCanonicalName();
        });
        if (this.world != null) {
            CrashReportSystemDetails.a(crashreportsystemdetails, this.position, this.getBlock());
            CrashReportSystemDetails.a(crashreportsystemdetails, this.position, this.world.getType(this.position));
        }
    }

    public void setPosition(BlockPosition blockposition) {
        this.position = blockposition.immutableCopy();
    }

    public boolean isFilteredNBT() {
        return false;
    }

    public void a(EnumBlockRotation enumblockrotation) {}

    public void a(EnumBlockMirror enumblockmirror) {}

    public TileEntityTypes<?> getTileType() {
        return this.tileType;
    }

    public void w() {
        if (!this.g) {
            this.g = true;
            TileEntity.LOGGER.warn("Block entity invalid: {} @ {}", new Supplier[]{() -> {
                        return IRegistry.BLOCK_ENTITY_TYPE.getKey(this.getTileType());
                    }, this::getPosition});
        }
    }

    // CraftBukkit start - add method
    public InventoryHolder getOwner() {
        if (world == null) return null;
        org.bukkit.block.BlockState state = world.getWorld().getBlockAt(position.getX(), position.getY(), position.getZ()).getState();
        if (state instanceof InventoryHolder) return (InventoryHolder) state;
        return null;
    }
    // CraftBukkit end
}
