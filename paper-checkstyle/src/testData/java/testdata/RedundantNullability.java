package testdata;

import org.jspecify.annotations.NullMarked;

public class RedundantNullability {

    @NullMarked
    public static class OuterClass {

        @NullMarked
        public static class RedundantInnerClass {
        }

        @NullMarked
        public interface RedundantInnerInterface {
        }

        @NullMarked
        public record RedundantInnerRecord() {
        }

        @NullMarked
        public enum RedundantInnerEnum {
        }

        public static class ValidInnerClass {
        }
    }

    public static class NonAnnotatedOuter {

        @NullMarked
        public static class ValidAnnotatedInner {
        }
    }
}
