package com.academy.common.constant;

public enum UserRole {

    STUDENT(100),
    REVIEWER(200),
    INSTRUCTOR(300),

    INTERNAL(1000),

    ADMIN(Integer.MAX_VALUE);

    private final int level;

    UserRole(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public boolean hasAccessTo(UserRole requiredRole) {
        return this.level >= requiredRole.level;
    }

    public static UserRole from(String roleStr) {
        try {
            return UserRole.valueOf(roleStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role: " + roleStr);
        }
    }

    public static boolean isValidRole(String roleStr) {
        for (UserRole role : UserRole.values()) {
            if (role.name().equalsIgnoreCase(roleStr)) {
                return true;
            }
        }
        return false;
    }
}
