package com.posatasa.aoa3fixes.mixin.client;

import com.posatasa.aoa3fixes.util.EntityReachCap;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Slice;

/**
 * Second, identical corrector for the entity-reach constant in {@code GameRenderer#pick}.
 *
 * <p>This is the same fix as {@link MixinGameRenderer}, registered from a different mixin config
 * with a priority on the <em>other</em> side of AoA's. Because the Mixin framework's priority
 * ordering is not something we can rely on being one particular way, the two correctors are placed
 * so that one of them is guaranteed to be applied after AoA's rewrite, while the other covers the
 * opposite ordering. Both compute the same answer, so whichever runs last simply wins.</p>
 *
 * <p>Maintaining two copies is a little redundant, but it costs a handful of lines and removes a
 * failure mode that would otherwise be silent - and effectively impossible to debug in a built
 * game, where the value of the constant is not visible anywhere.</p>
 */
@Mixin(GameRenderer.class)
public abstract class MixinGameRendererEcho {
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
