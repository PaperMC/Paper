package io.papermc.paper.generator;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

public class ItemTagsProvider extends IntrinsicHolderTagsProvider<Item> {

    public ItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Item>> parentProvider) {
        super(output, Registries.ITEM, lookupProvider, parentProvider, item -> item.builtInRegistryHolder().key());
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        (new BlockItemTagsProvider() {
            @Override
            protected TagAppender<Block, Block> tag(TagKey<Block> blockTag, TagKey<Item> itemTag) {
                return new BlockToItemConverter(ItemTagsProvider.this.tag(itemTag));
            }
        }).run();
        this.tag(ItemTags.BUCKET)
            .addTag(ItemTags.FISH_BUCKET)
            .add(
                Items.POWDER_SNOW_BUCKET,
                Items.TADPOLE_BUCKET,
                Items.AXOLOTL_BUCKET,
                Items.BUCKET,
                Items.LAVA_BUCKET,
                Items.MILK_BUCKET,
                Items.WATER_BUCKET
            );
        this.tag(ItemTags.BEEHIVES)
            .add(
                Items.BEE_NEST,
                Items.BEEHIVE
            );
        this.tag(ItemTags.FISH_BUCKET)
            .add(
                Items.PUFFERFISH_BUCKET,
                Items.TROPICAL_FISH_BUCKET,
                Items.SALMON_BUCKET,
                Items.COD_BUCKET
            );
        this.tag(ItemTags.CONCRETE_POWDER)
            .add(
                Items.WHITE_CONCRETE_POWDER,
                Items.ORANGE_CONCRETE_POWDER,
                Items.MAGENTA_CONCRETE_POWDER,
                Items.LIGHT_BLUE_CONCRETE_POWDER,
                Items.YELLOW_CONCRETE_POWDER,
                Items.LIME_CONCRETE_POWDER,
                Items.PINK_CONCRETE_POWDER,
                Items.GRAY_CONCRETE_POWDER,
                Items.LIGHT_GRAY_CONCRETE_POWDER,
                Items.CYAN_CONCRETE_POWDER,
                Items.PURPLE_CONCRETE_POWDER,
                Items.BLUE_CONCRETE_POWDER,
                Items.BROWN_CONCRETE_POWDER,
                Items.GREEN_CONCRETE_POWDER,
                Items.RED_CONCRETE_POWDER,
                Items.BLACK_CONCRETE_POWDER
            );
        this.tag(ItemTags.DYE)
            .add(
                Items.LIGHT_GRAY_DYE,
                Items.LIME_DYE,
                Items.RED_DYE,
                Items.BROWN_DYE,
                Items.GRAY_DYE,
                Items.YELLOW_DYE,
                Items.BLUE_DYE,
                Items.LIGHT_BLUE_DYE,
                Items.CYAN_DYE,
                Items.MAGENTA_DYE,
                Items.PURPLE_DYE,
                Items.WHITE_DYE,
                Items.PINK_DYE,
                Items.GREEN_DYE,
                Items.ORANGE_DYE,
                Items.BLACK_DYE
            );
        this.tag(ItemTags.ICE)
            .add(
                Items.ICE,
                Items.PACKED_ICE,
                Items.BLUE_ICE
            );
        this.tag(ItemTags.SNOW)
            .add(
                Items.SNOW,
                Items.SNOW_BLOCK
            );
        this.tag(ItemTags.GOLDEN_APPLE)
            .add(
                Items.ENCHANTED_GOLDEN_APPLE,
                Items.GOLDEN_APPLE
            );
        this.tag(ItemTags.HORSE_ARMORS)
            .add(
                Items.GOLDEN_HORSE_ARMOR,
                Items.LEATHER_HORSE_ARMOR,
                Items.COPPER_HORSE_ARMOR,
                Items.DIAMOND_HORSE_ARMOR,
                Items.IRON_HORSE_ARMOR,
                Items.NETHERITE_HORSE_ARMOR
            );
        this.tag(ItemTags.MUSIC_DISCS)
            .add(
                Items.MUSIC_DISC_13,
                Items.MUSIC_DISC_MALL,
                Items.MUSIC_DISC_CAT,
                Items.MUSIC_DISC_MELLOHI,
                Items.MUSIC_DISC_BLOCKS,
                Items.MUSIC_DISC_RELIC,
                Items.MUSIC_DISC_5,
                Items.MUSIC_DISC_CREATOR_MUSIC_BOX,
                Items.MUSIC_DISC_STAL,
                Items.MUSIC_DISC_TEARS,
                Items.MUSIC_DISC_WAIT,
                Items.MUSIC_DISC_CREATOR,
                Items.MUSIC_DISC_WARD,
                Items.MUSIC_DISC_11,
                Items.MUSIC_DISC_LAVA_CHICKEN,
                Items.MUSIC_DISC_PRECIPICE,
                Items.MUSIC_DISC_CHIRP,
                Items.MUSIC_DISC_OTHERSIDE,
                Items.MUSIC_DISC_STRAD,
                Items.MUSIC_DISC_FAR,
                Items.MUSIC_DISC_PIGSTEP
            );
        this.tag(ItemTags.POTATO)
            .add(
                Items.POTATO,
                Items.BAKED_POTATO,
                Items.POISONOUS_POTATO
            );
        this.tag(ItemTags.COOKED_FISH)
            .add(
                Items.COOKED_COD,
                Items.COOKED_SALMON
            );
        this.tag(ItemTags.RAW_FISH)
            .add(
                Items.SALMON,
                Items.TROPICAL_FISH,
                Items.PUFFERFISH,
                Items.COD
            );
        this.tag(ItemTags.SPAWN_EGGS)
            .add(
                Items.LLAMA_SPAWN_EGG,
                Items.ZOMBIE_HORSE_SPAWN_EGG,
                Items.COW_SPAWN_EGG,
                Items.AXOLOTL_SPAWN_EGG,
                Items.DONKEY_SPAWN_EGG,
                Items.PILLAGER_SPAWN_EGG,
                Items.RAVAGER_SPAWN_EGG,
                Items.BOGGED_SPAWN_EGG,
                Items.SHULKER_SPAWN_EGG,
                Items.BLAZE_SPAWN_EGG,
                Items.PHANTOM_SPAWN_EGG,
                Items.PIG_SPAWN_EGG,
                Items.TADPOLE_SPAWN_EGG,
                Items.SNIFFER_SPAWN_EGG,
                Items.ENDERMITE_SPAWN_EGG,
                Items.FOX_SPAWN_EGG,
                Items.RABBIT_SPAWN_EGG,
                Items.WANDERING_TRADER_SPAWN_EGG,
                Items.VILLAGER_SPAWN_EGG,
                Items.SNOW_GOLEM_SPAWN_EGG,
                Items.ENDER_DRAGON_SPAWN_EGG,
                Items.TRADER_LLAMA_SPAWN_EGG,
                Items.CAT_SPAWN_EGG,
                Items.BAT_SPAWN_EGG,
                Items.VINDICATOR_SPAWN_EGG,
                Items.COD_SPAWN_EGG,
                Items.SILVERFISH_SPAWN_EGG,
                Items.STRIDER_SPAWN_EGG,
                Items.BREEZE_SPAWN_EGG,
                Items.HORSE_SPAWN_EGG,
                Items.WOLF_SPAWN_EGG,
                Items.ZOMBIE_NAUTILUS_SPAWN_EGG,
                Items.FROG_SPAWN_EGG,
                Items.NAUTILUS_SPAWN_EGG,
                Items.WARDEN_SPAWN_EGG,
                Items.PARROT_SPAWN_EGG,
                Items.SKELETON_HORSE_SPAWN_EGG,
                Items.MULE_SPAWN_EGG,
                Items.TURTLE_SPAWN_EGG,
                Items.CAMEL_HUSK_SPAWN_EGG,
                Items.WITHER_SPAWN_EGG,
                Items.SKELETON_SPAWN_EGG,
                Items.IRON_GOLEM_SPAWN_EGG,
                Items.SPIDER_SPAWN_EGG,
                Items.MAGMA_CUBE_SPAWN_EGG,
                Items.PUFFERFISH_SPAWN_EGG,
                Items.ARMADILLO_SPAWN_EGG,
                Items.GLOW_SQUID_SPAWN_EGG,
                Items.ZOMBIE_VILLAGER_SPAWN_EGG,
                Items.GUARDIAN_SPAWN_EGG,
                Items.PIGLIN_SPAWN_EGG,
                Items.DROWNED_SPAWN_EGG,
                Items.EVOKER_SPAWN_EGG,
                Items.PIGLIN_BRUTE_SPAWN_EGG,
                Items.SALMON_SPAWN_EGG,
                Items.HUSK_SPAWN_EGG,
                Items.TROPICAL_FISH_SPAWN_EGG,
                Items.ELDER_GUARDIAN_SPAWN_EGG,
                Items.HAPPY_GHAST_SPAWN_EGG,
                Items.GOAT_SPAWN_EGG,
                Items.POLAR_BEAR_SPAWN_EGG,
                Items.CREEPER_SPAWN_EGG,
                Items.ZOMBIE_SPAWN_EGG,
                Items.DOLPHIN_SPAWN_EGG,
                Items.GHAST_SPAWN_EGG,
                Items.VEX_SPAWN_EGG,
                Items.PARCHED_SPAWN_EGG,
                Items.SHEEP_SPAWN_EGG,
                Items.WITCH_SPAWN_EGG,
                Items.WITHER_SKELETON_SPAWN_EGG,
                Items.ZOGLIN_SPAWN_EGG,
                Items.BEE_SPAWN_EGG,
                Items.CREAKING_SPAWN_EGG,
                Items.CAVE_SPIDER_SPAWN_EGG,
                Items.ENDERMAN_SPAWN_EGG,
                Items.PANDA_SPAWN_EGG,
                Items.SQUID_SPAWN_EGG,
                Items.COPPER_GOLEM_SPAWN_EGG,
                Items.HOGLIN_SPAWN_EGG,
                Items.CHICKEN_SPAWN_EGG,
                Items.MOOSHROOM_SPAWN_EGG,
                Items.OCELOT_SPAWN_EGG,
                Items.SLIME_SPAWN_EGG,
                Items.STRAY_SPAWN_EGG,
                Items.ALLAY_SPAWN_EGG,
                Items.CAMEL_SPAWN_EGG,
                Items.ZOMBIFIED_PIGLIN_SPAWN_EGG
            );
        this.tag(ItemTags.TORCHES)
            .add(
                Items.REDSTONE_TORCH,
                Items.SOUL_TORCH,
                Items.COPPER_TORCH,
                Items.TORCH
            );
        this.tag(ItemTags.CAMPFIRES)
            .add(
                Items.CAMPFIRE,
                Items.SOUL_CAMPFIRE
            );
        this.tag(ItemTags.ARMOR)
            .addTag(net.minecraft.tags.ItemTags.HEAD_ARMOR)
            .addTag(net.minecraft.tags.ItemTags.CHEST_ARMOR)
            .addTag(net.minecraft.tags.ItemTags.LEG_ARMOR)
            .addTag(net.minecraft.tags.ItemTags.FOOT_ARMOR);
        this.tag(ItemTags.BOWS)
            .add(
                Items.BOW,
                Items.CROSSBOW
            );
        this.tag(ItemTags.THROWABLE_PROJECTILES)
            .addTag(net.minecraft.tags.ItemTags.EGGS)
            .add(
                Items.SNOWBALL,
                Items.WIND_CHARGE,
                Items.TRIDENT,
                Items.FIREWORK_ROCKET,
                Items.LINGERING_POTION,
                Items.ENDER_PEARL,
                Items.SPLASH_POTION,
                Items.EXPERIENCE_BOTTLE
            );
        this.tag(ItemTags.COLORABLE)
            .addTag(ItemTags.CONCRETE_POWDER);
        this.tag(ItemTags.NYLIUM)
            .add(
                Items.CRIMSON_NYLIUM,
                Items.WARPED_NYLIUM
            );
        this.tag(ItemTags.RAW_ORES)
            .add(
                Items.RAW_GOLD,
                Items.RAW_IRON,
                Items.RAW_COPPER
            );
        this.tag(ItemTags.PRESSURE_PLATES)
            .addTag(net.minecraft.tags.ItemTags.WOODEN_PRESSURE_PLATES)
            .addTag(ItemTags.STONE_PRESSURE_PLATES)
            .add(
                Items.LIGHT_WEIGHTED_PRESSURE_PLATE,
                Items.HEAVY_WEIGHTED_PRESSURE_PLATE
            );
        this.tag(ItemTags.STONE_PRESSURE_PLATES)
            .add(
                Items.STONE_PRESSURE_PLATE,
                Items.POLISHED_BLACKSTONE_PRESSURE_PLATE
            );
        this.tag(ItemTags.WOODEN_TOOLS)
            .add(
                Items.WOODEN_HOE,
                Items.WOODEN_AXE,
                Items.WOODEN_SHOVEL,
                Items.WOODEN_SWORD,
                Items.WOODEN_PICKAXE,
                Items.WOODEN_SPEAR
            );
        this.tag(ItemTags.STONE_TOOLS)
            .add(
                Items.STONE_PICKAXE,
                Items.STONE_SPEAR,
                Items.STONE_AXE,
                Items.STONE_HOE,
                Items.STONE_SHOVEL,
                Items.STONE_SWORD
            );
        this.tag(ItemTags.COPPER_TOOLS)
            .add(
                Items.COPPER_SHOVEL,
                Items.COPPER_SWORD,
                Items.COPPER_SPEAR,
                Items.COPPER_HOE,
                Items.COPPER_AXE,
                Items.COPPER_PICKAXE
            );
        this.tag(ItemTags.IRON_TOOLS)
            .add(
                Items.IRON_AXE,
                Items.IRON_SPEAR,
                Items.IRON_HOE,
                Items.IRON_PICKAXE,
                Items.IRON_SWORD,
                Items.IRON_SHOVEL
            );
        this.tag(ItemTags.GOLDEN_TOOLS)
            .add(
                Items.GOLDEN_AXE,
                Items.GOLDEN_SHOVEL,
                Items.GOLDEN_SWORD,
                Items.GOLDEN_PICKAXE,
                Items.GOLDEN_SPEAR,
                Items.GOLDEN_HOE
            );
        this.tag(ItemTags.DIAMOND_TOOLS)
            .add(
                Items.DIAMOND_HOE,
                Items.DIAMOND_SWORD,
                Items.DIAMOND_AXE,
                Items.DIAMOND_PICKAXE,
                Items.DIAMOND_SHOVEL,
                Items.DIAMOND_SPEAR
            );
        this.tag(ItemTags.NETHERITE_TOOLS)
            .add(
                Items.NETHERITE_AXE,
                Items.NETHERITE_SWORD,
                Items.NETHERITE_HOE,
                Items.NETHERITE_SHOVEL,
                Items.NETHERITE_PICKAXE,
                Items.NETHERITE_SPEAR
            );
    }

    static class BlockToItemConverter implements TagAppender<Block, Block> {
        private final TagAppender<Item, Item> itemAppender;

        public BlockToItemConverter(TagAppender<Item, Item> itemAppender) {
            this.itemAppender = itemAppender;
        }

        private static Item blockToItem(Block block) {
            Item item = block.asItem();
            if (item == Items.AIR) {
                throw new RuntimeException("Unknown block item for " + BuiltInRegistries.BLOCK.getKey(block));
            }
            return item;
        }

        @Override
        public TagAppender<Block, Block> add(Block block) {
            this.itemAppender.add(blockToItem(block));
            return this;
        }

        @Override
        public TagAppender<Block, Block> addOptional(Block block) {
            this.itemAppender.addOptional(blockToItem(block));
            return this;
        }

        private static TagKey<Item> blockTagToItemTag(TagKey<Block> tag) {
            return TagKey.create(Registries.ITEM, tag.location());
        }

        @Override
        public TagAppender<Block, Block> addTag(TagKey<Block> tag) {
            this.itemAppender.addTag(blockTagToItemTag(tag));
            return this;
        }

        @Override
        public TagAppender<Block, Block> addOptionalTag(TagKey<Block> tag) {
            this.itemAppender.addOptionalTag(blockTagToItemTag(tag));
            return this;
        }
    }
}
