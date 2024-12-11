package io.papermc.paper.configuration.serializer;

import com.destroystokyo.paper.util.SneakyThrow;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.commands.arguments.NbtPathArgument;
import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

public class NbtPathSerializer extends ScalarSerializer<NbtPathArgument.NbtPath> {

    public static final NbtPathSerializer SERIALIZER = new NbtPathSerializer();
    private static final NbtPathArgument DUMMY_ARGUMENT = new NbtPathArgument();

    private NbtPathSerializer() {
        super(NbtPathArgument.NbtPath.class);
    }

    @Override
    public NbtPathArgument.NbtPath deserialize(final Type type, final Object obj) throws SerializationException {
        return fromString(obj.toString());
    }

    @Override
    protected Object serialize(final NbtPathArgument.NbtPath item, final Predicate<Class<?>> typeSupported) {
        return item.toString();
    }

    public static List<NbtPathArgument.NbtPath> fromString(final List<String> tags) {
        List<NbtPathArgument.NbtPath> paths = new ArrayList<>();
        try {
            for (final String tag : tags) {
                paths.add(fromString(tag));
            }
        } catch (SerializationException ex) {
            SneakyThrow.sneaky(ex);
        }
        return List.copyOf(paths);
    }

    private static NbtPathArgument.NbtPath fromString(final String tag) throws SerializationException {
        try {
            return DUMMY_ARGUMENT.parse(new StringReader(tag));
        } catch (CommandSyntaxException e) {
            throw new SerializationException(NbtPathArgument.NbtPath.class, e);
        }
    }
}
