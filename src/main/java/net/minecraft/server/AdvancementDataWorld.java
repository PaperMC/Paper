package net.minecraft.server;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AdvancementDataWorld extends ResourceDataJson {

    private static final Logger LOGGER = LogManager.getLogger();
    public static final Gson DESERIALIZER = (new GsonBuilder()).create();
    public Advancements REGISTRY = new Advancements();
    private final LootPredicateManager d;

    public AdvancementDataWorld(LootPredicateManager lootpredicatemanager) {
        super(AdvancementDataWorld.DESERIALIZER, "advancements");
        this.d = lootpredicatemanager;
    }

    protected void a(Map<MinecraftKey, JsonElement> map, IResourceManager iresourcemanager, GameProfilerFiller gameprofilerfiller) {
        Map<MinecraftKey, Advancement.SerializedAdvancement> map1 = Maps.newHashMap();

        map.forEach((minecraftkey, jsonelement) -> {
            try {
                JsonObject jsonobject = ChatDeserializer.m(jsonelement, "advancement");
                Advancement.SerializedAdvancement advancement_serializedadvancement = Advancement.SerializedAdvancement.a(jsonobject, new LootDeserializationContext(minecraftkey, this.d));

                map1.put(minecraftkey, advancement_serializedadvancement);
            } catch (IllegalArgumentException | JsonParseException jsonparseexception) {
                AdvancementDataWorld.LOGGER.error("Parsing error loading custom advancement {}: {}", minecraftkey, jsonparseexception.getMessage());
            }

        });
        Advancements advancements = new Advancements();

        advancements.a((Map) map1);
        Iterator iterator = advancements.b().iterator();

        while (iterator.hasNext()) {
            Advancement advancement = (Advancement) iterator.next();

            if (advancement.c() != null) {
                AdvancementTree.a(advancement);
            }
        }

        this.REGISTRY = advancements;
    }

    @Nullable
    public Advancement a(MinecraftKey minecraftkey) {
        return this.REGISTRY.a(minecraftkey);
    }

    public Collection<Advancement> getAdvancements() {
        return this.REGISTRY.c();
    }
}
