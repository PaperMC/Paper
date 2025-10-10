package io.papermc.paper.jsonrpc;

import org.jetbrains.annotations.NotNull;

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
public interface MethodHandle<Params, Result> {

    /**
     * Gets the namespace of this method.
     *
     * @return The namespace
     */
    @NotNull
    String getNamespace();

    /**
     * Gets the name of this method.
     *
     * @return The name
     */
    @NotNull
    String getName();

    /**
     * Gets the full method identifier in the format: {@code plugin/<namespace>/<name>}
     *
     * @return The full identifier
     */
    @NotNull
    String getFullName();

    /**
     * Gets the description of this method.
     *
     * @return The description
     */
    @NotNull
    String getDescription();

    /**
     * Checks if this method requires parameters.
     *
     * @return true if parameters are required
     */
    boolean requiresParameters();
}

