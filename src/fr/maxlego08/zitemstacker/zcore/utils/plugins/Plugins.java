package fr.maxlego08.zitemstacker.zcore.utils.plugins;

public enum Plugins {

	VAULT("Vault"),
	ESSENTIALS("Essentials"),
	
	UPGRADEABLEHOPPER("UpgradeableHopper"),
	ZTRANSLATOR("zTranslator"),
	
	;

	private final String name;

	private Plugins(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

}
