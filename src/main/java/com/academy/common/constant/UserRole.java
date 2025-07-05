package com.academy.common.constant;

public enum UserRole {

    STUDENT(1),
    REVIEWER(2),
    INSTRUCTOR(3),
    ADMIN(4);

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
}
