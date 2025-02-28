package io.papermc.paper.block;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface PaperLidded extends Lidded, org.bukkit.block.Lidded {

    @Override
    default LidMode setLidMode(final LidMode targetLidMode) {
        final LidMode oldLidMode = getLidMode();
        final LidMode newLidMode = getResultantLidMode(targetLidMode);

        if (oldLidMode == newLidMode) {
            // already in correct state
            return newLidMode;
        }

        boolean wasForcedOpen =
            oldLidMode == LidMode.FORCED_OPEN || oldLidMode == LidMode.OPEN_UNTIL_VIEWED;
        boolean wasForcedClosed =
            oldLidMode == LidMode.FORCED_CLOSED || oldLidMode == LidMode.CLOSED_UNTIL_NOT_VIEWED;
        boolean isForcedOpen =
            newLidMode == LidMode.FORCED_OPEN || newLidMode == LidMode.OPEN_UNTIL_VIEWED;
        boolean isForcedClosed =
            newLidMode == LidMode.FORCED_CLOSED || newLidMode == LidMode.CLOSED_UNTIL_NOT_VIEWED;

        // stop any existing force open/close, if next state doesn't need it.
        if (wasForcedOpen && !isForcedOpen) {
            stopForceLiddedLidOpen();
        } else if (wasForcedClosed && !isForcedClosed) {
            stopForceLiddedLidClose();
        }

        // start new force open/close, if it wasn't previously.
        if (isForcedOpen && !wasForcedOpen) {
            startForceLiddedLidOpen();
        } else if (isForcedClosed && !wasForcedClosed) {
            startForceLiddedLidClose();
        }

        // return the new lid mode, so it can be stored by the implementation.
        return newLidMode;
    }

    private LidMode getResultantLidMode(LidMode targetLidMode) {
        final LidState trueLidState = getTrueLidState();

        // check that target lid mode is valid for true lid state.
        LidMode newLidMode;

        if (targetLidMode == LidMode.CLOSED_UNTIL_NOT_VIEWED
            && trueLidState == LidState.CLOSED) {
            // insta-revert to default, as the lid is already closed.
            newLidMode = LidMode.DEFAULT;
        } else if (targetLidMode == LidMode.OPEN_UNTIL_VIEWED
            && trueLidState == LidState.OPEN) {
            // insta-revert to default, as the lid is already open.
            newLidMode = LidMode.DEFAULT;
        } else {
            newLidMode = targetLidMode;
        }
        return newLidMode;
    }

    // these should be similar to the vanilla open/close behavior.
    void startForceLiddedLidOpen();
    void stopForceLiddedLidOpen();
    void startForceLiddedLidClose();
    void stopForceLiddedLidClose();

    // bukkit lidded impl using the paper lidded api.

    @Override
    default boolean isOpen() {
        return getLidMode() == LidMode.FORCED_OPEN || getLidMode() == LidMode.OPEN_UNTIL_VIEWED;
    }

    @Override
    default void close() {
        setLidMode(LidMode.DEFAULT);
    }

    @Override
    default void open() {
        setLidMode(LidMode.FORCED_OPEN);
    }
}
