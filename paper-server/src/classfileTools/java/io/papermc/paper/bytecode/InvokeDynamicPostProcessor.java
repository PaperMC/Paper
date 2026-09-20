package io.papermc.paper.bytecode;

import java.io.IOException;
import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassModel;
import java.lang.classfile.ClassTransform;
import java.lang.classfile.CodeBuilder;
import java.lang.classfile.CodeElement;
import java.lang.classfile.Opcode;
import java.lang.classfile.instruction.InvokeDynamicInstruction;
import java.lang.classfile.instruction.InvokeInstruction;
import java.lang.constant.ClassDesc;
import java.lang.constant.ConstantDescs;
import java.lang.constant.DirectMethodHandleDesc;
import java.lang.constant.DynamicCallSiteDesc;
import java.lang.constant.MethodHandleDesc;
import java.lang.constant.MethodTypeDesc;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import io.papermc.paper.event.EventGuard;
import org.bukkit.event.Event;

public final class InvokeDynamicPostProcessor {
    private static final ClassDesc EVENT_BOOTSTRAP_CLASS_DESC = ClassDesc.of("io.papermc.paper.event.EventBootstrap");

    private final ClassFile classFile;

    private InvokeDynamicPostProcessor() {
        this.classFile = ClassFile.of();
    }

    public static void main(final String[] args) throws IOException {
        if (args.length == 0) {
            throw new IllegalArgumentException("Expected at least one argument: <classes-directory>...");
        }

        final InvokeDynamicPostProcessor postProcessor = new InvokeDynamicPostProcessor();
        for (final String arg : args) {
            postProcessor.processTree(Path.of(arg));
        }
    }

    private void processTree(final Path classesDirectory) throws IOException {
        if (!Files.isDirectory(classesDirectory)) {
            throw new IllegalArgumentException("Expected a directory of compiled classes: " + classesDirectory);
        }

        try (Stream<Path> paths = Files.walk(classesDirectory)) {
            for (final Path path : (Iterable<Path>) paths
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".class"))::iterator) {
                this.processClassFile(path);
            }
        }
    }

    private void processClassFile(final Path classFilePath) throws IOException {
        final byte[] originalBytes = Files.readAllBytes(classFilePath);
        final ClassModel classModel = this.classFile.parse(classFilePath);
        final byte[] rewrittenBytes = this.classFile.transformClass(
            classModel,
            ClassTransform.transformingMethodBodies(this::transformCode)
        );

        if (!Arrays.equals(originalBytes, rewrittenBytes)) {
            Files.write(classFilePath, rewrittenBytes);
        }
    }

    private void transformCode(final CodeBuilder builder, final CodeElement element) {
        switch (element) {
            case InvokeInstruction instruction -> {
                final Optional<DynamicCallSiteDesc> rewrite = this.findRewrite(instruction);
                if (rewrite.isPresent()) {
                    builder.invokedynamic(rewrite.get());
                } else {
                    builder.invoke(
                        instruction.opcode(),
                        instruction.owner().asSymbol(),
                        instruction.name().stringValue(),
                        instruction.typeSymbol(),
                        instruction.isInterface()
                    );
                }
            }
            case InvokeDynamicInstruction instruction -> builder.invokedynamic(instruction.invokedynamic().asSymbol());
            default -> builder.with(element);
        }
    }

    private Optional<DynamicCallSiteDesc> findRewrite(final InvokeInstruction instruction) {
        final Optional<DynamicCallSiteDesc> eventCallEventRewrite = this.findEventCallEventRewrite(instruction);
        if (eventCallEventRewrite.isPresent()) {
            // System.out.println("found call event: " + instruction);
            return eventCallEventRewrite;
        }
        final Optional<DynamicCallSiteDesc> hasListenersEventRewrite = this.findHasListenersRewrite(instruction);
        if (hasListenersEventRewrite.isPresent()) {
            System.out.println("found has listeners: " + instruction);
            return hasListenersEventRewrite;
        }
        return Optional.empty();
    }

    private Optional<DynamicCallSiteDesc> findEventCallEventRewrite(final InvokeInstruction instruction) {
        if (instruction.opcode() != Opcode.INVOKEVIRTUAL
            || instruction.isInterface()
            || !instruction.name().stringValue().equals("callEvent")
            || !instruction.typeSymbol().equals(callEventMethodType())) {
            return Optional.empty();
        }

        final Class<?> ownerClass = this.resolveOwnerClass(instruction.owner().asSymbol());
        if (!Event.class.isAssignableFrom(ownerClass)) {
            return Optional.empty();
        }

        final MethodTypeDesc invocationType = MethodTypeDesc.of(
            instruction.typeSymbol().returnType(),
            instruction.owner().asSymbol()
        );
        return Optional.of(DynamicCallSiteDesc.of(
            callEventBootstrap(),
            instruction.name().stringValue(),
            invocationType,
            instruction.owner().asSymbol()
        ));
    }

    private Optional<DynamicCallSiteDesc> findHasListenersRewrite(final InvokeInstruction instruction) {
        if (instruction.opcode() == Opcode.INVOKESTATIC
            && instruction.owner().matches(EventGuard.class.describeConstable().orElseThrow())
            && instruction.name().stringValue().equals("hasListeners")
            && instruction.typeSymbol().equals(hasListenersMethodType())
        ) {
            final MethodTypeDesc invocationType = MethodTypeDesc.of(
                instruction.typeSymbol().returnType(),
                Class.class.describeConstable().orElseThrow()
            );
            return Optional.of(DynamicCallSiteDesc.of(
                hasListenersBootstrap(),
                instruction.name().stringValue(),
                invocationType
            ));
        }
        return Optional.empty();
    }

    private static MethodTypeDesc hasListenersMethodType() {
        return MethodTypeDesc.of(ConstantDescs.CD_boolean, ConstantDescs.CD_Class);
    }

    private static MethodTypeDesc callEventMethodType() {
        return MethodTypeDesc.ofDescriptor("()Z");
    }

    private static DirectMethodHandleDesc callEventBootstrap() {
        return MethodHandleDesc.ofMethod(
            DirectMethodHandleDesc.Kind.STATIC,
            EVENT_BOOTSTRAP_CLASS_DESC,
            "createForCallEvent",
            MethodTypeDesc.ofDescriptor("(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/Class;)Ljava/lang/invoke/CallSite;")
        );
    }

    private static DirectMethodHandleDesc hasListenersBootstrap() {
        return MethodHandleDesc.ofMethod(
            DirectMethodHandleDesc.Kind.STATIC,
            EVENT_BOOTSTRAP_CLASS_DESC,
            "createForHasListeners",
            MethodTypeDesc.ofDescriptor("(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;")
        );
    }

    private Class<?> resolveOwnerClass(final ClassDesc owner) {
        try {
            return owner.resolveConstantDesc(MethodHandles.lookup());
        } catch (final ReflectiveOperationException ex) {
            throw new IllegalStateException("Failed to resolve owner class " + owner, ex);
        }
    }

}
