package fr.maxlego08.zitemstacker.zcore.enums;

public enum Permission {

    ZITEMSTACKER_USE, ZITEMSTACKER_RELOAD,

    ;

    private final String permission;

    Permission() {
        this.permission = this.name().toLowerCase().replace("_", ".");
    }

    public String getPermission() {
        return permission;
    }

}
