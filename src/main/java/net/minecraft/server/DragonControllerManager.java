package net.minecraft.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
// CraftBukkit start
import org.bukkit.craftbukkit.entity.CraftEnderDragon;
import org.bukkit.event.entity.EnderDragonChangePhaseEvent;
// CraftBukkit end

public class DragonControllerManager {

    private static final Logger LOGGER = LogManager.getLogger();
    private final EntityEnderDragon enderDragon;
    private final IDragonController[] dragonControllers = new IDragonController[DragonControllerPhase.c()];
    private IDragonController currentDragonController;

    public DragonControllerManager(EntityEnderDragon entityenderdragon) {
        this.enderDragon = entityenderdragon;
        this.setControllerPhase(DragonControllerPhase.HOVER);
    }

    public void setControllerPhase(DragonControllerPhase<?> dragoncontrollerphase) {
        if (this.currentDragonController == null || dragoncontrollerphase != this.currentDragonController.getControllerPhase()) {
            if (this.currentDragonController != null) {
                this.currentDragonController.e();
            }

            // CraftBukkit start - Call EnderDragonChangePhaseEvent
            EnderDragonChangePhaseEvent event = new EnderDragonChangePhaseEvent(
                    (CraftEnderDragon) this.enderDragon.getBukkitEntity(),
                    (this.currentDragonController == null) ? null : CraftEnderDragon.getBukkitPhase(this.currentDragonController.getControllerPhase()),
                    CraftEnderDragon.getBukkitPhase(dragoncontrollerphase)
            );
            this.enderDragon.world.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return;
            }
            dragoncontrollerphase = CraftEnderDragon.getMinecraftPhase(event.getNewPhase());
            // CraftBukkit end

            this.currentDragonController = this.b(dragoncontrollerphase);
            if (!this.enderDragon.world.isClientSide) {
                this.enderDragon.getDataWatcher().set(EntityEnderDragon.PHASE, dragoncontrollerphase.b());
            }

            DragonControllerManager.LOGGER.debug("Dragon is now in phase {} on the {}", dragoncontrollerphase, this.enderDragon.world.isClientSide ? "client" : "server");
            this.currentDragonController.d();
        }
    }

    public IDragonController a() {
        return this.currentDragonController;
    }

    public <T extends IDragonController> T b(DragonControllerPhase<T> dragoncontrollerphase) {
        int i = dragoncontrollerphase.b();

        if (this.dragonControllers[i] == null) {
            this.dragonControllers[i] = dragoncontrollerphase.a(this.enderDragon);
        }

        return (T) this.dragonControllers[i]; // CraftBukkit - decompile error
    }
}
