package io.papermc.paper.datacomponent.item;

import org.bukkit.Color;
import org.bukkit.craftbukkit.util.Handleable;

public record PaperDyedItemColor(
    net.minecraft.world.item.component.DyedItemColor impl
) implements DyedItemColor, Handleable<net.minecraft.world.item.component.DyedItemColor> {

    @Override
    public net.minecraft.world.item.component.DyedItemColor getHandle() {
        return this.impl;
    }

    @Override
    public Color color() {
        return Color.fromRGB(this.impl.rgb() & 0x00FFFFFF); // skip alpha channel
    }

    static final class BuilderImpl implements DyedItemColor.Builder {

        private Color color = Color.WHITE;

        @Override
        public DyedItemColor.Builder color(final Color color) {
            this.color = color;
            return this;
        }

        @Override
        public DyedItemColor build() {
            return new PaperDyedItemColor(new net.minecraft.world.item.component.DyedItemColor(
                this.color.asRGB())
            );
        }
    }
}
