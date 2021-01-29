package com.splicegames.sgboosters.booster;

public enum BoosterType {

    SELL,
    CROP_GROWTH,
    MOB_DROPS,
    MCMMO_GAIN,
    DISCOUNT,
    ;

    public static BoosterType getNullable(final String input) {
        BoosterType result = null;

        for (final BoosterType type : values()) {
            if (!type.name().equalsIgnoreCase(input))
                continue;

            result = type;
            break;
        }

        return result;
    }

}
