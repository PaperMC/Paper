package org.bukkit.craftbukkit.inventory;

import java.util.function.BiFunction;
import java.util.function.Function;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Tag;
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

    public record ItemMetaData<I extends ItemMeta>(Class<I> metaClass, Function<ItemStack, I> fromItemStack,
                                                   BiFunction<ItemType.Typed<I>, CraftMetaItem, I> fromItemMeta) {
    }

    private static final ItemMetaData<ItemMeta> EMPTY_META_DATA = new ItemMetaData<>(ItemMeta.class,
            item -> null,
            (type, meta) -> null);

    private static final ItemMetaData<ItemMeta> ITEM_META_DATA = new ItemMetaData<>(ItemMeta.class,
            item -> new CraftMetaItem(item.getComponentsPatch()),
            (type, meta) -> new CraftMetaItem(meta));

    private static final ItemMetaData<BookMeta> SIGNED_BOOK_META_DATA = new ItemMetaData<>(BookMeta.class,
            item -> new CraftMetaBookSigned(item.getComponentsPatch()),
            (type, meta) -> meta instanceof CraftMetaBookSigned signed ? signed : new CraftMetaBookSigned(meta));

    private static final ItemMetaData<BookMeta> WRITABLE_BOOK_META_DATA = new ItemMetaData<>(BookMeta.class,
            item -> new CraftMetaBook(item.getComponentsPatch()),
            (type, meta) -> meta != null && meta.getClass().equals(CraftMetaBook.class) ? (BookMeta) meta : new CraftMetaBook(meta));

    private static final ItemMetaData<SkullMeta> SKULL_META_DATA = new ItemMetaData<>(SkullMeta.class,
            item -> new CraftMetaSkull(item.getComponentsPatch()),
            (type, meta) -> meta instanceof CraftMetaSkull skull ? skull : new CraftMetaSkull(meta));

    private static final ItemMetaData<ArmorMeta> ARMOR_META_DATA = new ItemMetaData<>(ArmorMeta.class,
            item -> new CraftMetaArmor(item.getComponentsPatch()),
            (type, meta) -> meta != null && meta.getClass().equals(CraftMetaArmor.class) ? (ArmorMeta) meta : new CraftMetaArmor(meta));

    private static final ItemMetaData<ColorableArmorMeta> COLORABLE_ARMOR_META_DATA = new ItemMetaData<>(ColorableArmorMeta.class,
            item -> new CraftMetaColorableArmor(item.getComponentsPatch()),
            (type, meta) -> meta instanceof ColorableArmorMeta colorable ? colorable : new CraftMetaColorableArmor(meta));

    private static final ItemMetaData<LeatherArmorMeta> LEATHER_ARMOR_META_DATA = new ItemMetaData<>(LeatherArmorMeta.class,
            item -> new CraftMetaLeatherArmor(item.getComponentsPatch()),
            (type, meta) -> meta instanceof CraftMetaLeatherArmor leather ? leather : new CraftMetaLeatherArmor(meta));

    private static final ItemMetaData<PotionMeta> POTION_META_DATA = new ItemMetaData<>(PotionMeta.class,
            item -> new CraftMetaPotion(item.getComponentsPatch()),
            (type, meta) -> meta instanceof CraftMetaPotion potion ? potion : new CraftMetaPotion(meta));

    private static final ItemMetaData<MapMeta> MAP_META_DATA = new ItemMetaData<>(MapMeta.class,
            item -> new CraftMetaMap(item.getComponentsPatch()),
            (type, meta) -> meta instanceof CraftMetaMap map ? map : new CraftMetaMap(meta));

    private static final ItemMetaData<FireworkMeta> FIREWORK_META_DATA = new ItemMetaData<>(FireworkMeta.class,
            item -> new CraftMetaFirework(item.getComponentsPatch()),
            (type, meta) -> meta instanceof CraftMetaFirework firework ? firework : new CraftMetaFirework(meta));

    private static final ItemMetaData<FireworkEffectMeta> CHARGE_META_DATA = new ItemMetaData<>(FireworkEffectMeta.class,
            item -> new CraftMetaCharge(item.getComponentsPatch()),
            (type, meta) -> meta instanceof CraftMetaCharge charge ? charge : new CraftMetaCharge(meta));

    private static final ItemMetaData<EnchantmentStorageMeta> ENCHANTED_BOOK_META_DATA = new ItemMetaData<>(EnchantmentStorageMeta.class,
            item -> new CraftMetaEnchantedBook(item.getComponentsPatch()),
            (type, meta) -> meta instanceof CraftMetaEnchantedBook enchantedBook ? enchantedBook : new CraftMetaEnchantedBook(meta));

    private static final ItemMetaData<BannerMeta> BANNER_META_DATA = new ItemMetaData<>(BannerMeta.class,
            item -> new CraftMetaBanner(item.getComponentsPatch()),
            (type, meta) -> meta instanceof CraftMetaBanner banner ? banner : new CraftMetaBanner(meta));

    private static final ItemMetaData<SpawnEggMeta> SPAWN_EGG_META_DATA = new ItemMetaData<>(SpawnEggMeta.class,
            item -> new CraftMetaSpawnEgg(item.getComponentsPatch()),
            (type, meta) -> meta instanceof CraftMetaSpawnEgg spawnEgg ? spawnEgg : new CraftMetaSpawnEgg(meta));

    private static final ItemMetaData<ItemMeta> ARMOR_STAND_META_DATA = new ItemMetaData<>(ItemMeta.class,
            item -> new CraftMetaArmorStand(item.getComponentsPatch()),
            (type, meta) -> meta instanceof CraftMetaArmorStand armorStand ? armorStand : new CraftMetaArmorStand(meta));

    private static final ItemMetaData<KnowledgeBookMeta> KNOWLEDGE_BOOK_META_DATA = new ItemMetaData<>(KnowledgeBookMeta.class,
            item -> new CraftMetaKnowledgeBook(item.getComponentsPatch()),
            (type, meta) -> meta instanceof CraftMetaKnowledgeBook knowledgeBook ? knowledgeBook : new CraftMetaKnowledgeBook(meta));

    private static final ItemMetaData<BlockStateMeta> BLOCK_STATE_META_DATA = new ItemMetaData<>(BlockStateMeta.class,
            item -> new CraftMetaBlockState(item.getComponentsPatch(), CraftItemType.minecraftToBukkit(item.getItem())),
            (type, meta) -> new CraftMetaBlockState(meta, type.asMaterial()));

    private static final ItemMetaData<ShieldMeta> SHIELD_META_DATA = new ItemMetaData<>(ShieldMeta.class,
            item -> new CraftMetaShield(item.getComponentsPatch()),
            (type, meta) -> new CraftMetaShield(meta));

    private static final ItemMetaData<TropicalFishBucketMeta> TROPICAL_FISH_BUCKET_META_DATA = new ItemMetaData<>(TropicalFishBucketMeta.class,
            item -> new CraftMetaTropicalFishBucket(item.getComponentsPatch()),
            (type, meta) -> meta instanceof CraftMetaTropicalFishBucket tropicalFishBucket ? tropicalFishBucket : new CraftMetaTropicalFishBucket(meta));

    private static final ItemMetaData<AxolotlBucketMeta> AXOLOTL_BUCKET_META_DATA = new ItemMetaData<>(AxolotlBucketMeta.class,
            item -> new CraftMetaAxolotlBucket(item.getComponentsPatch()),
            (type, meta) -> meta instanceof CraftMetaAxolotlBucket axolotlBucket ? axolotlBucket : new CraftMetaAxolotlBucket(meta));

    private static final ItemMetaData<CrossbowMeta> CROSSBOW_META_DATA = new ItemMetaData<>(CrossbowMeta.class,
            item -> new CraftMetaCrossbow(item.getComponentsPatch()),
            (type, meta) -> meta instanceof CraftMetaCrossbow crossbow ? crossbow : new CraftMetaCrossbow(meta));

    private static final ItemMetaData<SuspiciousStewMeta> SUSPICIOUS_STEW_META_DATA = new ItemMetaData<>(SuspiciousStewMeta.class,
            item -> new CraftMetaSuspiciousStew(item.getComponentsPatch()),
            (type, meta) -> meta instanceof CraftMetaSuspiciousStew suspiciousStew ? suspiciousStew : new CraftMetaSuspiciousStew(meta));

    private static final ItemMetaData<ItemMeta> ENTITY_TAG_META_DATA = new ItemMetaData<>(ItemMeta.class,
            item -> new CraftMetaEntityTag(item.getComponentsPatch()),
            (type, meta) -> meta instanceof CraftMetaEntityTag entityTag ? entityTag : new CraftMetaEntityTag(meta));

    private static final ItemMetaData<CompassMeta> COMPASS_META_DATA = new ItemMetaData<>(CompassMeta.class,
            item -> new CraftMetaCompass(item.getComponentsPatch()),
            (type, meta) -> meta instanceof CraftMetaCompass compass ? compass : new CraftMetaCompass(meta));

    private static final ItemMetaData<BundleMeta> BUNDLE_META_DATA = new ItemMetaData<>(BundleMeta.class,
            item -> new CraftMetaBundle(item.getComponentsPatch()),
            (type, meta) -> meta instanceof CraftMetaBundle bundle ? bundle : new CraftMetaBundle(meta));

    private static final ItemMetaData<MusicInstrumentMeta> MUSIC_INSTRUMENT_META_DATA = new ItemMetaData<>(MusicInstrumentMeta.class,
            item -> new CraftMetaMusicInstrument(item.getComponentsPatch()),
            (type, meta) -> meta instanceof CraftMetaMusicInstrument musicInstrument ? musicInstrument : new CraftMetaMusicInstrument(meta));

    private static final ItemMetaData<OminousBottleMeta> OMINOUS_BOTTLE_META_DATA = new ItemMetaData<>(OminousBottleMeta.class,
            item -> new CraftMetaOminousBottle(item.getComponentsPatch()),
            (type, meta) -> meta instanceof CraftMetaOminousBottle musicInstrument ? musicInstrument : new CraftMetaOminousBottle(meta));

    // We use if instead of a set, since the result gets cached in CraftItemType,
    // which would result in dead memory once all ItemTypes have cached the data.
    public static <I extends ItemMeta> ItemMetaData<I> getItemMetaData(ItemType itemType) {
        if (itemType == ItemType.AIR) {
            return asType(EMPTY_META_DATA);
        }
        if (itemType == ItemType.WRITTEN_BOOK) {
            return asType(SIGNED_BOOK_META_DATA);
        }
        if (itemType == ItemType.WRITABLE_BOOK) {
            return asType(WRITABLE_BOOK_META_DATA);
        }
        if (itemType == ItemType.CREEPER_HEAD || itemType == ItemType.DRAGON_HEAD
                || itemType == ItemType.PIGLIN_HEAD || itemType == ItemType.PLAYER_HEAD
                || itemType == ItemType.SKELETON_SKULL || itemType == ItemType.WITHER_SKELETON_SKULL
                || itemType == ItemType.ZOMBIE_HEAD) {
            return asType(SKULL_META_DATA);
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
                || itemType == ItemType.TURTLE_HELMET) {
            return asType(ARMOR_META_DATA);
        }
        if (itemType == ItemType.LEATHER_HELMET || itemType == ItemType.LEATHER_CHESTPLATE
                || itemType == ItemType.LEATHER_LEGGINGS || itemType == ItemType.LEATHER_BOOTS
                || itemType == ItemType.WOLF_ARMOR) {
            return asType(COLORABLE_ARMOR_META_DATA);
        }
        if (itemType == ItemType.LEATHER_HORSE_ARMOR) {
            return asType(LEATHER_ARMOR_META_DATA);
        }
        if (itemType == ItemType.POTION || itemType == ItemType.SPLASH_POTION
                || itemType == ItemType.LINGERING_POTION || itemType == ItemType.TIPPED_ARROW) {
            return asType(POTION_META_DATA);
        }
        if (itemType == ItemType.FILLED_MAP) {
            return asType(MAP_META_DATA);
        }
        if (itemType == ItemType.FIREWORK_ROCKET) {
            return asType(FIREWORK_META_DATA);
        }
        if (itemType == ItemType.FIREWORK_STAR) {
            return asType(CHARGE_META_DATA);
        }
        if (itemType == ItemType.ENCHANTED_BOOK) {
            return asType(ENCHANTED_BOOK_META_DATA);
        }
        if (itemType.hasBlockType() && Tag.BANNERS.isTagged(itemType.getBlockType().asMaterial())) {
            return asType(BANNER_META_DATA);
        }
        if (itemType == ItemType.ARMADILLO_SPAWN_EGG || itemType == ItemType.ALLAY_SPAWN_EGG
                || itemType == ItemType.ARMADILLO_SPAWN_EGG || itemType == ItemType.ALLAY_SPAWN_EGG
                || itemType == ItemType.AXOLOTL_SPAWN_EGG || itemType == ItemType.BAT_SPAWN_EGG
                || itemType == ItemType.BEE_SPAWN_EGG || itemType == ItemType.BLAZE_SPAWN_EGG
                || itemType == ItemType.BOGGED_SPAWN_EGG || itemType == ItemType.BREEZE_SPAWN_EGG
                || itemType == ItemType.CAT_SPAWN_EGG || itemType == ItemType.CAMEL_SPAWN_EGG
                || itemType == ItemType.CAVE_SPIDER_SPAWN_EGG || itemType == ItemType.CHICKEN_SPAWN_EGG
                || itemType == ItemType.COD_SPAWN_EGG || itemType == ItemType.COW_SPAWN_EGG
                || itemType == ItemType.CREEPER_SPAWN_EGG || itemType == ItemType.DOLPHIN_SPAWN_EGG
                || itemType == ItemType.DONKEY_SPAWN_EGG || itemType == ItemType.DROWNED_SPAWN_EGG
                || itemType == ItemType.ELDER_GUARDIAN_SPAWN_EGG || itemType == ItemType.ENDER_DRAGON_SPAWN_EGG
                || itemType == ItemType.ENDERMAN_SPAWN_EGG || itemType == ItemType.ENDERMITE_SPAWN_EGG
                || itemType == ItemType.EVOKER_SPAWN_EGG || itemType == ItemType.FOX_SPAWN_EGG
                || itemType == ItemType.FROG_SPAWN_EGG || itemType == ItemType.GHAST_SPAWN_EGG
                || itemType == ItemType.GLOW_SQUID_SPAWN_EGG || itemType == ItemType.GOAT_SPAWN_EGG
                || itemType == ItemType.GUARDIAN_SPAWN_EGG || itemType == ItemType.HOGLIN_SPAWN_EGG
                || itemType == ItemType.HORSE_SPAWN_EGG || itemType == ItemType.HUSK_SPAWN_EGG
                || itemType == ItemType.IRON_GOLEM_SPAWN_EGG || itemType == ItemType.LLAMA_SPAWN_EGG
                || itemType == ItemType.MAGMA_CUBE_SPAWN_EGG || itemType == ItemType.MOOSHROOM_SPAWN_EGG
                || itemType == ItemType.MULE_SPAWN_EGG || itemType == ItemType.OCELOT_SPAWN_EGG
                || itemType == ItemType.PANDA_SPAWN_EGG || itemType == ItemType.PARROT_SPAWN_EGG
                || itemType == ItemType.PHANTOM_SPAWN_EGG || itemType == ItemType.PIGLIN_BRUTE_SPAWN_EGG
                || itemType == ItemType.PIGLIN_SPAWN_EGG || itemType == ItemType.PIG_SPAWN_EGG
                || itemType == ItemType.PILLAGER_SPAWN_EGG || itemType == ItemType.POLAR_BEAR_SPAWN_EGG
                || itemType == ItemType.PUFFERFISH_SPAWN_EGG || itemType == ItemType.RABBIT_SPAWN_EGG
                || itemType == ItemType.RAVAGER_SPAWN_EGG || itemType == ItemType.SALMON_SPAWN_EGG
                || itemType == ItemType.SHEEP_SPAWN_EGG || itemType == ItemType.SHULKER_SPAWN_EGG
                || itemType == ItemType.SILVERFISH_SPAWN_EGG || itemType == ItemType.SKELETON_HORSE_SPAWN_EGG
                || itemType == ItemType.SKELETON_SPAWN_EGG || itemType == ItemType.SLIME_SPAWN_EGG
                || itemType == ItemType.SNIFFER_SPAWN_EGG || itemType == ItemType.SNOW_GOLEM_SPAWN_EGG
                || itemType == ItemType.SPIDER_SPAWN_EGG || itemType == ItemType.SQUID_SPAWN_EGG
                || itemType == ItemType.STRAY_SPAWN_EGG || itemType == ItemType.STRIDER_SPAWN_EGG
                || itemType == ItemType.TADPOLE_SPAWN_EGG || itemType == ItemType.TRADER_LLAMA_SPAWN_EGG
                || itemType == ItemType.TROPICAL_FISH_SPAWN_EGG || itemType == ItemType.TURTLE_SPAWN_EGG
                || itemType == ItemType.VEX_SPAWN_EGG || itemType == ItemType.VILLAGER_SPAWN_EGG
                || itemType == ItemType.VINDICATOR_SPAWN_EGG || itemType == ItemType.WANDERING_TRADER_SPAWN_EGG
                || itemType == ItemType.WARDEN_SPAWN_EGG || itemType == ItemType.WITCH_SPAWN_EGG
                || itemType == ItemType.WITHER_SKELETON_SPAWN_EGG || itemType == ItemType.WITHER_SPAWN_EGG
                || itemType == ItemType.WOLF_SPAWN_EGG || itemType == ItemType.ZOGLIN_SPAWN_EGG
                || itemType == ItemType.ZOMBIE_HORSE_SPAWN_EGG || itemType == ItemType.ZOMBIE_SPAWN_EGG
                || itemType == ItemType.ZOMBIE_VILLAGER_SPAWN_EGG || itemType == ItemType.ZOMBIFIED_PIGLIN_SPAWN_EGG) {
            return asType(SPAWN_EGG_META_DATA);
        }
        if (itemType == ItemType.ARMOR_STAND) {
            return asType(ARMOR_STAND_META_DATA);
        }
        if (itemType == ItemType.KNOWLEDGE_BOOK) {
            return asType(KNOWLEDGE_BOOK_META_DATA);
        }
        if (itemType == ItemType.FURNACE || itemType == ItemType.CHEST
                || itemType == ItemType.TRAPPED_CHEST || itemType == ItemType.JUKEBOX
                || itemType == ItemType.DISPENSER || itemType == ItemType.DROPPER
                || (itemType.hasBlockType() && Tag.ALL_SIGNS.isTagged(itemType.getBlockType().asMaterial())) || itemType == ItemType.SPAWNER
                || itemType == ItemType.BREWING_STAND || itemType == ItemType.ENCHANTING_TABLE
                || itemType == ItemType.COMMAND_BLOCK || itemType == ItemType.REPEATING_COMMAND_BLOCK
                || itemType == ItemType.CHAIN_COMMAND_BLOCK || itemType == ItemType.BEACON
                || itemType == ItemType.DAYLIGHT_DETECTOR || itemType == ItemType.HOPPER
                || itemType == ItemType.COMPARATOR || itemType == ItemType.STRUCTURE_BLOCK
                || (itemType.hasBlockType() && Tag.SHULKER_BOXES.isTagged(itemType.getBlockType().asMaterial()))
                || itemType == ItemType.ENDER_CHEST || itemType == ItemType.BARREL
                || itemType == ItemType.BELL || itemType == ItemType.BLAST_FURNACE
                || itemType == ItemType.CAMPFIRE || itemType == ItemType.SOUL_CAMPFIRE
                || itemType == ItemType.JIGSAW || itemType == ItemType.LECTERN
                || itemType == ItemType.SMOKER || itemType == ItemType.BEEHIVE
                || itemType == ItemType.BEE_NEST || itemType == ItemType.SCULK_CATALYST
                || itemType == ItemType.SCULK_SHRIEKER || itemType == ItemType.SCULK_SENSOR
                || itemType == ItemType.CALIBRATED_SCULK_SENSOR || itemType == ItemType.CHISELED_BOOKSHELF
                || itemType == ItemType.DECORATED_POT || itemType == ItemType.SUSPICIOUS_SAND
                || itemType == ItemType.SUSPICIOUS_GRAVEL || itemType == ItemType.CRAFTER
                || itemType == ItemType.TRIAL_SPAWNER || itemType == ItemType.VAULT) {
            return asType(BLOCK_STATE_META_DATA);
        }
        if (itemType == ItemType.SHIELD) {
            return asType(SHIELD_META_DATA);
        }
        if (itemType == ItemType.TROPICAL_FISH_BUCKET) {
            return asType(TROPICAL_FISH_BUCKET_META_DATA);
        }
        if (itemType == ItemType.AXOLOTL_BUCKET) {
            return asType(AXOLOTL_BUCKET_META_DATA);
        }
        if (itemType == ItemType.CROSSBOW) {
            return asType(CROSSBOW_META_DATA);
        }
        if (itemType == ItemType.SUSPICIOUS_STEW) {
            return asType(SUSPICIOUS_STEW_META_DATA);
        }
        if (itemType == ItemType.COD_BUCKET || itemType == ItemType.PUFFERFISH_BUCKET
                || itemType == ItemType.SALMON_BUCKET || itemType == ItemType.ITEM_FRAME
                || itemType == ItemType.GLOW_ITEM_FRAME || itemType == ItemType.PAINTING) {
            return asType(ENTITY_TAG_META_DATA);
        }
        if (itemType == ItemType.COMPASS) {
            return asType(COMPASS_META_DATA);
        }
        if (itemType == ItemType.BUNDLE) {
            return asType(BUNDLE_META_DATA);
        }
        if (itemType == ItemType.GOAT_HORN) {
            return asType(MUSIC_INSTRUMENT_META_DATA);
        }

        if (itemType == ItemType.OMINOUS_BOTTLE) {
            return asType(OMINOUS_BOTTLE_META_DATA);
        }

        return asType(ITEM_META_DATA);
    }

    private static <I extends ItemMeta> ItemMetaData<I> asType(ItemMetaData<?> metaData) {
        return (ItemMetaData<I>) metaData;
    }

    private CraftItemMetas() {
    }
}
