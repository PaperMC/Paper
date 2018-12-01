package org.bukkit.craftbukkit.util;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import net.minecraft.server.MojangsonParser;
import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTList;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.NBTTagString;

public class CraftNBTTagConfigSerializer {

    private static final Pattern ARRAY = Pattern.compile("^\\[.*]");
    private static final MojangsonParser MOJANGSON_PARSER = new MojangsonParser(new StringReader(""));

    public static Object serialize(NBTBase base) {
        if (base instanceof NBTTagCompound) {
            Map<String, Object> innerMap = new HashMap<>();
            for (String key : ((NBTTagCompound) base).getKeys()) {
                innerMap.put(key, serialize(((NBTTagCompound) base).get(key)));
            }

            return innerMap;
        } else if (base instanceof NBTTagList) {
            List<Object> baseList = new ArrayList<>();
            for (int i = 0; i < ((NBTList) base).size(); i++) {
                baseList.add(serialize(((NBTList) base).get(i)));
            }

            return baseList;
        } else if (base instanceof NBTTagString) {
            return base.b_(); //PAIL Rename getString
        }

        return base.toString();
    }

    public static NBTBase deserialize(Object object) {
        if (object instanceof Map) {
            NBTTagCompound compound = new NBTTagCompound();
            for (Map.Entry<String, Object> entry : ((Map<String, Object>) object).entrySet()) {
                compound.set(entry.getKey(), deserialize(entry.getValue()));
            }

            return compound;
        } else if (object instanceof List) {
            List<Object> list = (List<Object>) object;
            if (list.isEmpty()) {
                return new NBTTagList(); // Default
            }

            NBTTagList tagList = new NBTTagList();
            for (Object tag : list) {
                tagList.add(deserialize(tag));
            }

            return tagList;
        } else if (object instanceof String) {
            String string = (String) object;

            if (ARRAY.matcher(string).matches()) {
                try {
                    return new MojangsonParser(new StringReader(string)).h(); // PAIL Rename parseTagList
                } catch (CommandSyntaxException e) {
                    throw new RuntimeException("Could not deserialize found list ", e);
                }
            } else {
                return MOJANGSON_PARSER.b(string); // PAIL Rename parse tagBase
            }
        }

        throw new RuntimeException("Could not deserialize NBTBase");
    }
}
