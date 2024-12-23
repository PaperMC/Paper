package com.destroystokyo.paper.event.executor.asm;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.INVOKEINTERFACE;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.V1_8;

@ApiStatus.Internal
@NullMarked
public final class ASMEventExecutorGenerator {

    private static final String EXECUTE_DESCRIPTOR = "(Lorg/bukkit/event/Listener;Lorg/bukkit/event/Event;)V";

    public static byte[] generateEventExecutor(final Method m, final String name) {
        final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        writer.visit(V1_8, ACC_PUBLIC, name.replace('.', '/'), null, Type.getInternalName(Object.class), new String[]{Type.getInternalName(EventExecutor.class)});
        // Generate constructor
        GeneratorAdapter methodGenerator = new GeneratorAdapter(writer.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null), ACC_PUBLIC, "<init>", "()V");
        methodGenerator.loadThis();
        methodGenerator.visitMethodInsn(INVOKESPECIAL, Type.getInternalName(Object.class), "<init>", "()V", false); // Invoke the super class (Object) constructor
        methodGenerator.returnValue();
        methodGenerator.endMethod();
        // Generate the execute method
        methodGenerator = new GeneratorAdapter(writer.visitMethod(ACC_PUBLIC, "execute", EXECUTE_DESCRIPTOR, null, null), ACC_PUBLIC, "execute", EXECUTE_DESCRIPTOR);
        methodGenerator.loadArg(0);
        methodGenerator.checkCast(Type.getType(m.getDeclaringClass()));
        methodGenerator.loadArg(1);
        methodGenerator.checkCast(Type.getType(m.getParameterTypes()[0]));
        methodGenerator.visitMethodInsn(m.getDeclaringClass().isInterface() ? INVOKEINTERFACE : INVOKEVIRTUAL, Type.getInternalName(m.getDeclaringClass()), m.getName(), Type.getMethodDescriptor(m), m.getDeclaringClass().isInterface());
        // The only purpose of this switch statement is to generate the correct pop instruction, should the event handler method return something other than void.
        // Non-void event handlers will be unsupported in a future release.
        switch (Type.getType(m.getReturnType()).getSize()) {
            // case 0 is omitted because the only type that has size 0 is void - no pop instruction needed.
            case 1 -> methodGenerator.pop(); // handles reference types and most primitives
            case 2 -> methodGenerator.pop2(); // handles long and double
        }
        methodGenerator.returnValue();
        methodGenerator.endMethod();
        writer.visitEnd();
        return writer.toByteArray();
    }

    public static AtomicInteger NEXT_ID = new AtomicInteger(1);

    public static String generateName() {
        final int id = NEXT_ID.getAndIncrement();
        return "com.destroystokyo.paper.event.executor.asm.generated.GeneratedEventExecutor" + id;
    }
}
