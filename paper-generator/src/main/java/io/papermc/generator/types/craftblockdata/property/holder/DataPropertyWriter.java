package io.papermc.generator.types.craftblockdata.property.holder;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import io.papermc.generator.types.Types;
import io.papermc.generator.types.craftblockdata.CraftBlockDataGenerator;
import io.papermc.generator.types.craftblockdata.property.converter.ConverterBase;
import io.papermc.generator.types.craftblockdata.property.holder.appender.DataAppenders;
import io.papermc.generator.utils.BlockStateMapping;
import io.papermc.generator.utils.ClassHelper;
import io.papermc.generator.utils.CommonVariable;
import io.papermc.generator.utils.NamingManager;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChiseledBookShelfBlock;
import net.minecraft.world.level.block.MossyCarpetBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.properties.Property;
import org.bukkit.block.BlockFace;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.jspecify.annotations.NullMarked;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.STATIC;

@NullMarked
public class DataPropertyWriter extends DataPropertyWriterBase {

    private record FieldKey(Class<? extends Block> blockClass, String fieldName) {
    }

    private static FieldKey key(Class<? extends Block> blockClass, String fieldName) {
        return new FieldKey(blockClass, fieldName);
    }

    private static final Map<String, String> FIELD_TO_BASE_NAME = Map.of(
        "PROPERTY_BY_DIRECTION", "FACE"
    );

    private static final Map<FieldKey, String> FIELD_TO_BASE_NAME_SPECIFICS = Map.of(
        key(ChiseledBookShelfBlock.class, "SLOT_OCCUPIED_PROPERTIES"), "SLOT_OCCUPIED",
        key(MossyCarpetBlock.class, "PROPERTY_BY_DIRECTION"), "HEIGHT",
        key(WallBlock.class, "PROPERTY_BY_DIRECTION"), "HEIGHT"
    );

    protected final Field field;
    protected @MonotonicNonNull DataHolderType type;
    protected @MonotonicNonNull Class<?> indexClass, internalIndexClass;
    protected @MonotonicNonNull TypeName fieldType;

    protected DataPropertyWriter(Field field, Collection<? extends Property<?>> properties, Class<? extends Block> blockClass) {
        super(properties, blockClass);
        this.field = field;
        this.computeTypes(field);
    }

    protected void computeTypes(Field field) {
        this.fieldType = TypeName.get(field.getGenericType());

        if (field.getType().isArray()) {
            this.type = DataHolderType.ARRAY;
            this.indexClass = Integer.TYPE;
        } else if (List.class.isAssignableFrom(field.getType())) {
            this.type = DataHolderType.LIST;
            this.indexClass = Integer.TYPE;
        } else if (Map.class.isAssignableFrom(field.getType()) && field.getGenericType() instanceof ParameterizedType complexType) {
            this.type = DataHolderType.MAP;
            this.internalIndexClass = ClassHelper.eraseType(complexType.getActualTypeArguments()[0]);
            if (this.internalIndexClass.isEnum()) {
                this.indexClass = BlockStateMapping.ENUM_BRIDGE.getOrDefault(this.internalIndexClass, (Class<? extends Enum<?>>) this.internalIndexClass);
                this.fieldType = ParameterizedTypeName.get(
                    ClassName.get(field.getType()),
                    ClassName.get(this.indexClass),
                    ClassName.get(complexType.getActualTypeArguments()[1])
                );
            } else {
                this.indexClass = this.internalIndexClass;
            }
        } else {
            throw new IllegalStateException("Don't know how to turn " + field + " into api");
        }
    }

    @Override
    public FieldSpec.Builder getOrCreateField(Map<Property<?>, Field> fields) {
        FieldSpec.Builder fieldBuilder = FieldSpec.builder(this.fieldType, this.field.getName(), PRIVATE, STATIC, FINAL);
        if (Modifier.isPublic(this.field.getModifiers())) {
            // accessible phew
            if (this.type == DataHolderType.MAP &&
                this.internalIndexClass == Direction.class && this.indexClass == BlockFace.class) { // Direction -> BlockFace
                // convert the key manually only this one is needed for now
                fieldBuilder.initializer("$[$1T.$2L.entrySet().stream()\n.collect($3T.toMap($4L -> $5T.notchToBlockFace($4L.getKey()), $4L -> $4L.getValue()))$]",
                    this.blockClass, this.field.getName(), Collectors.class, CommonVariable.MAP_ENTRY, Types.CRAFT_BLOCK);
            } else {
                fieldBuilder.initializer("$T.$L", this.blockClass, this.field.getName());
            }
        } else {
            if (this.type == DataHolderType.ARRAY || this.type == DataHolderType.LIST) {
                CodeBlock.Builder code = CodeBlock.builder();
                this.createSyntheticCollection(code, this.type == DataHolderType.ARRAY, fields);
                fieldBuilder.initializer(code.build());
            } else if (this.type == DataHolderType.MAP) {
                CodeBlock.Builder code = CodeBlock.builder();
                this.createSyntheticMap(code, this.indexClass, fields);
                fieldBuilder.initializer(code.build());
            }
        }
        return fieldBuilder;
    }

    @Override
    public Class<?> getIndexClass() {
        return this.indexClass;
    }

    @Override
    public DataHolderType getType() {
        return this.type;
    }

    @Override
    public String getBaseName() {
        String constantName = this.field.getName();

        FieldKey key = key(this.blockClass, constantName);
        if (FIELD_TO_BASE_NAME_SPECIFICS.containsKey(key)) {
            return FIELD_TO_BASE_NAME_SPECIFICS.get(key);
        }

        if (FIELD_TO_BASE_NAME.containsKey(constantName)) {
            return FIELD_TO_BASE_NAME.get(constantName);
        }
        return stripFieldAccessKeyword(constantName);
    }

    private static final List<String> CUSTOM_KEYWORD = List.of("HAS", "IS", "CAN");

    private String stripFieldAccessKeyword(String name) {
        for (String keyword : CUSTOM_KEYWORD) {
            if (name.startsWith(keyword + "_")) {
                return name.substring(keyword.length() + 1);
            }
        }
        return name;
    }

    @Override
    public void addExtras(TypeSpec.Builder builder, FieldSpec field, ParameterSpec indexParameter, ConverterBase childConverter, CraftBlockDataGenerator<?> generator, NamingManager baseNaming) {
        DataAppenders.ifPresent(this.type, appender -> appender.addExtras(builder, field, indexParameter, childConverter, generator, baseNaming));
    }
}
