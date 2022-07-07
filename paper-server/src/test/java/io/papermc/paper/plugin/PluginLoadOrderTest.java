package io.papermc.paper.plugin;

import com.google.common.graph.GraphBuilder;
import io.papermc.paper.plugin.entrypoint.dependency.SimpleMetaDependencyTree;
import io.papermc.paper.plugin.provider.entrypoint.DependencyContext;
import io.papermc.paper.plugin.entrypoint.strategy.modern.ModernPluginLoadingStrategy;
import io.papermc.paper.plugin.entrypoint.strategy.ProviderConfiguration;
import io.papermc.paper.plugin.provider.PluginProvider;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Normal
public class PluginLoadOrderTest {

    private static List<PluginProvider<PaperTestPlugin>> REGISTERED_PROVIDERS = new ArrayList<>();
    private static Map<String, Integer> LOAD_ORDER = new HashMap<>();
    private static final String[] EMPTY = {};

    static {
        setup();
    }

    private static TestJavaPluginProvider setup(String identifier, String[] loadAfter, String[] loadAfterSoft, String[] before) {
        TestPluginMeta configuration = new TestPluginMeta(identifier);
        configuration.setHardDependencies(List.of(loadAfter));
        configuration.setSoftDependencies(List.of(loadAfterSoft));
        configuration.setLoadBefore(List.of(before));

        TestJavaPluginProvider provider = new TestJavaPluginProvider(configuration);
        REGISTERED_PROVIDERS.add(provider);
        return provider;
    }

    /**
     * Obfuscated plugin names, this uses a real dependency tree...
     */
    private static void setup() {
        setup("RedAir", EMPTY, new String[]{"NightShovel", "EmeraldFire"}, new String[]{"GreenShovel", "IronSpork", "BrightBlueShovel", "WireDoor"});
        setup("BigGrass", EMPTY, new String[]{"IronEarth", "RedAir"}, new String[]{"BlueFire"});
        setup("BlueFire", EMPTY, EMPTY, EMPTY);
        setup("BigPaper", EMPTY, new String[]{"BlueFire"}, EMPTY);
        setup("EmeraldSpork", EMPTY, EMPTY, new String[]{"GoldPaper", "YellowSnow"});
        setup("GreenShovel", EMPTY, EMPTY, EMPTY);
        setup("BrightBlueGrass", new String[]{"BigPaper"}, new String[]{"DarkSpork"}, EMPTY);
        setup("GoldPaper", EMPTY, new String[]{"BlueFire"}, EMPTY);
        setup("GreenGlass", EMPTY, EMPTY, EMPTY);
        setup("GoldNeptune", EMPTY, new String[]{"GreenShovel", "GoldNeptuneVersioning"}, EMPTY);
        setup("RedPaper", EMPTY, new String[]{"GoldPaper", "GoldFire", "EmeraldGrass", "BlueFire", "CopperSpork", "YellowDoor", "OrangeClam", "BlueSponge", "GoldNeptune", "BrightBlueGrass", "DarkSpoon", "BigShovel", "GreenGlass", "IronGlass"}, new String[]{"IronPaper", "YellowFire"});
        setup("YellowGrass", EMPTY, new String[]{"RedAir"}, EMPTY);
        setup("WireFire", EMPTY, new String[]{"RedPaper", "WireGrass", "YellowSpork", "NightAir"}, EMPTY);
        setup("OrangeNeptune", EMPTY, EMPTY, EMPTY);
        setup("BigSpoon", new String[]{"YellowGrass", "GreenShovel"}, new String[]{"RedAir", "GoldNeptune", "BrightBlueGrass", "LightDoor", "LightSpork", "LightEarth", "NightDoor", "OrangeSpoon", "GoldSponge", "GoldDoor", "DarkPaper", "RedPaper", "GreenGlass", "IronGlass", "NightGlass", "BigGrass", "BlueFire", "YellowSpoon", "DiamondGrass", "DiamondShovel", "DarkSnow", "EmeraldGlass", "EmeraldSpoon", "LightFire", "WireGrass", "RedEarth", "WireFire"}, EMPTY);
        setup("CopperSnow", EMPTY, new String[]{"RedSnow", "OrangeFire", "WireAir", "GreenGlass", "NightSpork", "EmeraldPaper"}, new String[]{"BlueGrass"});
        setup("BrightBluePaper", EMPTY, new String[]{"GoldEarth", "BrightBlueSpoon", "CopperGlass", "LightSporkChat", "DarkAir", "LightEarth", "DiamondDoor", "YellowShovel", "BlueAir", "DarkShovel", "GoldPaper", "BlueFire", "GreenGlass", "YellowSpork", "BigGrass", "OrangePaper", "DarkPaper"}, new String[]{"WireShovel"});
        setup("LightSponge", EMPTY, EMPTY, EMPTY);
        setup("OrangeShovel", EMPTY, EMPTY, EMPTY);
        setup("GoldGrass", EMPTY, new String[]{"GreenGlass", "BlueFire"}, EMPTY);
        setup("IronSponge", EMPTY, new String[]{"DiamondEarth"}, EMPTY);
        setup("EmeraldSnow", EMPTY, EMPTY, EMPTY);
        setup("BlueSpoon", new String[]{"BigGrass"}, new String[]{"GreenGlass", "GoldPaper", "GreenShovel", "YellowClam"}, EMPTY);
        setup("BigSpork", EMPTY, new String[]{"BigPaper"}, EMPTY);
        setup("BluePaper", EMPTY, new String[]{"BigClam", "RedSpoon", "GreenFire", "WireSnow", "OrangeSnow", "BlueFire", "BrightBlueGrass", "YellowSpork", "GreenGlass"}, EMPTY);
        setup("OrangeSpork", EMPTY, EMPTY, EMPTY);
        setup("DiamondNeptune", EMPTY, new String[]{"GreenGlass", "GreenShovel", "YellowNeptune"}, EMPTY);
        setup("BigFire", EMPTY, new String[]{"BlueFire", "BrightBlueDoor", "GreenGlass"}, EMPTY);
        setup("NightNeptune", EMPTY, new String[]{"BlueFire", "DarkGlass", "GoldPaper", "YellowNeptune", "BlueShovel"}, EMPTY);
        setup("YellowEarth", new String[]{"RedAir"}, EMPTY, EMPTY);
        setup("DiamondClam", EMPTY, EMPTY, EMPTY);
        setup("CopperAir", EMPTY, new String[]{"BigPaper"}, EMPTY);
        setup("NightSpoon", new String[]{"OrangeNeptune"}, new String[]{"BlueFire", "GreenGlass", "RedSpork", "GoldPaper", "BigShovel", "YellowSponge", "EmeraldSpork"}, EMPTY);
        setup("GreenClam", EMPTY, new String[]{"GreenShovel", "BrightBlueEarth", "BigSpoon", "RedPaper", "BlueFire", "GreenGlass", "WireFire", "GreenSnow"}, EMPTY);
        setup("YellowPaper", EMPTY, EMPTY, EMPTY);
        setup("WireGlass", new String[]{"YellowGrass"}, new String[]{"YellowGlass", "BigSpoon", "CopperSnow", "GreenGlass", "BlueEarth"}, EMPTY);
        setup("BlueSpork", EMPTY, new String[]{"BrightBlueGrass"}, EMPTY);
        setup("CopperShovel", EMPTY, new String[]{"GreenGlass"}, EMPTY);
        setup("RedClam", EMPTY, EMPTY, EMPTY);
        setup("EmeraldClam", EMPTY, new String[]{"BlueFire"}, EMPTY);
        setup("DarkClam", EMPTY, new String[]{"GoldAir", "LightGlass"}, EMPTY);
        setup("WireSpoon", EMPTY, new String[]{"GoldPaper", "LightSnow"}, EMPTY);
        setup("CopperNeptune", EMPTY, new String[]{"GreenGlass", "BigGrass"}, EMPTY);
        setup("RedNeptune", EMPTY, EMPTY, EMPTY);
        setup("GreenAir", EMPTY, EMPTY, EMPTY);
        setup("RedFire", new String[]{"BrightBlueGrass", "BigPaper"}, new String[]{"BlueFire", "GreenGlass", "BigGrass"}, EMPTY);
    }

    @BeforeEach
    public void loadProviders() {
        AtomicInteger currentLoad = new AtomicInteger();
        ModernPluginLoadingStrategy<PaperTestPlugin> modernPluginLoadingStrategy = new ModernPluginLoadingStrategy<>(new ProviderConfiguration<>() {
            @Override
            public void applyContext(PluginProvider<PaperTestPlugin> provider, DependencyContext dependencyContext) {
            }

            @Override
            public boolean load(PluginProvider<PaperTestPlugin> provider, PaperTestPlugin provided) {
                LOAD_ORDER.put(provider.getMeta().getName(), currentLoad.getAndIncrement());
                return false;
            }

        });

        modernPluginLoadingStrategy.loadProviders(REGISTERED_PROVIDERS, new SimpleMetaDependencyTree(GraphBuilder.directed().build()));
    }

    @Test
    public void testDependencies() {
        for (PluginProvider<PaperTestPlugin> provider : REGISTERED_PROVIDERS) {
            TestPluginMeta pluginMeta = (TestPluginMeta) provider.getMeta();
            String identifier = pluginMeta.getName();
            Assertions.assertTrue(LOAD_ORDER.containsKey(identifier), "Provider wasn't loaded! (%s)".formatted(identifier));

            int index = LOAD_ORDER.get(identifier);

            // Hard dependencies should be loaded BEFORE
            for (String hardDependency : pluginMeta.getPluginDependencies()) {
                Assertions.assertTrue(LOAD_ORDER.containsKey(hardDependency), "Plugin (%s) is missing hard dependency (%s)".formatted(identifier, hardDependency));

                int dependencyIndex = LOAD_ORDER.get(hardDependency);
                Assertions.assertTrue(index > dependencyIndex, "Plugin (%s) was not loaded BEFORE soft dependency. (%s)".formatted(identifier, hardDependency));
            }

            for (String softDependency : pluginMeta.getPluginSoftDependencies()) {
                if (!LOAD_ORDER.containsKey(softDependency)) {
                    continue;
                }

                int dependencyIndex = LOAD_ORDER.get(softDependency);

                Assertions.assertTrue(index > dependencyIndex, "Plugin (%s) was not loaded BEFORE soft dependency. (%s)".formatted(identifier, softDependency));
            }

            for (String loadBefore : pluginMeta.getLoadBeforePlugins()) {
                if (!LOAD_ORDER.containsKey(loadBefore)) {
                    continue;
                }

                int dependencyIndex = LOAD_ORDER.get(loadBefore);
                Assertions.assertTrue(index < dependencyIndex, "Plugin (%s) was NOT loaded BEFORE loadbefore dependency. (%s)".formatted(identifier, loadBefore));
            }
        }
    }
}
