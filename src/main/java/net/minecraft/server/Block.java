package net.minecraft.server;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Block {

    public static final RegistryMaterials REGISTRY = new RegistryBlocks("air");
    private CreativeModeTab creativeTab;
    protected String d;
    public static final StepSound e = new StepSound("stone", 1.0F, 1.0F);
    public static final StepSound f = new StepSound("wood", 1.0F, 1.0F);
    public static final StepSound g = new StepSound("gravel", 1.0F, 1.0F);
    public static final StepSound h = new StepSound("grass", 1.0F, 1.0F);
    public static final StepSound i = new StepSound("stone", 1.0F, 1.0F);
    public static final StepSound j = new StepSound("stone", 1.0F, 1.5F);
    public static final StepSound k = new StepSoundStone("stone", 1.0F, 1.0F);
    public static final StepSound l = new StepSound("cloth", 1.0F, 1.0F);
    public static final StepSound m = new StepSound("sand", 1.0F, 1.0F);
    public static final StepSound n = new StepSound("snow", 1.0F, 1.0F);
    public static final StepSound o = new StepSoundLadder("ladder", 1.0F, 1.0F);
    public static final StepSound p = new StepSoundAnvil("anvil", 0.3F, 1.0F);
    protected boolean q;
    protected int r;
    protected boolean s;
    protected int t;
    protected boolean u;
    protected float strength;
    protected float durability;
    protected boolean x = true;
    protected boolean y = true;
    protected boolean z;
    protected boolean isTileEntity;
    protected double minX;
    protected double minY;
    protected double minZ;
    protected double maxX;
    protected double maxY;
    protected double maxZ;
    public StepSound stepSound;
    public float I;
    protected final Material material;
    public float frictionFactor;
    private String name;

    public static int b(Block block) {
        return REGISTRY.b(block);
    }

    public static Block e(int i) {
        return (Block) REGISTRY.a(i);
    }

    public static Block a(Item item) {
        return e(Item.b(item));
    }

    public static Block b(String s) {
        if (REGISTRY.b(s)) {
            return (Block) REGISTRY.a(s);
        } else {
            try {
                return (Block) REGISTRY.a(Integer.parseInt(s));
            } catch (NumberFormatException numberformatexception) {
                return null;
            }
        }
    }

    public boolean j() {
        return this.q;
    }

    public int k() {
        return this.r;
    }

    public int m() {
        return this.t;
    }

    public boolean n() {
        return this.u;
    }

    public Material getMaterial() {
        return this.material;
    }

    public MaterialMapColor f(int i) {
        return this.getMaterial().r();
    }

    public static void p() {
        REGISTRY.a(0, "air", (new BlockAir()).c("air"));
        REGISTRY.a(1, "stone", (new BlockStone()).c(1.5F).b(10.0F).a(i).c("stone").d("stone"));
        REGISTRY.a(2, "grass", (new BlockGrass()).c(0.6F).a(h).c("grass").d("grass"));
        REGISTRY.a(3, "dirt", (new BlockDirt()).c(0.5F).a(g).c("dirt").d("dirt"));
        Block block = (new Block(Material.STONE)).c(2.0F).b(10.0F).a(i).c("stonebrick").a(CreativeModeTab.b).d("cobblestone");

        REGISTRY.a(4, "cobblestone", block);
        Block block1 = (new BlockWood()).c(2.0F).b(5.0F).a(f).c("wood").d("planks");

        REGISTRY.a(5, "planks", block1);
        REGISTRY.a(6, "sapling", (new BlockSapling()).c(0.0F).a(h).c("sapling").d("sapling"));
        REGISTRY.a(7, "bedrock", (new Block(Material.STONE)).s().b(6000000.0F).a(i).c("bedrock").H().a(CreativeModeTab.b).d("bedrock"));
        REGISTRY.a(8, "flowing_water", (new BlockFlowing(Material.WATER)).c(100.0F).g(3).c("water").H().d("water_flow"));
        REGISTRY.a(9, "water", (new BlockStationary(Material.WATER)).c(100.0F).g(3).c("water").H().d("water_still"));
        REGISTRY.a(10, "flowing_lava", (new BlockFlowing(Material.LAVA)).c(100.0F).a(1.0F).c("lava").H().d("lava_flow"));
        REGISTRY.a(11, "lava", (new BlockStationary(Material.LAVA)).c(100.0F).a(1.0F).c("lava").H().d("lava_still"));
        REGISTRY.a(12, "sand", (new BlockSand()).c(0.5F).a(m).c("sand").d("sand"));
        REGISTRY.a(13, "gravel", (new BlockGravel()).c(0.6F).a(g).c("gravel").d("gravel"));
        REGISTRY.a(14, "gold_ore", (new BlockOre()).c(3.0F).b(5.0F).a(i).c("oreGold").d("gold_ore"));
        REGISTRY.a(15, "iron_ore", (new BlockOre()).c(3.0F).b(5.0F).a(i).c("oreIron").d("iron_ore"));
        REGISTRY.a(16, "coal_ore", (new BlockOre()).c(3.0F).b(5.0F).a(i).c("oreCoal").d("coal_ore"));
        REGISTRY.a(17, "log", (new BlockLog1()).c("log").d("log"));
        REGISTRY.a(18, "leaves", (new BlockLeaves1()).c("leaves").d("leaves"));
        REGISTRY.a(19, "sponge", (new BlockSponge()).c(0.6F).a(h).c("sponge").d("sponge"));
        REGISTRY.a(20, "glass", (new BlockGlass(Material.SHATTERABLE, false)).c(0.3F).a(k).c("glass").d("glass"));
        REGISTRY.a(21, "lapis_ore", (new BlockOre()).c(3.0F).b(5.0F).a(i).c("oreLapis").d("lapis_ore"));
        REGISTRY.a(22, "lapis_block", (new BlockOreBlock(MaterialMapColor.H)).c(3.0F).b(5.0F).a(i).c("blockLapis").a(CreativeModeTab.b).d("lapis_block"));
        REGISTRY.a(23, "dispenser", (new BlockDispenser()).c(3.5F).a(i).c("dispenser").d("dispenser"));
        Block block2 = (new BlockSandStone()).a(i).c(0.8F).c("sandStone").d("sandstone");

        REGISTRY.a(24, "sandstone", block2);
        REGISTRY.a(25, "noteblock", (new BlockNote()).c(0.8F).c("musicBlock").d("noteblock"));
        REGISTRY.a(26, "bed", (new BlockBed()).c(0.2F).c("bed").H().d("bed"));
        REGISTRY.a(27, "golden_rail", (new BlockPoweredRail()).c(0.7F).a(j).c("goldenRail").d("rail_golden"));
        REGISTRY.a(28, "detector_rail", (new BlockMinecartDetector()).c(0.7F).a(j).c("detectorRail").d("rail_detector"));
        REGISTRY.a(29, "sticky_piston", (new BlockPiston(true)).c("pistonStickyBase"));
        REGISTRY.a(30, "web", (new BlockWeb()).g(1).c(4.0F).c("web").d("web"));
        REGISTRY.a(31, "tallgrass", (new BlockLongGrass()).c(0.0F).a(h).c("tallgrass"));
        REGISTRY.a(32, "deadbush", (new BlockDeadBush()).c(0.0F).a(h).c("deadbush").d("deadbush"));
        REGISTRY.a(33, "piston", (new BlockPiston(false)).c("pistonBase"));
        REGISTRY.a(34, "piston_head", new BlockPistonExtension());
        REGISTRY.a(35, "wool", (new BlockCloth(Material.CLOTH)).c(0.8F).a(l).c("cloth").d("wool_colored"));
        REGISTRY.a(36, "piston_extension", new BlockPistonMoving());
        REGISTRY.a(37, "yellow_flower", (new BlockFlowers(0)).c(0.0F).a(h).c("flower1").d("flower_dandelion"));
        REGISTRY.a(38, "red_flower", (new BlockFlowers(1)).c(0.0F).a(h).c("flower2").d("flower_rose"));
        REGISTRY.a(39, "brown_mushroom", (new BlockMushroom()).c(0.0F).a(h).a(0.125F).c("mushroom").d("mushroom_brown"));
        REGISTRY.a(40, "red_mushroom", (new BlockMushroom()).c(0.0F).a(h).c("mushroom").d("mushroom_red"));
        REGISTRY.a(41, "gold_block", (new BlockOreBlock(MaterialMapColor.F)).c(3.0F).b(10.0F).a(j).c("blockGold").d("gold_block"));
        REGISTRY.a(42, "iron_block", (new BlockOreBlock(MaterialMapColor.h)).c(5.0F).b(10.0F).a(j).c("blockIron").d("iron_block"));
        REGISTRY.a(43, "double_stone_slab", (new BlockStep(true)).c(2.0F).b(10.0F).a(i).c("stoneSlab"));
        REGISTRY.a(44, "stone_slab", (new BlockStep(false)).c(2.0F).b(10.0F).a(i).c("stoneSlab"));
        Block block3 = (new Block(Material.STONE)).c(2.0F).b(10.0F).a(i).c("brick").a(CreativeModeTab.b).d("brick");

        REGISTRY.a(45, "brick_block", block3);
        REGISTRY.a(46, "tnt", (new BlockTNT()).c(0.0F).a(h).c("tnt").d("tnt"));
        REGISTRY.a(47, "bookshelf", (new BlockBookshelf()).c(1.5F).a(f).c("bookshelf").d("bookshelf"));
        REGISTRY.a(48, "mossy_cobblestone", (new Block(Material.STONE)).c(2.0F).b(10.0F).a(i).c("stoneMoss").a(CreativeModeTab.b).d("cobblestone_mossy"));
        REGISTRY.a(49, "obsidian", (new BlockObsidian()).c(50.0F).b(2000.0F).a(i).c("obsidian").d("obsidian"));
        REGISTRY.a(50, "torch", (new BlockTorch()).c(0.0F).a(0.9375F).a(f).c("torch").d("torch_on"));
        REGISTRY.a(51, "fire", (new BlockFire()).c(0.0F).a(1.0F).a(f).c("fire").H().d("fire"));
        REGISTRY.a(52, "mob_spawner", (new BlockMobSpawner()).c(5.0F).a(j).c("mobSpawner").H().d("mob_spawner"));
        REGISTRY.a(53, "oak_stairs", (new BlockStairs(block1, 0)).c("stairsWood"));
        REGISTRY.a(54, "chest", (new BlockChest(0)).c(2.5F).a(f).c("chest"));
        REGISTRY.a(55, "redstone_wire", (new BlockRedstoneWire()).c(0.0F).a(e).c("redstoneDust").H().d("redstone_dust"));
        REGISTRY.a(56, "diamond_ore", (new BlockOre()).c(3.0F).b(5.0F).a(i).c("oreDiamond").d("diamond_ore"));
        REGISTRY.a(57, "diamond_block", (new BlockOreBlock(MaterialMapColor.G)).c(5.0F).b(10.0F).a(j).c("blockDiamond").d("diamond_block"));
        REGISTRY.a(58, "crafting_table", (new BlockWorkbench()).c(2.5F).a(f).c("workbench").d("crafting_table"));
        REGISTRY.a(59, "wheat", (new BlockCrops()).c("crops").d("wheat"));
        Block block4 = (new BlockSoil()).c(0.6F).a(g).c("farmland").d("farmland");

        REGISTRY.a(60, "farmland", block4);
        REGISTRY.a(61, "furnace", (new BlockFurnace(false)).c(3.5F).a(i).c("furnace").a(CreativeModeTab.c));
        REGISTRY.a(62, "lit_furnace", (new BlockFurnace(true)).c(3.5F).a(i).a(0.875F).c("furnace"));
        REGISTRY.a(63, "standing_sign", (new BlockSign(TileEntitySign.class, true)).c(1.0F).a(f).c("sign").H());
        REGISTRY.a(64, "wooden_door", (new BlockDoor(Material.WOOD)).c(3.0F).a(f).c("doorWood").H().d("door_wood"));
        REGISTRY.a(65, "ladder", (new BlockLadder()).c(0.4F).a(o).c("ladder").d("ladder"));
        REGISTRY.a(66, "rail", (new BlockMinecartTrack()).c(0.7F).a(j).c("rail").d("rail_normal"));
        REGISTRY.a(67, "stone_stairs", (new BlockStairs(block, 0)).c("stairsStone"));
        REGISTRY.a(68, "wall_sign", (new BlockSign(TileEntitySign.class, false)).c(1.0F).a(f).c("sign").H());
        REGISTRY.a(69, "lever", (new BlockLever()).c(0.5F).a(f).c("lever").d("lever"));
        REGISTRY.a(70, "stone_pressure_plate", (new BlockPressurePlateBinary("stone", Material.STONE, EnumMobType.MOBS)).c(0.5F).a(i).c("pressurePlate"));
        REGISTRY.a(71, "iron_door", (new BlockDoor(Material.ORE)).c(5.0F).a(j).c("doorIron").H().d("door_iron"));
        REGISTRY.a(72, "wooden_pressure_plate", (new BlockPressurePlateBinary("planks_oak", Material.WOOD, EnumMobType.EVERYTHING)).c(0.5F).a(f).c("pressurePlate"));
        REGISTRY.a(73, "redstone_ore", (new BlockRedstoneOre(false)).c(3.0F).b(5.0F).a(i).c("oreRedstone").a(CreativeModeTab.b).d("redstone_ore"));
        REGISTRY.a(74, "lit_redstone_ore", (new BlockRedstoneOre(true)).a(0.625F).c(3.0F).b(5.0F).a(i).c("oreRedstone").d("redstone_ore"));
        REGISTRY.a(75, "unlit_redstone_torch", (new BlockRedstoneTorch(false)).c(0.0F).a(f).c("notGate").d("redstone_torch_off"));
        REGISTRY.a(76, "redstone_torch", (new BlockRedstoneTorch(true)).c(0.0F).a(0.5F).a(f).c("notGate").a(CreativeModeTab.d).d("redstone_torch_on"));
        REGISTRY.a(77, "stone_button", (new BlockStoneButton()).c(0.5F).a(i).c("button"));
        REGISTRY.a(78, "snow_layer", (new BlockSnow()).c(0.1F).a(n).c("snow").g(0).d("snow"));
        REGISTRY.a(79, "ice", (new BlockIce()).c(0.5F).g(3).a(k).c("ice").d("ice"));
        REGISTRY.a(80, "snow", (new BlockSnowBlock()).c(0.2F).a(n).c("snow").d("snow"));
        REGISTRY.a(81, "cactus", (new BlockCactus()).c(0.4F).a(l).c("cactus").d("cactus"));
        REGISTRY.a(82, "clay", (new BlockClay()).c(0.6F).a(g).c("clay").d("clay"));
        REGISTRY.a(83, "reeds", (new BlockReed()).c(0.0F).a(h).c("reeds").H().d("reeds"));
        REGISTRY.a(84, "jukebox", (new BlockJukeBox()).c(2.0F).b(10.0F).a(i).c("jukebox").d("jukebox"));
        REGISTRY.a(85, "fence", (new BlockFence("planks_oak", Material.WOOD)).c(2.0F).b(5.0F).a(f).c("fence"));
        Block block5 = (new BlockPumpkin(false)).c(1.0F).a(f).c("pumpkin").d("pumpkin");

        REGISTRY.a(86, "pumpkin", block5);
        REGISTRY.a(87, "netherrack", (new BlockBloodStone()).c(0.4F).a(i).c("hellrock").d("netherrack"));
        REGISTRY.a(88, "soul_sand", (new BlockSlowSand()).c(0.5F).a(m).c("hellsand").d("soul_sand"));
        REGISTRY.a(89, "glowstone", (new BlockLightStone(Material.SHATTERABLE)).c(0.3F).a(k).a(1.0F).c("lightgem").d("glowstone"));
        REGISTRY.a(90, "portal", (new BlockPortal()).c(-1.0F).a(k).a(0.75F).c("portal").d("portal"));
        REGISTRY.a(91, "lit_pumpkin", (new BlockPumpkin(true)).c(1.0F).a(f).a(1.0F).c("litpumpkin").d("pumpkin"));
        REGISTRY.a(92, "cake", (new BlockCake()).c(0.5F).a(l).c("cake").H().d("cake"));
        REGISTRY.a(93, "unpowered_repeater", (new BlockRepeater(false)).c(0.0F).a(f).c("diode").H().d("repeater_off"));
        REGISTRY.a(94, "powered_repeater", (new BlockRepeater(true)).c(0.0F).a(0.625F).a(f).c("diode").H().d("repeater_on"));
        REGISTRY.a(95, "stained_glass", (new BlockStainedGlass(Material.SHATTERABLE)).c(0.3F).a(k).c("stainedGlass").d("glass"));
        REGISTRY.a(96, "trapdoor", (new BlockTrapdoor(Material.WOOD)).c(3.0F).a(f).c("trapdoor").H().d("trapdoor"));
        REGISTRY.a(97, "monster_egg", (new BlockMonsterEggs()).c(0.75F).c("monsterStoneEgg"));
        Block block6 = (new BlockSmoothBrick()).c(1.5F).b(10.0F).a(i).c("stonebricksmooth").d("stonebrick");

        REGISTRY.a(98, "stonebrick", block6);
        REGISTRY.a(99, "brown_mushroom_block", (new BlockHugeMushroom(Material.WOOD, 0)).c(0.2F).a(f).c("mushroom").d("mushroom_block"));
        REGISTRY.a(100, "red_mushroom_block", (new BlockHugeMushroom(Material.WOOD, 1)).c(0.2F).a(f).c("mushroom").d("mushroom_block"));
        REGISTRY.a(101, "iron_bars", (new BlockThin("iron_bars", "iron_bars", Material.ORE, true)).c(5.0F).b(10.0F).a(j).c("fenceIron"));
        REGISTRY.a(102, "glass_pane", (new BlockThin("glass", "glass_pane_top", Material.SHATTERABLE, false)).c(0.3F).a(k).c("thinGlass"));
        Block block7 = (new BlockMelon()).c(1.0F).a(f).c("melon").d("melon");

        REGISTRY.a(103, "melon_block", block7);
        REGISTRY.a(104, "pumpkin_stem", (new BlockStem(block5)).c(0.0F).a(f).c("pumpkinStem").d("pumpkin_stem"));
        REGISTRY.a(105, "melon_stem", (new BlockStem(block7)).c(0.0F).a(f).c("pumpkinStem").d("melon_stem"));
        REGISTRY.a(106, "vine", (new BlockVine()).c(0.2F).a(h).c("vine").d("vine"));
        REGISTRY.a(107, "fence_gate", (new BlockFenceGate()).c(2.0F).b(5.0F).a(f).c("fenceGate"));
        REGISTRY.a(108, "brick_stairs", (new BlockStairs(block3, 0)).c("stairsBrick"));
        REGISTRY.a(109, "stone_brick_stairs", (new BlockStairs(block6, 0)).c("stairsStoneBrickSmooth"));
        REGISTRY.a(110, "mycelium", (new BlockMycel()).c(0.6F).a(h).c("mycel").d("mycelium"));
        REGISTRY.a(111, "waterlily", (new BlockWaterLily()).c(0.0F).a(h).c("waterlily").d("waterlily"));
        Block block8 = (new Block(Material.STONE)).c(2.0F).b(10.0F).a(i).c("netherBrick").a(CreativeModeTab.b).d("nether_brick");

        REGISTRY.a(112, "nether_brick", block8);
        REGISTRY.a(113, "nether_brick_fence", (new BlockFence("nether_brick", Material.STONE)).c(2.0F).b(10.0F).a(i).c("netherFence"));
        REGISTRY.a(114, "nether_brick_stairs", (new BlockStairs(block8, 0)).c("stairsNetherBrick"));
        REGISTRY.a(115, "nether_wart", (new BlockNetherWart()).c("netherStalk").d("nether_wart"));
        REGISTRY.a(116, "enchanting_table", (new BlockEnchantmentTable()).c(5.0F).b(2000.0F).c("enchantmentTable").d("enchanting_table"));
        REGISTRY.a(117, "brewing_stand", (new BlockBrewingStand()).c(0.5F).a(0.125F).c("brewingStand").d("brewing_stand"));
        REGISTRY.a(118, "cauldron", (new BlockCauldron()).c(2.0F).c("cauldron").d("cauldron"));
        REGISTRY.a(119, "end_portal", (new BlockEnderPortal(Material.PORTAL)).c(-1.0F).b(6000000.0F));
        REGISTRY.a(120, "end_portal_frame", (new BlockEnderPortalFrame()).a(k).a(0.125F).c(-1.0F).c("endPortalFrame").b(6000000.0F).a(CreativeModeTab.c).d("endframe"));
        REGISTRY.a(121, "end_stone", (new Block(Material.STONE)).c(3.0F).b(15.0F).a(i).c("whiteStone").a(CreativeModeTab.b).d("end_stone"));
        REGISTRY.a(122, "dragon_egg", (new BlockDragonEgg()).c(3.0F).b(15.0F).a(i).a(0.125F).c("dragonEgg").d("dragon_egg"));
        REGISTRY.a(123, "redstone_lamp", (new BlockRedstoneLamp(false)).c(0.3F).a(k).c("redstoneLight").a(CreativeModeTab.d).d("redstone_lamp_off"));
        REGISTRY.a(124, "lit_redstone_lamp", (new BlockRedstoneLamp(true)).c(0.3F).a(k).c("redstoneLight").d("redstone_lamp_on"));
        REGISTRY.a(125, "double_wooden_slab", (new BlockWoodStep(true)).c(2.0F).b(5.0F).a(f).c("woodSlab"));
        REGISTRY.a(126, "wooden_slab", (new BlockWoodStep(false)).c(2.0F).b(5.0F).a(f).c("woodSlab"));
        REGISTRY.a(127, "cocoa", (new BlockCocoa()).c(0.2F).b(5.0F).a(f).c("cocoa").d("cocoa"));
        REGISTRY.a(128, "sandstone_stairs", (new BlockStairs(block2, 0)).c("stairsSandStone"));
        REGISTRY.a(129, "emerald_ore", (new BlockOre()).c(3.0F).b(5.0F).a(i).c("oreEmerald").d("emerald_ore"));
        REGISTRY.a(130, "ender_chest", (new BlockEnderChest()).c(22.5F).b(1000.0F).a(i).c("enderChest").a(0.5F));
        REGISTRY.a(131, "tripwire_hook", (new BlockTripwireHook()).c("tripWireSource").d("trip_wire_source"));
        REGISTRY.a(132, "tripwire", (new BlockTripwire()).c("tripWire").d("trip_wire"));
        REGISTRY.a(133, "emerald_block", (new BlockOreBlock(MaterialMapColor.I)).c(5.0F).b(10.0F).a(j).c("blockEmerald").d("emerald_block"));
        REGISTRY.a(134, "spruce_stairs", (new BlockStairs(block1, 1)).c("stairsWoodSpruce"));
        REGISTRY.a(135, "birch_stairs", (new BlockStairs(block1, 2)).c("stairsWoodBirch"));
        REGISTRY.a(136, "jungle_stairs", (new BlockStairs(block1, 3)).c("stairsWoodJungle"));
        REGISTRY.a(137, "command_block", (new BlockCommand()).s().b(6000000.0F).c("commandBlock").d("command_block"));
        REGISTRY.a(138, "beacon", (new BlockBeacon()).c("beacon").a(1.0F).d("beacon"));
        REGISTRY.a(139, "cobblestone_wall", (new BlockCobbleWall(block)).c("cobbleWall"));
        REGISTRY.a(140, "flower_pot", (new BlockFlowerPot()).c(0.0F).a(e).c("flowerPot").d("flower_pot"));
        REGISTRY.a(141, "carrots", (new BlockCarrots()).c("carrots").d("carrots"));
        REGISTRY.a(142, "potatoes", (new BlockPotatoes()).c("potatoes").d("potatoes"));
        REGISTRY.a(143, "wooden_button", (new BlockWoodButton()).c(0.5F).a(f).c("button"));
        REGISTRY.a(144, "skull", (new BlockSkull()).c(1.0F).a(i).c("skull").d("skull"));
        REGISTRY.a(145, "anvil", (new BlockAnvil()).c(5.0F).a(p).b(2000.0F).c("anvil"));
        REGISTRY.a(146, "trapped_chest", (new BlockChest(1)).c(2.5F).a(f).c("chestTrap"));
        REGISTRY.a(147, "light_weighted_pressure_plate", (new BlockPressurePlateWeighted("gold_block", Material.ORE, 15)).c(0.5F).a(f).c("weightedPlate_light"));
        REGISTRY.a(148, "heavy_weighted_pressure_plate", (new BlockPressurePlateWeighted("iron_block", Material.ORE, 150)).c(0.5F).a(f).c("weightedPlate_heavy"));
        REGISTRY.a(149, "unpowered_comparator", (new BlockRedstoneComparator(false)).c(0.0F).a(f).c("comparator").H().d("comparator_off"));
        REGISTRY.a(150, "powered_comparator", (new BlockRedstoneComparator(true)).c(0.0F).a(0.625F).a(f).c("comparator").H().d("comparator_on"));
        REGISTRY.a(151, "daylight_detector", (new BlockDaylightDetector()).c(0.2F).a(f).c("daylightDetector").d("daylight_detector"));
        REGISTRY.a(152, "redstone_block", (new BlockRedstone(MaterialMapColor.f)).c(5.0F).b(10.0F).a(j).c("blockRedstone").d("redstone_block"));
        REGISTRY.a(153, "quartz_ore", (new BlockOre()).c(3.0F).b(5.0F).a(i).c("netherquartz").d("quartz_ore"));
        REGISTRY.a(154, "hopper", (new BlockHopper()).c(3.0F).b(8.0F).a(f).c("hopper").d("hopper"));
        Block block9 = (new BlockQuartz()).a(i).c(0.8F).c("quartzBlock").d("quartz_block");

        REGISTRY.a(155, "quartz_block", block9);
        REGISTRY.a(156, "quartz_stairs", (new BlockStairs(block9, 0)).c("stairsQuartz"));
        REGISTRY.a(157, "activator_rail", (new BlockPoweredRail()).c(0.7F).a(j).c("activatorRail").d("rail_activator"));
        REGISTRY.a(158, "dropper", (new BlockDropper()).c(3.5F).a(i).c("dropper").d("dropper"));
        REGISTRY.a(159, "stained_hardened_clay", (new BlockCloth(Material.STONE)).c(1.25F).b(7.0F).a(i).c("clayHardenedStained").d("hardened_clay_stained"));
        REGISTRY.a(160, "stained_glass_pane", (new BlockStainedGlassPane()).c(0.3F).a(k).c("thinStainedGlass").d("glass"));
        REGISTRY.a(161, "leaves2", (new BlockLeaves2()).c("leaves").d("leaves"));
        REGISTRY.a(162, "log2", (new BlockLog2()).c("log").d("log"));
        REGISTRY.a(163, "acacia_stairs", (new BlockStairs(block1, 4)).c("stairsWoodAcacia"));
        REGISTRY.a(164, "dark_oak_stairs", (new BlockStairs(block1, 5)).c("stairsWoodDarkOak"));
        REGISTRY.a(170, "hay_block", (new BlockHay()).c(0.5F).a(h).c("hayBlock").a(CreativeModeTab.b).d("hay_block"));
        REGISTRY.a(171, "carpet", (new BlockCarpet()).c(0.1F).a(l).c("woolCarpet").g(0));
        REGISTRY.a(172, "hardened_clay", (new BlockHardenedClay()).c(1.25F).b(7.0F).a(i).c("clayHardened").d("hardened_clay"));
        REGISTRY.a(173, "coal_block", (new Block(Material.STONE)).c(5.0F).b(10.0F).a(i).c("blockCoal").a(CreativeModeTab.b).d("coal_block"));
        REGISTRY.a(174, "packed_ice", (new BlockPackedIce()).c(0.5F).a(k).c("icePacked").d("ice_packed"));
        REGISTRY.a(175, "double_plant", new BlockTallPlant());
        Iterator iterator = REGISTRY.iterator();

        while (iterator.hasNext()) {
            Block block10 = (Block) iterator.next();

            if (block10.material == Material.AIR) {
                block10.u = false;
            } else {
                boolean flag = false;
                boolean flag1 = block10.b() == 10;
                boolean flag2 = block10 instanceof BlockStepAbstract;
                boolean flag3 = block10 == block4;
                boolean flag4 = block10.s;
                boolean flag5 = block10.r == 0;

                if (flag1 || flag2 || flag3 || flag4 || flag5) {
                    flag = true;
                }

                block10.u = flag;
            }
        }
    }

    protected Block(Material material) {
        this.stepSound = e;
        this.I = 1.0F;
        this.frictionFactor = 0.6F;
        this.material = material;
        this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        this.q = this.c();
        this.r = this.c() ? 255 : 0;
        this.s = !material.blocksLight();
    }

    protected Block a(StepSound stepsound) {
        this.stepSound = stepsound;
        return this;
    }

    protected Block g(int i) {
        this.r = i;
        return this;
    }

    protected Block a(float f) {
        this.t = (int) (15.0F * f);
        return this;
    }

    protected Block b(float f) {
        this.durability = f * 3.0F;
        return this;
    }

    public boolean r() {
        return this.material.k() && this.d() && !this.isPowerSource();
    }

    public boolean d() {
        return true;
    }

    public boolean b(IBlockAccess iblockaccess, int i, int j, int k) {
        return !this.material.isSolid();
    }

    public int b() {
        return 0;
    }

    protected Block c(float f) {
        this.strength = f;
        if (this.durability < f * 5.0F) {
            this.durability = f * 5.0F;
        }

        return this;
    }

    protected Block s() {
        this.c(-1.0F);
        return this;
    }

    public float f(World world, int i, int j, int k) {
        return this.strength;
    }

    protected Block a(boolean flag) {
        this.z = flag;
        return this;
    }

    public boolean isTicking() {
        return this.z;
    }

    public boolean isTileEntity() {
        return this.isTileEntity;
    }

    protected final void a(float f, float f1, float f2, float f3, float f4, float f5) {
        this.minX = (double) f;
        this.minY = (double) f1;
        this.minZ = (double) f2;
        this.maxX = (double) f3;
        this.maxY = (double) f4;
        this.maxZ = (double) f5;
    }

    public boolean d(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        return iblockaccess.getType(i, j, k).getMaterial().isBuildable();
    }

    public void a(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, List list, Entity entity) {
        AxisAlignedBB axisalignedbb1 = this.a(world, i, j, k);

        if (axisalignedbb1 != null && axisalignedbb.b(axisalignedbb1)) {
            list.add(axisalignedbb1);
        }
    }

    public AxisAlignedBB a(World world, int i, int j, int k) {
        return AxisAlignedBB.a().a((double) i + this.minX, (double) j + this.minY, (double) k + this.minZ, (double) i + this.maxX, (double) j + this.maxY, (double) k + this.maxZ);
    }

    public boolean c() {
        return true;
    }

    public boolean a(int i, boolean flag) {
        return this.v();
    }

    public boolean v() {
        return true;
    }

    public void a(World world, int i, int j, int k, Random random) {}

    public void postBreak(World world, int i, int j, int k, int l) {}

    public void doPhysics(World world, int i, int j, int k, Block block) {}

    public int a(World world) {
        return 10;
    }

    public void onPlace(World world, int i, int j, int k) {}

    public void remove(World world, int i, int j, int k, Block block, int l) {}

    public int a(Random random) {
        return 1;
    }

    public Item getDropType(int i, Random random, int j) {
        return Item.getItemOf(this);
    }

    public float getDamage(EntityHuman entityhuman, World world, int i, int j, int k) {
        float f = this.f(world, i, j, k);

        return f < 0.0F ? 0.0F : (!entityhuman.a(this) ? entityhuman.a(this, false) / f / 100.0F : entityhuman.a(this, true) / f / 30.0F);
    }

    public final void b(World world, int i, int j, int k, int l, int i1) {
        this.dropNaturally(world, i, j, k, l, 1.0F, i1);
    }

    public void dropNaturally(World world, int i, int j, int k, int l, float f, int i1) {
        if (!world.isStatic) {
            int j1 = this.getDropCount(i1, world.random);

            for (int k1 = 0; k1 < j1; ++k1) {
                // CraftBukkit - <= to < to allow for plugins to completely disable block drops from explosions
                if (world.random.nextFloat() < f) {
                    Item item = this.getDropType(l, world.random, i1);

                    if (item != null) {
                        this.a(world, i, j, k, new ItemStack(item, 1, this.getDropData(l)));
                    }
                }
            }
        }
    }

    protected void a(World world, int i, int j, int k, ItemStack itemstack) {
        if (!world.isStatic && world.getGameRules().getBoolean("doTileDrops")) {
            float f = 0.7F;
            double d0 = (double) (world.random.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
            double d1 = (double) (world.random.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
            double d2 = (double) (world.random.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
            EntityItem entityitem = new EntityItem(world, (double) i + d0, (double) j + d1, (double) k + d2, itemstack);

            entityitem.pickupDelay = 10;
            world.addEntity(entityitem);
        }
    }

    protected void dropExperience(World world, int i, int j, int k, int l) {
        if (!world.isStatic) {
            while (l > 0) {
                int i1 = EntityExperienceOrb.getOrbValue(l);

                l -= i1;
                world.addEntity(new EntityExperienceOrb(world, (double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, i1));
            }
        }
    }

    public int getDropData(int i) {
        return 0;
    }

    public float a(Entity entity) {
        return this.durability / 5.0F;
    }

    public MovingObjectPosition a(World world, int i, int j, int k, Vec3D vec3d, Vec3D vec3d1) {
        this.updateShape(world, i, j, k);
        vec3d = vec3d.add((double) (-i), (double) (-j), (double) (-k));
        vec3d1 = vec3d1.add((double) (-i), (double) (-j), (double) (-k));
        Vec3D vec3d2 = vec3d.b(vec3d1, this.minX);
        Vec3D vec3d3 = vec3d.b(vec3d1, this.maxX);
        Vec3D vec3d4 = vec3d.c(vec3d1, this.minY);
        Vec3D vec3d5 = vec3d.c(vec3d1, this.maxY);
        Vec3D vec3d6 = vec3d.d(vec3d1, this.minZ);
        Vec3D vec3d7 = vec3d.d(vec3d1, this.maxZ);

        if (!this.a(vec3d2)) {
            vec3d2 = null;
        }

        if (!this.a(vec3d3)) {
            vec3d3 = null;
        }

        if (!this.b(vec3d4)) {
            vec3d4 = null;
        }

        if (!this.b(vec3d5)) {
            vec3d5 = null;
        }

        if (!this.c(vec3d6)) {
            vec3d6 = null;
        }

        if (!this.c(vec3d7)) {
            vec3d7 = null;
        }

        Vec3D vec3d8 = null;

        if (vec3d2 != null && (vec3d8 == null || vec3d.distanceSquared(vec3d2) < vec3d.distanceSquared(vec3d8))) {
            vec3d8 = vec3d2;
        }

        if (vec3d3 != null && (vec3d8 == null || vec3d.distanceSquared(vec3d3) < vec3d.distanceSquared(vec3d8))) {
            vec3d8 = vec3d3;
        }

        if (vec3d4 != null && (vec3d8 == null || vec3d.distanceSquared(vec3d4) < vec3d.distanceSquared(vec3d8))) {
            vec3d8 = vec3d4;
        }

        if (vec3d5 != null && (vec3d8 == null || vec3d.distanceSquared(vec3d5) < vec3d.distanceSquared(vec3d8))) {
            vec3d8 = vec3d5;
        }

        if (vec3d6 != null && (vec3d8 == null || vec3d.distanceSquared(vec3d6) < vec3d.distanceSquared(vec3d8))) {
            vec3d8 = vec3d6;
        }

        if (vec3d7 != null && (vec3d8 == null || vec3d.distanceSquared(vec3d7) < vec3d.distanceSquared(vec3d8))) {
            vec3d8 = vec3d7;
        }

        if (vec3d8 == null) {
            return null;
        } else {
            byte b0 = -1;

            if (vec3d8 == vec3d2) {
                b0 = 4;
            }

            if (vec3d8 == vec3d3) {
                b0 = 5;
            }

            if (vec3d8 == vec3d4) {
                b0 = 0;
            }

            if (vec3d8 == vec3d5) {
                b0 = 1;
            }

            if (vec3d8 == vec3d6) {
                b0 = 2;
            }

            if (vec3d8 == vec3d7) {
                b0 = 3;
            }

            return new MovingObjectPosition(i, j, k, b0, vec3d8.add((double) i, (double) j, (double) k));
        }
    }

    private boolean a(Vec3D vec3d) {
        return vec3d == null ? false : vec3d.d >= this.minY && vec3d.d <= this.maxY && vec3d.e >= this.minZ && vec3d.e <= this.maxZ;
    }

    private boolean b(Vec3D vec3d) {
        return vec3d == null ? false : vec3d.c >= this.minX && vec3d.c <= this.maxX && vec3d.e >= this.minZ && vec3d.e <= this.maxZ;
    }

    private boolean c(Vec3D vec3d) {
        return vec3d == null ? false : vec3d.c >= this.minX && vec3d.c <= this.maxX && vec3d.d >= this.minY && vec3d.d <= this.maxY;
    }

    public void wasExploded(World world, int i, int j, int k, Explosion explosion) {}

    public boolean canPlace(World world, int i, int j, int k, int l, ItemStack itemstack) {
        return this.canPlace(world, i, j, k, l);
    }

    public boolean canPlace(World world, int i, int j, int k, int l) {
        return this.canPlace(world, i, j, k);
    }

    public boolean canPlace(World world, int i, int j, int k) {
        return world.getType(i, j, k).material.isReplaceable();
    }

    public boolean interact(World world, int i, int j, int k, EntityHuman entityhuman, int l, float f, float f1, float f2) {
        return false;
    }

    public void b(World world, int i, int j, int k, Entity entity) {}

    public int getPlacedData(World world, int i, int j, int k, int l, float f, float f1, float f2, int i1) {
        return i1;
    }

    public void attack(World world, int i, int j, int k, EntityHuman entityhuman) {}

    public void a(World world, int i, int j, int k, Entity entity, Vec3D vec3d) {}

    public void updateShape(IBlockAccess iblockaccess, int i, int j, int k) {}

    public final double x() {
        return this.minX;
    }

    public final double y() {
        return this.maxX;
    }

    public final double z() {
        return this.minY;
    }

    public final double A() {
        return this.maxY;
    }

    public final double B() {
        return this.minZ;
    }

    public final double C() {
        return this.maxZ;
    }

    public int b(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        return 0;
    }

    public boolean isPowerSource() {
        return false;
    }

    public void a(World world, int i, int j, int k, Entity entity) {}

    public int c(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        return 0;
    }

    public void g() {}

    public void a(World world, EntityHuman entityhuman, int i, int j, int k, int l) {
        entityhuman.a(StatisticList.C[b(this)], 1);
        entityhuman.a(0.025F);
        if (this.E() && EnchantmentManager.hasSilkTouchEnchantment(entityhuman)) {
            ItemStack itemstack = this.j(l);

            if (itemstack != null) {
                this.a(world, i, j, k, itemstack);
            }
        } else {
            int i1 = EnchantmentManager.getBonusBlockLootEnchantmentLevel(entityhuman);

            this.b(world, i, j, k, l, i1);
        }
    }

    protected boolean E() {
        return this.d() && !this.isTileEntity;
    }

    protected ItemStack j(int i) {
        int j = 0;
        Item item = Item.getItemOf(this);

        if (item != null && item.n()) {
            j = i;
        }

        return new ItemStack(item, 1, j);
    }

    public int getDropCount(int i, Random random) {
        return this.a(random);
    }

    public boolean j(World world, int i, int j, int k) {
        return true;
    }

    public void postPlace(World world, int i, int j, int k, EntityLiving entityliving, ItemStack itemstack) {}

    public void postPlace(World world, int i, int j, int k, int l) {}

    public Block c(String s) {
        this.name = s;
        return this;
    }

    public String getName() {
        return LocaleI18n.get(this.a() + ".name");
    }

    public String a() {
        return "tile." + this.name;
    }

    public boolean a(World world, int i, int j, int k, int l, int i1) {
        return false;
    }

    public boolean G() {
        return this.y;
    }

    protected Block H() {
        this.y = false;
        return this;
    }

    public int h() {
        return this.material.getPushReaction();
    }

    public void a(World world, int i, int j, int k, Entity entity, float f) {}

    public int getDropData(World world, int i, int j, int k) {
        return this.getDropData(world.getData(i, j, k));
    }

    public Block a(CreativeModeTab creativemodetab) {
        this.creativeTab = creativemodetab;
        return this;
    }

    public void a(World world, int i, int j, int k, int l, EntityHuman entityhuman) {}

    public void f(World world, int i, int j, int k, int l) {}

    public void l(World world, int i, int j, int k) {}

    public boolean L() {
        return true;
    }

    public boolean a(Explosion explosion) {
        return true;
    }

    public boolean c(Block block) {
        return this == block;
    }

    public static boolean a(Block block, Block block1) {
        return block != null && block1 != null ? (block == block1 ? true : block.c(block1)) : false;
    }

    public boolean M() {
        return false;
    }

    public int g(World world, int i, int j, int k, int l) {
        return 0;
    }

    protected Block d(String s) {
        this.d = s;
        return this;
    }

    // CraftBukkit start
    public int getExpDrop(World world, int data, int enchantmentLevel) {
        return 0;
    }
    // CraftBukkit end
}
