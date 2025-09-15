package com.github.theredbrain.healthregenerationoverhaul.mixin.server.network;

import com.github.theredbrain.healthregenerationoverhaul.entity.HealthRegeneratingEntity;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements HealthRegeneratingEntity {

	public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
		super(world, pos, yaw, gameProfile);
	}

	@Inject(method = "onSpawn", at = @At("TAIL"))
	public void healthregenerationoverhaul$onSpawn(CallbackInfo ci) {
		this.healthregenerationoverhaul$setApplyOldHealth(false);
	}

}
