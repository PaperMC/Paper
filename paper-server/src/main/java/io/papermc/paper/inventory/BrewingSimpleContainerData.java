package io.papermc.paper.inventory;

import net.minecraft.world.inventory.SimpleContainerData;

public class BrewingSimpleContainerData extends SimpleContainerData {

    public BrewingSimpleContainerData() {
        super(3);
        this.set(2, 400);
    }
}
