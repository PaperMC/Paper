package io.papermc.generator.types.craftblockdata.property.holder.appender;

import com.google.common.collect.ImmutableSet;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import io.papermc.generator.resources.DataFileLoader;
import io.papermc.generator.resources.DataFiles;
import io.papermc.generator.resources.predicate.BlockPredicate;
import io.papermc.generator.rewriter.types.Types;
import io.papermc.generator.types.craftblockdata.CraftBlockDataGenerator;
import io.papermc.generator.types.craftblockdata.property.converter.ConverterBase;
import io.papermc.generator.types.craftblockdata.property.holder.DataHolderType;
import io.papermc.generator.utils.BlockStateMapping;
import io.papermc.generator.utils.CommonVariable;
import io.papermc.generator.utils.NamingManager;
import io.papermc.typewriter.ClassNamed;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class MapAppender implements DataAppender {

    @Override
    public DataHolderType getType() {
        return DataHolderType.MAP;
    }

    @Override
    public void addExtras(TypeSpec.Builder builder, FieldSpec field, ParameterSpec indexParameter, ConverterBase childConverter, CraftBlockDataGenerator generator, NamingManager baseNaming) {
        if (childConverter.getApiType().equals(TypeName.BOOLEAN)) {
            String collectVarName = baseNaming.getVariableNameWrapper().post("s").concat();
            MethodSpec.Builder methodBuilder = generator.createMethod(baseNaming.getMethodNameWrapper().post("s").concat());
            methodBuilder.addStatement("$T $L = $T.builder()", ParameterizedTypeName.get(ClassName.get(ImmutableSet.Builder.class), indexParameter.type), collectVarName, ImmutableSet.class);
            methodBuilder.beginControlFlow("for ($T $N : $N.entrySet())", ParameterizedTypeName.get(ClassName.get(Map.Entry.class), indexParameter.type, TypeName.get(BooleanProperty.class)), CommonVariable.MAP_ENTRY, field);
            {
                methodBuilder.beginControlFlow("if (" + childConverter.rawGetExprent().formatted("$L.getValue()") + ")", CommonVariable.MAP_ENTRY);
                {
                    methodBuilder.addStatement("$L.add($N.getKey())", collectVarName, CommonVariable.MAP_ENTRY);
                }
                methodBuilder.endControlFlow();
            }
            methodBuilder.endControlFlow();
            methodBuilder.addStatement("return $L.build()", collectVarName);
            methodBuilder.returns(ParameterizedTypeName.get(ClassName.get(Set.class), indexParameter.type));

            builder.addMethod(methodBuilder.build());
        }

        if (indexParameter.type instanceof ClassName className) {
            if (generator.getBaseClass().equals(Types.BLOCK_DATA_MULTIPLE_FACING) ||
                generator.getBaseClass().equals(Types.BLOCK_DATA_REDSTONE_WIRE) ||
                isImplementing(generator.getBlock(), Types.BLOCK_DATA_MULTIPLE_FACING)) {

                MethodSpec.Builder methodBuilder = generator.createMethod(baseNaming.getMethodNameWrapper().pre("Allowed").post("s").concat());
                methodBuilder.addStatement("return $T.unmodifiableSet($N.keySet())", Collections.class, field);
                methodBuilder.returns(ParameterizedTypeName.get(ClassName.get(Set.class), className));

                builder.addMethod(methodBuilder.build());
            }
        }
    }

    public boolean isImplementing(Class<? extends Block> block, ClassNamed type) {
        Set<Property<?>> propertySet = new HashSet<>(BlockStateMapping.STATEFUL_BLOCKS.get(block));
        for (Map.Entry<ClassNamed, List<BlockPredicate>> predicateEntry : DataFileLoader.get(DataFiles.BLOCK_STATE_PREDICATES).entrySet()) {
            for (BlockPredicate predicate : predicateEntry.getValue()) {
                if (!predicate.matches(block, propertySet)) {
                    continue;
                }
                if (predicateEntry.getKey().equals(type)) {
                    return true;
                }
            }
        }

        return false;
    }
}
