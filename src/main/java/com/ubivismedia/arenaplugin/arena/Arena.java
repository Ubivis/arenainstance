package com.ubivismedia.arenaplugin.arena;

public class Arena {
    private final String name;
    private boolean active;

    public Arena(String name) {
        this.name = name;
        this.active = false;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
