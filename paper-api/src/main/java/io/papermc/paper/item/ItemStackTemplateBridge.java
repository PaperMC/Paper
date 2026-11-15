package io.papermc.paper.item;

import java.util.Optional;
import java.util.ServiceLoader;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
interface ItemStackTemplateBridge {

    static ItemStackTemplateBridge bridge() {
        final class Holder {
            static final Optional<ItemStackTemplateBridge> INSTANCE = ServiceLoader.load(ItemStackTemplateBridge.class, ItemStackTemplateBridge.class.getClassLoader()).findFirst();
        }

        return Holder.INSTANCE.orElseThrow();
    }

    ItemStackTemplate.Builder builder(ItemType type);
}
