package io.papermc.paper.dialog.body;

import io.papermc.paper.dialog.BodyElement;
import net.minecraft.server.dialog.body.DialogBody;
import net.minecraft.server.dialog.body.ItemBody;
import net.minecraft.server.dialog.body.PlainMessage;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import java.util.Optional;

public class PaperItemElement implements BodyElementConversion, BodyElement.Item {
    ItemStack itemStack = ItemStack.empty();
    PlainText description = null;
    boolean showDecoration = true;
    boolean showTooltip = true;
    int width = 16;
    int height = 16;

    @Override
    public ItemStack item() {
        return this.itemStack;
    }

    @Override
    public Item item(final ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }

    @Override
    public PlainText description() {
        return this.description;
    }

    @Override
    public Item description(final PlainText plainText) {
        this.description = plainText;
        return this;
    }

    @Override
    public boolean showDecoration() {
        return this.showDecoration;
    }

    @Override
    public Item showDecoration(final boolean flag) {
        this.showDecoration = flag;
        return this;
    }

    @Override
    public boolean showTooltip() {
        return this.showTooltip;
    }

    @Override
    public Item showTooltip(final boolean flag) {
        this.showTooltip = flag;
        return this;
    }

    @Override
    public int width() {
        return this.width;
    }

    @Override
    public Item width(final int width) {
        this.width = width;
        return this;
    }

    @Override
    public int height() {
        return this.height;
    }

    @Override
    public Item height(final int height) {
        this.height = height;
        return this;
    }

    @Override
    public DialogBody dialogBody() {
        return new ItemBody(
            CraftItemStack.asNMSCopy(this.itemStack),
            Optional.ofNullable(this.description)
                .map(x -> ((PaperPlainText) x).dialogBody())
                .map(x -> (PlainMessage) x),
            this.showDecoration,
            this.showTooltip,
            this.width,
            this.height
        );
    }
}
