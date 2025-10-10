package io.papermc.paper.jsonrpc;

import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

/**
 * A handle to a registered RPC method that can be called by management clients.
 * <p>
 * Unlike notifications (one-way), methods are request-response:
 * clients send a request with an ID and receive a response.
 * </p>
 *
 * @param <Params> The type of parameters this method accepts (Void for parameterless)
 * @param <Result> The type of result this method returns
 */
@NullMarked
public interface MethodHandle<Params, Result> {

    /**
     * Gets the namespace of this method.
     *
     * @return The namespace
     */
    String getNamespace();

    /**
     * Gets the name of this method.
     *
     * @return The name
     */
    String getName();

    /**
     * Gets the key of this method.
     *
     * @return The key
     */
    Key key();

    /**
     * Gets the full method identifier in the format: {@code plugin/<namespace>/<name>}
     *
     * @return The full identifier
     */
    String getFullName();

    /**
     * Gets the description of this method.
     *
     * @return The description
     */
    String getDescription();

    /**
     * Checks if this method requires parameters.
     *
     * @return true if parameters are required
     */
    boolean requiresParameters();
}

