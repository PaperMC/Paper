package io.papermc.paper.configuration.mapping;

import io.papermc.paper.configuration.ConfigurationPart;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * For use in maps inside {@link ConfigurationPart}s that have default keys that shouldn't be removed by users
 * <p>
 * Note that when the config is reloaded, the maps will be merged again, so make sure this map can't accumulate
 * keys overtime.
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MergeMap {
}
