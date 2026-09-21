package com.posatasa.aoa3fixes.mixin.client;

import com.posatasa.aoa3fixes.util.LocalPlayerUtil;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Fix for "Skills have been disabled by the server owner" appearing for the host of a
 * singleplayer / LAN world after any other player disconnects.
 *
 * <p>{@code MinecraftForge.EVENT_BUS} is a single, JVM-wide instance. When a world is hosted by
 * the integrated server (singleplayer + "Open to LAN"), a {@code PlayerLoggedOutEvent} is posted
 * for <em>every</em> player that disconnects &mdash; including remote players. AoA registers a
 * client-side listener for that event which unconditionally wipes the local player's client-side
 * skill/resource data, so the host's own skills vanish from the Advent GUI.</p>
 *
 * <p>We simply skip the reset unless the disconnecting player really is the player sitting at
 * this computer. (Upstream later fixed this the same way by moving to
 * {@code ClientPlayerNetworkEvent.LoggingOut}, which is client-only, but that change was never
 * backported to the frozen 1.16.5 branch.)</p>
 *
 * <p>{@code remap = false} because the mixin targets a mod class, not a Minecraft class.</p>
 */
@Pseudo
@Mixin(targets = "net.tslat.aoa3.client.event.ClientEventHandler", remap = false)
public abstract class MixinAoAClientEventHandler {
	@Inject(method = "onPlayerLogout", at = @At("HEAD"), cancellable = true, remap = false)
	private static void aoa3fixes$ignoreRemotePlayerLogouts(PlayerEvent.PlayerLoggedOutEvent ev, CallbackInfo ci) {
		if (!LocalPlayerUtil.isLocalPlayer(ev.getPlayer()))
			ci.cancel();
	}
}
