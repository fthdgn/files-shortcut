package tr.name.fatihdogan.filesshortcut;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.view.View;
import android.widget.EditText;

import static tr.name.fatihdogan.filesshortcut.MainActivity.EXTRA_FANCY_FEATURES;
import static tr.name.fatihdogan.filesshortcut.MainActivity.EXTRA_SHOW_ADVANCED;
import static tr.name.fatihdogan.filesshortcut.MainActivity.EXTRA_SHOW_FILESIZE;

public class ShortcutWidgetConfigureActivity extends Activity {
    private static final int OPEN_DIRECTORY_REQUEST_CODE = 10;

    private static final String PREFS_NAME = "tr.name.fatihdogan.filesshortcut.ShortcutWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    public ShortcutWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String uriString) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, uriString);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return "";
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        Intent selectIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        selectIntent.putExtra(EXTRA_SHOW_ADVANCED, true);
        selectIntent.putExtra(EXTRA_FANCY_FEATURES, true);
        selectIntent.putExtra(EXTRA_SHOW_FILESIZE, true);
        startActivityForResult(selectIntent, OPEN_DIRECTORY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK || requestCode != OPEN_DIRECTORY_REQUEST_CODE || data == null) {
            setResult(RESULT_CANCELED);
            finish();
        } else {
            // When the button is clicked, store the string locally
            String widgetText = data.getData().toString();
            saveTitlePref(this, mAppWidgetId, widgetText);

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            ShortcutWidget.updateAppWidget(this, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    }
}

