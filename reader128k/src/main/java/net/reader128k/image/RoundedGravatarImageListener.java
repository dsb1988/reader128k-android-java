package net.reader128k.image;

import android.widget.ImageView;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import net.reader128k.R;
import net.reader128k.util.ImageHelper;

public class RoundedGravatarImageListener implements ImageLoader.ImageListener {
    private ImageView mView;

    public RoundedGravatarImageListener(ImageView view) {
        mView = view;
        mView.setImageResource(R.drawable.ic_gravatar_rounded_default);
    }

    @Override
    public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
        if (imageContainer.getBitmap() != null)
            mView.setImageBitmap(ImageHelper.getRoundedBitmap(imageContainer.getBitmap(), 64));
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {}
}
