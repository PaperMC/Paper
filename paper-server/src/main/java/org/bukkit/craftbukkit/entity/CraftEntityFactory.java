package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.MojangsonParser;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.entity.EntityTypes;
import org.bukkit.entity.EntityFactory;
import org.bukkit.entity.EntitySnapshot;

public class CraftEntityFactory implements EntityFactory {

    private static final CraftEntityFactory instance;

    static {
        instance = new CraftEntityFactory();
    }

    private CraftEntityFactory() {
    }

    @Override
    public EntitySnapshot createEntitySnapshot(String input) {
        Preconditions.checkArgument(input != null, "Input string cannot be null");

        NBTTagCompound tag;
        try {
            tag = MojangsonParser.parseTag(input);
        } catch (CommandSyntaxException e) {
            throw new IllegalArgumentException("Could not parse Entity: " + input, e);
        }

        EntityTypes<?> type = EntityTypes.by(tag).orElse(null);
        if (type == null) {
            throw new IllegalArgumentException("Could not parse Entity: " + input);
        }

        return CraftEntitySnapshot.create(tag, CraftEntityType.minecraftToBukkit(type));
    }

    public static CraftEntityFactory instance() {
        return instance;
    }
}
