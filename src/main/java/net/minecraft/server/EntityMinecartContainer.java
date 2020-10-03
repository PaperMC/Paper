package net.minecraft.server;

import java.util.Iterator;
import javax.annotation.Nullable;
// CraftBukkit start
import java.util.List;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.InventoryHolder;
// CraftBukkit end

public abstract class EntityMinecartContainer extends EntityMinecartAbstract implements IInventory, ITileInventory {

    private NonNullList<ItemStack> items;
    private boolean c;
    @Nullable
    public MinecraftKey lootTable;
    public long lootTableSeed;

    // CraftBukkit start
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();
    private int maxStack = MAX_STACK;

    public List<ItemStack> getContents() {
        return this.items;
    }

    public void onOpen(CraftHumanEntity who) {
        transaction.add(who);
    }

    public void onClose(CraftHumanEntity who) {
        transaction.remove(who);
    }

    public List<HumanEntity> getViewers() {
        return transaction;
    }

    public InventoryHolder getOwner() {
        org.bukkit.entity.Entity cart = getBukkitEntity();
        if(cart instanceof InventoryHolder) return (InventoryHolder) cart;
        return null;
    }

    @Override
    public int getMaxStackSize() {
        return maxStack;
    }

    public void setMaxStackSize(int size) {
        maxStack = size;
    }

    @Override
    public Location getLocation() {
        return getBukkitEntity().getLocation();
    }
    // CraftBukkit end

    protected EntityMinecartContainer(EntityTypes<?> entitytypes, World world) {
        super(entitytypes, world);
        this.items = NonNullList.a(this.getSize(), ItemStack.b); // CraftBukkit - SPIGOT-3513
        this.c = true;
    }

    protected EntityMinecartContainer(EntityTypes<?> entitytypes, double d0, double d1, double d2, World world) {
        super(entitytypes, world, d0, d1, d2);
        this.items = NonNullList.a(this.getSize(), ItemStack.b); // CraftBukkit - SPIGOT-3513
        this.c = true;
    }

    @Override
    public void a(DamageSource damagesource) {
        super.a(damagesource);
        if (this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
            InventoryUtils.dropEntity(this.world, this, this);
            if (!this.world.isClientSide) {
                Entity entity = damagesource.j();

                if (entity != null && entity.getEntityType() == EntityTypes.PLAYER) {
                    PiglinAI.a((EntityHuman) entity, true);
                }
            }
        }

    }

    @Override
    public boolean isEmpty() {
        Iterator iterator = this.items.iterator();

        ItemStack itemstack;

        do {
            if (!iterator.hasNext()) {
                return true;
            }

            itemstack = (ItemStack) iterator.next();
        } while (itemstack.isEmpty());

        return false;
    }

    @Override
    public ItemStack getItem(int i) {
        this.d((EntityHuman) null);
        return (ItemStack) this.items.get(i);
    }

    @Override
    public ItemStack splitStack(int i, int j) {
        this.d((EntityHuman) null);
        return ContainerUtil.a(this.items, i, j);
    }

    @Override
    public ItemStack splitWithoutUpdate(int i) {
        this.d((EntityHuman) null);
        ItemStack itemstack = (ItemStack) this.items.get(i);

        if (itemstack.isEmpty()) {
            return ItemStack.b;
        } else {
            this.items.set(i, ItemStack.b);
            return itemstack;
        }
    }

    @Override
    public void setItem(int i, ItemStack itemstack) {
        this.d((EntityHuman) null);
        this.items.set(i, itemstack);
        if (!itemstack.isEmpty() && itemstack.getCount() > this.getMaxStackSize()) {
            itemstack.setCount(this.getMaxStackSize());
        }

    }

    @Override
    public boolean a_(int i, ItemStack itemstack) {
        if (i >= 0 && i < this.getSize()) {
            this.setItem(i, itemstack);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void update() {}

    @Override
    public boolean a(EntityHuman entityhuman) {
        return this.dead ? false : entityhuman.h((Entity) this) <= 64.0D;
    }

    @Nullable
    @Override
    public Entity b(WorldServer worldserver) {
        this.c = false;
        return super.b(worldserver);
    }

    @Override
    public void die() {
        if (!this.world.isClientSide && this.c) {
            InventoryUtils.dropEntity(this.world, this, this);
        }

        super.die();
    }

    @Override
    protected void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        if (this.lootTable != null) {
            nbttagcompound.setString("LootTable", this.lootTable.toString());
            if (this.lootTableSeed != 0L) {
                nbttagcompound.setLong("LootTableSeed", this.lootTableSeed);
            }
        } else {
            ContainerUtil.a(nbttagcompound, this.items);
        }

    }

    @Override
    protected void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
        this.items = NonNullList.a(this.getSize(), ItemStack.b);
        if (nbttagcompound.hasKeyOfType("LootTable", 8)) {
            this.lootTable = new MinecraftKey(nbttagcompound.getString("LootTable"));
            this.lootTableSeed = nbttagcompound.getLong("LootTableSeed");
        } else {
            ContainerUtil.b(nbttagcompound, this.items);
        }

    }

    @Override
    public EnumInteractionResult a(EntityHuman entityhuman, EnumHand enumhand) {
        entityhuman.openContainer(this);
        if (!entityhuman.world.isClientSide) {
            PiglinAI.a(entityhuman, true);
            return EnumInteractionResult.CONSUME;
        } else {
            return EnumInteractionResult.SUCCESS;
        }
    }

    @Override
    protected void decelerate() {
        float f = 0.98F;

        if (this.lootTable == null) {
            int i = 15 - Container.b((IInventory) this);

            f += (float) i * 0.001F;
        }

        this.setMot(this.getMot().d((double) f, 0.0D, (double) f));
    }

    public void d(@Nullable EntityHuman entityhuman) {
        if (this.lootTable != null && this.world.getMinecraftServer() != null) {
            LootTable loottable = this.world.getMinecraftServer().getLootTableRegistry().getLootTable(this.lootTable);

            if (entityhuman instanceof EntityPlayer) {
                CriterionTriggers.N.a((EntityPlayer) entityhuman, this.lootTable);
            }

            this.lootTable = null;
            LootTableInfo.Builder loottableinfo_builder = (new LootTableInfo.Builder((WorldServer) this.world)).set(LootContextParameters.ORIGIN, this.getPositionVector()).a(this.lootTableSeed);

            if (entityhuman != null) {
                loottableinfo_builder.a(entityhuman.eT()).set(LootContextParameters.THIS_ENTITY, entityhuman);
            }

            loottable.fillInventory(this, loottableinfo_builder.build(LootContextParameterSets.CHEST));
        }

    }

    @Override
    public void clear() {
        this.d((EntityHuman) null);
        this.items.clear();
    }

    public void setLootTable(MinecraftKey minecraftkey, long i) {
        this.lootTable = minecraftkey;
        this.lootTableSeed = i;
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerinventory, EntityHuman entityhuman) {
        if (this.lootTable != null && entityhuman.isSpectator()) {
            return null;
        } else {
            this.d(playerinventory.player);
            return this.a(i, playerinventory);
        }
    }

    protected abstract Container a(int i, PlayerInventory playerinventory);
}
