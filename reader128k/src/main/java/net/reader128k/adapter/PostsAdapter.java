package net.reader128k.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import net.reader128k.R;
import net.reader128k.cache.BitmapLRUCache;
import net.reader128k.image.RoundedGravatarImageListener;
import net.reader128k.domain.Post;
import net.reader128k.util.Constants;
import net.reader128k.util.DateHelper;
import net.reader128k.util.URLHelper;

import java.util.*;


public class PostsAdapter extends ArrayAdapter<Post> {
    public static int RESOURCE = R.layout.post_item;
    private LayoutInflater mInflater;
    private List<Post> mPosts;
    private ImageLoader mLoader;

    public PostsAdapter(Context context, List<Post> objects) {
        super(context, RESOURCE, objects);
        mPosts = objects;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RequestQueue mQueue = Volley.newRequestQueue(context);

        mLoader = new ImageLoader(mQueue, new BitmapLRUCache());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View resultView;
        if (convertView == null) {
            resultView = mInflater.inflate(RESOURCE, parent, false);
        } else
            resultView = convertView;

        Post post = mPosts.get(position);

        TextView tvTitle = (TextView) resultView.findViewById(R.id.post_title);
        tvTitle.setText(post.title);

        TextView tvSourceTitle = (TextView) resultView.findViewById(R.id.post_source_title);
        tvSourceTitle.setText(post.source_title);

        TextView tvDate = (TextView) resultView.findViewById(R.id.post_date);
        tvDate.setText(DateHelper.convertDate(post.date));

        ImageView ivGravatarPrefix = (ImageView) resultView.findViewById(R.id.post_gravatar_prefix);
        TextView tvPosterName = (TextView) resultView.findViewById(R.id.poster_name);
        if (post.shared_by != null && post.shared_by.size() > 0) {
            mLoader.get(URLHelper.setDefaultGravatarType(
                    URLHelper.changeGravatarSize(post.shared_by.get(0).gravatar_prefix, 48), Constants.GRAVATAR_DEFAULT_MONSTERID),
                    new RoundedGravatarImageListener(ivGravatarPrefix));
            tvPosterName.setText(post.shared_by.get(0).name);

            ivGravatarPrefix.setVisibility(View.VISIBLE);
            tvPosterName.setVisibility(View.VISIBLE);
        } else {
            ivGravatarPrefix.setVisibility(View.GONE);
            tvPosterName.setVisibility(View.GONE);
        }


        return resultView;
    }
}
