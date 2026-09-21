package com.posatasa.aoa3fixes.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.ModList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Shared logic for restoring vanilla's entity-interaction cap, which AoA 3.6.11 removes globally.
 *
 * <p>Vanilla {@code GameRenderer#pick} contains</p>
 *
 * <pre>{@code
 * if (hasFarRange && distSq > 9.0D)            // 9.0 == VANILLA_ENTITY_REACH^2
 *     this.minecraft.hitResult = BlockRayTraceResult.miss(...);
 * }</pre>
 *
 * <p>AoA's {@code GameRendererMixin} replaces that {@code 9.0D} with {@code 100.0D} (10 blocks) and
 * does so unconditionally, for every player and every item. In Survival the guard therefore never
 * fires and entity targeting is limited only by the ray trace (4.5 blocks), which is the reported
 * "attack range is about a block longer" bug. In Creative the guard is not used at all, so AoA's
 * patch changes nothing there - which is why this code must leave the constant's value semantics
 * to vanilla rather than reimplementing the whole check.</p>
 *
 * <p>Both the vanilla value and AoA's replacement are corrected to</p>
 *
 * <pre>{@code
 * (VANILLA_ENTITY_REACH + extraReach)^2
 * }</pre>
 *
 * <p>where {@code extraReach} is however much the player's own reach attribute currently exceeds
 * its base value. A player with no reach bonus therefore gets vanilla's 3.0 block cap back, while
 * a greatblade (+1.5) or maul (+0.5) keeps exactly the extra reach AoA intended for it.</p>
 *
 * <p>This class is deliberately free of any Mixin code so that the two corrector mixins, which are
 * registered at different priorities, can both delegate here.</p>
 */
public final class EntityReachCap {
	/** Vanilla's entity-interaction range, in blocks. */
	public static final double VANILLA_ENTITY_REACH = 3.0D;
	/** The constant vanilla pushes into the bytecode: {@code VANILLA_ENTITY_REACH} squared. */
	public static final double VANILLA_CONSTANT = 9.0D;
	/** The constant AoA 3.6.11 pushes instead (10 blocks squared). */
	public static final double AOA_CONSTANT = 100.0D;

	private static final Logger LOGGER = LogManager.getLogger("AoA3Fixes");
	private static boolean logged = false;

	private EntityReachCap() {}

	/**
	 * @param received the constant currently present in {@code GameRenderer#pick}, i.e. either
	 *                 {@link #VANILLA_CONSTANT} or {@link #AOA_CONSTANT}
	 * @return the constant that should be present instead
	 */
	public static double correct(double received) {
		Minecraft minecraft = Minecraft.getInstance();
		PlayerEntity player = minecraft == null ? null : minecraft.player;

		// Leave everything untouched unless AoA is actually loaded, so this mod is inert in a
		// vanilla or otherwise modded game.
		if (player == null || !ModList.get().isLoaded("aoa3"))
			return received;

		ModifiableAttributeInstance reach = player.getAttribute(ForgeMod.REACH_DISTANCE.get());

		if (reach == null)
			return received;

		// getBaseValue() is the unmodified attribute value, so the difference is the "extra" reach
		// from held items (greatblades/mauls) or abilities. Using the difference rather than AoA's
		// modifier UUID keeps this independent of AoA's internal identifiers.
		double extraReach = Math.max(0.0D, reach.getValue() - reach.getBaseValue());
		double entityReach = VANILLA_ENTITY_REACH + extraReach;
		double corrected = entityReach * entityReach;

		if (!logged) {
			logged = true;

			LOGGER.info("AoA3 entity reach cap corrector active (found constant {}, using {} for {} blocks).",
					received, corrected, entityReach);
		}

		return corrected;
	}
}
