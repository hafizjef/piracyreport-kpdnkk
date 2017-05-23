package utem.workshop.piracyreport.fragmentsAdapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import utem.workshop.piracyreport.R;

public class ImagePagerAdapter extends PagerAdapter {

    @BindView(R.id.imageView)
    ImageView imageView;

    @BindView(R.id.imgProgress)
    ProgressBar imgProgress;

    Context mContext;
    LayoutInflater mLayoutInflater;
    ArrayList<String> mURL;

    public ImagePagerAdapter(Context context, ArrayList<String> url) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mURL = url;
    }

    @Override
    public int getCount() {
        return mURL.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.image_pager_item, container, false);

        ButterKnife.bind(this, itemView);

        Glide.with(mContext)
                .load(mURL.get(position))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        imgProgress.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        imgProgress.setVisibility(View.GONE);
                        return false;
                    }
                })
                .error(R.drawable.ic_error)
                .into(imageView);

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
