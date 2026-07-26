package io.papermc.generator.types.craftblockdata.property;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import io.papermc.generator.utils.BlockStateMapping;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class EnumPropertyWriter<T extends Enum<T> & StringRepresentable> extends PropertyWriter<T> {

    protected EnumPropertyWriter(EnumProperty<T> property) {
        super(property);
    }

    @Override
    public TypeName getPropertyType() {
        if (this.property.getClass() == EnumProperty.class) { // exact match
            return ParameterizedTypeName.get(this.property.getClass(), this.property.getValueClass());
        }
        return super.getPropertyType();
    }

    @Override
    protected Class<?> processApiType() {
        Class<?> apiClass = this.property.getValueClass();
        apiClass = BlockStateMapping.ENUM_BRIDGE.get(apiClass);
        if (apiClass == null) {
            throw new IllegalStateException("Unknown enum type for " + this.property);
        }
        return apiClass;
    }

    @Override
    public void convertGetter(MethodSpec.Builder method, FieldSpec field) {
        method.addStatement("return " + this.rawGetExprent().formatted("$N"), field, this.getApiType());
    }

    @Override
    public String rawGetExprent() {
        return "this.get(%s, $T.class)";
    }
}
