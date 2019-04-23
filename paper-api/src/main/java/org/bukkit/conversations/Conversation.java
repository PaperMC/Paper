package org.bukkit.conversations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The Conversation class is responsible for tracking the current state of a
 * conversation, displaying prompts to the user, and dispatching the user's
 * response to the appropriate place. Conversation objects are not typically
 * instantiated directly. Instead a {@link ConversationFactory} is used to
 * construct identical conversations on demand.
 * <p>
 * Conversation flow consists of a directed graph of {@link Prompt} objects.
 * Each time a prompt gets input from the user, it must return the next prompt
 * in the graph. Since each Prompt chooses the next Prompt, complex
 * conversation trees can be implemented where the nature of the player's
 * response directs the flow of the conversation.
 * <p>
 * Each conversation has a {@link ConversationPrefix} that prepends all output
 * from the conversation to the player. The ConversationPrefix can be used to
 * display the plugin name or conversation status as the conversation evolves.
 * <p>
 * Each conversation has a timeout measured in the number of inactive seconds
 * to wait before abandoning the conversation. If the inactivity timeout is
 * reached, the conversation is abandoned and the user's incoming and outgoing
 * chat is returned to normal.
 * <p>
 * You should not construct a conversation manually. Instead, use the {@link
 * ConversationFactory} for access to all available options.
 */
public class Conversation {

    private Prompt firstPrompt;
    private boolean abandoned;
    protected Prompt currentPrompt;
    protected ConversationContext context;
    protected boolean modal;
    protected boolean localEchoEnabled;
    protected ConversationPrefix prefix;
    protected List<ConversationCanceller> cancellers;
    protected List<ConversationAbandonedListener> abandonedListeners;

    /**
     * Initializes a new Conversation.
     *
     * @param plugin The plugin that owns this conversation.
     * @param forWhom The entity for whom this conversation is mediating.
     * @param firstPrompt The first prompt in the conversation graph.
     */
    public Conversation(@Nullable Plugin plugin, @NotNull Conversable forWhom, @Nullable Prompt firstPrompt) {
        this(plugin, forWhom, firstPrompt, new HashMap<Object, Object>());
    }

    /**
     * Initializes a new Conversation.
     *
     * @param plugin The plugin that owns this conversation.
     * @param forWhom The entity for whom this conversation is mediating.
     * @param firstPrompt The first prompt in the conversation graph.
     * @param initialSessionData Any initial values to put in the conversation
     *     context sessionData map.
     */
    public Conversation(@Nullable Plugin plugin, @NotNull Conversable forWhom, @Nullable Prompt firstPrompt, @NotNull Map<Object, Object> initialSessionData) {
        this.firstPrompt = firstPrompt;
        this.context = new ConversationContext(plugin, forWhom, initialSessionData);
        this.modal = true;
        this.localEchoEnabled = true;
        this.prefix = new NullConversationPrefix();
        this.cancellers = new ArrayList<ConversationCanceller>();
        this.abandonedListeners = new ArrayList<ConversationAbandonedListener>();
    }

    /**
     * Gets the entity for whom this conversation is mediating.
     *
     * @return The entity.
     */
    @NotNull
    public Conversable getForWhom() {
        return context.getForWhom();
    }

    /**
     * Gets the modality of this conversation. If a conversation is modal, all
     * messages directed to the player are suppressed for the duration of the
     * conversation.
     *
     * @return The conversation modality.
     */
    public boolean isModal() {
        return modal;
    }

    /**
     * Sets the modality of this conversation.  If a conversation is modal,
     * all messages directed to the player are suppressed for the duration of
     * the conversation.
     *
     * @param modal The new conversation modality.
     */
    void setModal(boolean modal) {
        this.modal = modal;
    }

    /**
     * Gets the status of local echo for this conversation. If local echo is
     * enabled, any text submitted to a conversation gets echoed back into the
     * submitter's chat window.
     *
     * @return The status of local echo.
     */
    public boolean isLocalEchoEnabled() {
        return localEchoEnabled;
    }

    /**
     * Sets the status of local echo for this conversation. If local echo is
     * enabled, any text submitted to a conversation gets echoed back into the
     * submitter's chat window.
     *
     * @param localEchoEnabled The status of local echo.
     */
    public void setLocalEchoEnabled(boolean localEchoEnabled) {
        this.localEchoEnabled = localEchoEnabled;
    }

    /**
     * Gets the {@link ConversationPrefix} that prepends all output from this
     * conversation.
     *
     * @return The ConversationPrefix in use.
     */
    @NotNull
    public ConversationPrefix getPrefix() {
        return prefix;
    }

    /**
     * Sets the {@link ConversationPrefix} that prepends all output from this
     * conversation.
     *
     * @param prefix The ConversationPrefix to use.
     */
    void setPrefix(@NotNull ConversationPrefix prefix) {
        this.prefix = prefix;
    }

    /**
     * Adds a {@link ConversationCanceller} to the cancellers collection.
     *
     * @param canceller The {@link ConversationCanceller} to add.
     */
    void addConversationCanceller(@NotNull ConversationCanceller canceller) {
        canceller.setConversation(this);
        this.cancellers.add(canceller);
    }

    /**
     * Gets the list of {@link ConversationCanceller}s
     *
     * @return The list.
     */
    @NotNull
    public List<ConversationCanceller> getCancellers() {
        return cancellers;
    }

    /**
     * Returns the Conversation's {@link ConversationContext}.
     *
     * @return The ConversationContext.
     */
    @NotNull
    public ConversationContext getContext() {
        return context;
    }

    /**
     * Displays the first prompt of this conversation and begins redirecting
     * the user's chat responses.
     */
    public void begin() {
        if (currentPrompt == null) {
            abandoned = false;
            currentPrompt = firstPrompt;
            context.getForWhom().beginConversation(this);
        }
    }

    /**
     * Returns Returns the current state of the conversation.
     *
     * @return The current state of the conversation.
     */
    @NotNull
    public ConversationState getState() {
        if (currentPrompt != null) {
            return ConversationState.STARTED;
        } else if (abandoned) {
            return ConversationState.ABANDONED;
        } else {
            return ConversationState.UNSTARTED;
        }
    }

    /**
     * Passes player input into the current prompt. The next prompt (as
     * determined by the current prompt) is then displayed to the user.
     *
     * @param input The user's chat text.
     */
    public void acceptInput(@NotNull String input) {
        if (currentPrompt != null) {

            // Echo the user's input
            if (localEchoEnabled) {
                context.getForWhom().sendRawMessage(prefix.getPrefix(context) + input);
            }

            // Test for conversation abandonment based on input
            for (ConversationCanceller canceller : cancellers) {
                if (canceller.cancelBasedOnInput(context, input)) {
                    abandon(new ConversationAbandonedEvent(this, canceller));
                    return;
                }
            }

            // Not abandoned, output the next prompt
            currentPrompt = currentPrompt.acceptInput(context, input);
            outputNextPrompt();
        }
    }

    /**
     * Adds a {@link ConversationAbandonedListener}.
     *
     * @param listener The listener to add.
     */
    public synchronized void addConversationAbandonedListener(@NotNull ConversationAbandonedListener listener) {
        abandonedListeners.add(listener);
    }

    /**
     * Removes a {@link ConversationAbandonedListener}.
     *
     * @param listener The listener to remove.
     */
    public synchronized void removeConversationAbandonedListener(@NotNull ConversationAbandonedListener listener) {
        abandonedListeners.remove(listener);
    }

    /**
     * Abandons and resets the current conversation. Restores the user's
     * normal chat behavior.
     */
    public void abandon() {
        abandon(new ConversationAbandonedEvent(this, new ManuallyAbandonedConversationCanceller()));
    }

    /**
     * Abandons and resets the current conversation. Restores the user's
     * normal chat behavior.
     *
     * @param details Details about why the conversation was abandoned
     */
    public synchronized void abandon(@NotNull ConversationAbandonedEvent details) {
        if (!abandoned) {
            abandoned = true;
            currentPrompt = null;
            context.getForWhom().abandonConversation(this);
            for (ConversationAbandonedListener listener : abandonedListeners) {
                listener.conversationAbandoned(details);
            }
        }
    }

    /**
     * Displays the next user prompt and abandons the conversation if the next
     * prompt is null.
     */
    public void outputNextPrompt() {
        if (currentPrompt == null) {
            abandon(new ConversationAbandonedEvent(this));
        } else {
            context.getForWhom().sendRawMessage(prefix.getPrefix(context) + currentPrompt.getPromptText(context));
            if (!currentPrompt.blocksForInput(context)) {
                currentPrompt = currentPrompt.acceptInput(context, null);
                outputNextPrompt();
            }
        }
    }

    public enum ConversationState {
        UNSTARTED,
        STARTED,
        ABANDONED
    }
}
