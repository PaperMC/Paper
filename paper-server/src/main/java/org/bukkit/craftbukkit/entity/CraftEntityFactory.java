package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.TagValueInput;
import org.bukkit.entity.EntityFactory;
import org.bukkit.entity.EntitySnapshot;
import org.slf4j.Logger;

public class CraftEntityFactory implements EntityFactory {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final CraftEntityFactory instance;

    static {
        instance = new CraftEntityFactory();
    }

    private CraftEntityFactory() {
    }

    @Override
    public EntitySnapshot createEntitySnapshot(String input) {
        Preconditions.checkArgument(input != null, "Input string cannot be null");

        CompoundTag tag;
        try {
            tag = TagParser.parseCompoundFully(input);
        } catch (CommandSyntaxException e) {
            throw new IllegalArgumentException("Could not parse Entity: " + input, e);
        }

        final EntityType<?> type;
        try (final ProblemReporter.ScopedCollector problemReporter = new ProblemReporter.ScopedCollector(
            () -> "createEntitySnapshot", LOGGER
        )) {
            type = EntityType.by(TagValueInput.createGlobal(problemReporter, tag)).orElse(null);
        }
        if (type == null) {
            throw new IllegalArgumentException("Could not parse Entity: " + input);
        }

        return CraftEntitySnapshot.create(tag, CraftEntityType.minecraftToBukkit(type));
    }

    public static CraftEntityFactory instance() {
        return CraftEntityFactory.instance;
    }
}
