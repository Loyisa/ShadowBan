package cn.loyisa.shadowban.enums;

public enum Permissions {
    USE("shadowban.use");

    private final String permission;

    Permissions(String permission){
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
