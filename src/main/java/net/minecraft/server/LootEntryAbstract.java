package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.List;
import java.util.function.Predicate;

public abstract class LootEntryAbstract implements LootEntryChildren {

    protected final LootItemCondition[] d;
    private final Predicate<LootTableInfo> c;

    protected LootEntryAbstract(LootItemCondition[] alootitemcondition) {
        this.d = alootitemcondition;
        this.c = LootItemConditions.a((Predicate[]) alootitemcondition);
    }

    public void a(LootCollector lootcollector) {
        for (int i = 0; i < this.d.length; ++i) {
            this.d[i].a(lootcollector.b(".condition[" + i + "]"));
        }

    }

    protected final boolean a(LootTableInfo loottableinfo) {
        return this.c.test(loottableinfo);
    }

    public abstract LootEntryType a();

    public abstract static class Serializer<T extends LootEntryAbstract> implements LootSerializer<T> {

        public Serializer() {}

        // CraftBukkit start
        @Override
        public final void a(JsonObject jsonobject, T t0, JsonSerializationContext jsonserializationcontext) {
            if (!org.apache.commons.lang3.ArrayUtils.isEmpty(t0.d)) {
                jsonobject.add("conditions", jsonserializationcontext.serialize(t0.d));
            }

            this.serializeType(jsonobject, t0, jsonserializationcontext);
        }
        // CraftBukkit end

        @Override
        public final T a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
            LootItemCondition[] alootitemcondition = (LootItemCondition[]) ChatDeserializer.a(jsonobject, "conditions", new LootItemCondition[0], jsondeserializationcontext, LootItemCondition[].class);

            return this.deserializeType(jsonobject, jsondeserializationcontext, alootitemcondition);
        }

        public abstract void serializeType(JsonObject jsonobject, T t0, JsonSerializationContext jsonserializationcontext);

        public abstract T deserializeType(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext, LootItemCondition[] alootitemcondition);
    }

    public abstract static class a<T extends LootEntryAbstract.a<T>> implements LootItemConditionUser<T> {

        private final List<LootItemCondition> a = Lists.newArrayList();

        public a() {}

        protected abstract T d();

        @Override
        public T b(LootItemCondition.a lootitemcondition_a) {
            this.a.add(lootitemcondition_a.build());
            return this.d();
        }

        @Override
        public final T c() {
            return this.d();
        }

        protected LootItemCondition[] f() {
            return (LootItemCondition[]) this.a.toArray(new LootItemCondition[0]);
        }

        public LootEntryAlternatives.a a(LootEntryAbstract.a<?> lootentryabstract_a) {
            return new LootEntryAlternatives.a(new LootEntryAbstract.a[]{this, lootentryabstract_a});
        }

        public abstract LootEntryAbstract b();
    }
}
