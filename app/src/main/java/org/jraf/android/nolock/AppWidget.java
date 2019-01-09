package org.jraf.android.nolock;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build.VERSION;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

public class AppWidget extends AppWidgetProvider {
    private static final String a = ("NoLock/" + AppWidget.class.getSimpleName());

    public static void a(Context context, AppWidgetManager appWidgetManager, int i) {
        Log.d(a, "updateAppWidget appWidgetId=" + i);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), Integer.parseInt(VERSION.SDK) < 4 ? R.layout.appwidget_3 : R.layout.appwidget);
        boolean z = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("PREF_LOCKING", true);
        Resources resources = context.getResources();
        float f = 8.0f * resources.getDisplayMetrics().density;
        Paint paint = new Paint(1);
        paint.setColor(resources.getColor(R.color.bubble_dark_background));
        int dimensionPixelSize = resources.getDimensionPixelSize(R.dimen.appWidget_width);
        int dimensionPixelSize2 = resources.getDimensionPixelSize(R.dimen.appWidget_height);
        Bitmap createBitmap = Bitmap.createBitmap(dimensionPixelSize, dimensionPixelSize2, Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        float dimension = resources.getDimension(R.dimen.appWidget_textMarginBottom);
        float dimension2 = resources.getDimension(R.dimen.appWidget_textMarginLeftAndRight);
        Paint paint2 = new Paint(1);
        paint2.setShadowLayer(2.0f, 0.0f, 0.0f, -16777216);
        paint2.setColor(-1);
        paint2.setTextSize(resources.getDimension(R.dimen.appWidget_textSize));
        paint2.setTextScaleX(1.01f);
        String charSequence = context.getText(z ? R.string.locked : R.string.unlocked).toString();
        float measureText = paint2.measureText(charSequence);
        RectF rectF = new RectF();
        int dimensionPixelSize3 = resources.getDimensionPixelSize(R.dimen.appWidget_bubbleHeight);
        float dimension3 = ((float) (dimensionPixelSize2 - dimensionPixelSize3)) - resources.getDimension(R.dimen.appWidget_bubbleMarginBottom);
        measureText += 2.0f * dimension2;
        float f2 = (((float) dimensionPixelSize) - measureText) / 2.0f;
        rectF.set(f2, dimension3, measureText + f2, ((float) dimensionPixelSize3) + dimension3);
        canvas.drawRoundRect(rectF, f, f, paint);
        canvas.drawText(charSequence, dimension2 + f2, (((float) dimensionPixelSize3) + dimension3) - dimension, paint2);
        Bitmap decodeResource = z ? BitmapFactory.decodeResource(resources, R.drawable.lock) : BitmapFactory.decodeResource(resources, R.drawable.unlock);
        float dimension4 = resources.getDimension(R.dimen.appWidget_iconWidth);
        f = (((float) dimensionPixelSize) - dimension4) / 2.0f;
        float dimension5 = (dimension3 - resources.getDimension(R.dimen.appWidget_iconMarginBottom)) - dimension4;
        rectF.set(f, dimension5, dimension4 + f, dimension4 + dimension5);
        canvas.drawBitmap(decodeResource, null, rectF, new Paint(2));
        remoteViews.setImageViewBitmap(R.id.toggleButton, createBitmap);
        remoteViews.setOnClickPendingIntent(R.id.layout, PendingIntent.getBroadcast(context, 0, new Intent("org.jraf.android.nolock.ACTION_TOGGLE"), 134217728));
        appWidgetManager.updateAppWidget(i, remoteViews);
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] iArr) {
        Log.d(a, "onUpdate");
        for (int a : iArr) {
            a(context, appWidgetManager, a);
        }
    }
}
