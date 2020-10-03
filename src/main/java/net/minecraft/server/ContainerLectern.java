package net.minecraft.server;

// CraftBukkit start
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.inventory.CraftInventoryLectern;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTakeLecternBookEvent;
// CraftBukkit end

public class ContainerLectern extends Container {

    // CraftBukkit start
    private CraftInventoryView bukkitEntity = null;
    private Player player;

    @Override
    public CraftInventoryView getBukkitView() {
        if (bukkitEntity != null) {
            return bukkitEntity;
        }

        CraftInventoryLectern inventory = new CraftInventoryLectern(this.inventory);
        bukkitEntity = new CraftInventoryView(this.player, inventory, this);
        return bukkitEntity;
    }
    // CraftBukkit end
    private final IInventory inventory;
    private final IContainerProperties containerProperties;

    // CraftBukkit start - add player
    public ContainerLectern(int i, PlayerInventory playerinventory) {
        this(i, new InventorySubcontainer(1), new ContainerProperties(1), playerinventory);
    }

    public ContainerLectern(int i, IInventory iinventory, IContainerProperties icontainerproperties, PlayerInventory playerinventory) {
        // CraftBukkit end
        super(Containers.LECTERN, i);
        a(iinventory, 1);
        a(icontainerproperties, 1);
        this.inventory = iinventory;
        this.containerProperties = icontainerproperties;
        this.a(new Slot(iinventory, 0, 0, 0) {
            @Override
            public void d() {
                super.d();
                ContainerLectern.this.a(this.inventory);
            }
        });
        this.a(icontainerproperties);
        player = (Player) playerinventory.player.getBukkitEntity(); // CraftBukkit
    }

    @Override
    public boolean a(EntityHuman entityhuman, int i) {
        int j;

        if (i >= 100) {
            j = i - 100;
            this.a(0, j);
            return true;
        } else {
            switch (i) {
                case 1:
                    j = this.containerProperties.getProperty(0);
                    this.a(0, j - 1);
                    return true;
                case 2:
                    j = this.containerProperties.getProperty(0);
                    this.a(0, j + 1);
                    return true;
                case 3:
                    if (!entityhuman.eJ()) {
                        return false;
                    }

                    // CraftBukkit start - Event for taking the book
                    PlayerTakeLecternBookEvent event = new PlayerTakeLecternBookEvent(player, ((CraftInventoryLectern) getBukkitView().getTopInventory()).getHolder());
                    Bukkit.getServer().getPluginManager().callEvent(event);
                    if (event.isCancelled()) {
                        return false;
                    }
                    // CraftBukkit end
                    ItemStack itemstack = this.inventory.splitWithoutUpdate(0);

                    this.inventory.update();
                    if (!entityhuman.inventory.pickup(itemstack)) {
                        entityhuman.drop(itemstack, false);
                    }

                    return true;
                default:
                    return false;
            }
        }
    }

    @Override
    public void a(int i, int j) {
        super.a(i, j);
        this.c();
    }

    @Override
    public boolean canUse(EntityHuman entityhuman) {
        if (!this.checkReachable) return true; // CraftBukkit
        return this.inventory.a(entityhuman);
    }
}
