package io.papermc.paper.jsonrpc;

import org.bukkit.Server;
import org.jspecify.annotations.NullMarked;

/**
 * Functional interfaces for RPC method handlers.
 */
@NullMarked
public final class MethodHandler {

    private MethodHandler() {
        // Utility class
    }

    /**
     * Handler for parameterless RPC methods.
     *
     * @param <Result> The result type
     */
    @FunctionalInterface
    public interface Parameterless<Result> {
        /**
         * Handles the RPC method call.
         *
         * @param server The Bukkit server instance
         * @param clientInfo Information about the calling client
         * @return The result to send back to the client
         */
        Result handle(Server server, ClientInfo clientInfo);
    }

    /**
     * Handler for RPC methods with parameters.
     *
     * @param <Params> The parameter type
     * @param <Result> The result type
     */
    @FunctionalInterface
    public interface WithParams<Params, Result> {
        /**
         * Handles the RPC method call.
         *
         * @param server The Bukkit server instance
         * @param params The parameters from the client
         * @param clientInfo Information about the calling client
         * @return The result to send back to the client
         */
        Result handle(Server server, Params params, ClientInfo clientInfo);
    }
}
