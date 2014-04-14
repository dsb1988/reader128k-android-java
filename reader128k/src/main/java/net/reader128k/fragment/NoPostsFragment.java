package net.reader128k.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import net.reader128k.R;

public class NoPostsFragment extends Fragment {
    public String mPoster = "";
    private static String BUNDLE_POSTER = "poster";

    public static NoPostsFragment newInstance(String poster) {
        NoPostsFragment fragment = new NoPostsFragment();
        fragment.mPoster = poster;

        return  fragment;
    }

    public NoPostsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            String poster = args.getString(BUNDLE_POSTER);
            if (poster != null)
                mPoster = poster;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState != null)
            mPoster = savedInstanceState.getString(BUNDLE_POSTER);

        View rootView = inflater.inflate(R.layout.fragment_no_posts, container, false);

        if (!mPoster.equals("")) {
            TextView tvNoPosts = (TextView) rootView.findViewById(R.id.noPostsMessage);
            tvNoPosts.setText(mPoster + " hasn't shared anything yet");
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BUNDLE_POSTER, mPoster);
    }
}
