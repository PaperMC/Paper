package org.bukkit;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.material.*;

/**
 * An enum of all material ids accepted by the official server + client
 */
public enum Material {
    AIR(0),
    STONE(1),
    GRASS(2),
    DIRT(3),
    COBBLESTONE(4),
    WOOD(5),
    SAPLING(6),
    BEDROCK(7),
    WATER(8),
    STATIONARY_WATER(9),
    LAVA(10),
    STATIONARY_LAVA(11),
    SAND(12),
    GRAVEL(13),
    GOLD_ORE(14),
    IRON_ORE(15),
    COAL_ORE(16),
    LOG(17),
    LEAVES(18),
    SPONGE(19),
    GLASS(20),
    LAPIS_ORE(21),
    LAPIS_BLOCK(22),
    DISPENSER(23),
    SANDSTONE(24),
    NOTE_BLOCK(25),
    WOOL(35, Wool.class),
    YELLOW_FLOWER(37),
    RED_ROSE(38),
    BROWN_MUSHROOM(39),
    RED_MUSHROOM(40),
    GOLD_BLOCK(41),
    IRON_BLOCK(42),
    DOUBLE_STEP(43),
    STEP(44),
    BRICK(45),
    TNT(46),
    BOOKSHELF(47),
    MOSSY_COBBLESTONE(48),
    OBSIDIAN(49),
    TORCH(50, Torch.class),
    FIRE(51),
    MOB_SPAWNER(52),
    WOOD_STAIRS(53),
    CHEST(54),
    REDSTONE_WIRE(55, RedstoneWire.class),
    DIAMOND_ORE(56),
    DIAMOND_BLOCK(57),
    WORKBENCH(58),
    CROPS(59),
    SOIL(60),
    FURNACE(61),
    BURNING_FURNACE(62),
    SIGN_POST(63),
    WOODEN_DOOR(64),
    LADDER(65, Ladder.class),
    RAILS(66),
    COBBLESTONE_STAIRS(67),
    WALL_SIGN(68),
    LEVER(69, Lever.class),
    STONE_PLATE(70),
    IRON_DOOR_BLOCK(71),
    WOOD_PLATE(72),
    REDSTONE_ORE(73),
    GLOWING_REDSTONE_ORE(74),
    REDSTONE_TORCH_OFF(75, RedstoneTorch.class),
    REDSTONE_TORCH_ON(76, RedstoneTorch.class),
    STONE_BUTTON(77),
    SNOW(78),
    ICE(79),
    SNOW_BLOCK(80),
    CACTUS(81),
    CLAY(82),
    SUGAR_CANE_BLOCK(83),
    JUKEBOX(84),
    FENCE(85),
    PUMPKIN(86),
    NETHERRACK(87),
    SOUL_SAND(88),
    GLOWSTONE(89),
    PORTAL(90),
    JACK_O_LANTERN(91),
    CAKE_BLOCK(92),
    IRON_SPADE(256),
    IRON_PICKAXE(257),
    IRON_AXE(258),
    FLINT_AND_STEEL(259),
    APPLE(260),
    BOW(261),
    ARROW(262),
    COAL(263),
    DIAMOND(264),
    IRON_INGOT(265),
    GOLD_INGOT(266),
    IRON_SWORD(267),
    WOOD_SWORD(268),
    WOOD_SPADE(269),
    WOOD_PICKAXE(270),
    WOOD_AXE(271),
    STONE_SWORD(272),
    STONE_SPADE(273),
    STONE_PICKAXE(274),
    STONE_AXE(275),
    DIAMOND_SWORD(276),
    DIAMOND_SPADE(277),
    DIAMOND_PICKAXE(278),
    DIAMOND_AXE(279),
    STICK(280),
    BOWL(281),
    MUSHROOM_SOUP(282),
    GOLD_SWORD(283),
    GOLD_SPADE(284),
    GOLD_PICKAXE(285),
    GOLD_AXE(286),
    STRING(287),
    FEATHER(288),
    SULPHUR(289),
    WOOD_HOE(290),
    STONE_HOE(291),
    IRON_HOE(292),
    DIAMOND_HOE(293),
    GOLD_HOE(294),
    SEEDS(295),
    WHEAT(296),
    BREAD(297),
    LEATHER_HELMET(298),
    LEATHER_CHESTPLATE(299),
    LEATHER_LEGGINGS(300),
    LEATHER_BOOTS(301),
    CHAINMAIL_HELMET(302),
    CHAINMAIL_CHESTPLATE(303),
    CHAINMAIL_LEGGINGS(304),
    CHAINMAIL_BOOTS(305),
    IRON_HELMET(306),
    IRON_CHESTPLATE(307),
    IRON_LEGGINGS(308),
    IRON_BOOTS(309),
    DIAMOND_HELMET(310),
    DIAMOND_CHESTPLATE(311),
    DIAMOND_LEGGINGS(312),
    DIAMOND_BOOTS(313),
    GOLD_HELMET(314),
    GOLD_CHESTPLATE(315),
    GOLD_LEGGINGS(316),
    GOLD_BOOTS(317),
    FLINT(318),
    PORK(319),
    GRILLED_PORK(320),
    PAINTING(321),
    GOLDEN_APPLE(322),
    SIGN(323),
    WOOD_DOOR(324),
    BUCKET(325),
    WATER_BUCKET(326),
    LAVA_BUCKET(327),
    MINECART(328),
    SADDLE(329),
    IRON_DOOR(330),
    REDSTONE(331, RedstoneWire.class),
    SNOW_BALL(332),
    BOAT(333),
    LEATHER(334),
    MILK_BUCKET(335),
    CLAY_BRICK(336),
    CLAY_BALL(337),
    SUGAR_CANE(338),
    PAPER(339),
    BOOK(340),
    SLIME_BALL(341),
    STORAGE_MINECART(342),
    POWERED_MINECART(343),
    EGG(344),
    COMPASS(345),
    FISHING_ROD(346),
    WATCH(347),
    GLOWSTONE_DUST(348),
    RAW_FISH(349),
    COOKED_FISH(350),
    INK_SACK(351, Dye.class),
    BONE(352),
    SUGAR(353),
    CAKE(354),
    GOLD_RECORD(2256),
    GREEN_RECORD(2257);

    private final int id;
    private final Class<? extends MaterialData> data;
    private static final Map<Integer, Material> lookupId = new HashMap<Integer, Material>();
    private static final Map<String, Material> lookupName = new HashMap<String, Material>();

    private Material(final int id) {
        this(id, null);
    }

    private Material(final int id, final Class<? extends MaterialData> data) {
        this.id = id;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public Class<? extends MaterialData> getData() {
        return data;
    }

    public MaterialData getNewData(final byte raw) {
        if (data == null) {
            return null;
        }

        try {
            Constructor<? extends MaterialData> ctor = data.getConstructor(int.class, byte.class);
            return ctor.newInstance(id, raw);
        } catch (InstantiationException ex) {
            Logger.getLogger(Material.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Material.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Material.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(Material.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(Material.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(Material.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public boolean isBlock() {
        return id < 256; 
    }
    
    public static Material getMaterial(final int id) {
        return lookupId.get(id);
    }

    public static Material getMaterial(final String name) {
        return lookupName.get(name);
    }
    
    static {
        for (Material material : values()) {
            lookupId.put(material.getId(), material);
            lookupName.put(material.name(), material);
        }
    }
}
