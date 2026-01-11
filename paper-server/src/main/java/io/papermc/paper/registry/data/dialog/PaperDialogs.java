package io.papermc.paper.registry.data.dialog;

import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.dialog.type.ConfirmationType;
import io.papermc.paper.registry.data.dialog.type.DialogListType;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import io.papermc.paper.registry.data.dialog.type.MultiActionType;
import io.papermc.paper.registry.data.dialog.type.NoticeType;
import io.papermc.paper.registry.data.dialog.type.ServerLinksType;
import io.papermc.paper.registry.data.util.Conversions;
import io.papermc.paper.registry.set.PaperRegistrySets;
import io.papermc.paper.registry.set.RegistrySet;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.dialog.CommonDialogData;
import net.minecraft.server.dialog.ConfirmationDialog;
import net.minecraft.server.dialog.Dialog;
import net.minecraft.server.dialog.DialogListDialog;
import net.minecraft.server.dialog.MultiActionDialog;
import net.minecraft.server.dialog.NoticeDialog;
import net.minecraft.server.dialog.ServerLinksDialog;

public final class PaperDialogs {

    private PaperDialogs() {
    }

    public static DialogType extractType(final Dialog nmsDialog, final Conversions conversions) {
        final Function<net.minecraft.server.dialog.ActionButton, ActionButton> convertButton = button -> conversions.convert(button, PaperDialogCodecs.ACTION_BUTTON_CODEC, net.minecraft.server.dialog.ActionButton.CODEC);
        return switch (nmsDialog) {
            case final ConfirmationDialog conf ->
                DialogType.confirmation(convertButton.apply(conf.yesButton()), convertButton.apply(conf.noButton()));
            case final DialogListDialog list -> {
                final RegistrySet<io.papermc.paper.dialog.Dialog> apiSet = PaperRegistrySets.convertToApiWithDirects(RegistryKey.DIALOG, list.dialogs());
                yield DialogType.dialogList(
                    apiSet,
                    list.exitAction().map(convertButton).orElse(null),
                    list.columns(),
                    list.buttonWidth()
                );
            }
            case final MultiActionDialog multi ->
                DialogType.multiAction(multi.actions().stream().map(convertButton).toList(), multi.exitAction().map(convertButton).orElse(null), multi.columns());
            case final NoticeDialog notice -> DialogType.notice(convertButton.apply(notice.action()));
            case final ServerLinksDialog links ->
                DialogType.serverLinks(links.exitAction().map(convertButton).orElse(null), links.columns(), links.buttonWidth());
            default -> throw new IllegalArgumentException("Unsupported dialog type: " + nmsDialog.getClass().getName());
        };
    }

    public static Dialog constructDialog(final DialogBase dialogBase, final DialogType dialogType, final Conversions conversions) {
        final Function<ActionButton, net.minecraft.server.dialog.ActionButton> convertButton = button -> conversions.convert(button, net.minecraft.server.dialog.ActionButton.CODEC, PaperDialogCodecs.ACTION_BUTTON_CODEC);
        final CommonDialogData common = conversions.convert(dialogBase, CommonDialogData.MAP_CODEC.codec(), PaperDialogCodecs.DIALOG_BASE_CODEC);
        switch (dialogType) {
            case final ConfirmationType conf -> {
                return new ConfirmationDialog(common, convertButton.apply(conf.yesButton()), convertButton.apply(conf.noButton()));
            }
            case final DialogListType list -> {
                return new DialogListDialog(
                    common,
                    PaperRegistrySets.convertToNmsWithDirects(Registries.DIALOG, conversions.lookup(), list.dialogs()),
                    Optional.ofNullable(list.exitAction()).map(convertButton),
                    list.columns(),
                    list.buttonWidth()
                );
            }
            case final MultiActionType multi -> {
                return new MultiActionDialog(
                    common,
                    multi.actions().stream().map(convertButton).toList(),
                    Optional.ofNullable(multi.exitAction()).map(convertButton),
                    multi.columns()
                );
            }
            case final NoticeType notice -> {
                return new NoticeDialog(common, convertButton.apply(notice.action()));
            }
            case final ServerLinksType links -> {
                return new ServerLinksDialog(common, Optional.ofNullable(links.exitAction()).map(convertButton), links.columns(), links.buttonWidth());
            }
        }
    }
}
