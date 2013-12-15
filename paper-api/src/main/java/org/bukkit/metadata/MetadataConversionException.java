package org.bukkit.metadata;

/**
 * A MetadataConversionException is thrown any time a {@link
 * LazyMetadataValue} attempts to convert a metadata value to an inappropriate
 * data type.
 */
@SuppressWarnings("serial")
public class MetadataConversionException extends RuntimeException {
    MetadataConversionException(String message) {
        super(message);
    }
}
