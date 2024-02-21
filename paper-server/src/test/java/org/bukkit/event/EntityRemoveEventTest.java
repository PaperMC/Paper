package org.bukkit.event;

import static org.junit.jupiter.api.Assertions.*;
import com.google.common.base.Joiner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.stream.Stream;
import net.minecraft.WorldVersion;
import net.minecraft.server.Main;
import net.minecraft.world.level.entity.EntityAccess;
import org.bukkit.support.AbstractTestingBase;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class EntityRemoveEventTest extends AbstractTestingBase {

    // Needs to be a class, which is present in the source, and not a test class
    private static final URI CRAFT_BUKKIT_CLASSES;
    // Needs to be a class, which is from the minecraft package and not patch by CraftBukkit
    private static final URI MINECRAFT_CLASSES;

    static {
        try {
            CRAFT_BUKKIT_CLASSES = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI();
            MINECRAFT_CLASSES = WorldVersion.class.getProtectionDomain().getCodeSource().getLocation().toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static JarFile jarFile = null;
    private static Stream<Path> files = null;

    public static Stream<Arguments> craftBukkitData() {
        return files
                .map(Path::toFile)
                .filter(File::isFile)
                .filter(file -> file.getName().endsWith(".class"))
                .filter(file -> !file.getName().equals("EntityAccess.class"))
                .map(file -> {
                    try {
                        return new FileInputStream(file);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }).map(Arguments::of);
    }

    public static Stream<Arguments> minecraftData() {
        return jarFile
                .stream()
                .filter(entry -> entry.getName().endsWith(".class"))
                .filter(entry -> !new File(CRAFT_BUKKIT_CLASSES.resolve(entry.getName())).exists())
                .filter(entry -> !entry.getName().startsWith("net/minecraft/gametest/framework"))
                .map(entry -> {
                    try {
                        return jarFile.getInputStream(entry);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).map(Arguments::arguments);
    }

    @BeforeAll
    public static void beforeAll() throws IOException {
        assertNotEquals(CRAFT_BUKKIT_CLASSES, MINECRAFT_CLASSES, """
                The minecraft and craft bukkit uri point to the same directory / file.
                Please make sure the CRAFT_BUKKIT_CLASSES points to the test class directory and MINECRAFT_CLASSES to the minecraft server jar.
                """);

        jarFile = new JarFile(new File(MINECRAFT_CLASSES));
        files = Files.walk(Path.of(CRAFT_BUKKIT_CLASSES));
    }

    @ParameterizedTest
    @MethodSource("minecraftData")
    public void testMinecraftClasses(InputStream inputStream) throws IOException, ClassNotFoundException {
        test(inputStream);
    }

    @ParameterizedTest
    @MethodSource("craftBukkitData")
    public void testCraftBukkitModifiedClasses(InputStream inputStream) throws IOException, ClassNotFoundException {
        test(inputStream);
    }

    private void test(InputStream inputStream) throws IOException, ClassNotFoundException {
        List<String> missingReason = new ArrayList<>();

        try (inputStream) {
            ClassReader classReader = new ClassReader(inputStream);
            ClassNode classNode = new ClassNode(Opcodes.ASM9);

            classReader.accept(classNode, Opcodes.ASM9);
            boolean minecraftCause = false;
            boolean bukkitCause = false;

            for (MethodNode methodNode : classNode.methods) {
                if (methodNode.name.equals("remove") && methodNode.desc.contains("Lnet/minecraft/world/entity/Entity$RemovalReason;")) {
                    if (methodNode.desc.contains("Lorg/bukkit/event/entity/EntityRemoveEvent$Cause;")) {
                        bukkitCause = true;
                    } else {
                        minecraftCause = true;
                    }
                }

                LineNumberNode lastLineNumber = null;
                for (AbstractInsnNode instruction : methodNode.instructions) {
                    if (instruction instanceof LineNumberNode lineNumberNode) {
                        lastLineNumber = lineNumberNode;
                        continue;
                    }

                    if (instruction instanceof MethodInsnNode methodInsnNode) {
                        // Check for discard and remove method call
                        if (check(methodInsnNode.owner, methodInsnNode.name, methodInsnNode.desc)) {
                            // Add to list
                            missingReason.add(String.format("Method name: %s, name: %s, line number: %s", methodNode.name, methodInsnNode.name, lastLineNumber.line));
                        }
                    } else if (instruction instanceof InvokeDynamicInsnNode dynamicInsnNode) {
                        // Check for discard and remove method call
                        if (!dynamicInsnNode.bsm.getOwner().equals("java/lang/invoke/LambdaMetafactory")
                                || !dynamicInsnNode.bsm.getName().equals("metafactory") || dynamicInsnNode.bsmArgs.length != 3) {
                            continue;
                        }

                        Handle handle = (Handle) dynamicInsnNode.bsmArgs[1];

                        if (check(handle.getOwner(), handle.getName(), handle.getDesc())) {
                            // Add to list
                            missingReason.add(String.format("[D] Method name: %s, name: %s, line number: %s", methodNode.name, handle.getName(), lastLineNumber.line));
                        }
                    }
                }
            }

            assertTrue(missingReason.isEmpty(), String.format("""
                    The class %s has Entity#discard, Entity#remove and/or Entity#setRemoved method calls, which don't have a bukkit reason.
                    Please add a bukkit reason to them, if the event should not be called use null as reason.

                    Following missing reasons where found:
                    %s""", classNode.name, Joiner.on('\n').join(missingReason)));

            if (minecraftCause == bukkitCause) {
                return;
            }

            if (minecraftCause) {
                fail(String.format("""
                        The class %s has the Entity#remove method override, but there is no bukkit override.
                        Please add a bukkit method override, which adds the bukkit cause.
                        """, classNode.name));
                return; // Will never reach ):
            }

            fail(String.format("""
                    The class %s has the Entity#remove method override, to add a bukkit cause, but there is no normal override.
                    Please remove the bukkit method override, since it is no longer needed.
                    """, classNode.name));
        }
    }

    private boolean check(String owner, String name, String desc) throws ClassNotFoundException {
        if (!name.equals("discard") && !name.equals("remove") && !name.equals("setRemoved")) {
            if (!checkExtraMethod(owner, name, desc)) {
                return false;
            }
        }

        if (desc.contains("Lorg/bukkit/event/entity/EntityRemoveEvent$Cause;")) {
            return false;
        }

        Class<?> ownerClass = Class.forName(owner.replace('/', '.'), false, getClass().getClassLoader());

        // Found missing discard, remove or setRemoved method call
        return EntityAccess.class.isAssignableFrom(ownerClass);
    }

    private boolean checkExtraMethod(String owner, String name, String desc) {
        if (owner.equals("net/minecraft/world/entity/projectile/EntityShulkerBullet")) {
            return name.equals("destroy");
        }

        return false;
    }

    @AfterAll
    public static void clear() throws IOException {
        if (jarFile != null) {
            jarFile.close();
        }

        if (files != null) {
            files.close();
        }
    }
}
