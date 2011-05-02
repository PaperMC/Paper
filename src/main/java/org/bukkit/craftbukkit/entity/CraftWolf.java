
package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityWolf;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import net.minecraft.server.PathEntity;

public class CraftWolf extends CraftAnimals implements Wolf {
    private AnimalTamer owner;

    public CraftWolf(CraftServer server, EntityWolf wolf) {
        super(server, wolf);
    }

    public boolean isAngry() {
        return getHandle().isAngry();
    }

    public void setAngry(boolean angry) {
        getHandle().setAngry(angry);
    }

    public boolean isSitting() {
        return getHandle().isSitting();
    }

    public void setSitting(boolean sitting) {
        getHandle().setSitting(sitting);
        // TODO determine what the following would do - it is affected every time a player makes their wolf sit or stand
        //getHandle().ay = false;
        setPath((PathEntity) null);
    }

    public boolean isTamed() {
        return getHandle().m_();
    }

    public void setTamed(boolean tame) {
        getHandle().d(tame);
    }

    public AnimalTamer getOwner() {
        // If the wolf has a previously set owner use that, otherwise try and find the player who owns it
        if (owner == null) {
            // TODO try and recover owner from persistence store before defaulting to playername
            owner = getServer().getPlayer(getOwnerName());
        }
        return owner;
    }

    public void setOwner(AnimalTamer tamer) {
        owner = tamer;
        if (owner != null) {
            setTamed(true); /* Make him tame */
            setPath((PathEntity) null); /* Clear path */
            /* Set owner */
            // TODO persist owner to the persistence store
            if (owner instanceof Player) {
                setOwnerName(((Player) owner).getName()); 
            } else {
                setOwnerName(""); 
            }
        } else {
            setTamed(false); /* Make him not tame */
            setOwnerName(""); /* Clear owner */
        }
    }

    /**
     * The owner's name is how MC knows and persists the Wolf's owner. Since we choose to instead use an AnimalTamer, this functionality
     * is used only as a backup. If the animal tamer is a player, we will store their name, otherwise we store an empty string.
     * @return the owner's name, if they are a player; otherwise, the empty string or null.
     */
    String getOwnerName() {
        return getHandle().x();
    }

    void setOwnerName(String ownerName) {
        getHandle().a(ownerName);
    }

    /**
     * Only used internally at the moment, and there to set the path to null (that is stop the thing from running around)
     * TODO use this later to extend the API, when we have Path classes in Bukkit
     * @param pathentity currently the MC defined PathEntity class. Should be replaced with an API interface at some point.
     */
    private void setPath(PathEntity pathentity) {
        getHandle().a(pathentity);
    }

    /*
     * This method requires a(boolean) to be made visible. It will allow for hearts to be animated on a successful taming.
     * TODO add this to the API, and make it visible
    private void playTamingAnimation(boolean successful){
        getHandle().a(successful);
    }
    */

    @Override
    public EntityWolf getHandle() {
        // It's somewhat easier to override this here, as many internal methods rely on EntityWolf specific methods.
        // Doing this has no impact on anything outside this class.
        return (EntityWolf) entity;
    }

    @Override
    public String toString() {
        return "CraftWolf[anger=" + isAngry() + ",owner=" + getOwner() + ",tame=" + isTamed() + ",sitting=" + isSitting() + "]";
    }
}
