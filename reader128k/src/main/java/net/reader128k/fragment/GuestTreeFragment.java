package net.reader128k.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import net.reader128k.R;
import net.reader128k.adapter.GuestTreeAdapter;
import net.reader128k.domain.TreeNode;
import net.reader128k.interfaces.OnTreeNodeSelectListener;
import net.reader128k.ui.PostsActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GuestTreeFragment extends Fragment {
    private GuestTreeAdapter mGuestTreeAdapter;
    public List<TreeNode> mTree = new ArrayList<TreeNode>();
    private int mSelectedNode;
    private OnTreeNodeSelectListener mTreeNodeSelectListener;

    public static GuestTreeFragment newInstance(List<TreeNode> tree, int selectedNode) {
        GuestTreeFragment fragment = new GuestTreeFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_guest_tree, container, false);
        ListView lvUsers = (ListView) rootView.findViewById(R.id.tree);

        mGuestTreeAdapter = new GuestTreeAdapter(getActivity(), mTree);
        mGuestTreeAdapter.selectNode(mSelectedNode);
        lvUsers.setAdapter(mGuestTreeAdapter);

        lvUsers.setOnItemClickListener(new GuestTreeItemClickListener());
        lvUsers.setItemChecked(mSelectedNode, true);
        return rootView;
    }

    private class GuestTreeItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mSelectedNode = position;
            mGuestTreeAdapter.selectNode(mSelectedNode);
            mTreeNodeSelectListener.onTreeNodeSelected(mSelectedNode);
            ((PostsActivity) getActivity()).closeDrawer();
        }
    }
}
