package org.bukkit.structure;

import com.google.common.base.Preconditions;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import java.util.Random;

public class PlacementOptions {

    private boolean includeEntities = true;
    private StructureRotation structureRotation = StructureRotation.NONE;
    private Mirror mirror = Mirror.NONE;
    private int palette = -1;
    private float integrity = 1;
    private final Random random;
    private boolean strict = false;
    private boolean applyWaterlogging = true;

    public PlacementOptions(Random random) {
        this.random = random;
    }

    public PlacementOptions includeEntities(boolean includeEntities) {
        this.includeEntities = includeEntities;
        return this;
    }

    public PlacementOptions structureRotation(StructureRotation structureRotation) {
        this.structureRotation = structureRotation;
        return this;
    }

    public PlacementOptions mirror(Mirror mirror) {
        this.mirror = mirror;
        return this;
    }

    public PlacementOptions palette(int palette) {
        this.palette = palette;
        return this;
    }

    public PlacementOptions integrity(float integrity) {
        Preconditions.checkArgument(integrity >= 0F && integrity <= 1F, "Integrity value (%S) must be between 0 and 1 inclusive", integrity);
        this.integrity = integrity;
        return this;
    }

    public PlacementOptions strict(boolean strict) {
        this.strict = strict;
        return this;
    }

    public PlacementOptions applyWaterlogging(boolean applyWaterlogging) {
        this.applyWaterlogging = applyWaterlogging;
        return this;
    }

    public boolean isIncludeEntities() {
        return includeEntities;
    }

    public StructureRotation getStructureRotation() {
        return structureRotation;
    }

    public Mirror getMirror() {
        return mirror;
    }

    public int getPalette() {
        return palette;
    }

    public float getIntegrity() {
        return integrity;
    }

    public Random getRandom() {
        return random;
    }

    public boolean isStrict() {
        return strict;
    }

    public boolean isApplyWaterlogging() {
        return applyWaterlogging;
    }

}
