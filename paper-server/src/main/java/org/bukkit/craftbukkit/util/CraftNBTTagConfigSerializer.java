package org.bukkit.craftbukkit.util;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.SnbtPrinterTagVisitor;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagParser;
import org.jetbrains.annotations.NotNull;

public class CraftNBTTagConfigSerializer {

    private static final Pattern ARRAY = Pattern.compile("^\\[.*]");
    private static final Pattern INTEGER = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)i", Pattern.CASE_INSENSITIVE); // Paper - fix regex
    private static final Pattern DOUBLE = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?d", Pattern.CASE_INSENSITIVE);
    private static final TagParser<Tag> MOJANGSON_PARSER = TagParser.create(NbtOps.INSTANCE);

    public static String serialize(@NotNull final Tag tag) {
        final SnbtPrinterTagVisitor snbtVisitor = new SnbtPrinterTagVisitor();
        return snbtVisitor.visit(tag);
    }

    public static Tag deserialize(final Object object) {
        // The new logic expects the top level object to be a single string, holding the entire nbt tag as SNBT.
        if (object instanceof final String snbtString) {
            try {
                return TagParser.parseCompoundFully(snbtString);
            } catch (final CommandSyntaxException e) {
                throw new RuntimeException("Failed to deserialise nbt", e);
            }
        } else { // Legacy logic is passed to the internal legacy deserialization that attempts to read the old format that *unsuccessfully* attempted to read/write nbt to a full yml tree.
            return CraftNBTTagConfigSerializer.internalLegacyDeserialization(object);
        }
    }

    private static Tag internalLegacyDeserialization(@NotNull final Object object) {
        if (object instanceof Map) {
            CompoundTag compound = new CompoundTag();
            for (Map.Entry<String, Object> entry : ((Map<String, Object>) object).entrySet()) {
                compound.put(entry.getKey(), CraftNBTTagConfigSerializer.internalLegacyDeserialization(entry.getValue()));
            }

            return compound;
        } else if (object instanceof List) {
            List<Object> list = (List<Object>) object;
            if (list.isEmpty()) {
                return new ListTag(); // Default
            }

            ListTag tagList = new ListTag();
            for (Object tag : list) {
                tagList.add(CraftNBTTagConfigSerializer.internalLegacyDeserialization(tag));
            }

            return tagList;
        } else if (object instanceof final String string) {
            if (CraftNBTTagConfigSerializer.ARRAY.matcher(string).matches()) {
                try {
                    return MOJANGSON_PARSER.parseAsArgument(new StringReader(string));
                } catch (CommandSyntaxException e) {
                    throw new RuntimeException("Could not deserialize found list ", e);
                }
            } else if (CraftNBTTagConfigSerializer.INTEGER.matcher(string).matches()) { //Read integers on our own
                return IntTag.valueOf(Integer.parseInt(string.substring(0, string.length() - 1)));
            } else if (CraftNBTTagConfigSerializer.DOUBLE.matcher(string).matches()) {
                return DoubleTag.valueOf(Double.parseDouble(string.substring(0, string.length() - 1)));
            } else {
                try {
                    Tag tag = CraftNBTTagConfigSerializer.MOJANGSON_PARSER.parseAsArgument(new StringReader(string));

                    if (tag instanceof IntTag) { // If this returns an integer, it did not use our method from above
                        return StringTag.valueOf(tag.toString()); // It then is a string that was falsely read as an int
                    } else if (tag instanceof DoubleTag) {
                        return StringTag.valueOf(String.valueOf(((DoubleTag) tag).doubleValue())); // Doubles add "d" at the end
                    } else if (tag instanceof StringTag) {
                        return StringTag.valueOf(string); // Return the whole string
                    } else {
                        return tag;
                    }
                } catch (final CommandSyntaxException commandSyntaxException) {
                    throw new RuntimeException("Could not deserialize found primitive ", commandSyntaxException);
                }
            }
        }

        throw new RuntimeException("Could not deserialize Tag");
    }
}
