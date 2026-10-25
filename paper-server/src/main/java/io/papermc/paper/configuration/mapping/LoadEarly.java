package io.papermc.paper.configuration.mapping;

import io.papermc.paper.configuration.ConfigurationPart;
import io.papermc.paper.configuration.GlobalConfiguration;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.BiPredicate;

import static io.leangen.geantyref.GenericTypeReflector.erase;

/**
 * Marks a field of the {@link GlobalConfiguration} that is deserialized early during startup,
 * before {@link net.minecraft.server.Bootstrap} has run and before a {@link net.minecraft.core.RegistryAccess} exists.
 * The same instance is populated again with every other field once the full config is loaded.
 * <p>
 * Every {@link ConfigurationPart} containing an early field is instantiated during the early load,
 * which runs <b>all</b> of its field initializers, not just the ones of early fields. Those must therefore
 * not touch anything that requires the game to be bootstrapped, like registries.
 * <p>
 * Early fields may only use registry-free types, and {@link org.spongepowered.configurate.objectmapping.meta.PostProcess}
 * methods are not run during the early load. Marking a {@link ConfigurationPart} field itself has no effect, mark its fields instead.
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoadEarly {

    /**
     * Accepts only fields marked with {@link LoadEarly}, and {@link ConfigurationPart}s that (transitively) contain such a field.
     * Rejected sections are never instantiated.
     */
    BiPredicate<AnnotatedType, AnnotatedElement> FIELD_FILTER = new FieldFilter();

    final class FieldFilter implements BiPredicate<AnnotatedType, AnnotatedElement> {

        private static final ClassValue<Boolean> CONTAINS_EARLY_FIELDS = new ClassValue<>() {
            @Override
            protected Boolean computeValue(final Class<?> type) {
                for (Class<?> clazz = type; clazz != null && clazz != Object.class; clazz = clazz.getSuperclass()) {
                    for (final Field field : clazz.getDeclaredFields()) {
                        if (field.isSynthetic() || Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers())) {
                            continue;
                        }
                        if (field.isAnnotationPresent(LoadEarly.class) || isEarlySection(field.getType())) {
                            return true;
                        }
                    }
                }
                return false;
            }
        };

        private FieldFilter() {
        }

        private static boolean isEarlySection(final Class<?> type) {
            return ConfigurationPart.class.isAssignableFrom(type) && CONTAINS_EARLY_FIELDS.get(type);
        }

        @Override
        public boolean test(final AnnotatedType fieldType, final AnnotatedElement container) {
            return container.isAnnotationPresent(LoadEarly.class) || isEarlySection(erase(fieldType.getType()));
        }
    }
}
