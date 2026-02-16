package com.github.theredbrain.healthregenerationoverhaul.mixin.client.gui.hud;

import com.github.theredbrain.healthregenerationoverhaul.HealthRegenerationOverhaul;
import com.github.theredbrain.healthregenerationoverhaul.HealthRegenerationOverhaulClient;
import com.github.theredbrain.healthregenerationoverhaul.gui.hud.DuckInGameHudMixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Gui.class)
public abstract class InGameHudMixin implements DuckInGameHudMixin {

	@Shadow
	private long healthBlinkTime;

	@Shadow
	private int tickCount;

	public long healthregenerationoverhaul$getHeartJumpEndTick() {
		return this.healthBlinkTime;
	}

	public int healthregenerationoverhaul$getTicks() {
		return this.tickCount;
	}

	@Inject(method = "renderHearts", at = @At("HEAD"), cancellable = true)
	private void healthregenerationoverhaul$renderHealthBar(GuiGraphics context, Player player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo ci) {
		var clientConfig = HealthRegenerationOverhaulClient.CLIENT_CONFIG;
		if (clientConfig.enable_alternative_health_bar) {
			ci.cancel();
		}
	}

	@WrapOperation(
			method = "renderPlayerHealth",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/Gui;getVehicleMaxHearts(Lnet/minecraft/world/entity/LivingEntity;)I"
			)
	)
	public int healthregenerationoverhaul$wrap_getHeartCount(Gui instance, LivingEntity entity, Operation<Integer> original) {
		return HealthRegenerationOverhaul.SERVER_CONFIG.disable_vanilla_food_system ? 1 : original.call(instance, entity);
	}
}