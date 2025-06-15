package io.papermc.paper.registry.data.dialog;

import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.dialog.specialty.ConfirmationSpecialty;
import io.papermc.paper.registry.data.dialog.specialty.DialogListSpecialty;
import io.papermc.paper.registry.data.dialog.specialty.DialogSpecialty;
import io.papermc.paper.registry.data.dialog.specialty.MultiActionSpecialty;
import io.papermc.paper.registry.data.dialog.specialty.NoticeSpecialty;
import io.papermc.paper.registry.data.dialog.specialty.ServerLinksSpecialty;
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

    public static DialogSpecialty extractSpecialty(final Dialog nmsDialog, final Conversions conversions) {
        final Function<net.minecraft.server.dialog.ActionButton, ActionButton> convertButton = button -> conversions.convert(button, PaperDialogCodecs.ACTION_BUTTON_CODEC, net.minecraft.server.dialog.ActionButton.CODEC);
        return switch (nmsDialog) {
            case final ConfirmationDialog conf ->
                DialogSpecialty.confirmation(convertButton.apply(conf.yesButton()), convertButton.apply(conf.noButton()));
            case final DialogListDialog list -> {
                final RegistrySet<io.papermc.paper.dialog.Dialog> apiSet = PaperRegistrySets.convertToApiWithDirects(RegistryKey.DIALOG, list.dialogs());
                yield DialogSpecialty.dialogList(
                    apiSet,
                    list.exitAction().map(convertButton).orElse(null),
                    list.columns(),
                    list.buttonWidth()
                );
            }
            case final MultiActionDialog multi ->
                DialogSpecialty.multiAction(multi.actions().stream().map(convertButton).toList(), multi.exitAction().map(convertButton).orElse(null), multi.columns());
            case final NoticeDialog notice -> DialogSpecialty.notice(convertButton.apply(notice.action()));
            case final ServerLinksDialog links ->
                DialogSpecialty.serverLinks(links.exitAction().map(convertButton).orElse(null), links.columns(), links.buttonWidth());
            default -> throw new IllegalArgumentException("Unsupported dialog type: " + nmsDialog.getClass().getName());
        };
    }

    public static Dialog constructDialog(final DialogBase dialogBase, final DialogSpecialty dialogSpecialty, final Conversions conversions) {
        final Function<ActionButton, net.minecraft.server.dialog.ActionButton> convertButton = button -> conversions.convert(button, net.minecraft.server.dialog.ActionButton.CODEC, PaperDialogCodecs.ACTION_BUTTON_CODEC);
        final CommonDialogData common = conversions.convert(dialogBase, CommonDialogData.MAP_CODEC.codec(), PaperDialogCodecs.DIALOG_BASE_MAP_CODEC.codec());
        switch (dialogSpecialty) {
            case final ConfirmationSpecialty conf -> {
                return new ConfirmationDialog(common, conversions.convert(conf.yesButton(), net.minecraft.server.dialog.ActionButton.CODEC, PaperDialogCodecs.ACTION_BUTTON_CODEC), conversions.convert(conf.noButton(), net.minecraft.server.dialog.ActionButton.CODEC, PaperDialogCodecs.ACTION_BUTTON_CODEC));
            }
            case final DialogListSpecialty list -> {
                return new DialogListDialog(
                    common,
                    PaperRegistrySets.convertToNmsWithDirects(Registries.DIALOG, conversions.lookup(), list.dialogs()),
                    Optional.ofNullable(list.exitAction()).map(convertButton),
                    list.columns(),
                    list.buttonWidth()
                );
            }
            case final MultiActionSpecialty multi -> {
                return new MultiActionDialog(
                    common,
                    multi.actions().stream().map(convertButton).toList(),
                    Optional.ofNullable(multi.exitAction()).map(convertButton),
                    multi.columns()
                );
            }
            case final NoticeSpecialty notice -> {
                return new NoticeDialog(common, convertButton.apply(notice.action()));
            }
            case final ServerLinksSpecialty links -> {
                return new ServerLinksDialog(common, Optional.ofNullable(links.exitAction()).map(convertButton), links.columns(), links.buttonWidth());
            }
        }
    }
}
