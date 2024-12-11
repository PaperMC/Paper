package io.papermc.paper.datacomponent.item;

import org.bukkit.Color;
import org.bukkit.craftbukkit.util.Handleable;

public record PaperMapItemColor(
    net.minecraft.world.item.component.MapItemColor impl
) implements MapItemColor, Handleable<net.minecraft.world.item.component.MapItemColor> {

    @Override
    public net.minecraft.world.item.component.MapItemColor getHandle() {
        return this.impl;
    }

    @Override
    public Color color() {
        return Color.fromRGB(this.impl.rgb() & 0x00FFFFFF); // skip alpha channel
    }

    static final class BuilderImpl implements Builder {

        private Color color = Color.fromRGB(net.minecraft.world.item.component.MapItemColor.DEFAULT.rgb());

        @Override
        public Builder color(final Color color) {
            this.color = color;
            return this;
        }

        @Override
        public MapItemColor build() {
            return new PaperMapItemColor(new net.minecraft.world.item.component.MapItemColor(this.color.asRGB()));
        }
    }
}
