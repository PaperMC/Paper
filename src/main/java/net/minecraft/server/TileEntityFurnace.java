package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
// CraftBukkit start
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
// CraftBukkit end

public abstract class TileEntityFurnace extends TileEntityContainer implements IWorldInventory, RecipeHolder, AutoRecipeOutput, ITickable {

    private static final int[] g = new int[]{0};
    private static final int[] h = new int[]{2, 1};
    private static final int[] i = new int[]{1};
    protected NonNullList<ItemStack> items;
    public int burnTime;
    private int ticksForCurrentFuel;
    public int cookTime;
    public int cookTimeTotal;
    protected final IContainerProperties b;
    private final Object2IntOpenHashMap<MinecraftKey> n;
    protected final Recipes<? extends RecipeCooking> c;

    protected TileEntityFurnace(TileEntityTypes<?> tileentitytypes, Recipes<? extends RecipeCooking> recipes) {
        super(tileentitytypes);
        this.items = NonNullList.a(3, ItemStack.b);
        this.b = new IContainerProperties() {
            @Override
            public int getProperty(int i) {
                switch (i) {
                    case 0:
                        return TileEntityFurnace.this.burnTime;
                    case 1:
                        return TileEntityFurnace.this.ticksForCurrentFuel;
                    case 2:
                        return TileEntityFurnace.this.cookTime;
                    case 3:
                        return TileEntityFurnace.this.cookTimeTotal;
                    default:
                        return 0;
                }
            }

            @Override
            public void setProperty(int i, int j) {
                switch (i) {
                    case 0:
                        TileEntityFurnace.this.burnTime = j;
                        break;
                    case 1:
                        TileEntityFurnace.this.ticksForCurrentFuel = j;
                        break;
                    case 2:
                        TileEntityFurnace.this.cookTime = j;
                        break;
                    case 3:
                        TileEntityFurnace.this.cookTimeTotal = j;
                }

            }

            @Override
            public int a() {
                return 4;
            }
        };
        this.n = new Object2IntOpenHashMap();
        this.c = recipes;
    }

    public static Map<Item, Integer> f() {
        Map<Item, Integer> map = Maps.newLinkedHashMap();

        a(map, (IMaterial) Items.LAVA_BUCKET, 20000);
        a(map, (IMaterial) Blocks.COAL_BLOCK, 16000);
        a(map, (IMaterial) Items.BLAZE_ROD, 2400);
        a(map, (IMaterial) Items.COAL, 1600);
        a(map, (IMaterial) Items.CHARCOAL, 1600);
        a(map, (Tag) TagsItem.LOGS, 300);
        a(map, (Tag) TagsItem.PLANKS, 300);
        a(map, (Tag) TagsItem.WOODEN_STAIRS, 300);
        a(map, (Tag) TagsItem.WOODEN_SLABS, 150);
        a(map, (Tag) TagsItem.WOODEN_TRAPDOORS, 300);
        a(map, (Tag) TagsItem.WOODEN_PRESSURE_PLATES, 300);
        a(map, (IMaterial) Blocks.OAK_FENCE, 300);
        a(map, (IMaterial) Blocks.BIRCH_FENCE, 300);
        a(map, (IMaterial) Blocks.SPRUCE_FENCE, 300);
        a(map, (IMaterial) Blocks.JUNGLE_FENCE, 300);
        a(map, (IMaterial) Blocks.DARK_OAK_FENCE, 300);
        a(map, (IMaterial) Blocks.ACACIA_FENCE, 300);
        a(map, (IMaterial) Blocks.OAK_FENCE_GATE, 300);
        a(map, (IMaterial) Blocks.BIRCH_FENCE_GATE, 300);
        a(map, (IMaterial) Blocks.SPRUCE_FENCE_GATE, 300);
        a(map, (IMaterial) Blocks.JUNGLE_FENCE_GATE, 300);
        a(map, (IMaterial) Blocks.DARK_OAK_FENCE_GATE, 300);
        a(map, (IMaterial) Blocks.ACACIA_FENCE_GATE, 300);
        a(map, (IMaterial) Blocks.NOTE_BLOCK, 300);
        a(map, (IMaterial) Blocks.BOOKSHELF, 300);
        a(map, (IMaterial) Blocks.LECTERN, 300);
        a(map, (IMaterial) Blocks.JUKEBOX, 300);
        a(map, (IMaterial) Blocks.CHEST, 300);
        a(map, (IMaterial) Blocks.TRAPPED_CHEST, 300);
        a(map, (IMaterial) Blocks.CRAFTING_TABLE, 300);
        a(map, (IMaterial) Blocks.DAYLIGHT_DETECTOR, 300);
        a(map, (Tag) TagsItem.BANNERS, 300);
        a(map, (IMaterial) Items.BOW, 300);
        a(map, (IMaterial) Items.FISHING_ROD, 300);
        a(map, (IMaterial) Blocks.LADDER, 300);
        a(map, (Tag) TagsItem.SIGNS, 200);
        a(map, (IMaterial) Items.WOODEN_SHOVEL, 200);
        a(map, (IMaterial) Items.WOODEN_SWORD, 200);
        a(map, (IMaterial) Items.WOODEN_HOE, 200);
        a(map, (IMaterial) Items.WOODEN_AXE, 200);
        a(map, (IMaterial) Items.WOODEN_PICKAXE, 200);
        a(map, (Tag) TagsItem.WOODEN_DOORS, 200);
        a(map, (Tag) TagsItem.BOATS, 1200);
        a(map, (Tag) TagsItem.WOOL, 100);
        a(map, (Tag) TagsItem.WOODEN_BUTTONS, 100);
        a(map, (IMaterial) Items.STICK, 100);
        a(map, (Tag) TagsItem.SAPLINGS, 100);
        a(map, (IMaterial) Items.BOWL, 100);
        a(map, (Tag) TagsItem.CARPETS, 67);
        a(map, (IMaterial) Blocks.DRIED_KELP_BLOCK, 4001);
        a(map, (IMaterial) Items.CROSSBOW, 300);
        a(map, (IMaterial) Blocks.BAMBOO, 50);
        a(map, (IMaterial) Blocks.DEAD_BUSH, 100);
        a(map, (IMaterial) Blocks.SCAFFOLDING, 400);
        a(map, (IMaterial) Blocks.LOOM, 300);
        a(map, (IMaterial) Blocks.BARREL, 300);
        a(map, (IMaterial) Blocks.CARTOGRAPHY_TABLE, 300);
        a(map, (IMaterial) Blocks.FLETCHING_TABLE, 300);
        a(map, (IMaterial) Blocks.SMITHING_TABLE, 300);
        a(map, (IMaterial) Blocks.COMPOSTER, 300);
        return map;
    }

    // CraftBukkit start - add fields and methods
    private int maxStack = MAX_STACK;
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();

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

    @Override
    public int getMaxStackSize() {
        return maxStack;
    }

    public void setMaxStackSize(int size) {
        maxStack = size;
    }
    // CraftBukkit end

    private static boolean b(Item item) {
        return TagsItem.NON_FLAMMABLE_WOOD.isTagged(item);
    }

    private static void a(Map<Item, Integer> map, Tag<Item> tag, int i) {
        Iterator iterator = tag.getTagged().iterator();

        while (iterator.hasNext()) {
            Item item = (Item) iterator.next();

            if (!b(item)) {
                map.put(item, i);
            }
        }

    }

    private static void a(Map<Item, Integer> map, IMaterial imaterial, int i) {
        Item item = imaterial.getItem();

        if (b(item)) {
            if (SharedConstants.d) {
                throw (IllegalStateException) SystemUtils.c((Throwable) (new IllegalStateException("A developer tried to explicitly make fire resistant item " + item.h((ItemStack) null).getString() + " a furnace fuel. That will not work!")));
            }
        } else {
            map.put(item, i);
        }
    }

    private boolean isBurning() {
        return this.burnTime > 0;
    }

    @Override
    public void load(IBlockData iblockdata, NBTTagCompound nbttagcompound) {
        super.load(iblockdata, nbttagcompound);
        this.items = NonNullList.a(this.getSize(), ItemStack.b);
        ContainerUtil.b(nbttagcompound, this.items);
        this.burnTime = nbttagcompound.getShort("BurnTime");
        this.cookTime = nbttagcompound.getShort("CookTime");
        this.cookTimeTotal = nbttagcompound.getShort("CookTimeTotal");
        this.ticksForCurrentFuel = this.fuelTime((ItemStack) this.items.get(1));
        NBTTagCompound nbttagcompound1 = nbttagcompound.getCompound("RecipesUsed");
        Iterator iterator = nbttagcompound1.getKeys().iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();

            this.n.put(new MinecraftKey(s), nbttagcompound1.getInt(s));
        }

    }

    @Override
    public NBTTagCompound save(NBTTagCompound nbttagcompound) {
        super.save(nbttagcompound);
        nbttagcompound.setShort("BurnTime", (short) this.burnTime);
        nbttagcompound.setShort("CookTime", (short) this.cookTime);
        nbttagcompound.setShort("CookTimeTotal", (short) this.cookTimeTotal);
        ContainerUtil.a(nbttagcompound, this.items);
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();

        this.n.forEach((minecraftkey, integer) -> {
            nbttagcompound1.setInt(minecraftkey.toString(), integer);
        });
        nbttagcompound.set("RecipesUsed", nbttagcompound1);
        return nbttagcompound;
    }

    @Override
    public void tick() {
        boolean flag = this.isBurning();
        boolean flag1 = false;

        if (this.isBurning()) {
            --this.burnTime;
        }

        if (!this.world.isClientSide) {
            ItemStack itemstack = (ItemStack) this.items.get(1);

            if (!this.isBurning() && (itemstack.isEmpty() || ((ItemStack) this.items.get(0)).isEmpty())) {
                if (!this.isBurning() && this.cookTime > 0) {
                    this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.cookTimeTotal);
                }
            } else {
                IRecipe irecipe = this.world.getCraftingManager().craft((Recipes<RecipeCooking>) this.c, this, this.world).orElse(null); // Eclipse fail

                if (!this.isBurning() && this.canBurn(irecipe)) {
                    // CraftBukkit start
                    CraftItemStack fuel = CraftItemStack.asCraftMirror(itemstack);

                    FurnaceBurnEvent furnaceBurnEvent = new FurnaceBurnEvent(CraftBlock.at(this.world, this.position), fuel, fuelTime(itemstack));
                    this.world.getServer().getPluginManager().callEvent(furnaceBurnEvent);

                    if (furnaceBurnEvent.isCancelled()) {
                        return;
                    }

                    this.burnTime = furnaceBurnEvent.getBurnTime();
                    this.ticksForCurrentFuel = this.burnTime;
                    if (this.isBurning() && furnaceBurnEvent.isBurning()) {
                        // CraftBukkit end
                        flag1 = true;
                        if (!itemstack.isEmpty()) {
                            Item item = itemstack.getItem();

                            itemstack.subtract(1);
                            if (itemstack.isEmpty()) {
                                Item item1 = item.getCraftingRemainingItem();

                                this.items.set(1, item1 == null ? ItemStack.b : new ItemStack(item1));
                            }
                        }
                    }
                }

                if (this.isBurning() && this.canBurn(irecipe)) {
                    ++this.cookTime;
                    if (this.cookTime == this.cookTimeTotal) {
                        this.cookTime = 0;
                        this.cookTimeTotal = this.getRecipeCookingTime();
                        this.burn(irecipe);
                        flag1 = true;
                    }
                } else {
                    this.cookTime = 0;
                }
            }

            if (flag != this.isBurning()) {
                flag1 = true;
                this.world.setTypeAndData(this.position, (IBlockData) this.world.getType(this.position).set(BlockFurnace.LIT, this.isBurning()), 3);
            }
        }

        if (flag1) {
            this.update();
        }

    }

    protected boolean canBurn(@Nullable IRecipe<?> irecipe) {
        if (!((ItemStack) this.items.get(0)).isEmpty() && irecipe != null) {
            ItemStack itemstack = irecipe.getResult();

            if (itemstack.isEmpty()) {
                return false;
            } else {
                ItemStack itemstack1 = (ItemStack) this.items.get(2);

                return itemstack1.isEmpty() ? true : (!itemstack1.doMaterialsMatch(itemstack) ? false : (itemstack1.getCount() < this.getMaxStackSize() && itemstack1.getCount() < itemstack1.getMaxStackSize() ? true : itemstack1.getCount() < itemstack.getMaxStackSize()));
            }
        } else {
            return false;
        }
    }

    private void burn(@Nullable IRecipe<?> irecipe) {
        if (irecipe != null && this.canBurn(irecipe)) {
            ItemStack itemstack = (ItemStack) this.items.get(0);
            ItemStack itemstack1 = irecipe.getResult();
            ItemStack itemstack2 = (ItemStack) this.items.get(2);

            // CraftBukkit start - fire FurnaceSmeltEvent
            CraftItemStack source = CraftItemStack.asCraftMirror(itemstack);
            org.bukkit.inventory.ItemStack result = CraftItemStack.asBukkitCopy(itemstack1);

            FurnaceSmeltEvent furnaceSmeltEvent = new FurnaceSmeltEvent(this.world.getWorld().getBlockAt(position.getX(), position.getY(), position.getZ()), source, result);
            this.world.getServer().getPluginManager().callEvent(furnaceSmeltEvent);

            if (furnaceSmeltEvent.isCancelled()) {
                return;
            }

            result = furnaceSmeltEvent.getResult();
            itemstack1 = CraftItemStack.asNMSCopy(result);

            if (!itemstack1.isEmpty()) {
                if (itemstack2.isEmpty()) {
                    this.items.set(2, itemstack1.cloneItemStack());
                } else if (CraftItemStack.asCraftMirror(itemstack2).isSimilar(result)) {
                    itemstack2.add(itemstack1.getCount());
                } else {
                    return;
                }
            }

            /*
            if (itemstack2.isEmpty()) {
                this.items.set(2, itemstack1.cloneItemStack());
            } else if (itemstack2.getItem() == itemstack1.getItem()) {
                itemstack2.add(1);
            }
            */
            // CraftBukkit end

            if (!this.world.isClientSide) {
                this.a(irecipe);
            }

            if (itemstack.getItem() == Blocks.WET_SPONGE.getItem() && !((ItemStack) this.items.get(1)).isEmpty() && ((ItemStack) this.items.get(1)).getItem() == Items.BUCKET) {
                this.items.set(1, new ItemStack(Items.WATER_BUCKET));
            }

            itemstack.subtract(1);
        }
    }

    protected int fuelTime(ItemStack itemstack) {
        if (itemstack.isEmpty()) {
            return 0;
        } else {
            Item item = itemstack.getItem();

            return (Integer) f().getOrDefault(item, 0);
        }
    }

    protected int getRecipeCookingTime() {
        return (this.hasWorld()) ? (Integer) this.world.getCraftingManager().craft((Recipes<RecipeCooking>) this.c, this, this.world).map(RecipeCooking::getCookingTime).orElse(200) : 200; // CraftBukkit - SPIGOT-4302 // Eclipse fail
    }

    public static boolean isFuel(ItemStack itemstack) {
        return f().containsKey(itemstack.getItem());
    }

    @Override
    public int[] getSlotsForFace(EnumDirection enumdirection) {
        return enumdirection == EnumDirection.DOWN ? TileEntityFurnace.h : (enumdirection == EnumDirection.UP ? TileEntityFurnace.g : TileEntityFurnace.i);
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemstack, @Nullable EnumDirection enumdirection) {
        return this.b(i, itemstack);
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemstack, EnumDirection enumdirection) {
        if (enumdirection == EnumDirection.DOWN && i == 1) {
            Item item = itemstack.getItem();

            if (item != Items.WATER_BUCKET && item != Items.BUCKET) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int getSize() {
        return this.items.size();
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
        return (ItemStack) this.items.get(i);
    }

    @Override
    public ItemStack splitStack(int i, int j) {
        return ContainerUtil.a(this.items, i, j);
    }

    @Override
    public ItemStack splitWithoutUpdate(int i) {
        return ContainerUtil.a(this.items, i);
    }

    @Override
    public void setItem(int i, ItemStack itemstack) {
        ItemStack itemstack1 = (ItemStack) this.items.get(i);
        boolean flag = !itemstack.isEmpty() && itemstack.doMaterialsMatch(itemstack1) && ItemStack.equals(itemstack, itemstack1);

        this.items.set(i, itemstack);
        if (itemstack.getCount() > this.getMaxStackSize()) {
            itemstack.setCount(this.getMaxStackSize());
        }

        if (i == 0 && !flag) {
            this.cookTimeTotal = this.getRecipeCookingTime();
            this.cookTime = 0;
            this.update();
        }

    }

    @Override
    public boolean a(EntityHuman entityhuman) {
        return this.world.getTileEntity(this.position) != this ? false : entityhuman.h((double) this.position.getX() + 0.5D, (double) this.position.getY() + 0.5D, (double) this.position.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public boolean b(int i, ItemStack itemstack) {
        if (i == 2) {
            return false;
        } else if (i != 1) {
            return true;
        } else {
            ItemStack itemstack1 = (ItemStack) this.items.get(1);

            return isFuel(itemstack) || itemstack.getItem() == Items.BUCKET && itemstack1.getItem() != Items.BUCKET;
        }
    }

    @Override
    public void clear() {
        this.items.clear();
    }

    @Override
    public void a(@Nullable IRecipe<?> irecipe) {
        if (irecipe != null) {
            MinecraftKey minecraftkey = irecipe.getKey();

            this.n.addTo(minecraftkey, 1);
        }

    }

    @Nullable
    @Override
    public IRecipe<?> ak_() {
        return null;
    }

    @Override
    public void b(EntityHuman entityhuman) {}

    public void d(EntityHuman entityhuman, ItemStack itemstack, int amount) { // CraftBukkit
        List<IRecipe<?>> list = this.a(entityhuman.world, entityhuman.getPositionVector(), entityhuman, itemstack, amount); // CraftBukkit

        entityhuman.discoverRecipes(list);
        this.n.clear();
    }

    public List<IRecipe<?>> a(World world, Vec3D vec3d) {
        // CraftBukkit start
        return this.a(world, vec3d, null, null, 0);
    }

    public List<IRecipe<?>> a(World world, Vec3D vec3d, EntityHuman entityhuman, ItemStack itemstack, int amount) {
        // CraftBukkit end
        List<IRecipe<?>> list = Lists.newArrayList();
        ObjectIterator objectiterator = this.n.object2IntEntrySet().iterator();

        while (objectiterator.hasNext()) {
            Entry<MinecraftKey> entry = (Entry) objectiterator.next();

            world.getCraftingManager().getRecipe((MinecraftKey) entry.getKey()).ifPresent((irecipe) -> {
                list.add(irecipe);
                a(world, vec3d, entry.getIntValue(), ((RecipeCooking) irecipe).getExperience(), entityhuman, itemstack, amount); // CraftBukkit
            });
        }

        return list;
    }

    private void a(World world, Vec3D vec3d, int i, float f, EntityHuman entityhuman, ItemStack itemstack, int amount) { // CraftBukkit
        int j = MathHelper.d((float) i * f);
        float f1 = MathHelper.h((float) i * f);

        if (f1 != 0.0F && Math.random() < (double) f1) {
            ++j;
        }

        // CraftBukkit start - fire FurnaceExtractEvent
        if (amount != 0) {
            FurnaceExtractEvent event = new FurnaceExtractEvent((Player) entityhuman.getBukkitEntity(), CraftBlock.at(world, position), org.bukkit.craftbukkit.util.CraftMagicNumbers.getMaterial(itemstack.getItem()), amount, j);
            world.getServer().getPluginManager().callEvent(event);
            j = event.getExpToDrop();
        }
        // CraftBukkit end

        while (j > 0) {
            int k = EntityExperienceOrb.getOrbValue(j);

            j -= k;
            world.addEntity(new EntityExperienceOrb(world, vec3d.x, vec3d.y, vec3d.z, k));
        }

    }

    @Override
    public void a(AutoRecipeStackManager autorecipestackmanager) {
        Iterator iterator = this.items.iterator();

        while (iterator.hasNext()) {
            ItemStack itemstack = (ItemStack) iterator.next();

            autorecipestackmanager.b(itemstack);
        }

    }
}
