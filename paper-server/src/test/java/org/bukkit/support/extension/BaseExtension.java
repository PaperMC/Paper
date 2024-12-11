package org.bukkit.support.extension;

import static org.junit.jupiter.api.Assertions.*;
import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public abstract class BaseExtension implements BeforeAllCallback, BeforeEachCallback {

    private static final Set<String> IGNORE_TAGS = Sets.newHashSet();

    private static String currentlyRunning = null;
    private static boolean run = false;
    protected final Logger logger;
    private final String testSuite;

    public BaseExtension(String testSuite) {
        this.testSuite = testSuite;
        this.logger = Logger.getLogger(testSuite);
    }

    @Override
    public final void beforeAll(ExtensionContext extensionContext) throws Exception {
        if (!this.isTestCase(extensionContext)) {
            return;
        }

        this.checkRunBeforeOnce(extensionContext);
    }

    @Override
    public final void beforeEach(ExtensionContext extensionContext) throws Exception {
        if (!this.isTestCase(extensionContext)) {
            return;
        }

        this.checkRunBeforeOnce(extensionContext);
        this.runBeforeEach(extensionContext);
    }

    private boolean isTestCase(ExtensionContext extensionContext) {
        if (BaseExtension.currentlyRunning != null) {
            return this.testSuite.equals(BaseExtension.currentlyRunning);
        }

        Optional<String> runningTestSuite = extensionContext.getConfigurationParameter("TestSuite");
        if (runningTestSuite.isPresent()) {
            // We are inside a test suite, check if it is the test suite from this extension
            if (!runningTestSuite.get().equals(this.testSuite)) {
                return false;
            }

            BaseExtension.currentlyRunning = this.testSuite;
            this.logger.info("Running tests with environment: " + this.testSuite);
            return true;
        }

        Set<String> tags = new HashSet<>(extensionContext.getTags());
        tags.removeAll(BaseExtension.IGNORE_TAGS);

        if (!tags.contains(this.testSuite)) {
            fail(String.format("""
                    Extension was triggered without the tag for the test suite being present. This should not happen.
                    Did you forget to add @Tag to a new environment annotation? Or maybe a spelling mistake?

                    Expected %s, but found:
                    %s""", this.testSuite, Joiner.on('\n').join(tags)));
            return false; // Will never reach ):
        }

        if (tags.size() >= 2) {
            fail((String.format("""
                    Found more than one tag present in test case %s.
                    Will it is possible to run a test in multiple environments, this is only possible when running the test via a test suite,
                    otherwise it is not possible to distinguish which environment should get loaded.

                    To run a test standalone please temporary comment out the environments which should not be run, so that only one is present.
                    If only one is set and you still see this message make sure that every tag which is not for selecting environments,
                    is added to the org.bukkit.support.extension.BaseExtension#IGNORE_TAGS set.
                    Also note, that class level environments have a higher priority then method level environments.
                    Following multiple tags are present:
                    %s""", extensionContext.getDisplayName(), Joiner.on('\n').join(tags))));
            return false; // Will never reach ):
        }

        BaseExtension.currentlyRunning = this.testSuite;
        this.logger.info("Running tests with environment: " + this.testSuite);
        return true;
    }

    private void checkRunBeforeOnce(ExtensionContext extensionContext) {
        if (BaseExtension.run) {
            return;
        }

        this.init(extensionContext);
        BaseExtension.run = true;
    }

    abstract void init(ExtensionContext extensionContext);

    abstract void runBeforeEach(ExtensionContext extensionContext);
}
