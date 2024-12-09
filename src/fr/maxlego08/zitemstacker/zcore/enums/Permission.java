package fr.maxlego08.zitemstacker.zcore.enums;

public enum Permission {
	ZITEMSTACKER_RELOAD, ZITEMSTACKER_USE

	;

	private String permission;

	private Permission() {
		this.permission = this.name().toLowerCase().replace("_", ".");
	}

	public String getPermission() {
		return permission;
	}

}
