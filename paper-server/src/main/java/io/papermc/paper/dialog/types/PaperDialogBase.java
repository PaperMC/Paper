package io.papermc.paper.dialog.types;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.dialog.BodyElement;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.dialog.InputElement;
import io.papermc.paper.dialog.body.BodyElementConversion;
import io.papermc.paper.dialog.inputs.InputElementConversion;
import net.kyori.adventure.text.Component;
import net.minecraft.server.dialog.CommonDialogData;
import net.minecraft.server.dialog.DialogAction;
import net.minecraft.server.dialog.Input;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class PaperDialogBase<B extends PaperDialogBase<B>> implements Dialog<B> {
    public Component title = Component.empty();
    public Component externalTitle = null;
    public List<BodyElementConversion> dialogBodyList = new ArrayList<>();
    public List<? extends InputElementConversion<?>> inputControls = new ArrayList<>();
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

    @SuppressWarnings("unchecked")
    public B inputElements(List<InputElement<?>> inputElements) {
        this.inputControls = inputElements.stream()
            .map(x -> (InputElementConversion<?>) x)
            .toList();
        return (B) this;
    }

    public List<? extends InputElement<?>> inputElements() {
        return this.inputControls.stream().map(x -> (InputElement<?>) x).toList();
    }

    public CommonDialogData commonDialogData() {
        return new CommonDialogData(
            PaperAdventure.asVanilla(this.title),
            Optional.ofNullable(this.externalTitle).map(PaperAdventure::asVanilla),
            this.canCloseWithEscape,
            this.canPause,
            this.afterAction,
            this.dialogBodyList.stream().map(BodyElementConversion::dialogBody).toList(),
            this.inputControls.stream().map(InputElementConversion::input).toList()
        );
    }

    @Override
    public Component title() {
        return this.title;
    }

    @Override
    @SuppressWarnings("unchecked")
    public B title(final Component component) {
        this.title = component;
        return (B) this;
    }


    @Override
    public Component externalTitle() {
        return this.externalTitle;
    }

    @Override
    @SuppressWarnings("unchecked")
    public B externalTitle(final Component component) {
        this.externalTitle = component;
        return (B) this;
    }

    @Override
    public boolean canCloseWithEscape() {
        return this.canCloseWithEscape;
    }

    @Override
    @SuppressWarnings("unchecked")
    public B canCloseWithEscape(final boolean flag) {
        this.canCloseWithEscape = flag;
        return (B) this;
    }
}
