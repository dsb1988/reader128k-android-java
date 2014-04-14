package net.reader128k.ui;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.*;
import android.widget.Toast;

import net.reader128k.R;
import net.reader128k.app.ReaderApplication;
import net.reader128k.domain.*;
import net.reader128k.fragment.*;
import net.reader128k.network.ReaderConnection;
import net.reader128k.util.Constants;
import net.reader128k.util.LogUtils;
import net.reader128k.util.ReaderMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.tavendo.autobahn.WebSocketException;

import static net.reader128k.util.LogUtils.LOGD;
import static net.reader128k.util.LogUtils.LOGE;

public class PostsActivity extends BaseReaderActivity {
    private static final String TAG = LogUtils.makeLogTag(PostsActivity.class);

    private ReaderConnection mConnection;
    private User mUser;
    private List<TreeNode> mTree = new ArrayList<TreeNode>();
    private int mLastNodePosition = -1;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Menu mMenu;
    private FragmentManager mFragmentManager;
    private int mLastLoadedPage = 0;

    public boolean mLastPageReached = false;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_posts);

        mFragmentManager = getFragmentManager();

        ReaderApplication readerApplication = (ReaderApplication) getApplication();
        mConnection = readerApplication.mConnection;

        customizeNavigationDrawer();
	}

    @Override
    protected void onStart() {
        super.onStart();

        updateTreeFragment();
        updatePostsFragment(new ArrayList<Post>());

        mConnection.attach(this);
        if (mConnection.isConnected())
            loadUser();
        else {
            try {
                mConnection.connect();
            } catch (WebSocketException e) {
                LOGE(TAG, "Connection to " + Constants.READER_HOST + " failed", e);
                Toast.makeText(this, "Websocket connection failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mConnection.detach(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_posts, menu);
        mMenu = menu;
        updateMenu();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item))
            return true;

        switch (item.getItemId()) {
            case R.id.menu_login:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_settings:
                return true;
            case R.id.menu_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateMenu() {
        if (mMenu != null) {
            if (mUser != null && mUser.is_authenticated) {
                mMenu.findItem(R.id.menu_login).setVisible(false);
                mMenu.findItem(R.id.menu_logout).setVisible(true);
            } else {
                mMenu.findItem(R.id.menu_login).setVisible(true);
                mMenu.findItem(R.id.menu_logout).setVisible(false);
            }
        }
    }

    private void customizeNavigationDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    public void updateTreeFragment() {
        Fragment oldFragment = mFragmentManager.findFragmentByTag(Constants.TREE_FRAGMENT_TAG);
        if (oldFragment != null)
            mFragmentManager.beginTransaction().remove(oldFragment).commit();

        if (mUser != null)
            if (mUser.is_authenticated)
                if (mTree.size() > 0)
                    mFragmentManager.beginTransaction()
                            .replace(R.id.tree_frame,
                                    UserTreeFragment.newInstance(mTree, mLastNodePosition),
                                    Constants.TREE_FRAGMENT_TAG)
                            .commit();
                else
                    mFragmentManager.beginTransaction()
                            .replace(R.id.tree_frame,
                                    TreeIsEmptyFragment.newInstance(),
                                    Constants.TREE_FRAGMENT_TAG)
                            .commit();
            else {
                if (mTree.size() > 0)
                    mFragmentManager.beginTransaction()
                            .replace(R.id.tree_frame,
                                    GuestTreeFragment.newInstance(mTree, mLastNodePosition),
                                    Constants.TREE_FRAGMENT_TAG)
                            .commit();
                else
                    mFragmentManager.beginTransaction()
                            .replace(R.id.tree_frame,
                                    TreeIsEmptyFragment.newInstance(),
                                    Constants.TREE_FRAGMENT_TAG)
                            .commit();
            }
        else
            mFragmentManager.beginTransaction()
                    .replace(R.id.tree_frame,
                            TreeIsEmptyFragment.newInstance(),
                            Constants.TREE_FRAGMENT_TAG)
                    .commit();

    }

    public void updatePostsFragment(List<Post> posts) {
        Fragment oldFragment = mFragmentManager.findFragmentByTag(Constants.POSTS_FRAGMENT_TAG);
        if (oldFragment == null) {
            if (posts.size() > 0)
                mFragmentManager.beginTransaction()
                        .replace(R.id.posts_frame,
                                PostsFragment.newInstance(posts),
                                Constants.POSTS_FRAGMENT_TAG)
                        .commit();
            else
                mFragmentManager.beginTransaction()
                        .replace(R.id.posts_frame,
                                NoPostsFragment.newInstance(
                                        mTree.size() > 0 ? mTree.get(mLastNodePosition).name : ""),
                                Constants.POSTS_FRAGMENT_TAG)
                        .commit();
        } else if (oldFragment.getClass() == PostsFragment.class)
            ((PostsFragment) oldFragment).addMorePosts(posts);
        else {
            mFragmentManager.beginTransaction().remove(oldFragment).commit();
            if (posts.size() > 0)
                mFragmentManager.beginTransaction()
                        .replace(R.id.posts_frame,
                                PostsFragment.newInstance(posts),
                                Constants.POSTS_FRAGMENT_TAG)
                        .commit();
            else
                mFragmentManager.beginTransaction()
                        .replace(R.id.posts_frame,
                                NoPostsFragment.newInstance(
                                        mTree.size() > 0 ? mTree.get(mLastNodePosition).name : ""),
                                Constants.POSTS_FRAGMENT_TAG)
                        .commit();
        }
    }

    @Override
    public void onConnect() {
        loadUser();
    }

    private void loadUser() {
        setProgressBarIndeterminateVisibility(true);
        mConnection.send(ReaderMethod.USER_INFO, null, null);
    }

    @Override
    public void onUserInfo(User user) {
        setProgressBarIndeterminateVisibility(false);
        mUser = user;
        updateMenu();
        loadTree();
    }

    private void logout() {
        setProgressBarIndeterminateVisibility(true);
        mConnection.send(ReaderMethod.LOGOUT, null, null);
    }

    @Override
    public void onLogout(LogoutResponse response) {
        setProgressBarIndeterminateVisibility(false);
        if (response.success) {
            mUser = null;
            mTree.clear();
            updateTreeFragment();
            updatePostsFragment(new ArrayList<Post>());
            updateMenu();
        }
    }

    private void loadTree() {
        setProgressBarIndeterminateVisibility(true);
        mConnection.send(ReaderMethod.TREE, null, null);
    }

    @Override
    public void onTree(List<TreeNode> tree) {
        setProgressBarIndeterminateVisibility(false);
        // Assuming new tree each time
        mTree = tree;
        onTreeNodeSelected(findTreeNodePositionById(""));
        updateTreeFragment();
    }

    @Override
    public void onTreeNodeSelected(int position) {
        Fragment oldFragment = mFragmentManager.findFragmentByTag(Constants.POSTS_FRAGMENT_TAG);
        if (oldFragment != null)
            mFragmentManager.beginTransaction().remove(oldFragment).commit();

        mLastLoadedPage = 0;
        mLastPageReached = false;
        mLastNodePosition = position;
        TreeNode node = mTree.get(mLastNodePosition);
        setTitle(node.name);
        loadMorePosts(node, ++mLastLoadedPage, false);
    }

    public void loadMorePosts() {
        if (mTree.size() > 0)
            loadMorePosts(mTree.get(mLastNodePosition), ++mLastLoadedPage, false);
    }

    private void loadMorePosts(TreeNode node, int page, boolean viewAll) {
        setProgressBarIndeterminateVisibility(true);
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("kind", node.type);
        params.put("id", node.id);
        params.put("page", page);
        params.put("viewAll", viewAll);

        new LoadMorePostsTask().execute(params);
    }

    @Override
    public void onPosts(List<Post> posts) {
        setProgressBarIndeterminateVisibility(false);
        updatePostsFragment(posts);
        mLastPageReached = posts.size() == 0;
    }

    public void closeDrawer() {
        mDrawerLayout.closeDrawer(Gravity.START);
    }

    private class LoadMorePostsTask extends AsyncTask<HashMap<String, Object>, Void, Void> {
        @Override
        protected Void doInBackground(HashMap<String, Object>... params) {
            mConnection.send(ReaderMethod.POSTS, params[0], null);
            return null;
        }
    }

    private int findTreeNodePositionById(String id) {
        for (int i=0; i<mTree.size(); i++)
            if (mTree.get(i).id.equals(id))
                return i;
        return -1;
    }
}
