package io.papermc.checkstyle.checks;

import io.papermc.checkstyle.CheckTest;
import io.papermc.checkstyle.CheckTestBuilder;

class TestRedundantNullabilityCheck {

    @CheckTest("testdata.RedundantNullability")
    void test(final CheckTestBuilder builder) {
        builder
            .addViolation("10:8: Redundant NullMarked annotation")
            .addViolation("14:8: Redundant NullMarked annotation")
            .addViolation("18:8: Redundant NullMarked annotation")
            .addViolation("22:8: Redundant NullMarked annotation")
            .addCheck(new RedundantNullabilityCheck());
    }
}
