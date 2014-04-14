package net.reader128k.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import net.reader128k.R;
import net.reader128k.adapter.PostsAdapter;
import net.reader128k.domain.Post;
import net.reader128k.ui.PostDetailsActivity;
import net.reader128k.ui.PostsActivity;
import net.reader128k.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class PostsFragment extends Fragment {
    private List<Post> mPosts = new ArrayList<Post>();
    private PostsAdapter mPostsAdapter;
    private ShareActionProvider mShareActionProvider;
    private ListView lvPosts;
    private ActionMode mActionMode;

    public static PostsFragment newInstance(List<Post> posts) {
        PostsFragment fragment = new PostsFragment();
        fragment.mPosts = posts;

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPostsAdapter = new PostsAdapter(getActivity(), mPosts);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_posts, container, false);
        lvPosts = (ListView) rootView.findViewById(R.id.posts);

        lvPosts.setAdapter(mPostsAdapter);
        lvPosts.setOnItemClickListener(new PostItemClickListener());
        lvPosts.setOnScrollListener(new PostsOnScrollListener());
        lvPosts.setOnItemLongClickListener(new PostsItemLongClickListener());

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void addMorePosts(List<Post> posts) {
        mPosts.addAll(posts);
        mPostsAdapter.notifyDataSetChanged();
    }

    private class PostItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getActivity(), PostDetailsActivity.class);
            intent.putExtra(Constants.BUNDLE_POST_TITLE, mPosts.get(position).title);
            intent.putExtra(Constants.BUNDLE_POST_URL, mPosts.get(position).link);
            startActivity(intent);
        }
    }

    private class PostsOnScrollListener implements ListView.OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {}

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            boolean loadMorePosts = firstVisibleItem + visibleItemCount >= totalItemCount;
            PostsActivity postsActivity = (PostsActivity) getActivity();
            if (loadMorePosts && !postsActivity.mLastPageReached) {
                postsActivity.loadMorePosts();
            }
        }
    }

    private class PostsItemLongClickListener implements ListView.OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            if (mActionMode != null)
                return false;

            mActionMode = getActivity().startActionMode(new PostsActionModeCallback());

            lvPosts.requestFocusFromTouch();
            lvPosts.setSelection(position);

            if (mShareActionProvider != null) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                Post post = mPosts.get(position);
                String shareText = post.title + " " + post.link + " Shared with Reader128k";
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                sendIntent.setType("text/plain");
                mShareActionProvider.setShareIntent(sendIntent);
            }

            return true;
        }
    }

    private class PostsActionModeCallback implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater menuInflater = mode.getMenuInflater();
            menuInflater.inflate(R.menu.activity_posts_context_menu, menu);

            MenuItem shareItem = menu.findItem(R.id.action_share);
            mShareActionProvider = (ShareActionProvider) shareItem.getActionProvider();

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_share:
                    mode.finish();
                    return  true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    }
}
