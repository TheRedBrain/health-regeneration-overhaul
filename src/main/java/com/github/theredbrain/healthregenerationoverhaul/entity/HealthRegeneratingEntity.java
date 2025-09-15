package com.github.theredbrain.healthregenerationoverhaul.entity;

public interface HealthRegeneratingEntity {
	int healthregenerationoverhaul$getHealthTickThreshold();

	float healthregenerationoverhaul$getRegeneratedHealth();

	float healthregenerationoverhaul$getHealthRegeneration();

	int healthregenerationoverhaul$getHealthRegenerationDelayThreshold();

	float healthregenerationoverhaul$getUnreservedHealth();

	float healthregenerationoverhaul$getReservedHealth();

	void healthregenerationoverhaul$resetTickCounters();

	void healthregenerationoverhaul$setApplyOldHealth(boolean applyOldHealth);

	void healthregenerationoverhaul$setApplyMaxHealth(boolean applyMaxHealth);
}