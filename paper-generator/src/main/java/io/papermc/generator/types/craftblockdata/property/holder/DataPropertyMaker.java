package io.papermc.generator.types.craftblockdata.property.holder;

import com.mojang.datafixers.util.Either;
import com.squareup.javapoet.FieldSpec;
import io.papermc.generator.types.craftblockdata.property.holder.appender.DataAppender;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface DataPropertyMaker extends DataAppender {

    FieldSpec.Builder getOrCreateField(Map<Property<?>, Field> fields);

    Class<?> getIndexClass();

    @Override
    DataHolderType getType();

    String getBaseName();

    static DataPropertyMaker make(Collection<? extends Property<?>> properties, Class<? extends Block> blockClass, Either<Field, VirtualField> fieldData) {
        return fieldData.map(
            field -> new DataPropertyWriter(field, properties, blockClass),
            virtualField -> new VirtualDataPropertyWriter(virtualField, properties, blockClass)
        );
    }
}
