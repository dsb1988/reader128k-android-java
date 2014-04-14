package net.reader128k.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import net.reader128k.R;
import net.reader128k.adapter.SharedPostsAdapter;
import net.reader128k.domain.TreeNode;
import net.reader128k.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class AllPostsFragment extends Fragment {
    private List<TreeNode> mTree = new ArrayList<TreeNode>();
    private SharedPostsAdapter mSharedPostsAdapter;

    public static AllPostsFragment newInstance(List<TreeNode> tree) {
        AllPostsFragment fragment = new AllPostsFragment();
        fragment.mTree = tree;
        return fragment;
    }

    public AllPostsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            mTree.addAll(getArguments().<TreeNode>getParcelableArrayList(Constants.BUNDLE_TREE));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ExpandableListView list = (ExpandableListView) inflater.inflate(
                R.layout.fragment_shared_posts, container, false);
        mSharedPostsAdapter = new SharedPostsAdapter(getActivity(), mTree);
        list.setAdapter(mSharedPostsAdapter);
        list.expandGroup(0);
        list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i2, long l) {
                int index = expandableListView.getFlatListPosition(
                        ExpandableListView.getPackedPositionForChild(i, i2));
                expandableListView.setItemChecked(index, true);
                return true;
            }
        });

        return list;
    }
}
