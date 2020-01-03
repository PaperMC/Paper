package com.destroystokyo.paper.entity.ai;

import com.google.common.base.Objects;
import java.util.StringJoiner;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Mob;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Used to identify a Goal. Consists of a {@link NamespacedKey} and the type of mob the goal can be applied to
 *
 * @param <T> the type of mob the goal can be applied to
 */
@NullMarked
public final class GoalKey<T extends Mob> {

    private final Class<T> entityClass;
    private final NamespacedKey namespacedKey;

    private GoalKey(Class<T> entityClass, NamespacedKey namespacedKey) {
        this.entityClass = entityClass;
        this.namespacedKey = namespacedKey;
    }

    public Class<T> getEntityClass() {
        return this.entityClass;
    }

    public NamespacedKey getNamespacedKey() {
        return this.namespacedKey;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        GoalKey<?> goalKey = (GoalKey<?>) o;
        return Objects.equal(this.entityClass, goalKey.entityClass) &&
            Objects.equal(this.namespacedKey, goalKey.namespacedKey);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.entityClass, this.namespacedKey);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", GoalKey.class.getSimpleName() + "[", "]")
            .add("entityClass=" + this.entityClass)
            .add("namespacedKey=" + this.namespacedKey)
            .toString();
    }

    public static <A extends Mob> GoalKey<A> of(Class<A> entityClass, NamespacedKey namespacedKey) {
        return new GoalKey<>(entityClass, namespacedKey);
    }
}
