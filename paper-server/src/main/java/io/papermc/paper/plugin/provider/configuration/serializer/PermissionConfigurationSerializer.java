package io.papermc.paper.plugin.provider.configuration.serializer;

import io.papermc.paper.plugin.provider.configuration.type.PermissionConfiguration;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PermissionConfigurationSerializer {

    public static final Serializer SERIALIZER = new Serializer();

    private static final class Serializer implements TypeSerializer<PermissionConfiguration> {
        private Serializer() {
            super();
        }

        @Override
        public PermissionConfiguration deserialize(Type type, ConfigurationNode node) throws SerializationException {
            Map<?, ?> map = (Map<?, ?>) node.node("permissions").raw();

            PermissionDefault permissionDefault;
            ConfigurationNode permNode = node.node("defaultPerm");
            if (permNode.virtual()) {
                permissionDefault = PermissionDefault.OP;
            } else {
                permissionDefault = PermissionDefault.getByName(permNode.getString());
            }

            List<Permission> result = new ArrayList<>();
            if (map != null) {
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    try {
                        result.add(Permission.loadPermission(entry.getKey().toString(), (Map<?, ?>) entry.getValue(), permissionDefault, result));
                    } catch (Throwable ex) {
                        throw new SerializationException((Type) null, "Error loading permission %s".formatted(entry.getKey()), ex);
                    }
                }
            }

            return new PermissionConfiguration(permissionDefault, List.copyOf(result));
        }

        @Override
        public void serialize(Type type, @org.checkerframework.checker.nullness.qual.Nullable PermissionConfiguration obj, ConfigurationNode node) throws SerializationException {

        }

    }
}
