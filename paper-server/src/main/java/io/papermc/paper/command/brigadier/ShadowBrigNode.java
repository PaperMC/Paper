package io.papermc.paper.command.brigadier;

import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;

import java.util.Collection;

public class ShadowBrigNode extends LiteralCommandNode<CommandSourceStack> {

    private final CommandNode<net.minecraft.commands.CommandSourceStack> handle;

    public ShadowBrigNode(CommandNode<net.minecraft.commands.CommandSourceStack> node) {
        super(node.getName(), context -> 0, (s) -> false, node.getRedirect() == null ? null : new ShadowBrigNode(node.getRedirect()), null, node.isFork());
        this.handle = node;
    }

    @Override
    public Collection<CommandNode<CommandSourceStack>> getChildren() {
        throw new UnsupportedOperationException("Cannot retrieve children from this node.");
    }

    @Override
    public CommandNode<CommandSourceStack> getChild(String name) {
        throw new UnsupportedOperationException("Cannot retrieve children from this node.");
    }

    @Override
    public void addChild(CommandNode<CommandSourceStack> node) {
        throw new UnsupportedOperationException("Cannot modify children for this node.");
    }

    public CommandNode<net.minecraft.commands.CommandSourceStack> getHandle() {
        return this.handle;
    }
}
