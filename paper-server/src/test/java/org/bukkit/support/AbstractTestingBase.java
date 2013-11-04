package org.bukkit.support;

import com.google.common.collect.ImmutableList;
import java.util.List;
import net.minecraft.server.DispenserRegistry;
import org.bukkit.Material;
import org.junit.BeforeClass;

/**
 *  If you are getting: java.lang.ExceptionInInitializerError
 *    at net.minecraft.server.StatisticList.<clinit>(SourceFile:58)
 *    at net.minecraft.server.Item.<clinit>(SourceFile:252)
 *    at net.minecraft.server.Block.<clinit>(Block.java:577)
 *
 *  extend this class to solve it.
 */
public abstract class AbstractTestingBase {
    public static final List<Material> INVALIDATED_MATERIALS = ImmutableList.<Material>builder().add(Material.BREWING_STAND, Material.BED_BLOCK, Material.NETHER_WARTS, Material.CAULDRON, Material.FLOWER_POT, Material.CROPS, Material.SUGAR_CANE_BLOCK, Material.CAKE_BLOCK, Material.SKULL, Material.PISTON_EXTENSION, Material.PISTON_MOVING_PIECE, Material.GLOWING_REDSTONE_ORE, Material.DIODE_BLOCK_ON, Material.PUMPKIN_STEM, Material.SIGN_POST, Material.REDSTONE_COMPARATOR_ON, Material.TRIPWIRE, Material.REDSTONE_LAMP_ON, Material.MELON_STEM, Material.REDSTONE_TORCH_OFF, Material.REDSTONE_COMPARATOR_OFF, Material.REDSTONE_WIRE, Material.WALL_SIGN, Material.DIODE_BLOCK_OFF, Material.IRON_DOOR_BLOCK, Material.WOODEN_DOOR).add(Material.LOCKED_CHEST).build();

    @BeforeClass
    public static void setup() {
        DispenserRegistry.b();
        DummyServer.setup();
        DummyPotions.setup();
        DummyEnchantments.setup();
    }
}