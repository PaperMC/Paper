package io.papermc.paper.dialog;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import java.util.function.Supplier;

@ApiStatus.Experimental
public interface BodyElement {
    @ApiStatus.Experimental
    static PlainText plainText() {
        return DialogBridge.BRIDGE.plainText();
    }

    @ApiStatus.Experimental
    static Item item() {
        return DialogBridge.BRIDGE.itemElement();
    }

    @ApiStatus.Experimental
    interface PlainText extends BodyElement {
        Component component();
        PlainText component(Component component);

        int width();
        PlainText width(int width);
    }

    @ApiStatus.Experimental
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
