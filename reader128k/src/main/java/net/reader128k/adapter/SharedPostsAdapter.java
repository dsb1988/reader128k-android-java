package net.reader128k.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import net.reader128k.R;
import net.reader128k.domain.TreeNode;
import net.reader128k.drawer.UserTreeDrawer;

import java.util.HashMap;
import java.util.List;

public class SharedPostsAdapter extends BaseExpandableListAdapter {
    public List<TreeNode> mTree;
    public LayoutInflater mInflater;
    public Context mContext;

    private UserTreeDrawer userTreeDrawer;

    public static int CATEGORY_RESOURCE = R.layout.user_tree_category;
    public static int CATEGORY_ITEM_RESOURCE = R.layout.user_tree_category_item;

    public SharedPostsAdapter(Context context, List<TreeNode> tree) {
        mTree = tree;
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        userTreeDrawer = new UserTreeDrawer(mContext);
    }

    @Override
    public int getGroupCount() {
        return mTree.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mTree.get(groupPosition).children.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mTree.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mTree.get(groupPosition).children.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View resultView = (View) mTree.get(groupPosition).accept(userTreeDrawer, convertView, parent, this);

//        if (groupPosition == mSelectedNode)
//            resultView.findViewById(R.id.selectedIndicator).setVisibility(View.VISIBLE);
//        else
//            resultView.findViewById(R.id.selectedIndicator).setVisibility(View.INVISIBLE);

        return resultView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View resultView = (View) mTree.get(groupPosition).children.get(childPosition)
                .accept(userTreeDrawer, convertView, parent, this);

        return resultView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
