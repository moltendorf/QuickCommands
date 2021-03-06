package net.moltendorf.Bukkit.QuickCommands;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 * Created by moltendorf on 15/05/17.
 *
 * @author moltendorf
 */
public class Listeners implements Listener {
	@EventHandler(ignoreCancelled = true)
	public void PlayerGameModeChangeEventHandler(final PlayerGameModeChangeEvent event) {
		if (Settings.getInstance().isCreativeInventory()) {
			final Player player = event.getPlayer();
			final GameMode gameMode = player.getGameMode();
			final GameMode newGameMode = event.getNewGameMode();

			if (gameMode != GameMode.CREATIVE && newGameMode == GameMode.CREATIVE) {
				PlayerBackupManager.backup(player, true);
			} else if (gameMode == GameMode.CREATIVE && newGameMode != GameMode.CREATIVE) {
				PlayerBackupManager.restore(player);
			}
		}
	}

	@EventHandler
	public void PlayerJoinEventHandler(final PlayerJoinEvent event) {
		if (Settings.getInstance().isCreativeInventory()) {
			final Player player = event.getPlayer();

			if (player.getGameMode() == GameMode.CREATIVE) {
				PlayerBackupManager.backup(player, true);
			}
		}
	}

	@EventHandler
	public void PlayerQuitEventHandler(final PlayerQuitEvent event) {
		PlayerBackupManager.remove(event.getPlayer());
		SpectatorManager.remove(event.getPlayer());
	}

	@EventHandler
	public void PlayerRespawnEventHandler(final PlayerRespawnEvent event) {
		if (!event.isBedSpawn()) {
			final World world = QuickCommands.getInstance().getServer().getWorld("world");
			final Location spawn = Settings.getInstance().getStorage().getSpawnForPlayer(event.getPlayer(), world);

			if (spawn != null) {
				event.setRespawnLocation(spawn);
			}
		}
	}
}
