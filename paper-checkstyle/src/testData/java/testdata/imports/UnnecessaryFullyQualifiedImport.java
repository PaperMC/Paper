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
}
