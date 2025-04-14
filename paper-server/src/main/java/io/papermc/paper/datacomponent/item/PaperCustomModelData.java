package io.papermc.paper.datacomponent.item;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.booleans.BooleanArrayList;
import it.unimi.dsi.fastutil.booleans.BooleanList;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.floats.FloatList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collections;
import java.util.List;
import io.papermc.paper.util.MCUtil;
import org.bukkit.Color;
import org.bukkit.craftbukkit.util.Handleable;

public record PaperCustomModelData(
    net.minecraft.world.item.component.CustomModelData impl
) implements CustomModelData, Handleable<net.minecraft.world.item.component.CustomModelData> {

    @Override
    public net.minecraft.world.item.component.CustomModelData getHandle() {
        return this.impl;
    }

    @Override
    public List<Float> floats() {
        return Collections.unmodifiableList(this.impl.floats());
    }

    @Override
    public List<Boolean> flags() {
        return Collections.unmodifiableList(this.impl.flags());
    }

    @Override
    public List<String> strings() {
        return Collections.unmodifiableList(this.impl.strings());
    }

    @Override
    public List<Color> colors() {
        return MCUtil.transformUnmodifiable(this.impl.colors(), color -> Color.fromRGB(color & 0x00FFFFFF)); // skip alpha channel
    }

    static final class BuilderImpl implements CustomModelData.Builder {

        private final FloatList floats = new FloatArrayList();
        private final BooleanList flags = new BooleanArrayList();
        private final List<String> strings = new ObjectArrayList<>();
        private final IntList colors = new IntArrayList();

        @Override
        public Builder addFloat(final float f) {
            this.floats.add(f);
            return this;
        }

        @Override
        public Builder addFloats(final List<Float> floats) {
            for (Float f : floats) {
                Preconditions.checkArgument(f != null, "Float cannot be null");
            }
            this.floats.addAll(floats);
            return this;
        }

        @Override
        public Builder addFlag(final boolean flag) {
            this.flags.add(flag);
            return this;
        }

        @Override
        public Builder addFlags(final List<Boolean> flags) {
            for (Boolean flag : flags) {
                Preconditions.checkArgument(flag != null, "Flag cannot be null");
            }
            this.flags.addAll(flags);
            return this;
        }

        @Override
        public Builder addString(final String string) {
            Preconditions.checkArgument(string != null, "String cannot be null");
            this.strings.add(string);
            return this;
        }

        @Override
        public Builder addStrings(final List<String> strings) {
            strings.forEach(this::addString);
            return this;
        }

        @Override
        public Builder addColor(final Color color) {
            Preconditions.checkArgument(color != null, "Color cannot be null");
            this.colors.add(color.asRGB());
            return this;
        }

        @Override
        public Builder addColors(final List<Color> colors) {
            colors.forEach(this::addColor);
            return this;
        }

        @Override
        public CustomModelData build() {
            return new PaperCustomModelData(
                new net.minecraft.world.item.component.CustomModelData(
                    new FloatArrayList(this.floats),
                    new BooleanArrayList(this.flags),
                    new ObjectArrayList<>(this.strings),
                    new IntArrayList(this.colors)
                )
            );
        }
    }
}
