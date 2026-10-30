package io.papermc.checkstyle.checks;

import io.papermc.checkstyle.CheckTest;
import io.papermc.checkstyle.CheckTestBuilder;

class TestUnnecessaryFullyQualifiedImportCheck {

    @CheckTest("testdata.imports.UnnecessaryFullyQualifiedImport")
    void test(final CheckTestBuilder builder) {
        builder
            // variable def
            .addViolation("20:15: Fully qualified import used: testdata.imports.testclasses.subpkg.OtherClass")
            // new literal
            .addViolation("20:84: Fully qualified import used: testdata.imports.testclasses.subpkg.OtherClass")
            // variable def
            .addViolation("21:15: Fully qualified import used: testdata.imports.testclasses.subpkg.OtherClass.InnerClass")
            // new literal
            .addViolation("21:95: Fully qualified import used: testdata.imports.testclasses.subpkg.OtherClass.InnerClass")
            // type parm def (extends)
            .addViolation("29:23: Fully qualified import used: testdata.imports.testclasses.subpkg.OtherClass.InnerClass")
            // type argument, type param super
            .addViolation("29:115: Fully qualified import used: testdata.imports.testclasses.subpkg.OtherClass.InnerClass")
            .addViolation("37:23: Fully qualified import used: testdata.imports.testclasses.subpkg.OtherClass.InnerClass")
            .addViolation("37:71: Unnecessary fully qualified import (no conflict): org.jspecify.annotations.Nullable")
            .addViolation("37:143: Fully qualified import used: testdata.imports.testclasses.subpkg.OtherClass.InnerClass")
            .addViolation("46:41: Fully qualified import used: testdata.imports.testclasses.subpkg.OtherClass.InnerInterface")
            .addViolation("50:30: Unnecessary fully qualified import (no conflict): testdata.imports.testclasses.subpkg.OtherInterface")
            .addViolation("54:9: Unnecessary fully qualified import (no conflict): testdata.imports.testclasses.subpkg.OtherInterface")
            .addViolation("54:61: Unnecessary fully qualified import (no conflict): testdata.imports.testclasses.subpkg.OtherInterface")
            .addViolation("55:9: Unnecessary fully qualified import (no conflict): testdata.imports.testclasses.subpkg.OtherInterface")
            // field def
            .addViolation("66:5: Fully qualified import used: testdata.imports.testclasses.subpkg.OtherClass")
            // class literal ".class"
            .addViolation("70:28: Fully qualified import used: testdata.imports.testclasses.subpkg.OtherClass")
            // throws clause, multiple types
            .addViolation("74:39: Unnecessary fully qualified import (no conflict): java.io.IOException")
            .addViolation("74:60: Unnecessary fully qualified import (no conflict): java.io.UncheckedIOException")
            // type param with multiple bounds
            .addViolation("77:36: Unnecessary fully qualified import (no conflict): testdata.imports.testclasses.subpkg.OtherInterface")
            .addViolation("77:89: Unnecessary fully qualified import (no conflict): java.io.Serializable")
            // implements clause, multiple types
            .addViolation("80:57: Unnecessary fully qualified import (no conflict): testdata.imports.testclasses.subpkg.OtherInterface")
            .addViolation("80:109: Unnecessary fully qualified import (no conflict): java.io.Serializable")
            // static field access
            .addViolation("84:36: Unnecessary fully qualified import (no conflict): java.util.Locale")
            .addViolation("85:35: Fully qualified import used: testdata.imports.testclasses.subpkg.OtherClass")
            // method call through a field
            .addViolation("86:9: Unnecessary fully qualified import (no conflict): java.lang.System")
            // method call on a constant, reported without the constant
            .addViolation("87:29: Unnecessary fully qualified import (no conflict): java.util.Locale")
            // method and constructor references
            .addViolation("88:36: Unnecessary fully qualified import (no conflict): java.lang.Thread")
            .addViolation("89:41: Unnecessary fully qualified import (no conflict): java.util.ArrayList")
            // no violation for holder.inner.VALUE (91), java.util.Random conflicting with a declared type (103),
            // or testdata.imports.testclasses.SamePackage conflicting with an unqualified use (108)
            // reference to the declared type itself
            .addViolation("104:5: Unnecessary fully qualified import (no conflict): testdata.imports.UnnecessaryFullyQualifiedImport")
            // same package type, no conflict
            .addViolation("109:5: Unnecessary fully qualified import (no conflict): testdata.imports.SamePackage")
            // no violation for testdata.imports.Shadowed (115), the nested Shadowed hides the same package type
            .addCheck(new UnnecessaryFullyQualifiedImportCheck());
    }
}
