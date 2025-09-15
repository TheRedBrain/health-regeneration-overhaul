package com.github.theredbrain.healthregenerationoverhaul.config;

import com.github.theredbrain.healthregenerationoverhaul.HealthRegenerationOverhaul;
import me.fzzyhmstrs.fzzy_config.annotations.ConvertFrom;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;

@ConvertFrom(fileName = "server.json5", folder = "healthregenerationoverhaul")
public class ServerConfig extends Config {

	public ServerConfig() {
		super(HealthRegenerationOverhaul.identifier("server"));
	}

	public boolean disable_vanilla_food_system = true;

	public NaturalPlayerAttributeValuesSection naturalPlayerAttributeValues = new NaturalPlayerAttributeValuesSection();

	public static class NaturalPlayerAttributeValuesSection extends ConfigSection {
		public float natural_health_regeneration = 0.0F;
		public float natural_max_health = 19.0F;
		public float natural_health_regeneration_delay_threshold = 100.0F;
		public float natural_health_tick_threshold = 100.0F;
		public float natural_reserved_health = 0.0F;
	}
}