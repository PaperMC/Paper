package org.bukkit.craftbukkit.legacy;

import static org.junit.jupiter.api.Assertions.*;
import com.google.common.base.Joiner;
import com.google.common.base.Predicates;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.stream.Stream;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.legacy.reroute.Reroute;
import org.bukkit.craftbukkit.legacy.reroute.RerouteBuilder;
import org.bukkit.craftbukkit.util.ApiVersion;
import org.bukkit.craftbukkit.util.Commodore;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

@Normal
public class MaterialReroutingTest {

    // Needs to be a bukkit class
    private static final URI BUKKIT_CLASSES;
    private static final Reroute MATERIAL_METHOD_REROUTE = RerouteBuilder.create(Predicates.alwaysTrue()).forClass(MaterialRerouting.class).build();

    static {
        try {
            BUKKIT_CLASSES = Bukkit.class.getProtectionDomain().getCodeSource().getLocation().toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static JarFile jarFile = null;

    public static Stream<Arguments> bukkitData() {
        return MaterialReroutingTest.jarFile
                .stream()
                .filter(entry -> entry.getName().endsWith(".class"))
                 // Add class exceptions here
                .filter(entry -> !entry.getName().endsWith("Material.class"))
                .filter(entry -> !entry.getName().endsWith("UnsafeValues.class"))
                .filter(entry -> !entry.getName().endsWith("BlockType.class"))
                .filter(entry -> !entry.getName().endsWith("ItemType.class"))
                .filter(entry -> !entry.getName().endsWith("Registry.class"))
                .filter(entry -> !entry.getName().startsWith("org/bukkit/material"))
                // Paper start - types that cannot be translated to ItemType/BlockType
                .filter(entry -> !entry.getName().equals("com/destroystokyo/paper/MaterialSetTag.class"))
                // Paper end - types that cannot be translated to ItemType/BlockType
                .map(entry -> {
                    try {
                        return MaterialReroutingTest.jarFile.getInputStream(entry);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).map(Arguments::arguments);
    }

    @BeforeAll
    public static void beforeAll() throws IOException {
        MaterialReroutingTest.jarFile = new JarFile(new File(MaterialReroutingTest.BUKKIT_CLASSES));
    }

    @ParameterizedTest
    @MethodSource("bukkitData")
    public void testBukkitClasses(InputStream inputStream) throws IOException {
        List<String> missingReroute = new ArrayList<>();

        try (inputStream) {
            ClassReader classReader = new ClassReader(inputStream);
            ClassNode classNode = new ClassNode(Opcodes.ASM9);

            classReader.accept(classNode, Opcodes.ASM9);

            for (MethodNode methodNode : classNode.methods) {
                String signature = methodNode.signature == null ? "" : methodNode.signature;
                // Add signature exceptions here
                signature = signature.replace("Lorg/bukkit/Tag<Lorg/bukkit/Material;>;", ""); // Gets handled specially

                if (methodNode.desc.contains("Lorg/bukkit/Material;") || signature.contains("Lorg/bukkit/Material;")) {
                    // Add method exceptions here
                    switch (methodNode.name) {
                        case "<init>", "setItemMeta0" -> {
                            continue;
                        }
                    }
                    // Paper start - filter out more methods from rerouting test
                    if (methodNode.name.startsWith("lambda$")) continue;
                    if ((methodNode.access & Opcodes.ACC_PRIVATE) != 0 || isInternal(methodNode.invisibleAnnotations)) continue;
                    // Paper end - filter out more methods from rerouting test

                    if (!Commodore.rerouteMethods(ApiVersion.CURRENT, MaterialReroutingTest.MATERIAL_METHOD_REROUTE, (methodNode.access & Opcodes.ACC_STATIC) != 0, classNode.name, methodNode.name, methodNode.desc, a -> { })) {
                        missingReroute.add(methodNode.name + " " + methodNode.desc + " " + methodNode.signature);
                    }
                }
            }

            assertTrue(missingReroute.isEmpty(), String.format("""
                    The class %s has methods with a Material as return or parameter in it, which does not have a reroute added to MaterialRerouting.
                    Please add it to MaterialRerouting or add an exception for it, if it should not be rerouted.

                    Following missing methods where found:
                    %s""", classNode.name, Joiner.on('\n').join(missingReroute)));
        }
    }

    // Paper start - filter out more methods from rerouting test
    private static boolean isInternal(final List<org.objectweb.asm.tree.AnnotationNode> annotationNodes) {
        return annotationNodes != null
            && annotationNodes.stream().anyMatch(a -> a.desc.equals("Lorg/jetbrains/annotations/ApiStatus$Internal;"));
    }
    // Paper end - filter out more methods from rerouting test

    @AfterAll
    public static void clear() throws IOException {
        if (MaterialReroutingTest.jarFile != null) {
            MaterialReroutingTest.jarFile.close();
        }
    }
}
