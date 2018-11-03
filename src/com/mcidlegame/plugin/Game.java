package com.mcidlegame.plugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.Zombie;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Game {

	private static BossBar healthbar = null;
	private static int health = 100;

	public static void spawnMonster() {
		if (healthbar != null) {
			healthbar.removeAll();
		}
		final World world = Bukkit.getWorld("world");
		for (final Entity entity : world.getEntities()) {
			if (!(entity instanceof Player)) {
				entity.remove();
			}
		}
		final Zombie zombie = (Zombie) world.spawnEntity(new Location(world, 0.5, 66, 0.5), EntityType.ZOMBIE);
		zombie.setAI(false);
		final Snowman snowman = (Snowman) world.spawnEntity(new Location(world, 4.5, 66, 0.5), EntityType.SNOWMAN);
		snowman.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 30));
		snowman.setTarget(zombie);

		healthbar = Bukkit.createBossBar("Zombie Health", BarColor.RED, BarStyle.SEGMENTED_10);
		for (final Player player : Bukkit.getOnlinePlayers()) {
			healthbar.addPlayer(player);
		}
	}

	public static void hit() {
		health = health == 10 ? 100 : health - 10;
		healthbar.setProgress(1.0 / 100 * health);
	}

}
