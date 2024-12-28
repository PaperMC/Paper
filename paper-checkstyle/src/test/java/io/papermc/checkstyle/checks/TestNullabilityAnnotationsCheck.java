package io.papermc.checkstyle.checks;

import io.papermc.checkstyle.CheckTest;
import io.papermc.checkstyle.CheckTestBuilder;

class TestNullabilityAnnotationsCheck {

    @CheckTest("testdata.NullabilityAnnotations")
    void test(final CheckTestBuilder builder) {
        builder
            .addViolation("13:11: Missing nullability annotation for method2")
            .addViolation("13:33: Missing nullability annotation for param")
            .addViolation("17:27: Array is missing nullability annotation")
            .addViolation("21:11: Missing nullability annotation for missingTypeAnnotation")
            .addViolation("25:14: Array is missing nullability annotation")
            .addViolation("25:34: Array is missing nullability annotation")
            .addCheck(new NullabilityAnnotationsCheck());
    }
}
