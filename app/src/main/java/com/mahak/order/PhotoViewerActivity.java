/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.mahak.order;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.mahak.order.common.PicturesProduct;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.libs.HackyViewPager;
import com.mahak.order.storage.DbAdapter;

import java.util.List;

import uk.co.senab.photoview.PhotoView;

public class PhotoViewerActivity extends BaseActivity {

    private HackyViewPager mViewPager;
    private long userId;
    private long productId;
    private DbAdapter dba;
    private List<PicturesProduct> picturesProducts;
    private int index;
    private ImageView[] pagerShowers;
    final String colorNotCurrentPage = "#d4c1b3";
    final String colorCurrentPage = "#494038";
    private LinearLayout panelPagerShower;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viwer);
        mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
        panelPagerShower = (LinearLayout) findViewById(R.id.panelPageShower);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            if (bundle.containsKey(ProjectInfo._json_key_user_id)) {
                userId = bundle.getLong(ProjectInfo._json_key_user_id);
            }
            if (bundle.containsKey(ProjectInfo._json_key_product_id)) {
                productId = bundle.getLong(ProjectInfo._json_key_product_id);
            }
            if (bundle.containsKey(ProjectInfo._json_key_index)) {
                index = bundle.getInt(ProjectInfo._json_key_index);
            }
        }

        dba = new DbAdapter(this);

        if (productId > 0 && userId > 0) {
            dba.open();
            picturesProducts = dba.getAllPictureByProductId(productId);
            dba.close();
        }

        mViewPager.setAdapter(new SamplePagerAdapter());
        if (picturesProducts != null && picturesProducts.size() > index) {
            mViewPager.setCurrentItem(index);
            pagerShowers = new ImageView[picturesProducts.size()];
            for (int i = 0; i < picturesProducts.size(); i++) {
                if (index == i)
                    pagerShowers[i] = getImageView(Color.parseColor(colorCurrentPage));
                else
                    pagerShowers[i] = getImageView(Color.parseColor(colorNotCurrentPage));
                panelPagerShower.addView(pagerShowers[i]);
            }
        }
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pagerShowers[position].setImageDrawable(getImageFragmnetChooser(Color.parseColor(colorCurrentPage)));
                if (position - 1 >= 0)
                    pagerShowers[position - 1].setImageDrawable(getImageFragmnetChooser(Color.parseColor(colorNotCurrentPage)));
                if (position + 1 < pagerShowers.length)
                    pagerShowers[position + 1].setImageDrawable(getImageFragmnetChooser(Color.parseColor(colorNotCurrentPage)));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class SamplePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return picturesProducts == null ? 0 : picturesProducts.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PicturesProduct product = picturesProducts.get(position);
            PhotoView photoView = new PhotoView(container.getContext());

//            Glide.with(getBaseContext()).load(product.getUrl()).placeholder(R.drawable.image_empty_box).crossFade().listener(ServiceTools.getGlideListener(photoView))
//                    .into(photoView);
            ProductItemInitialize.loadImage(mContext, product.getUrl(), photoView);


            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    private ImageView getImageView(int color) {
        ImageView imageView = new ImageView(this);
        int size = ServiceTools.dpToPX(this, 10);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
        params.setMargins(ServiceTools.dpToPX(this, 15), 0, 0, 0);
        imageView.setLayoutParams(params);
        imageView.setImageDrawable(getImageFragmnetChooser(color));
        return imageView;
    }

    private Drawable getImageFragmnetChooser(int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(65);
        return drawable;
    }


}
