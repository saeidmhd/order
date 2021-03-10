package com.mahak.order.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.mahak.order.InvoiceFragments.InvoiceCustomerDetail;
import com.mahak.order.InvoiceFragments.InvoiceGoodsDetail;
import com.mahak.order.InvoiceFragments.InvoicePromotionDetail;
import com.mahak.order.R;

public class InvoicePagerAdapter extends FragmentPagerAdapter {
    private Context context;

    public InvoicePagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new InvoicePromotionDetail();
            case 1:
                return new InvoiceGoodsDetail();
            case 2:
                return new InvoiceCustomerDetail();

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
                return context.getString(R.string.str_invoice_promo);
            case 1:
                return context.getString(R.string.str_invoice_goods);
            case 2:
                return context.getString(R.string.str_invoice_spec);
            default:
                return null;
        }
    }
}
