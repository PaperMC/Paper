package net.minecraft.server;

import java.util.List;
import java.util.Random;

public class Block {

    private CreativeModeTab creativeTab;
    protected String f;
    public static final StepSound g = new StepSound("stone", 1.0F, 1.0F);
    public static final StepSound h = new StepSound("wood", 1.0F, 1.0F);
    public static final StepSound i = new StepSound("gravel", 1.0F, 1.0F);
    public static final StepSound j = new StepSound("grass", 1.0F, 1.0F);
    public static final StepSound k = new StepSound("stone", 1.0F, 1.0F);
    public static final StepSound l = new StepSound("stone", 1.0F, 1.5F);
    public static final StepSound m = new StepSoundStone("stone", 1.0F, 1.0F);
    public static final StepSound n = new StepSound("cloth", 1.0F, 1.0F);
    public static final StepSound o = new StepSound("sand", 1.0F, 1.0F);
    public static final StepSound p = new StepSound("snow", 1.0F, 1.0F);
    public static final StepSound q = new StepSoundLadder("ladder", 1.0F, 1.0F);
    public static final StepSound r = new StepSoundAnvil("anvil", 0.3F, 1.0F);
    public static final Block[] byId = new Block[4096];
    public static final boolean[] t = new boolean[4096];
    public static final int[] lightBlock = new int[4096];
    public static final boolean[] v = new boolean[4096];
    public static final int[] lightEmission = new int[4096];
    public static boolean[] x = new boolean[4096];
    public static final Block STONE = (new BlockStone(1)).c(1.5F).b(10.0F).a(k).c("stone").d("stone");
    public static final BlockGrass GRASS = (BlockGrass) (new BlockGrass(2)).c(0.6F).a(j).c("grass").d("grass");
    public static final Block DIRT = (new BlockDirt(3)).c(0.5F).a(i).c("dirt").d("dirt");
    public static final Block COBBLESTONE = (new Block(4, Material.STONE)).c(2.0F).b(10.0F).a(k).c("stonebrick").a(CreativeModeTab.b).d("cobblestone");
    public static final Block WOOD = (new BlockWood(5)).c(2.0F).b(5.0F).a(h).c("wood").d("planks");
    public static final Block SAPLING = (new BlockSapling(6)).c(0.0F).a(j).c("sapling").d("sapling");
    public static final Block BEDROCK = (new Block(7, Material.STONE)).r().b(6000000.0F).a(k).c("bedrock").C().a(CreativeModeTab.b).d("bedrock");
    public static final BlockFluids WATER = (BlockFluids) (new BlockFlowing(8, Material.WATER)).c(100.0F).k(3).c("water").C().d("water_flow");
    public static final Block STATIONARY_WATER = (new BlockStationary(9, Material.WATER)).c(100.0F).k(3).c("water").C().d("water_still");
    public static final BlockFluids LAVA = (BlockFluids) (new BlockFlowing(10, Material.LAVA)).c(0.0F).a(1.0F).c("lava").C().d("lava_flow");
    public static final Block STATIONARY_LAVA = (new BlockStationary(11, Material.LAVA)).c(100.0F).a(1.0F).c("lava").C().d("lava_still");
    public static final Block SAND = (new BlockSand(12)).c(0.5F).a(o).c("sand").d("sand");
    public static final Block GRAVEL = (new BlockGravel(13)).c(0.6F).a(i).c("gravel").d("gravel");
    public static final Block GOLD_ORE = (new BlockOre(14)).c(3.0F).b(5.0F).a(k).c("oreGold").d("gold_ore");
    public static final Block IRON_ORE = (new BlockOre(15)).c(3.0F).b(5.0F).a(k).c("oreIron").d("iron_ore");
    public static final Block COAL_ORE = (new BlockOre(16)).c(3.0F).b(5.0F).a(k).c("oreCoal").d("coal_ore");
    public static final Block LOG = (new BlockLog(17)).c(2.0F).a(h).c("log").d("log");
    public static final BlockLeaves LEAVES = (BlockLeaves) (new BlockLeaves(18)).c(0.2F).k(1).a(j).c("leaves").d("leaves");
    public static final Block SPONGE = (new BlockSponge(19)).c(0.6F).a(j).c("sponge").d("sponge");
    public static final Block GLASS = (new BlockGlass(20, Material.SHATTERABLE, false)).c(0.3F).a(m).c("glass").d("glass");
    public static final Block LAPIS_ORE = (new BlockOre(21)).c(3.0F).b(5.0F).a(k).c("oreLapis").d("lapis_ore");
    public static final Block LAPIS_BLOCK = (new Block(22, Material.STONE)).c(3.0F).b(5.0F).a(k).c("blockLapis").a(CreativeModeTab.b).d("lapis_block");
    public static final Block DISPENSER = (new BlockDispenser(23)).c(3.5F).a(k).c("dispenser").d("dispenser");
    public static final Block SANDSTONE = (new BlockSandStone(24)).a(k).c(0.8F).c("sandStone").d("sandstone");
    public static final Block NOTE_BLOCK = (new BlockNote(25)).c(0.8F).c("musicBlock").d("noteblock");
    public static final Block BED = (new BlockBed(26)).c(0.2F).c("bed").C().d("bed");
    public static final Block GOLDEN_RAIL = (new BlockPoweredRail(27)).c(0.7F).a(l).c("goldenRail").d("rail_golden");
    public static final Block DETECTOR_RAIL = (new BlockMinecartDetector(28)).c(0.7F).a(l).c("detectorRail").d("rail_detector");
    public static final BlockPiston PISTON_STICKY = (BlockPiston) (new BlockPiston(29, true)).c("pistonStickyBase");
    public static final Block WEB = (new BlockWeb(30)).k(1).c(4.0F).c("web").d("web");
    public static final BlockLongGrass LONG_GRASS = (BlockLongGrass) (new BlockLongGrass(31)).c(0.0F).a(j).c("tallgrass");
    public static final BlockDeadBush DEAD_BUSH = (BlockDeadBush) (new BlockDeadBush(32)).c(0.0F).a(j).c("deadbush").d("deadbush");
    public static final BlockPiston PISTON = (BlockPiston) (new BlockPiston(33, false)).c("pistonBase");
    public static final BlockPistonExtension PISTON_EXTENSION = new BlockPistonExtension(34);
    public static final Block WOOL = (new BlockCloth(35, Material.CLOTH)).c(0.8F).a(n).c("cloth").d("wool_colored");
    public static final BlockPistonMoving PISTON_MOVING = new BlockPistonMoving(36);
    public static final BlockFlower YELLOW_FLOWER = (BlockFlower) (new BlockFlower(37)).c(0.0F).a(j).c("flower").d("flower_dandelion");
    public static final BlockFlower RED_ROSE = (BlockFlower) (new BlockFlower(38)).c(0.0F).a(j).c("rose").d("flower_rose");
    public static final BlockFlower BROWN_MUSHROOM = (BlockFlower) (new BlockMushroom(39)).c(0.0F).a(j).a(0.125F).c("mushroom").d("mushroom_brown");
    public static final BlockFlower RED_MUSHROOM = (BlockFlower) (new BlockMushroom(40)).c(0.0F).a(j).c("mushroom").d("mushroom_red");
    public static final Block GOLD_BLOCK = (new BlockOreBlock(41)).c(3.0F).b(10.0F).a(l).c("blockGold").d("gold_block");
    public static final Block IRON_BLOCK = (new BlockOreBlock(42)).c(5.0F).b(10.0F).a(l).c("blockIron").d("iron_block");
    public static final BlockStepAbstract DOUBLE_STEP = (BlockStepAbstract) (new BlockStep(43, true)).c(2.0F).b(10.0F).a(k).c("stoneSlab");
    public static final BlockStepAbstract STEP = (BlockStepAbstract) (new BlockStep(44, false)).c(2.0F).b(10.0F).a(k).c("stoneSlab");
    public static final Block BRICK = (new Block(45, Material.STONE)).c(2.0F).b(10.0F).a(k).c("brick").a(CreativeModeTab.b).d("brick");
    public static final Block TNT = (new BlockTNT(46)).c(0.0F).a(j).c("tnt").d("tnt");
    public static final Block BOOKSHELF = (new BlockBookshelf(47)).c(1.5F).a(h).c("bookshelf").d("bookshelf");
    public static final Block MOSSY_COBBLESTONE = (new Block(48, Material.STONE)).c(2.0F).b(10.0F).a(k).c("stoneMoss").a(CreativeModeTab.b).d("cobblestone_mossy");
    public static final Block OBSIDIAN = (new BlockObsidian(49)).c(50.0F).b(2000.0F).a(k).c("obsidian").d("obsidian");
    public static final Block TORCH = (new BlockTorch(50)).c(0.0F).a(0.9375F).a(h).c("torch").d("torch_on");
    public static final BlockFire FIRE = (BlockFire) (new BlockFire(51)).c(0.0F).a(1.0F).a(h).c("fire").C().d("fire");
    public static final Block MOB_SPAWNER = (new BlockMobSpawner(52)).c(5.0F).a(l).c("mobSpawner").C().d("mob_spawner");
    public static final Block WOOD_STAIRS = (new BlockStairs(53, WOOD, 0)).c("stairsWood");
    public static final BlockChest CHEST = (BlockChest) (new BlockChest(54, 0)).c(2.5F).a(h).c("chest");
    public static final BlockRedstoneWire REDSTONE_WIRE = (BlockRedstoneWire) (new BlockRedstoneWire(55)).c(0.0F).a(g).c("redstoneDust").C().d("redstone_dust");
    public static final Block DIAMOND_ORE = (new BlockOre(56)).c(3.0F).b(5.0F).a(k).c("oreDiamond").d("diamond_ore");
    public static final Block DIAMOND_BLOCK = (new BlockOreBlock(57)).c(5.0F).b(10.0F).a(l).c("blockDiamond").d("diamond_block");
    public static final Block WORKBENCH = (new BlockWorkbench(58)).c(2.5F).a(h).c("workbench").d("crafting_table");
    public static final Block CROPS = (new BlockCrops(59)).c("crops").d("wheat");
    public static final Block SOIL = (new BlockSoil(60)).c(0.6F).a(i).c("farmland").d("farmland");
    public static final Block FURNACE = (new BlockFurnace(61, false)).c(3.5F).a(k).c("furnace").a(CreativeModeTab.c);
    public static final Block BURNING_FURNACE = (new BlockFurnace(62, true)).c(3.5F).a(k).a(0.875F).c("furnace");
    public static final Block SIGN_POST = (new BlockSign(63, TileEntitySign.class, true)).c(1.0F).a(h).c("sign").C();
    public static final Block WOODEN_DOOR = (new BlockDoor(64, Material.WOOD)).c(3.0F).a(h).c("doorWood").C().d("door_wood");
    public static final Block LADDER = (new BlockLadder(65)).c(0.4F).a(q).c("ladder").d("ladder");
    public static final Block RAILS = (new BlockMinecartTrack(66)).c(0.7F).a(l).c("rail").d("rail_normal");
    public static final Block COBBLESTONE_STAIRS = (new BlockStairs(67, COBBLESTONE, 0)).c("stairsStone");
    public static final Block WALL_SIGN = (new BlockSign(68, TileEntitySign.class, false)).c(1.0F).a(h).c("sign").C();
    public static final Block LEVER = (new BlockLever(69)).c(0.5F).a(h).c("lever").d("lever");
    public static final Block STONE_PLATE = (new BlockPressurePlateBinary(70, "stone", Material.STONE, EnumMobType.MOBS)).c(0.5F).a(k).c("pressurePlate");
    public static final Block IRON_DOOR_BLOCK = (new BlockDoor(71, Material.ORE)).c(5.0F).a(l).c("doorIron").C().d("door_iron");
    public static final Block WOOD_PLATE = (new BlockPressurePlateBinary(72, "planks_oak", Material.WOOD, EnumMobType.EVERYTHING)).c(0.5F).a(h).c("pressurePlate");
    public static final Block REDSTONE_ORE = (new BlockRedstoneOre(73, false)).c(3.0F).b(5.0F).a(k).c("oreRedstone").a(CreativeModeTab.b).d("redstone_ore");
    public static final Block GLOWING_REDSTONE_ORE = (new BlockRedstoneOre(74, true)).a(0.625F).c(3.0F).b(5.0F).a(k).c("oreRedstone").d("redstone_ore");
    public static final Block REDSTONE_TORCH_OFF = (new BlockRedstoneTorch(75, false)).c(0.0F).a(h).c("notGate").d("redstone_torch_off");
    public static final Block REDSTONE_TORCH_ON = (new BlockRedstoneTorch(76, true)).c(0.0F).a(0.5F).a(h).c("notGate").a(CreativeModeTab.d).d("redstone_torch_on");
    public static final Block STONE_BUTTON = (new BlockStoneButton(77)).c(0.5F).a(k).c("button");
    public static final Block SNOW = (new BlockSnow(78)).c(0.1F).a(p).c("snow").k(0).d("snow");
    public static final Block ICE = (new BlockIce(79)).c(0.5F).k(3).a(m).c("ice").d("ice");
    public static final Block SNOW_BLOCK = (new BlockSnowBlock(80)).c(0.2F).a(p).c("snow").d("snow");
    public static final Block CACTUS = (new BlockCactus(81)).c(0.4F).a(n).c("cactus").d("cactus");
    public static final Block CLAY = (new BlockClay(82)).c(0.6F).a(i).c("clay").d("clay");
    public static final Block SUGAR_CANE_BLOCK = (new BlockReed(83)).c(0.0F).a(j).c("reeds").C().d("reeds");
    public static final Block JUKEBOX = (new BlockJukeBox(84)).c(2.0F).b(10.0F).a(k).c("jukebox").d("jukebox");
    public static final Block FENCE = (new BlockFence(85, "planks_oak", Material.WOOD)).c(2.0F).b(5.0F).a(h).c("fence");
    public static final Block PUMPKIN = (new BlockPumpkin(86, false)).c(1.0F).a(h).c("pumpkin").d("pumpkin");
    public static final Block NETHERRACK = (new BlockBloodStone(87)).c(0.4F).a(k).c("hellrock").d("netherrack");
    public static final Block SOUL_SAND = (new BlockSlowSand(88)).c(0.5F).a(o).c("hellsand").d("soul_sand");
    public static final Block GLOWSTONE = (new BlockLightStone(89, Material.SHATTERABLE)).c(0.3F).a(m).a(1.0F).c("lightgem").d("glowstone");
    public static final BlockPortal PORTAL = (BlockPortal) (new BlockPortal(90)).c(-1.0F).a(m).a(0.75F).c("portal").d("portal");
    public static final Block JACK_O_LANTERN = (new BlockPumpkin(91, true)).c(1.0F).a(h).a(1.0F).c("litpumpkin").d("pumpkin");
    public static final Block CAKE_BLOCK = (new BlockCake(92)).c(0.5F).a(n).c("cake").C().d("cake");
    public static final BlockRepeater DIODE_OFF = (BlockRepeater) (new BlockRepeater(93, false)).c(0.0F).a(h).c("diode").C().d("repeater_off");
    public static final BlockRepeater DIODE_ON = (BlockRepeater) (new BlockRepeater(94, true)).c(0.0F).a(0.625F).a(h).c("diode").C().d("repeater_on");
    public static final Block LOCKED_CHEST = (new BlockLockedChest(95)).c(0.0F).a(1.0F).a(h).c("lockedchest").b(true);
    public static final Block TRAP_DOOR = (new BlockTrapdoor(96, Material.WOOD)).c(3.0F).a(h).c("trapdoor").C().d("trapdoor");
    public static final Block MONSTER_EGGS = (new BlockMonsterEggs(97)).c(0.75F).c("monsterStoneEgg");
    public static final Block SMOOTH_BRICK = (new BlockSmoothBrick(98)).c(1.5F).b(10.0F).a(k).c("stonebricksmooth").d("stonebrick");
    public static final Block BIG_MUSHROOM_1 = (new BlockHugeMushroom(99, Material.WOOD, 0)).c(0.2F).a(h).c("mushroom").d("mushroom_block");
    public static final Block BIG_MUSHROOM_2 = (new BlockHugeMushroom(100, Material.WOOD, 1)).c(0.2F).a(h).c("mushroom").d("mushroom_block");
    public static final Block IRON_FENCE = (new BlockThinFence(101, "iron_bars", "iron_bars", Material.ORE, true)).c(5.0F).b(10.0F).a(l).c("fenceIron");
    public static final Block THIN_GLASS = (new BlockThinFence(102, "glass", "glass_pane_top", Material.SHATTERABLE, false)).c(0.3F).a(m).c("thinGlass");
    public static final Block MELON = (new BlockMelon(103)).c(1.0F).a(h).c("melon").d("melon");
    public static final Block PUMPKIN_STEM = (new BlockStem(104, PUMPKIN)).c(0.0F).a(h).c("pumpkinStem").d("pumpkin_stem");
    public static final Block MELON_STEM = (new BlockStem(105, MELON)).c(0.0F).a(h).c("pumpkinStem").d("melon_stem");
    public static final Block VINE = (new BlockVine(106)).c(0.2F).a(j).c("vine").d("vine");
    public static final Block FENCE_GATE = (new BlockFenceGate(107)).c(2.0F).b(5.0F).a(h).c("fenceGate");
    public static final Block BRICK_STAIRS = (new BlockStairs(108, BRICK, 0)).c("stairsBrick");
    public static final Block STONE_STAIRS = (new BlockStairs(109, SMOOTH_BRICK, 0)).c("stairsStoneBrickSmooth");
    public static final BlockMycel MYCEL = (BlockMycel) (new BlockMycel(110)).c(0.6F).a(j).c("mycel").d("mycelium");
    public static final Block WATER_LILY = (new BlockWaterLily(111)).c(0.0F).a(j).c("waterlily").d("waterlily");
    public static final Block NETHER_BRICK = (new Block(112, Material.STONE)).c(2.0F).b(10.0F).a(k).c("netherBrick").a(CreativeModeTab.b).d("nether_brick");
    public static final Block NETHER_FENCE = (new BlockFence(113, "nether_brick", Material.STONE)).c(2.0F).b(10.0F).a(k).c("netherFence");
    public static final Block NETHER_BRICK_STAIRS = (new BlockStairs(114, NETHER_BRICK, 0)).c("stairsNetherBrick");
    public static final Block NETHER_WART = (new BlockNetherWart(115)).c("netherStalk").d("nether_wart");
    public static final Block ENCHANTMENT_TABLE = (new BlockEnchantmentTable(116)).c(5.0F).b(2000.0F).c("enchantmentTable").d("enchanting_table");
    public static final Block BREWING_STAND = (new BlockBrewingStand(117)).c(0.5F).a(0.125F).c("brewingStand").d("brewing_stand");
    public static final BlockCauldron CAULDRON = (BlockCauldron) (new BlockCauldron(118)).c(2.0F).c("cauldron").d("cauldron");
    public static final Block ENDER_PORTAL = (new BlockEnderPortal(119, Material.PORTAL)).c(-1.0F).b(6000000.0F);
    public static final Block ENDER_PORTAL_FRAME = (new BlockEnderPortalFrame(120)).a(m).a(0.125F).c(-1.0F).c("endPortalFrame").b(6000000.0F).a(CreativeModeTab.c).d("endframe");
    public static final Block WHITESTONE = (new Block(121, Material.STONE)).c(3.0F).b(15.0F).a(k).c("whiteStone").a(CreativeModeTab.b).d("end_stone");
    public static final Block DRAGON_EGG = (new BlockDragonEgg(122)).c(3.0F).b(15.0F).a(k).a(0.125F).c("dragonEgg").d("dragon_egg");
    public static final Block REDSTONE_LAMP_OFF = (new BlockRedstoneLamp(123, false)).c(0.3F).a(m).c("redstoneLight").a(CreativeModeTab.d).d("redstone_lamp_off");
    public static final Block REDSTONE_LAMP_ON = (new BlockRedstoneLamp(124, true)).c(0.3F).a(m).c("redstoneLight").d("redstone_lamp_on");
    public static final BlockStepAbstract WOOD_DOUBLE_STEP = (BlockStepAbstract) (new BlockWoodStep(125, true)).c(2.0F).b(5.0F).a(h).c("woodSlab");
    public static final BlockStepAbstract WOOD_STEP = (BlockStepAbstract) (new BlockWoodStep(126, false)).c(2.0F).b(5.0F).a(h).c("woodSlab");
    public static final Block COCOA = (new BlockCocoa(127)).c(0.2F).b(5.0F).a(h).c("cocoa").d("cocoa");
    public static final Block SANDSTONE_STAIRS = (new BlockStairs(128, SANDSTONE, 0)).c("stairsSandStone");
    public static final Block EMERALD_ORE = (new BlockOre(129)).c(3.0F).b(5.0F).a(k).c("oreEmerald").d("emerald_ore");
    public static final Block ENDER_CHEST = (new BlockEnderChest(130)).c(22.5F).b(1000.0F).a(k).c("enderChest").a(0.5F);
    public static final BlockTripwireHook TRIPWIRE_SOURCE = (BlockTripwireHook) (new BlockTripwireHook(131)).c("tripWireSource").d("trip_wire_source");
    public static final Block TRIPWIRE = (new BlockTripwire(132)).c("tripWire").d("trip_wire");
    public static final Block EMERALD_BLOCK = (new BlockOreBlock(133)).c(5.0F).b(10.0F).a(l).c("blockEmerald").d("emerald_block");
    public static final Block SPRUCE_WOOD_STAIRS = (new BlockStairs(134, WOOD, 1)).c("stairsWoodSpruce");
    public static final Block BIRCH_WOOD_STAIRS = (new BlockStairs(135, WOOD, 2)).c("stairsWoodBirch");
    public static final Block JUNGLE_WOOD_STAIRS = (new BlockStairs(136, WOOD, 3)).c("stairsWoodJungle");
    public static final Block COMMAND = (new BlockCommand(137)).r().b(6000000.0F).c("commandBlock").d("command_block");
    public static final BlockBeacon BEACON = (BlockBeacon) (new BlockBeacon(138)).c("beacon").a(1.0F).d("beacon");
    public static final Block COBBLE_WALL = (new BlockCobbleWall(139, COBBLESTONE)).c("cobbleWall");
    public static final Block FLOWER_POT = (new BlockFlowerPot(140)).c(0.0F).a(g).c("flowerPot").d("flower_pot");
    public static final Block CARROTS = (new BlockCarrots(141)).c("carrots").d("carrots");
    public static final Block POTATOES = (new BlockPotatoes(142)).c("potatoes").d("potatoes");
    public static final Block WOOD_BUTTON = (new BlockWoodButton(143)).c(0.5F).a(h).c("button");
    public static final Block SKULL = (new BlockSkull(144)).c(1.0F).a(k).c("skull").d("skull");
    public static final Block ANVIL = (new BlockAnvil(145)).c(5.0F).a(r).b(2000.0F).c("anvil");
    public static final Block TRAPPED_CHEST = (new BlockChest(146, 1)).c(2.5F).a(h).c("chestTrap");
    public static final Block GOLD_PLATE = (new BlockPressurePlateWeighted(147, "gold_block", Material.ORE, 64)).c(0.5F).a(h).c("weightedPlate_light");
    public static final Block IRON_PLATE = (new BlockPressurePlateWeighted(148, "iron_block", Material.ORE, 640)).c(0.5F).a(h).c("weightedPlate_heavy");
    public static final BlockRedstoneComparator REDSTONE_COMPARATOR_OFF = (BlockRedstoneComparator) (new BlockRedstoneComparator(149, false)).c(0.0F).a(h).c("comparator").C().d("comparator_off");
    public static final BlockRedstoneComparator REDSTONE_COMPARATOR_ON = (BlockRedstoneComparator) (new BlockRedstoneComparator(150, true)).c(0.0F).a(0.625F).a(h).c("comparator").C().d("comparator_on");
    public static final BlockDaylightDetector DAYLIGHT_DETECTOR = (BlockDaylightDetector) (new BlockDaylightDetector(151)).c(0.2F).a(h).c("daylightDetector").d("daylight_detector");
    public static final Block REDSTONE_BLOCK = (new BlockRedstone(152)).c(5.0F).b(10.0F).a(l).c("blockRedstone").d("redstone_block");
    public static final Block QUARTZ_ORE = (new BlockOre(153)).c(3.0F).b(5.0F).a(k).c("netherquartz").d("quartz_ore");
    public static final BlockHopper HOPPER = (BlockHopper) (new BlockHopper(154)).c(3.0F).b(8.0F).a(h).c("hopper").d("hopper");
    public static final Block QUARTZ_BLOCK = (new BlockQuartz(155)).a(k).c(0.8F).c("quartzBlock").d("quartz_block");
    public static final Block QUARTZ_STAIRS = (new BlockStairs(156, QUARTZ_BLOCK, 0)).c("stairsQuartz");
    public static final Block ACTIVATOR_RAIL = (new BlockPoweredRail(157)).c(0.7F).a(l).c("activatorRail").d("rail_activator");
    public static final Block DROPPER = (new BlockDropper(158)).c(3.5F).a(k).c("dropper").d("dropper");
    public static final Block STAINED_HARDENED_CLAY = (new BlockCloth(159, Material.STONE)).c(1.25F).b(7.0F).a(k).c("clayHardenedStained").d("hardened_clay_stained");
    public static final Block HAY_BLOCK = (new BlockHay(170)).c(0.5F).a(j).c("hayBlock").a(CreativeModeTab.b).d("hay_block");
    public static final Block WOOL_CARPET = (new BlockCarpet(171)).c(0.1F).a(n).c("woolCarpet").k(0);
    public static final Block HARDENED_CLAY = (new Block(172, Material.STONE)).c(1.25F).b(7.0F).a(k).c("clayHardened").a(CreativeModeTab.b).d("hardened_clay");
    public static final Block COAL_BLOCK = (new Block(173, Material.STONE)).c(5.0F).b(10.0F).a(k).c("blockCoal").a(CreativeModeTab.b).d("coal_block");
    public final int id;
    protected float strength;
    protected float durability;
    protected boolean cI = true;
    protected boolean cJ = true;
    protected boolean cK;
    protected boolean isTileEntity;
    protected double minX;
    protected double minY;
    protected double minZ;
    protected double maxX;
    protected double maxY;
    protected double maxZ;
    public StepSound stepSound;
    public float cT;
    public final Material material;
    public float frictionFactor;
    private String name;

    protected Block(int i, Material material) {
        this.stepSound = g;
        this.cT = 1.0F;
        this.frictionFactor = 0.6F;
        if (byId[i] != null) {
            throw new IllegalArgumentException("Slot " + i + " is already occupied by " + byId[i] + " when adding " + this);
        } else {
            this.material = material;
            byId[i] = this;
            this.id = i;
            this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            t[i] = this.c();
            lightBlock[i] = this.c() ? 255 : 0;
            v[i] = !material.blocksLight();
        }
    }

    protected void s_() {}

    protected Block a(StepSound stepsound) {
        this.stepSound = stepsound;
        return this;
    }

    protected Block k(int i) {
        lightBlock[this.id] = i;
        return this;
    }

    protected Block a(float f) {
        lightEmission[this.id] = (int) (15.0F * f);
        return this;
    }

    protected Block b(float f) {
        this.durability = f * 3.0F;
        return this;
    }

    public static boolean l(int i) {
        Block block = byId[i];

        return block == null ? false : block.material.k() && block.b() && !block.isPowerSource();
    }

    public boolean b() {
        return true;
    }

    public boolean b(IBlockAccess iblockaccess, int i, int j, int k) {
        return !this.material.isSolid();
    }

    public int d() {
        return 0;
    }

    protected Block c(float f) {
        this.strength = f;
        if (this.durability < f * 5.0F) {
            this.durability = f * 5.0F;
        }

        return this;
    }

    protected Block r() {
        this.c(-1.0F);
        return this;
    }

    public float l(World world, int i, int j, int k) {
        return this.strength;
    }

    protected Block b(boolean flag) {
        this.cK = flag;
        return this;
    }

    public boolean isTicking() {
        return this.cK;
    }

    public boolean t() {
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

    public boolean a_(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        return iblockaccess.getMaterial(i, j, k).isBuildable();
    }

    public void a(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, List list, Entity entity) {
        AxisAlignedBB axisalignedbb1 = this.b(world, i, j, k);

        if (axisalignedbb1 != null && axisalignedbb.b(axisalignedbb1)) {
            list.add(axisalignedbb1);
        }
    }

    public AxisAlignedBB b(World world, int i, int j, int k) {
        return AxisAlignedBB.a().a((double) i + this.minX, (double) j + this.minY, (double) k + this.minZ, (double) i + this.maxX, (double) j + this.maxY, (double) k + this.maxZ);
    }

    public boolean c() {
        return true;
    }

    public boolean a(int i, boolean flag) {
        return this.m();
    }

    public boolean m() {
        return true;
    }

    public void a(World world, int i, int j, int k, Random random) {}

    public void postBreak(World world, int i, int j, int k, int l) {}

    public void doPhysics(World world, int i, int j, int k, int l) {}

    public int a(World world) {
        return 10;
    }

    public void onPlace(World world, int i, int j, int k) {}

    public void remove(World world, int i, int j, int k, int l, int i1) {}

    public int a(Random random) {
        return 1;
    }

    public int getDropType(int i, Random random, int j) {
        return this.id;
    }

    public float getDamage(EntityHuman entityhuman, World world, int i, int j, int k) {
        float f = this.l(world, i, j, k);

        return f < 0.0F ? 0.0F : (!entityhuman.a(this) ? entityhuman.a(this, false) / f / 100.0F : entityhuman.a(this, true) / f / 30.0F);
    }

    public final void c(World world, int i, int j, int k, int l, int i1) {
        this.dropNaturally(world, i, j, k, l, 1.0F, i1);
    }

    public void dropNaturally(World world, int i, int j, int k, int l, float f, int i1) {
        if (!world.isStatic) {
            int j1 = this.getDropCount(i1, world.random);

            for (int k1 = 0; k1 < j1; ++k1) {
                // CraftBukkit - <= to < to allow for plugins to completely disable block drops from explosions
                if (world.random.nextFloat() < f) {
                    int l1 = this.getDropType(l, world.random, i1);

                    if (l1 > 0) {
                        this.b(world, i, j, k, new ItemStack(l1, 1, this.getDropData(l)));
                    }
                }
            }
        }
    }

    protected void b(World world, int i, int j, int k, ItemStack itemstack) {
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

    protected void j(World world, int i, int j, int k, int l) {
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
        int l = world.getTypeId(i, j, k);

        return l == 0 || byId[l].material.isReplaceable();
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

    public final double u() {
        return this.minX;
    }

    public final double v() {
        return this.maxX;
    }

    public final double w() {
        return this.minY;
    }

    public final double x() {
        return this.maxY;
    }

    public final double y() {
        return this.minZ;
    }

    public final double z() {
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
        entityhuman.a(StatisticList.C[this.id], 1);
        entityhuman.a(0.025F);
        if (this.r_() && EnchantmentManager.hasSilkTouchEnchantment(entityhuman)) {
            ItemStack itemstack = this.d_(l);

            if (itemstack != null) {
                this.b(world, i, j, k, itemstack);
            }
        } else {
            int i1 = EnchantmentManager.getBonusBlockLootEnchantmentLevel(entityhuman);

            this.c(world, i, j, k, l, i1);
        }
    }

    protected boolean r_() {
        return this.b() && !this.isTileEntity;
    }

    protected ItemStack d_(int i) {
        int j = 0;

        if (this.id >= 0 && this.id < Item.byId.length && Item.byId[this.id].n()) {
            j = i;
        }

        return new ItemStack(this.id, 1, j);
    }

    public int getDropCount(int i, Random random) {
        return this.a(random);
    }

    public boolean f(World world, int i, int j, int k) {
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

    public boolean b(World world, int i, int j, int k, int l, int i1) {
        return false;
    }

    public boolean B() {
        return this.cJ;
    }

    protected Block C() {
        this.cJ = false;
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

    public void l(World world, int i, int j, int k, int l) {}

    public void g(World world, int i, int j, int k) {}

    public boolean l() {
        return true;
    }

    public boolean a(Explosion explosion) {
        return true;
    }

    public boolean i(int i) {
        return this.id == i;
    }

    public static boolean b(int i, int j) {
        return i == j ? true : (i != 0 && j != 0 && byId[i] != null && byId[j] != null ? byId[i].i(j) : false);
    }

    public boolean q_() {
        return false;
    }

    public int b_(World world, int i, int j, int k, int l) {
        return 0;
    }

    protected Block d(String s) {
        this.f = s;
        return this;
    }

    static {
        Item.byId[WOOL.id] = (new ItemCloth(WOOL.id - 256)).b("cloth");
        Item.byId[STAINED_HARDENED_CLAY.id] = (new ItemCloth(STAINED_HARDENED_CLAY.id - 256)).b("clayHardenedStained");
        Item.byId[WOOL_CARPET.id] = (new ItemCloth(WOOL_CARPET.id - 256)).b("woolCarpet");
        Item.byId[LOG.id] = (new ItemMultiTexture(LOG.id - 256, LOG, BlockLog.b)).b("log");
        Item.byId[WOOD.id] = (new ItemMultiTexture(WOOD.id - 256, WOOD, BlockWood.a)).b("wood");
        Item.byId[MONSTER_EGGS.id] = (new ItemMultiTexture(MONSTER_EGGS.id - 256, MONSTER_EGGS, BlockMonsterEggs.a)).b("monsterStoneEgg");
        Item.byId[SMOOTH_BRICK.id] = (new ItemMultiTexture(SMOOTH_BRICK.id - 256, SMOOTH_BRICK, BlockSmoothBrick.a)).b("stonebricksmooth");
        Item.byId[SANDSTONE.id] = (new ItemMultiTexture(SANDSTONE.id - 256, SANDSTONE, BlockSandStone.a)).b("sandStone");
        Item.byId[QUARTZ_BLOCK.id] = (new ItemMultiTexture(QUARTZ_BLOCK.id - 256, QUARTZ_BLOCK, BlockQuartz.a)).b("quartzBlock");
        Item.byId[STEP.id] = (new ItemStep(STEP.id - 256, STEP, DOUBLE_STEP, false)).b("stoneSlab");
        Item.byId[DOUBLE_STEP.id] = (new ItemStep(DOUBLE_STEP.id - 256, STEP, DOUBLE_STEP, true)).b("stoneSlab");
        Item.byId[WOOD_STEP.id] = (new ItemStep(WOOD_STEP.id - 256, WOOD_STEP, WOOD_DOUBLE_STEP, false)).b("woodSlab");
        Item.byId[WOOD_DOUBLE_STEP.id] = (new ItemStep(WOOD_DOUBLE_STEP.id - 256, WOOD_STEP, WOOD_DOUBLE_STEP, true)).b("woodSlab");
        Item.byId[SAPLING.id] = (new ItemMultiTexture(SAPLING.id - 256, SAPLING, BlockSapling.a)).b("sapling");
        Item.byId[LEAVES.id] = (new ItemLeaves(LEAVES.id - 256)).b("leaves");
        Item.byId[VINE.id] = new ItemWithAuxData(VINE.id - 256, false);
        Item.byId[LONG_GRASS.id] = (new ItemWithAuxData(LONG_GRASS.id - 256, true)).a(new String[] { "shrub", "grass", "fern"});
        Item.byId[SNOW.id] = new ItemSnow(SNOW.id - 256, SNOW);
        Item.byId[WATER_LILY.id] = new ItemWaterLily(WATER_LILY.id - 256);
        Item.byId[PISTON.id] = new ItemPiston(PISTON.id - 256);
        Item.byId[PISTON_STICKY.id] = new ItemPiston(PISTON_STICKY.id - 256);
        Item.byId[COBBLE_WALL.id] = (new ItemMultiTexture(COBBLE_WALL.id - 256, COBBLE_WALL, BlockCobbleWall.a)).b("cobbleWall");
        Item.byId[ANVIL.id] = (new ItemAnvil(ANVIL)).b("anvil");
        // CraftBukkit start
        Item.byId[BIG_MUSHROOM_1.id] = new ItemWithAuxData(BIG_MUSHROOM_1.id - 256, true);
        Item.byId[BIG_MUSHROOM_2.id] = new ItemWithAuxData(BIG_MUSHROOM_2.id - 256, true);
        Item.byId[MOB_SPAWNER.id] = new ItemWithAuxData(MOB_SPAWNER.id - 256, true);
        // CraftBukkit end

        for (int i = 0; i < 256; ++i) {
            if (byId[i] != null) {
                if (Item.byId[i] == null) {
                    Item.byId[i] = new ItemBlock(i - 256);
                    byId[i].s_();
                }

                boolean flag = false;

                if (i > 0 && byId[i].d() == 10) {
                    flag = true;
                }

                if (i > 0 && byId[i] instanceof BlockStepAbstract) {
                    flag = true;
                }

                if (i == SOIL.id) {
                    flag = true;
                }

                if (v[i]) {
                    flag = true;
                }

                if (lightBlock[i] == 0) {
                    flag = true;
                }

                x[i] = flag;
            }
        }

        v[0] = true;
        StatisticList.b();
    }

    // CraftBukkit start
    public int getExpDrop(World world, int data, int enchantmentLevel) {
        return 0;
    }
    // CraftBukkit end
}
