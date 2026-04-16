package com.github.theredbrain.healthregenerationoverhaul.mixin.entity.player;

import com.github.theredbrain.healthregenerationoverhaul.HealthRegenerationOverhaul;
import com.github.theredbrain.healthregenerationoverhaul.entity.HealthRegeneratingEntity;
import com.google.common.collect.HashMultimap;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity implements HealthRegeneratingEntity {

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
		super(entityType, world);
	}

	@Inject(method = "createAttributes", at = @At("RETURN"))
	private static void healthregenerationoverhaul$createPlayerAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
		cir.getReturnValue()
				.add(Attributes.MAX_HEALTH, 1.0)
				.add(HealthRegenerationOverhaul.HEALTH_TICK_THRESHOLD, 0.0)
				.add(HealthRegenerationOverhaul.HEALTH_REGENERATION_DELAY_THRESHOLD, 0.0)
		;
	}

	@Inject(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;setHealth(F)V", shift = At.Shift.AFTER))
	protected void healthregenerationoverhaul$applyDamage(ServerLevel world, DamageSource source, float amount, CallbackInfo ci) {
		this.healthregenerationoverhaul$resetTickCounters();
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void healthregenerationoverhaul$tick(CallbackInfo ci) {
		if (!this.level().isClientSide()) {
			this.getAttributes().addTransientAttributeModifiers(getNaturalAttributeModifiers());
		}
	}

	@Unique
	private HashMultimap<Holder<Attribute>, AttributeModifier> getNaturalAttributeModifiers() {
		HashMultimap<Holder<Attribute>, AttributeModifier> hashMultimap = HashMultimap.create();
		hashMultimap.put(HealthRegenerationOverhaul.HEALTH_REGENERATION, new AttributeModifier(HealthRegenerationOverhaul.identifier("natural_health_regeneration_modifier"), HealthRegenerationOverhaul.SERVER_CONFIG.natural_player_attribute_values.natural_health_regeneration + (((ServerLevel) this.level()).getGameRules().get(GameRules.NATURAL_HEALTH_REGENERATION) ? 1.0 : 0.0), AttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(Attributes.MAX_HEALTH, new AttributeModifier(HealthRegenerationOverhaul.identifier("natural_max_health_modifier"), HealthRegenerationOverhaul.SERVER_CONFIG.natural_player_attribute_values.natural_max_health, AttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(HealthRegenerationOverhaul.HEALTH_TICK_THRESHOLD, new AttributeModifier(HealthRegenerationOverhaul.identifier("natural_health_tick_threshold_modifier"), HealthRegenerationOverhaul.SERVER_CONFIG.natural_player_attribute_values.natural_health_regeneration_delay_threshold, AttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(HealthRegenerationOverhaul.HEALTH_REGENERATION_DELAY_THRESHOLD, new AttributeModifier(HealthRegenerationOverhaul.identifier("natural_health_regeneration_delay_threshold_modifier"), HealthRegenerationOverhaul.SERVER_CONFIG.natural_player_attribute_values.natural_health_tick_threshold, AttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(HealthRegenerationOverhaul.RESERVED_HEALTH, new AttributeModifier(HealthRegenerationOverhaul.identifier("natural_reserved_health_modifier"), HealthRegenerationOverhaul.SERVER_CONFIG.natural_player_attribute_values.natural_reserved_health, AttributeModifier.Operation.ADD_VALUE));
		return hashMultimap;
	}
}
