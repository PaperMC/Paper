package io.papermc.generator.types;

import com.squareup.javapoet.ClassName;
import io.papermc.typewriter.ClassNamed;
import java.util.Arrays;
import org.jspecify.annotations.NullMarked;

import static io.papermc.generator.utils.BasePackage.BUKKIT;
import static io.papermc.generator.utils.BasePackage.CRAFT_BUKKIT;
import static io.papermc.generator.utils.BasePackage.PAPER;
import static io.papermc.generator.utils.BasePackage.PAPER_LEGACY;

@NullMarked
public final class Types {

    public static final ClassName NAMESPACED_KEY = BUKKIT.rootClass("NamespacedKey");

    public static final ClassName MINECRAFT_EXPERIMENTAL = BUKKIT.rootClass("MinecraftExperimental");

    public static final ClassName MINECRAFT_EXPERIMENTAL_REQUIRES = BUKKIT.rootClass("MinecraftExperimental", "Requires");

    public static final ClassName VECTOR = BUKKIT.relativeClass("util", "Vector");

    public static final ClassName AXIS = BUKKIT.rootClass("Axis");

    public static final ClassName BLOCK_FACE = BUKKIT.relativeClass("block", "BlockFace");

    public static final ClassName NOTE = BUKKIT.rootClass("Note");

    public static final ClassName BLOCK_DATA_RAIL_SHAPE = BUKKIT.relativeClass("block.data", "Rail", "Shape");

    public static final ClassName KEY = ClassName.get("net.kyori.adventure.key", "Key");

    public static final ClassName REGISTRY_KEY = PAPER.relativeClass("registry", "RegistryKey");

    public static final ClassName TYPED_KEY = PAPER.relativeClass("registry", "TypedKey");

    public static final ClassName GOAL_KEY = PAPER_LEGACY.relativeClass("entity.ai", "GoalKey");

    public static final ClassName GOAL = PAPER_LEGACY.relativeClass("entity.ai", "Goal");

    public static final ClassName MOB = BUKKIT.relativeClass("entity", "Mob");

    public static final ClassName RANGED_ENTITY = PAPER_LEGACY.relativeClass("entity", "RangedEntity");


    public static final ClassName CRAFT_BLOCK_DATA = CRAFT_BUKKIT.relativeClass("block.data", "CraftBlockData");

    public static final ClassName CRAFT_BLOCK = CRAFT_BUKKIT.relativeClass("block", "CraftBlock");

    public static final ClassName TAG_KEY = PAPER.relativeClass("registry.tag", "TagKey");

    public static final ClassName GENERATED_FROM = PAPER.relativeClass("generated", "GeneratedFrom");

    public static ClassName typed(ClassNamed name) {
        if (name.knownClass() != null) {
            return ClassName.get(name.knownClass());
        }

        String[] names = name.dottedNestedName().split("\\.", -1);
        String topName = names[0];
        return ClassName.get(name.packageName(), topName, Arrays.copyOfRange(names, 1, names.length));
    }
}
