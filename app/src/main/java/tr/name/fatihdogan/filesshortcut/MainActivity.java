package tr.name.fatihdogan.filesshortcut;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

public class MainActivity extends Activity {

    /**
     * android.provider.DocumentsContract.EXTRA_SHOW_ADVANCED
     */
    private static final String EXTRA_SHOW_ADVANCED = "android.content.extra.SHOW_ADVANCED";

    /**
     * android.provider.DocumentsContract.EXTRA_SHOW_FILESIZE
     */
    private static final String EXTRA_SHOW_FILESIZE = "android.content.extra.SHOW_FILESIZE";

    /**
     * android.provider.DocumentsContract.EXTRA_FANCY_FEATURES
     */
    private static final String EXTRA_FANCY_FEATURES = "android.content.extra.FANCY";

    /**
     * android.provider.DocumentsContract.ACTION_BROWSE
     */
    private static final String ACTION_BROWSE = "android.provider.action.BROWSE";

    /**
     * android.os.storage.VolumeInfo.DOCUMENT_AUTHORITY
     */
    private static final String DOCUMENT_AUTHORITY = "com.android.externalstorage.documents";

    /**
     * android.provider.DocumentsContract.PATH_ROOT
     */
    private static final String PATH_ROOT = "root";

    /**
     * android.os.storage.VolumeInfo.DOCUMENT_ROOT_PRIMARY_EMULATED
     */
    private static final String DOCUMENT_ROOT_PRIMARY_EMULATED = "primary";

    /**
     * android.provider.DocumentsContract.Root.MIME_TYPE_ITEM
     */
    private static final String MIME_TYPE_ITEM = "vnd.android.document/root";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent();
        switch (Build.VERSION.SDK_INT) {
            case Build.VERSION_CODES.P: //28
            case Build.VERSION_CODES.O_MR1: //27
            case Build.VERSION_CODES.O: //26
                intent.setAction(Intent.ACTION_VIEW);
                break;
            default:
                intent.setAction(ACTION_BROWSE);
        }
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType(MIME_TYPE_ITEM);
        intent.setData(new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT)
                .authority(DOCUMENT_AUTHORITY).appendPath(PATH_ROOT).appendPath(DOCUMENT_ROOT_PRIMARY_EMULATED).build());
        intent.putExtra(EXTRA_SHOW_ADVANCED, true);
        intent.putExtra(EXTRA_FANCY_FEATURES, true);
        intent.putExtra(EXTRA_SHOW_FILESIZE, true);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
