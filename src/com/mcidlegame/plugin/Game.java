package com.mcidlegame.plugin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.Zombie;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.mcidlegame.plugin.enemy.EnemyUnit;
import com.mcidlegame.plugin.enemy.ZombieUnit;
import com.mcidlegame.plugin.friend.SnowmanUnit;

public class Game {

	private static LivingEntity monster = null;
	private static final List<LivingEntity> allies = new ArrayList<>();

	public static void startGame() {
		removeHealthbar();
		final World world = Bukkit.getWorld("world");
		for (final Entity entity : world.getEntities()) {
			if (!(entity instanceof Player)) {
				entity.remove();
			}
		}

		spawnMonster(new Location(world, 0.5, 66, 0.5), 1);

		final Snowman snowman = (Snowman) world.spawnEntity(new Location(world, 4.5, 66, 0.5), EntityType.SNOWMAN);
		snowman.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 30));
		snowman.setCollidable(false);
		snowman.setMetadata("allyUnit", new FixedMetadataValue(Main.main, new SnowmanUnit(1)));
		allies.add(snowman);

	}

	public static void spawnMonster(final Location location, final int level) {
		if (monster != null) {
			return;
		}
		final Zombie zombie = (Zombie) location.getWorld().spawnEntity(location, EntityType.ZOMBIE);
		zombie.setAI(false);
		zombie.setMetadata("enemyUnit", new FixedMetadataValue(Main.main, new ZombieUnit(zombie, level)));
		monster = zombie;
		for (final LivingEntity ally : allies) {
			if (ally instanceof Creature) {
				((Creature) ally).setTarget(monster);
			}
		}
	}

	public static void checkDeath(final Entity entity) {
		if (monster == entity) {
			monster = null;
		}
	}

	public static void removeHealthbar() {
		if (monster == null) {
			return;
		}

		EnemyUnit enemy = null;
		for (final MetadataValue value : monster.getMetadata("enemyUnit")) {
			if (value.getOwningPlugin() == Main.main) {
				enemy = (EnemyUnit) value.value();
			}
		}
		enemy.removeHealthbar();
	}

}
