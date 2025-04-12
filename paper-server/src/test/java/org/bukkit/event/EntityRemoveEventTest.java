package org.bukkit.event;

import static org.junit.jupiter.api.Assertions.*;
import com.google.common.base.Joiner;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.level.entity.EntityAccess;
import org.bukkit.support.environment.Normal;
import org.bukkit.support.test.ClassNodeTest;
import org.junit.jupiter.api.Disabled;
import org.objectweb.asm.Handle;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

@Normal
@Disabled // TODO Delete this test or re-enable it with changes
public class EntityRemoveEventTest {

    @ClassNodeTest(value = ClassNodeTest.ClassType.CRAFT_BUKKIT,
            excludedClasses = EntityAccess.class,
            excludedPackages = "net/minecraft/gametest/framework")
    public void testForMissing(ClassNode classNode, String name) throws ClassNotFoundException {
        List<String> missingReason = new ArrayList<>();

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
                    if (this.check(methodInsnNode.owner, methodInsnNode.name, methodInsnNode.desc)) {
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

                    if (this.check(handle.getOwner(), handle.getName(), handle.getDesc())) {
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

    private boolean check(String owner, String name, String desc) throws ClassNotFoundException {
        if (!name.equals("discard") && !name.equals("remove") && !name.equals("setRemoved")) {
            if (!this.checkExtraMethod(owner, name, desc)) {
                return false;
            }
        }

        if (desc.contains("Lorg/bukkit/event/entity/EntityRemoveEvent$Cause;")) {
            return false;
        }

        Class<?> ownerClass = Class.forName(owner.replace('/', '.'), false, this.getClass().getClassLoader());
        if (ownerClass == EntityAccess.class) {
            return false;
        }

        // Found missing discard, remove or setRemoved method call
        return EntityAccess.class.isAssignableFrom(ownerClass);
    }

    private boolean checkExtraMethod(String owner, String name, String desc) {
        if (owner.equals("net/minecraft/world/entity/projectile/EntityShulkerBullet")) {
            return name.equals("destroy");
        }

        return false;
    }
}
