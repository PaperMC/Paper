package io.papermc.generator.utils;

import com.squareup.javapoet.ClassName;
import io.papermc.typewriter.ClassNamed;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

public record BasePackage(String name) {

    public static final BasePackage PAPER = new BasePackage("io.papermc.paper");
    public static final BasePackage BUKKIT = new BasePackage("org.bukkit");
    public static final BasePackage CRAFT_BUKKIT = new BasePackage("org.bukkit.craftbukkit");
    @ApiStatus.Obsolete
    public static final BasePackage PAPER_LEGACY = new BasePackage("com.destroystokyo.paper");
    @Deprecated
    public static final BasePackage SPIGOT = new BasePackage("org.spigotmc");

    public ClassName rootClass(String simpleName, String... simpleNames) {
        return relativeClass(null, simpleName, simpleNames);
    }

    public ClassName relativeClass(@Nullable String packageName, String simpleName, String... simpleNames) {
        return ClassName.get(packageName == null ? this.name : String.join(".", this.name, packageName), simpleName, simpleNames);
    }

    public ClassNamed rootClassNamed(String simpleName, String... simpleNames) {
        return relativeClassNamed(null, simpleName, simpleNames);
    }

    public ClassNamed relativeClassNamed(@Nullable String packageName, String simpleName, String... simpleNames) {
        return ClassNamed.of(packageName == null ? this.name : String.join(".", this.name, packageName), simpleName, simpleNames);
    }
}
