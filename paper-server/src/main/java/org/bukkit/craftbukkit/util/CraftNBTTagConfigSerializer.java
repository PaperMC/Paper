package org.bukkit.craftbukkit.util;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import net.minecraft.nbt.MojangsonParser;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

public class CraftNBTTagConfigSerializer {

    private static final Pattern ARRAY = Pattern.compile("^\\[.*]");
    private static final Pattern INTEGER = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)?i", Pattern.CASE_INSENSITIVE);
    private static final Pattern DOUBLE = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?d", Pattern.CASE_INSENSITIVE);
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
                baseList.add(serialize((NBTBase) ((NBTList) base).get(i)));
            }

            return baseList;
        } else if (base instanceof NBTTagString) {
            return base.asString();
        } else if (base instanceof NBTTagInt) { // No need to check for doubles, those are covered by the double itself
            return base.toString() + "i";
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
                    return new MojangsonParser(new StringReader(string)).parseArray();
                } catch (CommandSyntaxException e) {
                    throw new RuntimeException("Could not deserialize found list ", e);
                }
            } else if (INTEGER.matcher(string).matches()) { //Read integers on our own
                return NBTTagInt.a(Integer.parseInt(string.substring(0, string.length() - 1)));
            } else if (DOUBLE.matcher(string).matches()) {
                return NBTTagDouble.a(Double.parseDouble(string.substring(0, string.length() - 1)));
            } else {
                NBTBase nbtBase = MOJANGSON_PARSER.parseLiteral(string);

                if (nbtBase instanceof NBTTagInt) { // If this returns an integer, it did not use our method from above
                    return NBTTagString.a(nbtBase.asString()); // It then is a string that was falsely read as an int
                } else if (nbtBase instanceof NBTTagDouble) {
                    return NBTTagString.a(String.valueOf(((NBTTagDouble) nbtBase).asDouble())); // Doubles add "d" at the end
                } else {
                    return nbtBase;
                }
            }
        }

        throw new RuntimeException("Could not deserialize NBTBase");
    }
}
