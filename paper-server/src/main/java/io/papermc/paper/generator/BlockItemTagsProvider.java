package io.papermc.paper.generator;

import com.google.common.collect.ImmutableList;
import java.util.function.Function;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WeatheringCopperBlocks;

public abstract class BlockItemTagsProvider {

    protected void run() {
        this.tag(BlockTags.COBBLESTONE_WALL, ItemTags.COBBLESTONE_WALL)
            .add(
                Blocks.MOSSY_COBBLESTONE_WALL,
                Blocks.COBBLESTONE_WALL
            );
        this.tag(BlockTags.COBBLESTONE, ItemTags.COBBLESTONE)
            .add(
                Blocks.MOSSY_COBBLESTONE,
                Blocks.COBBLESTONE
            );
        this.tag(BlockTags.CONCRETE, ItemTags.CONCRETE)
            .add(
                Blocks.GRAY_CONCRETE,
                Blocks.LIME_CONCRETE,
                Blocks.MAGENTA_CONCRETE,
                Blocks.CYAN_CONCRETE,
                Blocks.BROWN_CONCRETE,
                Blocks.RED_CONCRETE,
                Blocks.WHITE_CONCRETE,
                Blocks.LIGHT_BLUE_CONCRETE,
                Blocks.GREEN_CONCRETE,
                Blocks.BLUE_CONCRETE,
                Blocks.PINK_CONCRETE,
                Blocks.BLACK_CONCRETE,
                Blocks.ORANGE_CONCRETE,
                Blocks.LIGHT_GRAY_CONCRETE,
                Blocks.PURPLE_CONCRETE,
                Blocks.YELLOW_CONCRETE
            );
        this.tag(BlockTags.GLASS, ItemTags.GLASS)
            .addTag(BlockTags.STAINED_GLASS)
            .add(
                Blocks.TINTED_GLASS,
                Blocks.GLASS
            );
        this.tag(BlockTags.GLASS_PANE, ItemTags.GLASS_PANE)
            .addTag(BlockTags.STAINED_GLASS_PANE)
            .add(Blocks.GLASS_PANE);
        this.tag(BlockTags.GLAZED_TERRACOTTA, ItemTags.GLAZED_TERRACOTTA)
            .add(
                Blocks.PINK_GLAZED_TERRACOTTA,
                Blocks.GREEN_GLAZED_TERRACOTTA,
                Blocks.LIME_GLAZED_TERRACOTTA,
                Blocks.BLUE_GLAZED_TERRACOTTA,
                Blocks.BLACK_GLAZED_TERRACOTTA,
                Blocks.RED_GLAZED_TERRACOTTA,
                Blocks.BROWN_GLAZED_TERRACOTTA,
                Blocks.GRAY_GLAZED_TERRACOTTA,
                Blocks.MAGENTA_GLAZED_TERRACOTTA,
                Blocks.CYAN_GLAZED_TERRACOTTA,
                Blocks.ORANGE_GLAZED_TERRACOTTA,
                Blocks.WHITE_GLAZED_TERRACOTTA,
                Blocks.PURPLE_GLAZED_TERRACOTTA,
                Blocks.YELLOW_GLAZED_TERRACOTTA,
                Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA,
                Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA
            );
        this.tag(BlockTags.STAINED_TERRACOTTA, ItemTags.STAINED_TERRACOTTA)
            .add(
                Blocks.GRAY_TERRACOTTA,
                Blocks.GREEN_TERRACOTTA,
                Blocks.BROWN_TERRACOTTA,
                Blocks.YELLOW_TERRACOTTA,
                Blocks.LIGHT_BLUE_TERRACOTTA,
                Blocks.ORANGE_TERRACOTTA,
                Blocks.RED_TERRACOTTA,
                Blocks.WHITE_TERRACOTTA,
                Blocks.LIME_TERRACOTTA,
                Blocks.LIGHT_GRAY_TERRACOTTA,
                Blocks.CYAN_TERRACOTTA,
                Blocks.MAGENTA_TERRACOTTA,
                Blocks.PINK_TERRACOTTA,
                Blocks.BLACK_TERRACOTTA,
                Blocks.PURPLE_TERRACOTTA,
                Blocks.BLUE_TERRACOTTA
            );
        this.tag(BlockTags.TERRACOTTA, ItemTags.TERRACOTTA)
            .addTag(BlockTags.GLAZED_TERRACOTTA)
            .addTag(BlockTags.STAINED_TERRACOTTA)
            .add(Blocks.TERRACOTTA);
        this.tag(BlockTags.INFESTED_BLOCKS, ItemTags.INFESTED_BLOCKS)
            .add(
                Blocks.INFESTED_STONE_BRICKS,
                Blocks.INFESTED_COBBLESTONE,
                Blocks.INFESTED_DEEPSLATE,
                Blocks.INFESTED_STONE,
                Blocks.INFESTED_CHISELED_STONE_BRICKS,
                Blocks.INFESTED_CRACKED_STONE_BRICKS,
                Blocks.INFESTED_MOSSY_STONE_BRICKS
            );
        this.tag(BlockTags.MUSHROOM_BLOCKS, ItemTags.MUSHROOM_BLOCKS)
            .add(
                Blocks.BROWN_MUSHROOM_BLOCK,
                Blocks.MUSHROOM_STEM,
                Blocks.RED_MUSHROOM_BLOCK
            );
        this.tag(BlockTags.MUSHROOMS, ItemTags.MUSHROOMS)
            .add(
                Blocks.BROWN_MUSHROOM,
                Blocks.RED_MUSHROOM
            );
        this.tag(BlockTags.PISTONS, ItemTags.PISTONS)
            .add(
                Blocks.STICKY_PISTON,
                Blocks.PISTON
            );
        this.tag(BlockTags.PRISMARINE, ItemTags.PRISMARINE)
            .add(
                Blocks.PRISMARINE_BRICKS,
                Blocks.PRISMARINE,
                Blocks.DARK_PRISMARINE
            );
        this.tag(BlockTags.PRISMARINE_SLABS, ItemTags.PRISMARINE_SLABS)
            .add(
                Blocks.PRISMARINE_BRICK_SLAB,
                Blocks.PRISMARINE_SLAB,
                Blocks.DARK_PRISMARINE_SLAB
            );
        this.tag(BlockTags.PRISMARINE_STAIRS, ItemTags.PRISMARINE_STAIRS)
            .add(
                Blocks.DARK_PRISMARINE_STAIRS,
                Blocks.PRISMARINE_BRICK_STAIRS,
                Blocks.PRISMARINE_STAIRS
            );
        this.tag(BlockTags.PUMPKIN, ItemTags.PUMPKIN)
            .add(
                Blocks.CARVED_PUMPKIN,
                Blocks.JACK_O_LANTERN,
                Blocks.PUMPKIN
            );
        this.tag(BlockTags.QUARTZ_BLOCKS, ItemTags.QUARTZ_BLOCKS)
            .add(
                Blocks.CHISELED_QUARTZ_BLOCK,
                Blocks.QUARTZ_BLOCK,
                Blocks.SMOOTH_QUARTZ,
                Blocks.QUARTZ_PILLAR
            );
        this.tag(BlockTags.RED_SANDSTONES, ItemTags.RED_SANDSTONES)
            .add(
                Blocks.SMOOTH_RED_SANDSTONE,
                Blocks.RED_SANDSTONE,
                Blocks.CHISELED_RED_SANDSTONE,
                Blocks.CUT_RED_SANDSTONE
            );
        this.tag(BlockTags.SANDSTONES, ItemTags.SANDSTONES)
            .add(
                Blocks.SMOOTH_SANDSTONE,
                Blocks.CUT_SANDSTONE,
                Blocks.SANDSTONE,
                Blocks.CHISELED_SANDSTONE
            );
        this.tag(BlockTags.SPONGE, ItemTags.SPONGE)
            .add(
                Blocks.WET_SPONGE,
                Blocks.SPONGE
            );
        this.tag(BlockTags.STAINED_GLASS, ItemTags.STAINED_GLASS)
            .add(
                Blocks.GREEN_STAINED_GLASS,
                Blocks.LIGHT_BLUE_STAINED_GLASS,
                Blocks.PURPLE_STAINED_GLASS,
                Blocks.GRAY_STAINED_GLASS,
                Blocks.ORANGE_STAINED_GLASS,
                Blocks.BROWN_STAINED_GLASS,
                Blocks.BLACK_STAINED_GLASS,
                Blocks.WHITE_STAINED_GLASS,
                Blocks.LIGHT_GRAY_STAINED_GLASS,
                Blocks.PINK_STAINED_GLASS,
                Blocks.YELLOW_STAINED_GLASS,
                Blocks.MAGENTA_STAINED_GLASS,
                Blocks.BLUE_STAINED_GLASS,
                Blocks.CYAN_STAINED_GLASS,
                Blocks.LIME_STAINED_GLASS,
                Blocks.RED_STAINED_GLASS
            );
        this.tag(BlockTags.STAINED_GLASS_PANE, ItemTags.STAINED_GLASS_PANE)
            .add(
                Blocks.LIGHT_GRAY_STAINED_GLASS_PANE,
                Blocks.LIGHT_BLUE_STAINED_GLASS_PANE,
                Blocks.LIME_STAINED_GLASS_PANE,
                Blocks.PURPLE_STAINED_GLASS_PANE,
                Blocks.WHITE_STAINED_GLASS_PANE,
                Blocks.PINK_STAINED_GLASS_PANE,
                Blocks.GRAY_STAINED_GLASS_PANE,
                Blocks.BROWN_STAINED_GLASS_PANE,
                Blocks.GREEN_STAINED_GLASS_PANE,
                Blocks.ORANGE_STAINED_GLASS_PANE,
                Blocks.RED_STAINED_GLASS_PANE,
                Blocks.BLUE_STAINED_GLASS_PANE,
                Blocks.BLACK_STAINED_GLASS_PANE,
                Blocks.MAGENTA_STAINED_GLASS_PANE,
                Blocks.CYAN_STAINED_GLASS_PANE,
                Blocks.YELLOW_STAINED_GLASS_PANE
            );
        this.tag(BlockTags.PURPUR, ItemTags.PURPUR)
            .add(
                Blocks.PURPUR_PILLAR,
                Blocks.PURPUR_BLOCK,
                Blocks.PURPUR_SLAB,
                Blocks.PURPUR_STAIRS
            );
        this.tag(BlockTags.COLORABLE, ItemTags.COLORABLE)
            .addTag(net.minecraft.tags.BlockTags.WOOL)
            .addTag(net.minecraft.tags.BlockTags.WOOL_CARPETS)
            .addTag(net.minecraft.tags.BlockTags.SHULKER_BOXES)
            .addTag(net.minecraft.tags.BlockTags.BEDS)
            .addTag(net.minecraft.tags.BlockTags.CANDLES)
            .addTag(net.minecraft.tags.BlockTags.TERRACOTTA)
            .addTag(BlockTags.CONCRETE)
            .addTag(BlockTags.GLAZED_TERRACOTTA)
            .addTag(BlockTags.STAINED_GLASS)
            .addTag(BlockTags.STAINED_GLASS_PANE);
        this.tag(BlockTags.CORAL, ItemTags.CORAL)
            .add(
                Blocks.BRAIN_CORAL,
                Blocks.DEAD_BRAIN_CORAL,
                Blocks.DEAD_BUBBLE_CORAL,
                Blocks.FIRE_CORAL,
                Blocks.TUBE_CORAL,
                Blocks.HORN_CORAL,
                Blocks.BUBBLE_CORAL,
                Blocks.DEAD_HORN_CORAL,
                Blocks.DEAD_FIRE_CORAL,
                Blocks.DEAD_TUBE_CORAL
            );
        this.tag(BlockTags.CORAL_FAN, ItemTags.CORAL_FAN)
            .add(
                Blocks.BRAIN_CORAL_FAN,
                Blocks.TUBE_CORAL_FAN,
                Blocks.DEAD_FIRE_CORAL_FAN,
                Blocks.DEAD_TUBE_CORAL_FAN,
                Blocks.DEAD_BRAIN_CORAL_FAN,
                Blocks.FIRE_CORAL_FAN,
                Blocks.HORN_CORAL_FAN,
                Blocks.DEAD_HORN_CORAL_FAN,
                Blocks.BUBBLE_CORAL_FAN,
                Blocks.DEAD_BUBBLE_CORAL_FAN
            );
        this.tag(BlockTags.CORAL_BLOCKS, ItemTags.CORAL_BLOCKS)
            .add(
                Blocks.DEAD_BRAIN_CORAL_BLOCK,
                Blocks.TUBE_CORAL_BLOCK,
                Blocks.DEAD_BUBBLE_CORAL_BLOCK,
                Blocks.BUBBLE_CORAL_BLOCK,
                Blocks.BRAIN_CORAL_BLOCK,
                Blocks.DEAD_HORN_CORAL_BLOCK,
                Blocks.FIRE_CORAL_BLOCK,
                Blocks.DEAD_FIRE_CORAL_BLOCK,
                Blocks.DEAD_TUBE_CORAL_BLOCK,
                Blocks.HORN_CORAL_BLOCK
            );
        this.tag(BlockTags.COMMAND_BLOCKS, ItemTags.COMMAND_BLOCKS)
            .add(
                Blocks.COMMAND_BLOCK,
                Blocks.REPEATING_COMMAND_BLOCK,
                Blocks.CHAIN_COMMAND_BLOCK
            );
        this.tag(BlockTags.FURNACES, ItemTags.FURNACES)
            .add(
                Blocks.FURNACE,
                Blocks.SMOKER,
                Blocks.BLAST_FURNACE
            );
        this.tag(BlockTags.ORES, ItemTags.ORES)
            .addTag(net.minecraft.tags.BlockTags.COAL_ORES)
            .addTag(net.minecraft.tags.BlockTags.COPPER_ORES)
            .addTag(net.minecraft.tags.BlockTags.IRON_ORES)
            .addTag(net.minecraft.tags.BlockTags.GOLD_ORES)
            .addTag(net.minecraft.tags.BlockTags.LAPIS_ORES)
            .addTag(net.minecraft.tags.BlockTags.REDSTONE_ORES)
            .addTag(net.minecraft.tags.BlockTags.DIAMOND_ORES)
            .addTag(net.minecraft.tags.BlockTags.EMERALD_ORES)
            .add(
                Blocks.ANCIENT_DEBRIS,
                Blocks.NETHER_QUARTZ_ORE
            );
        this.tag(BlockTags.DEEPSLATE_ORES, ItemTags.DEEPSLATE_ORES)
            .add(
                Blocks.DEEPSLATE_DIAMOND_ORE,
                Blocks.DEEPSLATE_GOLD_ORE,
                Blocks.DEEPSLATE_COPPER_ORE,
                Blocks.DEEPSLATE_COAL_ORE,
                Blocks.DEEPSLATE_EMERALD_ORE,
                Blocks.DEEPSLATE_REDSTONE_ORE,
                Blocks.DEEPSLATE_IRON_ORE,
                Blocks.DEEPSLATE_LAPIS_ORE
            );
        this.tag(BlockTags.RAW_ORE_BLOCKS, ItemTags.RAW_ORE_BLOCKS)
            .add(
                Blocks.RAW_IRON_BLOCK,
                Blocks.RAW_GOLD_BLOCK,
                Blocks.RAW_COPPER_BLOCK
            );
        this.tag(BlockTags.OXIDIZED_COPPER_BLOCKS, ItemTags.OXIDIZED_COPPER_BLOCKS)
            .add(
                Blocks.WAXED_OXIDIZED_COPPER_CHEST,
                Blocks.OXIDIZED_LIGHTNING_ROD,
                Blocks.WAXED_OXIDIZED_COPPER_GRATE,
                Blocks.WAXED_OXIDIZED_CUT_COPPER_SLAB,
                Blocks.OXIDIZED_CUT_COPPER,
                Blocks.OXIDIZED_COPPER_CHEST,
                Blocks.WAXED_OXIDIZED_COPPER_TRAPDOOR,
                Blocks.WAXED_OXIDIZED_CUT_COPPER_STAIRS,
                Blocks.OXIDIZED_COPPER_BULB,
                Blocks.OXIDIZED_CHISELED_COPPER,
                Blocks.OXIDIZED_COPPER_GRATE,
                Blocks.OXIDIZED_COPPER_GOLEM_STATUE,
                Blocks.OXIDIZED_CUT_COPPER_SLAB,
                Blocks.WAXED_OXIDIZED_COPPER_DOOR,
                Blocks.WAXED_OXIDIZED_CUT_COPPER,
                Blocks.WAXED_OXIDIZED_COPPER_BULB,
                Blocks.WAXED_OXIDIZED_COPPER_GOLEM_STATUE,
                Blocks.OXIDIZED_COPPER,
                Blocks.WAXED_OXIDIZED_COPPER,
                Blocks.WAXED_OXIDIZED_CHISELED_COPPER,
                Blocks.OXIDIZED_CUT_COPPER_STAIRS,
                Blocks.OXIDIZED_COPPER_DOOR,
                Blocks.WAXED_OXIDIZED_LIGHTNING_ROD,
                Blocks.OXIDIZED_COPPER_TRAPDOOR
            )
            .addAll(oxidized(Blocks.COPPER_BARS))
            .addAll(oxidized(Blocks.COPPER_LANTERN))
            .addAll(oxidized(Blocks.COPPER_CHAIN));
        this.tag(BlockTags.WEATHERED_COPPER_BLOCKS, ItemTags.WEATHERED_COPPER_BLOCKS)
            .add(
                Blocks.WEATHERED_CHISELED_COPPER,
                Blocks.WAXED_WEATHERED_LIGHTNING_ROD,
                Blocks.WEATHERED_CUT_COPPER,
                Blocks.WAXED_WEATHERED_COPPER_BULB,
                Blocks.WEATHERED_COPPER_TRAPDOOR,
                Blocks.WEATHERED_COPPER_GOLEM_STATUE,
                Blocks.WEATHERED_COPPER,
                Blocks.WEATHERED_LIGHTNING_ROD,
                Blocks.WAXED_WEATHERED_CUT_COPPER_SLAB,
                Blocks.WEATHERED_CUT_COPPER_SLAB,
                Blocks.WAXED_WEATHERED_CUT_COPPER_STAIRS,
                Blocks.WAXED_WEATHERED_CHISELED_COPPER,
                Blocks.WAXED_WEATHERED_CUT_COPPER,
                Blocks.WEATHERED_COPPER_CHEST,
                Blocks.WEATHERED_COPPER_DOOR,
                Blocks.WEATHERED_COPPER_BULB,
                Blocks.WAXED_WEATHERED_COPPER_GOLEM_STATUE,
                Blocks.WAXED_WEATHERED_COPPER_TRAPDOOR,
                Blocks.WAXED_WEATHERED_COPPER,
                Blocks.WEATHERED_COPPER_GRATE,
                Blocks.WAXED_WEATHERED_COPPER_DOOR,
                Blocks.WEATHERED_CUT_COPPER_STAIRS,
                Blocks.WAXED_WEATHERED_COPPER_CHEST,
                Blocks.WAXED_WEATHERED_COPPER_GRATE
            )
            .addAll(weathered(Blocks.COPPER_CHAIN))
            .addAll(weathered(Blocks.COPPER_LANTERN))
            .addAll(weathered(Blocks.COPPER_BARS));
        this.tag(BlockTags.EXPOSED_COPPER_BLOCKS, ItemTags.EXPOSED_COPPER_BLOCKS)
            .add(
                Blocks.WAXED_EXPOSED_COPPER_BULB,
                Blocks.WAXED_EXPOSED_COPPER_DOOR,
                Blocks.EXPOSED_COPPER_GRATE,
                Blocks.WAXED_EXPOSED_LIGHTNING_ROD,
                Blocks.WAXED_EXPOSED_CHISELED_COPPER,
                Blocks.WAXED_EXPOSED_COPPER,
                Blocks.WAXED_EXPOSED_COPPER_GRATE,
                Blocks.EXPOSED_CHISELED_COPPER,
                Blocks.EXPOSED_CUT_COPPER_SLAB,
                Blocks.WAXED_EXPOSED_CUT_COPPER,
                Blocks.EXPOSED_COPPER_GOLEM_STATUE,
                Blocks.EXPOSED_COPPER_BULB,
                Blocks.WAXED_EXPOSED_COPPER_CHEST,
                Blocks.WAXED_EXPOSED_COPPER_TRAPDOOR,
                Blocks.EXPOSED_COPPER_DOOR,
                Blocks.EXPOSED_LIGHTNING_ROD,
                Blocks.WAXED_EXPOSED_CUT_COPPER_SLAB,
                Blocks.WAXED_EXPOSED_COPPER_GOLEM_STATUE,
                Blocks.EXPOSED_CUT_COPPER,
                Blocks.EXPOSED_CUT_COPPER_STAIRS,
                Blocks.EXPOSED_COPPER,
                Blocks.EXPOSED_COPPER_TRAPDOOR,
                Blocks.EXPOSED_COPPER_CHEST,
                Blocks.WAXED_EXPOSED_CUT_COPPER_STAIRS
            )
            .addAll(exposed(Blocks.COPPER_CHAIN))
            .addAll(exposed(Blocks.COPPER_LANTERN))
            .addAll(exposed(Blocks.COPPER_BARS));
        this.tag(BlockTags.UNAFFECTED_COPPER_BLOCKS, ItemTags.UNAFFECTED_COPPER_BLOCKS)
            .add(
                Blocks.CUT_COPPER,
                Blocks.WAXED_COPPER_GOLEM_STATUE,
                Blocks.COPPER_BULB,
                Blocks.WAXED_CUT_COPPER_SLAB,
                Blocks.WAXED_COPPER_CHEST,
                Blocks.CUT_COPPER_STAIRS,
                Blocks.WAXED_COPPER_BULB,
                Blocks.WAXED_COPPER_BLOCK,
                Blocks.COPPER_DOOR,
                Blocks.CUT_COPPER_SLAB,
                Blocks.WAXED_COPPER_DOOR,
                Blocks.COPPER_TORCH,
                Blocks.COPPER_GOLEM_STATUE,
                Blocks.CHISELED_COPPER,
                Blocks.COPPER_TRAPDOOR,
                Blocks.WAXED_CUT_COPPER_STAIRS,
                Blocks.COPPER_BLOCK,
                Blocks.COPPER_WALL_TORCH,
                Blocks.COPPER_GRATE,
                Blocks.WAXED_CHISELED_COPPER,
                Blocks.WAXED_COPPER_GRATE,
                Blocks.WAXED_COPPER_TRAPDOOR,
                Blocks.COPPER_CHEST,
                Blocks.WAXED_CUT_COPPER
            )
            .addAll(unaffected(Blocks.COPPER_BARS))
            .addAll(unaffected(Blocks.COPPER_CHAIN))
            .addAll(unaffected(Blocks.COPPER_LANTERN));
        this.tag(BlockTags.WAXED_COPPER_BLOCKS, ItemTags.WAXED_COPPER_BLOCKS)
            .add(
                Blocks.WAXED_EXPOSED_COPPER_BULB,
                Blocks.WAXED_EXPOSED_COPPER_DOOR,
                Blocks.WAXED_WEATHERED_COPPER_BULB,
                Blocks.WAXED_WEATHERED_CUT_COPPER_STAIRS,
                Blocks.WAXED_OXIDIZED_COPPER_TRAPDOOR,
                Blocks.WAXED_OXIDIZED_CUT_COPPER_STAIRS,
                Blocks.WAXED_EXPOSED_COPPER_GRATE,
                Blocks.WAXED_EXPOSED_CUT_COPPER,
                Blocks.WAXED_COPPER_BLOCK,
                Blocks.WAXED_WEATHERED_CHISELED_COPPER,
                Blocks.WAXED_WEATHERED_CUT_COPPER,
                Blocks.WAXED_WEATHERED_COPPER_TRAPDOOR,
                Blocks.WAXED_WEATHERED_COPPER,
                Blocks.WAXED_OXIDIZED_CUT_COPPER,
                Blocks.WAXED_OXIDIZED_COPPER_GOLEM_STATUE,
                Blocks.WAXED_CUT_COPPER_STAIRS,
                Blocks.WAXED_WEATHERED_COPPER_DOOR,
                Blocks.WAXED_OXIDIZED_CHISELED_COPPER,
                Blocks.WAXED_CUT_COPPER,
                Blocks.WAXED_EXPOSED_CUT_COPPER_STAIRS,
                Blocks.WAXED_WEATHERED_COPPER_CHEST,
                Blocks.WAXED_OXIDIZED_COPPER_CHEST,
                Blocks.WAXED_OXIDIZED_COPPER_GRATE,
                Blocks.WAXED_COPPER_GOLEM_STATUE,
                Blocks.WAXED_OXIDIZED_CUT_COPPER_SLAB,
                Blocks.WAXED_CUT_COPPER_SLAB,
                Blocks.WAXED_WEATHERED_CUT_COPPER_SLAB,
                Blocks.WAXED_EXPOSED_CHISELED_COPPER,
                Blocks.WAXED_EXPOSED_COPPER,
                Blocks.WAXED_COPPER_CHEST,
                Blocks.WAXED_COPPER_BULB,
                Blocks.WAXED_COPPER_DOOR,
                Blocks.WAXED_WEATHERED_COPPER_GOLEM_STATUE,
                Blocks.WAXED_EXPOSED_COPPER_CHEST,
                Blocks.WAXED_EXPOSED_COPPER_TRAPDOOR,
                Blocks.WAXED_OXIDIZED_COPPER_DOOR,
                Blocks.WAXED_OXIDIZED_COPPER_BULB,
                Blocks.WAXED_OXIDIZED_COPPER,
                Blocks.WAXED_EXPOSED_CUT_COPPER_SLAB,
                Blocks.WAXED_EXPOSED_COPPER_GOLEM_STATUE,
                Blocks.WAXED_CHISELED_COPPER,
                Blocks.WAXED_COPPER_GRATE,
                Blocks.WAXED_COPPER_TRAPDOOR,
                Blocks.WAXED_WEATHERED_COPPER_GRATE
            )
            .addAll(Blocks.COPPER_CHAIN.waxedMapping().values())
            .addAll(Blocks.COPPER_LANTERN.waxedMapping().values())
            .addAll(Blocks.COPPER_BARS.waxedMapping().values());
        this.tag(BlockTags.UNWAXED_COPPER_BLOCKS, ItemTags.UNWAXED_COPPER_BLOCKS)
            .add(
                Blocks.EXPOSED_COPPER_GRATE,
                Blocks.WEATHERED_COPPER_GOLEM_STATUE,
                Blocks.COPPER_BULB,
                Blocks.WEATHERED_CUT_COPPER_SLAB,
                Blocks.EXPOSED_CUT_COPPER_SLAB,
                Blocks.OXIDIZED_COPPER_BULB,
                Blocks.EXPOSED_COPPER_GOLEM_STATUE,
                Blocks.COPPER_DOOR,
                Blocks.WEATHERED_COPPER_CHEST,
                Blocks.CUT_COPPER_SLAB,
                Blocks.EXPOSED_COPPER_BULB,
                Blocks.WEATHERED_COPPER_BULB,
                Blocks.OXIDIZED_COPPER_GRATE,
                Blocks.OXIDIZED_COPPER_GOLEM_STATUE,
                Blocks.COPPER_TORCH,
                Blocks.COPPER_GOLEM_STATUE,
                Blocks.OXIDIZED_CUT_COPPER_SLAB,
                Blocks.EXPOSED_COPPER_DOOR,
                Blocks.WEATHERED_COPPER_GRATE,
                Blocks.COPPER_BLOCK,
                Blocks.COPPER_WALL_TORCH,
                Blocks.EXPOSED_CUT_COPPER,
                Blocks.OXIDIZED_COPPER_DOOR,
                Blocks.EXPOSED_COPPER,
                Blocks.EXPOSED_COPPER_TRAPDOOR,
                Blocks.OXIDIZED_COPPER_TRAPDOOR,
                Blocks.EXPOSED_COPPER_CHEST,
                Blocks.WEATHERED_CHISELED_COPPER,
                Blocks.CUT_COPPER,
                Blocks.OXIDIZED_LIGHTNING_ROD,
                Blocks.WEATHERED_CUT_COPPER,
                Blocks.WEATHERED_COPPER_TRAPDOOR,
                Blocks.WEATHERED_COPPER,
                Blocks.WEATHERED_LIGHTNING_ROD,
                Blocks.OXIDIZED_CUT_COPPER,
                Blocks.OXIDIZED_COPPER_CHEST,
                Blocks.EXPOSED_CHISELED_COPPER,
                Blocks.CUT_COPPER_STAIRS,
                Blocks.OXIDIZED_CHISELED_COPPER,
                Blocks.WEATHERED_COPPER_DOOR,
                Blocks.EXPOSED_LIGHTNING_ROD,
                Blocks.CHISELED_COPPER,
                Blocks.COPPER_TRAPDOOR,
                Blocks.OXIDIZED_COPPER,
                Blocks.COPPER_GRATE,
                Blocks.OXIDIZED_CUT_COPPER_STAIRS,
                Blocks.WEATHERED_CUT_COPPER_STAIRS,
                Blocks.EXPOSED_CUT_COPPER_STAIRS,
                Blocks.COPPER_CHEST
            )
            .addAll(Blocks.COPPER_LANTERN.waxedMapping().keySet())
            .addAll(Blocks.COPPER_CHAIN.waxedMapping().keySet())
            .addAll(Blocks.COPPER_BARS.waxedMapping().keySet());
        this.tag(BlockTags.COPPER_BLOCKS, ItemTags.COPPER_BLOCKS)
            .addTag(BlockTags.WAXED_COPPER_BLOCKS)
            .addTag(BlockTags.UNWAXED_COPPER_BLOCKS);
        this.tag(BlockTags.FULL_COPPER_BLOCKS, ItemTags.FULL_COPPER_BLOCKS)
            .add(
                Blocks.COPPER_BLOCK,
                Blocks.WAXED_WEATHERED_COPPER,
                Blocks.WAXED_OXIDIZED_COPPER,
                Blocks.WEATHERED_COPPER,
                Blocks.WAXED_COPPER_BLOCK,
                Blocks.OXIDIZED_COPPER,
                Blocks.EXPOSED_COPPER,
                Blocks.WAXED_EXPOSED_COPPER
            );
        this.tag(BlockTags.CUT_COPPER_BLOCKS, ItemTags.CUT_COPPER_BLOCKS)
            .add(
                Blocks.CUT_COPPER,
                Blocks.WAXED_CUT_COPPER,
                Blocks.WAXED_WEATHERED_CUT_COPPER,
                Blocks.EXPOSED_CUT_COPPER,
                Blocks.OXIDIZED_CUT_COPPER,
                Blocks.WAXED_OXIDIZED_CUT_COPPER,
                Blocks.WAXED_EXPOSED_CUT_COPPER,
                Blocks.WEATHERED_CUT_COPPER
            );
        this.tag(BlockTags.CUT_COPPER_STAIRS, ItemTags.CUT_COPPER_STAIRS)
            .add(
                Blocks.OXIDIZED_CUT_COPPER_STAIRS,
                Blocks.WAXED_WEATHERED_CUT_COPPER_STAIRS,
                Blocks.WEATHERED_CUT_COPPER_STAIRS,
                Blocks.WAXED_CUT_COPPER_STAIRS,
                Blocks.CUT_COPPER_STAIRS,
                Blocks.WAXED_OXIDIZED_CUT_COPPER_STAIRS,
                Blocks.WAXED_EXPOSED_CUT_COPPER_STAIRS,
                Blocks.EXPOSED_CUT_COPPER_STAIRS
            );
        this.tag(BlockTags.CUT_COPPER_SLABS, ItemTags.CUT_COPPER_SLABS)
            .add(
                Blocks.WAXED_EXPOSED_CUT_COPPER_SLAB,
                Blocks.CUT_COPPER_SLAB,
                Blocks.WAXED_WEATHERED_CUT_COPPER_SLAB,
                Blocks.WAXED_CUT_COPPER_SLAB,
                Blocks.OXIDIZED_CUT_COPPER_SLAB,
                Blocks.EXPOSED_CUT_COPPER_SLAB,
                Blocks.WAXED_OXIDIZED_CUT_COPPER_SLAB,
                Blocks.WEATHERED_CUT_COPPER_SLAB
            );
    }

    private static ImmutableList<Block> unaffected(WeatheringCopperBlocks blocks) {
        return copperVariants(blocks, WeatheringCopperBlocks::unaffected, WeatheringCopperBlocks::waxed);
    }

    private static ImmutableList<Block> exposed(WeatheringCopperBlocks blocks) {
        return copperVariants(blocks, WeatheringCopperBlocks::exposed, WeatheringCopperBlocks::waxedExposed);
    }

    private static ImmutableList<Block> weathered(WeatheringCopperBlocks blocks) {
        return copperVariants(blocks, WeatheringCopperBlocks::weathered, WeatheringCopperBlocks::waxedWeathered);
    }

    private static ImmutableList<Block> oxidized(WeatheringCopperBlocks blocks) {
        return copperVariants(blocks, WeatheringCopperBlocks::oxidized, WeatheringCopperBlocks::waxedOxidized);
    }

    private static ImmutableList<Block> copperVariants(WeatheringCopperBlocks blocks, Function<WeatheringCopperBlocks, Block> nonWaxed, Function<WeatheringCopperBlocks, Block> waxed) {
        return ImmutableList.of(
            nonWaxed.apply(blocks),
            waxed.apply(blocks)
        );
    }

    protected abstract TagAppender<Block, Block> tag(TagKey<Block> blockTag, TagKey<Item> itemTag);
}
