package io.papermc.testplugin;

import io.papermc.paper.event.player.PlayerLiddedOpenEvent;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.tag.Tag;
import io.papermc.paper.registry.tag.TagKey;
import java.util.Objects;
import java.util.logging.Logger;
import net.kyori.adventure.key.Key;
import org.bukkit.Registry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;

public class LiddedTestListener implements Listener {

    private static final TagKey<ItemType> WOOL_TAG_KEY = TagKey.create(RegistryKey.ITEM,
        Key.key("minecraft:wool"));
    private final Logger logger;

    public LiddedTestListener(Logger logger) {
        this.logger = logger;
    }

    @EventHandler
    public void onLiddedEvent(PlayerLiddedOpenEvent event) {
        Registry<ItemType> itemTypeRegistry = RegistryAccess.registryAccess()
            .getRegistry(RegistryKey.ITEM);
        if (!itemTypeRegistry.hasTag(WOOL_TAG_KEY)) {
            logger.warning("Wool tag not found");
            return;
        }
        Tag<ItemType> woolTag = itemTypeRegistry.getTag(WOOL_TAG_KEY);

        ItemStack mainHandItem = event.getPlayer().getInventory().getItemInMainHand();

        boolean isWool = woolTag.contains(
            TypedKey.create(RegistryKey.ITEM, itemTypeRegistry.getKeyOrThrow(
                Objects.requireNonNull(mainHandItem.getType().asItemType()))));

        if (isWool) {
            event.getPlayer().sendRichMessage(
                "<gray>Opening <gold>" + event.getLidded().getType().getKey().asString()
                    + "</gold> Quietly");
            event.setCancelled(true);
        }
    }

}
