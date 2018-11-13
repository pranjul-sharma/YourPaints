package com.yourpaints.yourpaints;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;

import com.yourpaints.yourpaints.model.AppDatabase;
import com.yourpaints.yourpaints.model.Post;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class InfoWidgetProvider extends AppWidgetProvider {

    static int homeCount, reqCount, msgCount;

    static RemoteViews views;
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        views = new RemoteViews(context.getPackageName(), R.layout.info_widget_provider);

        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.my_prefs),Context.MODE_PRIVATE);
        views.setTextViewText(R.id.tt_home,String.valueOf(preferences.getInt(context.getString(R.string.home_count),0)));
        views.setTextViewText(R.id.tt_requests,String.valueOf(preferences.getInt(context.getString(R.string.req_count),0)));
        views.setTextViewText(R.id.tt_messages,String.valueOf(preferences.getInt(context.getString(R.string.message_count),0)));
        // Instruct the widget manager to update the widget
        Intent intent = new Intent(context, LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.tt_home,pendingIntent);
        views.setOnClickPendingIntent(R.id.tt_requests,pendingIntent);
        views.setOnClickPendingIntent(R.id.tt_messages,pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

