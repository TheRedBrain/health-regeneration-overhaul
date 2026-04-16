package com.github.theredbrain.healthregenerationoverhaul.mixin.client.gui;

import com.github.theredbrain.healthregenerationoverhaul.HealthRegenerationOverhaul;
import com.github.theredbrain.healthregenerationoverhaul.HealthRegenerationOverhaulClient;
import com.github.theredbrain.healthregenerationoverhaul.gui.hud.DuckGuiMixin;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(Gui.class)
public abstract class GuiMixin implements DuckGuiMixin {

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

	@WrapMethod(method = "extractHearts")
	private void healthregenerationoverhaul$wrap_extractHearts(GuiGraphicsExtractor graphics, Player player, int xLeft, int yLineBase, int healthRowHeight, int heartOffsetIndex, float maxHealth, int currentHealth, int oldHealth, int absorption, boolean blink, Operation<Void> original) {
		if (!HealthRegenerationOverhaulClient.CLIENT_CONFIG.enable_alternative_health_bar) {
			original.call(graphics, player, xLeft, yLineBase, healthRowHeight, heartOffsetIndex, maxHealth, currentHealth, oldHealth, absorption, blink);
		}
	}

	@WrapOperation(
			method = "extractPlayerHealth",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/Gui;getVehicleMaxHearts(Lnet/minecraft/world/entity/LivingEntity;)I"
			)
	)
	public int healthregenerationoverhaul$wrap_getVehicleMaxHearts(Gui instance, LivingEntity entity, Operation<Integer> original) {
		return HealthRegenerationOverhaul.SERVER_CONFIG.disable_vanilla_food_system ? 1 : original.call(instance, entity);
	}
}