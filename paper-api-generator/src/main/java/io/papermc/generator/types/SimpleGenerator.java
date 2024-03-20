package io.papermc.generator.types;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class SimpleGenerator implements SourceGenerator {

    protected final String className;
    protected final String packageName;

    protected SimpleGenerator(String className, String packageName) {
        this.className = className;
        this.packageName = packageName;
    }

    protected abstract TypeSpec getTypeSpec();

    protected abstract JavaFile.Builder file(JavaFile.Builder builder);

    @Override
    public void writeToFile(Path parent) throws IOException {
        Path packagePath = parent.resolve(this.packageName.replace('.', '/'));
        Files.createDirectories(packagePath);

        JavaFile.Builder builder = JavaFile.builder(this.packageName, this.getTypeSpec())
            .indent("    ");
        this.file(builder);

        Files.writeString(packagePath.resolve(this.className + ".java"), this.file(builder).build().toString(), StandardCharsets.UTF_8);
    }

}
