package io.papermc.testplugin;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.WrittenBookContent;
import io.papermc.paper.event.player.PlayerInsertLecternBookEvent;
import io.papermc.paper.event.player.PlayerLecternPageChangeEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTakeLecternBookEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.MenuType;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

        // io.papermc.testplugin.brigtests.Registration.registerViaOnEnable(this);
    }

    @EventHandler
    public void onLecternOne(PlayerLecternPageChangeEvent event) {
        event.getPlayer().sendMessage(event.getLectern() + " PlayerLecternPageChangeEvent");
    }

    @EventHandler
    public void onLecternTwo(PlayerTakeLecternBookEvent event) {
        event.getPlayer().sendMessage(event.getLectern() + " PlayerTakeLecternBookEvent");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_BLOCK && event.getHand() == EquipmentSlot.HAND) {
            final var loc = event.getClickedBlock().getLocation();
            final ItemStack book = ItemType.WRITTEN_BOOK.createItemStack();
            book.setData(DataComponentTypes.WRITTEN_BOOK_CONTENT, WrittenBookContent.writtenBookContent("JeryTheCarry", "JeryTheCarry")
                .addPage(Component.text("1"))
                .addPage(Component.text("2"))
                .addPage(Component.text("3"))
            );
            if (loc.getBlock().getType() == Material.LECTERN) {
                var view = MenuType.LECTERN.builder()
                    .checkReachable(false)
                    .title(Component.text("Lectern"))
                    .build(event.getPlayer());
                view.setPage(3);
                view.setItem(0, book);
                view.open();
            }
        }
    }

    @EventHandler
    public void onInventoryOpen(BlockBreakEvent event) {
        if (!event.getPlayer().getName().equals("Y2K_")) {
            return;
        }
        final var lecternView = MenuType.LECTERN.create(event.getPlayer(), Component.text("Title"));
        var bookItem = ItemStack.of(Material.WRITTEN_BOOK);
        bookItem.setData(DataComponentTypes.WRITTEN_BOOK_CONTENT, WrittenBookContent.writtenBookContent("JeryTheCarry", "JeryTheCarry")
            .addPage(Component.text("1"))
            .addPage(Component.text("2"))
            .addPage(Component.text("3"))
        );
        lecternView.setItem(0, bookItem);
        lecternView.setPage(3);
        lecternView.open();

    }
}
