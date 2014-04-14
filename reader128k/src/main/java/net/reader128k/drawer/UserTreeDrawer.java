package net.reader128k.drawer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import net.reader128k.R;
import net.reader128k.adapter.SharedPostsAdapter;
import net.reader128k.cache.BitmapLRUCache;
import net.reader128k.image.FeedImageListener;
import net.reader128k.image.RoundedGravatarImageListener;
import net.reader128k.domain.*;
import net.reader128k.interfaces.TreeNodeVisitor;
import net.reader128k.util.Constants;
import net.reader128k.util.URLHelper;

public class UserTreeDrawer implements TreeNodeVisitor {
    private ImageLoader mLoader;

    public UserTreeDrawer(Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        mLoader = new ImageLoader(queue, new BitmapLRUCache());
    }

    @Override
    public Object visit(AllUsersTreeNode node, Object... userData) {
        throw new RuntimeException("You Came to the Wrong Neighborhood, '" + node.getClass().getName() + "'");
    }

    @Override
    public Object visit(UserTreeNode node, Object ... userData) {
        throw new RuntimeException("You Came to the Wrong Neighborhood, '" + node.getClass().getName() + "'");
    }

    @Override
    public Object visit(FriendTreeNode node, Object... userData) {
        View convertView = (View) userData[0];
        ViewGroup parent = (ViewGroup) userData[1];
        SharedPostsAdapter adapter = (SharedPostsAdapter) userData[2];

        View resultView = drawCategoryItem(node, convertView, parent, adapter);

        ImageView ivFavicon = (ImageView) resultView.findViewById(R.id.ivCategoryItemFavicon);
        mLoader.get(URLHelper.changeGravatarSize(
                URLHelper.setDefaultGravatarType(node.gravatar_prefix, Constants.GRAVATAR_DEFAULT_MONSTERID), 64),
                new RoundedGravatarImageListener(ivFavicon));

        return resultView;
    }

    @Override
    public Object visit(FeedTreeNode node, Object... userData) {
        View convertView = (View) userData[0];
        ViewGroup parent = (ViewGroup) userData[1];
        SharedPostsAdapter adapter = (SharedPostsAdapter) userData[2];

        View resultView = drawCategoryItem(node, convertView, parent, adapter);

        ImageView ivFavicon = (ImageView) resultView.findViewById(R.id.ivCategoryItemFavicon);
        String url = "http://" + Constants.READER_FAVICON_HOST + node.favicon;
        mLoader.get(url, new FeedImageListener(ivFavicon));

        return resultView;
    }

    @Override
    public Object visit(AllMyPostsTreeNode node, Object ... userData) {
        View convertView = (View) userData[0];
        ViewGroup parent = (ViewGroup) userData[1];
        SharedPostsAdapter adapter = (SharedPostsAdapter) userData[2];

        return drawCategory(node, convertView, parent, adapter);
    }

    @Override
    public Object visit(CategoryTreeNode node, Object ... userData) {
        View convertView = (View) userData[0];
        ViewGroup parent = (ViewGroup) userData[1];
        SharedPostsAdapter adapter = (SharedPostsAdapter) userData[2];

        return drawCategory(node, convertView, parent, adapter);
    }

    @Override
    public Object visit(CircleTreeNode node, Object ... userData) {
        View convertView = (View) userData[0];
        ViewGroup parent = (ViewGroup) userData[1];
        SharedPostsAdapter adapter = (SharedPostsAdapter) userData[2];

        return drawCategory(node, convertView, parent, adapter);
    }

    private View drawCategory(TreeNode node, View convertView, ViewGroup parent, SharedPostsAdapter adapter) {
        LinearLayout resultView;
        if (convertView == null) {
            resultView = (LinearLayout) adapter.mInflater.inflate(SharedPostsAdapter.CATEGORY_RESOURCE, null);
        } else {
            resultView = (LinearLayout) convertView;
        }

        TextView name = (TextView) resultView.findViewById(R.id.tvCategoryName);
        name.setText(node.name);

        return resultView;
    }

    private View drawCategoryItem(TreeNode node, View convertView, ViewGroup parent, SharedPostsAdapter adapter) {
        LinearLayout resultView;
        if (convertView == null) {
            resultView = (LinearLayout) adapter.mInflater.inflate(SharedPostsAdapter.CATEGORY_ITEM_RESOURCE, null);
        } else {
            resultView = (LinearLayout) convertView;
        }

        TextView name = (TextView) resultView.findViewById(R.id.tvCategoryItemName);
        name.setText(node.name);

        return resultView;
    }
}
