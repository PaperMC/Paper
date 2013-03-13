package net.minecraft.server;

import java.util.List;
import java.util.Random;

public class Block {

    private CreativeModeTab creativeTab;
    public static final StepSound f = new StepSound("stone", 1.0F, 1.0F);
    public static final StepSound g = new StepSound("wood", 1.0F, 1.0F);
    public static final StepSound h = new StepSound("gravel", 1.0F, 1.0F);
    public static final StepSound i = new StepSound("grass", 1.0F, 1.0F);
    public static final StepSound j = new StepSound("stone", 1.0F, 1.0F);
    public static final StepSound k = new StepSound("stone", 1.0F, 1.5F);
    public static final StepSound l = new StepSoundStone("stone", 1.0F, 1.0F);
    public static final StepSound m = new StepSound("cloth", 1.0F, 1.0F);
    public static final StepSound n = new StepSound("sand", 1.0F, 1.0F);
    public static final StepSound o = new StepSound("snow", 1.0F, 1.0F);
    public static final StepSound p = new StepSoundLadder("ladder", 1.0F, 1.0F);
    public static final StepSound q = new StepSoundAnvil("anvil", 0.3F, 1.0F);
    public static final Block[] byId = new Block[4096];
    public static final boolean[] s = new boolean[4096];
    public static final int[] lightBlock = new int[4096];
    public static final boolean[] u = new boolean[4096];
    public static final int[] lightEmission = new int[4096];
    public static boolean[] w = new boolean[4096];
    public static final Block STONE = (new BlockStone(1)).c(1.5F).b(10.0F).a(j).c("stone");
    public static final BlockGrass GRASS = (BlockGrass) (new BlockGrass(2)).c(0.6F).a(i).c("grass");
    public static final Block DIRT = (new BlockDirt(3)).c(0.5F).a(h).c("dirt");
    public static final Block COBBLESTONE = (new Block(4, Material.STONE)).c(2.0F).b(10.0F).a(j).c("stonebrick").a(CreativeModeTab.b);
    public static final Block WOOD = (new BlockWood(5)).c(2.0F).b(5.0F).a(g).c("wood");
    public static final Block SAPLING = (new BlockSapling(6)).c(0.0F).a(i).c("sapling");
    public static final Block BEDROCK = (new Block(7, Material.STONE)).r().b(6000000.0F).a(j).c("bedrock").D().a(CreativeModeTab.b);
    public static final BlockFluids WATER = (BlockFluids) (new BlockFlowing(8, Material.WATER)).c(100.0F).k(3).c("water").D();
    public static final Block STATIONARY_WATER = (new BlockStationary(9, Material.WATER)).c(100.0F).k(3).c("water").D();
    public static final BlockFluids LAVA = (BlockFluids) (new BlockFlowing(10, Material.LAVA)).c(0.0F).a(1.0F).c("lava").D();
    public static final Block STATIONARY_LAVA = (new BlockStationary(11, Material.LAVA)).c(100.0F).a(1.0F).c("lava").D();
    public static final Block SAND = (new BlockSand(12)).c(0.5F).a(n).c("sand");
    public static final Block GRAVEL = (new BlockGravel(13)).c(0.6F).a(h).c("gravel");
    public static final Block GOLD_ORE = (new BlockOre(14)).c(3.0F).b(5.0F).a(j).c("oreGold");
    public static final Block IRON_ORE = (new BlockOre(15)).c(3.0F).b(5.0F).a(j).c("oreIron");
    public static final Block COAL_ORE = (new BlockOre(16)).c(3.0F).b(5.0F).a(j).c("oreCoal");
    public static final Block LOG = (new BlockLog(17)).c(2.0F).a(g).c("log");
    public static final BlockLeaves LEAVES = (BlockLeaves) (new BlockLeaves(18)).c(0.2F).k(1).a(i).c("leaves");
    public static final Block SPONGE = (new BlockSponge(19)).c(0.6F).a(i).c("sponge");
    public static final Block GLASS = (new BlockGlass(20, Material.SHATTERABLE, false)).c(0.3F).a(l).c("glass");
    public static final Block LAPIS_ORE = (new BlockOre(21)).c(3.0F).b(5.0F).a(j).c("oreLapis");
    public static final Block LAPIS_BLOCK = (new Block(22, Material.STONE)).c(3.0F).b(5.0F).a(j).c("blockLapis").a(CreativeModeTab.b);
    public static final Block DISPENSER = (new BlockDispenser(23)).c(3.5F).a(j).c("dispenser");
    public static final Block SANDSTONE = (new BlockSandStone(24)).a(j).c(0.8F).c("sandStone");
    public static final Block NOTE_BLOCK = (new BlockNote(25)).c(0.8F).c("musicBlock");
    public static final Block BED = (new BlockBed(26)).c(0.2F).c("bed").D();
    public static final Block GOLDEN_RAIL = (new BlockPoweredRail(27)).c(0.7F).a(k).c("goldenRail");
    public static final Block DETECTOR_RAIL = (new BlockMinecartDetector(28)).c(0.7F).a(k).c("detectorRail");
    public static final BlockPiston PISTON_STICKY = (BlockPiston) (new BlockPiston(29, true)).c("pistonStickyBase");
    public static final Block WEB = (new BlockWeb(30)).k(1).c(4.0F).c("web");
    public static final BlockLongGrass LONG_GRASS = (BlockLongGrass) (new BlockLongGrass(31)).c(0.0F).a(i).c("tallgrass");
    public static final BlockDeadBush DEAD_BUSH = (BlockDeadBush) (new BlockDeadBush(32)).c(0.0F).a(i).c("deadbush");
    public static final BlockPiston PISTON = (BlockPiston) (new BlockPiston(33, false)).c("pistonBase");
    public static final BlockPistonExtension PISTON_EXTENSION = new BlockPistonExtension(34);
    public static final Block WOOL = (new BlockCloth()).c(0.8F).a(m).c("cloth");
    public static final BlockPistonMoving PISTON_MOVING = new BlockPistonMoving(36);
    public static final BlockFlower YELLOW_FLOWER = (BlockFlower) (new BlockFlower(37)).c(0.0F).a(i).c("flower");
    public static final BlockFlower RED_ROSE = (BlockFlower) (new BlockFlower(38)).c(0.0F).a(i).c("rose");
    public static final BlockFlower BROWN_MUSHROOM = (BlockFlower) (new BlockMushroom(39, "mushroom_brown")).c(0.0F).a(i).a(0.125F).c("mushroom");
    public static final BlockFlower RED_MUSHROOM = (BlockFlower) (new BlockMushroom(40, "mushroom_red")).c(0.0F).a(i).c("mushroom");
    public static final Block GOLD_BLOCK = (new BlockOreBlock(41)).c(3.0F).b(10.0F).a(k).c("blockGold");
    public static final Block IRON_BLOCK = (new BlockOreBlock(42)).c(5.0F).b(10.0F).a(k).c("blockIron");
    public static final BlockStepAbstract DOUBLE_STEP = (BlockStepAbstract) (new BlockStep(43, true)).c(2.0F).b(10.0F).a(j).c("stoneSlab");
    public static final BlockStepAbstract STEP = (BlockStepAbstract) (new BlockStep(44, false)).c(2.0F).b(10.0F).a(j).c("stoneSlab");
    public static final Block BRICK = (new Block(45, Material.STONE)).c(2.0F).b(10.0F).a(j).c("brick").a(CreativeModeTab.b);
    public static final Block TNT = (new BlockTNT(46)).c(0.0F).a(i).c("tnt");
    public static final Block BOOKSHELF = (new BlockBookshelf(47)).c(1.5F).a(g).c("bookshelf");
    public static final Block MOSSY_COBBLESTONE = (new Block(48, Material.STONE)).c(2.0F).b(10.0F).a(j).c("stoneMoss").a(CreativeModeTab.b);
    public static final Block OBSIDIAN = (new BlockObsidian(49)).c(50.0F).b(2000.0F).a(j).c("obsidian");
    public static final Block TORCH = (new BlockTorch(50)).c(0.0F).a(0.9375F).a(g).c("torch");
    public static final BlockFire FIRE = (BlockFire) (new BlockFire(51)).c(0.0F).a(1.0F).a(g).c("fire").D();
    public static final Block MOB_SPAWNER = (new BlockMobSpawner(52)).c(5.0F).a(k).c("mobSpawner").D();
    public static final Block WOOD_STAIRS = (new BlockStairs(53, WOOD, 0)).c("stairsWood");
    public static final BlockChest CHEST = (BlockChest) (new BlockChest(54, 0)).c(2.5F).a(g).c("chest");
    public static final BlockRedstoneWire REDSTONE_WIRE = (BlockRedstoneWire) (new BlockRedstoneWire(55)).c(0.0F).a(f).c("redstoneDust").D();
    public static final Block DIAMOND_ORE = (new BlockOre(56)).c(3.0F).b(5.0F).a(j).c("oreDiamond");
    public static final Block DIAMOND_BLOCK = (new BlockOreBlock(57)).c(5.0F).b(10.0F).a(k).c("blockDiamond");
    public static final Block WORKBENCH = (new BlockWorkbench(58)).c(2.5F).a(g).c("workbench");
    public static final Block CROPS = (new BlockCrops(59)).c("crops");
    public static final Block SOIL = (new BlockSoil(60)).c(0.6F).a(h).c("farmland");
    public static final Block FURNACE = (new BlockFurnace(61, false)).c(3.5F).a(j).c("furnace").a(CreativeModeTab.c);
    public static final Block BURNING_FURNACE = (new BlockFurnace(62, true)).c(3.5F).a(j).a(0.875F).c("furnace");
    public static final Block SIGN_POST = (new BlockSign(63, TileEntitySign.class, true)).c(1.0F).a(g).c("sign").D();
    public static final Block WOODEN_DOOR = (new BlockDoor(64, Material.WOOD)).c(3.0F).a(g).c("doorWood").D();
    public static final Block LADDER = (new BlockLadder(65)).c(0.4F).a(p).c("ladder");
    public static final Block RAILS = (new BlockMinecartTrack(66)).c(0.7F).a(k).c("rail");
    public static final Block COBBLESTONE_STAIRS = (new BlockStairs(67, COBBLESTONE, 0)).c("stairsStone");
    public static final Block WALL_SIGN = (new BlockSign(68, TileEntitySign.class, false)).c(1.0F).a(g).c("sign").D();
    public static final Block LEVER = (new BlockLever(69)).c(0.5F).a(g).c("lever");
    public static final Block STONE_PLATE = (new BlockPressurePlateBinary(70, "stone", Material.STONE, EnumMobType.MOBS)).c(0.5F).a(j).c("pressurePlate");
    public static final Block IRON_DOOR_BLOCK = (new BlockDoor(71, Material.ORE)).c(5.0F).a(k).c("doorIron").D();
    public static final Block WOOD_PLATE = (new BlockPressurePlateBinary(72, "wood", Material.WOOD, EnumMobType.EVERYTHING)).c(0.5F).a(g).c("pressurePlate");
    public static final Block REDSTONE_ORE = (new BlockRedstoneOre(73, false)).c(3.0F).b(5.0F).a(j).c("oreRedstone").a(CreativeModeTab.b);
    public static final Block GLOWING_REDSTONE_ORE = (new BlockRedstoneOre(74, true)).a(0.625F).c(3.0F).b(5.0F).a(j).c("oreRedstone");
    public static final Block REDSTONE_TORCH_OFF = (new BlockRedstoneTorch(75, false)).c(0.0F).a(g).c("notGate");
    public static final Block REDSTONE_TORCH_ON = (new BlockRedstoneTorch(76, true)).c(0.0F).a(0.5F).a(g).c("notGate").a(CreativeModeTab.d);
    public static final Block STONE_BUTTON = (new BlockStoneButton(77)).c(0.5F).a(j).c("button");
    public static final Block SNOW = (new BlockSnow(78)).c(0.1F).a(o).c("snow").k(0);
    public static final Block ICE = (new BlockIce(79)).c(0.5F).k(3).a(l).c("ice");
    public static final Block SNOW_BLOCK = (new BlockSnowBlock(80)).c(0.2F).a(o).c("snow");
    public static final Block CACTUS = (new BlockCactus(81)).c(0.4F).a(m).c("cactus");
    public static final Block CLAY = (new BlockClay(82)).c(0.6F).a(h).c("clay");
    public static final Block SUGAR_CANE_BLOCK = (new BlockReed(83)).c(0.0F).a(i).c("reeds").D();
    public static final Block JUKEBOX = (new BlockJukeBox(84)).c(2.0F).b(10.0F).a(j).c("jukebox");
    public static final Block FENCE = (new BlockFence(85, "wood", Material.WOOD)).c(2.0F).b(5.0F).a(g).c("fence");
    public static final Block PUMPKIN = (new BlockPumpkin(86, false)).c(1.0F).a(g).c("pumpkin");
    public static final Block NETHERRACK = (new BlockBloodStone(87)).c(0.4F).a(j).c("hellrock");
    public static final Block SOUL_SAND = (new BlockSlowSand(88)).c(0.5F).a(n).c("hellsand");
    public static final Block GLOWSTONE = (new BlockLightStone(89, Material.SHATTERABLE)).c(0.3F).a(l).a(1.0F).c("lightgem");
    public static final BlockPortal PORTAL = (BlockPortal) (new BlockPortal(90)).c(-1.0F).a(l).a(0.75F).c("portal");
    public static final Block JACK_O_LANTERN = (new BlockPumpkin(91, true)).c(1.0F).a(g).a(1.0F).c("litpumpkin");
    public static final Block CAKE_BLOCK = (new BlockCake(92)).c(0.5F).a(m).c("cake").D();
    public static final BlockRepeater DIODE_OFF = (BlockRepeater) (new BlockRepeater(93, false)).c(0.0F).a(g).c("diode").D();
    public static final BlockRepeater DIODE_ON = (BlockRepeater) (new BlockRepeater(94, true)).c(0.0F).a(0.625F).a(g).c("diode").D();
    public static final Block LOCKED_CHEST = (new BlockLockedChest(95)).c(0.0F).a(1.0F).a(g).c("lockedchest").b(true);
    public static final Block TRAP_DOOR = (new BlockTrapdoor(96, Material.WOOD)).c(3.0F).a(g).c("trapdoor").D();
    public static final Block MONSTER_EGGS = (new BlockMonsterEggs(97)).c(0.75F).c("monsterStoneEgg");
    public static final Block SMOOTH_BRICK = (new BlockSmoothBrick(98)).c(1.5F).b(10.0F).a(j).c("stonebricksmooth");
    public static final Block BIG_MUSHROOM_1 = (new BlockHugeMushroom(99, Material.WOOD, 0)).c(0.2F).a(g).c("mushroom");
    public static final Block BIG_MUSHROOM_2 = (new BlockHugeMushroom(100, Material.WOOD, 1)).c(0.2F).a(g).c("mushroom");
    public static final Block IRON_FENCE = (new BlockThinFence(101, "fenceIron", "fenceIron", Material.ORE, true)).c(5.0F).b(10.0F).a(k).c("fenceIron");
    public static final Block THIN_GLASS = (new BlockThinFence(102, "glass", "thinglass_top", Material.SHATTERABLE, false)).c(0.3F).a(l).c("thinGlass");
    public static final Block MELON = (new BlockMelon(103)).c(1.0F).a(g).c("melon");
    public static final Block PUMPKIN_STEM = (new BlockStem(104, PUMPKIN)).c(0.0F).a(g).c("pumpkinStem");
    public static final Block MELON_STEM = (new BlockStem(105, MELON)).c(0.0F).a(g).c("pumpkinStem");
    public static final Block VINE = (new BlockVine(106)).c(0.2F).a(i).c("vine");
    public static final Block FENCE_GATE = (new BlockFenceGate(107)).c(2.0F).b(5.0F).a(g).c("fenceGate");
    public static final Block BRICK_STAIRS = (new BlockStairs(108, BRICK, 0)).c("stairsBrick");
    public static final Block STONE_STAIRS = (new BlockStairs(109, SMOOTH_BRICK, 0)).c("stairsStoneBrickSmooth");
    public static final BlockMycel MYCEL = (BlockMycel) (new BlockMycel(110)).c(0.6F).a(i).c("mycel");
    public static final Block WATER_LILY = (new BlockWaterLily(111)).c(0.0F).a(i).c("waterlily");
    public static final Block NETHER_BRICK = (new Block(112, Material.STONE)).c(2.0F).b(10.0F).a(j).c("netherBrick").a(CreativeModeTab.b);
    public static final Block NETHER_FENCE = (new BlockFence(113, "netherBrick", Material.STONE)).c(2.0F).b(10.0F).a(j).c("netherFence");
    public static final Block NETHER_BRICK_STAIRS = (new BlockStairs(114, NETHER_BRICK, 0)).c("stairsNetherBrick");
    public static final Block NETHER_WART = (new BlockNetherWart(115)).c("netherStalk");
    public static final Block ENCHANTMENT_TABLE = (new BlockEnchantmentTable(116)).c(5.0F).b(2000.0F).c("enchantmentTable");
    public static final Block BREWING_STAND = (new BlockBrewingStand(117)).c(0.5F).a(0.125F).c("brewingStand");
    public static final BlockCauldron CAULDRON = (BlockCauldron) (new BlockCauldron(118)).c(2.0F).c("cauldron");
    public static final Block ENDER_PORTAL = (new BlockEnderPortal(119, Material.PORTAL)).c(-1.0F).b(6000000.0F);
    public static final Block ENDER_PORTAL_FRAME = (new BlockEnderPortalFrame(120)).a(l).a(0.125F).c(-1.0F).c("endPortalFrame").b(6000000.0F).a(CreativeModeTab.c);
    public static final Block WHITESTONE = (new Block(121, Material.STONE)).c(3.0F).b(15.0F).a(j).c("whiteStone").a(CreativeModeTab.b);
    public static final Block DRAGON_EGG = (new BlockDragonEgg(122)).c(3.0F).b(15.0F).a(j).a(0.125F).c("dragonEgg");
    public static final Block REDSTONE_LAMP_OFF = (new BlockRedstoneLamp(123, false)).c(0.3F).a(l).c("redstoneLight").a(CreativeModeTab.d);
    public static final Block REDSTONE_LAMP_ON = (new BlockRedstoneLamp(124, true)).c(0.3F).a(l).c("redstoneLight");
    public static final BlockStepAbstract WOOD_DOUBLE_STEP = (BlockStepAbstract) (new BlockWoodStep(125, true)).c(2.0F).b(5.0F).a(g).c("woodSlab");
    public static final BlockStepAbstract WOOD_STEP = (BlockStepAbstract) (new BlockWoodStep(126, false)).c(2.0F).b(5.0F).a(g).c("woodSlab");
    public static final Block COCOA = (new BlockCocoa(127)).c(0.2F).b(5.0F).a(g).c("cocoa");
    public static final Block SANDSTONE_STAIRS = (new BlockStairs(128, SANDSTONE, 0)).c("stairsSandStone");
    public static final Block EMERALD_ORE = (new BlockOre(129)).c(3.0F).b(5.0F).a(j).c("oreEmerald");
    public static final Block ENDER_CHEST = (new BlockEnderChest(130)).c(22.5F).b(1000.0F).a(j).c("enderChest").a(0.5F);
    public static final BlockTripwireHook TRIPWIRE_SOURCE = (BlockTripwireHook) (new BlockTripwireHook(131)).c("tripWireSource");
    public static final Block TRIPWIRE = (new BlockTripwire(132)).c("tripWire");
    public static final Block EMERALD_BLOCK = (new BlockOreBlock(133)).c(5.0F).b(10.0F).a(k).c("blockEmerald");
    public static final Block SPRUCE_WOOD_STAIRS = (new BlockStairs(134, WOOD, 1)).c("stairsWoodSpruce");
    public static final Block BIRCH_WOOD_STAIRS = (new BlockStairs(135, WOOD, 2)).c("stairsWoodBirch");
    public static final Block JUNGLE_WOOD_STAIRS = (new BlockStairs(136, WOOD, 3)).c("stairsWoodJungle");
    public static final Block COMMAND = (new BlockCommand(137)).c("commandBlock");
    public static final BlockBeacon BEACON = (BlockBeacon) (new BlockBeacon(138)).c("beacon").a(1.0F);
    public static final Block COBBLE_WALL = (new BlockCobbleWall(139, COBBLESTONE)).c("cobbleWall");
    public static final Block FLOWER_POT = (new BlockFlowerPot(140)).c(0.0F).a(f).c("flowerPot");
    public static final Block CARROTS = (new BlockCarrots(141)).c("carrots");
    public static final Block POTATOES = (new BlockPotatoes(142)).c("potatoes");
    public static final Block WOOD_BUTTON = (new BlockWoodButton(143)).c(0.5F).a(g).c("button");
    public static final Block SKULL = (new BlockSkull(144)).c(1.0F).a(j).c("skull");
    public static final Block ANVIL = (new BlockAnvil(145)).c(5.0F).a(q).b(2000.0F).c("anvil");
    public static final Block TRAPPED_CHEST = (new BlockChest(146, 1)).c(2.5F).a(g).c("chestTrap");
    public static final Block GOLD_PLATE = (new BlockPressurePlateWeighted(147, "blockGold", Material.ORE, 64)).c(0.5F).a(g).c("weightedPlate_light");
    public static final Block IRON_PLATE = (new BlockPressurePlateWeighted(148, "blockIron", Material.ORE, 640)).c(0.5F).a(g).c("weightedPlate_heavy");
    public static final BlockRedstoneComparator REDSTONE_COMPARATOR_OFF = (BlockRedstoneComparator) (new BlockRedstoneComparator(149, false)).c(0.0F).a(g).c("comparator").D();
    public static final BlockRedstoneComparator REDSTONE_COMPARATOR_ON = (BlockRedstoneComparator) (new BlockRedstoneComparator(150, true)).c(0.0F).a(0.625F).a(g).c("comparator").D();
    public static final BlockDaylightDetector DAYLIGHT_DETECTOR = (BlockDaylightDetector) (new BlockDaylightDetector(151)).c(0.2F).a(g).c("daylightDetector");
    public static final Block REDSTONE_BLOCK = (new BlockRedstone(152)).c(5.0F).b(10.0F).a(k).c("blockRedstone");
    public static final Block QUARTZ_ORE = (new BlockOre(153)).c(3.0F).b(5.0F).a(j).c("netherquartz");
    public static final BlockHopper HOPPER = (BlockHopper) (new BlockHopper(154)).c(3.0F).b(8.0F).a(g).c("hopper");
    public static final Block QUARTZ_BLOCK = (new BlockQuartz(155)).a(j).c(0.8F).c("quartzBlock");
    public static final Block QUARTZ_STAIRS = (new BlockStairs(156, QUARTZ_BLOCK, 0)).c("stairsQuartz");
    public static final Block ACTIVATOR_RAIL = (new BlockPoweredRail(157)).c(0.7F).a(k).c("activatorRail");
    public static final Block DROPPER = (new BlockDropper(158)).c(3.5F).a(j).c("dropper");
    public final int id;
    protected float strength;
    protected float durability;
    protected boolean cC = true;
    protected boolean cD = true;
    protected boolean cE;
    protected boolean isTileEntity;
    protected double minX;
    protected double minY;
    protected double minZ;
    protected double maxX;
    protected double maxY;
    protected double maxZ;
    public StepSound stepSound;
    public float cN;
    public final Material material;
    public float frictionFactor;
    private String name;

    protected Block(int i, Material material) {
        this.stepSound = f;
        this.cN = 1.0F;
        this.frictionFactor = 0.6F;
        if (byId[i] != null) {
            throw new IllegalArgumentException("Slot " + i + " is already occupied by " + byId[i] + " when adding " + this);
        } else {
            this.material = material;
            byId[i] = this;
            this.id = i;
            this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            s[i] = this.c();
            lightBlock[i] = this.c() ? 255 : 0;
            u[i] = !material.blocksLight();
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
        this.cE = flag;
        return this;
    }

    public boolean isTicking() {
        return this.cE;
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

        if (axisalignedbb1 != null && axisalignedbb.a(axisalignedbb1)) {
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
        entityhuman.j(0.025F);
        if (this.r_() && EnchantmentManager.hasSilkTouchEnchantment(entityhuman)) {
            ItemStack itemstack = this.c_(l);

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

    protected ItemStack c_(int i) {
        int j = 0;

        if (this.id >= 0 && this.id < Item.byId.length && Item.byId[this.id].m()) {
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

    public boolean C() {
        return this.cD;
    }

    protected Block D() {
        this.cD = false;
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

    static {
        Item.byId[WOOL.id] = (new ItemCloth(WOOL.id - 256)).b("cloth");
        Item.byId[LOG.id] = (new ItemMultiTexture(LOG.id - 256, LOG, BlockLog.a)).b("log");
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

                if (u[i]) {
                    flag = true;
                }

                if (lightBlock[i] == 0) {
                    flag = true;
                }

                w[i] = flag;
            }
        }

        u[0] = true;
        StatisticList.b();
    }

    // CraftBukkit start
    public int getExpDrop(World world, int data, int enchantmentLevel) {
        return 0;
    }
    // CraftBukkit end
}
