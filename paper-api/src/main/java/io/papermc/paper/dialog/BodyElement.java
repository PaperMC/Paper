package io.papermc.paper.dialog;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import java.util.function.Supplier;

public interface BodyElement {
    static PlainText plainText() {
        return DialogBuilderBridge.BRIDGE.plainText();
    }

    static Item item() {
        return DialogBuilderBridge.BRIDGE.itemElement();
    }

    interface PlainText extends BodyElement {
        Component component();
        PlainText component(Component component);

        int width();
        PlainText width(int width);
    }

    interface Item extends BodyElement {
        ItemStack item();
        Item item(ItemStack itemStack);
        default Item item(Supplier<ItemStack> itemStack) {
            this.item(itemStack.get());
            return this;
        }

        PlainText description();
        Item description(PlainText plainText);

        boolean showDecoration();
        Item showDecoration(boolean flag);

        boolean showTooltip();
        Item showTooltip(boolean flag);

        int width();
        Item width(int width);

        int height();
        Item height(int height);
    }
}
