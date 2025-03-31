package io.papermc.generator.rewriter.types.registry;

import com.mojang.logging.LogUtils;
import io.papermc.generator.Main;
import io.papermc.generator.registry.RegistryEntries;
import io.papermc.generator.rewriter.types.Types;
import io.papermc.generator.rewriter.utils.Annotations;
import io.papermc.generator.utils.Formatting;
import io.papermc.generator.utils.experimental.SingleFlagHolder;
import io.papermc.typewriter.ClassNamed;
import io.papermc.typewriter.parser.Lexer;
import io.papermc.typewriter.parser.sequence.SequenceTokens;
import io.papermc.typewriter.parser.sequence.TokenTaskBuilder;
import io.papermc.typewriter.parser.token.CharSequenceToken;
import io.papermc.typewriter.parser.token.TokenType;
import io.papermc.typewriter.replace.SearchMetadata;
import io.papermc.typewriter.replace.SearchReplaceRewriter;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;

import static io.papermc.generator.utils.Formatting.quoted;

@ApiStatus.Obsolete
public class TagRewriter extends SearchReplaceRewriter {

    private static final Logger LOGGER = LogUtils.getLogger();
    static final String FETCH_METHOD = "getTag";

    private record TagRegistry(String legacyFolderName, ClassNamed apiType, ResourceKey<? extends Registry<?>> registryKey) {
        public TagRegistry(String legacyFolderName, ResourceKey<? extends Registry<?>> registryKey) {
            this(legacyFolderName, RegistryEntries.BY_REGISTRY_KEY.get(registryKey).data().api().klass().name(), registryKey);
        }
    }

    private static final TagRegistry[] SUPPORTED_REGISTRIES = { // 1.21 folder name are normalized to registry key but api will stay as is
        new TagRegistry("blocks", Types.MATERIAL, Registries.BLOCK),
        new TagRegistry("items", Types.MATERIAL, Registries.ITEM),
        new TagRegistry("fluids", Registries.FLUID),
        new TagRegistry("entity_types", Registries.ENTITY_TYPE),
        new TagRegistry("game_events", Registries.GAME_EVENT)
        // new TagRegistry("damage_types", Registries.DAMAGE_TYPE) - separate in DamageTypeTags
    };

    private static final Set<TokenType> FORMAT_TOKENS = EnumSet.of(
        TokenType.COMMENT,
        TokenType.SINGLE_COMMENT,
        TokenType.JAVADOC,
        TokenType.MARKDOWN_JAVADOC
    );

    private List<String> parseExistingFields(String content) {
        List<String> fields = new ArrayList<>();

        Lexer lex = new Lexer(content.toCharArray());
        lex.checkMarkdownDocComments = !this.sourcesMetadata.canSkipMarkdownDocComments();
        // todo skipUntilNextLine + cleanup lexer flags
        SequenceTokens.wrap(lex, FORMAT_TOKENS)
            .group(action -> {
                action
                    .skip(TokenType.IDENTIFIER) // Tag
                    .skipClosure(TokenType.LT, TokenType.GT, true) // <*>
                    .map(TokenType.IDENTIFIER, token -> {
                        fields.add(((CharSequenceToken) token).value());
                    })
                    .skipQualifiedName() // Bukkit.getTag
                    .skipClosure(TokenType.LPAREN, TokenType.RPAREN, true) // (*)
                    .skip(TokenType.SECO); // ;
            }, TokenTaskBuilder::asRepeatable)
            .executeOrThrow();
        return fields;
    }

    @Override
    protected void insert(SearchMetadata metadata, StringBuilder builder) {
        List<String> replacedFields = !Main.IS_UPDATING ? new ArrayList<>() : this.parseExistingFields(metadata.replacedContent());

        for (int i = 0, len = SUPPORTED_REGISTRIES.length; i < len; i++) {
            final TagRegistry tagRegistry = SUPPORTED_REGISTRIES[i];

            final ResourceKey<? extends Registry<?>> registryKey = tagRegistry.registryKey();
            final Registry<?> registry = Main.REGISTRY_ACCESS.lookupOrThrow(registryKey);

            final String fieldPrefix = Formatting.formatTagFieldPrefix(tagRegistry.legacyFolderName(), registryKey);
            final String registryFieldName = "REGISTRY_" + tagRegistry.legacyFolderName().toUpperCase(Locale.ENGLISH);

            if (i != 0) {
                builder.append('\n'); // extra line before the registry field
            }

            // registry name field
            //builder.append(metadata.indent());
            //builder.append("%s %s = %s;".formatted(String.class.getSimpleName(), registryFieldName, quoted(tagRegistry.legacyFolderName())));

            //builder.append('\n');
            //builder.append('\n');

            Iterator<? extends TagKey<?>> keyIterator = registry.listTagIds().sorted(Formatting.TAG_ORDER).iterator();

            while (keyIterator.hasNext()) {
                TagKey<?> tagKey = keyIterator.next();
                final String keyPath = tagKey.location().getPath();
                final String fieldName = fieldPrefix + Formatting.formatKeyAsField(keyPath);
                replacedFields.remove(fieldName);

                // tag field
                String featureFlagName = Main.EXPERIMENTAL_TAGS.get(tagKey);
                if (featureFlagName != null) {
                    Annotations.experimentalAnnotations(builder, metadata.indent(), this.importCollector, SingleFlagHolder.fromName(featureFlagName));
                }

                builder.append(metadata.indent());
                builder.append("%s<%s>".formatted(this.source.mainClass().simpleName(), this.importCollector.getShortName(tagRegistry.apiType()))).append(' ').append(fieldName);
                builder.append(" = ");
                builder.append("%s.%s(%s, %s.minecraft(%s), %s.class)".formatted(Types.BUKKIT_CLASS.simpleName(), FETCH_METHOD, registryFieldName, Types.NAMESPACED_KEY.simpleName(), quoted(keyPath), tagRegistry.apiType().simpleName())); // assume type is imported properly
                builder.append(';');

                builder.append('\n');
                if (keyIterator.hasNext()) {
                    builder.append('\n');
                }
            }
        }

        if (!replacedFields.isEmpty()) {
            LOGGER.warn("Removed {} tag field(s) from {} because they are no longer present in the game:", replacedFields.size(), Types.TAG.canonicalName());
            replacedFields.forEach(fieldName -> LOGGER.warn("- {}", fieldName));
        }
    }
}
