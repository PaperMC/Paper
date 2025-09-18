package org.bukkit.craftbukkit.inventory;

import com.destroystokyo.paper.inventory.meta.ArmorStandMeta;
import java.util.function.BiFunction;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.BedItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.CopperGolemStatueBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.ShelfBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.AxolotlBucketMeta;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.BundleMeta;
import org.bukkit.inventory.meta.ColorableArmorMeta;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.KnowledgeBookMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.MusicInstrumentMeta;
import org.bukkit.inventory.meta.OminousBottleMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.ShieldMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.inventory.meta.SuspiciousStewMeta;
import org.bukkit.inventory.meta.TropicalFishBucketMeta;

public final class CraftItemMetas {

    public record ItemMetaData<I extends ItemMeta>(Class<I> metaClass, BiFunction<ItemStack, java.util.Set<net.minecraft.core.component.DataComponentType<?>>, I> fromItemStack,
                                                   BiFunction<ItemType.Typed<I>, CraftMetaItem, I> fromItemMeta) {
    }

    private static final ItemMetaData<ItemMeta> EMPTY_META_DATA = new ItemMetaData<>(ItemMeta.class,
            (item, extras) -> null,
            (type, meta) -> null);

    private static final ItemMetaData<ItemMeta> ITEM_META_DATA = new ItemMetaData<>(ItemMeta.class,
            (item, extras) -> new CraftMetaItem(item.getComponentsPatch(), extras),
            (type, meta) -> new CraftMetaItem(meta));

    private static final ItemMetaData<BookMeta> SIGNED_BOOK_META_DATA = new ItemMetaData<>(BookMeta.class,
            (item, extras) -> new CraftMetaBookSigned(item.getComponentsPatch(), extras),
            (type, meta) -> meta instanceof CraftMetaBookSigned signed ? signed : new CraftMetaBookSigned(meta));

    private static final ItemMetaData<BookMeta> WRITABLE_BOOK_META_DATA = new ItemMetaData<>(BookMeta.class,
            (item, extras) -> new CraftMetaBook(item.getComponentsPatch(), extras),
            (type, meta) -> meta != null && meta.getClass().equals(CraftMetaBook.class) ? (BookMeta) meta : new CraftMetaBook(meta));

    private static final ItemMetaData<SkullMeta> SKULL_META_DATA = new ItemMetaData<>(SkullMeta.class,
            (item, extras) -> new CraftMetaSkull(item.getComponentsPatch(), extras),
            (type, meta) -> meta instanceof CraftMetaSkull skull ? skull : new CraftMetaSkull(meta));

    private static final ItemMetaData<ArmorMeta> ARMOR_META_DATA = new ItemMetaData<>(ArmorMeta.class,
            (item, extras) -> new CraftMetaArmor(item.getComponentsPatch(), extras),
            (type, meta) -> meta != null && meta.getClass().equals(CraftMetaArmor.class) ? (ArmorMeta) meta : new CraftMetaArmor(meta));

    private static final ItemMetaData<ColorableArmorMeta> COLORABLE_ARMOR_META_DATA = new ItemMetaData<>(ColorableArmorMeta.class,
            (item, extras) -> new CraftMetaColorableArmor(item.getComponentsPatch(), extras),
            (type, meta) -> meta instanceof ColorableArmorMeta colorable ? colorable : new CraftMetaColorableArmor(meta));

    private static final ItemMetaData<LeatherArmorMeta> LEATHER_ARMOR_META_DATA = new ItemMetaData<>(LeatherArmorMeta.class,
            (item, extras) -> new CraftMetaLeatherArmor(item.getComponentsPatch(), extras),
            (type, meta) -> meta instanceof CraftMetaLeatherArmor leather ? leather : new CraftMetaLeatherArmor(meta));

    private static final ItemMetaData<PotionMeta> POTION_META_DATA = new ItemMetaData<>(PotionMeta.class,
            (item, extras) -> new CraftMetaPotion(item.getComponentsPatch(), extras),
            (type, meta) -> meta instanceof CraftMetaPotion potion ? potion : new CraftMetaPotion(meta));

    private static final ItemMetaData<MapMeta> MAP_META_DATA = new ItemMetaData<>(MapMeta.class,
            (item, extras) -> new CraftMetaMap(item.getComponentsPatch(), extras),
            (type, meta) -> meta instanceof CraftMetaMap map ? map : new CraftMetaMap(meta));

    private static final ItemMetaData<FireworkMeta> FIREWORK_META_DATA = new ItemMetaData<>(FireworkMeta.class,
            (item, extras) -> new CraftMetaFirework(item.getComponentsPatch(), extras),
            (type, meta) -> meta instanceof CraftMetaFirework firework ? firework : new CraftMetaFirework(meta));

    private static final ItemMetaData<FireworkEffectMeta> CHARGE_META_DATA = new ItemMetaData<>(FireworkEffectMeta.class,
            (item, extras) -> new CraftMetaCharge(item.getComponentsPatch(), extras),
            (type, meta) -> meta instanceof CraftMetaCharge charge ? charge : new CraftMetaCharge(meta));

    private static final ItemMetaData<EnchantmentStorageMeta> ENCHANTED_BOOK_META_DATA = new ItemMetaData<>(EnchantmentStorageMeta.class,
            (item, extras) -> new CraftMetaEnchantedBook(item.getComponentsPatch(), extras),
            (type, meta) -> meta instanceof CraftMetaEnchantedBook enchantedBook ? enchantedBook : new CraftMetaEnchantedBook(meta));

    private static final ItemMetaData<BannerMeta> BANNER_META_DATA = new ItemMetaData<>(BannerMeta.class,
            (item, extras) -> new CraftMetaBanner(item.getComponentsPatch(), extras),
            (type, meta) -> meta instanceof CraftMetaBanner banner ? banner : new CraftMetaBanner(meta));

    private static final ItemMetaData<SpawnEggMeta> SPAWN_EGG_META_DATA = new ItemMetaData<>(SpawnEggMeta.class,
            (item, extras) -> new CraftMetaSpawnEgg(item.getComponentsPatch(), extras),
            (type, meta) -> meta instanceof CraftMetaSpawnEgg spawnEgg ? spawnEgg : new CraftMetaSpawnEgg(meta));

    private static final ItemMetaData<ArmorStandMeta> ARMOR_STAND_META_DATA = new ItemMetaData<>(ArmorStandMeta.class, // paper
            (item, extras) -> new CraftMetaArmorStand(item.getComponentsPatch(), extras),
            (type, meta) -> meta instanceof CraftMetaArmorStand armorStand ? armorStand : new CraftMetaArmorStand(meta));

    private static final ItemMetaData<KnowledgeBookMeta> KNOWLEDGE_BOOK_META_DATA = new ItemMetaData<>(KnowledgeBookMeta.class,
            (item, extras) -> new CraftMetaKnowledgeBook(item.getComponentsPatch(), extras),
            (type, meta) -> meta instanceof CraftMetaKnowledgeBook knowledgeBook ? knowledgeBook : new CraftMetaKnowledgeBook(meta));

    private static final ItemMetaData<BlockStateMeta> BLOCK_STATE_META_DATA = new ItemMetaData<>(BlockStateMeta.class,
            (item, extras) -> new CraftMetaBlockState(item.getComponentsPatch(), CraftItemType.minecraftToBukkit(item.getItem()), extras),
            (type, meta) -> new CraftMetaBlockState(meta, type.asMaterial()));

    private static final ItemMetaData<ShieldMeta> SHIELD_META_DATA = new ItemMetaData<>(ShieldMeta.class,
            (item, extras) -> new CraftMetaShield(item.getComponentsPatch(), extras),
            (type, meta) -> new CraftMetaShield(meta));

    private static final ItemMetaData<TropicalFishBucketMeta> TROPICAL_FISH_BUCKET_META_DATA = new ItemMetaData<>(TropicalFishBucketMeta.class,
            (item, extras) -> new CraftMetaTropicalFishBucket(item.getComponentsPatch(), extras),
            (type, meta) -> meta instanceof CraftMetaTropicalFishBucket tropicalFishBucket ? tropicalFishBucket : new CraftMetaTropicalFishBucket(meta));

    private static final ItemMetaData<AxolotlBucketMeta> AXOLOTL_BUCKET_META_DATA = new ItemMetaData<>(AxolotlBucketMeta.class,
            (item, extras) -> new CraftMetaAxolotlBucket(item.getComponentsPatch(), extras),
            (type, meta) -> meta instanceof CraftMetaAxolotlBucket axolotlBucket ? axolotlBucket : new CraftMetaAxolotlBucket(meta));

    private static final ItemMetaData<CrossbowMeta> CROSSBOW_META_DATA = new ItemMetaData<>(CrossbowMeta.class,
            (item, extras) -> new CraftMetaCrossbow(item.getComponentsPatch(), extras),
            (type, meta) -> meta instanceof CraftMetaCrossbow crossbow ? crossbow : new CraftMetaCrossbow(meta));

    private static final ItemMetaData<SuspiciousStewMeta> SUSPICIOUS_STEW_META_DATA = new ItemMetaData<>(SuspiciousStewMeta.class,
            (item, extras) -> new CraftMetaSuspiciousStew(item.getComponentsPatch(), extras),
            (type, meta) -> meta instanceof CraftMetaSuspiciousStew suspiciousStew ? suspiciousStew : new CraftMetaSuspiciousStew(meta));

    private static final ItemMetaData<ItemMeta> ENTITY_TAG_META_DATA = new ItemMetaData<>(ItemMeta.class,
            (item, extras) -> new CraftMetaEntityTag(item.getComponentsPatch(), extras),
            (type, meta) -> meta instanceof CraftMetaEntityTag entityTag ? entityTag : new CraftMetaEntityTag(meta));

    private static final ItemMetaData<CompassMeta> COMPASS_META_DATA = new ItemMetaData<>(CompassMeta.class,
            (item, extras) -> new CraftMetaCompass(item.getComponentsPatch(), extras),
            (type, meta) -> meta instanceof CraftMetaCompass compass ? compass : new CraftMetaCompass(meta));

    private static final ItemMetaData<BundleMeta> BUNDLE_META_DATA = new ItemMetaData<>(BundleMeta.class,
            (item, extras) -> new CraftMetaBundle(item.getComponentsPatch(), extras),
            (type, meta) -> meta instanceof CraftMetaBundle bundle ? bundle : new CraftMetaBundle(meta));

    private static final ItemMetaData<MusicInstrumentMeta> MUSIC_INSTRUMENT_META_DATA = new ItemMetaData<>(MusicInstrumentMeta.class,
            (item, extras) -> new CraftMetaMusicInstrument(item.getComponentsPatch(), extras),
            (type, meta) -> meta instanceof CraftMetaMusicInstrument musicInstrument ? musicInstrument : new CraftMetaMusicInstrument(meta));

    private static final ItemMetaData<OminousBottleMeta> OMINOUS_BOTTLE_META_DATA = new ItemMetaData<>(OminousBottleMeta.class,
            (item, extras) -> new CraftMetaOminousBottle(item.getComponentsPatch(), extras),
            (type, meta) -> meta instanceof CraftMetaOminousBottle musicInstrument ? musicInstrument : new CraftMetaOminousBottle(meta));

    // We use if instead of a set, since the result gets cached in CraftItemType,
    // which would result in dead memory once all ItemTypes have cached the data.
    public static <I extends ItemMeta> ItemMetaData<I> getItemMetaData(CraftItemType<?> itemType) {
        Item itemHandle = itemType.getHandle();
        Block blockHandle = (itemHandle instanceof BlockItem itemBlock) ? itemBlock.getBlock() : null;

        if (itemType == ItemType.AIR) {
            return CraftItemMetas.asType(CraftItemMetas.EMPTY_META_DATA);
        }
        if (itemType == ItemType.WRITTEN_BOOK) {
            return CraftItemMetas.asType(CraftItemMetas.SIGNED_BOOK_META_DATA);
        }
        if (itemType == ItemType.WRITABLE_BOOK) {
            return CraftItemMetas.asType(CraftItemMetas.WRITABLE_BOOK_META_DATA);
        }
        if (itemType == ItemType.CREEPER_HEAD || itemType == ItemType.DRAGON_HEAD
                || itemType == ItemType.PIGLIN_HEAD || itemType == ItemType.PLAYER_HEAD
                || itemType == ItemType.SKELETON_SKULL || itemType == ItemType.WITHER_SKELETON_SKULL
                || itemType == ItemType.ZOMBIE_HEAD) {
            return CraftItemMetas.asType(CraftItemMetas.SKULL_META_DATA);
        }
        if (itemType == ItemType.CHAINMAIL_HELMET || itemType == ItemType.CHAINMAIL_CHESTPLATE
                || itemType == ItemType.CHAINMAIL_LEGGINGS || itemType == ItemType.CHAINMAIL_BOOTS
                || itemType == ItemType.DIAMOND_HELMET || itemType == ItemType.DIAMOND_CHESTPLATE
                || itemType == ItemType.DIAMOND_LEGGINGS || itemType == ItemType.DIAMOND_BOOTS
                || itemType == ItemType.GOLDEN_HELMET || itemType == ItemType.GOLDEN_CHESTPLATE
                || itemType == ItemType.GOLDEN_LEGGINGS || itemType == ItemType.GOLDEN_BOOTS
                || itemType == ItemType.IRON_HELMET || itemType == ItemType.IRON_CHESTPLATE
                || itemType == ItemType.IRON_LEGGINGS || itemType == ItemType.IRON_BOOTS
                || itemType == ItemType.NETHERITE_HELMET || itemType == ItemType.NETHERITE_CHESTPLATE
                || itemType == ItemType.NETHERITE_LEGGINGS || itemType == ItemType.NETHERITE_BOOTS
                || itemType == ItemType.COPPER_HELMET || itemType == ItemType.COPPER_CHESTPLATE
                || itemType == ItemType.COPPER_LEGGINGS || itemType == ItemType.COPPER_BOOTS
                || itemType == ItemType.TURTLE_HELMET) {
            return CraftItemMetas.asType(CraftItemMetas.ARMOR_META_DATA);
        }
        if (itemType == ItemType.LEATHER_HELMET || itemType == ItemType.LEATHER_CHESTPLATE
                || itemType == ItemType.LEATHER_LEGGINGS || itemType == ItemType.LEATHER_BOOTS
                || itemType == ItemType.WOLF_ARMOR) {
            return CraftItemMetas.asType(CraftItemMetas.COLORABLE_ARMOR_META_DATA);
        }
        if (itemType == ItemType.LEATHER_HORSE_ARMOR) {
            return CraftItemMetas.asType(CraftItemMetas.LEATHER_ARMOR_META_DATA);
        }
        if (itemType == ItemType.POTION || itemType == ItemType.SPLASH_POTION
                || itemType == ItemType.LINGERING_POTION || itemType == ItemType.TIPPED_ARROW) {
            return CraftItemMetas.asType(CraftItemMetas.POTION_META_DATA);
        }
        if (itemType == ItemType.FILLED_MAP) {
            return CraftItemMetas.asType(CraftItemMetas.MAP_META_DATA);
        }
        if (itemType == ItemType.FIREWORK_ROCKET) {
            return CraftItemMetas.asType(CraftItemMetas.FIREWORK_META_DATA);
        }
        if (itemType == ItemType.FIREWORK_STAR) {
            return CraftItemMetas.asType(CraftItemMetas.CHARGE_META_DATA);
        }
        if (itemType == ItemType.ENCHANTED_BOOK) {
            return CraftItemMetas.asType(CraftItemMetas.ENCHANTED_BOOK_META_DATA);
        }
        if (itemHandle instanceof BannerItem) {
            return CraftItemMetas.asType(CraftItemMetas.BANNER_META_DATA);
        }
        if (itemHandle instanceof SpawnEggItem) {
            return CraftItemMetas.asType(CraftItemMetas.SPAWN_EGG_META_DATA);
        }
        if (itemType == ItemType.ARMOR_STAND) {
            return CraftItemMetas.asType(CraftItemMetas.ARMOR_STAND_META_DATA);
        }
        if (itemType == ItemType.KNOWLEDGE_BOOK) {
            return CraftItemMetas.asType(CraftItemMetas.KNOWLEDGE_BOOK_META_DATA);
        }
        if (blockHandle instanceof EntityBlock) {
            return CraftItemMetas.asType(CraftItemMetas.BLOCK_STATE_META_DATA);
        }
        if (itemType == ItemType.SHIELD) {
            return CraftItemMetas.asType(CraftItemMetas.SHIELD_META_DATA);
        }
        if (itemType == ItemType.TROPICAL_FISH_BUCKET) {
            return CraftItemMetas.asType(CraftItemMetas.TROPICAL_FISH_BUCKET_META_DATA);
        }
        if (itemType == ItemType.AXOLOTL_BUCKET) {
            return CraftItemMetas.asType(CraftItemMetas.AXOLOTL_BUCKET_META_DATA);
        }
        if (itemType == ItemType.CROSSBOW) {
            return CraftItemMetas.asType(CraftItemMetas.CROSSBOW_META_DATA);
        }
        if (itemType == ItemType.SUSPICIOUS_STEW) {
            return CraftItemMetas.asType(CraftItemMetas.SUSPICIOUS_STEW_META_DATA);
        }
        if (itemType == ItemType.COD_BUCKET || itemType == ItemType.PUFFERFISH_BUCKET || itemType == ItemType.TADPOLE_BUCKET
                || itemType == ItemType.SALMON_BUCKET || itemType == ItemType.ITEM_FRAME
                || itemType == ItemType.GLOW_ITEM_FRAME || itemType == ItemType.PAINTING) {
            return CraftItemMetas.asType(CraftItemMetas.ENTITY_TAG_META_DATA);
        }
        if (itemType == ItemType.COMPASS) {
            return CraftItemMetas.asType(CraftItemMetas.COMPASS_META_DATA);
        }
        if (itemHandle instanceof BundleItem) {
            return CraftItemMetas.asType(CraftItemMetas.BUNDLE_META_DATA);
        }
        if (itemType == ItemType.GOAT_HORN) {
            return CraftItemMetas.asType(CraftItemMetas.MUSIC_INSTRUMENT_META_DATA);
        }

        if (itemType == ItemType.OMINOUS_BOTTLE) {
            return CraftItemMetas.asType(CraftItemMetas.OMINOUS_BOTTLE_META_DATA);
        }

        return CraftItemMetas.asType(CraftItemMetas.ITEM_META_DATA);
    }

    private static <I extends ItemMeta> ItemMetaData<I> asType(ItemMetaData<?> metaData) {
        return (ItemMetaData<I>) metaData;
    }

    private CraftItemMetas() {
    }
}
