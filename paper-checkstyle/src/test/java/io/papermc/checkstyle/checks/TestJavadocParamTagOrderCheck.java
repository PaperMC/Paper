package io.papermc.checkstyle.checks;

import io.papermc.checkstyle.CheckTest;
import io.papermc.checkstyle.CheckTestBuilder;

class TestJavadocParamTagOrderCheck {

    @CheckTest("testdata.JavadocParamTagOrder")
    void test(final CheckTestBuilder builder) {
        builder
            .addCheck(new JavadocParamTagOrderCheck())
            // regular parameters swapped
            .addViolation("28:15: @param tag for a should be in declaration order, with type parameters last")
            // type parameter before regular parameter
            .addViolation("37:15: @param tag for a should be in declaration order, with type parameters last")
            // type parameters swapped
            .addViolation("47:15: @param tag for <T> should be in declaration order, with type parameters last")
            // constructor
            .addViolation("66:15: @param tag for a should be in declaration order, with type parameters last")
            // class type parameters
            .addViolation("75:15: @param tag for <A> should be in declaration order, with type parameters last")
            // record component after type parameter
            .addViolation("94:15: @param tag for a should be in declaration order, with type parameters last")
            // record components swapped
            .addViolation("111:15: @param tag for a should be in declaration order, with type parameters last")
            // compact constructor
            .addViolation("119:19: @param tag for a should be in declaration order, with type parameters last");
    }
}
