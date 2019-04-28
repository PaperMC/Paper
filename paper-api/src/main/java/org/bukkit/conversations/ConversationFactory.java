package org.bukkit.conversations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A ConversationFactory is responsible for creating a {@link Conversation}
 * from a predefined template. A ConversationFactory is typically created when
 * a plugin is instantiated and builds a Conversation each time a user
 * initiates a conversation with the plugin. Each Conversation maintains its
 * own state and calls back as needed into the plugin.
 * <p>
 * The ConversationFactory implements a fluid API, allowing parameters to be
 * set as an extension to the constructor.
 */
public class ConversationFactory {

    protected Plugin plugin;
    protected boolean isModal;
    protected boolean localEchoEnabled;
    protected ConversationPrefix prefix;
    protected Prompt firstPrompt;
    protected Map<Object, Object> initialSessionData;
    protected String playerOnlyMessage;
    protected List<ConversationCanceller> cancellers;
    protected List<ConversationAbandonedListener> abandonedListeners;

    /**
     * Constructs a ConversationFactory.
     *
     * @param plugin The plugin that owns the factory.
     */
    public ConversationFactory(@NotNull Plugin plugin) {
        this.plugin = plugin;
        isModal = true;
        localEchoEnabled = true;
        prefix = new NullConversationPrefix();
        firstPrompt = Prompt.END_OF_CONVERSATION;
        initialSessionData = new HashMap<Object, Object>();
        playerOnlyMessage = null;
        cancellers = new ArrayList<ConversationCanceller>();
        abandonedListeners = new ArrayList<ConversationAbandonedListener>();
    }

    /**
     * Sets the modality of all {@link Conversation}s created by this factory.
     * If a conversation is modal, all messages directed to the player are
     * suppressed for the duration of the conversation.
     * <p>
     * The default is True.
     *
     * @param modal The modality of all conversations to be created.
     * @return This object.
     */
    @NotNull
    public ConversationFactory withModality(boolean modal) {
        isModal = modal;
        return this;
    }

    /**
     * Sets the local echo status for all {@link Conversation}s created by
     * this factory. If local echo is enabled, any text submitted to a
     * conversation gets echoed back into the submitter's chat window.
     *
     * @param localEchoEnabled The status of local echo.
     * @return This object.
     */
    @NotNull
    public ConversationFactory withLocalEcho(boolean localEchoEnabled) {
        this.localEchoEnabled = localEchoEnabled;
        return this;
    }

    /**
     * Sets the {@link ConversationPrefix} that prepends all output from all
     * generated conversations.
     * <p>
     * The default is a {@link NullConversationPrefix};
     *
     * @param prefix The ConversationPrefix to use.
     * @return This object.
     */
    @NotNull
    public ConversationFactory withPrefix(@NotNull ConversationPrefix prefix) {
        this.prefix = prefix;
        return this;
    }

    /**
     * Sets the number of inactive seconds to wait before automatically
     * abandoning all generated conversations.
     * <p>
     * The default is 600 seconds (5 minutes).
     *
     * @param timeoutSeconds The number of seconds to wait.
     * @return This object.
     */
    @NotNull
    public ConversationFactory withTimeout(int timeoutSeconds) {
        return withConversationCanceller(new InactivityConversationCanceller(plugin, timeoutSeconds));
    }

    /**
     * Sets the first prompt to use in all generated conversations.
     * <p>
     * The default is Prompt.END_OF_CONVERSATION.
     *
     * @param firstPrompt The first prompt.
     * @return This object.
     */
    @NotNull
    public ConversationFactory withFirstPrompt(@Nullable Prompt firstPrompt) {
        this.firstPrompt = firstPrompt;
        return this;
    }

    /**
     * Sets any initial data with which to populate the conversation context
     * sessionData map.
     *
     * @param initialSessionData The conversation context's initial
     *     sessionData.
     * @return This object.
     */
    @NotNull
    public ConversationFactory withInitialSessionData(@NotNull Map<Object, Object> initialSessionData) {
        this.initialSessionData = initialSessionData;
        return this;
    }

    /**
     * Sets the player input that, when received, will immediately terminate
     * the conversation.
     *
     * @param escapeSequence Input to terminate the conversation.
     * @return This object.
     */
    @NotNull
    public ConversationFactory withEscapeSequence(@NotNull String escapeSequence) {
        return withConversationCanceller(new ExactMatchConversationCanceller(escapeSequence));
    }

    /**
     * Adds a {@link ConversationCanceller} to constructed conversations.
     *
     * @param canceller The {@link ConversationCanceller} to add.
     * @return This object.
     */
    @NotNull
    public ConversationFactory withConversationCanceller(@NotNull ConversationCanceller canceller) {
        cancellers.add(canceller);
        return this;
    }

    /**
     * Prevents this factory from creating a conversation for non-player
     * {@link Conversable} objects.
     *
     * @param playerOnlyMessage The message to return to a non-play in lieu of
     *     starting a conversation.
     * @return This object.
     */
    @NotNull
    public ConversationFactory thatExcludesNonPlayersWithMessage(@Nullable String playerOnlyMessage) {
        this.playerOnlyMessage = playerOnlyMessage;
        return this;
    }

    /**
     * Adds a {@link ConversationAbandonedListener} to all conversations
     * constructed by this factory.
     *
     * @param listener The listener to add.
     * @return This object.
     */
    @NotNull
    public ConversationFactory addConversationAbandonedListener(@NotNull ConversationAbandonedListener listener) {
        abandonedListeners.add(listener);
        return this;
    }

    /**
     * Constructs a {@link Conversation} in accordance with the defaults set
     * for this factory.
     *
     * @param forWhom The entity for whom the new conversation is mediating.
     * @return A new conversation.
     */
    @NotNull
    public Conversation buildConversation(@NotNull Conversable forWhom) {
        //Abort conversation construction if we aren't supposed to talk to non-players
        if (playerOnlyMessage != null && !(forWhom instanceof Player)) {
            return new Conversation(plugin, forWhom, new NotPlayerMessagePrompt());
        }

        //Clone any initial session data
        Map<Object, Object> copiedInitialSessionData = new HashMap<Object, Object>();
        copiedInitialSessionData.putAll(initialSessionData);

        //Build and return a conversation
        Conversation conversation = new Conversation(plugin, forWhom, firstPrompt, copiedInitialSessionData);
        conversation.setModal(isModal);
        conversation.setLocalEchoEnabled(localEchoEnabled);
        conversation.setPrefix(prefix);

        //Clone the conversation cancellers
        for (ConversationCanceller canceller : cancellers) {
            conversation.addConversationCanceller(canceller.clone());
        }

        //Add the ConversationAbandonedListeners
        for (ConversationAbandonedListener listener : abandonedListeners) {
            conversation.addConversationAbandonedListener(listener);
        }

        return conversation;
    }

    private class NotPlayerMessagePrompt extends MessagePrompt {

        @Override
        @NotNull
        public String getPromptText(@NotNull ConversationContext context) {
            return playerOnlyMessage;
        }

        @Nullable
        @Override
        protected Prompt getNextPrompt(@NotNull ConversationContext context) {
            return Prompt.END_OF_CONVERSATION;
        }
    }
}
