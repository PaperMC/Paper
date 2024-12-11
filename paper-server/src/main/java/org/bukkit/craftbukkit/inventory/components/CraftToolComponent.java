package org.bukkit.craftbukkit.inventory.components;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.Block;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.craftbukkit.block.CraftBlockType;
import org.bukkit.craftbukkit.inventory.SerializableMeta;
import org.bukkit.craftbukkit.tag.CraftBlockTag;
import org.bukkit.inventory.meta.components.ToolComponent;

@SerializableAs("Tool")
public final class CraftToolComponent implements ToolComponent {

    private Tool handle;

    public CraftToolComponent(Tool tool) {
        this.handle = tool;
    }

    public CraftToolComponent(CraftToolComponent tool) {
        this.handle = tool.handle;
    }

    public CraftToolComponent(Map<String, Object> map) {
        Float speed = SerializableMeta.getObject(Float.class, map, "default-mining-speed", false);
        Integer damage = SerializableMeta.getObject(Integer.class, map, "damage-per-block", false);

        ImmutableList.Builder<ToolRule> rules = ImmutableList.builder();
        Iterable<?> rawRuleList = SerializableMeta.getObject(Iterable.class, map, "rules", true);
        if (rawRuleList != null) {
            for (Object obj : rawRuleList) {
                Preconditions.checkArgument(obj instanceof ToolRule, "Object (%s) in rule list is not valid", obj.getClass());

                CraftToolRule rule = new CraftToolRule((ToolRule) obj);
                if (rule.handle.blocks().size() > 0) {
                    rules.add(rule);
                }
            }
        }

        this.handle = new Tool(rules.build().stream().map(CraftToolRule::new).map(CraftToolRule::getHandle).toList(), speed, damage);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("default-mining-speed", this.getDefaultMiningSpeed());
        result.put("damage-per-block", this.getDamagePerBlock());
        result.put("rules", this.getRules());
        return result;
    }

    public Tool getHandle() {
        return this.handle;
    }

    @Override
    public float getDefaultMiningSpeed() {
        return this.handle.defaultMiningSpeed();
    }

    @Override
    public void setDefaultMiningSpeed(float speed) {
        this.handle = new Tool(this.handle.rules(), speed, this.handle.damagePerBlock());
    }

    @Override
    public int getDamagePerBlock() {
        return this.handle.damagePerBlock();
    }

    @Override
    public void setDamagePerBlock(int damage) {
        Preconditions.checkArgument(damage >= 0, "damage must be >= 0, was %d", damage);
        this.handle = new Tool(this.handle.rules(), this.handle.defaultMiningSpeed(), damage);
    }

    @Override
    public List<ToolRule> getRules() {
        return this.handle.rules().stream().map(CraftToolRule::new).collect(Collectors.toList());
    }

    @Override
    public void setRules(List<ToolRule> rules) {
        Preconditions.checkArgument(rules != null, "rules must not be null");
        this.handle = new Tool(rules.stream().map(CraftToolRule::new).map(CraftToolRule::getHandle).toList(), this.handle.defaultMiningSpeed(), this.handle.damagePerBlock());
    }

    @Override
    public ToolRule addRule(Material block, Float speed, Boolean correctForDrops) {
        Preconditions.checkArgument(block != null, "block must not be null");
        Preconditions.checkArgument(block.isBlock(), "block must be a block type, given %s", block.getKey());

        Holder.Reference<Block> nmsBlock = CraftBlockType.bukkitToMinecraft(block).builtInRegistryHolder();
        return this.addRule(HolderSet.direct(nmsBlock), speed, correctForDrops);
    }

    @Override
    public ToolRule addRule(Collection<Material> blocks, Float speed, Boolean correctForDrops) {
        List<Holder.Reference<Block>> nmsBlocks = new ArrayList<>(blocks.size());

        for (Material material : blocks) {
            Preconditions.checkArgument(material.isBlock(), "blocks contains non-block type: %s", material.getKey());
            nmsBlocks.add(CraftBlockType.bukkitToMinecraft(material).builtInRegistryHolder());
        }

        return this.addRule(HolderSet.direct(nmsBlocks), speed, correctForDrops);
    }

    @Override
    public ToolRule addRule(Tag<Material> tag, Float speed, Boolean correctForDrops) {
        Preconditions.checkArgument(tag instanceof CraftBlockTag, "tag must be a block tag");
        return this.addRule(((CraftBlockTag) tag).getHandle(), speed, correctForDrops);
    }

    private ToolRule addRule(HolderSet<Block> blocks, Float speed, Boolean correctForDrops) {
        Tool.Rule rule = new Tool.Rule(blocks, Optional.ofNullable(speed), Optional.ofNullable(correctForDrops));

        List<Tool.Rule> rules = new ArrayList<>(this.handle.rules().size() + 1);
        rules.addAll(this.handle.rules());
        rules.add(rule);

        this.handle = new Tool(rules, this.handle.defaultMiningSpeed(), this.handle.damagePerBlock());
        return new CraftToolRule(rule);
    }

    @Override
    public boolean removeRule(ToolRule rule) {
        Preconditions.checkArgument(rule != null, "rule must not be null");

        List<Tool.Rule> rules = new ArrayList<>(this.handle.rules());
        boolean removed = rules.remove(((CraftToolRule) rule).handle);
        this.handle = new Tool(rules, this.handle.defaultMiningSpeed(), this.handle.damagePerBlock());

        return removed;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.handle);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final CraftToolComponent other = (CraftToolComponent) obj;
        return Objects.equals(this.handle, other.handle);
    }

    @Override
    public String toString() {
        return "CraftToolComponent{" + "handle=" + this.handle + '}';
    }

    @SerializableAs("ToolRule")
    public static class CraftToolRule implements ToolRule {

        private Tool.Rule handle;

        public CraftToolRule(Tool.Rule handle) {
            this.handle = handle;
        }

        public CraftToolRule(ToolRule bukkit) {
            Tool.Rule toCopy = ((CraftToolRule) bukkit).handle;
            this.handle = new Tool.Rule(toCopy.blocks(), toCopy.speed(), toCopy.correctForDrops());
        }

        public CraftToolRule(Map<String, Object> map) {
            Float speed = SerializableMeta.getObject(Float.class, map, "speed", true);
            Boolean correct = SerializableMeta.getObject(Boolean.class, map, "correct-for-drops", true);
            HolderSet<Block> blocks = CraftHolderUtil.parse(SerializableMeta.getObject(Object.class, map, "blocks", false), Registries.BLOCK, BuiltInRegistries.BLOCK);

            this.handle = new Tool.Rule(blocks, Optional.ofNullable(speed), Optional.ofNullable(correct));
        }

        @Override
        public Map<String, Object> serialize() {
            Map<String, Object> result = new LinkedHashMap<>();

            CraftHolderUtil.serialize(result, "blocks", this.handle.blocks());

            Float speed = this.getSpeed();
            if (speed != null) {
                result.put("speed", speed);
            }

            Boolean correct = this.isCorrectForDrops();
            if (correct != null) {
                result.put("correct-for-drops", correct);
            }

            return result;
        }

        public Tool.Rule getHandle() {
            return this.handle;
        }

        @Override
        public Collection<Material> getBlocks() {
            return this.handle.blocks().stream().map(Holder::value).map(CraftBlockType::minecraftToBukkit).collect(Collectors.toList());
        }

        @Override
        public void setBlocks(Material block) {
            Preconditions.checkArgument(block != null, "block must not be null");
            Preconditions.checkArgument(block.isBlock(), "block must be a block type, given %s", block.getKey());
            this.handle = new Tool.Rule(HolderSet.direct(CraftBlockType.bukkitToMinecraft(block).builtInRegistryHolder()), this.handle.speed(), this.handle.correctForDrops());
        }

        @Override
        public void setBlocks(Collection<Material> blocks) {
            Preconditions.checkArgument(blocks != null, "blocks must not be null");
            for (Material material : blocks) {
                Preconditions.checkArgument(material.isBlock(), "blocks contains non-block type: %s", material.getKey());
            }

            this.handle = new Tool.Rule(HolderSet.direct((List) blocks.stream().map(CraftBlockType::bukkitToMinecraft).map(Block::builtInRegistryHolder).collect(Collectors.toList())), this.handle.speed(), this.handle.correctForDrops());
        }

        @Override
        public void setBlocks(Tag<Material> tag) {
            Preconditions.checkArgument(tag instanceof CraftBlockTag, "tag must be a block tag");
            this.handle = new Tool.Rule(((CraftBlockTag) tag).getHandle(), this.handle.speed(), this.handle.correctForDrops());
        }

        @Override
        public Float getSpeed() {
            return this.handle.speed().orElse(null);
        }

        @Override
        public void setSpeed(Float speed) {
            this.handle = new Tool.Rule(this.handle.blocks(), Optional.ofNullable(speed), this.handle.correctForDrops());
        }

        @Override
        public Boolean isCorrectForDrops() {
            return this.handle.correctForDrops().orElse(null);
        }

        @Override
        public void setCorrectForDrops(Boolean correct) {
            this.handle = new Tool.Rule(this.handle.blocks(), this.handle.speed(), Optional.ofNullable(correct));
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 97 * hash + Objects.hashCode(this.handle);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (this.getClass() != obj.getClass()) {
                return false;
            }
            final CraftToolRule other = (CraftToolRule) obj;
            return Objects.equals(this.handle, other.handle);
        }

        @Override
        public String toString() {
            return "CraftToolRule{" + "handle=" + this.handle + '}';
        }
    }
}
