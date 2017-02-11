package tr.name.fatihdogan.filesshortcut;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import static tr.name.fatihdogan.filesshortcut.MainActivity.ACTION_BROWSE;
import static tr.name.fatihdogan.filesshortcut.MainActivity.EXTRA_FANCY_FEATURES;
import static tr.name.fatihdogan.filesshortcut.MainActivity.EXTRA_SHOW_ADVANCED;
import static tr.name.fatihdogan.filesshortcut.MainActivity.EXTRA_SHOW_FILESIZE;
import static tr.name.fatihdogan.filesshortcut.MainActivity.MIME_TYPE_ITEM;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link ShortcutWidgetConfigureActivity ShortcutWidgetConfigureActivity}
 */
public class ShortcutWidget extends AppWidgetProvider {


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = ShortcutWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.shortcut_widget);

        Intent intent = new Intent(ACTION_BROWSE);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setData(Uri.parse(widgetText.toString()));
        intent.putExtra(EXTRA_SHOW_ADVANCED, true);
        intent.putExtra(EXTRA_FANCY_FEATURES, true);
        intent.setDataAndType(Uri.parse(widgetText.toString()), MIME_TYPE_ITEM);
        intent.putExtra(EXTRA_SHOW_FILESIZE, true);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        views.setOnClickPendingIntent(R.id.appwidget_button, pendingIntent);
        // Instruct the widget manager to update the widget
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
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            ShortcutWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
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

