package io.papermc.generator.types.craftblockdata.property.holder.appender;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import io.papermc.generator.types.craftblockdata.CraftBlockDataGenerator;
import io.papermc.generator.types.craftblockdata.property.converter.ConverterBase;
import io.papermc.generator.types.craftblockdata.property.holder.DataHolderType;
import io.papermc.generator.utils.NamingManager;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface DataAppender {

    DataHolderType getType();

    void addExtras(TypeSpec.Builder builder, FieldSpec field, ParameterSpec indexParameter, ConverterBase converter, CraftBlockDataGenerator<?> generator, NamingManager baseNaming);
}
