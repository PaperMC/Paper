package io.papermc.testplugin;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.WrittenBookContent;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

        // io.papermc.testplugin.brigtests.Registration.registerViaOnEnable(this);
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
