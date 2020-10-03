package net.minecraft.server;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Locale;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LootItemFunctionExplorationMap extends LootItemFunctionConditional {

    private static final Logger LOGGER = LogManager.getLogger();
    public static final StructureGenerator<?> a = StructureGenerator.BURIED_TREASURE;
    public static final MapIcon.Type b = MapIcon.Type.MANSION;
    private final StructureGenerator<?> e;
    private final MapIcon.Type f;
    private final byte g;
    private final int h;
    private final boolean i;

    private LootItemFunctionExplorationMap(LootItemCondition[] alootitemcondition, StructureGenerator<?> structuregenerator, MapIcon.Type mapicon_type, byte b0, int i, boolean flag) {
        super(alootitemcondition);
        this.e = structuregenerator;
        this.f = mapicon_type;
        this.g = b0;
        this.h = i;
        this.i = flag;
    }

    @Override
    public LootItemFunctionType b() {
        return LootItemFunctions.k;
    }

    @Override
    public Set<LootContextParameter<?>> a() {
        return ImmutableSet.of(LootContextParameters.ORIGIN);
    }

    @Override
    public ItemStack a(ItemStack itemstack, LootTableInfo loottableinfo) {
        if (itemstack.getItem() != Items.MAP) {
            return itemstack;
        } else {
            Vec3D vec3d = (Vec3D) loottableinfo.getContextParameter(LootContextParameters.ORIGIN);

            if (vec3d != null) {
                WorldServer worldserver = loottableinfo.getWorld();
                BlockPosition blockposition = worldserver.a(this.e, new BlockPosition(vec3d), this.h, this.i);

                if (blockposition != null) {
                    ItemStack itemstack1 = ItemWorldMap.createFilledMapView(worldserver, blockposition.getX(), blockposition.getZ(), this.g, true, true);

                    ItemWorldMap.applySepiaFilter(worldserver, itemstack1);
                    WorldMap.decorateMap(itemstack1, blockposition, "+", this.f);
                    itemstack1.a((IChatBaseComponent) (new ChatMessage("filled_map." + this.e.i().toLowerCase(Locale.ROOT))));
                    return itemstack1;
                }
            }

            return itemstack;
        }
    }

    public static LootItemFunctionExplorationMap.a c() {
        return new LootItemFunctionExplorationMap.a();
    }

    public static class b extends LootItemFunctionConditional.c<LootItemFunctionExplorationMap> {

        public b() {}

        public void a(JsonObject jsonobject, LootItemFunctionExplorationMap lootitemfunctionexplorationmap, JsonSerializationContext jsonserializationcontext) {
            super.a(jsonobject, (LootItemFunctionConditional) lootitemfunctionexplorationmap, jsonserializationcontext);
            if (!lootitemfunctionexplorationmap.e.equals(LootItemFunctionExplorationMap.a)) {
                jsonobject.add("destination", jsonserializationcontext.serialize(lootitemfunctionexplorationmap.e.i()));
            }

            if (lootitemfunctionexplorationmap.f != LootItemFunctionExplorationMap.b) {
                jsonobject.add("decoration", jsonserializationcontext.serialize(lootitemfunctionexplorationmap.f.toString().toLowerCase(Locale.ROOT)));
            }

            if (lootitemfunctionexplorationmap.g != 2) {
                jsonobject.addProperty("zoom", lootitemfunctionexplorationmap.g);
            }

            if (lootitemfunctionexplorationmap.h != 50) {
                jsonobject.addProperty("search_radius", lootitemfunctionexplorationmap.h);
            }

            if (!lootitemfunctionexplorationmap.i) {
                jsonobject.addProperty("skip_existing_chunks", lootitemfunctionexplorationmap.i);
            }

        }

        @Override
        public LootItemFunctionExplorationMap b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext, LootItemCondition[] alootitemcondition) {
            StructureGenerator<?> structuregenerator = a(jsonobject);
            String s = jsonobject.has("decoration") ? ChatDeserializer.h(jsonobject, "decoration") : "mansion";
            MapIcon.Type mapicon_type = LootItemFunctionExplorationMap.b;

            try {
                mapicon_type = MapIcon.Type.valueOf(s.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException illegalargumentexception) {
                LootItemFunctionExplorationMap.LOGGER.error("Error while parsing loot table decoration entry. Found {}. Defaulting to " + LootItemFunctionExplorationMap.b, s);
            }

            byte b0 = ChatDeserializer.a(jsonobject, "zoom", (byte) 2);
            int i = ChatDeserializer.a(jsonobject, "search_radius", (int) 50);
            boolean flag = ChatDeserializer.a(jsonobject, "skip_existing_chunks", true);

            return new LootItemFunctionExplorationMap(alootitemcondition, structuregenerator, mapicon_type, b0, i, flag);
        }

        private static StructureGenerator<?> a(JsonObject jsonobject) {
            if (jsonobject.has("destination")) {
                String s = ChatDeserializer.h(jsonobject, "destination");
                StructureGenerator<?> structuregenerator = (StructureGenerator) StructureGenerator.a.get(s.toLowerCase(Locale.ROOT));

                if (structuregenerator != null) {
                    return structuregenerator;
                }
            }

            return LootItemFunctionExplorationMap.a;
        }
    }

    public static class a extends LootItemFunctionConditional.a<LootItemFunctionExplorationMap.a> {

        private StructureGenerator<?> a;
        private MapIcon.Type b;
        private byte c;
        private int d;
        private boolean e;

        public a() {
            this.a = LootItemFunctionExplorationMap.a;
            this.b = LootItemFunctionExplorationMap.b;
            this.c = 2;
            this.d = 50;
            this.e = true;
        }

        @Override
        protected LootItemFunctionExplorationMap.a d() {
            return this;
        }

        public LootItemFunctionExplorationMap.a a(StructureGenerator<?> structuregenerator) {
            this.a = structuregenerator;
            return this;
        }

        public LootItemFunctionExplorationMap.a a(MapIcon.Type mapicon_type) {
            this.b = mapicon_type;
            return this;
        }

        public LootItemFunctionExplorationMap.a a(byte b0) {
            this.c = b0;
            return this;
        }

        public LootItemFunctionExplorationMap.a a(boolean flag) {
            this.e = flag;
            return this;
        }

        @Override
        public LootItemFunction b() {
            return new LootItemFunctionExplorationMap(this.g(), this.a, this.b, this.c, this.d, this.e);
        }
    }
}
