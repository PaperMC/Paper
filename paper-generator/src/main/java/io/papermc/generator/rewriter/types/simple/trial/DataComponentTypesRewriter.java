package io.papermc.generator.rewriter.types.simple.trial;

import com.google.gson.internal.Primitives;
import com.mojang.serialization.Codec;
import io.papermc.generator.registry.RegistryEntries;
import io.papermc.generator.rewriter.types.registry.RegistryFieldRewriter;
import io.papermc.generator.utils.ClassHelper;
import io.papermc.paper.datacomponent.item.BlockItemDataProperties;
import io.papermc.paper.datacomponent.item.ItemAdventurePredicate;
import io.papermc.paper.datacomponent.item.ItemArmorTrim;
import io.papermc.typewriter.ClassNamed;
import io.papermc.typewriter.parser.Lexer;
import io.papermc.typewriter.parser.sequence.SequenceTokens;
import io.papermc.typewriter.parser.sequence.TokenTaskBuilder;
import io.papermc.typewriter.parser.token.CharSequenceBlockToken;
import io.papermc.typewriter.parser.token.CharSequenceToken;
import io.papermc.typewriter.parser.token.TokenType;
import io.papermc.typewriter.replace.SearchMetadata;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import net.kyori.adventure.key.Key;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Unit;
import net.minecraft.world.item.AdventureModePredicate;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import org.bukkit.FireworkEffect;
import org.bukkit.MusicInstrument;
import org.bukkit.inventory.ItemRarity;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.index.qual.Positive;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.jspecify.annotations.Nullable;

import static io.papermc.generator.utils.Formatting.quoted;

public class DataComponentTypesRewriter extends RegistryFieldRewriter<DataComponentType<?>> {

    public DataComponentTypesRewriter() {
        super(Registries.DATA_COMPONENT_TYPE, null);
    }

    private static final Set<TokenType> FORMAT_TOKENS = EnumSet.of(
        TokenType.COMMENT,
        TokenType.SINGLE_COMMENT
    );

    private @MonotonicNonNull Map<String, CharSequenceBlockToken> javadocsPerConstant;

    private Map<String, CharSequenceBlockToken> parseConstantJavadocs(String content) {
        Map<String, CharSequenceBlockToken> map = new HashMap<>();

        Lexer lex = new Lexer(content.toCharArray());
        lex.checkMarkdownDocComments = !this.sourcesMetadata.canSkipMarkdownDocComments();
        SequenceTokens.wrap(lex, FORMAT_TOKENS)
            .group(action -> {
                ProtoConstant constant = new ProtoConstant();
                action
                    .map(TokenType.JAVADOC, token -> {
                        constant.javadocs(((CharSequenceBlockToken) token));
                    }, TokenTaskBuilder::asOptional)
                    .skip(TokenType.PUBLIC).skip(TokenType.STATIC).skip(TokenType.FINAL)
                    .skipQualifiedName(Predicate.isEqual(TokenType.JAVADOC))
                    .skipClosure(TokenType.LT, TokenType.GT, true, TokenTaskBuilder::asOptional) // skip generic
                    .map(TokenType.IDENTIFIER, token -> {
                        constant.name(((CharSequenceToken) token).value());
                    })
                    .skip(TokenType.IDENTIFIER)
                    .skipClosure(TokenType.LPAREN, TokenType.RPAREN, true)
                    .map(TokenType.SECO, $ -> {
                        if (constant.isComplete()) {
                            map.put(constant.name(), constant.javadocs());
                        }
                    });
            }, TokenTaskBuilder::asRepeatable)
            .executeOrThrow();

        return map;
    }

    private static final Set<DataComponentType<?>> UNSUPPORTED_TYPES = Set.of(
        DataComponents.CUSTOM_DATA,
        DataComponents.CREATIVE_SLOT_LOCK,
        DataComponents.DEBUG_STICK_STATE,
        DataComponents.ENTITY_DATA,
        DataComponents.BUCKET_ENTITY_DATA,
        DataComponents.BLOCK_ENTITY_DATA,
        DataComponents.BEES,
        DataComponents.LOCK
    );

    private static final Map<ResourceKey<DataComponentType<?>>, Type> COMPONENT_GENERIC_TYPES = RegistryEntries.byRegistryKey(Registries.DATA_COMPONENT_TYPE).getFields(field -> {
        if (field.getGenericType() instanceof ParameterizedType complexType && complexType.getActualTypeArguments().length == 1) {
            return complexType.getActualTypeArguments()[0];
        }
        return null;
    });

    private static final Map<Class<?>, Class<?>> API_BRIDGE = Map.of(
        Component.class, net.kyori.adventure.text.Component.class,
        ResourceLocation.class, Key.class,
        Instrument.class, MusicInstrument.class,
        FireworkExplosion.class, FireworkEffect.class,
        Rarity.class, ItemRarity.class,
        ArmorTrim.class, ItemArmorTrim.class,
        // renames
        BlockItemStateProperties.class, BlockItemDataProperties.class,
        AdventureModePredicate.class, ItemAdventurePredicate.class
    );

    @Deprecated
    private static final Map<String, String> FIELD_RENAMES = Map.of(
        "BLOCK_STATE", "BLOCK_DATA"
    );

    @Override
    protected void insert(SearchMetadata metadata, StringBuilder builder) {
        this.javadocsPerConstant = parseConstantJavadocs(metadata.replacedContent());
        super.insert(metadata, builder);
    }

    @Override
    protected boolean canPrintField(Holder.Reference<DataComponentType<?>> reference) {
        return !UNSUPPORTED_TYPES.contains(reference.value());
    }

    @Override
    protected void rewriteJavadocs(Holder.Reference<DataComponentType<?>> reference, String replacedContent, String indent, StringBuilder builder) {
        String constantName = this.rewriteFieldName(reference);
        if (this.javadocsPerConstant.containsKey(constantName)) {
            CharSequenceBlockToken token = this.javadocsPerConstant.get(constantName);
            builder.append(indent).append(replacedContent, token.pos(), token.endPos()).append('\n');
        }
    }

    private boolean isValued;

    private Class<?> handleParameterizedType(Type type) {
        if (type instanceof ParameterizedType complexType) {
            Type[] args = complexType.getActualTypeArguments();
            if (args.length != 1) {
                throw new UnsupportedOperationException("Unsupported type " + complexType);
            }

            Class<?> baseClass = ClassHelper.eraseType(complexType);
            if (baseClass == Holder.class) {
                return ClassHelper.eraseType(args[0]);
            }
            if (baseClass == ResourceKey.class) {
                Class<?> componentClass = ClassHelper.eraseType(args[0]);
                if (componentClass == Recipe.class) {
                    return ResourceLocation.class; // special case recipe registry is not really a thing
                }
            }
        }

        throw new UnsupportedOperationException("Unsupported type " + type);
    }

    @Override
    protected String rewriteFieldType(Holder.Reference<DataComponentType<?>> reference) {
        Type componentType = COMPONENT_GENERIC_TYPES.get(reference.key());
        this.isValued = componentType != Unit.class;
        if (this.isValued) {
            Class<?> componentClass = null;
            UnaryOperator<String> tryToWrap = UnaryOperator.identity();
            if (!reference.value().isTransient()) {
                final Class<? extends Annotation> annotation = getEquivalentAnnotation(reference.value().codecOrThrow());
                if (annotation != null) {
                    tryToWrap = value -> "@%s %s".formatted(this.importCollector.getShortName(annotation), value);
                }
            }

            if (componentType instanceof Class<?> clazz) {
                componentClass = clazz;
            } else if (componentType instanceof ParameterizedType complexType) {
                Type[] args = complexType.getActualTypeArguments();
                if (args.length != 1) {
                    throw new UnsupportedOperationException("Unsupported type " + componentType);
                }

                Class<?> baseClass = ClassHelper.eraseType(complexType);
                if (baseClass == List.class) {
                    tryToWrap = value -> "%s<%s>".formatted(this.importCollector.getShortName(List.class), value);
                    componentClass = this.handleParameterizedType(args[0]);
                } else {
                    componentClass = this.handleParameterizedType(complexType);
                }
            }

            if (componentClass == null) {
                throw new UnsupportedOperationException("Unsupported type " + componentType);
            }

            Class<?> apiComponentClass = null;
            if (Primitives.isWrapperType(componentClass)) {
                apiComponentClass = componentClass;
            } else if (API_BRIDGE.containsKey(componentClass)) {
                apiComponentClass = API_BRIDGE.get(componentClass);
            }

            final ClassNamed finalClass;
            if (apiComponentClass == null) {
                finalClass = this.classNamedView.tryFindFirst(io.papermc.typewriter.util.ClassHelper.retrieveFullNestedName(componentClass)).orElse(null);
            } else {
                finalClass = new ClassNamed(apiComponentClass);
            }
            return "%s.%s<%s>".formatted(
                io.papermc.paper.datacomponent.DataComponentType.class.getSimpleName(),
                io.papermc.paper.datacomponent.DataComponentType.Valued.class.getSimpleName(),
                tryToWrap.apply(Optional.ofNullable(finalClass).map(this.importCollector::getShortName).orElse(componentClass.getSimpleName()))
            );
        } else {
            return "%s.%s".formatted(
                io.papermc.paper.datacomponent.DataComponentType.class.getSimpleName(),
                io.papermc.paper.datacomponent.DataComponentType.NonValued.class.getSimpleName()
            );
        }
    }

    private @Nullable Class<? extends Annotation> getEquivalentAnnotation(Codec<?> codec) {
        Class<? extends Annotation> annotation = null; // int range maybe?
        if (codec == ExtraCodecs.POSITIVE_INT || codec == ExtraCodecs.POSITIVE_FLOAT) {
            annotation = Positive.class;
        } else if (codec == ExtraCodecs.NON_NEGATIVE_INT || codec == ExtraCodecs.NON_NEGATIVE_FLOAT) {
            annotation = NonNegative.class;
        }
        return annotation;
    }

    @Override
    protected String rewriteFieldName(Holder.Reference<DataComponentType<?>> reference) {
        String keyedName = super.rewriteFieldName(reference);
        return FIELD_RENAMES.getOrDefault(keyedName, keyedName);
    }

    @Override
    protected String rewriteFieldValue(Holder.Reference<DataComponentType<?>> reference) {
        return "%s(%s)".formatted(this.isValued ? "valued" : "unvalued", quoted(reference.key().location().getPath()));
    }
}
