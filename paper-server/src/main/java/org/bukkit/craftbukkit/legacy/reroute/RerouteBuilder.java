package org.bukkit.craftbukkit.legacy.reroute;

import com.google.common.base.Preconditions;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import org.bukkit.craftbukkit.util.ApiVersion;
import org.objectweb.asm.Type;

public class RerouteBuilder {

    private final List<Class<?>> classes = new ArrayList<>();
    private final Predicate<String> compatibilityPresent;

    private RerouteBuilder(Predicate<String> compatibilityPresent) {
        this.compatibilityPresent = compatibilityPresent;
    }

    public static RerouteBuilder create(Predicate<String> compatibilityPresent) {
        return new RerouteBuilder(compatibilityPresent);
    }

    public RerouteBuilder forClass(Class<?> clazz) {
        this.classes.add(clazz);
        return this;
    }

    public Reroute build() {
        Map<String, Reroute.RerouteDataHolder> rerouteDataHolderMap = new HashMap<>();

        for (Class<?> clazz : this.classes) {
            List<RerouteMethodData> data = RerouteBuilder.buildFromClass(clazz, this.compatibilityPresent);
            data.forEach(value -> rerouteDataHolderMap.computeIfAbsent(value.methodKey(), v -> new Reroute.RerouteDataHolder()).add(value));
        }

        return new Reroute(rerouteDataHolderMap);
    }

    private static List<RerouteMethodData> buildFromClass(Class<?> clazz, Predicate<String> compatibilityPresent) {
        Preconditions.checkArgument(!clazz.isInterface(), "Interface Classes are currently not supported");

        List<RerouteMethodData> result = new ArrayList<>();
        boolean shouldInclude = RerouteBuilder.shouldInclude(RerouteBuilder.getRequireCompatibility(clazz), true, compatibilityPresent);

        for (Method method : clazz.getDeclaredMethods()) {
            if (!RerouteBuilder.isMethodValid(method)) {
                continue;
            }

            if (!RerouteBuilder.shouldInclude(RerouteBuilder.getRequireCompatibility(method), shouldInclude, compatibilityPresent)) {
                continue;
            }

            result.add(RerouteBuilder.buildFromMethod(method));
        }

        return result;
    }

    private static RerouteMethodData buildFromMethod(Method method) {
        RerouteReturn rerouteReturn = new RerouteReturn(Type.getReturnType(method));
        List<RerouteArgument> arguments = new ArrayList<>();
        List<RerouteArgument> sourceArguments = new ArrayList<>();

        for (Parameter parameter : method.getParameters()) {
            Type type = Type.getType(parameter.getType());
            int count = 0;
            boolean injectPluginName = false;
            boolean injectPluginVersion = false;
            String injectCompatibility = null;
            if (parameter.isAnnotationPresent(InjectPluginName.class)) {
                if (parameter.getType() != String.class) {
                    throw new RuntimeException("Plugin name argument must be of type name, but got " + parameter.getType());
                }
                injectPluginName = true;
                count++;
            }

            if (parameter.isAnnotationPresent(InjectPluginVersion.class)) {
                if (parameter.getType() != ApiVersion.class) {
                    throw new RuntimeException("Plugin version argument must be of type ApiVersion, but got " + parameter.getType());
                }
                injectPluginVersion = true;
                count++;
            }

            if (parameter.isAnnotationPresent(InjectCompatibility.class)) {
                if (parameter.getType() != boolean.class) {
                    throw new RuntimeException("Compatibility argument must be of type boolean, but got " + parameter.getType());
                }
                injectCompatibility = parameter.getAnnotation(InjectCompatibility.class).value();
                count++;
            }

            if (count > 1) {
                // This should not happen, since we check types,
                // and those two have different types -> it would already have failed
                throw new RuntimeException("Wtf?");
            }

            RerouteArgumentType rerouteArgumentType = parameter.getAnnotation(RerouteArgumentType.class);
            if (count == 1 && rerouteArgumentType != null) {
                // Why would you do this?
                throw new RuntimeException("Wtf?");
            }

            Type sourceType;
            if (rerouteArgumentType != null) {
                sourceType = Type.getObjectType(rerouteArgumentType.value());
            } else {
                sourceType = type;
            }

            RerouteArgument argument = new RerouteArgument(type, sourceType, injectPluginName, injectPluginVersion, injectCompatibility);
            arguments.add(argument);
            if (count == 0) {
                sourceArguments.add(argument);
            }
        }

        RerouteStatic rerouteStatic = method.getAnnotation(RerouteStatic.class);
        Type sourceOwner;
        if (rerouteStatic != null) {
            sourceOwner = Type.getObjectType(rerouteStatic.value());
        } else {
            if (sourceArguments.isEmpty()) {
                throw new RuntimeException("Source argument list is empty, no owner class found");
            }
            RerouteArgument argument = sourceArguments.getFirst();
            sourceOwner = argument.sourceType();
            sourceArguments.remove(argument);
        }

        RerouteReturnType rerouteReturnType = method.getAnnotation(RerouteReturnType.class);
        Type returnType;
        if (rerouteReturnType != null) {
            returnType = Type.getObjectType(rerouteReturnType.value());
        } else {
            returnType = rerouteReturn.type();
        }
        Type sourceDesc = Type.getMethodType(returnType, sourceArguments.stream().map(RerouteArgument::sourceType).toArray(Type[]::new));

        RerouteMethodName rerouteMethodName = method.getAnnotation(RerouteMethodName.class);
        String methodName;
        if (rerouteMethodName != null) {
            methodName = rerouteMethodName.value();
        } else {
            methodName = method.getName();
        }

        String methodKey = sourceDesc.getDescriptor() + methodName;

        Type targetType = Type.getType(method);

        boolean inBukkit = !method.isAnnotationPresent(NotInBukkit.class) && !method.getDeclaringClass().isAnnotationPresent(NotInBukkit.class);

        RequirePluginVersionData requiredPluginVersion = null;
        if (method.isAnnotationPresent(RequirePluginVersion.class)) {
            requiredPluginVersion = RequirePluginVersionData.create(method.getAnnotation(RequirePluginVersion.class));
        } else if (method.getDeclaringClass().isAnnotationPresent(RequirePluginVersion.class)) {
            requiredPluginVersion = RequirePluginVersionData.create(method.getDeclaringClass().getAnnotation(RequirePluginVersion.class));
        }

        return new RerouteMethodData(methodKey, sourceDesc, sourceOwner, methodName, rerouteStatic != null, targetType, Type.getInternalName(method.getDeclaringClass()), method.getName(), arguments, rerouteReturn, inBukkit, requiredPluginVersion);
    }

    private static boolean isMethodValid(Method method) {
        if (method.isBridge()) {
            return false;
        }

        if (method.isSynthetic()) {
            return false;
        }

        if (!Modifier.isPublic(method.getModifiers())) {
            return false;
        }

        if (!Modifier.isStatic(method.getModifiers())) {
            return false;
        }

        if (method.isAnnotationPresent(DoNotReroute.class)) {
            return false;
        }

        return true;
    }

    private static String getRequireCompatibility(AnnotatedElement element) {
        RequireCompatibility annotation = element.getAnnotation(RequireCompatibility.class);
        if (annotation == null) {
            return null;
        }

        return annotation.value();
    }

    private static boolean shouldInclude(String string, boolean parent, Predicate<String> compatibilityPresent) {
        if (string == null) {
            return parent;
        }

        return compatibilityPresent.test(string);
    }
}
