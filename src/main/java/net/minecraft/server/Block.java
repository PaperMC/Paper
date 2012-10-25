package net.minecraft.server;

import java.util.List;
import java.util.Random;

public class Block {

    private CreativeModeTab creativeTab;
    public static final StepSound d = new StepSound("stone", 1.0F, 1.0F);
    public static final StepSound e = new StepSound("wood", 1.0F, 1.0F);
    public static final StepSound f = new StepSound("gravel", 1.0F, 1.0F);
    public static final StepSound g = new StepSound("grass", 1.0F, 1.0F);
    public static final StepSound h = new StepSound("stone", 1.0F, 1.0F);
    public static final StepSound i = new StepSound("stone", 1.0F, 1.5F);
    public static final StepSound j = new StepSoundStone("stone", 1.0F, 1.0F);
    public static final StepSound k = new StepSound("cloth", 1.0F, 1.0F);
    public static final StepSound l = new StepSound("sand", 1.0F, 1.0F);
    public static final StepSound m = new StepSound("snow", 1.0F, 1.0F);
    public static final StepSound n = new StepSoundLadder("ladder", 1.0F, 1.0F);
    public static final StepSound o = new StepSoundAnvil("anvil", 0.3F, 1.0F);
    public static final Block[] byId = new Block[4096];
    public static final boolean[] q = new boolean[4096];
    public static final int[] lightBlock = new int[4096];
    public static final boolean[] s = new boolean[4096];
    public static final int[] lightEmission = new int[4096];
    public static final boolean[] u = new boolean[4096];
    public static boolean[] v = new boolean[4096];
    public static final Block STONE = (new BlockStone(1, 1)).c(1.5F).b(10.0F).a(h).b("stone");
    public static final BlockGrass GRASS = (BlockGrass) (new BlockGrass(2)).c(0.6F).a(g).b("grass");
    public static final Block DIRT = (new BlockDirt(3, 2)).c(0.5F).a(f).b("dirt");
    public static final Block COBBLESTONE = (new Block(4, 16, Material.STONE)).c(2.0F).b(10.0F).a(h).b("stonebrick").a(CreativeModeTab.b);
    public static final Block WOOD = (new BlockWood(5)).c(2.0F).b(5.0F).a(e).b("wood").r();
    public static final Block SAPLING = (new BlockSapling(6, 15)).c(0.0F).a(g).b("sapling").r();
    public static final Block BEDROCK = (new Block(7, 17, Material.STONE)).s().b(6000000.0F).a(h).b("bedrock").D().a(CreativeModeTab.b);
    public static final Block WATER = (new BlockFlowing(8, Material.WATER)).c(100.0F).h(3).b("water").D().r();
    public static final Block STATIONARY_WATER = (new BlockStationary(9, Material.WATER)).c(100.0F).h(3).b("water").D().r();
    public static final Block LAVA = (new BlockFlowing(10, Material.LAVA)).c(0.0F).a(1.0F).h(255).b("lava").D().r();
    public static final Block STATIONARY_LAVA = (new BlockStationary(11, Material.LAVA)).c(100.0F).a(1.0F).h(255).b("lava").D().r();
    public static final Block SAND = (new BlockSand(12, 18)).c(0.5F).a(l).b("sand");
    public static final Block GRAVEL = (new BlockGravel(13, 19)).c(0.6F).a(f).b("gravel");
    public static final Block GOLD_ORE = (new BlockOre(14, 32)).c(3.0F).b(5.0F).a(h).b("oreGold");
    public static final Block IRON_ORE = (new BlockOre(15, 33)).c(3.0F).b(5.0F).a(h).b("oreIron");
    public static final Block COAL_ORE = (new BlockOre(16, 34)).c(3.0F).b(5.0F).a(h).b("oreCoal");
    public static final Block LOG = (new BlockLog(17)).c(2.0F).a(e).b("log").r();
    public static final BlockLeaves LEAVES = (BlockLeaves) (new BlockLeaves(18, 52)).c(0.2F).h(1).a(g).b("leaves").r();
    public static final Block SPONGE = (new BlockSponge(19)).c(0.6F).a(g).b("sponge");
    public static final Block GLASS = (new BlockGlass(20, 49, Material.SHATTERABLE, false)).c(0.3F).a(j).b("glass");
    public static final Block LAPIS_ORE = (new BlockOre(21, 160)).c(3.0F).b(5.0F).a(h).b("oreLapis");
    public static final Block LAPIS_BLOCK = (new Block(22, 144, Material.STONE)).c(3.0F).b(5.0F).a(h).b("blockLapis").a(CreativeModeTab.b);
    public static final Block DISPENSER = (new BlockDispenser(23)).c(3.5F).a(h).b("dispenser").r();
    public static final Block SANDSTONE = (new BlockSandStone(24)).a(h).c(0.8F).b("sandStone").r();
    public static final Block NOTE_BLOCK = (new BlockNote(25)).c(0.8F).b("musicBlock").r();
    public static final Block BED = (new BlockBed(26)).c(0.2F).b("bed").D().r();
    public static final Block GOLDEN_RAIL = (new BlockMinecartTrack(27, 179, true)).c(0.7F).a(i).b("goldenRail").r();
    public static final Block DETECTOR_RAIL = (new BlockMinecartDetector(28, 195)).c(0.7F).a(i).b("detectorRail").r();
    public static final Block PISTON_STICKY = (new BlockPiston(29, 106, true)).b("pistonStickyBase").r();
    public static final Block WEB = (new BlockWeb(30, 11)).h(1).c(4.0F).b("web");
    public static final BlockLongGrass LONG_GRASS = (BlockLongGrass) (new BlockLongGrass(31, 39)).c(0.0F).a(g).b("tallgrass");
    public static final BlockDeadBush DEAD_BUSH = (BlockDeadBush) (new BlockDeadBush(32, 55)).c(0.0F).a(g).b("deadbush");
    public static final Block PISTON = (new BlockPiston(33, 107, false)).b("pistonBase").r();
    public static final BlockPistonExtension PISTON_EXTENSION = (BlockPistonExtension) (new BlockPistonExtension(34, 107)).r();
    public static final Block WOOL = (new BlockCloth()).c(0.8F).a(k).b("cloth").r();
    public static final BlockPistonMoving PISTON_MOVING = new BlockPistonMoving(36);
    public static final BlockFlower YELLOW_FLOWER = (BlockFlower) (new BlockFlower(37, 13)).c(0.0F).a(g).b("flower");
    public static final BlockFlower RED_ROSE = (BlockFlower) (new BlockFlower(38, 12)).c(0.0F).a(g).b("rose");
    public static final BlockFlower BROWN_MUSHROOM = (BlockFlower) (new BlockMushroom(39, 29)).c(0.0F).a(g).a(0.125F).b("mushroom");
    public static final BlockFlower RED_MUSHROOM = (BlockFlower) (new BlockMushroom(40, 28)).c(0.0F).a(g).b("mushroom");
    public static final Block GOLD_BLOCK = (new BlockOreBlock(41, 23)).c(3.0F).b(10.0F).a(i).b("blockGold");
    public static final Block IRON_BLOCK = (new BlockOreBlock(42, 22)).c(5.0F).b(10.0F).a(i).b("blockIron");
    public static final BlockStepAbstract DOUBLE_STEP = (BlockStepAbstract) (new BlockStep(43, true)).c(2.0F).b(10.0F).a(h).b("stoneSlab");
    public static final BlockStepAbstract STEP = (BlockStepAbstract) (new BlockStep(44, false)).c(2.0F).b(10.0F).a(h).b("stoneSlab");
    public static final Block BRICK = (new Block(45, 7, Material.STONE)).c(2.0F).b(10.0F).a(h).b("brick").a(CreativeModeTab.b);
    public static final Block TNT = (new BlockTNT(46, 8)).c(0.0F).a(g).b("tnt");
    public static final Block BOOKSHELF = (new BlockBookshelf(47, 35)).c(1.5F).a(e).b("bookshelf");
    public static final Block MOSSY_COBBLESTONE = (new Block(48, 36, Material.STONE)).c(2.0F).b(10.0F).a(h).b("stoneMoss").a(CreativeModeTab.b);
    public static final Block OBSIDIAN = (new BlockObsidian(49, 37)).c(50.0F).b(2000.0F).a(h).b("obsidian");
    public static final Block TORCH = (new BlockTorch(50, 80)).c(0.0F).a(0.9375F).a(e).b("torch").r();
    public static final BlockFire FIRE = (BlockFire) (new BlockFire(51, 31)).c(0.0F).a(1.0F).a(e).b("fire").D();
    public static final Block MOB_SPAWNER = (new BlockMobSpawner(52, 65)).c(5.0F).a(i).b("mobSpawner").D();
    public static final Block WOOD_STAIRS = (new BlockStairs(53, WOOD, 0)).b("stairsWood").r();
    public static final Block CHEST = (new BlockChest(54)).c(2.5F).a(e).b("chest").r();
    public static final Block REDSTONE_WIRE = (new BlockRedstoneWire(55, 164)).c(0.0F).a(d).b("redstoneDust").D().r();
    public static final Block DIAMOND_ORE = (new BlockOre(56, 50)).c(3.0F).b(5.0F).a(h).b("oreDiamond");
    public static final Block DIAMOND_BLOCK = (new BlockOreBlock(57, 24)).c(5.0F).b(10.0F).a(i).b("blockDiamond");
    public static final Block WORKBENCH = (new BlockWorkbench(58)).c(2.5F).a(e).b("workbench");
    public static final Block CROPS = (new BlockCrops(59, 88)).b("crops");
    public static final Block SOIL = (new BlockSoil(60)).c(0.6F).a(f).b("farmland").r();
    public static final Block FURNACE = (new BlockFurnace(61, false)).c(3.5F).a(h).b("furnace").r().a(CreativeModeTab.c);
    public static final Block BURNING_FURNACE = (new BlockFurnace(62, true)).c(3.5F).a(h).a(0.875F).b("furnace").r();
    public static final Block SIGN_POST = (new BlockSign(63, TileEntitySign.class, true)).c(1.0F).a(e).b("sign").D().r();
    public static final Block WOODEN_DOOR = (new BlockDoor(64, Material.WOOD)).c(3.0F).a(e).b("doorWood").D().r();
    public static final Block LADDER = (new BlockLadder(65, 83)).c(0.4F).a(n).b("ladder").r();
    public static final Block RAILS = (new BlockMinecartTrack(66, 128, false)).c(0.7F).a(i).b("rail").r();
    public static final Block COBBLESTONE_STAIRS = (new BlockStairs(67, COBBLESTONE, 0)).b("stairsStone").r();
    public static final Block WALL_SIGN = (new BlockSign(68, TileEntitySign.class, false)).c(1.0F).a(e).b("sign").D().r();
    public static final Block LEVER = (new BlockLever(69, 96)).c(0.5F).a(e).b("lever").r();
    public static final Block STONE_PLATE = (new BlockPressurePlate(70, STONE.textureId, EnumMobType.MOBS, Material.STONE)).c(0.5F).a(h).b("pressurePlate").r();
    public static final Block IRON_DOOR_BLOCK = (new BlockDoor(71, Material.ORE)).c(5.0F).a(i).b("doorIron").D().r();
    public static final Block WOOD_PLATE = (new BlockPressurePlate(72, WOOD.textureId, EnumMobType.EVERYTHING, Material.WOOD)).c(0.5F).a(e).b("pressurePlate").r();
    public static final Block REDSTONE_ORE = (new BlockRedstoneOre(73, 51, false)).c(3.0F).b(5.0F).a(h).b("oreRedstone").r().a(CreativeModeTab.b);
    public static final Block GLOWING_REDSTONE_ORE = (new BlockRedstoneOre(74, 51, true)).a(0.625F).c(3.0F).b(5.0F).a(h).b("oreRedstone").r();
    public static final Block REDSTONE_TORCH_OFF = (new BlockRedstoneTorch(75, 115, false)).c(0.0F).a(e).b("notGate").r();
    public static final Block REDSTONE_TORCH_ON = (new BlockRedstoneTorch(76, 99, true)).c(0.0F).a(0.5F).a(e).b("notGate").r().a(CreativeModeTab.d);
    public static final Block STONE_BUTTON = (new BlockButton(77, STONE.textureId, false)).c(0.5F).a(h).b("button").r();
    public static final Block SNOW = (new BlockSnow(78, 66)).c(0.1F).a(m).b("snow").r().h(0);
    public static final Block ICE = (new BlockIce(79, 67)).c(0.5F).h(3).a(j).b("ice");
    public static final Block SNOW_BLOCK = (new BlockSnowBlock(80, 66)).c(0.2F).a(m).b("snow");
    public static final Block CACTUS = (new BlockCactus(81, 70)).c(0.4F).a(k).b("cactus");
    public static final Block CLAY = (new BlockClay(82, 72)).c(0.6F).a(f).b("clay");
    public static final Block SUGAR_CANE_BLOCK = (new BlockReed(83, 73)).c(0.0F).a(g).b("reeds").D();
    public static final Block JUKEBOX = (new BlockJukeBox(84, 74)).c(2.0F).b(10.0F).a(h).b("jukebox").r();
    public static final Block FENCE = (new BlockFence(85, 4)).c(2.0F).b(5.0F).a(e).b("fence");
    public static final Block PUMPKIN = (new BlockPumpkin(86, 102, false)).c(1.0F).a(e).b("pumpkin").r();
    public static final Block NETHERRACK = (new BlockBloodStone(87, 103)).c(0.4F).a(h).b("hellrock");
    public static final Block SOUL_SAND = (new BlockSlowSand(88, 104)).c(0.5F).a(l).b("hellsand");
    public static final Block GLOWSTONE = (new BlockLightStone(89, 105, Material.SHATTERABLE)).c(0.3F).a(j).a(1.0F).b("lightgem");
    public static final BlockPortal PORTAL = (BlockPortal) (new BlockPortal(90, 14)).c(-1.0F).a(j).a(0.75F).b("portal");
    public static final Block JACK_O_LANTERN = (new BlockPumpkin(91, 102, true)).c(1.0F).a(e).a(1.0F).b("litpumpkin").r();
    public static final Block CAKE_BLOCK = (new BlockCake(92, 121)).c(0.5F).a(k).b("cake").D().r();
    public static final Block DIODE_OFF = (new BlockDiode(93, false)).c(0.0F).a(e).b("diode").D().r();
    public static final Block DIODE_ON = (new BlockDiode(94, true)).c(0.0F).a(0.625F).a(e).b("diode").D().r();
    public static final Block LOCKED_CHEST = (new BlockLockedChest(95)).c(0.0F).a(1.0F).a(e).b("lockedchest").b(true).r();
    public static final Block TRAP_DOOR = (new BlockTrapdoor(96, Material.WOOD)).c(3.0F).a(e).b("trapdoor").D().r();
    public static final Block MONSTER_EGGS = (new BlockMonsterEggs(97)).c(0.75F).b("monsterStoneEgg");
    public static final Block SMOOTH_BRICK = (new BlockSmoothBrick(98)).c(1.5F).b(10.0F).a(h).b("stonebricksmooth");
    public static final Block BIG_MUSHROOM_1 = (new BlockHugeMushroom(99, Material.WOOD, 142, 0)).c(0.2F).a(e).b("mushroom").r();
    public static final Block BIG_MUSHROOM_2 = (new BlockHugeMushroom(100, Material.WOOD, 142, 1)).c(0.2F).a(e).b("mushroom").r();
    public static final Block IRON_FENCE = (new BlockThinFence(101, 85, 85, Material.ORE, true)).c(5.0F).b(10.0F).a(i).b("fenceIron");
    public static final Block THIN_GLASS = (new BlockThinFence(102, 49, 148, Material.SHATTERABLE, false)).c(0.3F).a(j).b("thinGlass");
    public static final Block MELON = (new BlockMelon(103)).c(1.0F).a(e).b("melon");
    public static final Block PUMPKIN_STEM = (new BlockStem(104, PUMPKIN)).c(0.0F).a(e).b("pumpkinStem").r();
    public static final Block MELON_STEM = (new BlockStem(105, MELON)).c(0.0F).a(e).b("pumpkinStem").r();
    public static final Block VINE = (new BlockVine(106)).c(0.2F).a(g).b("vine").r();
    public static final Block FENCE_GATE = (new BlockFenceGate(107, 4)).c(2.0F).b(5.0F).a(e).b("fenceGate").r();
    public static final Block BRICK_STAIRS = (new BlockStairs(108, BRICK, 0)).b("stairsBrick").r();
    public static final Block STONE_STAIRS = (new BlockStairs(109, SMOOTH_BRICK, 0)).b("stairsStoneBrickSmooth").r();
    public static final BlockMycel MYCEL = (BlockMycel) (new BlockMycel(110)).c(0.6F).a(g).b("mycel");
    public static final Block WATER_LILY = (new BlockWaterLily(111, 76)).c(0.0F).a(g).b("waterlily");
    public static final Block NETHER_BRICK = (new Block(112, 224, Material.STONE)).c(2.0F).b(10.0F).a(h).b("netherBrick").a(CreativeModeTab.b);
    public static final Block NETHER_FENCE = (new BlockFence(113, 224, Material.STONE)).c(2.0F).b(10.0F).a(h).b("netherFence");
    public static final Block NETHER_BRICK_STAIRS = (new BlockStairs(114, NETHER_BRICK, 0)).b("stairsNetherBrick").r();
    public static final Block NETHER_WART = (new BlockNetherWart(115)).b("netherStalk").r();
    public static final Block ENCHANTMENT_TABLE = (new BlockEnchantmentTable(116)).c(5.0F).b(2000.0F).b("enchantmentTable");
    public static final Block BREWING_STAND = (new BlockBrewingStand(117)).c(0.5F).a(0.125F).b("brewingStand").r();
    public static final Block CAULDRON = (new BlockCauldron(118)).c(2.0F).b("cauldron").r();
    public static final Block ENDER_PORTAL = (new BlockEnderPortal(119, Material.PORTAL)).c(-1.0F).b(6000000.0F);
    public static final Block ENDER_PORTAL_FRAME = (new BlockEnderPortalFrame(120)).a(j).a(0.125F).c(-1.0F).b("endPortalFrame").r().b(6000000.0F).a(CreativeModeTab.c);
    public static final Block WHITESTONE = (new Block(121, 175, Material.STONE)).c(3.0F).b(15.0F).a(h).b("whiteStone").a(CreativeModeTab.b);
    public static final Block DRAGON_EGG = (new BlockDragonEgg(122, 167)).c(3.0F).b(15.0F).a(h).a(0.125F).b("dragonEgg");
    public static final Block REDSTONE_LAMP_OFF = (new BlockRedstoneLamp(123, false)).c(0.3F).a(j).b("redstoneLight").a(CreativeModeTab.d);
    public static final Block REDSTONE_LAMP_ON = (new BlockRedstoneLamp(124, true)).c(0.3F).a(j).b("redstoneLight");
    public static final BlockStepAbstract WOOD_DOUBLE_STEP = (BlockStepAbstract) (new BlockWoodStep(125, true)).c(2.0F).b(5.0F).a(e).b("woodSlab");
    public static final BlockStepAbstract WOOD_STEP = (BlockStepAbstract) (new BlockWoodStep(126, false)).c(2.0F).b(5.0F).a(e).b("woodSlab");
    public static final Block COCOA = (new BlockCocoa(127)).c(0.2F).b(5.0F).a(e).b("cocoa").r();
    public static final Block SANDSTONE_STAIRS = (new BlockStairs(128, SANDSTONE, 0)).b("stairsSandStone").r();
    public static final Block EMERALD_ORE = (new BlockOre(129, 171)).c(3.0F).b(5.0F).a(h).b("oreEmerald");
    public static final Block ENDER_CHEST = (new BlockEnderChest(130)).c(22.5F).b(1000.0F).a(h).b("enderChest").r().a(0.5F);
    public static final BlockTripwireHook TRIPWIRE_SOURCE = (BlockTripwireHook) (new BlockTripwireHook(131)).b("tripWireSource").r();
    public static final Block TRIPWIRE = (new BlockTripwire(132)).b("tripWire").r();
    public static final Block EMERALD_BLOCK = (new BlockOreBlock(133, 25)).c(5.0F).b(10.0F).a(i).b("blockEmerald");
    public static final Block SPRUCE_WOOD_STAIRS = (new BlockStairs(134, WOOD, 1)).b("stairsWoodSpruce").r();
    public static final Block BIRCH_WOOD_STAIRS = (new BlockStairs(135, WOOD, 2)).b("stairsWoodBirch").r();
    public static final Block JUNGLE_WOOD_STAIRS = (new BlockStairs(136, WOOD, 3)).b("stairsWoodJungle").r();
    public static final Block COMMAND = (new BlockCommand(137)).b("commandBlock");
    public static final Block BEACON = (new BlockBeacon(138)).b("beacon").a(1.0F);
    public static final Block COBBLE_WALL = (new BlockCobbleWall(139, COBBLESTONE)).b("cobbleWall");
    public static final Block FLOWER_POT = (new BlockFlowerPot(140)).c(0.0F).a(d).b("flowerPot");
    public static final Block CARROTS = (new BlockCarrots(141)).b("carrots");
    public static final Block POTATOES = (new BlockPotatoes(142)).b("potatoes");
    public static final Block WOOD_BUTTON = (new BlockButton(143, WOOD.textureId, true)).c(0.5F).a(e).b("button").r();
    public static final Block SKULL = (new BlockSkull(144)).c(1.0F).a(h).b("skull").r();
    public static final Block ANVIL = (new BlockAnvil(145)).c(5.0F).a(o).b(2000.0F).b("anvil").r();
    public int textureId;
    public final int id;
    protected float strength;
    protected float durability;
    protected boolean cp;
    protected boolean cq;
    protected boolean cr;
    protected boolean isTileEntity;
    protected double minX;
    protected double minY;
    protected double minZ;
    protected double maxX;
    protected double maxY;
    protected double maxZ;
    public StepSound stepSound;
    public float cA;
    public final Material material;
    public float frictionFactor;
    private String name;

    protected Block(int i, Material material) {
        this.cp = true;
        this.cq = true;
        this.stepSound = d;
        this.cA = 1.0F;
        this.frictionFactor = 0.6F;
        if (byId[i] != null) {
            throw new IllegalArgumentException("Slot " + i + " is already occupied by " + byId[i] + " when adding " + this);
        } else {
            this.material = material;
            byId[i] = this;
            this.id = i;
            this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            q[i] = this.c();
            lightBlock[i] = this.c() ? 255 : 0;
            s[i] = !material.blocksLight();
        }
    }

    protected Block r() {
        u[this.id] = true;
        return this;
    }

    protected void t_() {}

    protected Block(int i, int j, Material material) {
        this(i, material);
        this.textureId = j;
    }

    protected Block a(StepSound stepsound) {
        this.stepSound = stepsound;
        return this;
    }

    protected Block h(int i) {
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

    public static boolean i(int i) {
        Block block = byId[i];

        return block == null ? false : block.material.k() && block.b();
    }

    public boolean b() {
        return true;
    }

    public boolean c(IBlockAccess iblockaccess, int i, int j, int k) {
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

    protected Block s() {
        this.c(-1.0F);
        return this;
    }

    public float m(World world, int i, int j, int k) {
        return this.strength;
    }

    protected Block b(boolean flag) {
        this.cr = flag;
        return this;
    }

    public boolean isTicking() {
        return this.cr;
    }

    public boolean u() {
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

    public int a(int i, int j) {
        return this.a(i);
    }

    public int a(int i) {
        return this.textureId;
    }

    public void a(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, List list, Entity entity) {
        AxisAlignedBB axisalignedbb1 = this.e(world, i, j, k);

        if (axisalignedbb1 != null && axisalignedbb.a(axisalignedbb1)) {
            list.add(axisalignedbb1);
        }
    }

    public AxisAlignedBB e(World world, int i, int j, int k) {
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

    public void b(World world, int i, int j, int k, Random random) {}

    public void postBreak(World world, int i, int j, int k, int l) {}

    public void doPhysics(World world, int i, int j, int k, int l) {}

    public int r_() {
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
        float f = this.m(world, i, j, k);

        return f < 0.0F ? 0.0F : (!entityhuman.b(this) ? 1.0F / f / 100.0F : entityhuman.a(this) / f / 30.0F);
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
                        this.a(world, i, j, k, new ItemStack(l1, 1, this.getDropData(l)));
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

    protected void f(World world, int i, int j, int k, int l) {
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

    public void wasExploded(World world, int i, int j, int k) {}

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

    public void postPlace(World world, int i, int j, int k, int l, float f, float f1, float f2) {}

    public void attack(World world, int i, int j, int k, EntityHuman entityhuman) {}

    public void a(World world, int i, int j, int k, Entity entity, Vec3D vec3d) {}

    public void updateShape(IBlockAccess iblockaccess, int i, int j, int k) {}

    public final double v() {
        return this.minX;
    }

    public final double w() {
        return this.maxX;
    }

    public final double x() {
        return this.minY;
    }

    public final double y() {
        return this.maxY;
    }

    public final double z() {
        return this.minZ;
    }

    public final double A() {
        return this.maxZ;
    }

    public boolean b(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        return false;
    }

    public boolean isPowerSource() {
        return false;
    }

    public void a(World world, int i, int j, int k, Entity entity) {}

    public boolean c(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        return false;
    }

    public void f() {}

    public void a(World world, EntityHuman entityhuman, int i, int j, int k, int l) {
        entityhuman.a(StatisticList.C[this.id], 1);
        entityhuman.j(0.025F);
        if (this.s_() && EnchantmentManager.hasSilkTouchEnchantment(entityhuman)) {
            ItemStack itemstack = this.f_(l);

            if (itemstack != null) {
                this.a(world, i, j, k, itemstack);
            }
        } else {
            int i1 = EnchantmentManager.getBonusBlockLootEnchantmentLevel(entityhuman);

            this.c(world, i, j, k, l, i1);
        }
    }

    protected boolean s_() {
        return this.b() && !this.isTileEntity;
    }

    protected ItemStack f_(int i) {
        int j = 0;

        if (this.id >= 0 && this.id < Item.byId.length && Item.byId[this.id].l()) {
            j = i;
        }

        return new ItemStack(this.id, 1, j);
    }

    public int getDropCount(int i, Random random) {
        return this.a(random);
    }

    public boolean d(World world, int i, int j, int k) {
        return true;
    }

    public void postPlace(World world, int i, int j, int k, EntityLiving entityliving) {}

    public Block b(String s) {
        this.name = "tile." + s;
        return this;
    }

    public String getName() {
        return LocaleI18n.get(this.a() + ".name");
    }

    public String a() {
        return this.name;
    }

    public void b(World world, int i, int j, int k, int l, int i1) {}

    public boolean C() {
        return this.cq;
    }

    protected Block D() {
        this.cq = false;
        return this;
    }

    public int q_() {
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

    public void g(World world, int i, int j, int k, int l) {}

    public void f(World world, int i, int j, int k) {}

    public boolean l() {
        return true;
    }

    static {
        Item.byId[WOOL.id] = (new ItemCloth(WOOL.id - 256)).b("cloth");
        Item.byId[LOG.id] = (new ItemMultiTexture(LOG.id - 256, LOG, BlockLog.a)).b("log");
        Item.byId[WOOD.id] = (new ItemMultiTexture(WOOD.id - 256, WOOD, BlockWood.a)).b("wood");
        Item.byId[MONSTER_EGGS.id] = (new ItemMultiTexture(MONSTER_EGGS.id - 256, MONSTER_EGGS, BlockMonsterEggs.a)).b("monsterStoneEgg");
        Item.byId[SMOOTH_BRICK.id] = (new ItemMultiTexture(SMOOTH_BRICK.id - 256, SMOOTH_BRICK, BlockSmoothBrick.a)).b("stonebricksmooth");
        Item.byId[SANDSTONE.id] = (new ItemMultiTexture(SANDSTONE.id - 256, SANDSTONE, BlockSandStone.a)).b("sandStone");
        Item.byId[STEP.id] = (new ItemStep(STEP.id - 256, STEP, DOUBLE_STEP, false)).b("stoneSlab");
        Item.byId[DOUBLE_STEP.id] = (new ItemStep(DOUBLE_STEP.id - 256, STEP, DOUBLE_STEP, true)).b("stoneSlab");
        Item.byId[WOOD_STEP.id] = (new ItemStep(WOOD_STEP.id - 256, WOOD_STEP, WOOD_DOUBLE_STEP, false)).b("woodSlab");
        Item.byId[WOOD_DOUBLE_STEP.id] = (new ItemStep(WOOD_DOUBLE_STEP.id - 256, WOOD_STEP, WOOD_DOUBLE_STEP, true)).b("woodSlab");
        Item.byId[SAPLING.id] = (new ItemMultiTexture(SAPLING.id - 256, SAPLING, BlockSapling.a)).b("sapling");
        Item.byId[LEAVES.id] = (new ItemLeaves(LEAVES.id - 256)).b("leaves");
        Item.byId[VINE.id] = new ItemWithAuxData(VINE.id - 256, false);
        Item.byId[LONG_GRASS.id] = (new ItemWithAuxData(LONG_GRASS.id - 256, true)).a(new String[] { "shrub", "grass", "fern"});
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
                    byId[i].t_();
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

                if (s[i]) {
                    flag = true;
                }

                if (lightBlock[i] == 0) {
                    flag = true;
                }

                v[i] = flag;
            }
        }

        s[0] = true;
        StatisticList.b();
    }

    // CraftBukkit start
    public int getExpDrop(World world, int data, int enchantmentLevel) {
        return 0;
    }
    // CraftBukkit end
}
