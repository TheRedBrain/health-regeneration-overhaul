package com.github.theredbrain.healthregenerationoverhaul.mixin.entity.player;

import com.github.theredbrain.healthregenerationoverhaul.HealthRegenerationOverhaul;
import com.github.theredbrain.healthregenerationoverhaul.entity.HealthRegeneratingEntity;
import com.google.common.collect.HashMultimap;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.rule.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements HealthRegeneratingEntity {

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "createPlayerAttributes", at = @At("RETURN"))
	private static void healthregenerationoverhaul$createPlayerAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
		cir.getReturnValue()
				.add(EntityAttributes.MAX_HEALTH, 1.0)
				.add(HealthRegenerationOverhaul.HEALTH_TICK_THRESHOLD, 0.0)
				.add(HealthRegenerationOverhaul.HEALTH_REGENERATION_DELAY_THRESHOLD, 0.0)
		;
	}

	@Inject(method = "applyDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setHealth(F)V", shift = At.Shift.AFTER))
	protected void healthregenerationoverhaul$applyDamage(ServerWorld world, DamageSource source, float amount, CallbackInfo ci) {
		this.healthregenerationoverhaul$resetTickCounters();
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void healthregenerationoverhaul$tick(CallbackInfo ci) {
		if (!this.getEntityWorld().isClient()) {
			this.getAttributes().addTemporaryModifiers(getNaturalAttributeModifiers());
		}
	}

	@Unique
	private HashMultimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> getNaturalAttributeModifiers() {
		HashMultimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> hashMultimap = HashMultimap.create();
		hashMultimap.put(HealthRegenerationOverhaul.HEALTH_REGENERATION, new EntityAttributeModifier(HealthRegenerationOverhaul.identifier("natural_health_regeneration_modifier"), HealthRegenerationOverhaul.SERVER_CONFIG.naturalPlayerAttributeValues.natural_health_regeneration + (((ServerWorld) this.getEntityWorld()).getGameRules().getValue(GameRules.NATURAL_HEALTH_REGENERATION) ? 1.0 : 0.0), EntityAttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(EntityAttributes.MAX_HEALTH, new EntityAttributeModifier(HealthRegenerationOverhaul.identifier("natural_max_health_modifier"), HealthRegenerationOverhaul.SERVER_CONFIG.naturalPlayerAttributeValues.natural_max_health, EntityAttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(HealthRegenerationOverhaul.HEALTH_TICK_THRESHOLD, new EntityAttributeModifier(HealthRegenerationOverhaul.identifier("natural_health_tick_threshold_modifier"), HealthRegenerationOverhaul.SERVER_CONFIG.naturalPlayerAttributeValues.natural_health_regeneration_delay_threshold, EntityAttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(HealthRegenerationOverhaul.HEALTH_REGENERATION_DELAY_THRESHOLD, new EntityAttributeModifier(HealthRegenerationOverhaul.identifier("natural_health_regeneration_delay_threshold_modifier"), HealthRegenerationOverhaul.SERVER_CONFIG.naturalPlayerAttributeValues.natural_health_tick_threshold, EntityAttributeModifier.Operation.ADD_VALUE));
		hashMultimap.put(HealthRegenerationOverhaul.RESERVED_HEALTH, new EntityAttributeModifier(HealthRegenerationOverhaul.identifier("natural_reserved_health_modifier"), HealthRegenerationOverhaul.SERVER_CONFIG.naturalPlayerAttributeValues.natural_reserved_health, EntityAttributeModifier.Operation.ADD_VALUE));
		return hashMultimap;
	}
}
