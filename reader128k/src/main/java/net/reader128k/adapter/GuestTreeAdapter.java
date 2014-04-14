package net.reader128k.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import net.reader128k.R;
import net.reader128k.domain.AllUsersTreeNode;
import net.reader128k.domain.TreeNode;
import net.reader128k.drawer.GuestTreeDrawer;
import net.reader128k.util.Constants;

import java.util.List;

public class GuestTreeAdapter extends ArrayAdapter<TreeNode> {
    public LayoutInflater mInflater;
    public List<TreeNode> mTree;
    public static int RESOURCE = R.layout.guest_tree_category;
    private int mSelectedNode = -1;
    private GuestTreeDrawer guestTreeDrawer;

    public GuestTreeAdapter(Context context, List<TreeNode> tree) {
        super(context, RESOURCE, tree);
        mTree = tree;
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        guestTreeDrawer = new GuestTreeDrawer(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View resultView = (View) mTree.get(position).accept(guestTreeDrawer, convertView, parent, this);

        return resultView;
    }

    public void selectNode(int position) {
        mSelectedNode = position;
        notifyDataSetChanged();
    }
}
