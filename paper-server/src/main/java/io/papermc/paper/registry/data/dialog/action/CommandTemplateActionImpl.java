package io.papermc.paper.registry.data.dialog.action;

import net.minecraft.commands.functions.StringTemplate;

public record CommandTemplateActionImpl(String template) implements DialogAction.CommandTemplateAction {

    public CommandTemplateActionImpl {
        StringTemplate.fromString(template);
    }
}
