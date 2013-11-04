package net.minecraft.server;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.bukkit.inventory.InventoryHolder; // CraftBukkit

public class TileEntity {

    private static final Logger a = LogManager.getLogger();
    private static Map i = new HashMap();
    private static Map j = new HashMap();
    protected World world;
    public int x;
    public int y;
    public int z;
    protected boolean f;
    public int g = -1;
    public Block h;

    public TileEntity() {}

    private static void a(Class oclass, String s) {
        if (i.containsKey(s)) {
            throw new IllegalArgumentException("Duplicate id: " + s);
        } else {
            i.put(s, oclass);
            j.put(oclass, s);
        }
    }

    public World getWorld() {
        return this.world;
    }

    public void a(World world) {
        this.world = world;
    }

    public boolean o() {
        return this.world != null;
    }

    public void a(NBTTagCompound nbttagcompound) {
        this.x = nbttagcompound.getInt("x");
        this.y = nbttagcompound.getInt("y");
        this.z = nbttagcompound.getInt("z");
    }

    public void b(NBTTagCompound nbttagcompound) {
        String s = (String) j.get(this.getClass());

        if (s == null) {
            throw new RuntimeException(this.getClass() + " is missing a mapping! This is a bug!");
        } else {
            nbttagcompound.setString("id", s);
            nbttagcompound.setInt("x", this.x);
            nbttagcompound.setInt("y", this.y);
            nbttagcompound.setInt("z", this.z);
        }
    }

    public void h() {}

    public static TileEntity c(NBTTagCompound nbttagcompound) {
        TileEntity tileentity = null;

        try {
            Class oclass = (Class) i.get(nbttagcompound.getString("id"));

            if (oclass != null) {
                tileentity = (TileEntity) oclass.newInstance();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        if (tileentity != null) {
            tileentity.a(nbttagcompound);
        } else {
            a.warn("Skipping BlockEntity with id " + nbttagcompound.getString("id"));
        }

        return tileentity;
    }

    public int p() {
        if (this.g == -1) {
            this.g = this.world.getData(this.x, this.y, this.z);
        }

        return this.g;
    }

    public void update() {
        if (this.world != null) {
            this.g = this.world.getData(this.x, this.y, this.z);
            this.world.b(this.x, this.y, this.z, this);
            if (this.q() != Blocks.AIR) {
                this.world.f(this.x, this.y, this.z, this.q());
            }
        }
    }

    public Block q() {
        if (this.h == null) {
            this.h = this.world.getType(this.x, this.y, this.z);
        }

        return this.h;
    }

    public Packet getUpdatePacket() {
        return null;
    }

    public boolean r() {
        return this.f;
    }

    public void s() {
        this.f = true;
    }

    public void t() {
        this.f = false;
    }

    public boolean c(int i, int j) {
        return false;
    }

    public void u() {
        this.h = null;
        this.g = -1;
    }

    public void a(CrashReportSystemDetails crashreportsystemdetails) {
        crashreportsystemdetails.a("Name", (Callable) (new CrashReportTileEntityName(this)));
        CrashReportSystemDetails.a(crashreportsystemdetails, this.x, this.y, this.z, this.q(), this.p());
        crashreportsystemdetails.a("Actual block type", (Callable) (new CrashReportTileEntityType(this)));
        crashreportsystemdetails.a("Actual block data value", (Callable) (new CrashReportTileEntityData(this)));
    }

    static Map v() {
        return j;
    }

    static {
        a(TileEntityFurnace.class, "Furnace");
        a(TileEntityChest.class, "Chest");
        a(TileEntityEnderChest.class, "EnderChest");
        a(TileEntityRecordPlayer.class, "RecordPlayer");
        a(TileEntityDispenser.class, "Trap");
        a(TileEntityDropper.class, "Dropper");
        a(TileEntitySign.class, "Sign");
        a(TileEntityMobSpawner.class, "MobSpawner");
        a(TileEntityNote.class, "Music");
        a(TileEntityPiston.class, "Piston");
        a(TileEntityBrewingStand.class, "Cauldron");
        a(TileEntityEnchantTable.class, "EnchantTable");
        a(TileEntityEnderPortal.class, "Airportal");
        a(TileEntityCommand.class, "Control");
        a(TileEntityBeacon.class, "Beacon");
        a(TileEntitySkull.class, "Skull");
        a(TileEntityLightDetector.class, "DLDetector");
        a(TileEntityHopper.class, "Hopper");
        a(TileEntityComparator.class, "Comparator");
        a(TileEntityFlowerPot.class, "FlowerPot");
    }

    // CraftBukkit start
    public InventoryHolder getOwner() {
        org.bukkit.block.BlockState state = world.getWorld().getBlockAt(x, y, z).getState();
        if (state instanceof InventoryHolder) return (InventoryHolder) state;
        return null;
    }
    // CraftBukkit end
}
