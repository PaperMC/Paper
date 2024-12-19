package org.bukkit.metadata;

/**
 * A MetadataConversionException is thrown any time a {@link
 * LazyMetadataValue} attempts to convert a metadata value to an inappropriate
 * data type.
 *
 * @since 1.1.0 R5
 */
@SuppressWarnings("serial")
public class MetadataConversionException extends RuntimeException {
    MetadataConversionException(String message) {
        super(message);
    }
}
