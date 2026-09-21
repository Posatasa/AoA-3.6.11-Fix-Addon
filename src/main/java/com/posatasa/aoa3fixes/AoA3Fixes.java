package com.posatasa.aoa3fixes;

import net.minecraftforge.fml.common.Mod;

/**
 * Client-side patch add-on for Advent of Ascension 3 (1.16.5-3.6.11).
 *
 * <p>This mod ships no Advent of Ascension code or assets. It only rewrites a couple of
 * instructions at class-load time (via Mixin) to fix two long-standing bugs.</p>
 */
@Mod(AoA3Fixes.MOD_ID)
public class AoA3Fixes {
	public static final String MOD_ID = "aoa3fixes";

	public AoA3Fixes() {
		// Mixins do all the work; nothing to register.
	}
}
