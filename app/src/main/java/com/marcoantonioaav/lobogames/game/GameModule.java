package com.marcoantonioaav.lobogames.game;

public enum GameModule {
    ALIGNMENT_OR_BLOCK,
    DISLOCATION,
    POSITION,
    CAPTURE,
    HUNT,
    UNDEFINED;


    public static GameModule parse(String module) {
        if (module == null || module.isEmpty()) {
            return UNDEFINED;
        }
        return GameModule.valueOf(module.toUpperCase());
    }

    public boolean isUndefined() {
        return this == UNDEFINED;
    }
}
