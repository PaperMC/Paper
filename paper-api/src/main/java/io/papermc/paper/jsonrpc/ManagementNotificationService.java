package io.papermc.paper.jsonrpc;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

/**
 * Service for sending custom notifications through the Management API (JSON-RPC over WebSocket).
 * <p>
 * Plugins can use this service to register custom notification types and broadcast data
 * to connected management clients.
 * </p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * ManagementNotificationService service = Bukkit.getServicesManager().load(ManagementNotificationService.class);
 *
 * // Register a notification type
 * NotificationHandle<MyData> handle = service.registerNotification(
 *     Key.key("myplugin", "custom_event"),
 *     "My custom event notification",
 *     MyData.class
 * );
 *
 * // Send notification
 * MyData data = new MyData("example", 123);
 * handle.send(data);
 * }</pre>
 */
@NullMarked
public interface ManagementNotificationService {

    /**
     * Registers a parameterless notification type.
     * <p>
     * The notification will be registered with the name: {@code notification/plugin/<namespace>/<name>}
     * </p>
     *
     * @param key The key for the notification (namespace typically your plugin name)
     * @param description A description of what this notification represents
     * @return A handle to send this notification type
     * @throws IllegalArgumentException if the notification is already registered
     * @throws IllegalStateException if the management server is not available
     */
    NotificationHandle<Void> registerNotification(
        Key key,
        String description
    );

    /**
     * Registers a notification type with a data parameter.
     * <p>
     * The notification will be registered with the name: {@code notification/plugin/<namespace>/<name>}
     * </p>
     * <p>
     * The data class must have appropriate JSON serialization support. Fields should be
     * primitive types, Strings, or other JSON-serializable objects.
     * </p>
     *
     * @param key The key for the notification (namespace typically your plugin name)
     * @param description A description of what this notification represents
     * @param dataClass The class type of the data to be sent with this notification
     * @param <T> The type of data
     * @return A handle to send this notification type
     * @throws IllegalArgumentException if the notification is already registered or dataClass is not serializable
     * @throws IllegalStateException if the management server is not available
     */
    <T> NotificationHandle<T> registerNotification(
        Key key,
        String description,
        Class<T> dataClass
    );

    /**
     * Checks if a notification with the given key is registered.
     *
     * @param key The key of the notification
     * @return true if the notification is registered
     */
    boolean isNotificationRegistered(Key key);

    /**
     * Gets a handle to a previously registered notification.
     *
     * @param key The key of the notification
     * @param <T> The type of data for this notification
     * @return The notification handle, or null if not registered
     */
    @Nullable
    <T> NotificationHandle<T> getNotificationHandle(Key key);

    /**
     * Checks if the management server is currently running and accepting connections.
     *
     * @return true if the management server is available
     */
    boolean isManagementServerAvailable();

    /**
     * Gets the number of currently connected management clients.
     *
     * @return The number of connected clients
     */
    int getConnectedClientCount();

    // ===== RPC Method Registration (Request-Response) =====

    /**
     * Registers a parameterless RPC method that clients can call.
     * <p>
     * The method will be registered with the name: {@code plugin/<namespace>/<name>}
     * </p>
     * <p>
     * Clients can call this method and receive a response:
     * <pre>{@code
     * Request:  {"jsonrpc":"2.0","method":"minecraft:plugin/myplugin/get_data","id":"123"}
     * Response: {"jsonrpc":"2.0","result":{...},"id":"123"}
     * }</pre>
     *
     * @param key The key for the method (namespace typically your plugin name)
     * @param description A description of what this method does
     * @param resultClass The class type of the result
     * @param handler The handler function that processes the request
     * @param <Result> The type of result
     * @return A handle to the registered method
     * @throws IllegalArgumentException if the method is already registered or resultClass is not serializable
     * @throws IllegalStateException if the management server is not available
     */
    <Result> MethodHandle<Void, Result> registerMethod(
        Key key,
        String description,
        Class<Result> resultClass,
        MethodHandler.Parameterless<Result> handler
    );

    /**
     * Registers an RPC method with parameters that clients can call.
     * <p>
     * The method will be registered with the name: {@code plugin/<namespace>/<name>}
     * </p>
     * <p>
     * Clients can call this method with parameters and receive a response:
     * <pre>{@code
     * Request:  {"jsonrpc":"2.0","method":"minecraft:plugin/myplugin/process","params":[{...}],"id":"123"}
     * Response: {"jsonrpc":"2.0","result":{...},"id":"123"}
     * }</pre>
     *
     * @param key The key for the method (namespace typically your plugin name)
     * @param description A description of what this method does
     * @param paramsClass The class type of the parameters
     * @param resultClass The class type of the result
     * @param handler The handler function that processes the request
     * @param <Params> The type of parameters
     * @param <Result> The type of result
     * @return A handle to the registered method
     * @throws IllegalArgumentException if the method is already registered or classes are not serializable
     * @throws IllegalStateException if the management server is not available
     */
    <Params, Result> MethodHandle<Params, Result> registerMethod(
        Key key,
        String description,
        Class<Params> paramsClass,
        Class<Result> resultClass,
        MethodHandler.WithParams<Params, Result> handler
    );

    /**
     * Checks if a method with the given key is registered.
     *
     * @param key The key of the method
     * @return true if the method is registered
     */
    boolean isMethodRegistered(Key key);

    /**
     * Gets a handle to a previously registered method.
     *
     * @param key The key of the method
     * @param <Params> The type of parameters
     * @param <Result> The type of result
     * @return The method handle, or null if not registered
     */
    @Nullable
    <Params, Result> MethodHandle<Params, Result> getMethodHandle(Key key);
}
