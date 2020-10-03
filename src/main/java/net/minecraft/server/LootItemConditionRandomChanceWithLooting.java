package net.minecraft.server;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Set;

public class LootItemConditionRandomChanceWithLooting implements LootItemCondition {

    private final float a;
    private final float b;

    private LootItemConditionRandomChanceWithLooting(float f, float f1) {
        this.a = f;
        this.b = f1;
    }

    @Override
    public LootItemConditionType b() {
        return LootItemConditions.d;
    }

    @Override
    public Set<LootContextParameter<?>> a() {
        return ImmutableSet.of(LootContextParameters.KILLER_ENTITY);
    }

    public boolean test(LootTableInfo loottableinfo) {
        Entity entity = (Entity) loottableinfo.getContextParameter(LootContextParameters.KILLER_ENTITY);
        int i = 0;

        if (entity instanceof EntityLiving) {
            i = EnchantmentManager.g((EntityLiving) entity);
        }

        return loottableinfo.a().nextFloat() < this.a + (float) i * this.b;
    }

    public static LootItemCondition.a a(float f, float f1) {
        return () -> {
            return new LootItemConditionRandomChanceWithLooting(f, f1);
        };
    }

    public static class a implements LootSerializer<LootItemConditionRandomChanceWithLooting> {

        public a() {}

        public void a(JsonObject jsonobject, LootItemConditionRandomChanceWithLooting lootitemconditionrandomchancewithlooting, JsonSerializationContext jsonserializationcontext) {
            jsonobject.addProperty("chance", lootitemconditionrandomchancewithlooting.a);
            jsonobject.addProperty("looting_multiplier", lootitemconditionrandomchancewithlooting.b);
        }

        @Override
        public LootItemConditionRandomChanceWithLooting a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
            return new LootItemConditionRandomChanceWithLooting(ChatDeserializer.l(jsonobject, "chance"), ChatDeserializer.l(jsonobject, "looting_multiplier"));
        }
    }
}
