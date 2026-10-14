package io.papermc.paper.world.saveddata;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PaperWorldMetadata extends SavedData {
    public static final Codec<PaperWorldMetadata> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        UUIDUtil.CODEC.fieldOf("uuid").forGetter(PaperWorldMetadata::uuid)
    ).apply(instance, PaperWorldMetadata::new));
    public static final SavedDataType<PaperWorldMetadata> TYPE = new SavedDataType<>(
        Identifier.fromNamespaceAndPath(Identifier.PAPER_NAMESPACE, "metadata"),
        () -> new PaperWorldMetadata(UUID.randomUUID()),
        CODEC,
        DataFixTypes.NONE
    );

    private UUID uuid;

    public PaperWorldMetadata(final UUID uuid) {
        this.uuid = uuid;
    }

    public UUID uuid() {
        return this.uuid;
    }

    public void set(final UUID uuid) {
        if (!Objects.equals(this.uuid, uuid)) {
            this.uuid = uuid;
            this.setDirty();
        }
    }
}
