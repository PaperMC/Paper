package io.papermc.paper.loot;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
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
 * Test suite for the new Paper LootTable API
 * Tests the builder pattern, context creation, and loot generation functionality
 */
@Normal
public class LootTableAPITest {

    private Location testLocation;
    private Player testPlayer;
    private Entity testEntity;
    private World testWorld;

    @BeforeEach
    public void setUp() {
        // Create mock objects for testing
        testWorld = mock(World.class);
        testLocation = new Location(testWorld, 0, 0, 0);
        testPlayer = mock(Player.class);
        testEntity = mock(Entity.class);
    }

    @Test
    public void testLootContextBuilderBasic() {
        LootContextBuilder builder = LootTableManager.createContext();
        
        assertNotNull(builder, "Builder should not be null");
        assertTrue(builder instanceof LootContextBuilder, "Should return LootContextBuilder instance");
        
        // Test method chaining
        LootContextBuilder chainedBuilder = builder.location(testLocation).luck(1.5f);
        assertSame(builder, chainedBuilder, "Builder should support method chaining");
    }

    @Test
    public void testLootContextBuilderWithAllParameters() {
        LootContext context = LootTableManager.createContext()
            .location(testLocation)
            .luck(2.5f)
            .lootedEntity(testEntity)
            .killer(testPlayer)
            .build();

        assertNotNull(context, "Context should not be null");
        assertEquals(testLocation, context.getLocation(), "Location should match");
        assertEquals(2.5f, context.getLuck(), 0.001f, "Luck should match");
        assertEquals(testEntity, context.getLootedEntity(), "Looted entity should match");
        assertEquals(testPlayer, context.getKiller(), "Killer should match");
    }

    @Test
    public void testLootContextBuilderWithMinimalParameters() {
        LootContext context = LootTableManager.createContext()
            .location(testLocation)
            .build();

        assertNotNull(context, "Context should not be null");
        assertEquals(testLocation, context.getLocation(), "Location should match");
        assertEquals(0.0f, context.getLuck(), 0.001f, "Default luck should be 0");
        assertNull(context.getLootedEntity(), "Looted entity should be null by default");
        assertNull(context.getKiller(), "Killer should be null by default");
    }

    @Test
    public void testLootTableManagerSingleton() {
        LootTableManager manager1 = LootTableManager.getInstance();
        LootTableManager manager2 = LootTableManager.getInstance();
        
        assertNotNull(manager1, "Manager instance should not be null");
        assertSame(manager1, manager2, "Should return same singleton instance");
    }

    @Test
    public void testLootGeneratorCreation() {
        LootTableManager manager = LootTableManager.getInstance();
        
        // Test with mock LootTable
        LootTable mockLootTable = mock(LootTable.class);
        LootGenerator generator = manager.createGenerator(mockLootTable);
        
        assertNotNull(generator, "Generator should not be null");
        assertTrue(generator instanceof LootGenerator, "Should return LootGenerator instance");
    }

    @Test
    public void testLootGeneratorCapabilities() {
        LootTableManager manager = LootTableManager.getInstance();
        LootTable mockLootTable = mock(LootTable.class);
        LootGenerator generator = manager.createGenerator(mockLootTable);
        
        LootContext context = LootTableManager.createContext()
            .location(testLocation)
            .build();

        // Test capability checking
        Collection<String> requiredTypes = generator.getRequiredContextTypes();
        Collection<String> optionalTypes = generator.getOptionalContextTypes();
        
        assertNotNull(requiredTypes, "Required context types should not be null");
        assertNotNull(optionalTypes, "Optional context types should not be null");
        
        boolean canGenerate = generator.canGenerateWith(context);
        // This should not throw an exception regardless of result
        assertNotNull(canGenerate); // Box the boolean to ensure no exception
    }

    @Test
    public void testLootGenerationMethods() {
        LootTableManager manager = LootTableManager.getInstance();
        LootTable mockLootTable = mock(LootTable.class);
        LootGenerator generator = manager.createGenerator(mockLootTable);
        
        LootContext context = LootTableManager.createContext()
            .location(testLocation)
            .luck(1.0f)
            .build();

        // Test loot generation methods exist and don't throw exceptions
        assertDoesNotThrow(() -> {
            Collection<ItemStack> loot = generator.generateLoot(context);
            assertNotNull(loot, "Generated loot should not be null");
        }, "generateLoot should not throw exceptions");

        assertDoesNotThrow(() -> {
            Random random = new Random();
            Collection<ItemStack> loot = generator.generateLoot(context, random);
            assertNotNull(loot, "Generated loot with random should not be null");
        }, "generateLoot with random should not throw exceptions");

        assertDoesNotThrow(() -> {
            ItemStack singleLoot = generator.generateSingleLoot(context);
            // Can be null if no loot generated
        }, "generateSingleLoot should not throw exceptions");
    }

    @Test
    public void testInventoryFilling() {
        LootTableManager manager = LootTableManager.getInstance();
        LootTable mockLootTable = mock(LootTable.class);
        LootGenerator generator = manager.createGenerator(mockLootTable);
        
        LootContext context = LootTableManager.createContext()
            .location(testLocation)
            .build();

        Inventory mockInventory = mock(Inventory.class);
        when(mockInventory.getSize()).thenReturn(27);

        assertDoesNotThrow(() -> {
            generator.fillInventory(mockInventory, context);
        }, "fillInventory should not throw exceptions");

        assertDoesNotThrow(() -> {
            Random random = new Random();
            generator.fillInventory(mockInventory, context, random);
        }, "fillInventory with random should not throw exceptions");
    }

    @Test
    public void testLootContextBuilderValidation() {
        // Test that null location throws exception
        assertThrows(IllegalArgumentException.class, () -> {
            LootTableManager.createContext()
                .location(null)
                .build();
        }, "Building context with null location should throw IllegalArgumentException");
    }

    @Test
    public void testParameterTypeSafety() {
        LootContext context = LootTableManager.createContext()
            .location(testLocation)
            .parameter("string", "value")
            .parameter("number", 123)
            .parameter("entity", testEntity)
            .build();

        // Test type-safe parameter retrieval
        assertEquals("value", context.getParameter("string"));
        assertEquals(123, context.getParameter("number"));
        assertSame(testEntity, context.getParameter("entity"));
        assertNull(context.getParameter("nonexistent"));
    }

    @Test
    public void testContextToString() {
        LootContext context = LootTableManager.createContext()
            .location(testLocation)
            .luck(1.5f)
            .parameter("test", "value")
            .build();

        String toString = context.toString();
        assertNotNull(toString, "toString should not return null");
        assertTrue(toString.contains("LootContext"), "toString should contain class name");
        assertTrue(toString.contains("luck=1.5"), "toString should contain luck value");
    }

    @Test
    public void testManagerFactoryMethods() {
        LootTableManager manager = LootTableManager.getInstance();
        
        // Test that all factory methods are accessible
        assertNotNull(manager.createContext(), "createContext should return non-null builder");
        
        // Test createGenerator with null should handle gracefully
        assertThrows(IllegalArgumentException.class, () -> {
            manager.createGenerator(null);
        }, "createGenerator with null should throw IllegalArgumentException");
    }

    @Test
    public void testContextBuilderReuse() {
        LootContextBuilder builder = LootTableManager.createContext();
        
        // Build first context
        LootContext context1 = builder
            .location(testLocation)
            .luck(1.0f)
            .build();
        
        // Build second context with same builder (should work independently)
        LootContext context2 = builder
            .location(testLocation)
            .luck(2.0f)
            .build();
        
        assertNotEquals(context1.getLuck(), context2.getLuck(), 
            "Different builds should have different values");
        assertEquals(1.0f, context1.getLuck(), 0.001f, "First context should retain its values");
        assertEquals(2.0f, context2.getLuck(), 0.001f, "Second context should have new values");
    }
}
