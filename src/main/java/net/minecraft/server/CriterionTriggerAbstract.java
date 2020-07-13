package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public abstract class CriterionTriggerAbstract<T extends CriterionInstanceAbstract> implements CriterionTrigger<T> {

    //private final Map<AdvancementDataPlayer, Set<CriterionTrigger.a<T>>> a = Maps.newIdentityHashMap(); // Paper - moved into AdvancementDataPlayer to fix memory leak

    public CriterionTriggerAbstract() {}

    @Override
    public final void a(AdvancementDataPlayer advancementdataplayer, CriterionTrigger.a<T> criteriontrigger_a) {
        (advancementdataplayer.criterionData.computeIfAbsent(this, (advancementdataplayer1) -> { // Paper - fix AdvancementDataPlayer leak
            return Sets.newHashSet();
        })).add(criteriontrigger_a);
    }

    @Override
    public final void b(AdvancementDataPlayer advancementdataplayer, CriterionTrigger.a<T> criteriontrigger_a) {
        Set<CriterionTrigger.a<T>> set = (Set) advancementdataplayer.criterionData.get(this); // Paper - fix AdvancementDataPlayer leak

        if (set != null) {
            set.remove(criteriontrigger_a);
            if (set.isEmpty()) {
                advancementdataplayer.criterionData.remove(this); // Paper - fix AdvancementDataPlayer leak
            }
        }

    }

    @Override
    public final void a(AdvancementDataPlayer advancementdataplayer) {
        advancementdataplayer.criterionData.remove(this); // Paper - fix AdvancementDataPlayer leak
    }

    protected abstract T b(JsonObject jsonobject, CriterionConditionEntity.b criterionconditionentity_b, LootDeserializationContext lootdeserializationcontext);

    @Override
    public final T a(JsonObject jsonobject, LootDeserializationContext lootdeserializationcontext) {
        CriterionConditionEntity.b criterionconditionentity_b = CriterionConditionEntity.b.a(jsonobject, "player", lootdeserializationcontext);

        return this.b(jsonobject, criterionconditionentity_b, lootdeserializationcontext);
    }

    protected void a(EntityPlayer entityplayer, Predicate<T> predicate) {
        AdvancementDataPlayer advancementdataplayer = entityplayer.getAdvancementData();
        Set<CriterionTrigger.a<T>> set = (Set) advancementdataplayer.criterionData.get(this); // Paper - fix AdvancementDataPlayer leak

        if (set != null && !set.isEmpty()) {
            LootTableInfo loottableinfo = CriterionConditionEntity.b(entityplayer, entityplayer);
            List<CriterionTrigger.a<T>> list = null;
            Iterator iterator = set.iterator();

            CriterionTrigger.a criteriontrigger_a;

            while (iterator.hasNext()) {
                criteriontrigger_a = (CriterionTrigger.a) iterator.next();
                T t0 = (T) criteriontrigger_a.a(); // Paper - decompile fix

                if (t0.b().a(loottableinfo) && predicate.test(t0)) {
                    if (list == null) {
                        list = Lists.newArrayList();
                    }

                    list.add(criteriontrigger_a);
                }
            }

            if (list != null) {
                iterator = list.iterator();

                while (iterator.hasNext()) {
                    criteriontrigger_a = (CriterionTrigger.a) iterator.next();
                    criteriontrigger_a.a(advancementdataplayer);
                }
            }

        }
    }
}
