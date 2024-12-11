package io.papermc.paper.configuration.constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.PARAMETER})
public @interface Constraint {
    Class<? extends org.spongepowered.configurate.objectmapping.meta.Constraint<?>> value();

    class Factory implements org.spongepowered.configurate.objectmapping.meta.Constraint.Factory<Constraint, Object> {
        @SuppressWarnings("unchecked")
        @Override
        public org.spongepowered.configurate.objectmapping.meta.Constraint<Object> make(final Constraint data, final Type type) {
            try {
                final Constructor<? extends org.spongepowered.configurate.objectmapping.meta.Constraint<?>> constructor = data.value().getDeclaredConstructor();
                constructor.trySetAccessible();
                return (org.spongepowered.configurate.objectmapping.meta.Constraint<Object>) constructor.newInstance();
            } catch (final ReflectiveOperationException e) {
                throw new RuntimeException("Could not create constraint", e);
            }
        }
    }
}
