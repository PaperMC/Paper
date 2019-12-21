package org.bukkit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.ParameterNode;

public class AnnotationTest {

    private static final String[] ACCEPTED_ANNOTATIONS = {
        "Lorg/jetbrains/annotations/Nullable;",
        "Lorg/jetbrains/annotations/NotNull;",
        "Lorg/jetbrains/annotations/Contract;",
        "Lorg/bukkit/UndefinedNullability;"
    };

    private static final String[] EXCLUDED_CLASSES = {
        // Internal technical classes
        "org/bukkit/plugin/java/JavaPluginLoader",
        "org/bukkit/util/io/BukkitObjectInputStream",
        "org/bukkit/util/io/BukkitObjectOutputStream",
        "org/bukkit/util/io/Wrapper",
        "org/bukkit/plugin/java/PluginClassLoader",
        // Generic functional interface
        "org/bukkit/util/Consumer"
    };

    @Test
    public void testAll() throws IOException, URISyntaxException {
        URL loc = Bukkit.class.getProtectionDomain().getCodeSource().getLocation();
        File file = new File(loc.toURI());

        // Running from jar is not supported yet
        Assert.assertTrue("code must be in a directory", file.isDirectory());

        final HashMap<String, ClassNode> foundClasses = new HashMap<>();
        collectClasses(file, foundClasses);

        final ArrayList<String> errors = new ArrayList<>();

        for (ClassNode clazz : foundClasses.values()) {
            if (!isClassIncluded(clazz, foundClasses)) {
                continue;
            }

            for (MethodNode method : clazz.methods) {
                if (!isMethodIncluded(clazz, method, foundClasses)) {
                    continue;
                }

                if (mustBeAnnotated(Type.getReturnType(method.desc)) && !isWellAnnotated(method.invisibleAnnotations)) {
                    warn(errors, clazz, method, "return value");
                }

                Type[] paramTypes = Type.getArgumentTypes(method.desc);
                List<ParameterNode> parameters = method.parameters;

                for (int i = 0; i < paramTypes.length; i++) {
                    if (mustBeAnnotated(paramTypes[i]) && !isWellAnnotated(method.invisibleParameterAnnotations == null ? null : method.invisibleParameterAnnotations[i])) {
                        ParameterNode paramNode = parameters == null ? null : parameters.get(i);
                        String paramName = paramNode == null ? null : paramNode.name;

                        warn(errors, clazz, method, "parameter " + i + (paramName == null ? "" : ": " + paramName));
                    }
                }
            }
        }

        if (errors.isEmpty()) {
            // Success
            return;
        }

        Collections.sort(errors);

        System.out.println(errors.size() + " missing annotation(s):");
        for (String message : errors) {
            System.out.print("\t");
            System.out.println(message);
        }

        Assert.fail("There " + errors.size() + " are missing annotation(s)");
    }

    private static void collectClasses(@NotNull File from, @NotNull Map<String, ClassNode> to) throws IOException {
        if (from.isDirectory()) {
            final File[] files = from.listFiles();
            assert files != null;

            for (File file : files) {
                collectClasses(file, to);
            }
            return;
        }

        if (!from.getName().endsWith(".class")) {
            return;
        }

        try (FileInputStream in = new FileInputStream(from)) {
            final ClassReader cr = new ClassReader(in);

            final ClassNode node = new ClassNode();
            cr.accept(node, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

            to.put(node.name, node);
        }
    }

    private static boolean isClassIncluded(@NotNull ClassNode clazz, @NotNull Map<String, ClassNode> allClasses) {
        // Exclude private, synthetic or deprecated classes and annotations, since their members can't be null
        if ((clazz.access & (Opcodes.ACC_PRIVATE | Opcodes.ACC_SYNTHETIC | Opcodes.ACC_DEPRECATED | Opcodes.ACC_ANNOTATION)) != 0) {
            return false;
        }

        if (isSubclassOf(clazz, "org/bukkit/material/MaterialData", allClasses)) {
            throw new AssertionError("Subclass of MaterialData must be deprecated: " + clazz.name);
        }

        if (isSubclassOf(clazz, "java/lang/Exception", allClasses)
                || isSubclassOf(clazz, "java/lang/RuntimeException", allClasses)) {
            // Exceptions are excluded
            return false;
        }

        for (String excludedClass : EXCLUDED_CLASSES) {
            if (excludedClass.equals(clazz.name)) {
                return false;
            }
        }

        return true;
    }

    private static boolean isMethodIncluded(@NotNull ClassNode clazz, @NotNull MethodNode method, @NotNull Map<String, ClassNode> allClasses) {
        // Exclude private, synthetic and deprecated methods
        if ((method.access & (Opcodes.ACC_PRIVATE | Opcodes.ACC_SYNTHETIC | Opcodes.ACC_DEPRECATED)) != 0) {
            return false;
        }

        // Exclude Java methods
        if (is(method, "toString", 0) || is(method, "clone", 0) || is(method, "equals", 1)) {
            return false;
        }

        // Exclude generated Enum methods
        if (isSubclassOf(clazz, "java/lang/Enum", allClasses) && (is(method, "values", 0) || is(method, "valueOf", 1))) {
            return false;
        }

        // Anonymous classes have generated constructors, which can't be annotated nor invoked
        if ("<init>".equals(method.name) && isAnonymous(clazz)) {
            return false;
        }

        return true;
    }

    private static boolean isWellAnnotated(@Nullable List<AnnotationNode> annotations) {
        if (annotations == null) {
            return false;
        }

        for (AnnotationNode node : annotations) {
            for (String acceptedAnnotation : ACCEPTED_ANNOTATIONS) {
                if (acceptedAnnotation.equals(node.desc)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean mustBeAnnotated(@NotNull Type type) {
        return type.getSort() == Type.ARRAY || type.getSort() == Type.OBJECT;
    }

    private static boolean is(@NotNull MethodNode method, @NotNull String name, int parameters) {
        final List<ParameterNode> params = method.parameters;
        return method.name.equals(name) && (params == null || params.size() == parameters);
    }

    /**
     * Checks if the class is anonymous.
     *
     * @param clazz the class to check
     * @return true if given class is anonymous
     */
    private static boolean isAnonymous(@NotNull ClassNode clazz) {
        final String name = clazz.name;
        if (name == null) {
            return false;
        }
        final int nestedSeparator = name.lastIndexOf('$');
        if (nestedSeparator == -1 || nestedSeparator + 1 == name.length()) {
            return false;
        }

        // Nested classes have purely numeric names. Java classes can't begin with a number,
        // so if first character is a number, the class must be anonymous
        final char c = name.charAt(nestedSeparator + 1);
        return c >= '0' && c <= '9';
    }

    private static boolean isSubclassOf(@NotNull ClassNode what, @NotNull String ofWhat, @NotNull Map<String, ClassNode> allClasses) {
        if (ofWhat.equals(what.name)
                // Not only optimization: Super class may not be present in allClasses, so it is checked here
                || ofWhat.equals(what.superName)) {
            return true;
        }

        final ClassNode parent = allClasses.get(what.superName);
        if (parent != null && isSubclassOf(parent, ofWhat, allClasses)) {
            return true;
        }

        for (String superInterface : what.interfaces) {
            final ClassNode interfaceParent = allClasses.get(superInterface);
            if (interfaceParent != null && isSubclassOf(interfaceParent, ofWhat, allClasses)) {
                return true;
            }
        }

        return false;
    }

    private static void warn(@NotNull Collection<String> out, @NotNull ClassNode clazz, @NotNull MethodNode method, @NotNull String description) {
        out.add(clazz.name + " \t" + method.name + " \t" + description);
    }
}
