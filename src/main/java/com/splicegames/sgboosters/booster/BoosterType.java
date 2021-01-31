package com.splicegames.sgboosters.booster;

public enum BoosterType {

    // Shop GUI Plus
    SHOP_GUI_PLUS_SELL,
    SHOP_GUI_PLUS_DISCOUNT,

    // Grief Prevention
    CROP_GROWTH,
    MOB_DROPS,

    // McMMO
    MCMMO_GAIN,

    // Vanilla
    EXPERIENCE_GAIN,
    BLOCK_BREAK,
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
