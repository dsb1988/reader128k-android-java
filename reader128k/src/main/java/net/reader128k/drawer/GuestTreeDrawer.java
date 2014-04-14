package net.reader128k.drawer;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import net.reader128k.R;
import net.reader128k.adapter.GuestTreeAdapter;
import net.reader128k.cache.BitmapLRUCache;
import net.reader128k.image.RoundedGravatarImageListener;
import net.reader128k.domain.*;
import net.reader128k.interfaces.TreeNodeVisitor;
import net.reader128k.util.Constants;
import net.reader128k.util.ImageHelper;
import net.reader128k.util.URLHelper;

public class GuestTreeDrawer implements TreeNodeVisitor {
    private ImageLoader mLoader;

    public GuestTreeDrawer(Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        mLoader = new ImageLoader(queue, new BitmapLRUCache());
    }

    @Override
    public Object visit(AllUsersTreeNode node, Object... userData) {
        View convertView = (View) userData[0];
        ViewGroup parent = (ViewGroup) userData[1];
        GuestTreeAdapter adapter = (GuestTreeAdapter) userData[2];

        View resultView = drawCategory(node, convertView, parent, adapter);

        ImageView ivFavicon = (ImageView) resultView.findViewById(R.id.ivTreeNodeFavicon);
        ivFavicon.setImageBitmap(ImageHelper.getRoundedBitmap(
                BitmapFactory.decodeResource(adapter.getContext().getResources(), R.drawable.ic_menu_all), 128));

        return resultView;
    }

    @Override
    public Object visit(UserTreeNode node, Object ... userData) {
        View convertView = (View) userData[0];
        ViewGroup parent = (ViewGroup) userData[1];
        GuestTreeAdapter adapter = (GuestTreeAdapter) userData[2];

        View resultView = drawCategory(node, convertView, parent, adapter);

        ImageView ivFavicon = (ImageView) resultView.findViewById(R.id.ivTreeNodeFavicon);
        String url = URLHelper.changeGravatarSize(
                URLHelper.setDefaultGravatarType(node.favicon, Constants.GRAVATAR_DEFAULT_MONSTERID), 128);
        mLoader.get(url,
                new RoundedGravatarImageListener(ivFavicon));

        return resultView;
    }

    @Override
    public Object visit(FriendTreeNode node, Object... userData) {
        throw new RuntimeException("You Came to the Wrong Neighborhood, '" + node.getClass().getName() + "'");
    }

    @Override
    public Object visit(FeedTreeNode node, Object... userData) {
        throw new RuntimeException("You Came to the Wrong Neighborhood, '" + node.getClass().getName() + "'");
    }

    @Override
    public Object visit(AllMyPostsTreeNode node, Object ... userData) {
        throw new RuntimeException("You Came to the Wrong Neighborhood, '" + node.getClass().getName() + "'");
    }

    @Override
    public Object visit(CategoryTreeNode node, Object ... userData) {
        throw new RuntimeException("You Came to the Wrong Neighborhood, '" + node.getClass().getName() + "'");
    }

    @Override
    public Object visit(CircleTreeNode node, Object ... userData) {
        throw new RuntimeException("You Came to the Wrong Neighborhood, '" + node.getClass().getName() + "'");
    }

    private View drawCategory(TreeNode node, View convertView, ViewGroup parent, GuestTreeAdapter adapter) {
        View resultView = convertView;
        if (resultView == null)
            resultView = adapter.mInflater.inflate(GuestTreeAdapter.RESOURCE, parent, false);

        TextView name = (TextView) resultView.findViewById(R.id.tvTreeNodeName);
        name.setText(node.name);

        return resultView;
    }
}
