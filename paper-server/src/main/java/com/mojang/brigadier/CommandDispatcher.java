// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT license.

package com.mojang.brigadier;

// CHECKSTYLE:OFF
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.context.SuggestionContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * The core command dispatcher, for registering, parsing, and executing commands.
 *
 * @param <S> a custom "source" type, such as a user or originator of a command
 */
public class CommandDispatcher<S> {
    /**
     * The string required to separate individual arguments in an input string
     *
     * @see #ARGUMENT_SEPARATOR_CHAR
     */
    public static final String ARGUMENT_SEPARATOR = " ";

    /**
     * The char required to separate individual arguments in an input string
     *
     * @see #ARGUMENT_SEPARATOR
     */
    public static final char ARGUMENT_SEPARATOR_CHAR = ' ';

    private static final String USAGE_OPTIONAL_OPEN = "[";
    private static final String USAGE_OPTIONAL_CLOSE = "]";
    private static final String USAGE_REQUIRED_OPEN = "(";
    private static final String USAGE_REQUIRED_CLOSE = ")";
    private static final String USAGE_OR = "|";

    private final RootCommandNode<S> root;

    private final Predicate<CommandNode<S>> hasCommand = new Predicate<CommandNode<S>>() {
        @Override
        public boolean test(final CommandNode<S> input) {
            return input != null && (input.getCommand() != null || input.getChildren().stream().anyMatch(hasCommand));
        }
    };
    private ResultConsumer<S> consumer = (c, s, r) -> {
    };

    /**
     * Create a new {@link CommandDispatcher} with the specified root node.
     *
     * <p>This is often useful to copy existing or pre-defined command trees.</p>
     *
     * @param root the existing {@link RootCommandNode} to use as the basis for this tree
     */
    public CommandDispatcher(final RootCommandNode<S> root) {
        this.root = root;
    }

    /**
     * Creates a new {@link CommandDispatcher} with an empty command tree.
     */
    public CommandDispatcher() {
        this(new RootCommandNode<>());
    }

    /**
     * Utility method for registering new commands.
     *
     * <p>This is a shortcut for calling {@link RootCommandNode#addChild(CommandNode)} after building the provided {@code command}.</p>
     *
     * <p>As {@link RootCommandNode} can only hold literals, this method will only allow literal arguments.</p>
     *
     * @param command a literal argument builder to add to this command tree
     * @return the node added to this tree
     */
    public LiteralCommandNode<S> register(final LiteralArgumentBuilder<S> command) {
        final LiteralCommandNode<S> build = command.build();
        root.addChild(build);
        return build;
    }

    /**
     * Sets a callback to be informed of the result of every command.
     *
     * @param consumer the new result consumer to be called
     */
    public void setConsumer(final ResultConsumer<S> consumer) {
        this.consumer = consumer;
    }

    /**
     * Parses and executes a given command.
     *
     * <p>This is a shortcut to first {@link #parse(StringReader, Object)} and then {@link #execute(ParseResults)}.</p>
     *
     * <p>It is recommended to parse and execute as separate steps, as parsing is often the most expensive step, and easiest to cache.</p>
     *
     * <p>If this command returns a value, then it successfully executed something. If it could not parse the command, or the execution was a failure,
     * then an exception will be thrown. Most exceptions will be of type {@link CommandSyntaxException}, but it is possible that a {@link RuntimeException}
     * may bubble up from the result of a command. The meaning behind the returned result is arbitrary, and will depend
     * entirely on what command was performed.</p>
     *
     * <p>If the command passes through a node that is {@link CommandNode#isFork()} then it will be 'forked'.
     * A forked command will not bubble up any {@link CommandSyntaxException}s, and the 'result' returned will turn into
     * 'amount of successful commands executes'.</p>
     *
     * <p>After each and any command is ran, a registered callback given to {@link #setConsumer(ResultConsumer)}
     * will be notified of the result and success of the command. You can use that method to gather more meaningful
     * results than this method will return, especially when a command forks.</p>
     *
     * @param input a command string to parse &amp; execute
     * @param source a custom "source" object, usually representing the originator of this command
     * @return a numeric result from a "command" that was performed
     * @throws CommandSyntaxException if the command failed to parse or execute
     * @throws RuntimeException if the command failed to execute and was not handled gracefully
     * @see #parse(String, Object)
     * @see #parse(StringReader, Object)
     * @see #execute(ParseResults)
     * @see #execute(StringReader, Object)
     */
    public int execute(final String input, final S source) throws CommandSyntaxException {
        return execute(new StringReader(input), source);
    }

    /**
     * Parses and executes a given command.
     *
     * <p>This is a shortcut to first {@link #parse(StringReader, Object)} and then {@link #execute(ParseResults)}.</p>
     *
     * <p>It is recommended to parse and execute as separate steps, as parsing is often the most expensive step, and easiest to cache.</p>
     *
     * <p>If this command returns a value, then it successfully executed something. If it could not parse the command, or the execution was a failure,
     * then an exception will be thrown. Most exceptions will be of type {@link CommandSyntaxException}, but it is possible that a {@link RuntimeException}
     * may bubble up from the result of a command. The meaning behind the returned result is arbitrary, and will depend
     * entirely on what command was performed.</p>
     *
     * <p>If the command passes through a node that is {@link CommandNode#isFork()} then it will be 'forked'.
     * A forked command will not bubble up any {@link CommandSyntaxException}s, and the 'result' returned will turn into
     * 'amount of successful commands executes'.</p>
     *
     * <p>After each and any command is ran, a registered callback given to {@link #setConsumer(ResultConsumer)}
     * will be notified of the result and success of the command. You can use that method to gather more meaningful
     * results than this method will return, especially when a command forks.</p>
     *
     * @param input a command string to parse &amp; execute
     * @param source a custom "source" object, usually representing the originator of this command
     * @return a numeric result from a "command" that was performed
     * @throws CommandSyntaxException if the command failed to parse or execute
     * @throws RuntimeException if the command failed to execute and was not handled gracefully
     * @see #parse(String, Object)
     * @see #parse(StringReader, Object)
     * @see #execute(ParseResults)
     * @see #execute(String, Object)
     */
    public int execute(final StringReader input, final S source) throws CommandSyntaxException {
        final ParseResults<S> parse = parse(input, source);
        return execute(parse);
    }

    /**
     * Executes a given pre-parsed command.
     *
     * <p>If this command returns a value, then it successfully executed something. If the execution was a failure,
     * then an exception will be thrown.
     * Most exceptions will be of type {@link CommandSyntaxException}, but it is possible that a {@link RuntimeException}
     * may bubble up from the result of a command. The meaning behind the returned result is arbitrary, and will depend
     * entirely on what command was performed.</p>
     *
     * <p>If the command passes through a node that is {@link CommandNode#isFork()} then it will be 'forked'.
     * A forked command will not bubble up any {@link CommandSyntaxException}s, and the 'result' returned will turn into
     * 'amount of successful commands executes'.</p>
     *
     * <p>After each and any command is ran, a registered callback given to {@link #setConsumer(ResultConsumer)}
     * will be notified of the result and success of the command. You can use that method to gather more meaningful
     * results than this method will return, especially when a command forks.</p>
     *
     * @param parse the result of a successful {@link #parse(StringReader, Object)}
     * @return a numeric result from a "command" that was performed.
     * @throws CommandSyntaxException if the command failed to parse or execute
     * @throws RuntimeException if the command failed to execute and was not handled gracefully
     * @see #parse(String, Object)
     * @see #parse(StringReader, Object)
     * @see #execute(String, Object)
     * @see #execute(StringReader, Object)
     */
    public int execute(final ParseResults<S> parse) throws CommandSyntaxException {
        if (parse.getReader().canRead()) {
            if (parse.getExceptions().size() == 1) {
                throw parse.getExceptions().values().iterator().next();
            } else if (parse.getContext().getRange().isEmpty()) {
                throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(parse.getReader());
            } else {
                throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(parse.getReader());
            }
        }

        int result = 0;
        int successfulForks = 0;
        boolean forked = false;
        boolean foundCommand = false;
        final String command = parse.getReader().getString();
        final CommandContext<S> original = parse.getContext().build(command);
        List<CommandContext<S>> contexts = Collections.singletonList(original);
        ArrayList<CommandContext<S>> next = null;

        while (contexts != null) {
            final int size = contexts.size();
            for (int i = 0; i < size; i++) {
                final CommandContext<S> context = contexts.get(i);
                final CommandContext<S> child = context.getChild();
                if (child != null) {
                    forked |= context.isForked();
                    if (child.hasNodes()) {
                        foundCommand = true;
                        final RedirectModifier<S> modifier = context.getRedirectModifier();
                        if (modifier == null) {
                            if (next == null) {
                                next = new ArrayList<>(1);
                            }
                            next.add(child.copyFor(context.getSource()));
                        } else {
                            try {
                                final Collection<S> results = modifier.apply(context);
                                if (!results.isEmpty()) {
                                    if (next == null) {
                                        next = new ArrayList<>(results.size());
                                    }
                                    for (final S source : results) {
                                        next.add(child.copyFor(source));
                                    }
                                }
                            } catch (final CommandSyntaxException ex) {
                                consumer.onCommandComplete(context, false, 0);
                                if (!forked) {
                                    throw ex;
                                }
                            }
                        }
                    }
                } else if (context.getCommand() != null) {
                    foundCommand = true;
                    try {
                        final int value = context.getCommand().run(context);
                        result += value;
                        consumer.onCommandComplete(context, true, value);
                        successfulForks++;
                    } catch (final CommandSyntaxException ex) {
                        consumer.onCommandComplete(context, false, 0);
                        if (!forked) {
                            throw ex;
                        }
                    }
                }
            }

            contexts = next;
            next = null;
        }

        if (!foundCommand) {
            consumer.onCommandComplete(original, false, 0);
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(parse.getReader());
        }

        return forked ? successfulForks : result;
    }

    /**
     * Parses a given command.
     *
     * <p>The result of this method can be cached, and it is advised to do so where appropriate. Parsing is often the
     * most expensive step, and this allows you to essentially "precompile" a command if it will be ran often.</p>
     *
     * <p>If the command passes through a node that is {@link CommandNode#isFork()} then the resulting context will be marked as 'forked'.
     * Forked contexts may contain child contexts, which may be modified by the {@link RedirectModifier} attached to the fork.</p>
     *
     * <p>Parsing a command can never fail, you will always be provided with a new {@link ParseResults}.
     * However, that does not mean that it will always parse into a valid command. You should inspect the returned results
     * to check for validity. If its {@link ParseResults#getReader()} {@link StringReader#canRead()} then it did not finish
     * parsing successfully. You can use that position as an indicator to the user where the command stopped being valid.
     * You may inspect {@link ParseResults#getExceptions()} if you know the parse failed, as it will explain why it could
     * not find any valid commands. It may contain multiple exceptions, one for each "potential node" that it could have visited,
     * explaining why it did not go down that node.</p>
     *
     * <p>When you eventually call {@link #execute(ParseResults)} with the result of this method, the above error checking
     * will occur. You only need to inspect it yourself if you wish to handle that yourself.</p>
     *
     * @param command a command string to parse
     * @param source a custom "source" object, usually representing the originator of this command
     * @return the result of parsing this command
     * @see #parse(StringReader, Object)
     * @see #execute(ParseResults)
     * @see #execute(String, Object)
     */
    public ParseResults<S> parse(final String command, final S source) {
        return parse(new StringReader(command), source);
    }

    /**
     * Parses a given command.
     *
     * <p>The result of this method can be cached, and it is advised to do so where appropriate. Parsing is often the
     * most expensive step, and this allows you to essentially "precompile" a command if it will be ran often.</p>
     *
     * <p>If the command passes through a node that is {@link CommandNode#isFork()} then the resulting context will be marked as 'forked'.
     * Forked contexts may contain child contexts, which may be modified by the {@link RedirectModifier} attached to the fork.</p>
     *
     * <p>Parsing a command can never fail, you will always be provided with a new {@link ParseResults}.
     * However, that does not mean that it will always parse into a valid command. You should inspect the returned results
     * to check for validity. If its {@link ParseResults#getReader()} {@link StringReader#canRead()} then it did not finish
     * parsing successfully. You can use that position as an indicator to the user where the command stopped being valid.
     * You may inspect {@link ParseResults#getExceptions()} if you know the parse failed, as it will explain why it could
     * not find any valid commands. It may contain multiple exceptions, one for each "potential node" that it could have visited,
     * explaining why it did not go down that node.</p>
     *
     * <p>When you eventually call {@link #execute(ParseResults)} with the result of this method, the above error checking
     * will occur. You only need to inspect it yourself if you wish to handle that yourself.</p>
     *
     * @param command a command string to parse
     * @param source a custom "source" object, usually representing the originator of this command
     * @return the result of parsing this command
     * @see #parse(String, Object)
     * @see #execute(ParseResults)
     * @see #execute(String, Object)
     */
    public ParseResults<S> parse(final StringReader command, final S source) {
        final CommandContextBuilder<S> context = new CommandContextBuilder<>(this, source, root, command.getCursor());
        return parseNodes(root, command, context);
    }

    private ParseResults<S> parseNodes(final CommandNode<S> node, final StringReader originalReader, final CommandContextBuilder<S> contextSoFar) {
        final S source = contextSoFar.getSource();
        Map<CommandNode<S>, CommandSyntaxException> errors = null;
        List<ParseResults<S>> potentials = null;
        final int cursor = originalReader.getCursor();

        for (final CommandNode<S> child : node.getRelevantNodes(originalReader)) {
            if (!child.canUse(source)) {
                continue;
            }
            final CommandContextBuilder<S> context = contextSoFar.copy();
            final StringReader reader = new StringReader(originalReader);
            try {
                try {
                    child.parse(reader, context);
                } catch (final RuntimeException ex) {
                    throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(reader, ex.getMessage());
                }
                if (reader.canRead()) {
                    if (reader.peek() != ARGUMENT_SEPARATOR_CHAR) {
                        throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherExpectedArgumentSeparator().createWithContext(reader);
                    }
                }
            } catch (final CommandSyntaxException ex) {
                if (errors == null) {
                    errors = new LinkedHashMap<>();
                }
                errors.put(child, ex);
                reader.setCursor(cursor);
                continue;
            }

            context.withCommand(child.getCommand());
            if (reader.canRead(child.getRedirect() == null ? 2 : 1)) {
                reader.skip();
                if (child.getRedirect() != null) {
                    final CommandContextBuilder<S> childContext = new CommandContextBuilder<>(this, source, child.getRedirect(), reader.getCursor());
                    final ParseResults<S> parse = parseNodes(child.getRedirect(), reader, childContext);
                    context.withChild(parse.getContext());
                    return new ParseResults<>(context, parse.getReader(), parse.getExceptions());
                } else {
                    final ParseResults<S> parse = parseNodes(child, reader, context);
                    if (potentials == null) {
                        potentials = new ArrayList<>(1);
                    }
                    potentials.add(parse);
                }
            } else {
                if (potentials == null) {
                    potentials = new ArrayList<>(1);
                }
                potentials.add(new ParseResults<>(context, reader, Collections.emptyMap()));
            }
        }

        if (potentials != null) {
            if (potentials.size() > 1) {
                potentials.sort((a, b) -> {
                    if (!a.getReader().canRead() && b.getReader().canRead()) {
                        return -1;
                    }
                    if (a.getReader().canRead() && !b.getReader().canRead()) {
                        return 1;
                    }
                    if (a.getExceptions().isEmpty() && !b.getExceptions().isEmpty()) {
                        return -1;
                    }
                    if (!a.getExceptions().isEmpty() && b.getExceptions().isEmpty()) {
                        return 1;
                    }
                    return 0;
                });
            }
            return potentials.get(0);
        }

        return new ParseResults<>(contextSoFar, originalReader, errors == null ? Collections.emptyMap() : errors);
    }

    /**
     * Gets all possible executable commands following the given node.
     *
     * <p>You may use {@link #getRoot()} as a target to get all usage data for the entire command tree.</p>
     *
     * <p>The returned syntax will be in "simple" form: {@code <param>} and {@code literal}. "Optional" nodes will be
     * listed as multiple entries: the parent node, and the child nodes.
     * For example, a required literal "foo" followed by an optional param "int" will be two nodes:</p>
     * <ul>
     *     <li>{@code foo}</li>
     *     <li>{@code foo <int>}</li>
     * </ul>
     *
     * <p>The path to the specified node will <b>not</b> be prepended to the output, as there can theoretically be many
     * ways to reach a given node. It will only give you paths relative to the specified node, not absolute from root.</p>
     *
     * @param node target node to get child usage strings for
     * @param source a custom "source" object, usually representing the originator of this command
     * @param restricted if true, commands that the {@code source} cannot access will not be mentioned
     * @return array of full usage strings under the target node
     */
    public String[] getAllUsage(final CommandNode<S> node, final S source, final boolean restricted) {
        final ArrayList<String> result = new ArrayList<>();
        getAllUsage(node, source, result, "", restricted);
        return result.toArray(new String[result.size()]);
    }

    private void getAllUsage(final CommandNode<S> node, final S source, final ArrayList<String> result, final String prefix, final boolean restricted) {
        if (restricted && !node.canUse(source)) {
            return;
        }

        if (node.getCommand() != null) {
            result.add(prefix);
        }

        if (node.getRedirect() != null) {
            final String redirect = node.getRedirect() == root ? "..." : "-> " + node.getRedirect().getUsageText();
            result.add(prefix.isEmpty() ? node.getUsageText() + ARGUMENT_SEPARATOR + redirect : prefix + ARGUMENT_SEPARATOR + redirect);
        } else if (!node.getChildren().isEmpty()) {
            for (final CommandNode<S> child : node.getChildren()) {
                getAllUsage(child, source, result, prefix.isEmpty() ? child.getUsageText() : prefix + ARGUMENT_SEPARATOR + child.getUsageText(), restricted);
            }
        }
    }

    /**
     * Gets the possible executable commands from a specified node.
     *
     * <p>You may use {@link #getRoot()} as a target to get usage data for the entire command tree.</p>
     *
     * <p>The returned syntax will be in "smart" form: {@code <param>}, {@code literal}, {@code [optional]} and {@code (either|or)}.
     * These forms may be mixed and matched to provide as much information about the child nodes as it can, without being too verbose.
     * For example, a required literal "foo" followed by an optional param "int" can be compressed into one string:</p>
     * <ul>
     *     <li>{@code foo [<int>]}</li>
     * </ul>
     *
     * <p>The path to the specified node will <b>not</b> be prepended to the output, as there can theoretically be many
     * ways to reach a given node. It will only give you paths relative to the specified node, not absolute from root.</p>
     *
     * <p>The returned usage will be restricted to only commands that the provided {@code source} can use.</p>
     *
     * @param node target node to get child usage strings for
     * @param source a custom "source" object, usually representing the originator of this command
     * @return array of full usage strings under the target node
     */
    public Map<CommandNode<S>, String> getSmartUsage(final CommandNode<S> node, final S source) {
        final Map<CommandNode<S>, String> result = new LinkedHashMap<>();

        final boolean optional = node.getCommand() != null;
        for (final CommandNode<S> child : node.getChildren()) {
            final String usage = getSmartUsage(child, source, optional, false);
            if (usage != null) {
                result.put(child, usage);
            }
        }
        return result;
    }

    private String getSmartUsage(final CommandNode<S> node, final S source, final boolean optional, final boolean deep) {
        if (!node.canUse(source)) {
            return null;
        }

        final String self = optional ? USAGE_OPTIONAL_OPEN + node.getUsageText() + USAGE_OPTIONAL_CLOSE : node.getUsageText();
        final boolean childOptional = node.getCommand() != null;
        final String open = childOptional ? USAGE_OPTIONAL_OPEN : USAGE_REQUIRED_OPEN;
        final String close = childOptional ? USAGE_OPTIONAL_CLOSE : USAGE_REQUIRED_CLOSE;

        if (!deep) {
            if (node.getRedirect() != null) {
                final String redirect = node.getRedirect() == root ? "..." : "-> " + node.getRedirect().getUsageText();
                return self + ARGUMENT_SEPARATOR + redirect;
            } else {
                final Collection<CommandNode<S>> children = node.getChildren().stream().filter(c -> c.canUse(source)).collect(Collectors.toList());
                if (children.size() == 1) {
                    final String usage = getSmartUsage(children.iterator().next(), source, childOptional, childOptional);
                    if (usage != null) {
                        return self + ARGUMENT_SEPARATOR + usage;
                    }
                } else if (children.size() > 1) {
                    final Set<String> childUsage = new LinkedHashSet<>();
                    for (final CommandNode<S> child : children) {
                        final String usage = getSmartUsage(child, source, childOptional, true);
                        if (usage != null) {
                            childUsage.add(usage);
                        }
                    }
                    if (childUsage.size() == 1) {
                        final String usage = childUsage.iterator().next();
                        return self + ARGUMENT_SEPARATOR + (childOptional ? USAGE_OPTIONAL_OPEN + usage + USAGE_OPTIONAL_CLOSE : usage);
                    } else if (childUsage.size() > 1) {
                        final StringBuilder builder = new StringBuilder(open);
                        int count = 0;
                        for (final CommandNode<S> child : children) {
                            if (count > 0) {
                                builder.append(USAGE_OR);
                            }
                            builder.append(child.getUsageText());
                            count++;
                        }
                        if (count > 0) {
                            builder.append(close);
                            return self + ARGUMENT_SEPARATOR + builder.toString();
                        }
                    }
                }
            }
        }

        return self;
    }

    /**
     * Gets suggestions for a parsed input string on what comes next.
     *
     * <p>As it is ultimately up to custom argument types to provide suggestions, it may be an asynchronous operation,
     * for example getting in-game data or player names etc. As such, this method returns a future and no guarantees
     * are made to when or how the future completes.</p>
     *
     * <p>The suggestions provided will be in the context of the end of the parsed input string, but may suggest
     * new or replacement strings for earlier in the input string. For example, if the end of the string was
     * {@code foobar} but an argument preferred it to be {@code minecraft:foobar}, it will suggest a replacement for that
     * whole segment of the input.</p>
     *
     * @param parse the result of a {@link #parse(StringReader, Object)}
     * @return a future that will eventually resolve into a {@link Suggestions} object
     */
    public CompletableFuture<Suggestions> getCompletionSuggestions(final ParseResults<S> parse) {
        return getCompletionSuggestions(parse, parse.getReader().getTotalLength());
    }

    public CompletableFuture<Suggestions> getCompletionSuggestions(final ParseResults<S> parse, int cursor) {
        final CommandContextBuilder<S> context = parse.getContext();

        final SuggestionContext<S> nodeBeforeCursor = context.findSuggestionContext(cursor);
        final CommandNode<S> parent = nodeBeforeCursor.parent;
        final int start = Math.min(nodeBeforeCursor.startPos, cursor);

        final String fullInput = parse.getReader().getString();
        final String truncatedInput = fullInput.substring(0, cursor);
        final String truncatedInputLowerCase = truncatedInput.toLowerCase(Locale.ROOT);
        @SuppressWarnings("unchecked") final CompletableFuture<Suggestions>[] futures = new CompletableFuture[parent.getChildren().size()];
        int i = 0;
        for (final CommandNode<S> node : parent.getChildren()) {
            CompletableFuture<Suggestions> future = Suggestions.empty();
            try {
                if (node.canUse(parse.getContext().getSource())) future = node.listSuggestions(context.build(truncatedInput), new SuggestionsBuilder(truncatedInput, truncatedInputLowerCase, start)); // CraftBukkit
            } catch (final CommandSyntaxException ignored) {
            }
            futures[i++] = future;
        }

        final CompletableFuture<Suggestions> result = new CompletableFuture<>();
        CompletableFuture.allOf(futures).thenRun(() -> {
            final List<Suggestions> suggestions = new ArrayList<>();
            for (final CompletableFuture<Suggestions> future : futures) {
                suggestions.add(future.join());
            }
            result.complete(Suggestions.merge(fullInput, suggestions));
        });

        return result;
    }

    /**
     * Gets the root of this command tree.
     *
     * <p>This is often useful as a target of a {@link com.mojang.brigadier.builder.ArgumentBuilder#redirect(CommandNode)},
     * {@link #getAllUsage(CommandNode, Object, boolean)} or {@link #getSmartUsage(CommandNode, Object)}.
     * You may also use it to clone the command tree via {@link #CommandDispatcher(RootCommandNode)}.</p>
     *
     * @return root of the command tree
     */
    public RootCommandNode<S> getRoot() {
        return root;
    }

    /**
     * Finds a valid path to a given node on the command tree.
     *
     * <p>There may theoretically be multiple paths to a node on the tree, especially with the use of forking or redirecting.
     * As such, this method makes no guarantees about which path it finds. It will not look at forks or redirects,
     * and find the first instance of the target node on the tree.</p>
     *
     * <p>The only guarantee made is that for the same command tree and the same version of this library, the result of
     * this method will <b>always</b> be a valid input for {@link #findNode(Collection)}, which should return the same node
     * as provided to this method.</p>
     *
     * @param target the target node you are finding a path for
     * @return a path to the resulting node, or an empty list if it was not found
     */
    public Collection<String> getPath(final CommandNode<S> target) {
        final List<List<CommandNode<S>>> nodes = new ArrayList<>();
        addPaths(root, nodes, new ArrayList<>());

        for (final List<CommandNode<S>> list : nodes) {
            if (list.get(list.size() - 1) == target) {
                final List<String> result = new ArrayList<>(list.size());
                for (final CommandNode<S> node : list) {
                    if (node != root) {
                        result.add(node.getName());
                    }
                }
                return result;
            }
        }

        return Collections.emptyList();
    }

    /**
     * Finds a node by its path
     *
     * <p>Paths may be generated with {@link #getPath(CommandNode)}, and are guaranteed (for the same tree, and the
     * same version of this library) to always produce the same valid node by this method.</p>
     *
     * <p>If a node could not be found at the specified path, then {@code null} will be returned.</p>
     *
     * @param path a generated path to a node
     * @return the node at the given path, or null if not found
     */
    public CommandNode<S> findNode(final Collection<String> path) {
        CommandNode<S> node = root;
        for (final String name : path) {
            node = node.getChild(name);
            if (node == null) {
                return null;
            }
        }
        return node;
    }

    /**
     * Scans the command tree for potential ambiguous commands.
     *
     * <p>This is a shortcut for {@link CommandNode#findAmbiguities(AmbiguityConsumer)} on {@link #getRoot()}.</p>
     *
     * <p>Ambiguities are detected by testing every {@link CommandNode#getExamples()} on one node verses every sibling
     * node. This is not fool proof, and relies a lot on the providers of the used argument types to give good examples.</p>
     *
     * @param consumer a callback to be notified of potential ambiguities
     */
    public void findAmbiguities(final AmbiguityConsumer<S> consumer) {
        root.findAmbiguities(consumer);
    }

    private void addPaths(final CommandNode<S> node, final List<List<CommandNode<S>>> result, final List<CommandNode<S>> parents) {
        final List<CommandNode<S>> current = new ArrayList<>(parents);
        current.add(node);
        result.add(current);

        for (final CommandNode<S> child : node.getChildren()) {
            addPaths(child, result, current);
        }
    }
}
