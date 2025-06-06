package io.papermc.paper.dialog.types;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.dialog.BodyElement;
import io.papermc.paper.dialog.body.BodyElementConversion;
import net.kyori.adventure.text.Component;
import net.minecraft.server.dialog.CommonDialogData;
import net.minecraft.server.dialog.DialogAction;
import net.minecraft.server.dialog.Input;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaperDialogBase<B extends PaperDialogBase<B>> {
    public Component title = Component.empty();
    public Component externalTitle = null;
    public List<BodyElementConversion> dialogBodyList = new ArrayList<>();
    public List<Input> inputControls = new ArrayList<>();
    public boolean canCloseWithEscape = true;
    public boolean canPause = true;
    public DialogAction afterAction = DialogAction.CLOSE;

    @SuppressWarnings("unchecked")
    public B bodyElements(List<BodyElement> bodyElements) {
        this.dialogBodyList = bodyElements.stream()
            .map(x -> (BodyElementConversion) x)
            .toList();
        return (B) this;
    }

    public List<BodyElement> bodyElements() {
        return this.dialogBodyList.stream().map(x -> (BodyElement) x).toList();
    }

    public CommonDialogData commonDialogData() {
        return new CommonDialogData(
            PaperAdventure.asVanilla(this.title),
            Optional.ofNullable(this.externalTitle).map(PaperAdventure::asVanilla),
            this.canCloseWithEscape,
            this.canPause,
            this.afterAction,
            this.dialogBodyList.stream().map(BodyElementConversion::dialogBody).toList(),
            this.inputControls
        );
    }
}
