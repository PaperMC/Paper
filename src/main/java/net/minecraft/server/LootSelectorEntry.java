package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import org.apache.commons.lang3.ArrayUtils;

public abstract class LootSelectorEntry extends LootEntryAbstract {

    protected final int c; public int getWeight() { return c; } // Paper - OBFHELPER
    protected final int e; public int getQuality() { return e; } // Paper - OBFHELPER
    protected final LootItemFunction[] f;
    private final BiFunction<ItemStack, LootTableInfo, ItemStack> g;
    private final LootEntry h = new LootSelectorEntry.c() {
        @Override
        public void a(Consumer<ItemStack> consumer, LootTableInfo loottableinfo) {
            LootSelectorEntry.this.a(LootItemFunction.a(LootSelectorEntry.this.g, consumer, loottableinfo), loottableinfo);
        }
    };

    protected LootSelectorEntry(int i, int j, LootItemCondition[] alootitemcondition, LootItemFunction[] alootitemfunction) {
        super(alootitemcondition);
        this.c = i;
        this.e = j;
        this.f = alootitemfunction;
        this.g = LootItemFunctions.a(alootitemfunction);
    }

    @Override
    public void a(LootCollector lootcollector) {
        super.a(lootcollector);

        for (int i = 0; i < this.f.length; ++i) {
            this.f[i].a(lootcollector.b(".functions[" + i + "]"));
        }

    }

    protected abstract void a(Consumer<ItemStack> consumer, LootTableInfo loottableinfo);

    @Override
    public boolean expand(LootTableInfo loottableinfo, Consumer<LootEntry> consumer) {
        if (this.a(loottableinfo)) {
            consumer.accept(this.h);
            return true;
        } else {
            return false;
        }
    }

    public static LootSelectorEntry.a<?> a(LootSelectorEntry.d lootselectorentry_d) {
        return new LootSelectorEntry.b(lootselectorentry_d);
    }

    public abstract static class e<T extends LootSelectorEntry> extends LootEntryAbstract.Serializer<T> {

        public e() {}

        public void a(JsonObject jsonobject, T t0, JsonSerializationContext jsonserializationcontext) {
            if (t0.c != 1) {
                jsonobject.addProperty("weight", t0.c);
            }

            if (t0.e != 0) {
                jsonobject.addProperty("quality", t0.e);
            }

            if (!ArrayUtils.isEmpty(t0.f)) {
                jsonobject.add("functions", jsonserializationcontext.serialize(t0.f));
            }

        }

        @Override
        public final T deserializeType(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext, LootItemCondition[] alootitemcondition) {
            int i = ChatDeserializer.a(jsonobject, "weight", (int) 1);
            int j = ChatDeserializer.a(jsonobject, "quality", (int) 0);
            LootItemFunction[] alootitemfunction = (LootItemFunction[]) ChatDeserializer.a(jsonobject, "functions", new LootItemFunction[0], jsondeserializationcontext, LootItemFunction[].class);

            return this.b(jsonobject, jsondeserializationcontext, i, j, alootitemcondition, alootitemfunction);
        }

        protected abstract T b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext, int i, int j, LootItemCondition[] alootitemcondition, LootItemFunction[] alootitemfunction);
    }

    static class b extends LootSelectorEntry.a<LootSelectorEntry.b> {

        private final LootSelectorEntry.d c;

        public b(LootSelectorEntry.d lootselectorentry_d) {
            this.c = lootselectorentry_d;
        }

        @Override
        protected LootSelectorEntry.b d() {
            return this;
        }

        @Override
        public LootEntryAbstract b() {
            return this.c.build(this.a, this.b, this.f(), this.a());
        }
    }

    @FunctionalInterface
    public interface d {

        LootSelectorEntry build(int i, int j, LootItemCondition[] alootitemcondition, LootItemFunction[] alootitemfunction);
    }

    public abstract static class a<T extends LootSelectorEntry.a<T>> extends LootEntryAbstract.a<T> implements LootItemFunctionUser<T> {

        protected int a = 1;
        protected int b = 0;
        private final List<LootItemFunction> c = Lists.newArrayList();

        public a() {}

        @Override
        public T b(LootItemFunction.a lootitemfunction_a) {
            this.c.add(lootitemfunction_a.b());
            return this.d(); // Paper - decompile fix
        }

        protected LootItemFunction[] a() {
            return (LootItemFunction[]) this.c.toArray(new LootItemFunction[0]);
        }

        public T a(int i) {
            this.a = i;
            return this.d(); // Paper - decompile fix
        }

        public T b(int i) {
            this.b = i;
            return this.d(); // Paper - decompile fix
        }
    }

    public abstract class c implements LootEntry {

        protected c() {
        }

        @Override
        public int a(float f) {
            // Paper start - Offer an alternative loot formula to refactor how luck bonus applies
            // SEE: https://luckformula.emc.gs for details and data
            if (lastLuck != null && lastLuck == f) {
                return lastWeight;
            }
            // This is vanilla
            float qualityModifer = (float) getQuality() * f;
            double baseWeight = (getWeight() + qualityModifer);
            if (com.destroystokyo.paper.PaperConfig.useAlternativeLuckFormula) {
                // Random boost to avoid losing precision in the final int cast on return
                final int weightBoost = 100;
                baseWeight *= weightBoost;
                // If we have vanilla 1, bump that down to 0 so nothing is is impacted
                // vanilla 3 = 300, 200 basis = impact 2%
                // =($B2*(($B2-100)/100/100))
                double impacted = baseWeight * ((baseWeight - weightBoost) / weightBoost / 100);
                // =($B$7/100)
                float luckModifier = Math.min(100, f * 10) / 100;
                // =B2 - (C2 *($B$7/100))
                baseWeight = Math.ceil(baseWeight - (impacted * luckModifier));
            }
            lastLuck = f;
            lastWeight = (int) Math.max(0, Math.floor(baseWeight));
            return lastWeight;
        }
    }
    private Float lastLuck = null;
    private int lastWeight = 0;
    // Paper end
}
