package net.minecraft.server;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Random;
import java.util.Set;

public class LootItemConditionSurvivesExplosion implements LootItemCondition {

    private static final LootItemConditionSurvivesExplosion a = new LootItemConditionSurvivesExplosion();

    private LootItemConditionSurvivesExplosion() {}

    @Override
    public LootItemConditionType b() {
        return LootItemConditions.k;
    }

    @Override
    public Set<LootContextParameter<?>> a() {
        return ImmutableSet.of(LootContextParameters.EXPLOSION_RADIUS);
    }

    public boolean test(LootTableInfo loottableinfo) {
        Float ofloat = (Float) loottableinfo.getContextParameter(LootContextParameters.EXPLOSION_RADIUS);

        if (ofloat != null) {
            Random random = loottableinfo.a();
            float f = 1.0F / ofloat;

            return random.nextFloat() <= f;
        } else {
            return true;
        }
    }

    public static LootItemCondition.a c() {
        return () -> {
            return LootItemConditionSurvivesExplosion.a;
        };
    }

    public static class a implements LootSerializer<LootItemConditionSurvivesExplosion> {

        public a() {}

        public void a(JsonObject jsonobject, LootItemConditionSurvivesExplosion lootitemconditionsurvivesexplosion, JsonSerializationContext jsonserializationcontext) {}

        @Override
        public LootItemConditionSurvivesExplosion a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
            return LootItemConditionSurvivesExplosion.a;
        }
    }
}
