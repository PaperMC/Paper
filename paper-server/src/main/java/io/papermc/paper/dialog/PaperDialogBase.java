package io.papermc.paper.dialog;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.server.dialog.CommonDialogData;
import net.minecraft.server.dialog.DialogAction;
import net.minecraft.server.dialog.Input;
import net.minecraft.server.dialog.body.DialogBody;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaperDialogBase {
    public Component title = Component.empty();
    public Component externalTitle = null;
    public List<DialogBody> dialogBodyList = new ArrayList<>();
    public List<Input> inputControls = new ArrayList<>();
    public boolean canCloseWithEscape = true;
    public boolean canPause = true;
    public DialogAction afterAction = DialogAction.CLOSE;

    public CommonDialogData commonDialogData() {
        return new CommonDialogData(
            PaperAdventure.asVanilla(this.title),
            Optional.ofNullable(this.externalTitle).map(PaperAdventure::asVanilla),
            this.canCloseWithEscape,
            this.canPause,
            this.afterAction,
            this.dialogBodyList,
            this.inputControls
        );
    }
}
