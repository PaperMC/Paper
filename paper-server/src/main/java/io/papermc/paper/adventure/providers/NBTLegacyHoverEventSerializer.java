package io.papermc.paper.adventure.providers;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.io.IOException;
import java.util.UUID;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.util.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagParser;
import org.intellij.lang.annotations.Subst;

final class NBTLegacyHoverEventSerializer implements LegacyHoverEventSerializer {
    public static final NBTLegacyHoverEventSerializer INSTANCE = new NBTLegacyHoverEventSerializer();
    private static final Codec<CompoundTag, String, CommandSyntaxException, RuntimeException> SNBT_CODEC = Codec.codec(TagParser::parseCompoundFully, Tag::toString);

    static final String ITEM_TYPE = "id";
    static final String ITEM_COUNT = "Count";
    static final String ITEM_TAG = "tag";

    static final String ENTITY_NAME = "name";
    static final String ENTITY_TYPE = "type";
    static final String ENTITY_ID = "id";

    NBTLegacyHoverEventSerializer() {
    }

    @Override
    public HoverEvent.ShowItem deserializeShowItem(final Component input) throws IOException {
        final String raw = PlainTextComponentSerializer.plainText().serialize(input);
        try {
            final CompoundTag contents = SNBT_CODEC.decode(raw);
            final CompoundTag tag = contents.getCompoundOrEmpty(ITEM_TAG);
            @Subst("key") final String keyString = contents.getStringOr(ITEM_TYPE, "");
            return HoverEvent.ShowItem.showItem(
                Key.key(keyString),
                contents.getByteOr(ITEM_COUNT, (byte) 1),
                tag.isEmpty() ? null : BinaryTagHolder.encode(tag, SNBT_CODEC)
            );
        } catch (final CommandSyntaxException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public HoverEvent.ShowEntity deserializeShowEntity(final Component input, final Codec.Decoder<Component, String, ? extends RuntimeException> componentCodec) throws IOException {
        final String raw = PlainTextComponentSerializer.plainText().serialize(input);
        try {
            final CompoundTag contents = SNBT_CODEC.decode(raw);
            @Subst("key") final String keyString = contents.getStringOr(ENTITY_TYPE, "");
            return HoverEvent.ShowEntity.showEntity(
                Key.key(keyString),
                UUID.fromString(contents.getStringOr(ENTITY_ID, "")),
                componentCodec.decode(contents.getStringOr(ENTITY_NAME, ""))
            );
        } catch (final CommandSyntaxException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public Component serializeShowItem(final HoverEvent.ShowItem input) throws IOException {
        final CompoundTag tag = new CompoundTag();
        tag.putString(ITEM_TYPE, input.item().asString());
        tag.putByte(ITEM_COUNT, (byte) input.count());
        if (input.nbt() != null) {
            try {
                tag.put(ITEM_TAG, input.nbt().get(SNBT_CODEC));
            } catch (final CommandSyntaxException ex) {
                throw new IOException(ex);
            }
        }
        return Component.text(SNBT_CODEC.encode(tag));
    }

    @Override
    public Component serializeShowEntity(final HoverEvent.ShowEntity input, final Codec.Encoder<Component, String, ? extends RuntimeException> componentCodec) {
        final CompoundTag tag = new CompoundTag();
        tag.putString(ENTITY_ID, input.id().toString());
        tag.putString(ENTITY_TYPE, input.type().asString());
        if (input.name() != null) {
            tag.putString(ENTITY_NAME, componentCodec.encode(input.name()));
        }
        return Component.text(SNBT_CODEC.encode(tag));
    }
}
