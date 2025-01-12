package org.bukkit;

import static org.junit.jupiter.api.Assertions.*;
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
import org.junit.jupiter.api.Test;
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
        "Lorg/bukkit/UndefinedNullability;",
        // Paper start
        "Lorg/checkerframework/checker/nullness/qual/MonotonicNonNull;",
        "Lorg/checkerframework/checker/nullness/qual/NonNull;",
        "Lorg/checkerframework/checker/nullness/qual/Nullable;",
        "Lorg/checkerframework/checker/nullness/qual/PolyNull;",
        // Paper end
    };

    private static final String[] EXCLUDED_CLASSES = {
        // Internal technical classes
        "org/bukkit/plugin/java/JavaPluginLoader",
        "org/bukkit/util/io/BukkitObjectInputStream",
        "org/bukkit/util/io/BukkitObjectOutputStream",
        "org/bukkit/util/io/Wrapper",
        "org/bukkit/plugin/java/PluginClassLoader",
        // Generic functional interface
        "org/bukkit/util/Consumer",
        // Paper start
        "io/papermc/paper/util/TransformingRandomAccessList",
        "io/papermc/paper/util/TransformingRandomAccessList$TransformedListIterator",
        // Timings history is broken in terms of nullability due to guavas Function defining that the param is NonNull
        "co/aikar/timings/TimingHistory$2",
        "co/aikar/timings/TimingHistory$2$1",
        "co/aikar/timings/TimingHistory$2$1$1",
        "co/aikar/timings/TimingHistory$2$1$2",
        "co/aikar/timings/TimingHistory$3",
        "co/aikar/timings/TimingHistory$4",
        "co/aikar/timings/TimingHistoryEntry$1"
        // Paper end
    };

    @Test
    public void testAll() throws IOException, URISyntaxException {
        URL loc = Bukkit.class.getProtectionDomain().getCodeSource().getLocation();
        File file = new File(loc.toURI());

        // Running from jar is not supported yet
        assertTrue(file.isDirectory(), "code must be in a directory");

        final HashMap<String, ClassNode> foundClasses = new HashMap<>();
        collectClasses(file, foundClasses);

        final ArrayList<String> errors = new ArrayList<>();

        for (ClassNode clazz : foundClasses.values()) {
            if (!isClassIncluded(clazz, foundClasses)) {
                continue;
            }

            // Paper start - skip class if it's @NullMarked
            if (isClassNullMarked(clazz, foundClasses)) {
                continue;
            }
            // Paper end - skip class if it's @NullMarked

            for (MethodNode method : clazz.methods) {
                if (!isMethodIncluded(clazz, method, foundClasses)) {
                    continue;
                }

                if (mustBeAnnotated(Type.getReturnType(method.desc)) && !isWellAnnotated(method.invisibleAnnotations)) {
                    // Paper start - Allow use of TYPE_USE annotations
                    boolean warn = true;
                    if (isWellAnnotated(method.visibleTypeAnnotations)) {
                        warn = false;
                    } else if (method.invisibleTypeAnnotations != null) {
                        dance: for (final org.objectweb.asm.tree.TypeAnnotationNode invisibleTypeAnnotation : method.invisibleTypeAnnotations) {
                            final org.objectweb.asm.TypeReference ref = new org.objectweb.asm.TypeReference(invisibleTypeAnnotation.typeRef);
                            if (ref.getSort() == org.objectweb.asm.TypeReference.METHOD_RETURN && java.util.Arrays.asList(ACCEPTED_ANNOTATIONS).contains(invisibleTypeAnnotation.desc)) {
                                warn = false;
                                break dance; // cha cha real smooth
                            }
                        }
                    }
                    if (warn)
                    // Paper end
                    warn(errors, clazz, method, "return value");
                }

                Type[] paramTypes = Type.getArgumentTypes(method.desc);
                List<ParameterNode> parameters = method.parameters;

                dancing: // Paper
                for (int i = 0; i < paramTypes.length; i++) {
                    if (mustBeAnnotated(paramTypes[i]) ^ isWellAnnotated(method.invisibleParameterAnnotations == null ? null : method.invisibleParameterAnnotations[i])) {
                        // Paper start
                        if (method.invisibleTypeAnnotations != null) {
                            for (final org.objectweb.asm.tree.TypeAnnotationNode invisibleTypeAnnotation : method.invisibleTypeAnnotations) {
                                final org.objectweb.asm.TypeReference ref = new org.objectweb.asm.TypeReference(invisibleTypeAnnotation.typeRef);
                                if (ref.getSort() == org.objectweb.asm.TypeReference.METHOD_FORMAL_PARAMETER && ref.getTypeParameterIndex() == i && java.util.Arrays.asList(ACCEPTED_ANNOTATIONS).contains(invisibleTypeAnnotation.desc)) {
                                    continue dancing;
                                }
                            }
                        }
                        if (method.visibleTypeAnnotations != null) {
                            for (final org.objectweb.asm.tree.TypeAnnotationNode invisibleTypeAnnotation : method.visibleTypeAnnotations) {
                                final org.objectweb.asm.TypeReference ref = new org.objectweb.asm.TypeReference(invisibleTypeAnnotation.typeRef);
                                if (ref.getSort() == org.objectweb.asm.TypeReference.METHOD_FORMAL_PARAMETER && ref.getTypeParameterIndex() == i && java.util.Arrays.asList(ACCEPTED_ANNOTATIONS).contains(invisibleTypeAnnotation.desc)) {
                                    continue dancing;
                                }
                            }
                        }
                        // Paper end - Allow use of TYPE_USE annotations
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

        StringBuilder builder = new StringBuilder()
            .append("There ")
            .append(errors.size() != 1 ? "are " : "is ")
            .append(errors.size())
            .append(" missing annotation")
            .append(errors.size() != 1 ? "s:\n" : ":\n");

        for (String message : errors) {
            builder.append("\t").append(message).append("\n");
        }

        fail(builder.toString());
    }

    private static void collectClasses(@NotNull File from, @NotNull Map<String, ClassNode> to) throws IOException {
        if (from.isDirectory()) {
            // Paper start - skip packages with @NullMarked
            final File packageInfo = new File(from, "package-info.class");
            if (packageInfo.exists()) {
                try (final FileInputStream in = new FileInputStream(packageInfo)) {
                    final ClassReader cr = new ClassReader(in);

                    final ClassNode node = new ClassNode();
                    cr.accept(node, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

                    if (isClassNullMarked0(node)) {
                        return; // skip packages with @NullMarked
                    }
                }
            }
            // Paper end - skip packages with @NullMarked
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

    // Paper start - skip class if it's @NullMarked
    private static boolean isClassNullMarked(@NotNull ClassNode clazz, @NotNull Map<String, ClassNode> allClasses) {
        if (isClassNullMarked0(clazz)) {
            return true;
        }
        if (clazz.nestHostClass != null) {
            final ClassNode nestHostNode = allClasses.get(clazz.nestHostClass);
            if (nestHostNode != null) {
                return isClassNullMarked(nestHostNode, allClasses);
            }
        }
        return false;
    }

    private static boolean isClassNullMarked0(@NotNull ClassNode clazz) {
        return clazz.visibleAnnotations != null && clazz.visibleAnnotations.stream().anyMatch(node -> "Lorg/jspecify/annotations/NullMarked;".equals(node.desc));
    }
    // Paper end - skip class if it's @NullMarked

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
        // Paper start
        if (isInternal(clazz.invisibleAnnotations)) {
            return false;
        }
        // Paper end

        for (String excludedClass : EXCLUDED_CLASSES) {
            if (excludedClass.equals(clazz.name)) {
                return false;
            }
        }

        return true;
    }

    private static boolean isMethodIncluded(@NotNull ClassNode clazz, @NotNull MethodNode method, @NotNull Map<String, ClassNode> allClasses) {
        // Exclude private, synthetic and deprecated methods
        if ((method.access & (Opcodes.ACC_PRIVATE | Opcodes.ACC_SYNTHETIC | Opcodes.ACC_DEPRECATED)) != 0 || (method.access & (Opcodes.ACC_PRIVATE | Opcodes.ACC_PROTECTED | Opcodes.ACC_PUBLIC)) == 0) { // Paper - ignore package-private
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
        // Paper start
        if (isInternal(method.invisibleAnnotations)) {
            return false;
        }
        // Paper end

        return true;
    }
    // Paper start
    private static boolean isInternal(List<? extends AnnotationNode> annotations) {
        if (annotations == null) {
            return false;
        }
        for (AnnotationNode node : annotations) {
            if (node.desc.equals("Lorg/jetbrains/annotations/ApiStatus$Internal;")) {
                return true;
            }
        }

        return false;
    }
    // Paper end

    private static boolean isWellAnnotated(@Nullable List<? extends AnnotationNode> annotations) { // Paper
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
