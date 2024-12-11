package io.papermc.paper.plugin;

import io.papermc.paper.plugin.entrypoint.dependency.MetaDependencyTree;
import io.papermc.paper.plugin.entrypoint.dependency.SimpleMetaDependencyTree;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

@Normal
public class PluginDependencyValidationTest {

    private static final TestPluginMeta MAIN;
    private static final TestPluginMeta HARD_DEPENDENCY_1;
    private static final TestPluginMeta SOFT_DEPENDENCY_1;

    public static final String ROOT_NAME = "main";

    public static final String REGISTERED_HARD_DEPEND = "hard1";
    public static final String REGISTERED_SOFT_DEPEND = "soft1";
    public static final String UNREGISTERED_HARD_DEPEND = "hard2";
    public static final String UNREGISTERED_SOFT_DEPEND = "soft2";

    static {
        MAIN = new TestPluginMeta(ROOT_NAME);
        MAIN.setSoftDependencies(List.of(REGISTERED_SOFT_DEPEND, UNREGISTERED_SOFT_DEPEND));
        MAIN.setHardDependencies(List.of(REGISTERED_HARD_DEPEND, UNREGISTERED_HARD_DEPEND));

        HARD_DEPENDENCY_1 = new TestPluginMeta(REGISTERED_HARD_DEPEND);
        SOFT_DEPENDENCY_1 = new TestPluginMeta(REGISTERED_SOFT_DEPEND);
    }

    @Test
    public void testDependencyTree() {
        MetaDependencyTree tree = new SimpleMetaDependencyTree();
        tree.add(MAIN);
        tree.add(HARD_DEPENDENCY_1);
        tree.add(SOFT_DEPENDENCY_1);

        // Test simple transitive dependencies
        assertThat("%s was not a transitive dependency of %s".formatted(ROOT_NAME, REGISTERED_SOFT_DEPEND), tree.isTransitiveDependency(MAIN, SOFT_DEPENDENCY_1));
        assertThat("%s was not a transitive dependency of %s".formatted(ROOT_NAME, REGISTERED_HARD_DEPEND), tree.isTransitiveDependency(MAIN, HARD_DEPENDENCY_1));

        assertThat("%s was a transitive dependency of %s".formatted(REGISTERED_SOFT_DEPEND, ROOT_NAME), !tree.isTransitiveDependency(SOFT_DEPENDENCY_1, MAIN));
        assertThat("%s was a transitive dependency of %s".formatted(REGISTERED_HARD_DEPEND, ROOT_NAME), !tree.isTransitiveDependency(HARD_DEPENDENCY_1, MAIN));

        // Test to ensure that registered dependencies exist
        assertThat("tree did not contain dependency %s".formatted(ROOT_NAME), tree.hasDependency(ROOT_NAME));
        assertThat("tree did not contain dependency %s".formatted(REGISTERED_HARD_DEPEND), tree.hasDependency(REGISTERED_HARD_DEPEND));
        assertThat("tree did not contain dependency %s".formatted(REGISTERED_SOFT_DEPEND), tree.hasDependency(REGISTERED_SOFT_DEPEND));

        // Test to ensure unregistered dependencies don't exist
        assertThat("tree contained dependency %s".formatted(UNREGISTERED_HARD_DEPEND), !tree.hasDependency(UNREGISTERED_HARD_DEPEND));
        assertThat("tree contained dependency %s".formatted(UNREGISTERED_SOFT_DEPEND), !tree.hasDependency(UNREGISTERED_SOFT_DEPEND));

        // Test removal
        tree.remove(HARD_DEPENDENCY_1);
        assertThat("tree contained dependency %s".formatted(REGISTERED_HARD_DEPEND), !tree.hasDependency(REGISTERED_HARD_DEPEND));
    }
}
