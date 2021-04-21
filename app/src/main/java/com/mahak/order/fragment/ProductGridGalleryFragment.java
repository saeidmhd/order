package com.mahak.order.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.mahak.order.BaseActivity;
import com.mahak.order.ProductItemInitialize;
import com.mahak.order.ProductPickerListActivity;
import com.mahak.order.ProductsListActivity;
import com.mahak.order.R;
import com.mahak.order.common.PicturesProduct;
import com.mahak.order.common.Product;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.storage.DbAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import static com.mahak.order.common.ServiceTools.formatCount;

/**
 * Created by mostafavi on 7/5/2016.
 */
public class ProductGridGalleryFragment extends Fragment {
    private Context mContext;
    List<Product> products = new ArrayList<>();
    private ImageLoader imageLoader;
    private ProductsListActivity productsListActivity;
    private ProductPickerListActivity productPickerListActivity;
    private AlertDialog dialog;
    private RecyclerView recyclerView;
    private DialogProduct productItemFragment;
    private List<Product> arrayOriginal = new ArrayList();
    public ProductRecycleAdapter recycleAdapter;
    private DbAdapter db;
    static RefreshList refreshList;

    public interface RefreshList {
        void onRefreshList();
    }

    @Override
    public void onAttach(Context context) {
        mContext = context;
        db = new DbAdapter(mContext);
        super.onAttach(context);
        if (mContext instanceof ProductsListActivity) {
            productsListActivity = (ProductsListActivity) mContext;
            products.addAll(ProductsListActivity.arrayProductMain);
            arrayOriginal.addAll(products);
            //ProductsListActivity.tvPageTitle.setText(getString(R.string.str_nav_product_list) + "(" + products.size() + ")");
        } else if (mContext instanceof ProductPickerListActivity) {
            productPickerListActivity = (ProductPickerListActivity) mContext;
            products.addAll(ProductPickerListActivity.arrayProductMain);
            arrayOriginal.addAll(products);
            //ProductPickerListActivity.tvPageTitle.setText(getString(R.string.str_nav_product_list) + "(" + products.size() + ")");
            calculateSelectedProducts();
        }
    }

    public void refresh(String barcode) {

        calculateSelectedProducts();
        if (recycleAdapter != null)
            recycleAdapter.notifyDataSetChanged();

        if (!TextUtils.isEmpty(barcode)) {
            recycleAdapter = new ProductRecycleAdapter(barcode);
            recyclerView.setAdapter(recycleAdapter);
            recycleAdapter.notifyDataSetChanged();
        }
    }

    private void calculateSelectedProducts() {
        if (products == null)
            return;
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            if (ProductPickerListActivity.HashMap_Product != null && ProductPickerListActivity.HashMap_Product.size() > 0 && ProductPickerListActivity.HashMap_Product.containsKey(product.getProductId())) {
                product.setSelectedCount(ProductPickerListActivity.HashMap_Product.get(product.getProductId()).getCount1());
            } else
                product.setSelectedCount(0);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mContext instanceof ProductsListActivity) {
            String search = ProductsListActivity.txtSearch.getText().toString().trim();
            if (search != null && !search.equals(""))
                filter(search, new Filter.FilterListener() {
                    @Override
                    public void onFilterComplete(int count) {

                    }
                });
        } else if (mContext instanceof ProductPickerListActivity) {
            String search = ProductPickerListActivity.txtSearch.getText().toString().trim();
            if (search != null && !search.equals(""))
                filter(search, new Filter.FilterListener() {
                    @Override
                    public void onFilterComplete(int count) {

                    }
                });
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recyclerView = new RecyclerView(mContext);
        initGridDisplay(recyclerView);
        recycleAdapter = new ProductRecycleAdapter("");
        recyclerView.setAdapter(recycleAdapter);
        return recyclerView;
    }

    private void initGridDisplay(RecyclerView recyclerView) {
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, mContext.getResources().getInteger(R.integer.column_number_orders_list));
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        int padding = ServiceTools.dpToPX(mContext, 5);
        recyclerView.setPadding(padding, 0, padding, 0);
    }

    class Holder extends RecyclerView.ViewHolder {

        TextView tvItemCount;
        TextView tvName;
        ImageView imgProduct;

        public Holder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvItemCount = (TextView) itemView.findViewById(R.id.tvItemCount);
            imgProduct = (ImageView) itemView.findViewById(R.id.imgProduct);
        }
    }

    private void createDialogItems(final View.OnClickListener accept) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_fragment_product_item, null, false);
        ViewPager pager = (ViewPager) view.findViewById(R.id.pager_product);
        pager.setAdapter(new FragmentPagerAdapter(((BaseActivity) mContext).getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                productItemFragment = new DialogProduct();
                Bundle args = new Bundle();
                args.putLong("productId", products.get(position).getId());
                productItemFragment.setArguments(args);
                return productItemFragment;
            }

            @Override
            public int getCount() {
                return 1;
            }
        });

        builder.setView(view);
        builder.setPositiveButton(R.string.str_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                accept.onClick(null);
            }
        });
        dialog = builder.create();
    }

    private void showDialog(final int position, String barcode) {

        db.open();

        if (dialog == null)
            productItemFragment = new DialogProduct();
        Bundle args = new Bundle();
        args.putLong("productId", products.get(position).getId());
        args.putString("barcode", barcode);
        if (productItemFragment != null)
            productItemFragment.setArguments(args);

        productItemFragment.show(getChildFragmentManager(), "ProductItem");

        for (int i = 0; i < arrayOriginal.size(); i++) {
            if (barcode.equals(db.getProductDetailWithProductId(arrayOriginal.get(i).getProductId()).getBarcode())) {
                recyclerView.scrollToPosition(i);
            }

        }

        refreshList = new RefreshList() {
            @Override
            public void onRefreshList() {

                if (recycleAdapter != null) {
                    recycleAdapter = new ProductRecycleAdapter("");
                    recyclerView.setAdapter(recycleAdapter);
                    recycleAdapter.notifyDataSetChanged();
                }

            }
        };

    }

    public void filter(CharSequence text, Filter.FilterListener listener) {
        if (recycleAdapter != null)
            recycleAdapter.getFilter().filter(text, listener);
    }

    public static class DialogProduct extends ProductItemFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Dialog dialog = super.onCreateDialog(savedInstanceState);
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);
            return dialog;
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            //recyclerView.getAdapter().notifyDataSetChanged();
            if (refreshList != null)
                refreshList.onRefreshList();
            super.onDismiss(dialog);
        }
    }

    class ProductRecycleAdapter extends RecyclerView.Adapter implements Filterable {

        private CustomFilterList filter;
        String Barcode;

        public ProductRecycleAdapter(String barcode) {

            this.Barcode = barcode;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.grv_product_item_gallery, parent, false);
            Holder holder = new Holder(view);
            if (productsListActivity != null)
                holder.tvItemCount.setVisibility(View.GONE);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            db.open();
            Product product = null;
            for (Product listproduct : products) {
                if (db.getProductDetailWithProductId(listproduct.getProductId()).getBarcode() != null && db.getProductDetailWithProductId(listproduct.getProductId()).getBarcode().equals(Barcode)) {
                    product = listproduct;
                }
            }

            if (product == null)
                product = products.get(position);

            initHolder(product, getChildFragmentManager(), holder, position, Barcode);

            if (Barcode != null && !Barcode.isEmpty()) {
                showDialog(position, Barcode);
                Barcode = "";
            }

        }

        private void initHolder(Product product, FragmentManager childFragmentManager, RecyclerView.ViewHolder holder, final int position, String barcode) {

            final Holder mHolder = (Holder) holder;
            if (product.getPictures() == null) {
                if (db == null) db = new DbAdapter(mContext);
                db.open();
                product.setPictures(db.getAllPictureByProductId(product.getProductCode()));
                db.close();
            }
            if (product.getPictures() != null && product.getPictures().size() > 0) {//                if (imageLoader == null) {
//                    imageLoader = ImageLoader.getInstance();
//                    ServiceTools.initImageLoader(imageLoader, mContext);
//                }
                PicturesProduct picturesProduct = product.getPictures().get(0);
//                Glide.with(mContext).load(picturesProduct.getUrl()).placeholder(R.drawable.image_empty_box).crossFade().listener(ServiceTools.getGlideListener(mHolder.imgProduct))
//                        .into(mHolder.imgProduct);
                ProductItemInitialize.loadImage(mContext, picturesProduct.getUrl(), mHolder.imgProduct);
            } else {
                mHolder.imgProduct.setImageResource(R.drawable.img_default_product);
                mHolder.imgProduct.setBackgroundResource(R.drawable.image_empty_box);
            }
            mHolder.tvItemCount.setText(formatCount(product.getSelectedCount()));
            mHolder.tvName.setText(product.getName());
            if (product.getSelectedCount() > 0)
                mHolder.tvItemCount.setSelected(true);
            else
                mHolder.tvItemCount.setSelected(false);
            mHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(position, "");
                }
            });
        }

        @Override
        public int getItemCount() {
            return products == null ? 0 : products.size();
        }

        @Override
        public Filter getFilter() {
            if (filter == null)
                filter = new CustomFilterList();
            return filter;

        }
    }

    public void dismissDialog() {
        if (productItemFragment != null)
            productItemFragment.dismiss();
    }

    public class CustomFilterList extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            String name = "";

            if (constraint != null && constraint.toString().length() > 0) {
                ArrayList<Product> filterItem = new ArrayList<Product>();
                for (int i = 0; i < arrayOriginal.size(); i++) {
                    Product product = arrayOriginal.get(i);
                    name = product.getName();
                    boolean result_contain = ServiceTools.CheckContainsWithSimillar(constraint.toString(), name);
                    if (result_contain)
                        filterItem.add(product);
                }
                result.values = filterItem;
                result.count = filterItem.size();
            } else {
                synchronized (this) {
                    result.values = arrayOriginal;
                    result.count = arrayOriginal.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            products = (ArrayList<Product>) results.values;
            recyclerView.getAdapter().notifyDataSetChanged();

        }
    }

    private DisplayImageOptions getOptionImageLoader() {
        if (mContext instanceof ProductsListActivity) {
            return ProductsListActivity.options;
        } else if (mContext instanceof ProductPickerListActivity) {
            return ProductPickerListActivity.options;
        }
        return null;
    }
}
