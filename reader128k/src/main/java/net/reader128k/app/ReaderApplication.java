package net.reader128k.app;

import android.app.Application;
import android.widget.Toast;
import de.tavendo.autobahn.WebSocketException;
import net.reader128k.network.ReaderConnection;
import net.reader128k.util.Constants;
import net.reader128k.util.LogUtils;

import static net.reader128k.util.LogUtils.LOGD;
import static net.reader128k.util.LogUtils.LOGE;

public class ReaderApplication extends Application {
    private static final String TAG = LogUtils.makeLogTag(ReaderApplication.class);
    public ReaderConnection mConnection;

    @Override
    public void onCreate() {
        super.onCreate();
        mConnection = new ReaderConnection(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mConnection.disconnect();
    }
}
