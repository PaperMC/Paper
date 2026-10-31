// @formatter:off
package testdata.imports;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import testdata.imports.testclasses.subpkg.OtherClass;
import java.lang.*;

@SuppressWarnings("ALL")
public class UnnecessaryFullyQualifiedImport {

    static {
        final Map<String, String> map = new HashMap<>();
        final Set<Map.Entry<String, String>> entries = map.entrySet();
        final OtherClass otherClass = new OtherClass();
        final OtherClass.InnerClass innerClass = new OtherClass.InnerClass();
        final testdata.imports.testclasses.OtherClass otherClass1 = new testdata.imports.testclasses.OtherClass();
        final testdata.imports.testclasses.OtherClass.InnerClass innerClass1 = new testdata.imports.testclasses.OtherClass.InnerClass();
        final testdata.imports.testclasses.subpkg.OtherClass otherClass1_bad = new testdata.imports.testclasses.subpkg.OtherClass();
        final testdata.imports.testclasses.subpkg.OtherClass.InnerClass innerClass1_bad = new testdata.imports.testclasses.subpkg.OtherClass.InnerClass();

        final Map<testdata.imports.testclasses.OtherClass.InnerClass, OtherClass.InnerClass> map1 = new HashMap<>();
        final Map<testdata.imports.testclasses.OtherClass.InnerClass, OtherClass> map2 = new HashMap<>();
        final Map<? extends testdata.imports.testclasses.OtherClass.InnerClass, ? extends OtherClass.InnerClass> map3 = new HashMap<>();
        final Map<? super testdata.imports.testclasses.OtherClass.InnerClass, ? extends OtherClass> map4 = new HashMap<>();
    }

    public <A extends testdata.imports.testclasses.subpkg.OtherClass.InnerClass> A m(final Map<? super A, ? super testdata.imports.testclasses.subpkg.OtherClass.InnerClass> m) {
        return null;
    }

    public <A extends OtherClass.InnerClass> A m_correct(final Map<? super A, ? super OtherClass.InnerClass> m) {
        return null;
    }

    public <A extends testdata.imports.testclasses.subpkg.OtherClass.@org.jspecify.annotations.Nullable InnerClass> A m2(final Map<? super A, testdata.imports.testclasses.subpkg.OtherClass.InnerClass> m) {
        return null;
    }

    public <A extends OtherClass.InnerClass> A m2_correct(final Map<? super A, OtherClass.InnerClass> m) {
        return null;
    }

    public Object o() {
        return (OtherClass.InnerClass & testdata.imports.testclasses.subpkg.OtherClass.InnerInterface) null;
    }

    public Object o2() {
        return (OtherClass & testdata.imports.testclasses.subpkg.OtherInterface) null;
    }

    public void methods() {
        testdata.imports.testclasses.subpkg.OtherInterface.<testdata.imports.testclasses.subpkg.OtherInterface>test();
        testdata.imports.testclasses.subpkg.OtherInterface.test();
    }

    public void methods_correct() {
        OtherClass.<OtherClass>test();
        new OtherClass().instance_test();

        OtherClass oc = new OtherClass();
        oc.instance_test();
    }

    testdata.imports.testclasses.subpkg.OtherClass v = null;
    testdata.imports.testclasses.OtherClass v2 = null;

    public void clazz() {
        System.out.println(testdata.imports.testclasses.subpkg.OtherClass.class);
        System.out.println(OtherClass.class);
    }

    public void throwsClause() throws java.io.IOException, java.io.UncheckedIOException {
    }

    public <T extends OtherClass & testdata.imports.testclasses.subpkg.OtherInterface & java.io.Serializable> void bounds() {
    }

    abstract static class MultipleInterfaces implements testdata.imports.testclasses.subpkg.OtherInterface, java.io.Serializable {
    }

    public void expressions() {
        final Object fieldAccess = java.util.Locale.ENGLISH;
        final int importedField = testdata.imports.testclasses.subpkg.OtherClass.CONSTANT;
        java.lang.System.out.println();
        final String root = java.util.Locale.ROOT.toString();
        final Runnable methodRef = java.lang.Thread::yield;
        final Runnable constructorRef = java.util.ArrayList::new;
        final Holder holder = new Holder();
        final Object notAType = holder.inner.VALUE;
    }

    static final class Holder {
        static final int VALUE = 0;
        Holder inner;
    }

    // declared in this file, so java.util.Random needs to be qualified
    static final class Random {
    }

    java.util.Random declaredConflict;
    testdata.imports.UnnecessaryFullyQualifiedImport self;

    // SamePackage resolves to testdata.imports.SamePackage, so the other one needs to be qualified
    SamePackage samePackage;
    testdata.imports.testclasses.SamePackage otherPackage;
    testdata.imports.SamePackage qualifiedSamePackage;

    // the nested Shadowed hides testdata.imports.Shadowed, so it needs to be qualified even though it's in the same package
    static final class Shadowed {
    }

    testdata.imports.Shadowed shadowedSamePackage;
}
