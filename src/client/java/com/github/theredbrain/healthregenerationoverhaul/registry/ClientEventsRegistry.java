package com.github.theredbrain.healthregenerationoverhaul.registry;

import com.github.theredbrain.healthregenerationoverhaul.HealthRegenerationOverhaul;
import com.github.theredbrain.healthregenerationoverhaul.HealthRegenerationOverhaulClient;
import com.github.theredbrain.healthregenerationoverhaul.config.ClientConfig;
import com.github.theredbrain.healthregenerationoverhaul.entity.HealthRegeneratingEntity;
import com.github.theredbrain.healthregenerationoverhaul.gui.hud.DuckInGameHudMixin;
import com.github.theredbrain.resourcebarapi.ResourceBarAPI;
import com.github.theredbrain.resourcebarapi.ResourceBarAPIClient;
import me.fzzyhmstrs.fzzy_config.api.ConfigApi;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.ArrayList;

public class ClientEventsRegistry {
	private static final String RESOURCE_BAR_IDENTIFIER_STRING = HealthRegenerationOverhaul.MOD_ID + ":health";
	private static final Identifier ICON_HEALTH_CONTAINER = Identifier.withDefaultNamespace("hud/heart/container");
	private static final Identifier ICON_HEALTH_CONTAINER_BLINKING = Identifier.withDefaultNamespace("hud/heart/container_blinking");
	private static final Identifier ICON_HEALTH_CONTAINER_HARDCORE = Identifier.withDefaultNamespace("hud/heart/container_hardcore");
	private static final Identifier ICON_HEALTH_CONTAINER_HARDCORE_BLINKING = Identifier.withDefaultNamespace("hud/heart/container_hardcore_blinking");
	private static final Identifier ICON_HEALTH_FULL = Identifier.withDefaultNamespace("hud/heart/full");
	private static final Identifier ICON_HEALTH_FULL_BLINKING = Identifier.withDefaultNamespace("hud/heart/full_blinking");
	private static final Identifier ICON_HEALTH_HALF = Identifier.withDefaultNamespace("hud/heart/half");
	private static final Identifier ICON_HEALTH_HALF_BLINKING = Identifier.withDefaultNamespace("hud/heart/half_blinking");
	private static final Identifier ICON_HEALTH_FULL_POISONED = Identifier.withDefaultNamespace("hud/heart/poisoned_full");
	private static final Identifier ICON_HEALTH_FULL_POISONED_BLINKING = Identifier.withDefaultNamespace("hud/heart/poisoned_full_blinking");
	private static final Identifier ICON_HEALTH_HALF_POISONED = Identifier.withDefaultNamespace("hud/heart/poisoned_half");
	private static final Identifier ICON_HEALTH_HALF_POISONED_BLINKING = Identifier.withDefaultNamespace("hud/heart/poisoned_half_blinking");
	private static final Identifier ICON_HEALTH_FULL_WITHERED = Identifier.withDefaultNamespace("hud/heart/withered_full");
	private static final Identifier ICON_HEALTH_FULL_WITHERED_BLINKING = Identifier.withDefaultNamespace("hud/heart/withered_full_blinking");
	private static final Identifier ICON_HEALTH_HALF_WITHERED = Identifier.withDefaultNamespace("hud/heart/withered_half");
	private static final Identifier ICON_HEALTH_HALF_WITHERED_BLINKING = Identifier.withDefaultNamespace("hud/heart/withered_half_blinking");
	private static final Identifier ICON_HEALTH_FULL_FROZEN = Identifier.withDefaultNamespace("hud/heart/frozen_full");
	private static final Identifier ICON_HEALTH_FULL_FROZEN_BLINKING = Identifier.withDefaultNamespace("hud/heart/frozen_full_blinking");
	private static final Identifier ICON_HEALTH_HALF_FROZEN = Identifier.withDefaultNamespace("hud/heart/frozen_half");
	private static final Identifier ICON_HEALTH_HALF_FROZEN_BLINKING = Identifier.withDefaultNamespace("hud/heart/frozen_half_blinking");
	private static final Identifier ICON_HEALTH_FULL_HARDCORE = Identifier.withDefaultNamespace("hud/heart/hardcore_full");
	private static final Identifier ICON_HEALTH_FULL_HARDCORE_BLINKING = Identifier.withDefaultNamespace("hud/heart/hardcore_full_blinking");
	private static final Identifier ICON_HEALTH_HALF_HARDCORE = Identifier.withDefaultNamespace("hud/heart/hardcore_half");
	private static final Identifier ICON_HEALTH_HALF_HARDCORE_BLINKING = Identifier.withDefaultNamespace("hud/heart/hardcore_half_blinking");
	private static final Identifier ICON_HEALTH_FULL_POISONED_HARDCORE = Identifier.withDefaultNamespace("hud/heart/poisoned_hardcore_full");
	private static final Identifier ICON_HEALTH_FULL_POISONED_HARDCORE_BLINKING = Identifier.withDefaultNamespace("hud/heart/poisoned_hardcore_full_blinking");
	private static final Identifier ICON_HEALTH_HALF_POISONED_HARDCORE = Identifier.withDefaultNamespace("hud/heart/poisoned_hardcore_half");
	private static final Identifier ICON_HEALTH_HALF_POISONED_HARDCORE_BLINKING = Identifier.withDefaultNamespace("hud/heart/poisoned_hardcore_half_blinking");
	private static final Identifier ICON_HEALTH_FULL_WITHERED_HARDCORE = Identifier.withDefaultNamespace("hud/heart/withered_hardcore_full");
	private static final Identifier ICON_HEALTH_FULL_WITHERED_HARDCORE_BLINKING = Identifier.withDefaultNamespace("hud/heart/withered_hardcore_full_blinking");
	private static final Identifier ICON_HEALTH_HALF_WITHERED_HARDCORE = Identifier.withDefaultNamespace("hud/heart/withered_hardcore_half");
	private static final Identifier ICON_HEALTH_HALF_WITHERED_HARDCORE_BLINKING = Identifier.withDefaultNamespace("hud/heart/withered_hardcore_half_blinking");
	private static final Identifier ICON_HEALTH_FULL_FROZEN_HARDCORE = Identifier.withDefaultNamespace("hud/heart/frozen_hardcore_full");
	private static final Identifier ICON_HEALTH_FULL_FROZEN_HARDCORE_BLINKING = Identifier.withDefaultNamespace("hud/heart/frozen_hardcore_full_blinking");
	private static final Identifier ICON_HEALTH_HALF_FROZEN_HARDCORE = Identifier.withDefaultNamespace("hud/heart/frozen_hardcore_half");
	private static final Identifier ICON_HEALTH_HALF_FROZEN_HARDCORE_BLINKING = Identifier.withDefaultNamespace("hud/heart/frozen_hardcore_half_blinking");

	public static void initializeClientEvents() {
		HudElementRegistry.attachElementAfter(VanillaHudElements.HEALTH_BAR, HealthRegenerationOverhaul.identifier("health"), ((matrixStack, delta) -> {
			Minecraft minecraft = Minecraft.getInstance();
			LocalPlayer localPlayer = minecraft.player;
			ClientConfig clientConfig = HealthRegenerationOverhaulClient.CLIENT_CONFIG;

			if (localPlayer != null && !minecraft.options.hideGui && clientConfig.enable_alternative_health_bar) {
				double health = localPlayer.getHealth();
				double maxHealth = localPlayer.getMaxHealth();
				double unreservedHealth = Mth.ceil(((HealthRegeneratingEntity) localPlayer).healthregenerationoverhaul$getUnreservedHealth());

				if (!localPlayer.isCreative() && maxHealth > 0) {

					MutablePair<Integer, Integer> originPos = ResourceBarAPIClient.getOriginPos(matrixStack, clientConfig.origin);

					if (clientConfig.health_bar_display == ResourceBarAPI.ResourceBarDisplay.ICON && (health < maxHealth || clientConfig.show_full_health_bar)) {

						Identifier containerId;
						Identifier fullId;
						Identifier halfId;

						boolean blinking = ((DuckInGameHudMixin) minecraft.gui).healthregenerationoverhaul$getHeartJumpEndTick() > ((DuckInGameHudMixin) minecraft.gui).healthregenerationoverhaul$getTicks() && (((DuckInGameHudMixin) minecraft.gui).healthregenerationoverhaul$getHeartJumpEndTick() - ((DuckInGameHudMixin) minecraft.gui).healthregenerationoverhaul$getTicks()) / 3L % 2L == 1L;

						if (localPlayer.level().getLevelData().isHardcore()) {
							if (blinking) {
								containerId = ICON_HEALTH_CONTAINER_HARDCORE_BLINKING;
							} else {
								containerId = ICON_HEALTH_CONTAINER_HARDCORE;
							}
							if (localPlayer.hasEffect(MobEffects.POISON)) {
								if (blinking) {
									fullId = ICON_HEALTH_FULL_POISONED_HARDCORE_BLINKING;
									halfId = ICON_HEALTH_HALF_POISONED_HARDCORE_BLINKING;
								} else {
									fullId = ICON_HEALTH_FULL_POISONED_HARDCORE;
									halfId = ICON_HEALTH_HALF_POISONED_HARDCORE;
								}
							} else if (localPlayer.hasEffect(MobEffects.WITHER)) {
								if (blinking) {
									fullId = ICON_HEALTH_FULL_WITHERED_HARDCORE_BLINKING;
									halfId = ICON_HEALTH_HALF_WITHERED_HARDCORE_BLINKING;
								} else {
									fullId = ICON_HEALTH_FULL_WITHERED_HARDCORE;
									halfId = ICON_HEALTH_HALF_WITHERED_HARDCORE;
								}
							} else if (localPlayer.isFullyFrozen()) {
								if (blinking) {
									fullId = ICON_HEALTH_FULL_FROZEN_HARDCORE_BLINKING;
									halfId = ICON_HEALTH_HALF_FROZEN_HARDCORE_BLINKING;
								} else {
									fullId = ICON_HEALTH_FULL_FROZEN_HARDCORE;
									halfId = ICON_HEALTH_HALF_FROZEN_HARDCORE;
								}
							} else {
								if (blinking) {
									fullId = ICON_HEALTH_FULL_HARDCORE_BLINKING;
									halfId = ICON_HEALTH_HALF_HARDCORE_BLINKING;
								} else {
									fullId = ICON_HEALTH_FULL_HARDCORE;
									halfId = ICON_HEALTH_HALF_HARDCORE;
								}
							}
						} else {
							if (blinking) {
								containerId = ICON_HEALTH_CONTAINER_BLINKING;
							} else {
								containerId = ICON_HEALTH_CONTAINER;
							}
							if (localPlayer.hasEffect(MobEffects.POISON)) {
								if (blinking) {
									fullId = ICON_HEALTH_FULL_POISONED_BLINKING;
									halfId = ICON_HEALTH_HALF_POISONED_BLINKING;
								} else {
									fullId = ICON_HEALTH_FULL_POISONED;
									halfId = ICON_HEALTH_HALF_POISONED;
								}
							} else if (localPlayer.hasEffect(MobEffects.WITHER)) {
								if (blinking) {
									fullId = ICON_HEALTH_FULL_WITHERED_BLINKING;
									halfId = ICON_HEALTH_HALF_WITHERED_BLINKING;
								} else {
									fullId = ICON_HEALTH_FULL_WITHERED;
									halfId = ICON_HEALTH_HALF_WITHERED;
								}
							} else if (localPlayer.isFullyFrozen()) {
								if (blinking) {
									fullId = ICON_HEALTH_FULL_FROZEN_BLINKING;
									halfId = ICON_HEALTH_HALF_FROZEN_BLINKING;
								} else {
									fullId = ICON_HEALTH_FULL_FROZEN;
									halfId = ICON_HEALTH_HALF_FROZEN;
								}
							} else {
								if (blinking) {
									fullId = ICON_HEALTH_FULL_BLINKING;
									halfId = ICON_HEALTH_HALF_BLINKING;
								} else {
									fullId = ICON_HEALTH_FULL;
									halfId = ICON_HEALTH_HALF;
								}
							}
						}

						ResourceBarAPIClient.drawIconResourceBar(
								minecraft,
								matrixStack,
								RESOURCE_BAR_IDENTIFIER_STRING,
								health,
								maxHealth,
								containerId,
								fullId,
								halfId,
								new ArrayList<>(),
								new ArrayList<>(),// TODO reserved health, absorption
								originPos.getLeft(),
								originPos.getRight(),
								clientConfig.iconBarSettings.offset_x.get(),
								clientConfig.iconBarSettings.offset_y.get(),
								clientConfig.fill_direction,
								clientConfig.iconBarSettings.reverse_stack_direction.get(),
								clientConfig.iconBarSettings.max_icon_amount_per_bar.get()
						);
					} else if (clientConfig.health_bar_display == ResourceBarAPI.ResourceBarDisplay.SMOOTH && (health < maxHealth || clientConfig.show_full_health_bar)) {
						ResourceBarAPIClient.drawSmoothResourceBar(
								minecraft,
								matrixStack,
								RESOURCE_BAR_IDENTIFIER_STRING,
								new double[]{
										-1,
										-1,
										0,
										-91,
										-45,
										5,
										182,
										5,
										182,
										5,
										182,
										5,
										5,
										0,
										0
								},
								new Identifier[]{
										HealthRegenerationOverhaul.identifier("textures/gui/sprites/hud/horizontal_health_background.png"),
										HealthRegenerationOverhaul.identifier("textures/gui/sprites/hud/horizontal_health_progress_decrease_animation.png"),
										HealthRegenerationOverhaul.identifier("textures/gui/sprites/hud/horizontal_health_progress_increase_animation.png"),
										HealthRegenerationOverhaul.identifier("textures/gui/sprites/hud/horizontal_health_progress_increase_value.png"),
										HealthRegenerationOverhaul.identifier("textures/gui/sprites/hud/horizontal_health_progress.png"),
										HealthRegenerationOverhaul.identifier("textures/gui/sprites/hud/horizontal_health_reserved.png"),
										HealthRegenerationOverhaul.identifier("textures/gui/sprites/hud/horizontal_health_overlay.png"),
										null
								},
								health,
								maxHealth,
								Mth.ceil(((HealthRegeneratingEntity) localPlayer).healthregenerationoverhaul$getRegeneratedHealth()),
								unreservedHealth,
								originPos.getLeft(),
								originPos.getRight(),
								clientConfig.smoothBarSettings.positionSettings.offsets_x,
								clientConfig.smoothBarSettings.positionSettings.offsets_y,
								0,
								0,
								clientConfig.fill_direction,
								clientConfig.smoothBarSettings.textureSettings.backgroundTextureSettings.texture_heights,
								clientConfig.smoothBarSettings.textureSettings.backgroundTextureSettings.texture_widths,
								clientConfig.smoothBarSettings.textureSettings.backgroundTextureSettings.texture_ids,
								clientConfig.smoothBarSettings.textureSettings.progressTextureSettings.offset_x,
								clientConfig.smoothBarSettings.textureSettings.progressTextureSettings.offset_y,
								clientConfig.smoothBarSettings.textureSettings.progressTextureSettings.texture_heights,
								clientConfig.smoothBarSettings.textureSettings.progressTextureSettings.texture_widths,
								clientConfig.smoothBarSettings.textureSettings.progressTextureSettings.progress_decrease_animation_texture_ids,
								clientConfig.smoothBarSettings.textureSettings.progressTextureSettings.progress_increase_animation_texture_ids,
								clientConfig.smoothBarSettings.textureSettings.progressTextureSettings.progress_increase_value_texture_ids,
								clientConfig.smoothBarSettings.textureSettings.progressTextureSettings.progress_texture_ids,
								clientConfig.smoothBarSettings.textureSettings.reservedTextureSettings.offset_x,
								clientConfig.smoothBarSettings.textureSettings.reservedTextureSettings.offset_y,
								clientConfig.smoothBarSettings.textureSettings.reservedTextureSettings.texture_heights,
								clientConfig.smoothBarSettings.textureSettings.reservedTextureSettings.texture_widths,
								clientConfig.smoothBarSettings.textureSettings.reservedTextureSettings.texture_ids,
								clientConfig.smoothBarSettings.show_current_value_overlay,
								clientConfig.smoothBarSettings.textureSettings.overlayTextureSettings.offset_x,
								clientConfig.smoothBarSettings.textureSettings.overlayTextureSettings.offset_y,
								clientConfig.smoothBarSettings.textureSettings.overlayTextureSettings.texture_heights,
								clientConfig.smoothBarSettings.textureSettings.overlayTextureSettings.texture_widths,
								clientConfig.smoothBarSettings.textureSettings.overlayTextureSettings.texture_ids,
								clientConfig.smoothBarSettings.show_icon,
								clientConfig.smoothBarSettings.iconTextureSettings.offset_x,
								clientConfig.smoothBarSettings.iconTextureSettings.offset_y,
								clientConfig.smoothBarSettings.iconTextureSettings.texture_heights,
								clientConfig.smoothBarSettings.iconTextureSettings.texture_widths,
								clientConfig.smoothBarSettings.iconTextureSettings.texture_ids,
								clientConfig.smoothBarSettings.enable_smooth_animation,
								clientConfig.smoothBarSettings.animationSettings.animation_interval,
								clientConfig.smoothBarSettings.animationSettings.max_value_change_is_animated
						);
					}
					if (clientConfig.numberSettings.show_number && (health < maxHealth || clientConfig.show_full_health_bar)) {
						ResourceBarAPIClient.drawResourceNumber(
								minecraft,
								minecraft.font,
								matrixStack,
								RESOURCE_BAR_IDENTIFIER_STRING,
								health,
								maxHealth,
								unreservedHealth,
								originPos.getLeft(),
								originPos.getRight(),
								clientConfig.numberSettings.show_max_value,
								clientConfig.numberSettings.offset_x,
								clientConfig.numberSettings.offset_y,
								clientConfig.numberSettings.color.toInt()
						);
					}
				}
			}
		}));
		ConfigApi.event().onUpdateClient((identifier, config) -> {
			if (identifier.equals(Identifier.fromNamespaceAndPath(HealthRegenerationOverhaul.MOD_ID, "client"))) {
				ResourceBarAPIClient.clearCache(
						RESOURCE_BAR_IDENTIFIER_STRING,
						new double[]{
								-1,
								-1,
								0,
								-91,
								-45,
								5,
								182,
								5,
								182,
								5,
								182,
								5,
								5,
								0,
								0
						},
						new Identifier[]{
								HealthRegenerationOverhaul.identifier("textures/gui/sprites/hud/horizontal_health_background.png"),
								HealthRegenerationOverhaul.identifier("textures/gui/sprites/hud/horizontal_health_progress_decrease_animation.png"),
								HealthRegenerationOverhaul.identifier("textures/gui/sprites/hud/horizontal_health_progress_increase_animation.png"),
								HealthRegenerationOverhaul.identifier("textures/gui/sprites/hud/horizontal_health_progress_increase_value.png"),
								HealthRegenerationOverhaul.identifier("textures/gui/sprites/hud/horizontal_health_progress.png"),
								HealthRegenerationOverhaul.identifier("textures/gui/sprites/hud/horizontal_health_reserved.png"),
								HealthRegenerationOverhaul.identifier("textures/gui/sprites/hud/horizontal_health_overlay.png"),
								null
						}
				);
			}
		});
	}
}
