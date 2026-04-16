package com.github.theredbrain.healthregenerationoverhaul.mixin.server;

import com.github.theredbrain.healthregenerationoverhaul.entity.HealthRegeneratingEntity;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerList.class)
public class PlayerManagerMixin {

	@Inject(method = "respawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;initInventoryMenu()V"))
	protected void healthregenerationoverhaul$respawn(ServerPlayer player, boolean alive, Entity.RemovalReason removalReason, CallbackInfoReturnable<ServerPlayer> cir, @Local(ordinal = 1) ServerPlayer serverPlayerEntity) {
		((HealthRegeneratingEntity) serverPlayerEntity).healthregenerationoverhaul$setApplyMaxHealth(true);
	}

}
