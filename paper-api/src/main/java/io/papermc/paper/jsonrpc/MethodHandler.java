package io.papermc.paper.jsonrpc;

import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;

/**
 * Functional interfaces for RPC method handlers.
 */
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
        @NotNull
        Result handle(@NotNull Server server, @NotNull ClientInfo clientInfo);
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
        @NotNull
        Result handle(@NotNull Server server, @NotNull Params params, @NotNull ClientInfo clientInfo);
    }
}
