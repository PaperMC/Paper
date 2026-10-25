package io.papermc.paper.configuration;

import io.papermc.paper.configuration.mapping.LoadEarly;
import io.papermc.paper.configuration.yaml.YamlConfigurationLoader;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import joptsimple.OptionParser;
import org.bukkit.support.environment.Normal;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.spongepowered.configurate.ConfigurationNode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Makes sure the early global config load, which runs before {@code Bootstrap}, never touches anything that requires it.
 * Test environments are always bootstrapped, so the load runs in a separate JVM that never bootstraps.
 * Fails if a {@link LoadEarly} field, or any field initializer of a section containing one, requires the game to be bootstrapped.
 */
@Normal
public class EarlyGlobalConfigPreBootstrapTest {

    private static final String DONE = "early global config loaded";
    private static final String CLASS_INIT_LOG = "[class,init]";
    // Initializing either of these before Bootstrap#bootStrap means registries were accessed
    private static final List<String> BOOTSTRAP_CLASSES = List.of(
        "net/minecraft/server/Bootstrap",
        "net/minecraft/core/registries/BuiltInRegistries"
    );

    @TempDir
    Path dir;

    @Test
    public void earlyLoadDoesNotRequireBootstrap() throws Exception {
        final Path outputFile = this.dir.resolve("output.txt");
        final Process process = new ProcessBuilder(
            Path.of(System.getProperty("java.home"), "bin", "java").toString(),
            "-Xlog:class+init=info:stdout",
            "-cp", System.getProperty("java.class.path"),
            EarlyLoad.class.getName(),
            this.dir.toString()
        )
            .directory(this.dir.toFile())
            .redirectErrorStream(true)
            .redirectOutput(outputFile.toFile())
            .start();
        try {
            assertTrue(process.waitFor(2, TimeUnit.MINUTES), "early load did not finish");
        } finally {
            process.destroyForcibly();
        }

        final List<String> output = Files.readAllLines(outputFile);
        final String programOutput = output.stream().filter(line -> !line.contains(CLASS_INIT_LOG)).collect(Collectors.joining("\n"));
        assertEquals(0, process.exitValue(), () -> "early load failed:\n" + programOutput);
        assertTrue(output.contains(DONE), () -> "early load did not complete:\n" + programOutput);

        String lastConfigClass = "<none>";
        for (final String line : output) {
            final String initialized = initializedClass(line);
            if (initialized == null) {
                continue;
            }
            if (initialized.startsWith("io/papermc/paper/configuration/")) {
                lastConfigClass = initialized;
            } else if (BOOTSTRAP_CLASSES.contains(initialized)) {
                fail("The early global config load initialized " + initialized + ", which requires the game to be bootstrapped. "
                    + "Check the @LoadEarly fields and the field initializers of the sections containing them. "
                    + "Last initialized config class: " + lastConfigClass);
            }
        }
    }

    private static @Nullable String initializedClass(final String line) {
        if (!line.contains(CLASS_INIT_LOG)) {
            return null;
        }
        final int start = line.indexOf("Initializing '");
        if (start == -1) {
            return null;
        }
        final int nameStart = start + "Initializing '".length();
        return line.substring(nameStart, line.indexOf('\'', nameStart));
    }

    // Runs in the separate JVM
    public static final class EarlyLoad {

        public static void main(final String[] args) throws Exception {
            final Path dir = Path.of(args[0]);

            // the defaults create every section containing early fields
            final GlobalConfiguration defaults = PaperConfigurationsInitializer.loadEarlyGlobalConfig(dir.resolve("missing.yml"));

            // write all early fields out, so loading them back goes through every early field's serializer
            final Path configDir = dir.resolve("config");
            Files.createDirectories(configDir);
            final YamlConfigurationLoader loader = ConfigurationLoaders.naturallySorted()
                .defaultOptions(options -> PaperConfigurations.earlyGlobalOptions(options, defaults))
                .path(configDir.resolve(PaperConfigurations.GLOBAL_CONFIG_FILE_NAME))
                .build();
            final ConfigurationNode node = loader.createNode();
            node.set(GlobalConfiguration.class, defaults);
            loader.save(node);

            // old versions go through every versioned transformation that doesn't require the server
            final Path oldConfig = dir.resolve("old.yml");
            Files.writeString(oldConfig, """
                %s: 28
                packet-limiter:
                  overrides:
                    ServerboundChatPacket:
                      max-packet-rate: 1.0
                """.formatted(Configuration.VERSION_FIELD));
            PaperConfigurationsInitializer.loadEarlyGlobalConfig(oldConfig);

            // legacy files that weren't converted yet are read too
            final Path bukkitConfig = dir.resolve("bukkit.yml");
            final Path spigotConfig = dir.resolve("spigot.yml");
            Files.writeString(bukkitConfig, "settings:\n  world-container: worlds\n  update-folder: update\n");
            Files.writeString(spigotConfig, "advancements:\n  disabled:\n  - minecraft:story/root\n");

            final OptionParser parser = new OptionParser();
            parser.accepts("paper-settings-directory").withRequiredArg().ofType(File.class);
            parser.accepts("bukkit-settings").withRequiredArg().ofType(File.class);
            parser.accepts("spigot-settings").withRequiredArg().ofType(File.class);
            PaperConfigurationsInitializer.load(parser.parse(
                "--paper-settings-directory=" + configDir,
                "--bukkit-settings=" + bukkitConfig,
                "--spigot-settings=" + spigotConfig
            ));
            PaperConfigurationsInitializer.earlyGlobalConfig();

            System.out.println(DONE);
        }
    }
}
