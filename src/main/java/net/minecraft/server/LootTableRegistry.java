package net.minecraft.server;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LootTableRegistry extends ResourceDataJson {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson b = LootSerialization.c().create();
    private Map<MinecraftKey, LootTable> c = ImmutableMap.of();
    private final LootPredicateManager d;

    public LootTableRegistry(LootPredicateManager lootpredicatemanager) {
        super(LootTableRegistry.b, "loot_tables");
        this.d = lootpredicatemanager;
    }

    public LootTable getLootTable(MinecraftKey minecraftkey) {
        return (LootTable) this.c.getOrDefault(minecraftkey, LootTable.EMPTY);
    }

    protected void a(Map<MinecraftKey, JsonElement> map, IResourceManager iresourcemanager, GameProfilerFiller gameprofilerfiller) {
        Builder<MinecraftKey, LootTable> builder = ImmutableMap.builder();
        JsonElement jsonelement = (JsonElement) map.remove(LootTables.a);

        if (jsonelement != null) {
            LootTableRegistry.LOGGER.warn("Datapack tried to redefine {} loot table, ignoring", LootTables.a);
        }

        map.forEach((minecraftkey, jsonelement1) -> {
            try {
                LootTable loottable = (LootTable) LootTableRegistry.b.fromJson(jsonelement1, LootTable.class);

                builder.put(minecraftkey, loottable);
            } catch (Exception exception) {
                LootTableRegistry.LOGGER.error("Couldn't parse loot table {}", minecraftkey, exception);
            }

        });
        builder.put(LootTables.a, LootTable.EMPTY);
        ImmutableMap<MinecraftKey, LootTable> immutablemap = builder.build();
        LootContextParameterSet lootcontextparameterset = LootContextParameterSets.GENERIC;
        LootPredicateManager lootpredicatemanager = this.d;

        this.d.getClass();
        Function function = lootpredicatemanager::a;

        immutablemap.getClass();
        LootCollector lootcollector = new LootCollector(lootcontextparameterset, function, immutablemap::get);

        immutablemap.forEach((minecraftkey, loottable) -> {
            a(lootcollector, minecraftkey, loottable);
        });
        lootcollector.a().forEach((s, s1) -> {
            LootTableRegistry.LOGGER.warn("Found validation problem in " + s + ": " + s1);
        });
        this.c = immutablemap;
    }

    public static void a(LootCollector lootcollector, MinecraftKey minecraftkey, LootTable loottable) {
        loottable.a(lootcollector.a(loottable.getLootContextParameterSet()).a("{" + minecraftkey + "}", minecraftkey));
    }

    public static JsonElement a(LootTable loottable) {
        return LootTableRegistry.b.toJsonTree(loottable);
    }

    public Set<MinecraftKey> a() {
        return this.c.keySet();
    }
}
