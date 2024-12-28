package testdata;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

public class NullabilityAnnotations {

    public int method(final int param) { // primitive, no annotation
        return 0;
    }

    public Integer method2(final Integer param) {
        return null;
    }

    public @Nullable String[] missingArrayAnnotation() {
        return null;
    }

    public String @Nullable[] missingTypeAnnotation() {
        return null;
    }

    public int[] method3(final int[] param) {
        return null;
    }

    @NullMarked
    public static final class InnerClass {
        // no annotations cause null marked
        public static String method(final String param) {
            return "";
        }
    }

    public void method1(final @NonNull Integer param) {
    }
}
