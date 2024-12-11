package org.bukkit.craftbukkit.util;

import com.google.common.base.Predicates;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.io.ByteStreams;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import org.bukkit.Material;
import org.bukkit.craftbukkit.legacy.FieldRename;
import org.bukkit.craftbukkit.legacy.MaterialRerouting;
import org.bukkit.craftbukkit.legacy.MethodRerouting;
import org.bukkit.craftbukkit.legacy.enums.EnumEvil;
import org.bukkit.craftbukkit.legacy.reroute.Reroute;
import org.bukkit.craftbukkit.legacy.reroute.RerouteArgument;
import org.bukkit.craftbukkit.legacy.reroute.RerouteBuilder;
import org.bukkit.craftbukkit.legacy.reroute.RerouteMethodData;
import org.bukkit.plugin.AuthorNagException;
import org.jetbrains.annotations.VisibleForTesting;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.RecordComponentVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.TypePath;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.SimpleRemapper;

public class Commodore {
    private static final String BUKKIT_GENERATED_METHOD_PREFIX = "BUKKIT_CUSTOM_METHOD_";

    private static final Set<String> EVIL = new HashSet<>(Arrays.asList(
            "org/bukkit/World (III)I getBlockTypeIdAt",
            "org/bukkit/World (Lorg/bukkit/Location;)I getBlockTypeIdAt",
            "org/bukkit/block/Block ()I getTypeId",
            "org/bukkit/block/Block (I)Z setTypeId",
            "org/bukkit/block/Block (IZ)Z setTypeId",
            "org/bukkit/block/Block (IBZ)Z setTypeIdAndData",
            "org/bukkit/block/Block (B)V setData",
            "org/bukkit/block/Block (BZ)V setData",
            "org/bukkit/inventory/ItemStack ()I getTypeId",
            "org/bukkit/inventory/ItemStack (I)V setTypeId",
            "org/bukkit/inventory/ItemStack (S)V setDurability"
    ));

    private static final Map<String, String> ENUM_RENAMES = Map.of(
            "java/lang/Enum", "java/lang/Object",
            "java/util/EnumSet", "org/bukkit/craftbukkit/legacy/enums/ImposterEnumSet",
            "java/util/EnumMap", "org/bukkit/craftbukkit/legacy/enums/ImposterEnumMap"
    );

    private static final Map<String, String> RENAMES = Map.of(
            "org/bukkit/entity/TextDisplay$TextAligment", "org/bukkit/entity/TextDisplay$TextAlignment", // SPIGOT-7335
            "org/spigotmc/event/entity/EntityMountEvent", "org/bukkit/event/entity/EntityMountEvent",
            "org/spigotmc/event/entity/EntityDismountEvent", "org/bukkit/event/entity/EntityDismountEvent"
    );

    private static final Map<String, String> CLASS_TO_INTERFACE = Map.ofEntries(
            Map.entry("org/bukkit/inventory/InventoryView", "org/bukkit/craftbukkit/inventory/CraftAbstractInventoryView"),
            Map.entry("org/bukkit/entity/Villager$Type", "NOP"),
            Map.entry("org/bukkit/entity/Villager$Profession", "NOP"),
            Map.entry("org/bukkit/entity/Frog$Variant", "NOP"),
            Map.entry("org/bukkit/entity/Cat$Type", "NOP"),
            Map.entry("org/bukkit/map/MapCursor$Type", "NOP"),
            Map.entry("org/bukkit/block/banner/PatternType", "NOP"),
            Map.entry("org/bukkit/Art", "NOP"),
            Map.entry("org/bukkit/attribute/Attribute", "NOP"),
            Map.entry("org/bukkit/block/Biome", "NOP"),
            Map.entry("org/bukkit/Fluid", "NOP"),
            Map.entry("org/bukkit/Sound", "NOP")
    );

    private final List<Reroute> reroutes = new ArrayList<>(); // only for testing
    private Reroute materialReroute;
    private Reroute reroute;

    public Commodore() {
    }

    public Commodore(Predicate<String> compatibilityPresent) {
        this.updateReroute(compatibilityPresent);
    }

    public void updateReroute(Predicate<String> compatibilityPresent) {
        this.materialReroute = RerouteBuilder
                .create(compatibilityPresent)
                .forClass(MaterialRerouting.class)
                .build();
        this.reroute = RerouteBuilder
                .create(compatibilityPresent)
                .forClass(FieldRename.class)
                .forClass(MethodRerouting.class)
                .forClass(EnumEvil.class)
                .build();

        this.reroutes.clear();
        this.reroutes.add(this.materialReroute);
        this.reroutes.add(this.reroute);
    }

    @VisibleForTesting
    public List<Reroute> getReroutes() {
        return this.reroutes;
    }

    public static void main(String[] args) {
        OptionParser parser = new OptionParser();
        OptionSpec<File> inputFlag = parser.acceptsAll(Arrays.asList("i", "input")).withRequiredArg().ofType(File.class).required();
        OptionSpec<File> outputFlag = parser.acceptsAll(Arrays.asList("o", "output")).withRequiredArg().ofType(File.class).required();

        OptionSet options = parser.parse(args);

        File input = options.valueOf(inputFlag);
        File output = options.valueOf(outputFlag);

        Commodore commodore = new Commodore(Predicates.alwaysTrue());

        if (input.isDirectory()) {
            if (!output.isDirectory()) {
                System.err.println("If input directory specified, output directory required too");
                return;
            }

            for (File in : input.listFiles()) {
                if (in.getName().endsWith(".jar")) {
                    Commodore.convert(in, new File(output, in.getName()), commodore);
                }
            }
        } else {
            Commodore.convert(input, output, commodore);
        }
    }

    private static void convert(File in, File out, Commodore commodore) {
        System.out.println("Attempting to convert " + in + " to " + out);

        try {
            try (JarFile inJar = new JarFile(in, false)) {
                JarEntry entry = inJar.getJarEntry(".commodore");
                if (entry != null) {
                    return;
                }

                try (JarOutputStream outJar = new JarOutputStream(new FileOutputStream(out))) {
                    for (Enumeration<JarEntry> entries = inJar.entries(); entries.hasMoreElements();) {
                        entry = entries.nextElement();

                        try (InputStream is = inJar.getInputStream(entry)) {
                            byte[] b = ByteStreams.toByteArray(is);

                            if (entry.getName().endsWith(".class")) {
                                b = commodore.convert(b, "dummy", ApiVersion.NONE, Collections.emptySet());
                                entry = new JarEntry(entry.getName());
                            }

                            outJar.putNextEntry(entry);
                            outJar.write(b);
                        }
                    }

                    outJar.putNextEntry(new ZipEntry(".commodore"));
                }
            }
        } catch (Exception ex) {
            System.err.println("Fatal error trying to convert " + in);
            ex.printStackTrace();
        }
    }

    public byte[] convert(byte[] b, final String pluginName, final ApiVersion pluginVersion, final Set<String> activeCompatibilities) {
        final boolean modern = pluginVersion.isNewerThanOrSameAs(ApiVersion.FLATTENING);
        final boolean enumCompatibility = pluginVersion.isOlderThanOrSameAs(ApiVersion.getOrCreateVersion("1.20.6")) && activeCompatibilities.contains("enum-compatibility-mode");
        ClassReader cr = new ClassReader(b);
        ClassWriter cw = new ClassWriter(cr, 0);

        List<String> methodEnumSignatures = Commodore.getMethodSignatures(b);
        Multimap<String, String> enumLessToEnum = HashMultimap.create();
        for (String method : methodEnumSignatures) {
            enumLessToEnum.put(method.replace("Ljava/lang/Enum;", "Ljava/lang/Object;"), method);
        }

        ClassVisitor visitor = cw;
        if (enumCompatibility) {
            visitor = new LimitedClassRemapper(cw, new SimpleRemapper(Commodore.ENUM_RENAMES));
        }

        cr.accept(new ClassRemapper(new ClassVisitor(Opcodes.ASM9, visitor) {
            final Set<RerouteMethodData> rerouteMethodData = new HashSet<>();
            String className;
            boolean isInterface;

            @Override
            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                this.className = name;
                this.isInterface = (access & Opcodes.ACC_INTERFACE) != 0;
                String craftbukkitClass = Commodore.CLASS_TO_INTERFACE.get(superName);
                if (craftbukkitClass != null) {
                    superName = craftbukkitClass;
                }
                super.visit(version, access, name, signature, superName, interfaces);
            }

            @Override
            public void visitEnd() {
                for (RerouteMethodData rerouteMethodData : this.rerouteMethodData) {
                    MethodVisitor methodVisitor = super.visitMethod(Opcodes.ACC_STATIC | Opcodes.ACC_SYNTHETIC | Opcodes.ACC_PUBLIC, Commodore.buildMethodName(rerouteMethodData), Commodore.buildMethodDesc(rerouteMethodData), null, null);
                    methodVisitor.visitCode();
                    int index = 0;
                    int extraSize = 0;
                    for (RerouteArgument argument : rerouteMethodData.arguments()) {
                        if (argument.injectPluginName()) {
                            methodVisitor.visitLdcInsn(pluginName);
                        } else if (argument.injectPluginVersion()) {
                            methodVisitor.visitLdcInsn(pluginVersion.getVersionString());
                            methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getInternalName(ApiVersion.class), "getOrCreateVersion", "(Ljava/lang/String;)L" + Type.getInternalName(ApiVersion.class) + ";", false);
                        } else if (argument.injectCompatibility() != null) {
                            methodVisitor.visitLdcInsn(activeCompatibilities.contains(argument.injectCompatibility()));
                        } else {
                            methodVisitor.visitVarInsn(argument.instruction(), index);
                            index++;

                            // Long and double need two space
                            // https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-4.html#jvms-4.7.3
                            // https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-2.html#jvms-2.6.1
                            // https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-2.html#jvms-2.6.2
                            extraSize += argument.type().getSize() - 1;
                        }
                    }

                    methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, rerouteMethodData.targetOwner(), rerouteMethodData.targetName(), rerouteMethodData.targetType().getDescriptor(), false);
                    methodVisitor.visitInsn(rerouteMethodData.rerouteReturn().instruction());
                    methodVisitor.visitMaxs(rerouteMethodData.arguments().size() + extraSize, index + extraSize);
                    methodVisitor.visitEnd();
                }

                super.visitEnd();
            }

            @Override
            public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
                return Commodore.createAnnotationVisitor(pluginVersion, this.api, super.visitAnnotation(descriptor, visible));
            }

            @Override
            public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
                return Commodore.createAnnotationVisitor(pluginVersion, this.api, super.visitTypeAnnotation(typeRef, typePath, descriptor, visible));
            }

            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                if (enumCompatibility && (access & Opcodes.ACC_SYNTHETIC) != 0 && (access & Opcodes.ACC_BRIDGE) != 0 && desc.contains("Ljava/lang/Object;")) {
                    // SPIGOT-7820: Do not use object method if enum method is present
                    // The object method does only redirect to the enum method
                    Collection<String> result = enumLessToEnum.get(desc.replace("Ljava/lang/Enum;", "Ljava/lang/Object;") + " " + name);
                    if (result.size() == 2) {
                        name = name + "_BUKKIT_UNUSED";
                    }
                }

                return new MethodVisitor(this.api, super.visitMethod(access, name, desc, signature, exceptions)) {

                    @Override
                    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
                        name = FieldRename.rename(pluginVersion, owner, name);

                        if (modern) {
                            if (owner.equals("org/bukkit/Material")) {
                                switch (name) {
                                    case "CACTUS_GREEN":
                                        name = "GREEN_DYE";
                                        break;
                                    case "DANDELION_YELLOW":
                                        name = "YELLOW_DYE";
                                        break;
                                    case "ROSE_RED":
                                        name = "RED_DYE";
                                        break;
                                    case "SIGN":
                                        name = "OAK_SIGN";
                                        break;
                                    case "WALL_SIGN":
                                        name = "OAK_WALL_SIGN";
                                        break;
                                    case "ZOMBIE_PIGMAN_SPAWN_EGG":
                                        name = "ZOMBIFIED_PIGLIN_SPAWN_EGG";
                                        break;
                                    case "GRASS_PATH":
                                        name = "DIRT_PATH";
                                        break;
                                    case "GRASS":
                                        name = "SHORT_GRASS";
                                        break;
                                    case "SCUTE":
                                        name = "TURTLE_SCUTE";
                                        break;
                                }
                            }

                            super.visitFieldInsn(opcode, owner, name, desc);
                            return;
                        }

                        if (owner.equals("org/bukkit/Material")) {
                            try {
                                Material.valueOf("LEGACY_" + name);
                            } catch (IllegalArgumentException ex) {
                                throw new AuthorNagException("No legacy enum constant for " + name + ". Did you forget to define a modern (1.13+) api-version in your plugin.yml?");
                            }

                            super.visitFieldInsn(opcode, owner, "LEGACY_" + name, desc);
                            return;
                        }

                        if (owner.equals("org/bukkit/Art")) {
                            switch (name) {
                                case "BURNINGSKULL":
                                    super.visitFieldInsn(opcode, owner, "BURNING_SKULL", desc);
                                    return;
                                case "DONKEYKONG":
                                    super.visitFieldInsn(opcode, owner, "DONKEY_KONG", desc);
                                    return;
                            }
                        }

                        if (owner.equals("org/bukkit/DyeColor")) {
                            switch (name) {
                                case "SILVER":
                                    super.visitFieldInsn(opcode, owner, "LIGHT_GRAY", desc);
                                    return;
                            }
                        }

                        super.visitFieldInsn(opcode, owner, name, desc);
                    }

                    private void handleMethod(MethodPrinter visitor, int opcode, String owner, String name, String desc, boolean itf, Type samMethodType, Type instantiatedMethodType) {
                        if (this.checkReroute(visitor, Commodore.this.reroute, opcode, owner, name, desc, samMethodType, instantiatedMethodType)) {
                            return;
                        }

                        String craftbukkitClass = Commodore.CLASS_TO_INTERFACE.get(owner);
                        if (craftbukkitClass != null) {
                            if (opcode == Opcodes.INVOKESPECIAL || opcode == Opcodes.H_INVOKESPECIAL) {
                                owner = craftbukkitClass;
                            } else {
                                if (opcode == Opcodes.INVOKEVIRTUAL) {
                                    opcode = Opcodes.INVOKEINTERFACE;
                                }

                                if (opcode == Opcodes.H_INVOKEVIRTUAL) {
                                    opcode = Opcodes.H_INVOKEINTERFACE;
                                }

                                itf = true;
                            }
                        }

                        // SPIGOT-4496
                        if (owner.equals("org/bukkit/map/MapView") && name.equals("getId") && desc.equals("()S")) {
                            // Should be same size on stack so just call other method
                            visitor.visit(opcode, owner, name, "()I", itf, samMethodType, Type.getMethodType("(Lorg/bukkit/map/MapView;)Ljava/lang/Integer;"));
                            return;
                        }
                        // SPIGOT-4608
                        if ((owner.equals("org/bukkit/Bukkit") || owner.equals("org/bukkit/Server")) && name.equals("getMap") && desc.equals("(S)Lorg/bukkit/map/MapView;")) {
                            // Should be same size on stack so just call other method
                            visitor.visit(opcode, owner, name, "(I)Lorg/bukkit/map/MapView;", itf, samMethodType, instantiatedMethodType);
                            return;
                        }

                        if (owner.startsWith("org/bukkit") && desc.contains("org/bukkit/util/Consumer")) {
                            visitor.visit(opcode, owner, name, desc.replace("org/bukkit/util/Consumer", "java/util/function/Consumer"), itf, samMethodType, instantiatedMethodType);
                            return;
                        }

                        if (modern) {
                            if (owner.equals("org/bukkit/Material") || (instantiatedMethodType != null && instantiatedMethodType.getDescriptor().startsWith("(Lorg/bukkit/Material;)"))) {
                                switch (name) {
                                    case "values":
                                        visitor.visit(opcode, "org/bukkit/craftbukkit/util/CraftLegacy", "modern_" + name, desc, itf, samMethodType, instantiatedMethodType);
                                        return;
                                    case "ordinal":
                                        visitor.visit(Opcodes.INVOKESTATIC, "org/bukkit/craftbukkit/util/CraftLegacy", "modern_" + name, "(Lorg/bukkit/Material;)I", false, samMethodType, instantiatedMethodType);
                                        return;
                                }
                            }

                            visitor.visit(opcode, owner, name, desc, itf, samMethodType, instantiatedMethodType);
                            return;
                        }

                        // Change Particle#getDataType() from BlockData to MaterialData for legacy plugins and particle
                        if (owner.equals("org/bukkit/Particle") && name.equals("getDataType") && desc.equals("()Ljava/lang/Class;")) {
                            visitor.visit(Opcodes.INVOKESTATIC, "org/bukkit/craftbukkit/legacy/CraftEvil", name, "(Lorg/bukkit/Particle;)Ljava/lang/Class;", false, samMethodType, instantiatedMethodType);
                            return;
                        }

                        if (owner.equals("org/bukkit/ChunkSnapshot") && name.equals("getBlockData") && desc.equals("(III)I")) {
                            visitor.visit(opcode, owner, "getData", desc, itf, samMethodType, instantiatedMethodType);
                            return;
                        }

                        Type retType = Type.getReturnType(desc);

                        // TODO 2024-05-22: This can be moved over to use the reroute api
                        if (Commodore.EVIL.contains(owner + " " + desc + " " + name)
                                || (owner.startsWith("org/bukkit/block/") && (desc + " " + name).equals("()I getTypeId"))
                                || (owner.startsWith("org/bukkit/block/") && (desc + " " + name).equals("(I)Z setTypeId"))) {
                            Type[] args = Type.getArgumentTypes(desc);
                            Type[] newArgs = new Type[args.length + 1];
                            newArgs[0] = Type.getObjectType(owner);
                            System.arraycopy(args, 0, newArgs, 1, args.length);

                            visitor.visit(Opcodes.INVOKESTATIC, "org/bukkit/craftbukkit/legacy/CraftEvil", name, Type.getMethodDescriptor(retType, newArgs), false, samMethodType, instantiatedMethodType);
                            return;
                        }

                        if (owner.equals("org/bukkit/DyeColor")) {
                            if (name.equals("valueOf") && desc.equals("(Ljava/lang/String;)Lorg/bukkit/DyeColor;")) {
                                visitor.visit(opcode, owner, "legacyValueOf", desc, itf, samMethodType, instantiatedMethodType);
                                return;
                            }
                        }

                        if (owner.equals("org/bukkit/Material") || (instantiatedMethodType != null && instantiatedMethodType.getDescriptor().startsWith("(Lorg/bukkit/Material;)"))) {
                            if (name.equals("getMaterial") && desc.equals("(I)Lorg/bukkit/Material;")) {
                                visitor.visit(opcode, "org/bukkit/craftbukkit/legacy/CraftEvil", name, desc, itf, samMethodType, instantiatedMethodType);
                                return;
                            }

                            switch (name) {
                                case "values":
                                case "valueOf":
                                case "getMaterial":
                                case "matchMaterial":
                                    visitor.visit(opcode, "org/bukkit/craftbukkit/legacy/CraftLegacy", name, desc, itf, samMethodType, instantiatedMethodType);
                                    return;
                                case "ordinal":
                                    visitor.visit(Opcodes.INVOKESTATIC, "org/bukkit/craftbukkit/legacy/CraftLegacy", "ordinal", "(Lorg/bukkit/Material;)I", false, samMethodType, instantiatedMethodType);
                                    return;
                                case "name":
                                case "toString":
                                    visitor.visit(Opcodes.INVOKESTATIC, "org/bukkit/craftbukkit/legacy/CraftLegacy", name, "(Lorg/bukkit/Material;)Ljava/lang/String;", false, samMethodType, instantiatedMethodType);
                                    return;
                            }
                        }

                        // TODO 2024-05-21: Move this up, when material gets fully replaced with ItemType and BlockType
                        if (owner.startsWith("org/bukkit") && this.checkReroute(visitor, Commodore.this.materialReroute, opcode, owner, name, desc, samMethodType, instantiatedMethodType)) {
                            return;
                        }

                        visitor.visit(opcode, owner, name, desc, itf, samMethodType, instantiatedMethodType);
                    }

                    private boolean checkReroute(MethodPrinter visitor, Reroute reroute, int opcode, String owner, String name, String desc, Type samMethodType, Type instantiatedMethodType) {
                        return Commodore.rerouteMethods(pluginVersion, reroute, opcode == Opcodes.INVOKESTATIC || opcode == Opcodes.H_INVOKESTATIC, owner, name, desc, data -> {
                            visitor.visit(Opcodes.INVOKESTATIC, className, Commodore.buildMethodName(data), Commodore.buildMethodDesc(data), isInterface, samMethodType, instantiatedMethodType);
                            rerouteMethodData.add(data);
                        });
                    }

                    @Override
                    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
                        this.handleMethod((newOpcode, newOwner, newName, newDescription, newItf, newSam, newInstantiated) -> {
                            super.visitMethodInsn(newOpcode, newOwner, newName, newDescription, newItf);
                        }, opcode, owner, name, desc, itf, null, null);
                    }

                    @Override
                    public void visitLdcInsn(Object value) {
                        if (value instanceof String && ((String) value).equals("com.mysql.jdbc.Driver")) {
                            super.visitLdcInsn("com.mysql.cj.jdbc.Driver");
                            return;
                        }

                        super.visitLdcInsn(value);
                    }

                    @Override
                    public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
                        if (bootstrapMethodHandle.getOwner().equals("java/lang/invoke/LambdaMetafactory")
                                && bootstrapMethodHandle.getName().equals("metafactory") && bootstrapMethodArguments.length == 3) {
                            Type samMethodType = (Type) bootstrapMethodArguments[0];
                            Handle implMethod = (Handle) bootstrapMethodArguments[1];
                            Type instantiatedMethodType = (Type) bootstrapMethodArguments[2];

                            this.handleMethod((newOpcode, newOwner, newName, newDescription, newItf, newSam, newInstantiated) -> {
                                if (newOpcode == Opcodes.INVOKESTATIC) {
                                    newOpcode = Opcodes.H_INVOKESTATIC;
                                }

                                List<Object> methodArgs = new ArrayList<>();
                                methodArgs.add(newSam);
                                methodArgs.add(new Handle(newOpcode, newOwner, newName, newDescription, newItf));
                                methodArgs.add(newInstantiated);

                                super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, methodArgs.toArray(Object[]::new));
                            }, implMethod.getTag(), implMethod.getOwner(), implMethod.getName(), implMethod.getDesc(), implMethod.isInterface(), samMethodType, instantiatedMethodType);
                            return;
                        }

                        // TODO: 4/24/23 Handle other factories, other than LambdaMetafactory
                        // for example the String StringConcatFactory, which handles string concatenation
                        // -> System.out.println("Some" + hello);
                        // But as with the todo above, I encourage everyone who is reading this to to give it a shot
                        super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
                    }

                    @Override
                    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
                        return Commodore.createAnnotationVisitor(pluginVersion, this.api, super.visitAnnotation(descriptor, visible));
                    }

                    @Override
                    public AnnotationVisitor visitAnnotationDefault() {
                        return Commodore.createAnnotationVisitor(pluginVersion, this.api, super.visitAnnotationDefault());
                    }

                    @Override
                    public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
                        return Commodore.createAnnotationVisitor(pluginVersion, this.api, super.visitInsnAnnotation(typeRef, typePath, descriptor, visible));
                    }

                    @Override
                    public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String descriptor, boolean visible) {
                        return Commodore.createAnnotationVisitor(pluginVersion, this.api, super.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, descriptor, visible));
                    }

                    @Override
                    public AnnotationVisitor visitParameterAnnotation(int parameter, String descriptor, boolean visible) {
                        return Commodore.createAnnotationVisitor(pluginVersion, this.api, super.visitParameterAnnotation(parameter, descriptor, visible));
                    }

                    @Override
                    public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
                        return Commodore.createAnnotationVisitor(pluginVersion, this.api, super.visitTryCatchAnnotation(typeRef, typePath, descriptor, visible));
                    }

                    @Override
                    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
                        return Commodore.createAnnotationVisitor(pluginVersion, this.api, super.visitTypeAnnotation(typeRef, typePath, descriptor, visible));
                    }
                };
            }

            @Override
            public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
                return new FieldVisitor(this.api, super.visitField(access, name, descriptor, signature, value)) {
                    @Override
                    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
                        return Commodore.createAnnotationVisitor(pluginVersion, this.api, super.visitAnnotation(descriptor, visible));
                    }

                    @Override
                    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
                        return Commodore.createAnnotationVisitor(pluginVersion, this.api, super.visitTypeAnnotation(typeRef, typePath, descriptor, visible));
                    }
                };
            }

            @Override
            public RecordComponentVisitor visitRecordComponent(String name, String descriptor, String signature) {
                return new RecordComponentVisitor(this.api, super.visitRecordComponent(name, descriptor, signature)) {
                    @Override
                    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
                        return Commodore.createAnnotationVisitor(pluginVersion, this.api, super.visitAnnotation(descriptor, visible));
                    }

                    @Override
                    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
                        return Commodore.createAnnotationVisitor(pluginVersion, this.api, super.visitTypeAnnotation(typeRef, typePath, descriptor, visible));
                    }
                };
            }
        }, new SimpleRemapper(Commodore.RENAMES)), 0);

        return cw.toByteArray();
    }

    private static AnnotationVisitor createAnnotationVisitor(ApiVersion apiVersion, int api, AnnotationVisitor delegate) {
        return new AnnotationVisitor(api, delegate) {
            @Override
            public void visitEnum(String name, String descriptor, String value) {
                super.visitEnum(name, descriptor, FieldRename.rename(apiVersion, Type.getType(descriptor).getInternalName(), value));
            }

            @Override
            public AnnotationVisitor visitArray(String name) {
                return Commodore.createAnnotationVisitor(apiVersion, this.api, super.visitArray(name));
            }

            @Override
            public AnnotationVisitor visitAnnotation(String name, String descriptor) {
                return Commodore.createAnnotationVisitor(apiVersion, this.api, super.visitAnnotation(name, descriptor));
            }
        };
    }

    public static boolean rerouteMethods(ApiVersion pluginVersion, Reroute reroute, boolean staticCall, String owner, String name, String desc, Consumer<RerouteMethodData> consumer) {
        return reroute.apply(pluginVersion, owner, name, desc, staticCall, consumer);
    }

    private static List<String> getMethodSignatures(byte[] clazz) {
        List<String> methods = new ArrayList<>();
        ClassReader cr = new ClassReader(clazz);
        cr.accept(new ClassVisitor(Opcodes.ASM9) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                methods.add(descriptor + " " + name);
                return null;
            }
        }, 0);

        return methods;
    }

    private static String buildMethodName(RerouteMethodData rerouteMethodData) {
        return Commodore.BUKKIT_GENERATED_METHOD_PREFIX + rerouteMethodData.targetOwner().replace('/', '_') + "_" + rerouteMethodData.targetName();
    }

    private static String buildMethodDesc(RerouteMethodData rerouteMethodData) {
        return Type.getMethodDescriptor(rerouteMethodData.sourceDesc().getReturnType(), rerouteMethodData.arguments().stream().filter(a -> !a.injectPluginName()).filter(a -> !a.injectPluginVersion()).filter(a -> a.injectCompatibility() == null).map(RerouteArgument::sourceType).toArray(Type[]::new));
    }

    @FunctionalInterface
    private interface MethodPrinter {

        void visit(int opcode, String owner, String name, String description, boolean itf, Type samMethodType, Type instantiatedMethodType);
    }
}
