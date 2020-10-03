package net.minecraft.server;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Set;

public class LootEnchantFunction extends LootItemFunctionConditional {

    private final LootValueBounds a;
    private final int b;

    private LootEnchantFunction(LootItemCondition[] alootitemcondition, LootValueBounds lootvaluebounds, int i) {
        super(alootitemcondition);
        this.a = lootvaluebounds;
        this.b = i;
    }

    @Override
    public LootItemFunctionType b() {
        return LootItemFunctions.g;
    }

    @Override
    public Set<LootContextParameter<?>> a() {
        return ImmutableSet.of(LootContextParameters.KILLER_ENTITY);
    }

    private boolean c() {
        return this.b > 0;
    }

    @Override
    public ItemStack a(ItemStack itemstack, LootTableInfo loottableinfo) {
        Entity entity = (Entity) loottableinfo.getContextParameter(LootContextParameters.KILLER_ENTITY);

        if (entity instanceof EntityLiving) {
            int i = EnchantmentManager.g((EntityLiving) entity);
            // CraftBukkit start - use lootingModifier if set by plugin
            if (loottableinfo.hasContextParameter(LootContextParameters.LOOTING_MOD)) {
                i = loottableinfo.getContextParameter(LootContextParameters.LOOTING_MOD);
            }
            // CraftBukkit end

            if (i <= 0) { // CraftBukkit - account for possible negative looting values from Bukkit
                return itemstack;
            }

            float f = (float) i * this.a.b(loottableinfo.a());

            itemstack.add(Math.round(f));
            if (this.c() && itemstack.getCount() > this.b) {
                itemstack.setCount(this.b);
            }
        }

        return itemstack;
    }

    public static LootEnchantFunction.a a(LootValueBounds lootvaluebounds) {
        return new LootEnchantFunction.a(lootvaluebounds);
    }

    public static class b extends LootItemFunctionConditional.c<LootEnchantFunction> {

        public b() {}

        public void a(JsonObject jsonobject, LootEnchantFunction lootenchantfunction, JsonSerializationContext jsonserializationcontext) {
            super.a(jsonobject, lootenchantfunction, jsonserializationcontext); // CraftBukkit - decompile error
            jsonobject.add("count", jsonserializationcontext.serialize(lootenchantfunction.a));
            if (lootenchantfunction.c()) {
                jsonobject.add("limit", jsonserializationcontext.serialize(lootenchantfunction.b));
            }

        }

        @Override
        public LootEnchantFunction b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext, LootItemCondition[] alootitemcondition) {
            int i = ChatDeserializer.a(jsonobject, "limit", (int) 0);

            return new LootEnchantFunction(alootitemcondition, (LootValueBounds) ChatDeserializer.a(jsonobject, "count", jsondeserializationcontext, LootValueBounds.class), i);
        }
    }

    public static class a extends LootItemFunctionConditional.a<LootEnchantFunction.a> {

        private final LootValueBounds a;
        private int b = 0;

        public a(LootValueBounds lootvaluebounds) {
            this.a = lootvaluebounds;
        }

        @Override
        protected LootEnchantFunction.a d() {
            return this;
        }

        public LootEnchantFunction.a a(int i) {
            this.b = i;
            return this;
        }

        @Override
        public LootItemFunction b() {
            return new LootEnchantFunction(this.g(), this.a, this.b);
        }
    }
}
