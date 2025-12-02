package io.papermc.paper.jsonrpc;

 import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.kyori.adventure.key.Key;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.jsonrpc.ManagementServer;
import net.minecraft.server.jsonrpc.api.MethodInfo;
import net.minecraft.server.jsonrpc.api.ParamInfo;
import net.minecraft.server.jsonrpc.api.Schema;
import net.minecraft.server.jsonrpc.methods.InvalidParameterJsonRpcException;
import net.minecraft.server.jsonrpc.JsonRPCUtils;
import io.papermc.paper.jsonrpc.ClientInfo;
import io.papermc.paper.jsonrpc.ManagementNotificationService;
import io.papermc.paper.jsonrpc.MethodHandle;
import io.papermc.paper.jsonrpc.MethodHandler;
import io.papermc.paper.jsonrpc.NotificationHandle;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of the plugin management notification service that bridges
 * Bukkit API calls to the internal JSON-RPC notification system.
 */
@NullMarked
public class PluginManagementNotificationService implements ManagementNotificationService {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new GsonBuilder().create();

    private final ManagementServer managementServer;
    private final Map<String, PluginNotificationHandle<?>> registeredNotifications = new ConcurrentHashMap<>();
    private final Map<String, PluginMethodHandle<?, ?>> registeredMethods = new ConcurrentHashMap<>();

    public PluginManagementNotificationService(ManagementServer managementServer) {
        this.managementServer = managementServer;
    }

    @Override
    public NotificationHandle<Void> registerNotification(
        Key key,
        String description
    ) {
        String keyString = key.asString();
        Preconditions.checkArgument(!registeredNotifications.containsKey(keyString), "Notification already registered: %s", keyString);

        PluginNotificationHandle<Void> handle = new PluginNotificationHandle<>(
            key,
            description,
            null,
            this
        );

        registeredNotifications.put(keyString, handle);
        LOGGER.info("Registered parameterless notification: {}", handle.getFullName());

        return handle;
    }

    @Override
    public <T> NotificationHandle<T> registerNotification(
        Key key,
        String description,
        Class<T> dataClass
    ) {
        String keyString = key.asString();
        Preconditions.checkArgument(!registeredNotifications.containsKey(keyString), "Notification already registered: %s", keyString);

        PluginNotificationHandle<T> handle = new PluginNotificationHandle<>(
            key,
            description,
            dataClass,
            this
        );

        registeredNotifications.put(keyString, handle);
        LOGGER.info("Registered notification: {} with data type: {}", handle.getFullName(), dataClass.getSimpleName());

        return handle;
    }

    @Override
    public boolean isNotificationRegistered(Key key) {
        return registeredNotifications.containsKey(key.asString());
    }

    @Override
    @Nullable
    @SuppressWarnings("unchecked")
    public <T> NotificationHandle<T> getNotificationHandle(Key key) {
        return (NotificationHandle<T>) registeredNotifications.get(key.asString());
    }

    @Override
    public boolean isManagementServerAvailable() {
        return managementServer != null;
    }

    @Override
    public int getConnectedClientCount() {
        if (managementServer == null) {
            return 0;
        }
        final int[] count = {0};
        managementServer.forEachConnection(connection -> count[0]++);
        return count[0];
    }

    // ===== RPC Method Registration =====

    @Override
    public <Result> MethodHandle<Void, Result> registerMethod(
        Key key,
        String description,
        Class<Result> resultClass,
        MethodHandler.Parameterless<Result> handler
    ) {
        String keyString = key.asString();
        Preconditions.checkArgument(!registeredMethods.containsKey(keyString), "Method already registered: %s", keyString);

        PluginMethodHandle<Void, Result> methodHandle = new PluginMethodHandle<>(
            key,
            description,
            null,
            resultClass,
            handler,
            null,
            this
        );

        registeredMethods.put(keyString, methodHandle);
        LOGGER.info("Registered RPC method: {}", methodHandle.getFullName());

        return methodHandle;
    }

    @Override
    public <Params, Result> MethodHandle<Params, Result> registerMethod(
        Key key,
        String description,
        Class<Params> paramsClass,
        Class<Result> resultClass,
        MethodHandler.WithParams<Params, Result> handler
    ) {
        String keyString = key.asString();
        Preconditions.checkArgument(!registeredMethods.containsKey(keyString), "Method already registered: %s", keyString);

        PluginMethodHandle<Params, Result> methodHandle = new PluginMethodHandle<>(
            key,
            description,
            paramsClass,
            resultClass,
            null,
            handler,
            this
        );

        registeredMethods.put(keyString, methodHandle);
        LOGGER.info("Registered RPC method: {} with params: {}", methodHandle.getFullName(), paramsClass.getSimpleName());

        return methodHandle;
    }

    @Override
    public boolean isMethodRegistered(Key key) {
        return registeredMethods.containsKey(key.asString());
    }

    @Override
    @Nullable
    @SuppressWarnings("unchecked")
    public <Params, Result> MethodHandle<Params, Result> getMethodHandle(Key key) {
        return (MethodHandle<Params, Result>) registeredMethods.get(key.asString());
    }

    /**
     * Dispatches a plugin RPC method call. Returns null if method not found.
     */
    @Nullable
    public JsonElement dispatchPluginMethod(String methodName, @Nullable JsonElement params, net.minecraft.server.jsonrpc.methods.ClientInfo clientInfo) {
        // Extract namespace and name from method (format: "plugin/namespace/name")
        if (!methodName.startsWith("plugin/")) {
            return null;
        }

        String remainder = methodName.substring("plugin/".length());
        int slashIndex = remainder.indexOf('/');
        if (slashIndex == -1) {
            return null;
        }

        String namespace = remainder.substring(0, slashIndex);
        String name = remainder.substring(slashIndex + 1);
        Key key = Key.key(namespace, name);

        PluginMethodHandle<?, ?> method = registeredMethods.get(key.asString());
        if (method == null) {
            return null;
        }

        try {
            return method.invoke(Bukkit.getServer(), params, new BukkitClientInfo(clientInfo));
        } catch (Exception e) {
            LOGGER.error("Error invoking plugin method {}: {}", methodName, e.getMessage(), e);
            throw new RuntimeException("Error invoking method: " + e.getMessage(), e);
        }
    }

    /**
     * Internal method to send a notification to all connected clients.
     */
    <T> void sendNotificationInternal(String fullName, @Nullable T data, @Nullable Class<T> dataClass) {
        Preconditions.checkState(managementServer != null, "Management server is not available");

        com.google.gson.JsonObject notification;
        if (data == null) {
            // Parameterless notification
            notification = JsonRPCUtils.createRequest(null, ResourceLocation.parse(fullName), java.util.List.of());
        } else {
            // Notification with data
            JsonElement jsonData = GSON.toJsonTree(data, dataClass);
            notification = JsonRPCUtils.createRequest(null, ResourceLocation.parse(fullName), java.util.List.of(jsonData));
        }

        managementServer.sendRawNotification(notification);
        LOGGER.debug("Sent notification {} to {} clients", fullName, getConnectedClientCount());
    }

    /**
     * Implementation of NotificationHandle for plugin notifications.
     */
    private static class PluginNotificationHandle<T> implements NotificationHandle<T> {
        private final Key key;
        private final String description;
        private final Class<T> dataClass;
        private final PluginManagementNotificationService service;

        public PluginNotificationHandle(
            Key key,
            String description,
            @Nullable Class<T> dataClass,
            PluginManagementNotificationService service
        ) {
            this.key = key;
            this.description = description;
            this.dataClass = dataClass;
            this.service = service;
        }

        @Override
        public String getNamespace() {
            return key.namespace();
        }

        @Override
        public String getName() {
            return key.value();
        }

        @Override
        public Key key() {
            return key;
        }

        @Override
        public String getFullName() {
            return "notification/plugin/" + key.namespace() + "/" + key.value();
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public void send(@Nullable T data) {
            Preconditions.checkArgument(!requiresData() || data != null, "Data is required for this notification type");
            Preconditions.checkArgument(requiresData() || data == null, "This notification does not accept data");

            service.sendNotificationInternal(getFullName(), data, dataClass);
        }

        @Override
        public boolean requiresData() {
            return dataClass != null;
        }
    }

    /**
     * Implementation of MethodHandle for plugin RPC methods.
     */
    private static class PluginMethodHandle<Params, Result> implements MethodHandle<Params, Result> {
        private final Key key;
        private final String description;
        private final Class<Params> paramsClass;
        private final Class<Result> resultClass;
        private final MethodHandler.Parameterless<Result> parameterlessHandler;
        private final MethodHandler.WithParams<Params, Result> paramsHandler;
        private final PluginManagementNotificationService service;

        public PluginMethodHandle(
            Key key,
            String description,
            @Nullable Class<Params> paramsClass,
            Class<Result> resultClass,
            @Nullable MethodHandler.Parameterless<Result> parameterlessHandler,
            @Nullable MethodHandler.WithParams<Params, Result> paramsHandler,
            PluginManagementNotificationService service
        ) {
            this.key = key;
            this.description = description;
            this.paramsClass = paramsClass;
            this.resultClass = resultClass;
            this.parameterlessHandler = parameterlessHandler;
            this.paramsHandler = paramsHandler;
            this.service = service;
        }

        @Override
        public String getNamespace() {
            return key.namespace();
        }

        @Override
        public String getName() {
            return key.value();
        }

        @Override
        public Key key() {
            return key;
        }

        @Override
        public String getFullName() {
            return "plugin/" + key.namespace() + "/" + key.value();
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public boolean requiresParameters() {
            return paramsClass != null;
        }

        /**
         * Invokes the RPC method handler.
         */
        @SuppressWarnings("unchecked")
        public JsonElement invoke(org.bukkit.Server server, @Nullable JsonElement paramsJson, ClientInfo clientInfo) {
            Result result;

            if (requiresParameters()) {
                if (paramsJson == null || (paramsJson.isJsonArray() && paramsJson.getAsJsonArray().isEmpty())) {
                    throw new InvalidParameterJsonRpcException("Parameters are required for this method");
                }

                // Extract params from JSON
                JsonElement paramElement;
                if (paramsJson.isJsonArray()) {
                    com.google.gson.JsonArray array = paramsJson.getAsJsonArray();
                    if (array.size() != 1) {
                        throw new InvalidParameterJsonRpcException("Expected exactly one parameter");
                    }
                    paramElement = array.get(0);
                } else {
                    paramElement = paramsJson;
                }

                Params params = GSON.fromJson(paramElement, paramsClass);
                result = paramsHandler.handle(server, params, clientInfo);
            } else {
                if (paramsJson != null && paramsJson.isJsonArray() && !paramsJson.getAsJsonArray().isEmpty()) {
                    throw new InvalidParameterJsonRpcException("This method does not accept parameters");
                }
                result = parameterlessHandler.handle(server, clientInfo);
            }

            // Convert result to JSON
            return GSON.toJsonTree(result, resultClass);
        }
    }

    /**
     * Bukkit ClientInfo wrapper.
     */
    private static class BukkitClientInfo implements ClientInfo {
        private final net.minecraft.server.jsonrpc.methods.ClientInfo nmsClientInfo;

        public BukkitClientInfo(net.minecraft.server.jsonrpc.methods.ClientInfo nmsClientInfo) {
            this.nmsClientInfo = nmsClientInfo;
        }

        @Override
        public int getConnectionId() {
            return nmsClientInfo.connectionId();
        }
    }
}
