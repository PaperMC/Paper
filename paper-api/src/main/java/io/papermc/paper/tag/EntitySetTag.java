package io.papermc.paper.tag;

import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public class EntitySetTag extends BaseTag<EntityType, EntitySetTag> {

    public EntitySetTag(@NotNull NamespacedKey key, @NotNull Predicate<EntityType> filter) {
        super(EntityType.class, key, filter);
    }

    public EntitySetTag(@NotNull NamespacedKey key, @NotNull EntityType... values) {
        super(EntityType.class, key, values);
    }

    public EntitySetTag(@NotNull NamespacedKey key, @NotNull Collection<EntityType> values) {
        super(EntityType.class, key, values);
    }

    public EntitySetTag(@NotNull NamespacedKey key, @NotNull Collection<EntityType> values, @NotNull Predicate<EntityType>... globalPredicates) {
        super(EntityType.class, key, values, globalPredicates);
    }

    @NotNull
    @Override
    protected Set<EntityType> getAllPossibleValues() {
        return Stream.of(EntityType.values()).collect(Collectors.toSet());
    }

    @NotNull
    @Override
    protected String getName(@NotNull EntityType value) {
        return value.name();
    }
}
