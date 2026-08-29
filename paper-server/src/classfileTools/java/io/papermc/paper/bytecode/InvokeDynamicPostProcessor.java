package io.papermc.paper.bytecode;

import java.io.IOException;
import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassModel;
import java.lang.classfile.ClassTransform;
import java.lang.classfile.CodeBuilder;
import java.lang.classfile.CodeElement;
import java.lang.classfile.Opcode;
import java.lang.classfile.constantpool.PoolEntry;
import java.lang.classfile.instruction.InvokeDynamicInstruction;
import java.lang.classfile.instruction.InvokeInstruction;
import java.lang.constant.ClassDesc;
import java.lang.constant.ConstantDesc;
import java.lang.constant.ConstantDescs;
import java.lang.constant.DirectMethodHandleDesc;
import java.lang.constant.DynamicCallSiteDesc;
import java.lang.constant.MethodHandleDesc;
import java.lang.constant.MethodTypeDesc;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import io.papermc.paper.event.EventGuard;
import org.bukkit.event.Event;

public final class InvokeDynamicPostProcessor {
    private static final ClassDesc EVENT_BOOTSTRAP_CLASS_DESC = ClassDesc.of("io.papermc.paper.event.EventBootstrap");

    private final ClassFile classFile;
    private final List<MethodCallRewriteRule> rewriteRules;

    private InvokeDynamicPostProcessor(final List<MethodCallRewriteRule> rewriteRules) {
        this.classFile = ClassFile.of();
        this.rewriteRules = List.copyOf(rewriteRules);
    }

    public static void main(final String[] args) throws IOException {
        if (args.length == 0) {
            throw new IllegalArgumentException("Expected at least one argument: <classes-directory>...");
        }

        final InvokeDynamicPostProcessor postProcessor = new InvokeDynamicPostProcessor(defaultRules());
        for (final String arg : args) {
            postProcessor.processTree(Path.of(arg));
        }
    }

    private static List<MethodCallRewriteRule> defaultRules() {
        return List.of();
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
        for (final MethodCallRewriteRule rule : this.rewriteRules) {
            if (rule.matches(instruction)) {
                return Optional.of(rule.rewrite(instruction));
            }
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

    public record MethodCallRewriteRule(MethodCallSelector selector, InvokeDynamicRewritePlan rewritePlan) {

        public MethodCallRewriteRule {
            Objects.requireNonNull(selector, "selector");
            Objects.requireNonNull(rewritePlan, "rewritePlan");
        }

        boolean matches(final InvokeInstruction instruction) {
            return this.selector.matches(instruction);
        }

        DynamicCallSiteDesc rewrite(final InvokeInstruction instruction) {
            return this.rewritePlan.toDynamicCallSiteDesc(instruction);
        }
    }

    public record MethodCallSelector(
        Opcode opcode,
        ClassDesc owner,
        String methodName,
        MethodTypeDesc methodType,
        Boolean interfaceCall
    ) {

        boolean matches(final InvokeInstruction instruction) {
            return (this.opcode == null || instruction.opcode() == this.opcode)
                && (this.owner == null || instruction.owner().asSymbol().equals(this.owner))
                && (this.methodName == null || instruction.name().stringValue().equals(this.methodName))
                && (this.methodType == null || instruction.typeSymbol().equals(this.methodType))
                && (this.interfaceCall == null || instruction.isInterface() == this.interfaceCall);
        }
    }

    public record InvokeDynamicRewritePlan(
        DirectMethodHandleDesc bootstrapMethod,
        String invocationNameOverride,
        MethodTypeDesc invocationTypeOverride,
        List<ConstantDesc> bootstrapArgs
    ) {

        public InvokeDynamicRewritePlan {
            Objects.requireNonNull(bootstrapMethod, "bootstrapMethod");
            bootstrapArgs = List.copyOf(bootstrapArgs);
        }

        DynamicCallSiteDesc toDynamicCallSiteDesc(final InvokeInstruction instruction) {
            final String invocationName = this.invocationNameOverride != null
                ? this.invocationNameOverride
                : instruction.name().stringValue();
            final MethodTypeDesc invocationType = this.invocationTypeOverride != null
                ? this.invocationTypeOverride
                : instruction.typeSymbol();

            return DynamicCallSiteDesc.of(
                this.bootstrapMethod,
                invocationName,
                invocationType,
                this.bootstrapArgs.toArray(ConstantDesc[]::new)
            );
        }
    }
}
