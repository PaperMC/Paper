package io.papermc.paper.registry.data.dialog;

import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.dialog.specialty.DialogSpecialty;
import io.papermc.paper.registry.data.util.Conversions;
import net.minecraft.server.dialog.CommonDialogData;
import net.minecraft.server.dialog.Dialog;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgument;
import static io.papermc.paper.registry.data.util.Checks.asConfigured;

public class PaperDialogRegistryEntry implements DialogRegistryEntry {

    protected @Nullable DialogBase dialogBase;
    protected @Nullable DialogSpecialty dialogSpecialty;

    protected final Conversions conversions;

    public PaperDialogRegistryEntry(
        final Conversions conversions,
        final @Nullable Dialog internal
    ) {
        this.conversions = conversions;
        if (internal == null) return;

        final CommonDialogData common = internal.common();
        this.dialogBase = conversions.convert(common, PaperDialogCodecs.DIALOG_BASE_MAP_CODEC.codec(), CommonDialogData.MAP_CODEC.codec());

        this.dialogSpecialty = PaperDialogs.extractSpecialty(internal, conversions);
    }

    @Override
    public DialogBase dialogBase() {
        return asConfigured(this.dialogBase, "dialogBase");
    }

    @Override
    public DialogSpecialty dialogSpecialty() {
        return asConfigured(this.dialogSpecialty, "dialogSpecialty");
    }

    public static final class PaperBuilder extends PaperDialogRegistryEntry implements Builder, PaperRegistryBuilder<Dialog, io.papermc.paper.dialog.Dialog> {

        public PaperBuilder(final Conversions conversions, final @Nullable Dialog internal) {
            super(conversions, internal);
        }

        @Override
        public Builder dialogBase(final DialogBase dialogBase) {
            this.dialogBase = asArgument(dialogBase, "dialogBase");
            return this;
        }

        @Override
        public Builder dialogSpecialty(final DialogSpecialty dialogSpecialty) {
            this.dialogSpecialty = asArgument(dialogSpecialty, "dialogSpecialty");
            return this;
        }

        @Override
        public Dialog build() {
            return PaperDialogs.constructDialog(this.dialogBase(), this.dialogSpecialty(), this.conversions);
        }
    }
}
