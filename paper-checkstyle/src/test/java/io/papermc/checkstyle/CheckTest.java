package io.papermc.checkstyle;

import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
import com.puppycrawl.tools.checkstyle.PackageObjectFactory;
import com.puppycrawl.tools.checkstyle.TreeWalker;
import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.Violation;
import java.io.File;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith({CheckTest.Parameters.class, CheckTest.RunCheckTest.class})
@Test
public @interface CheckTest {
    Object BUILDER_KEY = new Object();

    @Language("jvm-class-name")
    String value();

    final class Parameters implements ParameterResolver {

        @Override
        public boolean supportsParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext) throws ParameterResolutionException {
            return parameterContext.getParameter().getType() == CheckTestBuilder.class;
        }

        @Override
        public Object resolveParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext) throws ParameterResolutionException {
            final CheckTestBuilder builder = new CheckTestBuilder();
            extensionContext.getStore(ExtensionContext.Namespace.GLOBAL).put(CheckTest.BUILDER_KEY, builder);
            return builder;
        }
    }

    final class RunCheckTest implements AfterTestExecutionCallback {

        @Override
        public void afterTestExecution(final ExtensionContext context) throws Exception {
            final CheckTest checkTest = AnnotationSupport.findAnnotation(context.getTestMethod(), CheckTest.class).orElseThrow();
            final CheckTestBuilder builder = context.getStore(ExtensionContext.Namespace.GLOBAL).get(CheckTest.BUILDER_KEY, CheckTestBuilder.class);
            assertNotNull(builder, "Method must have a CheckTestBuilder parameter");
            assertFalse(builder.checks.isEmpty(), "CheckTestBuilder must have at least one check");
            final TreeWalker walker = new TreeWalker();
            walker.setModuleFactory(new PackageObjectFactory("io.papermc.checkstyle.checks", Util.class.getClassLoader()));
            for (final AbstractCheck check : builder.checks) {
                check.init();
                check.configure(new DefaultConfiguration("Test"));
                MethodUtils.invokeMethod(walker, true, "registerCheck", check);
            }
            final Path filePath = Path.of("src", "testData", "java", checkTest.value().replace('.', File.separatorChar) + ".java");
            assertTrue(Files.exists(filePath), "File not found: " + filePath);
            assertFalse(Files.isDirectory(filePath), "File is a directory: " + filePath);
            final File file = filePath.toFile();
            final Set<Violation> violations = walker.process(file, new FileText(file, StandardCharsets.UTF_8.name()));
            final Set<String> violationSet = new LinkedHashSet<>(violations.stream().map(v -> "%d:%d: %s".formatted(v.getLineNo(), v.getColumnNo(), v.getViolation())).toList());
            final Set<String> expectedSet = new LinkedHashSet<>(builder.expectedViolations);
            final Set<String> extraViolations = new LinkedHashSet<>(violationSet);
            extraViolations.removeAll(expectedSet);
            final Set<String> missingViolations = new LinkedHashSet<>(expectedSet);
            missingViolations.removeAll(violationSet);
            if (!extraViolations.isEmpty() || !missingViolations.isEmpty()) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Violations do not match for ").append(filePath).append("\n");
                if (!extraViolations.isEmpty()) {
                    sb.append("Violations not accounted for in test: \n");
                    extraViolations.forEach(v -> sb.append("  ").append(v).append("\n"));
                }
                if (!missingViolations.isEmpty()) {
                    sb.append("Violations in tests that don't exist: \n");
                    missingViolations.forEach(v -> sb.append("  ").append(v).append("\n"));
                }
                throw new AssertionError(sb.toString());
            }
        }
    }
}
