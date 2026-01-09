package io.papermc.papergsk;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

/**
 * Controls and enforces player gamemodes
 */
public class GamemodeController {
    
    public GamemodeController() {
    }
    
    public void forceSurvival(Player player) {
        player.setGameMode(GameMode.SURVIVAL);
    }
    
    public void forceSpectator(Player player) {
        player.setGameMode(GameMode.SPECTATOR);
    }
    
    public void enforceGamemode(Player player, GameMode gameMode) {
        player.setGameMode(gameMode);
    }
}
