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

    /**
     * A method that does something
     *
     * @param a                  this is a description
     *                           with a {@link #method(String, String)} link tag
     * @param superLongParamName a description
     *                           blabla
     */
    public void method_multiline_correct_with_link(String a, String superLongParamName) {
    }

    /**
     * A method where line 2 starts with a link tag
     *
     * @param a                  this is a description
     *                           {@link #method(String, String)} is at line start
     * @param superLongParamName a description
     *                           blabla
     */
    public void method_line2_starts_with_link(String a, String superLongParamName) {
    }

    /**
     * A method where line 2 starts with a link tag and is misaligned
     *
     * @param a                  this is a description
     *             {@link #method(String, String)} is misaligned
     * @param superLongParamName a description
     *                           blabla
     */
    public void method_line2_starts_with_link_misaligned(String a, String superLongParamName) {
    }

}
