package io.papermc.paper.world.saveddata;

import com.mojang.serialization.Codec;
import java.util.Objects;
import net.minecraft.resources.Identifier;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import org.bukkit.craftbukkit.persistence.CraftPersistentDataContainer;
import org.bukkit.craftbukkit.persistence.CraftPersistentDataTypeRegistry;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PaperWorldPDC extends SavedData {
    private static final CraftPersistentDataTypeRegistry DATA_TYPE_REGISTRY = new CraftPersistentDataTypeRegistry();
    public static final Codec<PaperWorldPDC> CODEC = CraftPersistentDataContainer.createCodec(DATA_TYPE_REGISTRY)
        .xmap(PaperWorldPDC::new, PaperWorldPDC::persistentData);
    public static final SavedDataType<PaperWorldPDC> TYPE = new SavedDataType<>(
        Identifier.fromNamespaceAndPath(Identifier.PAPER_NAMESPACE, "persistent_data_container"),
        () -> new PaperWorldPDC(new CraftPersistentDataContainer(DATA_TYPE_REGISTRY)),
        CODEC,
        DataFixTypes.NONE
    );

    private final CraftPersistentDataContainer persistentData;

    public PaperWorldPDC(final CraftPersistentDataContainer persistentData) {
        this.persistentData = persistentData;
    }

    public CraftPersistentDataContainer persistentData() {
        return this.persistentData;
    }

    public void setFrom(final CraftPersistentDataContainer source) {
        if (!Objects.equals(this.persistentData, source)) {
            this.persistentData.clear();
            this.persistentData.putAll(source.getTagsCloned());
            this.setDirty();
        }
    }
}
