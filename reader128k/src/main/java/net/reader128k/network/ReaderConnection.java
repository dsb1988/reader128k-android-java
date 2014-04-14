package net.reader128k.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import de.tavendo.autobahn.WebSocketException;
import net.reader128k.domain.*;
import net.reader128k.interfaces.ReaderObservable;
import net.reader128k.interfaces.ReaderObserver;
import net.reader128k.util.AccountUtils;
import net.reader128k.util.Constants;
import net.reader128k.util.LogUtils;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static net.reader128k.util.LogUtils.LOGD;

public class ReaderConnection extends WSConnection implements ReaderObservable {
    private static final String TAG = LogUtils.makeLogTag(ReaderConnection.class);
    private List<ReaderObserver> mObservers = new ArrayList<ReaderObserver>();
    private BasicNameValuePair mReaderCookie = null;
    private Context mContext;

    public ReaderConnection(Context context) {
        mContext = context;
    }

    @Override
    protected void onConnect() {
        for (ReaderObserver observer : mObservers)
            observer.onConnect();
    }

    public void connect() throws WebSocketException {
        if (!AccountUtils.isAuthenticated(mContext))
            try {
                mReaderCookie = new DownloadReaderCookieTask().execute(Constants.READER_HOST).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        else
            mReaderCookie = AccountUtils.getSessionCookie(mContext);

        List<BasicNameValuePair> headers = new ArrayList<BasicNameValuePair>();
        headers.add(mReaderCookie);
        super.connect(Constants.READER_HOST, headers);
    }

    @WSAnswerHandler(method="user-info")
    public void onUserInfo(User user, Object userData) {
        LOGD(TAG, "onUserInfo: user = " + user.name);

        for (ReaderObserver observer : mObservers)
            observer.onUserInfo(user);
	}

	@WSAnswerHandler(method="tree")
    public void onTree(JsonNode node, Object userData) {
		List<TreeNode> tree = new ArrayList<TreeNode>();
		if (node != null) {
			for (JsonNode subnode : node) {
				try {
					tree.add(TreeNode.deserialize(subnode));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

        for (ReaderObserver observer : mObservers)
            observer.onTree(tree);
    }

    @WSAnswerHandler(method="posts")
    public void onPosts(PostsResponse postsResponse, Object userData) {
        for (ReaderObserver observer : mObservers)
            observer.onPosts(postsResponse.posts);
    }

    @WSAnswerHandler(method="login")
    public void onLogin(LoginResponse response, Object userData) throws WebSocketException {
        if (response.success)
            AccountUtils.setSessionCookie(mContext, mReaderCookie);

        for (ReaderObserver observer : mObservers)
            observer.onLogin(response);
    }

    @WSAnswerHandler(method="logout")
    public void onLogout(LogoutResponse response, Object userData) throws WebSocketException {
        AccountUtils.setSessionCookie(mContext, null);
        disconnect();
        connect();

        for (ReaderObserver observer : mObservers)
            observer.onLogout(response);
    }

    @Override
    public void attach(ReaderObserver observer) {
        mObservers.add(observer);
    }

    @Override
    public void detach(ReaderObserver observer) {
        mObservers.remove(observer);
    }

    private class DownloadReaderCookieTask extends AsyncTask<String, Void, BasicNameValuePair> {
        @Override
        protected BasicNameValuePair doInBackground(String... params) {
            BasicNameValuePair result = null;
            try {
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet("http://" + params[0]);
                client.execute(get);
                for (Cookie cookie : client.getCookieStore().getCookies()) {
                    if (cookie.getName().equals(Constants.READER_COOKIE_HUNCHENTOOT_SESSION))
                        result= new BasicNameValuePair("Cookie", cookie.getName() + "=" + cookie.getValue());
                }
            } catch (IOException e) {
                Log.d(Constants.TAG, "Cookie request failed, continue anonymous");
                e.printStackTrace();
            }

            return result;
        }
    }
}
