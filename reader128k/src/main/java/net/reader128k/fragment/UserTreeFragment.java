package net.reader128k.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v13.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import net.reader128k.R;
import net.reader128k.domain.*;
import net.reader128k.interfaces.OnTreeNodeSelectListener;
import net.reader128k.util.Constants;
import net.reader128k.util.TreeNodeHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserTreeFragment extends Fragment {
    public List<TreeNode> mTree = new ArrayList<TreeNode>();

    private OnTreeNodeSelectListener mTreeNodeSelectListener;
    private int mSelectedNode;

    private FragmentTabHost mTabHost;

    public static UserTreeFragment newInstance(List<TreeNode> tree, int selectedNode) {
        UserTreeFragment fragment = new UserTreeFragment();
        fragment.mTree = tree;
        fragment.mSelectedNode = selectedNode;

        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mTreeNodeSelectListener = (OnTreeNodeSelectListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnTreeNodeSelectListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mTabHost = new FragmentTabHost(getActivity());
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.tree_frame);
        mTabHost.getTabWidget().setBackgroundColor(Color.rgb(0xee, 0xee, 0xee));
        mTabHost.getTabWidget().setDividerDrawable(null);

        // Bookmarks tab
        View indicator = LayoutInflater.from(getActivity()).inflate(R.layout.user_tree_tab,
                (ViewGroup) mTabHost.findViewById(android.R.id.tabs), false);
        TextView title = (TextView) indicator.findViewById(R.id.title);
        title.setText("Bookmarks");
        ImageView icon = (ImageView) indicator.findViewById(R.id.icon);
        icon.setImageResource(R.drawable.tree_tab_bookmarks);

        TabHost.TabSpec bookmarksTab = mTabHost.newTabSpec("Bookmarks");
        bookmarksTab.setIndicator(indicator);
        mTabHost.addTab(bookmarksTab, NoPostsFragment.class, null);

        // Shared tab
        indicator = LayoutInflater.from(getActivity()).inflate(R.layout.user_tree_tab,
                (ViewGroup) mTabHost.findViewById(android.R.id.tabs), false);
        title = (TextView) indicator.findViewById(R.id.title);
        title.setText("Shared");
        icon = (ImageView) indicator.findViewById(R.id.icon);
        icon.setImageResource(R.drawable.tree_tab_shared);

        TabHost.TabSpec sharedTab = mTabHost.newTabSpec("Shared");
        sharedTab.setIndicator(indicator);

        Bundle args = new Bundle();
        args.putParcelableArrayList(Constants.BUNDLE_TREE, TreeNodeHelper.getCircleNodes(mTree));
        mTabHost.addTab(sharedTab, SharedPostsFragment.class, args);

        // All my posts tab
        indicator = LayoutInflater.from(getActivity()).inflate(R.layout.user_tree_tab,
                (ViewGroup) mTabHost.findViewById(android.R.id.tabs), false);
        title = (TextView) indicator.findViewById(R.id.title);
        title.setText("All");
        icon = (ImageView) indicator.findViewById(R.id.icon);
        icon.setImageResource(R.drawable.tree_tab_all);

        TabHost.TabSpec allTab = mTabHost.newTabSpec("All");
        allTab.setIndicator(indicator);

        args = new Bundle();
        args.putParcelableArrayList(Constants.BUNDLE_TREE, TreeNodeHelper.getCategoryNodes(mTree));
        mTabHost.addTab(allTab, AllPostsFragment.class, args);

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                getActivity().setTitle(s);
            }
        });
        mTabHost.setCurrentTabByTag("All");

        return mTabHost;
    }

    public interface TreeUpdateListener {
        public void onTreeUpdate(List<TreeNode> tree);
    }

    //
//    private class GuestTreeItemClickListener implements ExpandableListView.OnGroupClickListener {
//        @Override
//        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//            mSelectedTreeNode = groupPosition;
//            mSharedPostsAdapterAdapter.setItemSelected(mSelectedTreeNode);
//            mTreeNodeSelectListener.onTreeNodeSelected(mSelectedTreeNode);
//
//            return false;
//        }
//    }
}
