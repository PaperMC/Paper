package io.papermc.paper.item;

import io.papermc.paper.datacomponent.DataComponentTypes;
import java.util.Set;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@AllFeatures
class ItemStackDataComponentEqualsTest {

    @Test
    public void testEqual() {
        ItemStack item1 = ItemStack.of(Material.STONE, 1);
        item1.setData(DataComponentTypes.MAX_STACK_SIZE, 32);
        item1.setData(DataComponentTypes.ITEM_NAME, Component.text("HI"));

        ItemStack item2 = ItemStack.of(Material.STONE, 1);
        item2.setData(DataComponentTypes.MAX_STACK_SIZE, 32);
        item2.setData(DataComponentTypes.ITEM_NAME, Component.text("HI"));

        Assertions.assertTrue(item1.matchesWithoutData(item2, Set.of()));
    }

    @Test
    public void testEqualIgnoreComponent() {
        ItemStack item1 = ItemStack.of(Material.STONE, 2);
        item1.setData(DataComponentTypes.MAX_STACK_SIZE, 1);

        ItemStack item2 = ItemStack.of(Material.STONE, 1);
        item2.setData(DataComponentTypes.MAX_STACK_SIZE, 2);

        Assertions.assertFalse(item1.matchesWithoutData(item2, Set.of(DataComponentTypes.MAX_STACK_SIZE)));
    }

    @Test
    public void testEqualIgnoreComponentAndSize() {
        ItemStack item1 = ItemStack.of(Material.STONE, 2);
        item1.setData(DataComponentTypes.MAX_STACK_SIZE, 1);

        ItemStack item2 = ItemStack.of(Material.STONE, 1);
        item2.setData(DataComponentTypes.MAX_STACK_SIZE, 2);

        Assertions.assertTrue(item1.matchesWithoutData(item2, Set.of(DataComponentTypes.MAX_STACK_SIZE), true));
    }

    @Test
    public void testEqualWithoutComponent() {
        ItemStack item1 = ItemStack.of(Material.STONE, 1);

        ItemStack item2 = ItemStack.of(Material.STONE, 1);
        item2.setData(DataComponentTypes.MAX_STACK_SIZE, 2);

        Assertions.assertFalse(item1.matchesWithoutData(item2, Set.of(DataComponentTypes.WRITTEN_BOOK_CONTENT)));
    }

    @Test
    public void testEqualRemoveComponent() {
        ItemStack item1 = ItemStack.of(Material.STONE, 1);
        item1.unsetData(DataComponentTypes.MAX_STACK_SIZE);

        ItemStack item2 = ItemStack.of(Material.STONE, 1);
        item2.unsetData(DataComponentTypes.MAX_STACK_SIZE);

        Assertions.assertTrue(item1.matchesWithoutData(item2, Set.of()));
    }

    @Test
    public void testEqualIncludeComponentIgnoreSize() {
        ItemStack item1 = ItemStack.of(Material.STONE, 2);
        item1.setData(DataComponentTypes.MAX_STACK_SIZE, 1);

        ItemStack item2 = ItemStack.of(Material.STONE, 1);
        item2.setData(DataComponentTypes.MAX_STACK_SIZE, 1);

        Assertions.assertTrue(item1.matchesWithoutData(item2, Set.of(), true));
    }

    @Test
    public void testAdvancedExample() {
        ItemStack oakLeaves = ItemStack.of(Material.OAK_LEAVES, 1);
        oakLeaves.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay().hideTooltip(true).build());
        oakLeaves.setData(DataComponentTypes.MAX_STACK_SIZE, 1);

        ItemStack otherOakLeavesItem = ItemStack.of(Material.OAK_LEAVES, 2);

        Assertions.assertTrue(oakLeaves.matchesWithoutData(otherOakLeavesItem, Set.of(DataComponentTypes.TOOLTIP_DISPLAY, DataComponentTypes.MAX_STACK_SIZE), true));
    }
}
