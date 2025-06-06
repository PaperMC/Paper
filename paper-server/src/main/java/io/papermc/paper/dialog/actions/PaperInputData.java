package io.papermc.paper.dialog.actions;

import io.papermc.paper.dialog.InputData;
import net.minecraft.nbt.CompoundTag;

public class PaperInputData implements InputData {
    CompoundTag compoundTag;

    public PaperInputData(CompoundTag compoundTag) {
        this.compoundTag = compoundTag;
    }

    @Override
    public String getString(final String key) {
        return compoundTag.getStringOr(key, "");
    }

    @Override
    public Float getNumber(final String key) {
        return compoundTag.getFloatOr(key, 0.0f);
    }

    @Override
    public Boolean getBoolean(final String key) {
        return compoundTag.getBooleanOr(key, false);
    }

    @Override
    public InputData put(final String key, final String value) {
        this.compoundTag.putString(key, value);
        return this;
    }

    @Override
    public InputData put(final String key, final float value) {
        this.compoundTag.putFloat(key, value);
        return this;
    }

    @Override
    public InputData put(final String key, final boolean value) {
        this.compoundTag.putBoolean(key, value);
        return this;
    }
}
