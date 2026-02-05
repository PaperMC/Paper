// @formatter:off
package testdata;

@SuppressWarnings("ALL")
public class JavadocParamAlignment {

    /**
     * A boring method.
     *
     * @param a some description
     * @param superLongParamName some description
     */
    public void method(String a, String superLongParamName) {
    }

    /**
     * A boring method.
     *
     * @param a                  some description
     * @param superLongParamName some description
     * @param another            param
     */
    public void correct_method(String a, String superLongParamName) {
    }

    /**
     * A boring method.
     *
     * @param a                  some description
     *                with extra text
     * @param superLongParamName some description
     *                       blah
     */
    public void method_multiline(String a, String superLongParamName) {
    }

    /**
     * A boring method.
     *
     * @param a                  some description
     *                           with extra text
     * @param superLongParamName some description
     *                           blah
     */
    public void method_multiline_correct(String a, String superLongParamName) {
    }

}
