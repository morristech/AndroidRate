/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import androidx.annotation.IntDef;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * <p>StoreType Class - store type class of the AndroidRate library.</p>
 *
 * @since    1.0.0
 * @version  1.1.9
 * @author   Alexander Savin
 * @author   Shintaro Katafuchi
 */

public class StoreType {
    /** Amazon Appstore */
    @SuppressWarnings("WeakerAccess")
    public static final int AMAZON = 0;
    /** Apple App Store */
    @SuppressWarnings("WeakerAccess")
    public static final int APPLE = 1;
    /** Cafe Bazaar */
    @SuppressWarnings("WeakerAccess")
    public static final int BAZAAR = 2;
    /** BlackBerry World */
    @SuppressWarnings("WeakerAccess")
    public static final int BLACKBERRY = 3;
    /** 19 chinese app stores */
    @SuppressWarnings("WeakerAccess")
    public static final int CHINESESTORES = 4;
    /** Google Play */
    @SuppressWarnings("WeakerAccess")
    public static final int GOOGLEPLAY = 5;
    /** Mi Appstore (Xiaomi Market) */
    @SuppressWarnings("WeakerAccess")
    public static final int MI = 6;
    /** Samsung Galaxy Apps */
    @SuppressWarnings("WeakerAccess")
    public static final int SAMSUNG = 7;
    /** SlideME Marketplace */
    @SuppressWarnings("WeakerAccess")
    public static final int SLIDEME = 8;
    /** Tencent App Store */
    @SuppressWarnings("WeakerAccess")
    public static final int TENCENT = 9;
    /** Yandex.Store */
    @SuppressWarnings("WeakerAccess")
    public static final int YANDEX = 10;
    /** Any custom intents */
    @SuppressWarnings("WeakerAccess")
    public static final int INTENT = 11;
    /** Any Other Store */
    @SuppressWarnings("WeakerAccess")
    public static final int OTHER = 12;

    private StoreType() {
        throw new AssertionError();
    }

    @SuppressWarnings("WeakerAccess")
    @Documented
    @Retention(SOURCE)
    @IntDef({
            AMAZON,
            BAZAAR,
            CHINESESTORES,
            GOOGLEPLAY,
            MI,
            SAMSUNG,
            SLIDEME,
            TENCENT,
            YANDEX
    })
    public @interface StoreWithoutApplicationId {
    }

    @SuppressWarnings("WeakerAccess")
    @Documented
    @Retention(SOURCE)
    @IntDef({
            APPLE,
            BLACKBERRY
    })
    public @interface StoreWithApplicationId {
    }

    @SuppressWarnings("WeakerAccess")
    @Documented
    @Retention(SOURCE)
    @IntDef({
            AMAZON,
            APPLE,
            BAZAAR,
            BLACKBERRY,
            CHINESESTORES,
            GOOGLEPLAY,
            INTENT,
            MI,
            OTHER,
            SAMSUNG,
            SLIDEME,
            TENCENT,
            YANDEX
    })
    public @interface AnyStoreType {
    }
}