package io.papermc.paper.advancement;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.Lifecycle;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.advancements.criterion.ContextAwarePredicate;
import net.minecraft.advancements.criterion.SimpleCriterionTrigger;
import net.minecraft.util.ExtraCodecs;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PaperCustomCriterionTrigger<B> extends SimpleCriterionTrigger<PaperCustomCriterionTrigger.PaperCustomTriggerInstance<B>> {

    private final Codec<PaperCustomTriggerInstance<B>> codec;

    public PaperCustomCriterionTrigger(final Function<? super JsonObject, ? extends B> deserializer) {
        this.codec = ExtraCodecs.converter(JsonOps.INSTANCE).flatXmap(jsonElement -> {
            if (!(jsonElement instanceof final JsonObject jsonObject)) {
                return DataResult.error(() -> "Expected object");
            }
            return DataResult.success(new PaperCustomTriggerInstance<>(deserializer.apply(jsonObject)), Lifecycle.experimental());
        }, instance -> {
            return DataResult.error(() -> "Cannot encode custom criterion trigger instances");
        });
    }

    public void trigger(final Player player, final Predicate<? super B> instancePredicate) {
        this.trigger(((CraftPlayer) player).getHandle(), wrapper -> {
            return instancePredicate.test(wrapper.apiInstance());
        });
    }

    @Override
    public Codec<PaperCustomTriggerInstance<B>> codec() {
        return this.codec;
    }

    public record PaperCustomTriggerInstance<B>(B apiInstance) implements SimpleCriterionTrigger.SimpleInstance {

        @Override
        public Optional<ContextAwarePredicate> player() {
            return Optional.empty();
        }
    }
}
