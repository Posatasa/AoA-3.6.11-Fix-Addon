package com.posatasa.aoa3fixes.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;

import java.util.UUID;

/** Client-only helper for "is this entity the player sitting at this computer?" questions. */
public final class LocalPlayerUtil {
	private LocalPlayerUtil() {}

	public static boolean isLocalPlayer(PlayerEntity player) {
		if (player == null)
			return false;

		Minecraft mc = Minecraft.getInstance();

		if (mc == null || mc.getUser() == null || mc.getUser().getGameProfile() == null)
			return false;

		UUID localId = mc.getUser().getGameProfile().getId();

		return localId != null && localId.equals(player.getUUID());
	}
}
