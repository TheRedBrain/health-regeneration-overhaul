package com.github.theredbrain.healthregenerationoverhaul.mixin.entity.attribute;

import com.github.theredbrain.healthregenerationoverhaul.HealthRegenerationOverhaul;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Attributes.class)
public class EntityAttributesMixin {
	static {
		HealthRegenerationOverhaul.HEALTH_REGENERATION = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, HealthRegenerationOverhaul.identifier("health_regeneration"), new RangedAttribute("attribute.name.health_regeneration", 0.0, 0.0, 1024.0).setSyncable(true));
		HealthRegenerationOverhaul.HEALTH_TICK_THRESHOLD = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, HealthRegenerationOverhaul.identifier("health_tick_threshold"), new RangedAttribute("attribute.name.health_tick_threshold", 100.0, 0.0, 1024.0).setSyncable(true));
		HealthRegenerationOverhaul.HEALTH_REGENERATION_DELAY_THRESHOLD = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, HealthRegenerationOverhaul.identifier("health_regeneration_delay_threshold"), new RangedAttribute("attribute.name.health_regeneration_delay_threshold", 100.0, 0.0, 1024.0).setSyncable(true));
		HealthRegenerationOverhaul.RESERVED_HEALTH = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, HealthRegenerationOverhaul.identifier("reserved_health"), new RangedAttribute("attribute.name.reserved_health", 0.0, 0.0, 100.0).setSyncable(true));
	}
}
