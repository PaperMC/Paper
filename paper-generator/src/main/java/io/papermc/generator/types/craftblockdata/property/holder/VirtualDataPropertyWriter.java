package io.papermc.generator.types.craftblockdata.property.holder;

import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import io.papermc.generator.types.craftblockdata.CraftBlockDataGenerator;
import io.papermc.generator.types.craftblockdata.property.converter.ConverterBase;
import io.papermc.generator.utils.BlockStateMapping;
import io.papermc.generator.utils.NamingManager;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.jspecify.annotations.NullMarked;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.STATIC;

@NullMarked
public class VirtualDataPropertyWriter extends DataPropertyWriterBase {

    private final VirtualField virtualField;
    protected @MonotonicNonNull Class<?> indexClass;
    protected @MonotonicNonNull TypeName fieldType;

    protected VirtualDataPropertyWriter(VirtualField virtualField, Collection<? extends Property<?>> properties, Class<? extends Block> blockClass) {
        super(properties, blockClass);
        this.virtualField = virtualField;
        this.computeTypes(virtualField);
    }

    protected void computeTypes(VirtualField virtualField) {
        switch (virtualField.holderType()) {
            case ARRAY -> {
                this.indexClass = Integer.TYPE;
                this.fieldType = ArrayTypeName.of(virtualField.valueType());
            }
            case LIST -> {
                this.indexClass = Integer.TYPE;
                this.fieldType = ParameterizedTypeName.get(List.class, virtualField.valueType());
            }
            case MAP -> {
                if (virtualField.keyClass() != null) {
                    this.indexClass = virtualField.keyClass();
                } else {
                    this.indexClass = this.properties.iterator().next().getValueClass();
                    if (this.indexClass.isEnum()) {
                        this.indexClass = BlockStateMapping.ENUM_BRIDGE.getOrDefault(this.indexClass, (Class<? extends Enum<?>>) this.indexClass);
                    }
                }
                this.fieldType = ParameterizedTypeName.get(Map.class, this.indexClass, virtualField.valueType());
            }
        }
    }

    @Override
    public FieldSpec.Builder getOrCreateField(Map<Property<?>, Field> fields) {
        FieldSpec.Builder fieldBuilder = FieldSpec.builder(this.fieldType, this.virtualField.name(), PRIVATE, STATIC, FINAL);
        if (this.getType() == DataHolderType.ARRAY || this.getType() == DataHolderType.LIST) {
            CodeBlock.Builder code = CodeBlock.builder();
            this.createSyntheticCollection(code, this.getType() == DataHolderType.ARRAY, fields);
            fieldBuilder.initializer(code.build());
        } else if (this.getType() == DataHolderType.MAP) {
            CodeBlock.Builder code = CodeBlock.builder();
            this.createSyntheticMap(code, this.indexClass, fields);
            fieldBuilder.initializer(code.build());
        }

        return fieldBuilder;
    }

    @Override
    public Class<?> getIndexClass() {
        return this.indexClass;
    }

    @Override
    public DataHolderType getType() {
        return this.virtualField.holderType();
    }

    @Override
    public String getBaseName() {
        return this.virtualField.baseName();
    }

    @Override
    public void addExtras(TypeSpec.Builder builder, FieldSpec field, ParameterSpec indexParameter, ConverterBase converter, CraftBlockDataGenerator<?> generator, NamingManager baseNaming) {

    }
}
