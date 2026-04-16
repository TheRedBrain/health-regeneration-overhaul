package com.github.theredbrain.healthregenerationoverhaul.mixin.server.network;

import com.github.theredbrain.healthregenerationoverhaul.entity.HealthRegeneratingEntity;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerEntityMixin extends Player implements HealthRegeneratingEntity {

	@Shadow
	public abstract ServerStatsCounter getStats();

	public ServerPlayerEntityMixin(Level world, GameProfile profile) {
		super(world, profile);
	}

	@Inject(method = "initInventoryMenu", at = @At("TAIL"))
	public void healthregenerationoverhaul$initInventoryMenu(CallbackInfo ci) {
		this.healthregenerationoverhaul$setApplyOldHealth(false);
		if (this.getStats().getValue(Stats.CUSTOM.get(Stats.LEAVE_GAME)) <= 0) {
			this.healthregenerationoverhaul$setApplyMaxHealth(true);
		}
	}

}
