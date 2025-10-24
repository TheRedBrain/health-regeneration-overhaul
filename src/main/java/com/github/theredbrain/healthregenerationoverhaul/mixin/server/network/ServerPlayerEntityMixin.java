package com.github.theredbrain.healthregenerationoverhaul.mixin.server.network;

import com.github.theredbrain.healthregenerationoverhaul.entity.HealthRegeneratingEntity;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.ServerStatHandler;
import net.minecraft.stat.Stats;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements HealthRegeneratingEntity {

	@Shadow
	public abstract ServerStatHandler getStatHandler();

	public ServerPlayerEntityMixin(World world, GameProfile profile) {
		super(world, profile);
	}

	@Inject(method = "onSpawn", at = @At("TAIL"))
	public void healthregenerationoverhaul$onSpawn(CallbackInfo ci) {
		this.healthregenerationoverhaul$setApplyOldHealth(false);
		if (this.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.LEAVE_GAME)) <= 0) {
			this.healthregenerationoverhaul$setApplyMaxHealth(true);
		}
	}

}
