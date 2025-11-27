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

        public static final class InnerInnerClass {
        }
    }

    public void method1(final @NonNull Integer param) {
    }

    public void innerClass(final NullabilityAnnotations.InnerClass param) {
    }

    public void innerClassCorrect(final NullabilityAnnotations.@Nullable InnerClass param) {
    }

    public void innerInnerClass(final NullabilityAnnotations.InnerClass.InnerInnerClass param) {
    }

    public void innerInnerClassCorrect(final NullabilityAnnotations.InnerClass.@Nullable InnerInnerClass param) {
    }
}
