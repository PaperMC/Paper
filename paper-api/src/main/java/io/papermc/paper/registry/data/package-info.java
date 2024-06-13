/**
 * Collection of registry entry types that may be created or modified via the
 * {@link io.papermc.paper.registry.event.RegistryEvent}.
 * <p>
 * A registry entry represents its runtime API counterpart as a version-specific data-focused type.
 * Registry entries are not expected to be used during plugin runtime interactions with the API but are mostly
 * exposed during registry creation/modification.
 */
@NullMarked
package io.papermc.paper.registry.data;

import org.jspecify.annotations.NullMarked;
