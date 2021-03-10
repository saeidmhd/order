package com.mahak.order.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.mahak.order.PromoFragments.PromoDetails;
import com.mahak.order.PromoFragments.PromoGeneralSpec;
import com.mahak.order.PromoFragments.PromoTerms;
import com.mahak.order.R;

public class PromotionPagerAdapter extends FragmentPagerAdapter {

    private Context context;

    public PromotionPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new PromoDetails();
            case 1:
                return new PromoTerms();
            case 2:
                return new PromoGeneralSpec();
        }
        return null;
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.str_promo_details);
            case 1:
                return context.getString(R.string.str_promo_terms);
            case 2:
                return context.getString(R.string.str_general_promo_spec);
            default:
                return null;
        }
    }
}
