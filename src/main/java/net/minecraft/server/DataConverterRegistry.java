package net.minecraft.server;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

public class DataConverterRegistry {

    private static final BiFunction<Integer, Schema, Schema> a = Schema::new;
    private static final BiFunction<Integer, Schema, Schema> b = DataConverterSchemaNamed::new;
    private static final DataFixer c = b();

    private static DataFixer b() {
        DataFixerBuilder datafixerbuilder = new DataFixerBuilder(SharedConstants.getGameVersion().getWorldVersion());

        a(datafixerbuilder);
        return datafixerbuilder.build(SystemUtils.e());
    }

    public static DataFixer a() {
        return DataConverterRegistry.c;
    }

    private static void a(DataFixerBuilder datafixerbuilder) {
        Schema schema = datafixerbuilder.addSchema(99, DataConverterSchemaV99::new);
        Schema schema1 = datafixerbuilder.addSchema(100, DataConverterSchemaV100::new);

        datafixerbuilder.addFixer(new DataConverterEquipment(schema1, true));
        Schema schema2 = datafixerbuilder.addSchema(101, DataConverterRegistry.a);

        datafixerbuilder.addFixer(new DataConverterSignText(schema2, false));
        Schema schema3 = datafixerbuilder.addSchema(102, DataConverterSchemaV102::new);

        datafixerbuilder.addFixer(new DataConverterMaterialId(schema3, true));
        datafixerbuilder.addFixer(new DataConverterPotionId(schema3, false));
        Schema schema4 = datafixerbuilder.addSchema(105, DataConverterRegistry.a);

        datafixerbuilder.addFixer(new DataConverterSpawnEgg(schema4, true));
        Schema schema5 = datafixerbuilder.addSchema(106, DataConverterSchemaV106::new);

        datafixerbuilder.addFixer(new DataConverterMobSpawner(schema5, true));
        Schema schema6 = datafixerbuilder.addSchema(107, DataConverterSchemaV107::new);

        datafixerbuilder.addFixer(new DataConverterMinecart(schema6, true));
        Schema schema7 = datafixerbuilder.addSchema(108, DataConverterRegistry.a);

        datafixerbuilder.addFixer(new DataConverterUUID(schema7, true));
        Schema schema8 = datafixerbuilder.addSchema(109, DataConverterRegistry.a);

        datafixerbuilder.addFixer(new DataConverterHealth(schema8, true));
        Schema schema9 = datafixerbuilder.addSchema(110, DataConverterRegistry.a);

        datafixerbuilder.addFixer(new DataConverterSaddle(schema9, true));
        Schema schema10 = datafixerbuilder.addSchema(111, DataConverterRegistry.a);

        datafixerbuilder.addFixer(new DataConverterHanging(schema10, true));
        Schema schema11 = datafixerbuilder.addSchema(113, DataConverterRegistry.a);

        datafixerbuilder.addFixer(new DataConverterDropChances(schema11, true));
        Schema schema12 = datafixerbuilder.addSchema(135, DataConverterSchemaV135::new);

        datafixerbuilder.addFixer(new DataConverterRiding(schema12, true));
        Schema schema13 = datafixerbuilder.addSchema(143, DataConverterSchemaV143::new);

        datafixerbuilder.addFixer(new DataConverterEntityTippedArrow(schema13, true));
        Schema schema14 = datafixerbuilder.addSchema(147, DataConverterRegistry.a);

        datafixerbuilder.addFixer(new DataConverterArmorStand(schema14, true));
        Schema schema15 = datafixerbuilder.addSchema(165, DataConverterRegistry.a);

        datafixerbuilder.addFixer(new DataConverterBook(schema15, true));
        Schema schema16 = datafixerbuilder.addSchema(501, DataConverterSchemaV501::new);

        datafixerbuilder.addFixer(new DataConverterAddChoices(schema16, "Add 1.10 entities fix", DataConverterTypes.ENTITY));
        Schema schema17 = datafixerbuilder.addSchema(502, DataConverterRegistry.a);

        datafixerbuilder.addFixer(DataConverterItemName.a(schema17, "cooked_fished item renamer", (s) -> {
            return Objects.equals(DataConverterSchemaNamed.a(s), "minecraft:cooked_fished") ? "minecraft:cooked_fish" : s;
        }));
        datafixerbuilder.addFixer(new DataConverterZombie(schema17, false));
        Schema schema18 = datafixerbuilder.addSchema(505, DataConverterRegistry.a);

        datafixerbuilder.addFixer(new DataConverterVBO(schema18, false));
        Schema schema19 = datafixerbuilder.addSchema(700, DataConverterSchemaV700::new);

        datafixerbuilder.addFixer(new DataConverterGuardian(schema19, true));
        Schema schema20 = datafixerbuilder.addSchema(701, DataConverterSchemaV701::new);

        datafixerbuilder.addFixer(new DataConverterSkeleton(schema20, true));
        Schema schema21 = datafixerbuilder.addSchema(702, DataConverterSchemaV702::new);

        datafixerbuilder.addFixer(new DataConverterZombieType(schema21, true));
        Schema schema22 = datafixerbuilder.addSchema(703, DataConverterSchemaV703::new);

        datafixerbuilder.addFixer(new DataConverterHorse(schema22, true));
        Schema schema23 = datafixerbuilder.addSchema(704, DataConverterSchemaV704::new);

        datafixerbuilder.addFixer(new DataConverterTileEntity(schema23, true));
        Schema schema24 = datafixerbuilder.addSchema(705, DataConverterSchemaV705::new);

        datafixerbuilder.addFixer(new DataConverterEntity(schema24, true));
        Schema schema25 = datafixerbuilder.addSchema(804, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterBanner(schema25, true));
        Schema schema26 = datafixerbuilder.addSchema(806, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterPotionWater(schema26, false));
        Schema schema27 = datafixerbuilder.addSchema(808, DataConverterSchemaV808::new);

        datafixerbuilder.addFixer(new DataConverterAddChoices(schema27, "added shulker box", DataConverterTypes.BLOCK_ENTITY));
        Schema schema28 = datafixerbuilder.addSchema(808, 1, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterShulker(schema28, false));
        Schema schema29 = datafixerbuilder.addSchema(813, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterShulkerBoxItem(schema29, false));
        datafixerbuilder.addFixer(new DataConverterShulkerBoxBlock(schema29, false));
        Schema schema30 = datafixerbuilder.addSchema(816, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterLang(schema30, false));
        Schema schema31 = datafixerbuilder.addSchema(820, DataConverterRegistry.b);

        datafixerbuilder.addFixer(DataConverterItemName.a(schema31, "totem item renamer", a("minecraft:totem", "minecraft:totem_of_undying")));
        Schema schema32 = datafixerbuilder.addSchema(1022, DataConverterSchemaV1022::new);

        datafixerbuilder.addFixer(new DataConverterShoulderEntity(schema32, "added shoulder entities to players", DataConverterTypes.PLAYER));
        Schema schema33 = datafixerbuilder.addSchema(1125, DataConverterSchemaV1125::new);

        datafixerbuilder.addFixer(new DataConverterBedBlock(schema33, true));
        datafixerbuilder.addFixer(new DataConverterBedItem(schema33, false));
        Schema schema34 = datafixerbuilder.addSchema(1344, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterKeybind(schema34, false));
        Schema schema35 = datafixerbuilder.addSchema(1446, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterKeybind2(schema35, false));
        Schema schema36 = datafixerbuilder.addSchema(1450, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterFlattenState(schema36, false));
        Schema schema37 = datafixerbuilder.addSchema(1451, DataConverterSchemaV1451::new);

        datafixerbuilder.addFixer(new DataConverterAddChoices(schema37, "AddTrappedChestFix", DataConverterTypes.BLOCK_ENTITY));
        Schema schema38 = datafixerbuilder.addSchema(1451, 1, DataConverterSchemaV1451_1::new);

        datafixerbuilder.addFixer(new ChunkConverterPalette(schema38, true));
        Schema schema39 = datafixerbuilder.addSchema(1451, 2, DataConverterSchemaV1451_2::new);

        datafixerbuilder.addFixer(new DataConverterPiston(schema39, true));
        Schema schema40 = datafixerbuilder.addSchema(1451, 3, DataConverterSchemaV1451_3::new);

        datafixerbuilder.addFixer(new DataConverterEntityBlockState(schema40, true));
        datafixerbuilder.addFixer(new DataConverterMap(schema40, false));
        Schema schema41 = datafixerbuilder.addSchema(1451, 4, DataConverterSchemaV1451_4::new);

        datafixerbuilder.addFixer(new DataConverterBlockName(schema41, true));
        datafixerbuilder.addFixer(new DataConverterFlatten(schema41, false));
        Schema schema42 = datafixerbuilder.addSchema(1451, 5, DataConverterSchemaV1451_5::new);

        datafixerbuilder.addFixer(new DataConverterAddChoices(schema42, "RemoveNoteBlockFlowerPotFix", DataConverterTypes.BLOCK_ENTITY));
        datafixerbuilder.addFixer(new DataConverterFlattenSpawnEgg(schema42, false));
        datafixerbuilder.addFixer(new DataConverterWolf(schema42, false));
        datafixerbuilder.addFixer(new DataConverterBannerColour(schema42, false));
        datafixerbuilder.addFixer(new DataConverterWorldGenSettings(schema42, false));
        Schema schema43 = datafixerbuilder.addSchema(1451, 6, DataConverterSchemaV1451_6::new);

        datafixerbuilder.addFixer(new DataConverterStatistic(schema43, true));
        datafixerbuilder.addFixer(new DataConverterJukeBox(schema43, false));
        Schema schema44 = datafixerbuilder.addSchema(1451, 7, DataConverterSchemaV1451_7::new);

        datafixerbuilder.addFixer(new DataConverterVillage(schema44, true));
        Schema schema45 = datafixerbuilder.addSchema(1451, 7, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterVillagerTrade(schema45, false));
        Schema schema46 = datafixerbuilder.addSchema(1456, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterItemFrame(schema46, false));
        Schema schema47 = datafixerbuilder.addSchema(1458, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterCustomNameEntity(schema47, false));
        datafixerbuilder.addFixer(new DataConverterCustomNameItem(schema47, false));
        datafixerbuilder.addFixer(new DataConverterCustomNameTile(schema47, false));
        Schema schema48 = datafixerbuilder.addSchema(1460, DataConverterSchemaV1460::new);

        datafixerbuilder.addFixer(new DataConverterPainting(schema48, false));
        Schema schema49 = datafixerbuilder.addSchema(1466, DataConverterSchemaV1466::new);

        datafixerbuilder.addFixer(new DataConverterProtoChunk(schema49, true));
        Schema schema50 = datafixerbuilder.addSchema(1470, DataConverterSchemaV1470::new);

        datafixerbuilder.addFixer(new DataConverterAddChoices(schema50, "Add 1.13 entities fix", DataConverterTypes.ENTITY));
        Schema schema51 = datafixerbuilder.addSchema(1474, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterColorlessShulkerEntity(schema51, false));
        datafixerbuilder.addFixer(DataConverterBlockRename.a(schema51, "Colorless shulker block fixer", (s) -> {
            return Objects.equals(DataConverterSchemaNamed.a(s), "minecraft:purple_shulker_box") ? "minecraft:shulker_box" : s;
        }));
        datafixerbuilder.addFixer(DataConverterItemName.a(schema51, "Colorless shulker item fixer", (s) -> {
            return Objects.equals(DataConverterSchemaNamed.a(s), "minecraft:purple_shulker_box") ? "minecraft:shulker_box" : s;
        }));
        Schema schema52 = datafixerbuilder.addSchema(1475, DataConverterRegistry.b);

        datafixerbuilder.addFixer(DataConverterBlockRename.a(schema52, "Flowing fixer", a((Map) ImmutableMap.of("minecraft:flowing_water", "minecraft:water", "minecraft:flowing_lava", "minecraft:lava"))));
        Schema schema53 = datafixerbuilder.addSchema(1480, DataConverterRegistry.b);

        datafixerbuilder.addFixer(DataConverterBlockRename.a(schema53, "Rename coral blocks", a(DataConverterCoral.a)));
        datafixerbuilder.addFixer(DataConverterItemName.a(schema53, "Rename coral items", a(DataConverterCoral.a)));
        Schema schema54 = datafixerbuilder.addSchema(1481, DataConverterSchemaV1481::new);

        datafixerbuilder.addFixer(new DataConverterAddChoices(schema54, "Add conduit", DataConverterTypes.BLOCK_ENTITY));
        Schema schema55 = datafixerbuilder.addSchema(1483, DataConverterSchemaV1483::new);

        datafixerbuilder.addFixer(new DataConverterEntityPufferfish(schema55, true));
        datafixerbuilder.addFixer(DataConverterItemName.a(schema55, "Rename pufferfish egg item", a(DataConverterEntityPufferfish.a)));
        Schema schema56 = datafixerbuilder.addSchema(1484, DataConverterRegistry.b);

        datafixerbuilder.addFixer(DataConverterItemName.a(schema56, "Rename seagrass items", a((Map) ImmutableMap.of("minecraft:sea_grass", "minecraft:seagrass", "minecraft:tall_sea_grass", "minecraft:tall_seagrass"))));
        datafixerbuilder.addFixer(DataConverterBlockRename.a(schema56, "Rename seagrass blocks", a((Map) ImmutableMap.of("minecraft:sea_grass", "minecraft:seagrass", "minecraft:tall_sea_grass", "minecraft:tall_seagrass"))));
        datafixerbuilder.addFixer(new DataConverterHeightmapRenaming(schema56, false));
        Schema schema57 = datafixerbuilder.addSchema(1486, DataConverterSchemaV1486::new);

        datafixerbuilder.addFixer(new DataConverterEntityCodSalmon(schema57, true));
        datafixerbuilder.addFixer(DataConverterItemName.a(schema57, "Rename cod/salmon egg items", a(DataConverterEntityCodSalmon.b)));
        Schema schema58 = datafixerbuilder.addSchema(1487, DataConverterRegistry.b);

        datafixerbuilder.addFixer(DataConverterItemName.a(schema58, "Rename prismarine_brick(s)_* blocks", a((Map) ImmutableMap.of("minecraft:prismarine_bricks_slab", "minecraft:prismarine_brick_slab", "minecraft:prismarine_bricks_stairs", "minecraft:prismarine_brick_stairs"))));
        datafixerbuilder.addFixer(DataConverterBlockRename.a(schema58, "Rename prismarine_brick(s)_* items", a((Map) ImmutableMap.of("minecraft:prismarine_bricks_slab", "minecraft:prismarine_brick_slab", "minecraft:prismarine_bricks_stairs", "minecraft:prismarine_brick_stairs"))));
        Schema schema59 = datafixerbuilder.addSchema(1488, DataConverterRegistry.b);

        datafixerbuilder.addFixer(DataConverterBlockRename.a(schema59, "Rename kelp/kelptop", a((Map) ImmutableMap.of("minecraft:kelp_top", "minecraft:kelp", "minecraft:kelp", "minecraft:kelp_plant"))));
        datafixerbuilder.addFixer(DataConverterItemName.a(schema59, "Rename kelptop", a("minecraft:kelp_top", "minecraft:kelp")));
        datafixerbuilder.addFixer(new DataConverterNamedEntity(schema59, false, "Command block block entity custom name fix", DataConverterTypes.BLOCK_ENTITY, "minecraft:command_block") {
            @Override
            protected Typed<?> a(Typed<?> typed) {
                return typed.update(DSL.remainderFinder(), DataConverterCustomNameEntity::a);
            }
        });
        datafixerbuilder.addFixer(new DataConverterNamedEntity(schema59, false, "Command block minecart custom name fix", DataConverterTypes.ENTITY, "minecraft:commandblock_minecart") {
            @Override
            protected Typed<?> a(Typed<?> typed) {
                return typed.update(DSL.remainderFinder(), DataConverterCustomNameEntity::a);
            }
        });
        datafixerbuilder.addFixer(new DataConverterIglooMetadataRemoval(schema59, false));
        Schema schema60 = datafixerbuilder.addSchema(1490, DataConverterRegistry.b);

        datafixerbuilder.addFixer(DataConverterBlockRename.a(schema60, "Rename melon_block", a("minecraft:melon_block", "minecraft:melon")));
        datafixerbuilder.addFixer(DataConverterItemName.a(schema60, "Rename melon_block/melon/speckled_melon", a((Map) ImmutableMap.of("minecraft:melon_block", "minecraft:melon", "minecraft:melon", "minecraft:melon_slice", "minecraft:speckled_melon", "minecraft:glistering_melon_slice"))));
        Schema schema61 = datafixerbuilder.addSchema(1492, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterChunkStructuresTemplateRename(schema61, false));
        Schema schema62 = datafixerbuilder.addSchema(1494, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterItemStackEnchantment(schema62, false));
        Schema schema63 = datafixerbuilder.addSchema(1496, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterLeaves(schema63, false));
        Schema schema64 = datafixerbuilder.addSchema(1500, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterBlockEntityKeepPacked(schema64, false));
        Schema schema65 = datafixerbuilder.addSchema(1501, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterAdvancement(schema65, false));
        Schema schema66 = datafixerbuilder.addSchema(1502, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterRecipes(schema66, false));
        Schema schema67 = datafixerbuilder.addSchema(1506, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterLevelDataGeneratorOptions(schema67, false));
        Schema schema68 = datafixerbuilder.addSchema(1510, DataConverterSchemaV1510::new);

        datafixerbuilder.addFixer(DataConverterBlockRename.a(schema68, "Block renamening fix", a(DataConverterEntityRename.b)));
        datafixerbuilder.addFixer(DataConverterItemName.a(schema68, "Item renamening fix", a(DataConverterEntityRename.c)));
        datafixerbuilder.addFixer(new DataConverterRecipeRename(schema68, false));
        datafixerbuilder.addFixer(new DataConverterEntityRename(schema68, true));
        datafixerbuilder.addFixer(new DataConverterSwimStats(schema68, false));
        Schema schema69 = datafixerbuilder.addSchema(1514, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterObjectiveDisplayName(schema69, false));
        datafixerbuilder.addFixer(new DataConverterTeamDisplayName(schema69, false));
        datafixerbuilder.addFixer(new DataConverterObjectiveRenderType(schema69, false));
        Schema schema70 = datafixerbuilder.addSchema(1515, DataConverterRegistry.b);

        datafixerbuilder.addFixer(DataConverterBlockRename.a(schema70, "Rename coral fan blocks", a(DataConverterCoralFan.a)));
        Schema schema71 = datafixerbuilder.addSchema(1624, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterTrappedChest(schema71, false));
        Schema schema72 = datafixerbuilder.addSchema(1800, DataConverterSchemaV1800::new);

        datafixerbuilder.addFixer(new DataConverterAddChoices(schema72, "Added 1.14 mobs fix", DataConverterTypes.ENTITY));
        datafixerbuilder.addFixer(DataConverterItemName.a(schema72, "Rename dye items", a(DataConverterDye.a)));
        Schema schema73 = datafixerbuilder.addSchema(1801, DataConverterSchemaV1801::new);

        datafixerbuilder.addFixer(new DataConverterAddChoices(schema73, "Added Illager Beast", DataConverterTypes.ENTITY));
        Schema schema74 = datafixerbuilder.addSchema(1802, DataConverterRegistry.b);

        datafixerbuilder.addFixer(DataConverterBlockRename.a(schema74, "Rename sign blocks & stone slabs", a((Map) ImmutableMap.of("minecraft:stone_slab", "minecraft:smooth_stone_slab", "minecraft:sign", "minecraft:oak_sign", "minecraft:wall_sign", "minecraft:oak_wall_sign"))));
        datafixerbuilder.addFixer(DataConverterItemName.a(schema74, "Rename sign item & stone slabs", a((Map) ImmutableMap.of("minecraft:stone_slab", "minecraft:smooth_stone_slab", "minecraft:sign", "minecraft:oak_sign"))));
        Schema schema75 = datafixerbuilder.addSchema(1803, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterItemLoreComponentize(schema75, false));
        Schema schema76 = datafixerbuilder.addSchema(1904, DataConverterSchemaV1904::new);

        datafixerbuilder.addFixer(new DataConverterAddChoices(schema76, "Added Cats", DataConverterTypes.ENTITY));
        datafixerbuilder.addFixer(new DataConverterEntityCatSplit(schema76, false));
        Schema schema77 = datafixerbuilder.addSchema(1905, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterChunkStatus(schema77, false));
        Schema schema78 = datafixerbuilder.addSchema(1906, DataConverterSchemaV1906::new);

        datafixerbuilder.addFixer(new DataConverterAddChoices(schema78, "Add POI Blocks", DataConverterTypes.BLOCK_ENTITY));
        Schema schema79 = datafixerbuilder.addSchema(1909, DataConverterSchemaV1909::new);

        datafixerbuilder.addFixer(new DataConverterAddChoices(schema79, "Add jigsaw", DataConverterTypes.BLOCK_ENTITY));
        Schema schema80 = datafixerbuilder.addSchema(1911, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterChunkStatus2(schema80, false));
        Schema schema81 = datafixerbuilder.addSchema(1917, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterCatType(schema81, false));
        Schema schema82 = datafixerbuilder.addSchema(1918, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterVillagerProfession(schema82, "minecraft:villager"));
        datafixerbuilder.addFixer(new DataConverterVillagerProfession(schema82, "minecraft:zombie_villager"));
        Schema schema83 = datafixerbuilder.addSchema(1920, DataConverterSchemaV1920::new);

        datafixerbuilder.addFixer(new DataConverterNewVillage(schema83, false));
        datafixerbuilder.addFixer(new DataConverterAddChoices(schema83, "Add campfire", DataConverterTypes.BLOCK_ENTITY));
        Schema schema84 = datafixerbuilder.addSchema(1925, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterMapId(schema84, false));
        Schema schema85 = datafixerbuilder.addSchema(1928, DataConverterSchemaV1928::new);

        datafixerbuilder.addFixer(new DataConverterEntityRavagerRename(schema85, true));
        datafixerbuilder.addFixer(DataConverterItemName.a(schema85, "Rename ravager egg item", a(DataConverterEntityRavagerRename.a)));
        Schema schema86 = datafixerbuilder.addSchema(1929, DataConverterSchemaV1929::new);

        datafixerbuilder.addFixer(new DataConverterAddChoices(schema86, "Add Wandering Trader and Trader Llama", DataConverterTypes.ENTITY));
        Schema schema87 = datafixerbuilder.addSchema(1931, DataConverterSchemaV1931::new);

        datafixerbuilder.addFixer(new DataConverterAddChoices(schema87, "Added Fox", DataConverterTypes.ENTITY));
        Schema schema88 = datafixerbuilder.addSchema(1936, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterOptionsAddTextBackground(schema88, false));
        Schema schema89 = datafixerbuilder.addSchema(1946, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterPOI(schema89, false));
        Schema schema90 = datafixerbuilder.addSchema(1948, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterOminousBannerRename(schema90, false));
        Schema schema91 = datafixerbuilder.addSchema(1953, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterOminousBannerBlockEntityRename(schema91, false));
        Schema schema92 = datafixerbuilder.addSchema(1955, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterVillagerLevelXp(schema92, false));
        datafixerbuilder.addFixer(new DataConverterZombieVillagerLevelXp(schema92, false));
        Schema schema93 = datafixerbuilder.addSchema(1961, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterChunkLightRemove(schema93, false));
        Schema schema94 = datafixerbuilder.addSchema(1963, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterRemoveGolemGossip(schema94, false));
        Schema schema95 = datafixerbuilder.addSchema(2100, DataConverterSchemaV2100::new);

        datafixerbuilder.addFixer(new DataConverterAddChoices(schema95, "Added Bee and Bee Stinger", DataConverterTypes.ENTITY));
        datafixerbuilder.addFixer(new DataConverterAddChoices(schema95, "Add beehive", DataConverterTypes.BLOCK_ENTITY));
        datafixerbuilder.addFixer(new DataConverterRecipeBase(schema95, false, "Rename sugar recipe", a("minecraft:sugar", "sugar_from_sugar_cane")));
        datafixerbuilder.addFixer(new DataConverterAdvancementBase(schema95, false, "Rename sugar recipe advancement", a("minecraft:recipes/misc/sugar", "minecraft:recipes/misc/sugar_from_sugar_cane")));
        Schema schema96 = datafixerbuilder.addSchema(2202, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterLeavesBiome(schema96, false));
        Schema schema97 = datafixerbuilder.addSchema(2209, DataConverterRegistry.b);

        datafixerbuilder.addFixer(DataConverterItemName.a(schema97, "Rename bee_hive item to beehive", a("minecraft:bee_hive", "minecraft:beehive")));
        datafixerbuilder.addFixer(new DataConverterBeehive(schema97));
        datafixerbuilder.addFixer(DataConverterBlockRename.a(schema97, "Rename bee_hive block to beehive", a("minecraft:bee_hive", "minecraft:beehive")));
        Schema schema98 = datafixerbuilder.addSchema(2211, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterStructureReference(schema98, false));
        Schema schema99 = datafixerbuilder.addSchema(2218, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterPOIRebuild(schema99, false));
        Schema schema100 = datafixerbuilder.addSchema(2501, DataConverterSchemaV2501::new);

        datafixerbuilder.addFixer(new DataConverterFurnaceRecipesUsed(schema100, true));
        Schema schema101 = datafixerbuilder.addSchema(2502, DataConverterSchemaV2502::new);

        datafixerbuilder.addFixer(new DataConverterAddChoices(schema101, "Added Hoglin", DataConverterTypes.ENTITY));
        Schema schema102 = datafixerbuilder.addSchema(2503, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterWallProperty(schema102, false));
        datafixerbuilder.addFixer(new DataConverterAdvancementBase(schema102, false, "Composter category change", a("minecraft:recipes/misc/composter", "minecraft:recipes/decorations/composter")));
        Schema schema103 = datafixerbuilder.addSchema(2505, DataConverterSchemaV2505::new);

        datafixerbuilder.addFixer(new DataConverterAddChoices(schema103, "Added Piglin", DataConverterTypes.ENTITY));
        datafixerbuilder.addFixer(new DataConverterMemoryExpiry(schema103, "minecraft:villager"));
        Schema schema104 = datafixerbuilder.addSchema(2508, DataConverterRegistry.b);

        datafixerbuilder.addFixer(DataConverterItemName.a(schema104, "Renamed fungi items to fungus", a((Map) ImmutableMap.of("minecraft:warped_fungi", "minecraft:warped_fungus", "minecraft:crimson_fungi", "minecraft:crimson_fungus"))));
        datafixerbuilder.addFixer(DataConverterBlockRename.a(schema104, "Renamed fungi blocks to fungus", a((Map) ImmutableMap.of("minecraft:warped_fungi", "minecraft:warped_fungus", "minecraft:crimson_fungi", "minecraft:crimson_fungus"))));
        Schema schema105 = datafixerbuilder.addSchema(2509, DataConverterSchemaV2509::new);

        datafixerbuilder.addFixer(new DataConverterEntityZombifiedPiglinRename(schema105));
        datafixerbuilder.addFixer(DataConverterItemName.a(schema105, "Rename zombie pigman egg item", a(DataConverterEntityZombifiedPiglinRename.a)));
        Schema schema106 = datafixerbuilder.addSchema(2511, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterEntityProjectileOwner(schema106));
        Schema schema107 = datafixerbuilder.addSchema(2514, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterEntityUUID(schema107));
        datafixerbuilder.addFixer(new DataConverterBlockEntityUUID(schema107));
        datafixerbuilder.addFixer(new DataConverterPlayerUUID(schema107));
        datafixerbuilder.addFixer(new DataConverterMiscUUID(schema107));
        datafixerbuilder.addFixer(new DataConverterSavedDataUUID(schema107));
        datafixerbuilder.addFixer(new DataConverterItemStackUUID(schema107));
        Schema schema108 = datafixerbuilder.addSchema(2516, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterGossip(schema108, "minecraft:villager"));
        datafixerbuilder.addFixer(new DataConverterGossip(schema108, "minecraft:zombie_villager"));
        Schema schema109 = datafixerbuilder.addSchema(2518, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterJigsawProperties(schema109, false));
        datafixerbuilder.addFixer(new DataConverterJigsawRotation(schema109, false));
        Schema schema110 = datafixerbuilder.addSchema(2519, DataConverterSchemaV2519::new);

        datafixerbuilder.addFixer(new DataConverterAddChoices(schema110, "Added Strider", DataConverterTypes.ENTITY));
        Schema schema111 = datafixerbuilder.addSchema(2522, DataConverterSchemaV2522::new);

        datafixerbuilder.addFixer(new DataConverterAddChoices(schema111, "Added Zoglin", DataConverterTypes.ENTITY));
        Schema schema112 = datafixerbuilder.addSchema(2523, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterAttributes(schema112));
        Schema schema113 = datafixerbuilder.addSchema(2527, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterBitStorageAlign(schema113));
        Schema schema114 = datafixerbuilder.addSchema(2528, DataConverterRegistry.b);

        datafixerbuilder.addFixer(DataConverterItemName.a(schema114, "Rename soul fire torch and soul fire lantern", a((Map) ImmutableMap.of("minecraft:soul_fire_torch", "minecraft:soul_torch", "minecraft:soul_fire_lantern", "minecraft:soul_lantern"))));
        datafixerbuilder.addFixer(DataConverterBlockRename.a(schema114, "Rename soul fire torch and soul fire lantern", a((Map) ImmutableMap.of("minecraft:soul_fire_torch", "minecraft:soul_torch", "minecraft:soul_fire_wall_torch", "minecraft:soul_wall_torch", "minecraft:soul_fire_lantern", "minecraft:soul_lantern"))));
        Schema schema115 = datafixerbuilder.addSchema(2529, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterStriderGravity(schema115, false));
        Schema schema116 = datafixerbuilder.addSchema(2531, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterRedstoneConnections(schema116));
        Schema schema117 = datafixerbuilder.addSchema(2533, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterVillagerFollowRange(schema117));
        Schema schema118 = datafixerbuilder.addSchema(2535, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterEntityShulkerRotation(schema118));
        Schema schema119 = datafixerbuilder.addSchema(2550, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterWorldGenSettingsBuilding(schema119));
        Schema schema120 = datafixerbuilder.addSchema(2551, DataConverterSchemaV2551::new);

        datafixerbuilder.addFixer(new DataConverterShoulderEntity(schema120, "add types to WorldGenData", DataConverterTypes.WORLD_GEN_SETTINGS));
        Schema schema121 = datafixerbuilder.addSchema(2552, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterBiomeBase(schema121, false, "Nether biome rename", ImmutableMap.of("minecraft:nether", "minecraft:nether_wastes")));
        Schema schema122 = datafixerbuilder.addSchema(2553, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterBiome(schema122, false));
        Schema schema123 = datafixerbuilder.addSchema(2558, DataConverterRegistry.b);

        datafixerbuilder.addFixer(new DataConverterMissingDimension(schema123, false));
        datafixerbuilder.addFixer(new DataConverterSettingRename(schema123, false, "Rename swapHands setting", "key_key.swapHands", "key_key.swapOffhand"));
        Schema schema124 = datafixerbuilder.addSchema(2568, DataConverterSchemaV2568::new);

        datafixerbuilder.addFixer(new DataConverterAddChoices(schema124, "Added Piglin Brute", DataConverterTypes.ENTITY));
    }

    private static UnaryOperator<String> a(Map<String, String> map) {
        return (s) -> {
            return (String) map.getOrDefault(s, s);
        };
    }

    private static UnaryOperator<String> a(String s, String s1) {
        return (s2) -> {
            return Objects.equals(s2, s) ? s1 : s2;
        };
    }
}
