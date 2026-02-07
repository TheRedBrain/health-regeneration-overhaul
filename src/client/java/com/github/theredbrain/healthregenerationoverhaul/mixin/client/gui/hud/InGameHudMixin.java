package com.github.theredbrain.healthregenerationoverhaul.mixin.client.gui.hud;

import com.github.theredbrain.healthregenerationoverhaul.HealthRegenerationOverhaul;
import com.github.theredbrain.healthregenerationoverhaul.HealthRegenerationOverhaulClient;
import com.github.theredbrain.healthregenerationoverhaul.gui.hud.DuckInGameHudMixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin implements DuckInGameHudMixin {

	@Shadow
	private long heartJumpEndTick;

	@Shadow
	private int ticks;

	public long healthregenerationoverhaul$getHeartJumpEndTick() {
		return this.heartJumpEndTick;
	}

	public int healthregenerationoverhaul$getTicks() {
		return this.ticks;
	}

	@Inject(method = "renderHealthBar", at = @At("HEAD"), cancellable = true)
	private void healthregenerationoverhaul$renderHealthBar(DrawContext context, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo ci) {
		var clientConfig = HealthRegenerationOverhaulClient.CLIENT_CONFIG;
		if (clientConfig.enable_alternative_health_bar) {
			ci.cancel();
		}
	}

	@WrapOperation(
			method = "renderStatusBars",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/hud/InGameHud;getHeartCount(Lnet/minecraft/entity/LivingEntity;)I"
			)
	)
	public int healthregenerationoverhaul$wrap_getHeartCount(InGameHud instance, LivingEntity entity, Operation<Integer> original) {
		return HealthRegenerationOverhaul.SERVER_CONFIG.disable_vanilla_food_system ? 1 : original.call(instance, entity);
	}
}