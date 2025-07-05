package io.papermc.paper.advancement;

import io.papermc.paper.registry.HolderableBase;
import java.util.function.Predicate;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.Holder;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class PaperCriteriaTrigger<B> extends HolderableBase<CriterionTrigger<?>> implements CriteriaTrigger<B> {

    public PaperCriteriaTrigger(final Holder<CriterionTrigger<?>> holder) {
        super(holder);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void trigger(final Player player, final Predicate<? super B> instancePredicate) {
        if (this.getHandle() instanceof final PaperCustomCriterionTrigger<?> paperCustomCriterionTrigger) {
            ((PaperCustomCriterionTrigger<B>) paperCustomCriterionTrigger).trigger(player, instancePredicate);
        } else {
            throw new UnsupportedOperationException("Cannot trigger non-custom criteria trigger types");
        }
    }
}
