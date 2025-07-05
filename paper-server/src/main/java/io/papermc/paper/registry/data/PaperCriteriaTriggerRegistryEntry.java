package io.papermc.paper.registry.data;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import io.papermc.paper.advancement.CriteriaTrigger;
import io.papermc.paper.advancement.PaperCustomCriterionTrigger;
import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.util.Conversions;
import java.util.function.Function;
import net.minecraft.advancements.CriterionTrigger;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asConfigured;

public class PaperCriteriaTriggerRegistryEntry<I> implements CriteriaTriggerRegistryEntry<I> {

    protected @Nullable Function<JsonObject, I> deserializer;

    public PaperCriteriaTriggerRegistryEntry(final Conversions conversions, final @Nullable CriterionTrigger<?> ignored) {
        Preconditions.checkArgument(ignored == null, "Cannot create entry from existing trigger");
    }

    @Override
    public Function<JsonObject, I> deserializer() {
        return asConfigured(this.deserializer, "deserializer");
    }

    public static final class PaperBuilder<I> extends PaperCriteriaTriggerRegistryEntry<I>
        implements CriteriaTriggerRegistryEntry.Builder<I>, PaperRegistryBuilder<CriterionTrigger<?>, CriteriaTrigger<I>> {

        public PaperBuilder(final Conversions conversions, final @Nullable CriterionTrigger<?> ignored) {
            super(conversions, ignored);
        }

        @Override
        public PaperBuilder<I> deserializer(final Function<JsonObject, I> deserializer) {
            this.deserializer = deserializer;
            return this;
        }

        @Override
        public CriterionTrigger<?> build() {
            return new PaperCustomCriterionTrigger<>(this.deserializer());
        }
    }
}
