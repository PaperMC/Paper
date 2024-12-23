package org.bukkit.material;

@Deprecated(forRemoval = true, since = "1.13")
public interface Openable {

    /**
     * Check to see if the door is open.
     *
     * @return true if the door has swung counterclockwise around its hinge.
     */
    boolean isOpen();

    /**
     * Configure this door to be either open or closed;
     *
     * @param isOpen True to open the door.
     */
    void setOpen(boolean isOpen);
}
