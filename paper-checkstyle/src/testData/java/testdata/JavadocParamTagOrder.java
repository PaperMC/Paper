// @formatter:off
package testdata;

/**
 * Class with type parameters.
 *
 * @param <A> first
 * @param <B> second
 */
@SuppressWarnings("ALL")
public class JavadocParamTagOrder<A, B> {

    /**
     * Correct order.
     *
     * @param a   first
     * @param b   second
     * @param <T> type
     * @param <U> type
     */
    public <T, U> void correct(final int a, final int b) {
    }

    /**
     * Regular parameters out of order.
     *
     * @param b second
     * @param a first
     */
    public void swapped(final int a, final int b) {
    }

    /**
     * Type parameter before regular parameters.
     *
     * @param <T> type
     * @param a   first
     */
    public <T> void typeParamFirst(final int a) {
    }

    /**
     * Type parameters out of order.
     *
     * @param a   first
     * @param <U> type
     * @param <T> type
     */
    public <T, U> void typeParamsSwapped(final int a) {
    }

    /**
     * Unknown names are ignored.
     *
     * @param a       first
     * @param unknown not a parameter
     * @param b       second
     */
    public void unknown(final int a, final int b) {
    }

    /**
     * Constructor out of order.
     *
     * @param b second
     * @param a first
     */
    public JavadocParamTagOrder(final int a, final int b) {
    }

    /**
     * Class type parameters out of order.
     *
     * @param <B> second
     * @param <A> first
     */
    interface Swapped<A, B> {
    }

    /**
     * Record components then type parameters.
     *
     * @param a   first
     * @param b   second
     * @param <T> type
     */
    record CorrectRecord<T>(int a, int b) {
    }

    /**
     * Record type parameter first.
     *
     * @param <T> type
     * @param a   first
     */
    record TypeParamFirstRecord<T>(int a) {

        /**
         * Compact constructor ignores record type parameters.
         *
         * @param a first
         */
        TypeParamFirstRecord {
        }
    }

    /**
     * Compact constructor out of order.
     *
     * @param b second
     * @param a first
     */
    record CompactRecord(int a, int b) {

        /**
         * Compact constructor out of order.
         *
         * @param b second
         * @param a first
         */
        CompactRecord {
        }
    }
}
