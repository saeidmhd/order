package com.mahak.order.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.mahak.order.BaseActivity;
import com.mahak.order.ProductPickerListActivity;
import com.mahak.order.ProductsListActivity;
import com.mahak.order.R;
import com.mahak.order.common.Product;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.storage.DbAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mostafavi on 7/4/2016.
 */
public class ProductPagerFragment extends Fragment {

    private TextView tvPagerCounter;
    private ImageView imgRight, imgLeft;
    private ViewPager viewPager;
    private Context mContext;
    private List<Product> products = new ArrayList<>();
    private List<Product> originalProducts = new ArrayList<>();
    private DbAdapter db;
    private ProductPickerListActivity productPickerListActivity;
    private ProductsListActivity productsListActivity;
    private String nameSearch;
    String barcode = "";
    String mBarcode = "";


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        db = new DbAdapter(mContext);
        if (mContext instanceof ProductsListActivity) {
            productsListActivity = (ProductsListActivity) mContext;
            products.addAll(ProductsListActivity.arrayProductMain);
            originalProducts.addAll(ProductsListActivity.arrayProductMain);
            //ProductsListActivity.tvPageTitle.setText(getString(R.string.str_nav_product_list) + "(" + products.size() + ")");
        } else if (mContext instanceof ProductPickerListActivity) {
            productPickerListActivity = (ProductPickerListActivity) mContext;
            products.addAll(ProductPickerListActivity.arrayProductMain);
            originalProducts.addAll(ProductPickerListActivity.arrayProductMain);
           // ProductPickerListActivity.tvPageTitle.setText(getString(R.string.str_nav_product_list) + "(" + products.size() + ")");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mContext instanceof ProductsListActivity) {
            String search = ProductsListActivity.txtSearch.getText().toString().trim();
            if (search != null && !search.equals(""))
                search(search);
        } else if (mContext instanceof ProductPickerListActivity) {
            String search = ProductPickerListActivity.txtSearch.getText().toString().trim();
            if (search != null && !search.equals(""))
                search(search);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        db.open();
        View view = inflater.inflate(R.layout.fragment_product_gallery, container, false);

        initView(view);
        if (products == null || products.size() == 0)
            return view;
        tvPagerCounter.setText((1) + " / " + products.size());

        viewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                ProductItemFragment productItemFragment = new ProductItemFragment();
                Bundle args = new Bundle();
                args.putLong("productId", products.get(position).getId());
                args.putString("barcode", "");
                productItemFragment.setArguments(args);
                return productItemFragment;
            }

            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }

            @Override
            public int getCount() {
                return products == null ? 0 : products.size();
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvPagerCounter.setText((position + 1) + " / " + products.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

                if (mBarcode != null || !mBarcode.isEmpty()) {

                    for (int i = 0; i < products.size(); i++) {
                        if (mBarcode.equals(db.getProductDetailWithProductId(products.get(i).getProductId()).getBarcode())) {
                            viewPager.setCurrentItem(i + 1);
                            mBarcode = "";
                        }
                    }
                }


            }
        });

        imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPage = viewPager.getCurrentItem();
                currentPage--;
                if (currentPage >= 0)
                    viewPager.setCurrentItem(currentPage);
            }
        });
        imgRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPage = viewPager.getCurrentItem();
                currentPage++;
                if (currentPage < products.size())
                    viewPager.setCurrentItem(currentPage);
            }
        });
        return view;
    }

    private void initView(View view) {
        tvPagerCounter = (TextView) view.findViewById(R.id.tvPagerCounter);
        imgRight = (ImageView) view.findViewById(R.id.imgRight);
        imgLeft = (ImageView) view.findViewById(R.id.imgLeft);
        viewPager = (ViewPager) view.findViewById(R.id.pager_product);
    }

    private AppCompatActivity getMyActivity() {
        if (mContext == null)
            return null;
        if (mContext instanceof ProductPickerListActivity)
            return (ProductPickerListActivity) mContext;
        if (mContext instanceof ProductsListActivity)
            return (ProductsListActivity) mContext;
        return null;
    }

    public void searchInProduct(final String name) {
        nameSearch = name;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (name.equals(nameSearch)) {
                    new Thread() {
                        @Override
                        public void run() {
                            ((Activity) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    search(name);
                                }
                            });
                        }
                    }.start();
                }
            }
        }, 100);
    }

    public void refresh(final String Barcode) {

        if (viewPager != null && viewPager.getAdapter() != null)
            viewPager.getAdapter().notifyDataSetChanged();
        if (Barcode != null && !Barcode.equals("")) {
            barcode = Barcode;
            mBarcode = barcode;
        }

        viewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {

                ProductItemFragment productItemFragment = new ProductItemFragment();
                Bundle args = new Bundle();
                args.putLong("productId", products.get(position).getId());
                args.putString("barcode", barcode);
                args.putInt("position", position);
                productItemFragment.setArguments(args);
                barcode = "";
                return productItemFragment;

            }


            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }

            @Override
            public int getCount() {
                return products == null ? 0 : products.size();
            }
        });


    }

    private void search(String name) {
        try {
            products = new ArrayList<>();
            for (int i = 0; i < originalProducts.size(); i++) {
                Product product = originalProducts.get(i);
                boolean result_contain = ServiceTools.CheckContainsWithSimillar(name, product.getName().toLowerCase());
                if (result_contain)
                    products.add(product);
            }
            viewPager.getAdapter().notifyDataSetChanged();
            tvPagerCounter.setText(((products.size() > 0) ? (viewPager.getCurrentItem() + 1) : 0) + " / " + products.size());
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
    }

}
