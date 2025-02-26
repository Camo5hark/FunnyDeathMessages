package net.camo5hark.fdm;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class FDMListener implements Listener {
    private final FDMPlugin plugin;
    private final HashMap<UUID, String> killerMap = new HashMap<>();

    public FDMListener(FDMPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();

        if (!(entity instanceof Player)) {
            return;
        }

        Entity damager = event.getDamager();
        String killer = damager.getName();

        if (damager instanceof Projectile) {
            ProjectileSource shooter = ((Projectile) damager).getShooter();

            if (shooter != null) {
                if (shooter instanceof Entity) {
                    killer = ((Entity) shooter).getName();
                }
            }
        }

        killerMap.put(entity.getUniqueId(), killer);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        EntityDamageEvent cause = player.getLastDamageCause();

        if (cause == null) {
            return;
        }

        List<String> possibleMsgs = plugin.getConfig().getStringList("death-messages." + cause.getCause());
        int msgCount = possibleMsgs.size();

        if (msgCount <= 0) {
            return;
        }

        String cfgMsgColor = plugin.getConfig().getString("colors.message");
        String cfgPHColor = plugin.getConfig().getString("colors.placeholder");
        ChatColor msgColor = ChatColor.getByChar(cfgMsgColor == null ? "3" : cfgMsgColor);
        ChatColor phColor = ChatColor.getByChar(cfgPHColor == null ? "4" : cfgPHColor);
        String msg = msgColor + possibleMsgs.get(new Random().nextInt(msgCount));
        msg = msg.replaceAll("%PLAYER%", phColor + player.getName() + msgColor);

        String killer = killerMap.get(player.getUniqueId());

        if (killer != null) {
            msg = msg.replaceAll("%KILLER%", phColor + killer + msgColor);
        }

        event.setDeathMessage(msg);
    }
}
