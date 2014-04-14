package net.reader128k.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;
import net.reader128k.R;
import net.reader128k.app.ReaderApplication;
import net.reader128k.domain.LoginResponse;
import net.reader128k.network.ReaderConnection;
import net.reader128k.util.Constants;
import net.reader128k.util.ReaderMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.tavendo.autobahn.WebSocketException;

import static net.reader128k.util.LogUtils.LOGE;
import static net.reader128k.util.LogUtils.makeLogTag;

public class LoginActivity extends BaseReaderActivity {
    private static final String TAG = makeLogTag(LoginActivity.class);
    private ReaderConnection mConnection;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_login);

        mConnection = ((ReaderApplication) getApplication()).mConnection;
        mConnection.attach(this);

        if (!mConnection.isConnected())
            try {
                mConnection.connect();
            } catch (WebSocketException e) {
                LOGE(TAG, "Connection to " + Constants.READER_HOST + " failed", e);
                Toast.makeText(this, "Websocket connection failed", Toast.LENGTH_SHORT).show();
            }
        }

    public void onLoginButtonClick(View v) {
        setProgressBarIndeterminateVisibility(true);
        String username = ((EditText) findViewById(R.id.username)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();

        List<String> errors = validateLoginParams(username, password);
        if (errors.size() > 0) {
            setProgressBarIndeterminateVisibility(false);
            String errorMessage = "";
            for (String error : errors)
                errorMessage +=  error + "\n";
            errorMessage = errorMessage.substring(0, errorMessage.lastIndexOf('\n'));
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        } else
            login(username, password);
    }

    private List<String> validateLoginParams(String username, String password) {
        List<String> validationErrors = new ArrayList<String>();
        Resources resources = getResources();
        if (username.isEmpty())
            validationErrors.add(resources.getString(R.string.error_username_empty));
        if (password.isEmpty())
            validationErrors.add(resources.getString(R.string.error_password_empty));

        return validationErrors;
    }

    private void login(String username, String password) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("username", username);
        params.put("password", password);
        mConnection.send(ReaderMethod.LOGIN, params, null);
    }

    @Override
    public void onLogin(LoginResponse response) {
        setProgressBarIndeterminateVisibility(false);
        if (response.success) {
            Intent intent = new Intent(this, PostsActivity.class);
            startActivity(intent);
            this.finish();
        } else {
            Toast.makeText(this, response.form_errors.get(0), Toast.LENGTH_SHORT).show();
        }
    }
}