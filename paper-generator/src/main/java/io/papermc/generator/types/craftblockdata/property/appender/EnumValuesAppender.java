package io.papermc.generator.types.craftblockdata.property.appender;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import io.papermc.generator.types.craftblockdata.CraftBlockDataGenerator;
import io.papermc.generator.utils.NamingManager;
import java.util.Set;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class EnumValuesAppender<T extends Enum<T> & StringRepresentable, A extends Enum<A>> implements PropertyAppender<T, A> {

    private final EnumProperty<T> property;
    private final Class<A> apiType;
    private final String methodName;

    public EnumValuesAppender(EnumProperty<T> property, Class<A> apiType, String methodName) {
        this.property = property;
        this.apiType = apiType;
        this.methodName = methodName;
    }

    @Override
    public EnumProperty<T> getProperty() {
        return this.property;
    }

    @Override
    public Class<A> getApiType() {
        return this.apiType;
    }

    @Override
    public void addExtras(TypeSpec.Builder builder, FieldSpec field, CraftBlockDataGenerator<?> generator, NamingManager naming) {
        MethodSpec.Builder methodBuilder = generator.createMethod(this.methodName);
        methodBuilder.addStatement("return this.getValues($N, $T.class)", field, this.apiType);
        methodBuilder.returns(ParameterizedTypeName.get(Set.class, this.apiType));

        builder.addMethod(methodBuilder.build());
    }
}
