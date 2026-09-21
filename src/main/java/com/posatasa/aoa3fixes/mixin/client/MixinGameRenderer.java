package com.posatasa.aoa3fixes.mixin.client;

import com.posatasa.aoa3fixes.util.EntityReachCap;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Slice;

/**
 * Restores vanilla's 3 block entity-interaction cap, which AoA 3.6.11 removes unconditionally.
 * See {@link EntityReachCap} for the reasoning and the exact maths.
 *
 * <h2>Why there are two constant handlers</h2>
 *
 * <p>Vanilla pushes {@code 9.0D} (3.0 squared) into {@code GameRenderer#pick}. AoA's own mixin
 * rewrites that same instruction to {@code 100.0D}. Both mods are therefore competing for the same
 * bytecode, and which one ends up winning depends on the order the Mixin framework applies them
 * in - an order that is not reliably predictable across Mixin versions or configurations.</p>
 *
 * <p>Rather than betting on that order, this mixin corrects <em>whichever</em> of the two values it
 * happens to see, and {@code MixinGameRendererEcho} repeats the correction from a config registered
 * at a priority on the other side of AoA's. Whichever corrector runs last wins, and since both
 * compute the same answer the outcome is identical either way.</p>
 *
 * <p>{@code require = 0} on both handlers is essential: if the value we are looking for is not
 * present, that is an expected situation rather than an error, and without it a mismatch would
 * throw an {@code InjectionError} while the game is starting up.</p>
 */
@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {
	@ModifyConstant(
			method = "Lnet/minecraft/client/renderer/GameRenderer;pick(F)V",
			slice = @Slice(
					from = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/EntityRayTraceResult;getEntity()Lnet/minecraft/entity/Entity;"),
					to = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockRayTraceResult;miss(Lnet/minecraft/util/math/vector/Vector3d;Lnet/minecraft/util/Direction;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/math/BlockRayTraceResult;")
			),
			constant = @Constant(doubleValue = 9.0D),
			require = 0
	)
	private static double aoa3fixes$correctVanillaConstant(double receivedConstant) {
		return EntityReachCap.correct(receivedConstant);
	}

	@ModifyConstant(
			method = "Lnet/minecraft/client/renderer/GameRenderer;pick(F)V",
			slice = @Slice(
					from = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/EntityRayTraceResult;getEntity()Lnet/minecraft/entity/Entity;"),
					to = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockRayTraceResult;miss(Lnet/minecraft/util/math/vector/Vector3d;Lnet/minecraft/util/Direction;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/math/BlockRayTraceResult;")
			),
			constant = @Constant(doubleValue = 100.0D),
			require = 0
	)
	private static double aoa3fixes$correctAoAConstant(double receivedConstant) {
		return EntityReachCap.correct(receivedConstant);
	}
}
