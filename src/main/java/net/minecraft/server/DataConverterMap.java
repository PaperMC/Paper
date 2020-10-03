package net.minecraft.server;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import java.util.Objects;
import java.util.Optional;

public class DataConverterMap extends DataFix {

    public DataConverterMap(Schema schema, boolean flag) {
        super(schema, flag);
    }

    public TypeRewriteRule makeRule() {
        Type<?> type = this.getInputSchema().getType(DataConverterTypes.ITEM_STACK);
        OpticFinder<Pair<String, String>> opticfinder = DSL.fieldFinder("id", DSL.named(DataConverterTypes.ITEM_NAME.typeName(), DataConverterSchemaNamed.a()));
        OpticFinder<?> opticfinder1 = type.findField("tag");

        return this.fixTypeEverywhereTyped("ItemInstanceMapIdFix", type, (typed) -> {
            Optional<Pair<String, String>> optional = typed.getOptional(opticfinder);

            if (optional.isPresent() && Objects.equals(((Pair) optional.get()).getSecond(), "minecraft:filled_map")) {
                Dynamic<?> dynamic = (Dynamic) typed.get(DSL.remainderFinder());
                Typed<?> typed1 = typed.getOrCreateTyped(opticfinder1);
                Dynamic<?> dynamic1 = (Dynamic) typed1.get(DSL.remainderFinder());

                if (!dynamic1.getElement("map").result().isPresent()) dynamic1 = dynamic1.set("map", dynamic1.createInt(dynamic.get("Damage").asInt(0))); // CraftBukkit
                return typed.set(opticfinder1, typed1.set(DSL.remainderFinder(), dynamic1));
            } else {
                return typed;
            }
        });
    }
}
