package org.bukkit.craftbukkit.legacy.reroute;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import org.bukkit.craftbukkit.util.ApiVersion;
import org.bukkit.craftbukkit.util.ClassTraverser;
import org.jetbrains.annotations.VisibleForTesting;
import org.objectweb.asm.Type;

public class Reroute {

    @VisibleForTesting
    final Map<String, RerouteDataHolder> rerouteDataMap;

    Reroute(Map<String, RerouteDataHolder> rerouteDataMap) {
        this.rerouteDataMap = rerouteDataMap;
    }

    /*
    This method looks (and probably is) overengineered, but it gives the most flexible when it comes to remapping normal methods to static one.
    The problem with normal owner and desc replacement is that child classes have them as an owner, instead there parents for there parents methods

    For example, if we have following two interfaces org.bukkit.BlockData and org.bukkit.Orientable extents BlockData
    and BlockData has the method org.bukkit.Material getType which we want to reroute to the static method
    org.bukkit.Material org.bukkit.craftbukkit.legacy.EnumEvil#getType(org.bukkit.BlockData)

    If we now call BlockData#getType we get as the owner org/bukkit/BlockData and as desc ()Lorg/bukkit/Material;
    Which we can nicely reroute by checking if the owner is BlockData and the name getType
    The problem, starts if we use Orientable#getType no we get as owner org/bukkit/Orientable and as desc ()Lorg/bukkit/Material;
    Now we can now longer safely say to which getType method we need to reroute (assume there are multiple getType methods from different classes,
    which are not related to BlockData), simple using the owner class will not work, since would reroute to
    EnumEvil#getType(org.bukkit.Orientable) which is not EnumEvil#getType(org.bukkit.BlockData) and will throw a method not found error
    at runtime.

    Meaning we would need to add checks for each subclass, which would be pur insanity.

    To solve this, we go through each super class and interfaces (and their super class and interfaces etc.) and try to get an owner
    which matches with one of our replacement methods. Based on how inheritance works in java, this method should be safe to use.

    As a site note: This method could also be used for the other method reroute, e.g. legacy method rerouting, where only the replacement
    method needs to be written, and this method figures out the rest, which could reduce the size and complexity of the Commodore class.
    The question then becomes one about performance (since this is not the most performance way) and convenience.
    But since it is only applied for each class and method call once when they get first loaded, it should not be that bad.
    (Although some load time testing could be done)
     */
    public boolean apply(ApiVersion pluginVersion, String owner, String name, String desc, boolean staticCall, Consumer<RerouteMethodData> consumer) {
        RerouteDataHolder rerouteData = rerouteDataMap.get(desc + name);
        if (rerouteData == null) {
            return false;
        }

        Type ownerType = Type.getObjectType(owner);
        RerouteMethodData data = rerouteData.get(ownerType);

        if (staticCall && data == null) {
            return false;
        }

        if (data != null) {
            if (data.requiredPluginVersion() != null && !data.requiredPluginVersion().test(pluginVersion)) {
                return false;
            }

            consumer.accept(data);
            return true;
        }

        Class<?> ownerClass;
        try {
            ownerClass = Class.forName(ownerType.getClassName(), false, Reroute.class.getClassLoader());
        } catch (ClassNotFoundException e) {
            return false;
        }

        ClassTraverser it = new ClassTraverser(ownerClass);
        while (it.hasNext()) {
            Class<?> clazz = it.next();

            data = rerouteData.get(Type.getType(clazz));

            if (data == null) {
                continue;
            }

            if (data.requiredPluginVersion() != null && !data.requiredPluginVersion().test(pluginVersion)) {
                return false;
            }

            consumer.accept(data);
            return true;
        }

        return false;
    }

    static class RerouteDataHolder {

        @VisibleForTesting
        final Map<String, RerouteMethodData> rerouteMethodDataMap = new HashMap<>();

        public RerouteMethodData get(Class<?> clazz) {
            return rerouteMethodDataMap.get(Type.getInternalName(clazz));
        }

        private RerouteMethodData get(Type owner) {
            return rerouteMethodDataMap.get(owner.getInternalName());
        }

        void add(RerouteMethodData value) {
            RerouteMethodData rerouteMethodData = get(value.sourceOwner());

            if (rerouteMethodData != null) {
                throw new IllegalStateException("Reroute method data already exists: " + rerouteMethodData);
            }

            rerouteMethodDataMap.put(value.sourceOwner().getInternalName(), value);
        }
    }
}
