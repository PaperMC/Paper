package io.papermc.paper.plugin.lifecycle.event.registrar;

import org.jetbrains.annotations.ApiStatus;

/**
 * To be implemented by types that provide ways to register types
 * either on server start or during a reload
 *
 * @since 1.20.4
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface Registrar {
}
