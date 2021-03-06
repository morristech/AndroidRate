/*
 * Copyright 2017 - 2018 Vorlonsoft LLC
 *
 * Licensed under The MIT License (MIT)
 */

package com.vorlonsoft.android.rate;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import static com.vorlonsoft.android.rate.Constants.Utils.TAG;
import static com.vorlonsoft.android.rate.IntentHelper.createIntentsForStore;
import static com.vorlonsoft.android.rate.PreferenceHelper.getDialogFirstLaunchTime;
import static com.vorlonsoft.android.rate.PreferenceHelper.increment365DayPeriodDialogLaunchTimes;
import static com.vorlonsoft.android.rate.PreferenceHelper.setDialogFirstLaunchTime;
import static com.vorlonsoft.android.rate.PreferenceHelper.setIsAgreeShowDialog;
import static com.vorlonsoft.android.rate.PreferenceHelper.setRemindInterval;
import static com.vorlonsoft.android.rate.StoreType.AMAZON;
import static com.vorlonsoft.android.rate.StoreType.APPLE;
import static com.vorlonsoft.android.rate.StoreType.BAZAAR;
import static com.vorlonsoft.android.rate.StoreType.BLACKBERRY;
import static com.vorlonsoft.android.rate.StoreType.CHINESESTORES;
import static com.vorlonsoft.android.rate.StoreType.GOOGLEPLAY;
import static com.vorlonsoft.android.rate.StoreType.INTENT;
import static com.vorlonsoft.android.rate.StoreType.MI;
import static com.vorlonsoft.android.rate.StoreType.OTHER;
import static com.vorlonsoft.android.rate.StoreType.SAMSUNG;
import static com.vorlonsoft.android.rate.StoreType.SLIDEME;
import static com.vorlonsoft.android.rate.StoreType.TENCENT;
import static com.vorlonsoft.android.rate.StoreType.YANDEX;
import static com.vorlonsoft.android.rate.Utils.getDialogBuilder;
import static com.vorlonsoft.android.rate.Utils.isLollipop;

/**
 * <p>DefaultDialogManager Class - default dialog manager class implements
 * DialogManager interface of the AndroidRate library. You can extends it and use
 * {@code AppRate.with(this).setDialogManagerFactory(DialogManager.Factory)]}
 * if you want to use fully custom dialog (from support library etc.).
 * DefaultDialogManager Class is thread-safe and a fast singleton implementation
 * inside library, not outside (protected, not private constructor).</p>
 *
 * @since    1.0.2
 * @version  1.1.9
 * @author   Alexander Savin
 * @author   Antoine Vianey
 */

public class DefaultDialogManager implements DialogManager {
    @SuppressLint("StaticFieldLeak")
    private static volatile DefaultDialogManager singleton = null;
    private final Context context;
    private final DialogOptions dialogOptions;
    private final StoreOptions storeOptions;
    private final OnClickButtonListener listener;
    @SuppressWarnings("WeakerAccess")
    protected final DialogInterface.OnShowListener showListener = new DialogInterface.OnShowListener() {
        @Override
        public void onShow(DialogInterface dialog) {
            if (getDialogFirstLaunchTime(context) == 0L) {
                setDialogFirstLaunchTime(context);
            }
            increment365DayPeriodDialogLaunchTimes(context);
            if (isLollipop()) {
                try {
                    final Button positiveButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                    if (positiveButton != null) {
                        final LinearLayout linearLayout = (LinearLayout) positiveButton.getParent();
                        if ((linearLayout != null) && (positiveButton.getLeft() + positiveButton.getWidth() > linearLayout.getWidth())) {
                            final Button neutralButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEUTRAL);
                            final Button negativeButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                            linearLayout.setOrientation(LinearLayout.VERTICAL);
                            linearLayout.setGravity(Gravity.END);
                            if (neutralButton != null) {
                                linearLayout.removeView(neutralButton);
                                if (negativeButton != null) {
                                    linearLayout.removeView(negativeButton);
                                    linearLayout.addView(negativeButton);
                                }
                                linearLayout.addView(neutralButton);
                            } else if (negativeButton != null) {
                                linearLayout.removeView(negativeButton);
                                linearLayout.addView(negativeButton);
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.i(TAG, "Positive button may not fits in the window, can't change layout orientation to vertical");
                }
            }
        }
    };
    @SuppressWarnings("WeakerAccess")
    protected final DialogInterface.OnDismissListener dismissListener = new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
            AppRate.with(context).clearDialog();
        }
    };
    @SuppressWarnings("WeakerAccess")
    protected final DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(final DialogInterface dialog, final int which) {
            final Intent[] intentsToAppStores;
            final String packageName = context.getPackageName();
            if ((packageName != null) && (packageName.hashCode() != "".hashCode())) {
                switch (storeOptions.getStoreType()) {
                    case AMAZON:
                        intentsToAppStores = createIntentsForStore(context, AMAZON, packageName);
                        break;
                    case APPLE:
                        intentsToAppStores = createIntentsForStore(context, APPLE, storeOptions.getApplicationId());
                        break;
                    case BAZAAR:
                        intentsToAppStores = createIntentsForStore(context, BAZAAR, packageName);
                        break;
                    case BLACKBERRY:
                        intentsToAppStores = createIntentsForStore(context, BLACKBERRY, storeOptions.getApplicationId());
                        break;
                    case CHINESESTORES:
                        intentsToAppStores = createIntentsForStore(context, CHINESESTORES, packageName);
                        break;
                    case MI:
                        intentsToAppStores = createIntentsForStore(context, MI, packageName);
                        break;
                    case SAMSUNG:
                        intentsToAppStores = createIntentsForStore(context, SAMSUNG, packageName);
                        break;
                    case SLIDEME:
                        intentsToAppStores = createIntentsForStore(context, SLIDEME, packageName);
                        break;
                    case TENCENT:
                        intentsToAppStores = createIntentsForStore(context, TENCENT, packageName);
                        break;
                    case YANDEX:
                        intentsToAppStores = createIntentsForStore(context, YANDEX, packageName);
                        break;
                    case INTENT:
                    case OTHER:
                        intentsToAppStores = storeOptions.getIntents();
                        break;
                    default:
                        intentsToAppStores = createIntentsForStore(context, GOOGLEPLAY, packageName);
                }
                if (intentsToAppStores == null) {
                    Log.w(TAG, "Failed to rate app, can't create intents for store");
                }
            } else {
                Log.w(TAG, "Failed to rate app, can't get app package name");
                intentsToAppStores = null;
            }
            try {
                if (intentsToAppStores != null) {
                    if (intentsToAppStores.length == 0) {
                        Log.w(TAG, "Failed to rate app, no intent found for startActivity (intentsToAppStores.length == 0)");
                    } else if (intentsToAppStores[0] == null) {
                        throw new ActivityNotFoundException("Failed to rate app, no intent found for startActivity (intentsToAppStores[0] == null)");
                    } else {
                        context.startActivity(intentsToAppStores[0]);
                    }
                }
            } catch (ActivityNotFoundException e) {
                Log.w(TAG, "Failed to rate app, no activity found for " + intentsToAppStores[0], e);
                byte intentsToAppStoresNumber = (byte) intentsToAppStores.length;
                if (intentsToAppStoresNumber > 1) {
                    boolean isCatch;
                    for (byte b = 1; b < intentsToAppStoresNumber; b++) { // intentsToAppStores[1] - second intent in the array
                        try {
                            if (intentsToAppStores[b] == null) {
                                throw new ActivityNotFoundException("Failed to rate app, no intent found for startActivity (intentsToAppStores[" + b + "] == null)");
                            } else {
                                context.startActivity(intentsToAppStores[b]);
                            }
                            isCatch = false;
                        } catch (ActivityNotFoundException ex) {
                            Log.w(TAG, "Failed to rate app, no activity found for " + intentsToAppStores[b], ex);
                            isCatch = true;
                        }
                        if (!isCatch) {
                            break;
                        }
                    }
                }
            }
            setIsAgreeShowDialog(context, false);
            if (listener != null) listener.onClickButton((byte) which);
        }
    };
    @SuppressWarnings("WeakerAccess")
    protected final DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(final DialogInterface dialog, final int which) {
            setIsAgreeShowDialog(context, false);
            if (listener != null) listener.onClickButton((byte) which);
        }
    };
    @SuppressWarnings("WeakerAccess")
    protected final DialogInterface.OnClickListener neutralListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(final DialogInterface dialog, final int which) {
            setRemindInterval(context);
            if (listener != null) listener.onClickButton((byte) which);
        }
    };

    @SuppressWarnings("WeakerAccess")
    protected DefaultDialogManager(final Context context, final DialogOptions dialogOptions, final StoreOptions storeOptions) {
        this.context = context;
        this.dialogOptions = dialogOptions;
        this.storeOptions = storeOptions;
        this.listener = dialogOptions.getListener();
    }

    /**
     * Create rate dialog.
     *
     * @return created dialog
     */
    @Nullable
    @Override
    public Dialog createDialog() {
        AlertDialog.Builder builder = getDialogBuilder(context, dialogOptions.getThemeResId());

        if (builder == null) {
            return null;
        }

        builder.setMessage(dialogOptions.getMessageText(context));

        if (dialogOptions.shouldShowTitle()) {
            builder.setTitle(dialogOptions.getTitleText(context));
        }

        builder.setCancelable(dialogOptions.getCancelable());

        View view = dialogOptions.getView();
        if (view != null) {
            builder.setView(view);
        }

        builder.setPositiveButton(dialogOptions.getPositiveText(context), positiveListener);

        if (dialogOptions.shouldShowNeutralButton()) {
            builder.setNeutralButton(dialogOptions.getNeutralText(context), neutralListener);
        }

        if (dialogOptions.shouldShowNegativeButton()) {
            builder.setNegativeButton(dialogOptions.getNegativeText(context), negativeListener);
        }

        final AlertDialog alertDialog = builder.create();
        if (alertDialog != null) {
            alertDialog.setOnShowListener(showListener);
            alertDialog.setOnDismissListener(dismissListener);
        }

        return alertDialog;
    }

    /**
     * <p>DefaultDialogManager.Factory Class - default dialog manager factory class implements
     * DialogManager.Factory interface of the AndroidRate library. You can extends it
     * and use {@code AppRate.with(this).setDialogManagerFactory(DialogManager.Factory)]}
     * if you want to use fully custom dialog (from support library etc.)</p>
     *
     * @since    1.0.2
     * @version  1.1.9
     * @author   Alexander Savin
     * @author   Antoine Vianey
     */

    static class Factory implements DialogManager.Factory {

        Factory() {
            if (singleton != null) {
                singleton = null;
            }
        }

        /** Clear DefaultDialogManager singleton */
        @Override
        public void clearDialogManager() {
            if (singleton != null) {
                singleton = null;
            }
        }

        @Override
        public DialogManager createDialogManager(final Context context, final DialogOptions dialogOptions, final StoreOptions storeOptions) {
            if (singleton == null) {
                synchronized (DefaultDialogManager.class) {
                    if (singleton == null) {
                        singleton = new DefaultDialogManager(context, dialogOptions, storeOptions);
                    }
                }
            }
            return singleton;
        }
    }
}