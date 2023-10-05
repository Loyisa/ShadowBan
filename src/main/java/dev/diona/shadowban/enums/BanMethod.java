package dev.diona.shadowban.enums;

public enum BanMethod {
    FAKELAG, FUN, RANDOMBAN;

    public static BanMethod getByName(String name) {
        for (BanMethod method : BanMethod.values()) {
            if (method.name().equalsIgnoreCase(name)) {
                return method;
            }
        }
        return null;
    }
}
