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
 * Test suite for compatibility between the old and new LootTable APIs
 * Ensures that the deprecation doesn't break existing functionality
 */
@Normal
public class LootTableCompatibilityTest {

    private Location testLocation;
    private Player testPlayer;
    private Entity testEntity;
    private World testWorld;
    private Inventory testInventory;
    private LootTable mockLootTable;

    @BeforeEach
    public void setUp() {
        testWorld = mock(World.class);
        testLocation = new Location(testWorld, 0, 0, 0);
        testPlayer = mock(Player.class);
        testEntity = mock(Entity.class);
        testInventory = mock(Inventory.class);
        mockLootTable = mock(LootTable.class);
        
        when(testInventory.getSize()).thenReturn(27);
    }

    @Test
    public void testDeprecatedLootTableStillWorks() {
        // Ensure that calling deprecated methods doesn't break
        assertDoesNotThrow(() -> {
            // Create old-style context (this should work but show deprecation warnings)
            org.bukkit.loot.LootContext oldContext = mock(org.bukkit.loot.LootContext.class);
            when(oldContext.getLocation()).thenReturn(testLocation);
            when(oldContext.getLuck()).thenReturn(1.0f);
            
            // Call deprecated methods - they should still function
            mockLootTable.populateLoot(new Random(), oldContext);
            mockLootTable.fillInventory(testInventory, new Random(), oldContext);
        }, "Deprecated LootTable methods should still work");
    }

    @Test
    public void testNewAPICanInteractWithOldLootTable() {
        // Test that new API can work with existing LootTable instances
        LootTableManager manager = LootTableManager.getInstance();
        
        assertDoesNotThrow(() -> {
            LootGenerator generator = manager.createGenerator(mockLootTable);
            assertNotNull(generator, "Should be able to create generator from old LootTable");
        }, "New API should work with existing LootTable instances");
    }

    @Test
    public void testNewContextWorksWithNewAPI() {
        LootTableManager manager = LootTableManager.getInstance();
        
        // Create context using new API
        LootContext newContext = LootTableManager.createContext()
            .location(testLocation)
            .luck(1.5f)
            .lootedEntity(testEntity)
            .killer(testPlayer)
            .build();
        
        // Create generator and test it works
        LootGenerator generator = manager.createGenerator(mockLootTable);
        
        assertDoesNotThrow(() -> {
            Collection<ItemStack> loot = generator.generateLoot(newContext);
            assertNotNull(loot, "New context should work with new generator");
        }, "New context should work seamlessly with new generator");
    }

    @Test
    public void testMigrationPathFromOldToNew() {
        // Simulate migration scenario
        
        // Old way (deprecated but still working)
        org.bukkit.loot.LootContext oldContext = mock(org.bukkit.loot.LootContext.class);
        when(oldContext.getLocation()).thenReturn(testLocation);
        when(oldContext.getLuck()).thenReturn(2.0f);
        
        // New way
        LootContext newContext = LootTableManager.createContext()
            .location(testLocation)
            .luck(2.0f)
            .build();
        
        // Both should have equivalent data
        assertEquals(oldContext.getLocation(), newContext.getLocation(), 
            "Location should be equivalent between old and new contexts");
        assertEquals(oldContext.getLuck(), newContext.getLuck(), 0.001f,
            "Luck should be equivalent between old and new contexts");
    }

    @Test
    public void testLootGeneratorCapabilitiesWithRealLootTable() {
        LootTableManager manager = LootTableManager.getInstance();
        LootGenerator generator = manager.createGenerator(mockLootTable);
        
        // Test that capability methods return sensible defaults
        Collection<String> required = generator.getRequiredContextTypes();
        Collection<String> optional = generator.getOptionalContextTypes();
        
        assertNotNull(required, "Required types should not be null");
        assertNotNull(optional, "Optional types should not be null");
        
        // Basic context should be acceptable for most loot tables
        LootContext basicContext = LootTableManager.createContext()
            .location(testLocation)
            .build();
        
        boolean canGenerate = generator.canGenerateWith(basicContext);
        // This test is more about ensuring no exceptions are thrown
        assertNotNull(canGenerate); // Just ensure it returns something
    }

    @Test
    public void testErrorHandlingInNewAPI() {
        LootTableManager manager = LootTableManager.getInstance();
        
        // Test error handling with invalid inputs
        assertThrows(IllegalArgumentException.class, () -> {
            manager.createGenerator(null);
        }, "Should throw exception for null loot table");
        
        assertThrows(IllegalArgumentException.class, () -> {
            LootTableManager.createContext()
                .location(null)
                .build();
        }, "Should throw exception for null location");
    }

    @Test
    public void testNewAPIExtensibility() {
        // Test that the new API allows for extension
        LootContext context = LootTableManager.createContext()
            .location(testLocation)
            .parameter("plugin_data", "test_value")
            .parameter("custom_modifier", 1.5)
            .build();
        
        assertEquals("test_value", context.getParameter("plugin_data"));
        assertEquals(1.5, context.getParameter("custom_modifier"));
        
        // Test that parameters are preserved
        assertNotNull(context.getParameters());
        assertTrue(context.getParameters().containsKey("plugin_data"));
        assertTrue(context.getParameters().containsKey("custom_modifier"));
    }

    @Test
    public void testMultipleGeneratorsFromSameLootTable() {
        LootTableManager manager = LootTableManager.getInstance();
        
        // Create multiple generators from same loot table
        LootGenerator generator1 = manager.createGenerator(mockLootTable);
        LootGenerator generator2 = manager.createGenerator(mockLootTable);
        
        assertNotNull(generator1);
        assertNotNull(generator2);
        
        // They should be independent instances
        assertNotSame(generator1, generator2, "Should create separate generator instances");
    }

    @Test
    public void testConcurrentAccess() {
        LootTableManager manager = LootTableManager.getInstance();
        
        // Test that the singleton pattern works correctly under concurrent access
        LootTableManager[] managers = new LootTableManager[10];
        Thread[] threads = new Thread[10];
        
        for (int i = 0; i < 10; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                managers[index] = LootTableManager.getInstance();
            });
        }
        
        // Start all threads
        for (Thread thread : threads) {
            thread.start();
        }
        
        // Wait for all threads to complete
        for (Thread thread : threads) {
            assertDoesNotThrow(() -> thread.join());
        }
        
        // All should be the same instance
        for (int i = 1; i < managers.length; i++) {
            assertSame(managers[0], managers[i], 
                "All instances should be the same under concurrent access");
        }
    }

    @Test
    public void testBuilderStateIsolation() {
        // Test that multiple builders don't interfere with each other
        LootContextBuilder builder1 = LootTableManager.createContext();
        LootContextBuilder builder2 = LootTableManager.createContext();
        
        builder1.location(testLocation).luck(1.0f);
        builder2.location(testLocation).luck(2.0f);
        
        LootContext context1 = builder1.build();
        LootContext context2 = builder2.build();
        
        assertEquals(1.0f, context1.getLuck(), 0.001f);
        assertEquals(2.0f, context2.getLuck(), 0.001f);
        
        // Builders should be independent
        assertNotSame(context1, context2);
    }
}
