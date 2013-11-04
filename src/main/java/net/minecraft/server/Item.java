package net.minecraft.server;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.UUID;

import net.minecraft.util.com.google.common.collect.HashMultimap;
import net.minecraft.util.com.google.common.collect.Multimap;
import net.minecraft.util.com.google.common.collect.Sets;

public class Item {

    public static final RegistryMaterials REGISTRY = new RegistryMaterials();
    protected static final UUID f = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
    private CreativeModeTab a;
    protected static Random g = new Random();
    protected int maxStackSize = 64;
    private int durability;
    protected boolean i;
    protected boolean j;
    private Item craftingResult;
    private String d;
    private String name;
    protected String l;

    public Item() {}

    public static int b(Item item) {
        return item == null ? 0 : REGISTRY.b(item);
    }

    public static Item d(int i) {
        return (Item) REGISTRY.a(i);
    }

    public static Item getItemOf(Block block) {
        return d(Block.b(block));
    }

    public static void l() {
        REGISTRY.a(256, "iron_shovel", (new ItemSpade(EnumToolMaterial.IRON)).c("shovelIron").f("iron_shovel"));
        REGISTRY.a(257, "iron_pickaxe", (new ItemPickaxe(EnumToolMaterial.IRON)).c("pickaxeIron").f("iron_pickaxe"));
        REGISTRY.a(258, "iron_axe", (new ItemAxe(EnumToolMaterial.IRON)).c("hatchetIron").f("iron_axe"));
        REGISTRY.a(259, "flint_and_steel", (new ItemFlintAndSteel()).c("flintAndSteel").f("flint_and_steel"));
        REGISTRY.a(260, "apple", (new ItemFood(4, 0.3F, false)).c("apple").f("apple"));
        REGISTRY.a(261, "bow", (new ItemBow()).c("bow").f("bow"));
        REGISTRY.a(262, "arrow", (new Item()).c("arrow").a(CreativeModeTab.j).f("arrow"));
        REGISTRY.a(263, "coal", (new ItemCoal()).c("coal").f("coal"));
        REGISTRY.a(264, "diamond", (new Item()).c("diamond").a(CreativeModeTab.l).f("diamond"));
        REGISTRY.a(265, "iron_ingot", (new Item()).c("ingotIron").a(CreativeModeTab.l).f("iron_ingot"));
        REGISTRY.a(266, "gold_ingot", (new Item()).c("ingotGold").a(CreativeModeTab.l).f("gold_ingot"));
        REGISTRY.a(267, "iron_sword", (new ItemSword(EnumToolMaterial.IRON)).c("swordIron").f("iron_sword"));
        REGISTRY.a(268, "wooden_sword", (new ItemSword(EnumToolMaterial.WOOD)).c("swordWood").f("wood_sword"));
        REGISTRY.a(269, "wooden_shovel", (new ItemSpade(EnumToolMaterial.WOOD)).c("shovelWood").f("wood_shovel"));
        REGISTRY.a(270, "wooden_pickaxe", (new ItemPickaxe(EnumToolMaterial.WOOD)).c("pickaxeWood").f("wood_pickaxe"));
        REGISTRY.a(271, "wooden_axe", (new ItemAxe(EnumToolMaterial.WOOD)).c("hatchetWood").f("wood_axe"));
        REGISTRY.a(272, "stone_sword", (new ItemSword(EnumToolMaterial.STONE)).c("swordStone").f("stone_sword"));
        REGISTRY.a(273, "stone_shovel", (new ItemSpade(EnumToolMaterial.STONE)).c("shovelStone").f("stone_shovel"));
        REGISTRY.a(274, "stone_pickaxe", (new ItemPickaxe(EnumToolMaterial.STONE)).c("pickaxeStone").f("stone_pickaxe"));
        REGISTRY.a(275, "stone_axe", (new ItemAxe(EnumToolMaterial.STONE)).c("hatchetStone").f("stone_axe"));
        REGISTRY.a(276, "diamond_sword", (new ItemSword(EnumToolMaterial.DIAMOND)).c("swordDiamond").f("diamond_sword"));
        REGISTRY.a(277, "diamond_shovel", (new ItemSpade(EnumToolMaterial.DIAMOND)).c("shovelDiamond").f("diamond_shovel"));
        REGISTRY.a(278, "diamond_pickaxe", (new ItemPickaxe(EnumToolMaterial.DIAMOND)).c("pickaxeDiamond").f("diamond_pickaxe"));
        REGISTRY.a(279, "diamond_axe", (new ItemAxe(EnumToolMaterial.DIAMOND)).c("hatchetDiamond").f("diamond_axe"));
        REGISTRY.a(280, "stick", (new Item()).q().c("stick").a(CreativeModeTab.l).f("stick"));
        REGISTRY.a(281, "bowl", (new Item()).c("bowl").a(CreativeModeTab.l).f("bowl"));
        REGISTRY.a(282, "mushroom_stew", (new ItemSoup(6)).c("mushroomStew").f("mushroom_stew"));
        REGISTRY.a(283, "golden_sword", (new ItemSword(EnumToolMaterial.GOLD)).c("swordGold").f("gold_sword"));
        REGISTRY.a(284, "golden_shovel", (new ItemSpade(EnumToolMaterial.GOLD)).c("shovelGold").f("gold_shovel"));
        REGISTRY.a(285, "golden_pickaxe", (new ItemPickaxe(EnumToolMaterial.GOLD)).c("pickaxeGold").f("gold_pickaxe"));
        REGISTRY.a(286, "golden_axe", (new ItemAxe(EnumToolMaterial.GOLD)).c("hatchetGold").f("gold_axe"));
        REGISTRY.a(287, "string", (new ItemReed(Blocks.TRIPWIRE)).c("string").a(CreativeModeTab.l).f("string"));
        REGISTRY.a(288, "feather", (new Item()).c("feather").a(CreativeModeTab.l).f("feather"));
        REGISTRY.a(289, "gunpowder", (new Item()).c("sulphur").e(PotionBrewer.k).a(CreativeModeTab.l).f("gunpowder"));
        REGISTRY.a(290, "wooden_hoe", (new ItemHoe(EnumToolMaterial.WOOD)).c("hoeWood").f("wood_hoe"));
        REGISTRY.a(291, "stone_hoe", (new ItemHoe(EnumToolMaterial.STONE)).c("hoeStone").f("stone_hoe"));
        REGISTRY.a(292, "iron_hoe", (new ItemHoe(EnumToolMaterial.IRON)).c("hoeIron").f("iron_hoe"));
        REGISTRY.a(293, "diamond_hoe", (new ItemHoe(EnumToolMaterial.DIAMOND)).c("hoeDiamond").f("diamond_hoe"));
        REGISTRY.a(294, "golden_hoe", (new ItemHoe(EnumToolMaterial.GOLD)).c("hoeGold").f("gold_hoe"));
        REGISTRY.a(295, "wheat_seeds", (new ItemSeeds(Blocks.CROPS, Blocks.SOIL)).c("seeds").f("seeds_wheat"));
        REGISTRY.a(296, "wheat", (new Item()).c("wheat").a(CreativeModeTab.l).f("wheat"));
        REGISTRY.a(297, "bread", (new ItemFood(5, 0.6F, false)).c("bread").f("bread"));
        REGISTRY.a(298, "leather_helmet", (new ItemArmor(EnumArmorMaterial.CLOTH, 0, 0)).c("helmetCloth").f("leather_helmet"));
        REGISTRY.a(299, "leather_chestplate", (new ItemArmor(EnumArmorMaterial.CLOTH, 0, 1)).c("chestplateCloth").f("leather_chestplate"));
        REGISTRY.a(300, "leather_leggings", (new ItemArmor(EnumArmorMaterial.CLOTH, 0, 2)).c("leggingsCloth").f("leather_leggings"));
        REGISTRY.a(301, "leather_boots", (new ItemArmor(EnumArmorMaterial.CLOTH, 0, 3)).c("bootsCloth").f("leather_boots"));
        REGISTRY.a(302, "chainmail_helmet", (new ItemArmor(EnumArmorMaterial.CHAIN, 1, 0)).c("helmetChain").f("chainmail_helmet"));
        REGISTRY.a(303, "chainmail_chestplate", (new ItemArmor(EnumArmorMaterial.CHAIN, 1, 1)).c("chestplateChain").f("chainmail_chestplate"));
        REGISTRY.a(304, "chainmail_leggings", (new ItemArmor(EnumArmorMaterial.CHAIN, 1, 2)).c("leggingsChain").f("chainmail_leggings"));
        REGISTRY.a(305, "chainmail_boots", (new ItemArmor(EnumArmorMaterial.CHAIN, 1, 3)).c("bootsChain").f("chainmail_boots"));
        REGISTRY.a(306, "iron_helmet", (new ItemArmor(EnumArmorMaterial.IRON, 2, 0)).c("helmetIron").f("iron_helmet"));
        REGISTRY.a(307, "iron_chestplate", (new ItemArmor(EnumArmorMaterial.IRON, 2, 1)).c("chestplateIron").f("iron_chestplate"));
        REGISTRY.a(308, "iron_leggings", (new ItemArmor(EnumArmorMaterial.IRON, 2, 2)).c("leggingsIron").f("iron_leggings"));
        REGISTRY.a(309, "iron_boots", (new ItemArmor(EnumArmorMaterial.IRON, 2, 3)).c("bootsIron").f("iron_boots"));
        REGISTRY.a(310, "diamond_helmet", (new ItemArmor(EnumArmorMaterial.DIAMOND, 3, 0)).c("helmetDiamond").f("diamond_helmet"));
        REGISTRY.a(311, "diamond_chestplate", (new ItemArmor(EnumArmorMaterial.DIAMOND, 3, 1)).c("chestplateDiamond").f("diamond_chestplate"));
        REGISTRY.a(312, "diamond_leggings", (new ItemArmor(EnumArmorMaterial.DIAMOND, 3, 2)).c("leggingsDiamond").f("diamond_leggings"));
        REGISTRY.a(313, "diamond_boots", (new ItemArmor(EnumArmorMaterial.DIAMOND, 3, 3)).c("bootsDiamond").f("diamond_boots"));
        REGISTRY.a(314, "golden_helmet", (new ItemArmor(EnumArmorMaterial.GOLD, 4, 0)).c("helmetGold").f("gold_helmet"));
        REGISTRY.a(315, "golden_chestplate", (new ItemArmor(EnumArmorMaterial.GOLD, 4, 1)).c("chestplateGold").f("gold_chestplate"));
        REGISTRY.a(316, "golden_leggings", (new ItemArmor(EnumArmorMaterial.GOLD, 4, 2)).c("leggingsGold").f("gold_leggings"));
        REGISTRY.a(317, "golden_boots", (new ItemArmor(EnumArmorMaterial.GOLD, 4, 3)).c("bootsGold").f("gold_boots"));
        REGISTRY.a(318, "flint", (new Item()).c("flint").a(CreativeModeTab.l).f("flint"));
        REGISTRY.a(319, "porkchop", (new ItemFood(3, 0.3F, true)).c("porkchopRaw").f("porkchop_raw"));
        REGISTRY.a(320, "cooked_porkchop", (new ItemFood(8, 0.8F, true)).c("porkchopCooked").f("porkchop_cooked"));
        REGISTRY.a(321, "painting", (new ItemHanging(EntityPainting.class)).c("painting").f("painting"));
        REGISTRY.a(322, "golden_apple", (new ItemGoldenApple(4, 1.2F, false)).j().a(MobEffectList.REGENERATION.id, 5, 1, 1.0F).c("appleGold").f("apple_golden"));
        REGISTRY.a(323, "sign", (new ItemSign()).c("sign").f("sign"));
        REGISTRY.a(324, "wooden_door", (new ItemDoor(Material.WOOD)).c("doorWood").f("door_wood"));
        Item item = (new ItemBucket(Blocks.AIR)).c("bucket").e(16).f("bucket_empty");

        REGISTRY.a(325, "bucket", item);
        REGISTRY.a(326, "water_bucket", (new ItemBucket(Blocks.WATER)).c("bucketWater").c(item).f("bucket_water"));
        REGISTRY.a(327, "lava_bucket", (new ItemBucket(Blocks.LAVA)).c("bucketLava").c(item).f("bucket_lava"));
        REGISTRY.a(328, "minecart", (new ItemMinecart(0)).c("minecart").f("minecart_normal"));
        REGISTRY.a(329, "saddle", (new ItemSaddle()).c("saddle").f("saddle"));
        REGISTRY.a(330, "iron_door", (new ItemDoor(Material.ORE)).c("doorIron").f("door_iron"));
        REGISTRY.a(331, "redstone", (new ItemRedstone()).c("redstone").e(PotionBrewer.i).f("redstone_dust"));
        REGISTRY.a(332, "snowball", (new ItemSnowball()).c("snowball").f("snowball"));
        REGISTRY.a(333, "boat", (new ItemBoat()).c("boat").f("boat"));
        REGISTRY.a(334, "leather", (new Item()).c("leather").a(CreativeModeTab.l).f("leather"));
        REGISTRY.a(335, "milk_bucket", (new ItemMilkBucket()).c("milk").c(item).f("bucket_milk"));
        REGISTRY.a(336, "brick", (new Item()).c("brick").a(CreativeModeTab.l).f("brick"));
        REGISTRY.a(337, "clay_ball", (new Item()).c("clay").a(CreativeModeTab.l).f("clay_ball"));
        REGISTRY.a(338, "reeds", (new ItemReed(Blocks.SUGAR_CANE_BLOCK)).c("reeds").a(CreativeModeTab.l).f("reeds"));
        REGISTRY.a(339, "paper", (new Item()).c("paper").a(CreativeModeTab.f).f("paper"));
        REGISTRY.a(340, "book", (new ItemBook()).c("book").a(CreativeModeTab.f).f("book_normal"));
        REGISTRY.a(341, "slime_ball", (new Item()).c("slimeball").a(CreativeModeTab.f).f("slimeball"));
        REGISTRY.a(342, "chest_minecart", (new ItemMinecart(1)).c("minecartChest").f("minecart_chest"));
        REGISTRY.a(343, "furnace_minecart", (new ItemMinecart(2)).c("minecartFurnace").f("minecart_furnace"));
        REGISTRY.a(344, "egg", (new ItemEgg()).c("egg").f("egg"));
        REGISTRY.a(345, "compass", (new Item()).c("compass").a(CreativeModeTab.i).f("compass"));
        REGISTRY.a(346, "fishing_rod", (new ItemFishingRod()).c("fishingRod").f("fishing_rod"));
        REGISTRY.a(347, "clock", (new Item()).c("clock").a(CreativeModeTab.i).f("clock"));
        REGISTRY.a(348, "glowstone_dust", (new Item()).c("yellowDust").e(PotionBrewer.j).a(CreativeModeTab.l).f("glowstone_dust"));
        REGISTRY.a(349, "fish", (new ItemFish(false)).c("fish").f("fish_raw").a(true));
        REGISTRY.a(350, "cooked_fished", (new ItemFish(true)).c("fish").f("fish_cooked").a(true));
        REGISTRY.a(351, "dye", (new ItemDye()).c("dyePowder").f("dye_powder"));
        REGISTRY.a(352, "bone", (new Item()).c("bone").q().a(CreativeModeTab.f).f("bone"));
        REGISTRY.a(353, "sugar", (new Item()).c("sugar").e(PotionBrewer.b).a(CreativeModeTab.l).f("sugar"));
        REGISTRY.a(354, "cake", (new ItemReed(Blocks.CAKE_BLOCK)).e(1).c("cake").a(CreativeModeTab.h).f("cake"));
        REGISTRY.a(355, "bed", (new ItemBed()).e(1).c("bed").f("bed"));
        REGISTRY.a(356, "repeater", (new ItemReed(Blocks.DIODE_OFF)).c("diode").a(CreativeModeTab.d).f("repeater"));
        REGISTRY.a(357, "cookie", (new ItemFood(2, 0.1F, false)).c("cookie").f("cookie"));
        REGISTRY.a(358, "filled_map", (new ItemWorldMap()).c("map").f("map_filled"));
        REGISTRY.a(359, "shears", (new ItemShears()).c("shears").f("shears"));
        REGISTRY.a(360, "melon", (new ItemFood(2, 0.3F, false)).c("melon").f("melon"));
        REGISTRY.a(361, "pumpkin_seeds", (new ItemSeeds(Blocks.PUMPKIN_STEM, Blocks.SOIL)).c("seeds_pumpkin").f("seeds_pumpkin"));
        REGISTRY.a(362, "melon_seeds", (new ItemSeeds(Blocks.MELON_STEM, Blocks.SOIL)).c("seeds_melon").f("seeds_melon"));
        REGISTRY.a(363, "beef", (new ItemFood(3, 0.3F, true)).c("beefRaw").f("beef_raw"));
        REGISTRY.a(364, "cooked_beef", (new ItemFood(8, 0.8F, true)).c("beefCooked").f("beef_cooked"));
        REGISTRY.a(365, "chicken", (new ItemFood(2, 0.3F, true)).a(MobEffectList.HUNGER.id, 30, 0, 0.3F).c("chickenRaw").f("chicken_raw"));
        REGISTRY.a(366, "cooked_chicken", (new ItemFood(6, 0.6F, true)).c("chickenCooked").f("chicken_cooked"));
        REGISTRY.a(367, "rotten_flesh", (new ItemFood(4, 0.1F, true)).a(MobEffectList.HUNGER.id, 30, 0, 0.8F).c("rottenFlesh").f("rotten_flesh"));
        REGISTRY.a(368, "ender_pearl", (new ItemEnderPearl()).c("enderPearl").f("ender_pearl"));
        REGISTRY.a(369, "blaze_rod", (new Item()).c("blazeRod").a(CreativeModeTab.l).f("blaze_rod"));
        REGISTRY.a(370, "ghast_tear", (new Item()).c("ghastTear").e(PotionBrewer.c).a(CreativeModeTab.k).f("ghast_tear"));
        REGISTRY.a(371, "gold_nugget", (new Item()).c("goldNugget").a(CreativeModeTab.l).f("gold_nugget"));
        REGISTRY.a(372, "nether_wart", (new ItemSeeds(Blocks.NETHER_WART, Blocks.SOUL_SAND)).c("netherStalkSeeds").e("+4").f("nether_wart"));
        REGISTRY.a(373, "potion", (new ItemPotion()).c("potion").f("potion"));
        REGISTRY.a(374, "glass_bottle", (new ItemGlassBottle()).c("glassBottle").f("potion_bottle_empty"));
        REGISTRY.a(375, "spider_eye", (new ItemFood(2, 0.8F, false)).a(MobEffectList.POISON.id, 5, 0, 1.0F).c("spiderEye").e(PotionBrewer.d).f("spider_eye"));
        REGISTRY.a(376, "fermented_spider_eye", (new Item()).c("fermentedSpiderEye").e(PotionBrewer.e).a(CreativeModeTab.k).f("spider_eye_fermented"));
        REGISTRY.a(377, "blaze_powder", (new Item()).c("blazePowder").e(PotionBrewer.g).a(CreativeModeTab.k).f("blaze_powder"));
        REGISTRY.a(378, "magma_cream", (new Item()).c("magmaCream").e(PotionBrewer.h).a(CreativeModeTab.k).f("magma_cream"));
        REGISTRY.a(379, "brewing_stand", (new ItemReed(Blocks.BREWING_STAND)).c("brewingStand").a(CreativeModeTab.k).f("brewing_stand"));
        REGISTRY.a(380, "cauldron", (new ItemReed(Blocks.CAULDRON)).c("cauldron").a(CreativeModeTab.k).f("cauldron"));
        REGISTRY.a(381, "ender_eye", (new ItemEnderEye()).c("eyeOfEnder").f("ender_eye"));
        REGISTRY.a(382, "speckled_melon", (new Item()).c("speckledMelon").e(PotionBrewer.f).a(CreativeModeTab.k).f("melon_speckled"));
        REGISTRY.a(383, "spawn_egg", (new ItemMonsterEgg()).c("monsterPlacer").f("spawn_egg"));
        REGISTRY.a(384, "experience_bottle", (new ItemExpBottle()).c("expBottle").f("experience_bottle"));
        REGISTRY.a(385, "fire_charge", (new ItemFireball()).c("fireball").f("fireball"));
        REGISTRY.a(386, "writable_book", (new ItemBookAndQuill()).c("writingBook").a(CreativeModeTab.f).f("book_writable"));
        REGISTRY.a(387, "written_book", (new ItemWrittenBook()).c("writtenBook").f("book_written").e(16));
        REGISTRY.a(388, "emerald", (new Item()).c("emerald").a(CreativeModeTab.l).f("emerald"));
        REGISTRY.a(389, "item_frame", (new ItemHanging(EntityItemFrame.class)).c("frame").f("item_frame"));
        REGISTRY.a(390, "flower_pot", (new ItemReed(Blocks.FLOWER_POT)).c("flowerPot").a(CreativeModeTab.c).f("flower_pot"));
        REGISTRY.a(391, "carrot", (new ItemSeedFood(4, 0.6F, Blocks.CARROTS, Blocks.SOIL)).c("carrots").f("carrot"));
        REGISTRY.a(392, "potato", (new ItemSeedFood(1, 0.3F, Blocks.POTATOES, Blocks.SOIL)).c("potato").f("potato"));
        REGISTRY.a(393, "baked_potato", (new ItemFood(6, 0.6F, false)).c("potatoBaked").f("potato_baked"));
        REGISTRY.a(394, "poisonous_potato", (new ItemFood(2, 0.3F, false)).a(MobEffectList.POISON.id, 5, 0, 0.6F).c("potatoPoisonous").f("potato_poisonous"));
        REGISTRY.a(395, "map", (new ItemMapEmpty()).c("emptyMap").f("map_empty"));
        REGISTRY.a(396, "golden_carrot", (new ItemFood(6, 1.2F, false)).c("carrotGolden").e(PotionBrewer.l).f("carrot_golden"));
        REGISTRY.a(397, "skull", (new ItemSkull()).c("skull").f("skull"));
        REGISTRY.a(398, "carrot_on_a_stick", (new ItemCarrotStick()).c("carrotOnAStick").f("carrot_on_a_stick"));
        REGISTRY.a(399, "nether_star", (new ItemNetherStar()).c("netherStar").a(CreativeModeTab.l).f("nether_star"));
        REGISTRY.a(400, "pumpkin_pie", (new ItemFood(8, 0.3F, false)).c("pumpkinPie").a(CreativeModeTab.h).f("pumpkin_pie"));
        REGISTRY.a(401, "fireworks", (new ItemFireworks()).c("fireworks").f("fireworks"));
        REGISTRY.a(402, "firework_charge", (new ItemFireworksCharge()).c("fireworksCharge").a(CreativeModeTab.f).f("fireworks_charge"));
        REGISTRY.a(403, "enchanted_book", (new ItemEnchantedBook()).e(1).c("enchantedBook").f("book_enchanted"));
        REGISTRY.a(404, "comparator", (new ItemReed(Blocks.REDSTONE_COMPARATOR_OFF)).c("comparator").a(CreativeModeTab.d).f("comparator"));
        REGISTRY.a(405, "netherbrick", (new Item()).c("netherbrick").a(CreativeModeTab.l).f("netherbrick"));
        REGISTRY.a(406, "quartz", (new Item()).c("netherquartz").a(CreativeModeTab.l).f("quartz"));
        REGISTRY.a(407, "tnt_minecart", (new ItemMinecart(3)).c("minecartTnt").f("minecart_tnt"));
        REGISTRY.a(408, "hopper_minecart", (new ItemMinecart(5)).c("minecartHopper").f("minecart_hopper"));
        REGISTRY.a(417, "iron_horse_armor", (new Item()).c("horsearmormetal").e(1).a(CreativeModeTab.f).f("iron_horse_armor"));
        REGISTRY.a(418, "golden_horse_armor", (new Item()).c("horsearmorgold").e(1).a(CreativeModeTab.f).f("gold_horse_armor"));
        REGISTRY.a(419, "diamond_horse_armor", (new Item()).c("horsearmordiamond").e(1).a(CreativeModeTab.f).f("diamond_horse_armor"));
        REGISTRY.a(420, "lead", (new ItemLeash()).c("leash").f("lead"));
        REGISTRY.a(421, "name_tag", (new ItemNameTag()).c("nameTag").f("name_tag"));
        REGISTRY.a(422, "command_block_minecart", (new ItemMinecart(6)).c("minecartCommandBlock").f("minecart_command_block").a((CreativeModeTab) null));
        REGISTRY.a(2256, "record_13", (new ItemRecord("13")).c("record").f("record_13"));
        REGISTRY.a(2257, "record_cat", (new ItemRecord("cat")).c("record").f("record_cat"));
        REGISTRY.a(2258, "record_blocks", (new ItemRecord("blocks")).c("record").f("record_blocks"));
        REGISTRY.a(2259, "record_chirp", (new ItemRecord("chirp")).c("record").f("record_chirp"));
        REGISTRY.a(2260, "record_far", (new ItemRecord("far")).c("record").f("record_far"));
        REGISTRY.a(2261, "record_mall", (new ItemRecord("mall")).c("record").f("record_mall"));
        REGISTRY.a(2262, "record_mellohi", (new ItemRecord("mellohi")).c("record").f("record_mellohi"));
        REGISTRY.a(2263, "record_stal", (new ItemRecord("stal")).c("record").f("record_stal"));
        REGISTRY.a(2264, "record_strad", (new ItemRecord("strad")).c("record").f("record_strad"));
        REGISTRY.a(2265, "record_ward", (new ItemRecord("ward")).c("record").f("record_ward"));
        REGISTRY.a(2266, "record_11", (new ItemRecord("11")).c("record").f("record_11"));
        REGISTRY.a(2267, "record_wait", (new ItemRecord("wait")).c("record").f("record_wait"));
        HashSet hashset = Sets.newHashSet(new Block[] { Blocks.AIR, Blocks.BREWING_STAND, Blocks.BED, Blocks.NETHER_WART, Blocks.CAULDRON, Blocks.FLOWER_POT, Blocks.CROPS, Blocks.SUGAR_CANE_BLOCK, Blocks.CAKE_BLOCK, Blocks.SKULL, Blocks.PISTON_EXTENSION, Blocks.PISTON_MOVING, Blocks.GLOWING_REDSTONE_ORE, Blocks.DIODE_ON, Blocks.PUMPKIN_STEM, Blocks.SIGN_POST, Blocks.REDSTONE_COMPARATOR_ON, Blocks.TRIPWIRE, Blocks.REDSTONE_LAMP_ON, Blocks.MELON_STEM, Blocks.REDSTONE_TORCH_OFF, Blocks.REDSTONE_COMPARATOR_OFF, Blocks.REDSTONE_WIRE, Blocks.WALL_SIGN, Blocks.DIODE_OFF, Blocks.IRON_DOOR_BLOCK, Blocks.WOODEN_DOOR});
        Iterator iterator = Block.REGISTRY.b().iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();
            Block block = (Block) Block.REGISTRY.a(s);
            Object object;

            if (block == Blocks.WOOL) {
                object = (new ItemCloth(Blocks.WOOL)).b("cloth");
            } else if (block == Blocks.STAINED_HARDENED_CLAY) {
                object = (new ItemCloth(Blocks.STAINED_HARDENED_CLAY)).b("clayHardenedStained");
            } else if (block == Blocks.STAINED_GLASS) {
                object = (new ItemCloth(Blocks.STAINED_GLASS)).b("stainedGlass");
            } else if (block == Blocks.STAINED_GLASS_PANE) {
                object = (new ItemCloth(Blocks.STAINED_GLASS_PANE)).b("stainedGlassPane");
            } else if (block == Blocks.WOOL_CARPET) {
                object = (new ItemCloth(Blocks.WOOL_CARPET)).b("woolCarpet");
            } else if (block == Blocks.DIRT) {
                object = (new ItemMultiTexture(Blocks.DIRT, Blocks.DIRT, BlockDirt.a)).b("dirt");
            } else if (block == Blocks.SAND) {
                object = (new ItemMultiTexture(Blocks.SAND, Blocks.SAND, BlockSand.a)).b("sand");
            } else if (block == Blocks.LOG) {
                object = (new ItemMultiTexture(Blocks.LOG, Blocks.LOG, BlockLog1.M)).b("log");
            } else if (block == Blocks.LOG2) {
                object = (new ItemMultiTexture(Blocks.LOG2, Blocks.LOG2, BlockLog2.M)).b("log");
            } else if (block == Blocks.WOOD) {
                object = (new ItemMultiTexture(Blocks.WOOD, Blocks.WOOD, BlockWood.a)).b("wood");
            } else if (block == Blocks.MONSTER_EGGS) {
                object = (new ItemMultiTexture(Blocks.MONSTER_EGGS, Blocks.MONSTER_EGGS, BlockMonsterEggs.a)).b("monsterStoneEgg");
            } else if (block == Blocks.SMOOTH_BRICK) {
                object = (new ItemMultiTexture(Blocks.SMOOTH_BRICK, Blocks.SMOOTH_BRICK, BlockSmoothBrick.a)).b("stonebricksmooth");
            } else if (block == Blocks.SANDSTONE) {
                object = (new ItemMultiTexture(Blocks.SANDSTONE, Blocks.SANDSTONE, BlockSandStone.a)).b("sandStone");
            } else if (block == Blocks.QUARTZ_BLOCK) {
                object = (new ItemMultiTexture(Blocks.QUARTZ_BLOCK, Blocks.QUARTZ_BLOCK, BlockQuartz.a)).b("quartzBlock");
            } else if (block == Blocks.STEP) {
                object = (new ItemStep(Blocks.STEP, Blocks.STEP, Blocks.DOUBLE_STEP, false)).b("stoneSlab");
            } else if (block == Blocks.DOUBLE_STEP) {
                object = (new ItemStep(Blocks.DOUBLE_STEP, Blocks.STEP, Blocks.DOUBLE_STEP, true)).b("stoneSlab");
            } else if (block == Blocks.WOOD_STEP) {
                object = (new ItemStep(Blocks.WOOD_STEP, Blocks.WOOD_STEP, Blocks.WOOD_DOUBLE_STEP, false)).b("woodSlab");
            } else if (block == Blocks.WOOD_DOUBLE_STEP) {
                object = (new ItemStep(Blocks.WOOD_DOUBLE_STEP, Blocks.WOOD_STEP, Blocks.WOOD_DOUBLE_STEP, true)).b("woodSlab");
            } else if (block == Blocks.SAPLING) {
                object = (new ItemMultiTexture(Blocks.SAPLING, Blocks.SAPLING, BlockSapling.a)).b("sapling");
            } else if (block == Blocks.LEAVES) {
                object = (new ItemLeaves(Blocks.LEAVES)).b("leaves");
            } else if (block == Blocks.LEAVES2) {
                object = (new ItemLeaves(Blocks.LEAVES2)).b("leaves");
            } else if (block == Blocks.VINE) {
                object = new ItemWithAuxData(Blocks.VINE, false);
            } else if (block == Blocks.LONG_GRASS) {
                object = (new ItemWithAuxData(Blocks.LONG_GRASS, true)).a(new String[] { "shrub", "grass", "fern"});
            } else if (block == Blocks.YELLOW_FLOWER) {
                object = (new ItemMultiTexture(Blocks.YELLOW_FLOWER, Blocks.YELLOW_FLOWER, BlockFlowers.b)).b("flower");
            } else if (block == Blocks.RED_ROSE) {
                object = (new ItemMultiTexture(Blocks.RED_ROSE, Blocks.RED_ROSE, BlockFlowers.a)).b("rose");
            } else if (block == Blocks.SNOW) {
                object = new ItemSnow(Blocks.SNOW, Blocks.SNOW);
            } else if (block == Blocks.WATER_LILY) {
                object = new ItemWaterLily(Blocks.WATER_LILY);
            } else if (block == Blocks.PISTON) {
                object = new ItemPiston(Blocks.PISTON);
            } else if (block == Blocks.PISTON_STICKY) {
                object = new ItemPiston(Blocks.PISTON_STICKY);
            } else if (block == Blocks.COBBLE_WALL) {
                object = (new ItemMultiTexture(Blocks.COBBLE_WALL, Blocks.COBBLE_WALL, BlockCobbleWall.a)).b("cobbleWall");
            } else if (block == Blocks.ANVIL) {
                object = (new ItemAnvil(Blocks.ANVIL)).b("anvil");
            } else if (block == Blocks.DOUBLE_PLANT) {
                object = (new ItemTallPlant(Blocks.DOUBLE_PLANT, Blocks.DOUBLE_PLANT, BlockTallPlant.a)).b("doublePlant");
                // CraftBukkit start - allow certain blocks to retain data
            } else if (block == Blocks.MOB_SPAWNER || block == Blocks.BIG_MUSHROOM_1 || block == Blocks.BIG_MUSHROOM_2) {
                object = new ItemWithAuxData(block, true);
                // CraftBukkit end
            } else {
                if (hashset.contains(block)) {
                    continue;
                }

                object = new ItemBlock(block);
            }

            REGISTRY.a(Block.b(block), s, object);
        }
    }

    public Item e(int i) {
        this.maxStackSize = i;
        return this;
    }

    public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l, float f, float f1, float f2) {
        return false;
    }

    public float getDestroySpeed(ItemStack itemstack, Block block) {
        return 1.0F;
    }

    public ItemStack a(ItemStack itemstack, World world, EntityHuman entityhuman) {
        return itemstack;
    }

    public ItemStack b(ItemStack itemstack, World world, EntityHuman entityhuman) {
        return itemstack;
    }

    public int getMaxStackSize() {
        return this.maxStackSize;
    }

    public int filterData(int i) {
        return 0;
    }

    public boolean n() {
        return this.j;
    }

    protected Item a(boolean flag) {
        this.j = flag;
        return this;
    }

    public int getMaxDurability() {
        return this.durability;
    }

    protected Item setMaxDurability(int i) {
        this.durability = i;
        return this;
    }

    public boolean usesDurability() {
        return this.durability > 0 && !this.j;
    }

    public boolean a(ItemStack itemstack, EntityLiving entityliving, EntityLiving entityliving1) {
        return false;
    }

    public boolean a(ItemStack itemstack, World world, Block block, int i, int j, int k, EntityLiving entityliving) {
        return false;
    }

    public boolean canDestroySpecialBlock(Block block) {
        return false;
    }

    public boolean a(ItemStack itemstack, EntityHuman entityhuman, EntityLiving entityliving) {
        return false;
    }

    public Item q() {
        this.i = true;
        return this;
    }

    public Item c(String s) {
        this.name = s;
        return this;
    }

    public String k(ItemStack itemstack) {
        String s = this.a(itemstack);

        return s == null ? "" : LocaleI18n.get(s);
    }

    public String getName() {
        return "item." + this.name;
    }

    public String a(ItemStack itemstack) {
        return "item." + this.name;
    }

    public Item c(Item item) {
        this.craftingResult = item;
        return this;
    }

    public boolean l(ItemStack itemstack) {
        return true;
    }

    public boolean s() {
        return true;
    }

    public Item t() {
        return this.craftingResult;
    }

    public boolean u() {
        return this.craftingResult != null;
    }

    public void a(ItemStack itemstack, World world, Entity entity, int i, boolean flag) {}

    public void d(ItemStack itemstack, World world, EntityHuman entityhuman) {}

    public boolean h() {
        return false;
    }

    public EnumAnimation d(ItemStack itemstack) {
        return EnumAnimation.NONE;
    }

    public int d_(ItemStack itemstack) {
        return 0;
    }

    public void a(ItemStack itemstack, World world, EntityHuman entityhuman, int i) {}

    protected Item e(String s) {
        this.d = s;
        return this;
    }

    public String i(ItemStack itemstack) {
        return this.d;
    }

    public boolean m(ItemStack itemstack) {
        return this.i(itemstack) != null;
    }

    public String n(ItemStack itemstack) {
        return ("" + LocaleI18n.get(this.k(itemstack) + ".name")).trim();
    }

    public EnumItemRarity f(ItemStack itemstack) {
        return itemstack.hasEnchantments() ? EnumItemRarity.RARE : EnumItemRarity.COMMON;
    }

    public boolean e_(ItemStack itemstack) {
        return this.getMaxStackSize() == 1 && this.usesDurability();
    }

    protected MovingObjectPosition a(World world, EntityHuman entityhuman, boolean flag) {
        float f = 1.0F;
        float f1 = entityhuman.lastPitch + (entityhuman.pitch - entityhuman.lastPitch) * f;
        float f2 = entityhuman.lastYaw + (entityhuman.yaw - entityhuman.lastYaw) * f;
        double d0 = entityhuman.lastX + (entityhuman.locX - entityhuman.lastX) * (double) f;
        double d1 = entityhuman.lastY + (entityhuman.locY - entityhuman.lastY) * (double) f + 1.62D - (double) entityhuman.height;
        double d2 = entityhuman.lastZ + (entityhuman.locZ - entityhuman.lastZ) * (double) f;
        Vec3D vec3d = world.getVec3DPool().create(d0, d1, d2);
        float f3 = MathHelper.cos(-f2 * 0.017453292F - 3.1415927F);
        float f4 = MathHelper.sin(-f2 * 0.017453292F - 3.1415927F);
        float f5 = -MathHelper.cos(-f1 * 0.017453292F);
        float f6 = MathHelper.sin(-f1 * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        double d3 = 5.0D;
        Vec3D vec3d1 = vec3d.add((double) f7 * d3, (double) f6 * d3, (double) f8 * d3);

        return world.rayTrace(vec3d, vec3d1, flag, !flag, false);
    }

    public int c() {
        return 0;
    }

    public Item a(CreativeModeTab creativemodetab) {
        this.a = creativemodetab;
        return this;
    }

    public boolean v() {
        return true;
    }

    public boolean a(ItemStack itemstack, ItemStack itemstack1) {
        return false;
    }

    public Multimap k() {
        return HashMultimap.create();
    }

    protected Item f(String s) {
        this.l = s;
        return this;
    }
}
