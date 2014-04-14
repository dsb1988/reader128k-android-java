package net.reader128k.image;

import android.widget.ImageView;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import net.reader128k.R;

public class FeedImageListener implements ImageLoader.ImageListener {
    private ImageView mView;

    public FeedImageListener(ImageView view) {
        mView = view;
        mView.setImageResource(R.drawable.ic_gravatar_rounded_default);
    }

    @Override
    public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
        if (imageContainer.getBitmap() != null)
            mView.setImageBitmap(imageContainer.getBitmap());
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {}
}
