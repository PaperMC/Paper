package io.papermc.paper.loot;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collection;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test suite for the server-side Paper LootTable implementation
 * Tests NMS integration and server-specific functionality
 */
@Normal
public class PaperLootTableImplementationTest {

    private Location testLocation;
    private Player testPlayer;
    private Entity testEntity;
    private World testWorld;
    private ItemStack testTool;
    private LootTable mockLootTable;

    @BeforeEach
    public void setUp() {
        testWorld = mock(World.class);
        testLocation = new Location(testWorld, 10, 64, 20);
        testPlayer = mock(Player.class);
        testEntity = mock(Entity.class);
        testTool = new ItemStack(Material.DIAMOND_PICKAXE);
        mockLootTable = mock(LootTable.class);
        
        when(testPlayer.getName()).thenReturn("TestPlayer");
        when(testEntity.getType()).thenReturn(org.bukkit.entity.EntityType.ZOMBIE);
    }

    @Test
    public void testPaperLootContextBuilderImplementation() {
        // Test that the Paper implementation handles all context types properly
        LootContextBuilder builder = LootTableManager.createContext();
        
        LootContext context = builder
            .location(testLocation)
            .luck(3.5f)
            .lootedEntity(testEntity)
            .killer(testPlayer)
            .tool(testTool)
            .explosionRadius(8.0f)
            .parameter("mining_speed", 1.2)
            .parameter("block_state", "minecraft:chest[facing=north]")
            .build();

        // Verify all values are properly stored and accessible
        assertEquals(testLocation, context.getLocation());
        assertEquals(3.5f, context.getLuck(), 0.001f);
        assertEquals(testEntity, context.getLootedEntity());
        assertEquals(testPlayer, context.getKiller());
        assertEquals(testTool, context.getTool());
        assertEquals(8.0f, context.getExplosionRadius(), 0.001f);
        assertEquals(1.2, context.getParameter("mining_speed"));
        assertEquals("minecraft:chest[facing=north]", context.getParameter("block_state"));
    }

    @Test
    public void testPaperLootGeneratorImplementation() {
        LootTableManager manager = LootTableManager.getInstance();
        LootGenerator generator = manager.createGenerator(mockLootTable);
        
        LootContext context = LootTableManager.createContext()
            .location(testLocation)
            .luck(1.0f)
            .build();

        // Test that generator methods work without throwing exceptions
        assertDoesNotThrow(() -> {
            Collection<ItemStack> loot = generator.generateLoot(context);
            assertNotNull(loot, "Generated loot should not be null");
        });

        assertDoesNotThrow(() -> {
            Random customRandom = new Random(12345L); // Fixed seed for reproducible tests
            Collection<ItemStack> loot = generator.generateLoot(context, customRandom);
            assertNotNull(loot, "Generated loot with custom random should not be null");
        });

        assertDoesNotThrow(() -> {
            ItemStack singleItem = generator.generateSingleLoot(context);
            // Single item can be null if no loot is generated
        });
    }

    @Test
    public void testContextParameterValidation() {
        LootContextBuilder builder = LootTableManager.createContext();
        
        // Test parameter validation
        assertDoesNotThrow(() -> {
            builder.parameter("valid_string", "test");
            builder.parameter("valid_number", 42);
            builder.parameter("valid_boolean", true);
            builder.parameter("valid_double", 3.14159);
        }, "Valid parameters should be accepted");

        // Test null parameter key handling
        assertThrows(IllegalArgumentException.class, () -> {
            builder.parameter(null, "value");
        }, "Null parameter key should throw exception");
    }

    @Test
    public void testLootGeneratorContextValidation() {
        LootTableManager manager = LootTableManager.getInstance();
        LootGenerator generator = manager.createGenerator(mockLootTable);
        
        // Test context requirements
        Collection<String> requiredTypes = generator.getRequiredContextTypes();
        Collection<String> optionalTypes = generator.getOptionalContextTypes();
        
        assertNotNull(requiredTypes);
        assertNotNull(optionalTypes);
        
        // Test context validation
        LootContext minimalContext = LootTableManager.createContext()
            .location(testLocation)
            .build();
        
        boolean canGenerate = generator.canGenerateWith(minimalContext);
        assertNotNull(canGenerate); // Should return a boolean value
        
        LootContext fullContext = LootTableManager.createContext()
            .location(testLocation)
            .luck(1.0f)
            .lootedEntity(testEntity)
            .killer(testPlayer)
            .tool(testTool)
            .build();
        
        boolean canGenerateWithFull = generator.canGenerateWith(fullContext);
        assertNotNull(canGenerateWithFull); // Should return a boolean value
    }

    @Test
    public void testInventoryFillingImplementation() {
        LootTableManager manager = LootTableManager.getInstance();
        LootGenerator generator = manager.createGenerator(mockLootTable);
        
        Inventory mockInventory = mock(Inventory.class);
        when(mockInventory.getSize()).thenReturn(27);
        when(mockInventory.firstEmpty()).thenReturn(0, 1, 2, -1); // Simulate filling slots
        
        LootContext context = LootTableManager.createContext()
            .location(testLocation)
            .luck(1.5f)
            .build();

        assertDoesNotThrow(() -> {
            generator.fillInventory(mockInventory, context);
        }, "fillInventory should complete without exceptions");

        assertDoesNotThrow(() -> {
            Random customRandom = new Random(67890L);
            generator.fillInventory(mockInventory, context, customRandom);
        }, "fillInventory with custom random should complete without exceptions");
    }

    @Test
    public void testComplexContextScenarios() {
        // Test mining scenario
        LootContext miningContext = LootTableManager.createContext()
            .location(testLocation)
            .luck(2.0f)
            .killer(testPlayer)
            .tool(testTool)
            .parameter("fortune_level", 3)
            .parameter("silk_touch", false)
            .build();

        assertEquals(3, miningContext.getParameter("fortune_level"));
        assertEquals(false, miningContext.getParameter("silk_touch"));

        // Test entity death scenario
        LootContext entityDeathContext = LootTableManager.createContext()
            .location(testLocation)
            .luck(0.5f)
            .lootedEntity(testEntity)
            .killer(testPlayer)
            .tool(testTool)
            .parameter("looting_level", 2)
            .parameter("on_fire", true)
            .build();

        assertEquals(2, entityDeathContext.getParameter("looting_level"));
        assertEquals(true, entityDeathContext.getParameter("on_fire"));

        // Test explosion scenario
        LootContext explosionContext = LootTableManager.createContext()
            .location(testLocation)
            .explosionRadius(4.0f)
            .parameter("explosion_type", "creeper")
            .build();

        assertEquals(4.0f, explosionContext.getExplosionRadius(), 0.001f);
        assertEquals("creeper", explosionContext.getParameter("explosion_type"));
    }

    @Test
    public void testParameterTypeRetrieval() {
        LootContext context = LootTableManager.createContext()
            .location(testLocation)
            .parameter("string_param", "test_string")
            .parameter("int_param", 42)
            .parameter("double_param", 3.14159)
            .parameter("boolean_param", true)
            .parameter("entity_param", testEntity)
            .parameter("itemstack_param", testTool)
            .build();

        // Test type-safe parameter retrieval
        assertEquals("test_string", context.getParameter("string_param"));
        assertEquals(42, context.getParameter("int_param"));
        assertEquals(3.14159, context.getParameter("double_param"));
        assertEquals(true, context.getParameter("boolean_param"));
        assertSame(testEntity, context.getParameter("entity_param"));
        assertSame(testTool, context.getParameter("itemstack_param"));
        assertNull(context.getParameter("nonexistent_param"));
    }

    @Test
    public void testManagerImplementationSingleton() {
        // Test that the manager implementation maintains singleton behavior
        LootTableManager manager1 = LootTableManager.getInstance();
        LootTableManager manager2 = LootTableManager.getInstance();
        
        assertSame(manager1, manager2, "Manager should be singleton");
        
        // Test that context builders from same manager work consistently
        LootContextBuilder builder1 = manager1.createContext();
        LootContextBuilder builder2 = manager2.createContext();
        
        assertNotNull(builder1);
        assertNotNull(builder2);
        // Builders can be different instances, but should work the same way
    }

    @Test
    public void testErrorHandlingInImplementation() {
        LootTableManager manager = LootTableManager.getInstance();
        
        // Test null loot table handling
        assertThrows(IllegalArgumentException.class, () -> {
            manager.createGenerator(null);
        }, "Should reject null loot table");

        // Test invalid context building
        assertThrows(IllegalArgumentException.class, () -> {
            LootTableManager.createContext()
                .location(null)
                .build();
        }, "Should reject null location");

        // Test negative luck values (should be allowed)
        assertDoesNotThrow(() -> {
            LootTableManager.createContext()
                .location(testLocation)
                .luck(-1.0f)
                .build();
        }, "Negative luck should be allowed");

        // Test negative explosion radius (should be allowed for consistency)
        assertDoesNotThrow(() -> {
            LootTableManager.createContext()
                .location(testLocation)
                .explosionRadius(-1.0f)
                .build();
        }, "Negative explosion radius should be allowed");
    }

    @Test
    public void testStringRepresentations() {
        LootContext context = LootTableManager.createContext()
            .location(testLocation)
            .luck(1.5f)
            .lootedEntity(testEntity)
            .parameter("test", "value")
            .build();

        String contextString = context.toString();
        assertNotNull(contextString);
        assertTrue(contextString.contains("LootContext"));
        assertTrue(contextString.contains("location"));
        assertTrue(contextString.contains("luck"));
    }
}
