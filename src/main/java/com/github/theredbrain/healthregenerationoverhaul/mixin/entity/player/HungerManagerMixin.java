package com.github.theredbrain.healthregenerationoverhaul.mixin.entity.player;

import com.github.theredbrain.healthregenerationoverhaul.HealthRegenerationOverhaul;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodData.class)
public class HungerManagerMixin {

	/**
	 * @author TheRedBrain
	 */
	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	public void update(ServerPlayer player, CallbackInfo ci) {
		if (HealthRegenerationOverhaul.SERVER_CONFIG.disable_vanilla_food_system) {
			ci.cancel();
		}
	}

	/**
	 * @author TheRedBrain
	 */
	@Inject(method = "addExhaustion", at = @At("HEAD"), cancellable = true)
	public void addExhaustion(float exhaustion, CallbackInfo ci) {
		if (HealthRegenerationOverhaul.SERVER_CONFIG.disable_vanilla_food_system) {
			ci.cancel();
		}
	}
}
