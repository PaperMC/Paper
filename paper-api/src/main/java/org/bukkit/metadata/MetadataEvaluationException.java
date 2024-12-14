package org.bukkit.metadata;

/**
 * A MetadataEvaluationException is thrown any time a {@link
 * LazyMetadataValue} fails to evaluate its value due to an exception. The
 * originating exception will be included as this exception's cause.
 *
 * @since 1.1.0 R5
 */
@SuppressWarnings("serial")
public class MetadataEvaluationException extends RuntimeException {
    MetadataEvaluationException(Throwable cause) {
        super(cause);
    }
}
