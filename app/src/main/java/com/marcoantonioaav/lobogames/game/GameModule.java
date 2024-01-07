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

    public String getName() {
        switch (this) {
            case ALIGNMENT_OR_BLOCK:
                return "alinhamento ou bloqueio";
            case DISLOCATION:
                return "deslocamento";
            case POSITION:
                return "posicionamento";
            case CAPTURE:
                return "captura";
            case HUNT:
                return "caça";
            default:
                return "";
        }
    }


    public boolean isUndefined() {
        return this == UNDEFINED;
    }
}
