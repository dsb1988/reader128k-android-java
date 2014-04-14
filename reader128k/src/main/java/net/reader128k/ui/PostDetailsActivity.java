package net.reader128k.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ShareActionProvider;
import net.reader128k.R;
import net.reader128k.util.Constants;

public class PostDetailsActivity extends Activity {
    private String mPostURL;
    private String mPostTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        mPostTitle = getIntent().getStringExtra(Constants.BUNDLE_POST_TITLE);
        mPostURL = getIntent().getStringExtra(Constants.BUNDLE_POST_URL);

        WebView postDetails = (WebView) findViewById(R.id.post_details);
        postDetails.setWebViewClient(new WebViewClient());
        WebSettings settings = postDetails.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        postDetails.loadUrl(mPostURL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_post_details, menu);
        MenuItem shareItem = menu.findItem(R.id.action_share);
        ShareActionProvider mShareActionProvider = (ShareActionProvider) shareItem.getActionProvider();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        String shareText = mPostTitle + " " + mPostURL + " Shared with Reader128k";
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        shareIntent.setType("text/plain");

        mShareActionProvider.setShareIntent(shareIntent);

        return super.onCreateOptionsMenu(menu);
    }
}
