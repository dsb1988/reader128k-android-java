package net.reader128k.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import net.reader128k.R;

public class TreeIsEmptyFragment extends Fragment {
    public static TreeIsEmptyFragment newInstance() {
        return new TreeIsEmptyFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_empty_tree, container, false);
    }
}
