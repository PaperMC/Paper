package net.minecraft.server;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonParseException;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// CraftBukkit start
import com.mojang.serialization.Dynamic;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.world.StructureGrowEvent;
// CraftBukkit end

public final class ItemStack {

    public static final Codec<ItemStack> a = RecordCodecBuilder.create((instance) -> {
        return instance.group(IRegistry.ITEM.fieldOf("id").forGetter((itemstack) -> {
            return itemstack.item;
        }), Codec.INT.fieldOf("Count").forGetter((itemstack) -> {
            return itemstack.count;
        }), NBTTagCompound.a.optionalFieldOf("tag").forGetter((itemstack) -> {
            return Optional.ofNullable(itemstack.tag);
        })).apply(instance, ItemStack::new);
    });
    private static final Logger LOGGER = LogManager.getLogger();
    public static final ItemStack b = new ItemStack((Item) null);public static final ItemStack NULL_ITEM = b; // Paper - OBFHELPER
    public static final DecimalFormat c = (DecimalFormat) SystemUtils.a((new DecimalFormat("#.##")), (decimalformat) -> { // CraftBukkit - decompile error
        decimalformat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT));
    });
    private static final ChatModifier e = ChatModifier.a.setColor(EnumChatFormat.DARK_PURPLE).setItalic(true);
    private int count;
    private int g;
    @Deprecated
    private Item item;
    NBTTagCompound tag; // Paper -> package private
    private boolean j;
    private Entity k;
    private ShapeDetectorBlock l;
    private boolean m;
    private ShapeDetectorBlock n;
    private boolean o;

    // Paper start
    private static final java.util.Comparator<? super NBTTagCompound> enchantSorter = java.util.Comparator.comparing(o -> o.getString("id"));
    private void processEnchantOrder(NBTTagCompound tag) {
        if (tag == null || !tag.hasKeyOfType("Enchantments", 9)) {
            return;
        }
        NBTTagList list = tag.getList("Enchantments", 10);
        if (list.size() < 2) {
            return;
        }
        try {
            //noinspection unchecked
            list.sort((Comparator<? super NBTBase>) enchantSorter); // Paper
        } catch (Exception ignored) {}
    }

    private void processText() {
        NBTTagCompound display = getSubTag("display");
        if (display != null) {
            if (display.hasKeyOfType("Name", 8)) {
                String json = display.getString("Name");
                if (json != null && json.contains("\u00A7")) {
                    try {
                        display.set("Name", convert(json));
                    } catch (JsonParseException jsonparseexception) {
                        display.remove("Name");
                    }
                }
            }
            if (display.hasKeyOfType("Lore", 9)) {
                NBTTagList list = display.getList("Lore", 8);
                for (int index = 0; index < list.size(); index++) {
                    String json = list.getString(index);
                    if (json != null && json.contains("\u00A7")) { // Only try if it has legacy in the unparsed json
                        try {
                            list.set(index, convert(json));
                        } catch (JsonParseException e) {
                            list.set(index, NBTTagString.create(org.bukkit.craftbukkit.util.CraftChatMessage.toJSON(new ChatComponentText(""))));
                        }
                    }
                }
            }
        }
    }

    private NBTTagString convert(String json) {
        IChatBaseComponent component = IChatBaseComponent.ChatSerializer.jsonToComponent(json);
        if (component instanceof ChatComponentText && component.getText().contains("\u00A7") && component.getSiblings().isEmpty()) {
            // Only convert if the root component is a single comp with legacy in it, don't convert already normal components
            component = org.bukkit.craftbukkit.util.CraftChatMessage.fromString(component.getText())[0];
        }
        return NBTTagString.create(org.bukkit.craftbukkit.util.CraftChatMessage.toJSON(component));
    }
    // Paper end

    public ItemStack(IMaterial imaterial) {
        this(imaterial, 1);
    }

    private ItemStack(IMaterial imaterial, int i, Optional<NBTTagCompound> optional) {
        this(imaterial, i);
        optional.ifPresent(this::setTag);
    }

    public ItemStack(IMaterial imaterial, int i) {
        this.item = imaterial == null ? null : imaterial.getItem();
        this.count = i;
        if (this.item != null && this.item.usesDurability()) {
            this.setDamage(this.getDamage());
        }

        this.checkEmpty();
    }

    // Called to run this stack through the data converter to handle older storage methods and serialized items
    public void convertStack(int version) {
        if (0 < version && version < CraftMagicNumbers.INSTANCE.getDataVersion()) {
            NBTTagCompound savedStack = new NBTTagCompound();
            this.save(savedStack);
            savedStack = (NBTTagCompound) MinecraftServer.getServer().dataConverterManager.update(DataConverterTypes.ITEM_STACK, new Dynamic(DynamicOpsNBT.a, savedStack), version, CraftMagicNumbers.INSTANCE.getDataVersion()).getValue();
            this.load(savedStack);
        }
    }

    private void checkEmpty() {
        if (this.j && this == ItemStack.b) throw new AssertionError("TRAP"); // CraftBukkit
        this.j = false;
        this.j = this.isEmpty();
    }

    // CraftBukkit - break into own method
    private void load(NBTTagCompound nbttagcompound) {
        this.item = (Item) IRegistry.ITEM.get(new MinecraftKey(nbttagcompound.getString("id")));
        this.count = nbttagcompound.getByte("Count");
        if (nbttagcompound.hasKeyOfType("tag", 10)) {
            // CraftBukkit start - make defensive copy as this data may be coming from the save thread
            this.tag = (NBTTagCompound) nbttagcompound.getCompound("tag").clone();
            processEnchantOrder(this.tag); // Paper
            processText(); // Paper
            this.getItem().b(this.tag);
            // CraftBukkit end
        }

        if (this.getItem().usesDurability()) {
            this.setDamage(this.getDamage());
        }

    }

    private ItemStack(NBTTagCompound nbttagcompound) {
        this.load(nbttagcompound);
        // CraftBukkit end
        this.checkEmpty();
    }

    public static ItemStack fromCompound(NBTTagCompound nbttagcompound) { return a(nbttagcompound); } // Paper - OBFHELPER
    public static ItemStack a(NBTTagCompound nbttagcompound) {
        try {
            return new ItemStack(nbttagcompound);
        } catch (RuntimeException runtimeexception) {
            ItemStack.LOGGER.debug("Tried to load invalid item: {}", nbttagcompound, runtimeexception);
            return ItemStack.b;
        }
    }

    public boolean isEmpty() {
        return this == ItemStack.NULL_ITEM || this.item == null || this.item == Items.AIR || this.count <= 0; // Paper
    }

    public ItemStack cloneAndSubtract(int i) {
        int j = Math.min(i, this.count);
        ItemStack itemstack = this.cloneItemStack();

        itemstack.setCount(j);
        this.subtract(j);
        return itemstack;
    }

    public Item getItem() {
        return this.j ? Items.AIR : this.item;
    }

    public EnumInteractionResult placeItem(ItemActionContext itemactioncontext, EnumHand enumhand) { // CraftBukkit - add hand
        EntityHuman entityhuman = itemactioncontext.getEntity();
        BlockPosition blockposition = itemactioncontext.getClickPosition();
        ShapeDetectorBlock shapedetectorblock = new ShapeDetectorBlock(itemactioncontext.getWorld(), blockposition, false);

        if (entityhuman != null && !entityhuman.abilities.mayBuild && !this.b(itemactioncontext.getWorld().p(), shapedetectorblock)) {
            return EnumInteractionResult.PASS;
        } else {
            // CraftBukkit start - handle all block place event logic here
            NBTTagCompound oldData = this.getTagClone();
            int oldCount = this.getCount();
            WorldServer world = (WorldServer) itemactioncontext.getWorld();

            if (!(this.getItem() instanceof ItemBucket)) { // if not bucket
                world.captureBlockStates = true;
                // special case bonemeal
                if (this.getItem() == Items.BONE_MEAL) {
                    world.captureTreeGeneration = true;
                }
            }
            Item item = this.getItem();
            EnumInteractionResult enuminteractionresult = item.a(itemactioncontext);
            NBTTagCompound newData = this.getTagClone();
            int newCount = this.getCount();
            this.setCount(oldCount);
            this.setTagClone(oldData);
            world.captureBlockStates = false;
            if (enuminteractionresult.a() && world.captureTreeGeneration && world.capturedBlockStates.size() > 0) {
                world.captureTreeGeneration = false;
                Location location = new Location(world.getWorld(), blockposition.getX(), blockposition.getY(), blockposition.getZ());
                TreeType treeType = BlockSapling.treeType;
                BlockSapling.treeType = null;
                List<BlockState> blocks = new java.util.ArrayList<>(world.capturedBlockStates.values());
                world.capturedBlockStates.clear();
                StructureGrowEvent structureEvent = null;
                if (treeType != null) {
                    boolean isBonemeal = getItem() == Items.BONE_MEAL;
                    structureEvent = new StructureGrowEvent(location, treeType, isBonemeal, (Player) entityhuman.getBukkitEntity(), blocks);
                    org.bukkit.Bukkit.getPluginManager().callEvent(structureEvent);
                }

                BlockFertilizeEvent fertilizeEvent = new BlockFertilizeEvent(CraftBlock.at(world, blockposition), (Player) entityhuman.getBukkitEntity(), blocks);
                fertilizeEvent.setCancelled(structureEvent != null && structureEvent.isCancelled());
                org.bukkit.Bukkit.getPluginManager().callEvent(fertilizeEvent);

                if (!fertilizeEvent.isCancelled()) {
                    // Change the stack to its new contents if it hasn't been tampered with.
                    if (this.getCount() == oldCount && Objects.equals(this.tag, oldData)) {
                        this.setTag(newData);
                        this.setCount(newCount);
                    }
                    for (BlockState blockstate : blocks) {
                        blockstate.update(true);
                    }
                }

                return enuminteractionresult;
            }
            world.captureTreeGeneration = false;

            if (entityhuman != null && enuminteractionresult.a()) {
                org.bukkit.event.block.BlockPlaceEvent placeEvent = null;
                List<BlockState> blocks = new java.util.ArrayList<>(world.capturedBlockStates.values());
                world.capturedBlockStates.clear();
                if (blocks.size() > 1) {
                    placeEvent = org.bukkit.craftbukkit.event.CraftEventFactory.callBlockMultiPlaceEvent(world, entityhuman, enumhand, blocks, blockposition.getX(), blockposition.getY(), blockposition.getZ());
                } else if (blocks.size() == 1) {
                    placeEvent = org.bukkit.craftbukkit.event.CraftEventFactory.callBlockPlaceEvent(world, entityhuman, enumhand, blocks.get(0), blockposition.getX(), blockposition.getY(), blockposition.getZ());
                }

                if (placeEvent != null && (placeEvent.isCancelled() || !placeEvent.canBuild())) {
                    enuminteractionresult = EnumInteractionResult.FAIL; // cancel placement
                    // PAIL: Remove this when MC-99075 fixed
                    placeEvent.getPlayer().updateInventory();
                    world.capturedTileEntities.clear(); // Paper - clear out tile entities as chests and such will pop loot
                    // revert back all captured blocks
                    for (BlockState blockstate : blocks) {
                        blockstate.update(true, false);
                    }

                    // Brute force all possible updates
                    BlockPosition placedPos = ((CraftBlock) placeEvent.getBlock()).getPosition();
                    for (EnumDirection dir : EnumDirection.values()) {
                        ((EntityPlayer) entityhuman).playerConnection.sendPacket(new PacketPlayOutBlockChange(world, placedPos.shift(dir)));
                    }
                } else {
                    // Change the stack to its new contents if it hasn't been tampered with.
                    if (this.getCount() == oldCount && Objects.equals(this.tag, oldData)) {
                        this.setTag(newData);
                        this.setCount(newCount);
                    }

                    for (Map.Entry<BlockPosition, TileEntity> e : world.capturedTileEntities.entrySet()) {
                        world.setTileEntity(e.getKey(), e.getValue());
                    }

                    for (BlockState blockstate : blocks) {
                        int updateFlag = ((CraftBlockState) blockstate).getFlag();
                        IBlockData oldBlock = ((CraftBlockState) blockstate).getHandle();
                        BlockPosition newblockposition = ((CraftBlockState) blockstate).getPosition();
                        IBlockData block = world.getType(newblockposition);

                        if (!(block.getBlock() instanceof BlockTileEntity)) { // Containers get placed automatically
                            block.getBlock().onPlace(block, world, newblockposition, oldBlock, true, itemactioncontext); // Paper - pass itemactioncontext
                        }

                        world.notifyAndUpdatePhysics(newblockposition, null, oldBlock, block, world.getType(newblockposition), updateFlag, 512); // send null chunk as chunk.k() returns false by this point
                    }

                    // Special case juke boxes as they update their tile entity. Copied from ItemRecord.
                    // PAIL: checkme on updates.
                    if (this.item instanceof ItemRecord) {
                        ((BlockJukeBox) Blocks.JUKEBOX).a(world, blockposition, world.getType(blockposition), this);
                        world.a((EntityHuman) null, 1010, blockposition, Item.getId(this.item));
                        this.subtract(1);
                        entityhuman.a(StatisticList.PLAY_RECORD);
                    }

                    if (this.item == Items.WITHER_SKELETON_SKULL) { // Special case skulls to allow wither spawns to be cancelled
                        BlockPosition bp = blockposition;
                        if (!world.getType(blockposition).getMaterial().isReplaceable()) {
                            if (!world.getType(blockposition).getMaterial().isBuildable()) {
                                bp = null;
                            } else {
                                bp = bp.shift(itemactioncontext.getClickedFace());
                            }
                        }
                        if (bp != null) {
                            TileEntity te = world.getTileEntity(bp);
                            if (te instanceof TileEntitySkull) {
                                BlockWitherSkull.a(world, bp, (TileEntitySkull) te);
                            }
                        }
                    }

                    // SPIGOT-4678
                    if (this.item instanceof ItemSign && ItemSign.openSign != null) {
                        try {
                            entityhuman.openSign((TileEntitySign) world.getTileEntity(ItemSign.openSign));
                        } finally {
                            ItemSign.openSign = null;
                        }
                    }

                    // SPIGOT-1288 - play sound stripped from ItemBlock
                    if (this.item instanceof ItemBlock) {
                        SoundEffectType soundeffecttype = ((ItemBlock) this.item).getBlock().stepSound;
                        world.playSound(entityhuman, blockposition, soundeffecttype.e(), SoundCategory.BLOCKS, (soundeffecttype.a() + 1.0F) / 2.0F, soundeffecttype.b() * 0.8F);
                    }

                    entityhuman.b(StatisticList.ITEM_USED.b(item));
                }
            }
            world.capturedTileEntities.clear();
            world.capturedBlockStates.clear();
            // CraftBukkit end

            return enuminteractionresult;
        }
    }

    public float a(IBlockData iblockdata) {
        return this.getItem().getDestroySpeed(this, iblockdata);
    }

    public InteractionResultWrapper<ItemStack> a(World world, EntityHuman entityhuman, EnumHand enumhand) {
        return this.getItem().a(world, entityhuman, enumhand);
    }

    public ItemStack a(World world, EntityLiving entityliving) {
        return this.getItem().a(this, world, entityliving);
    }

    public NBTTagCompound save(NBTTagCompound nbttagcompound) {
        MinecraftKey minecraftkey = IRegistry.ITEM.getKey(this.getItem());

        nbttagcompound.setString("id", minecraftkey == null ? "minecraft:air" : minecraftkey.toString());
        nbttagcompound.setByte("Count", (byte) this.count);
        if (this.tag != null) {
            nbttagcompound.set("tag", this.tag.clone());
        }

        return nbttagcompound;
    }

    public int getMaxStackSize() {
        return this.getItem().getMaxStackSize();
    }

    public boolean isStackable() {
        return this.getMaxStackSize() > 1 && (!this.e() || !this.f());
    }

    public boolean e() {
        if (!this.j && this.getItem().getMaxDurability() > 0) {
            NBTTagCompound nbttagcompound = this.getTag();

            return nbttagcompound == null || !nbttagcompound.getBoolean("Unbreakable");
        } else {
            return false;
        }
    }

    public boolean f() {
        return this.e() && this.getDamage() > 0;
    }

    public int getDamage() {
        return this.tag == null ? 0 : this.tag.getInt("Damage");
    }

    public void setDamage(int i) {
        this.getOrCreateTag().setInt("Damage", Math.max(0, i));
    }

    public int h() {
        return this.getItem().getMaxDurability();
    }

    public boolean isDamaged(int i, Random random, @Nullable EntityPlayer entityplayer) {
        if (!this.e()) {
            return false;
        } else {
            int j;

            if (i > 0) {
                j = EnchantmentManager.getEnchantmentLevel(Enchantments.DURABILITY, this);
                int k = 0;

                for (int l = 0; j > 0 && l < i; ++l) {
                    if (EnchantmentDurability.a(this, j, random)) {
                        ++k;
                    }
                }

                i -= k;
                // CraftBukkit start
                if (entityplayer != null) {
                    PlayerItemDamageEvent event = new PlayerItemDamageEvent(entityplayer.getBukkitEntity(), CraftItemStack.asCraftMirror(this), i);
                    event.getPlayer().getServer().getPluginManager().callEvent(event);

                    if (i != event.getDamage() || event.isCancelled()) {
                        event.getPlayer().updateInventory();
                    }
                    if (event.isCancelled()) {
                        return false;
                    }

                    i = event.getDamage();
                }
                // CraftBukkit end
                if (i <= 0) {
                    return false;
                }
            }

            if (entityplayer != null && i != 0) {
                CriterionTriggers.t.a(entityplayer, this, this.getDamage() + i);
            }

            j = this.getDamage() + i;
            this.setDamage(j);
            return j >= this.h();
        }
    }

    public <T extends EntityLiving> void damage(int i, T t0, Consumer<T> consumer) {
        if (!t0.world.isClientSide && (!(t0 instanceof EntityHuman) || !((EntityHuman) t0).abilities.canInstantlyBuild)) {
            if (this.e()) {
                if (this.isDamaged(i, t0.getRandom(), t0 instanceof EntityPlayer ? (EntityPlayer) t0 : null)) {
                    consumer.accept(t0);
                    Item item = this.getItem();
                    // CraftBukkit start - Check for item breaking
                    if (this.count == 1 && t0 instanceof EntityHuman) {
                        org.bukkit.craftbukkit.event.CraftEventFactory.callPlayerItemBreakEvent((EntityHuman) t0, this);
                    }
                    // CraftBukkit end

                    this.subtract(1);
                    if (t0 instanceof EntityHuman) {
                        ((EntityHuman) t0).b(StatisticList.ITEM_BROKEN.b(item));
                    }

                    this.setDamage(0);
                }

            }
        }
    }

    public void a(EntityLiving entityliving, EntityHuman entityhuman) {
        Item item = this.getItem();

        if (item.a(this, entityliving, (EntityLiving) entityhuman)) {
            entityhuman.b(StatisticList.ITEM_USED.b(item));
        }

    }

    public void a(World world, IBlockData iblockdata, BlockPosition blockposition, EntityHuman entityhuman) {
        Item item = this.getItem();

        if (item.a(this, world, iblockdata, blockposition, entityhuman)) {
            entityhuman.b(StatisticList.ITEM_USED.b(item));
        }

    }

    public boolean canDestroySpecialBlock(IBlockData iblockdata) {
        return this.getItem().canDestroySpecialBlock(iblockdata);
    }

    public EnumInteractionResult a(EntityHuman entityhuman, EntityLiving entityliving, EnumHand enumhand) {
        return this.getItem().a(this, entityhuman, entityliving, enumhand);
    }

    public ItemStack cloneItemStack() { return cloneItemStack(false); } // Paper
    public ItemStack cloneItemStack(boolean origItem) { // Paper
        if (!origItem && this.isEmpty()) { // Paper
            return ItemStack.b;
        } else {
            ItemStack itemstack = new ItemStack(origItem ? this.item : this.getItem(), this.count); // Paper

            itemstack.d(this.D());
            if (this.tag != null) {
                itemstack.tag = this.tag.clone();
            }

            return itemstack;
        }
    }

    public static boolean equals(ItemStack itemstack, ItemStack itemstack1) {
        return itemstack.isEmpty() && itemstack1.isEmpty() ? true : (!itemstack.isEmpty() && !itemstack1.isEmpty() ? (itemstack.tag == null && itemstack1.tag != null ? false : itemstack.tag == null || itemstack.tag.equals(itemstack1.tag)) : false);
    }

    public static boolean matches(ItemStack itemstack, ItemStack itemstack1) {
        return itemstack.isEmpty() && itemstack1.isEmpty() ? true : (!itemstack.isEmpty() && !itemstack1.isEmpty() ? itemstack.c(itemstack1) : false);
    }

    private boolean c(ItemStack itemstack) {
        return this.count != itemstack.count ? false : (this.getItem() != itemstack.getItem() ? false : (this.tag == null && itemstack.tag != null ? false : this.tag == null || this.tag.equals(itemstack.tag)));
    }

    public static boolean c(ItemStack itemstack, ItemStack itemstack1) {
        return itemstack == itemstack1 ? true : (!itemstack.isEmpty() && !itemstack1.isEmpty() ? itemstack.doMaterialsMatch(itemstack1) : false);
    }

    public static boolean d(ItemStack itemstack, ItemStack itemstack1) {
        return itemstack == itemstack1 ? true : (!itemstack.isEmpty() && !itemstack1.isEmpty() ? itemstack.b(itemstack1) : false);
    }

    public boolean doMaterialsMatch(ItemStack itemstack) {
        return !itemstack.isEmpty() && this.getItem() == itemstack.getItem();
    }

    public boolean b(ItemStack itemstack) {
        return !this.e() ? this.doMaterialsMatch(itemstack) : !itemstack.isEmpty() && this.getItem() == itemstack.getItem();
    }

    public String j() {
        return this.getItem().f(this);
    }

    public String toString() {
        return this.count + " " + this.getItem();
    }

    public void a(World world, Entity entity, int i, boolean flag) {
        if (this.g > 0) {
            --this.g;
        }

        if (this.getItem() != null) {
            this.getItem().a(this, world, entity, i, flag);
        }

    }

    public void a(World world, EntityHuman entityhuman, int i) {
        entityhuman.a(StatisticList.ITEM_CRAFTED.b(this.getItem()), i);
        this.getItem().b(this, world, entityhuman);
    }

    public int getItemUseMaxDuration() { return k(); } // Paper - OBFHELPER
    public int k() {
        return this.getItem().e_(this);
    }

    public EnumAnimation l() {
        return this.getItem().d_(this);
    }

    public void a(World world, EntityLiving entityliving, int i) {
        this.getItem().a(this, world, entityliving, i);
    }

    public boolean m() {
        return this.getItem().j(this);
    }

    public boolean hasTag() {
        return !this.j && this.tag != null && !this.tag.isEmpty();
    }

    @Nullable
    public NBTTagCompound getTag() {
        return this.tag;
    }

    // CraftBukkit start
    @Nullable
    private NBTTagCompound getTagClone() {
        return this.tag == null ? null : this.tag.clone();
    }

    private void setTagClone(@Nullable NBTTagCompound nbtttagcompound) {
        this.setTag(nbtttagcompound == null ? null : nbtttagcompound.clone());
    }
    // CraftBukkit end

    public NBTTagCompound getOrCreateTag() {
        if (this.tag == null) {
            this.setTag(new NBTTagCompound());
        }

        return this.tag;
    }

    public NBTTagCompound a(String s) {
        if (this.tag != null && this.tag.hasKeyOfType(s, 10)) {
            return this.tag.getCompound(s);
        } else {
            NBTTagCompound nbttagcompound = new NBTTagCompound();

            this.a(s, (NBTBase) nbttagcompound);
            return nbttagcompound;
        }
    }

    @Nullable public NBTTagCompound getSubTag(String s) { return b(s); } // Paper - OBFHELPER
    @Nullable
    public NBTTagCompound b(String s) {
        return this.tag != null && this.tag.hasKeyOfType(s, 10) ? this.tag.getCompound(s) : null;
    }

    public void removeTag(String s) {
        if (this.tag != null && this.tag.hasKey(s)) {
            this.tag.remove(s);
            if (this.tag.isEmpty()) {
                this.tag = null;
            }
        }

    }

    public NBTTagList getEnchantments() {
        return this.tag != null ? this.tag.getList("Enchantments", 10) : new NBTTagList();
    }

    // Paper start - (this is just a good no conflict location)
    public org.bukkit.inventory.ItemStack asBukkitMirror() {
        return CraftItemStack.asCraftMirror(this);
    }
    public org.bukkit.inventory.ItemStack asBukkitCopy() {
        return CraftItemStack.asCraftMirror(this.cloneItemStack());
    }
    public static ItemStack fromBukkitCopy(org.bukkit.inventory.ItemStack itemstack) {
        return CraftItemStack.asNMSCopy(itemstack);
    }
    private org.bukkit.craftbukkit.inventory.CraftItemStack bukkitStack;
    public org.bukkit.inventory.ItemStack getBukkitStack() {
        if (bukkitStack == null || bukkitStack.getHandle() != this) {
            bukkitStack = org.bukkit.craftbukkit.inventory.CraftItemStack.asCraftMirror(this);
        }
        return bukkitStack;
    }
    // Paper end
    public void setTag(@Nullable NBTTagCompound nbttagcompound) {
        this.tag = nbttagcompound;
        processEnchantOrder(this.tag); // Paper
        if (this.getItem().usesDurability()) {
            this.setDamage(this.getDamage());
        }

    }

    public IChatBaseComponent getName() {
        NBTTagCompound nbttagcompound = this.b("display");

        if (nbttagcompound != null && nbttagcompound.hasKeyOfType("Name", 8)) {
            try {
                IChatMutableComponent ichatmutablecomponent = IChatBaseComponent.ChatSerializer.a(nbttagcompound.getString("Name"));

                if (ichatmutablecomponent != null) {
                    return ichatmutablecomponent;
                }

                nbttagcompound.remove("Name");
            } catch (JsonParseException jsonparseexception) {
                nbttagcompound.remove("Name");
            }
        }

        return this.getItem().h(this);
    }

    public ItemStack a(@Nullable IChatBaseComponent ichatbasecomponent) {
        NBTTagCompound nbttagcompound = this.a("display");

        if (ichatbasecomponent != null) {
            nbttagcompound.setString("Name", IChatBaseComponent.ChatSerializer.a(ichatbasecomponent));
        } else {
            nbttagcompound.remove("Name");
        }

        return this;
    }

    public void s() {
        NBTTagCompound nbttagcompound = this.b("display");

        if (nbttagcompound != null) {
            nbttagcompound.remove("Name");
            if (nbttagcompound.isEmpty()) {
                this.removeTag("display");
            }
        }

        if (this.tag != null && this.tag.isEmpty()) {
            this.tag = null;
        }

    }

    public boolean hasName() {
        NBTTagCompound nbttagcompound = this.b("display");

        return nbttagcompound != null && nbttagcompound.hasKeyOfType("Name", 8);
    }

    public void a(ItemStack.HideFlags itemstack_hideflags) {
        NBTTagCompound nbttagcompound = this.getOrCreateTag();

        nbttagcompound.setInt("HideFlags", nbttagcompound.getInt("HideFlags") | itemstack_hideflags.a());
    }

    public boolean u() {
        return this.getItem().e(this);
    }

    public EnumItemRarity v() {
        return this.getItem().i(this);
    }

    public boolean canEnchant() {
        return !this.getItem().f_(this) ? false : !this.hasEnchantments();
    }

    public void addEnchantment(Enchantment enchantment, int i) {
        this.getOrCreateTag();
        if (!this.tag.hasKeyOfType("Enchantments", 9)) {
            this.tag.set("Enchantments", new NBTTagList());
        }

        NBTTagList nbttaglist = this.tag.getList("Enchantments", 10);
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        nbttagcompound.setString("id", String.valueOf(IRegistry.ENCHANTMENT.getKey(enchantment)));
        nbttagcompound.setShort("lvl", (short) ((byte) i));
        nbttaglist.add(nbttagcompound);
        processEnchantOrder(nbttagcompound); // Paper
    }

    public boolean hasEnchantments() {
        return this.tag != null && this.tag.hasKeyOfType("Enchantments", 9) ? !this.tag.getList("Enchantments", 10).isEmpty() : false;
    }

    public void getOrCreateTagAndSet(String s, NBTBase nbtbase) { a(s, nbtbase);} // Paper - OBFHELPER
    public void a(String s, NBTBase nbtbase) {
        this.getOrCreateTag().set(s, nbtbase);
    }

    public boolean y() {
        return this.k instanceof EntityItemFrame;
    }

    public void a(@Nullable Entity entity) {
        this.k = entity;
    }

    @Nullable
    public EntityItemFrame z() {
        return this.k instanceof EntityItemFrame ? (EntityItemFrame) this.A() : null;
    }

    @Nullable
    public Entity A() {
        return !this.j ? this.k : null;
    }

    public int getRepairCost() {
        return this.hasTag() && this.tag.hasKeyOfType("RepairCost", 3) ? this.tag.getInt("RepairCost") : 0;
    }

    public void setRepairCost(int i) {
        // CraftBukkit start - remove RepairCost tag when 0 (SPIGOT-3945)
        if (i == 0) {
            this.removeTag("RepairCost");
            return;
        }
        // CraftBukkit end
        this.getOrCreateTag().setInt("RepairCost", i);
    }

    public Multimap<AttributeBase, AttributeModifier> a(EnumItemSlot enumitemslot) {
        Object object;

        if (this.hasTag() && this.tag.hasKeyOfType("AttributeModifiers", 9)) {
            object = HashMultimap.create();
            NBTTagList nbttaglist = this.tag.getList("AttributeModifiers", 10);

            for (int i = 0; i < nbttaglist.size(); ++i) {
                NBTTagCompound nbttagcompound = nbttaglist.getCompound(i);

                if (!nbttagcompound.hasKeyOfType("Slot", 8) || nbttagcompound.getString("Slot").equals(enumitemslot.getSlotName())) {
                    Optional<AttributeBase> optional = IRegistry.ATTRIBUTE.getOptional(MinecraftKey.a(nbttagcompound.getString("AttributeName")));

                    if (optional.isPresent()) {
                        AttributeModifier attributemodifier = AttributeModifier.a(nbttagcompound);

                        if (attributemodifier != null && attributemodifier.getUniqueId().getLeastSignificantBits() != 0L && attributemodifier.getUniqueId().getMostSignificantBits() != 0L) {
                            ((Multimap) object).put(optional.get(), attributemodifier);
                        }
                    }
                }
            }
        } else {
            object = this.getItem().a(enumitemslot);
        }

        return (Multimap) object;
    }

    public void a(AttributeBase attributebase, AttributeModifier attributemodifier, @Nullable EnumItemSlot enumitemslot) {
        this.getOrCreateTag();
        if (!this.tag.hasKeyOfType("AttributeModifiers", 9)) {
            this.tag.set("AttributeModifiers", new NBTTagList());
        }

        NBTTagList nbttaglist = this.tag.getList("AttributeModifiers", 10);
        NBTTagCompound nbttagcompound = attributemodifier.save();

        nbttagcompound.setString("AttributeName", IRegistry.ATTRIBUTE.getKey(attributebase).toString());
        if (enumitemslot != null) {
            nbttagcompound.setString("Slot", enumitemslot.getSlotName());
        }

        nbttaglist.add(nbttagcompound);
    }

    // CraftBukkit start
    @Deprecated
    public void setItem(Item item) {
        this.bukkitStack = null; // Paper
        this.item = item;
    }
    // CraftBukkit end

    public IChatBaseComponent C() {
        IChatMutableComponent ichatmutablecomponent = (new ChatComponentText("")).addSibling(this.getName());

        if (this.hasName()) {
            ichatmutablecomponent.a(EnumChatFormat.ITALIC);
        }

        IChatMutableComponent ichatmutablecomponent1 = ChatComponentUtils.a((IChatBaseComponent) ichatmutablecomponent);

        if (!this.j) {
            ichatmutablecomponent1.a(this.v().e).format((chatmodifier) -> {
                return chatmodifier.setChatHoverable(new ChatHoverable(ChatHoverable.EnumHoverAction.SHOW_ITEM, new ChatHoverable.c(this)));
            });
        }

        return ichatmutablecomponent1;
    }

    private static boolean a(ShapeDetectorBlock shapedetectorblock, @Nullable ShapeDetectorBlock shapedetectorblock1) {
        return shapedetectorblock1 != null && shapedetectorblock.a() == shapedetectorblock1.a() ? (shapedetectorblock.b() == null && shapedetectorblock1.b() == null ? true : (shapedetectorblock.b() != null && shapedetectorblock1.b() != null ? Objects.equals(shapedetectorblock.b().save(new NBTTagCompound()), shapedetectorblock1.b().save(new NBTTagCompound())) : false)) : false;
    }

    public boolean a(ITagRegistry itagregistry, ShapeDetectorBlock shapedetectorblock) {
        if (a(shapedetectorblock, this.l)) {
            return this.m;
        } else {
            this.l = shapedetectorblock;
            if (this.hasTag() && this.tag.hasKeyOfType("CanDestroy", 9)) {
                NBTTagList nbttaglist = this.tag.getList("CanDestroy", 8);

                for (int i = 0; i < nbttaglist.size(); ++i) {
                    String s = nbttaglist.getString(i);

                    try {
                        Predicate<ShapeDetectorBlock> predicate = ArgumentBlockPredicate.a().parse(new StringReader(s)).create(itagregistry);

                        if (predicate.test(shapedetectorblock)) {
                            this.m = true;
                            return true;
                        }
                    } catch (CommandSyntaxException commandsyntaxexception) {
                        ;
                    }
                }
            }

            this.m = false;
            return false;
        }
    }

    public boolean b(ITagRegistry itagregistry, ShapeDetectorBlock shapedetectorblock) {
        if (a(shapedetectorblock, this.n)) {
            return this.o;
        } else {
            this.n = shapedetectorblock;
            if (this.hasTag() && this.tag.hasKeyOfType("CanPlaceOn", 9)) {
                NBTTagList nbttaglist = this.tag.getList("CanPlaceOn", 8);

                for (int i = 0; i < nbttaglist.size(); ++i) {
                    String s = nbttaglist.getString(i);

                    try {
                        Predicate<ShapeDetectorBlock> predicate = ArgumentBlockPredicate.a().parse(new StringReader(s)).create(itagregistry);

                        if (predicate.test(shapedetectorblock)) {
                            this.o = true;
                            return true;
                        }
                    } catch (CommandSyntaxException commandsyntaxexception) {
                        ;
                    }
                }
            }

            this.o = false;
            return false;
        }
    }

    public int D() {
        return this.g;
    }

    public void d(int i) {
        this.g = i;
    }

    public int getCount() {
        return this.j ? 0 : this.count;
    }

    public void setCount(int i) {
        this.count = i;
        this.checkEmpty();
    }

    public void add(int i) {
        this.setCount(this.count + i);
    }

    public void subtract(int i) {
        this.add(-i);
    }

    public void b(World world, EntityLiving entityliving, int i) {
        this.getItem().a(world, entityliving, this, i);
    }

    public boolean F() {
        return this.getItem().isFood();
    }

    public SoundEffect G() {
        return this.getItem().ae_();
    }

    public SoundEffect H() {
        return this.getItem().ad_();
    }

    public static enum HideFlags {

        ENCHANTMENTS, MODIFIERS, UNBREAKABLE, CAN_DESTROY, CAN_PLACE, ADDITIONAL, DYE;

        private int h = 1 << this.ordinal();

        private HideFlags() {}

        public int a() {
            return this.h;
        }
    }
}
