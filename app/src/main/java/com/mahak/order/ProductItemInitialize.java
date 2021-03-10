package com.mahak.order;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.mahak.order.common.PicturesProduct;
import com.mahak.order.common.Product;
import com.mahak.order.common.ProductDetail;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.storage.DbAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.io.File;
import java.util.ArrayList;

import static com.mahak.order.common.ServiceTools.formatCount;

/**
 * Created by mostafavi on 7/14/2016.
 */
public class ProductItemInitialize {

    private ProductsListActivity productsListActivity;
    private Context mContext;
    private DbAdapter db;
    private ProductPickerListActivity productPickerListActivity;
    private int selectedPhotoindex;
    private ArrayList<Product> products;
    static TextView btnCount;
    static TextView btnPrice;

    public ProductItemInitialize(Context mContext, ProductsListActivity productsListActivity, ProductPickerListActivity productPickerListActivity) {
        this.productsListActivity = productsListActivity;
        this.mContext = mContext;
        this.productPickerListActivity = productPickerListActivity;
        products = ProductPickerListActivity.arrayProductMain;
    }

    public ProductItemInitialize() {
    }

    public Holder initView(View view) {
        Holder holder = new Holder(view);

        if (productsListActivity != null)
            view.findViewById(R.id.layoutCount).setVisibility(View.GONE);
        return holder;
    }

    public void initHolder(final int position, final Product product, final Holder holder, ArrayList<Product> arrayOrginal) {
        assert product != null;
        if (db == null) db = new DbAdapter(mContext);
        db.open();
        double SumCount2 = 0;
        double SumCount1 = 0;

        ArrayList<ProductDetail> productDetails = db.getAllProductDetailWithProductId(product.getProductId());

        for (ProductDetail productDetail : productDetails) {
            SumCount1 = ServiceTools.getSumCount1(productDetail.getProductId(), mContext);
            SumCount2 = ServiceTools.getSumCount2(productDetail.getProductId(), mContext);
        }
        db.close();
        if (product.getPictures() == null) {
            if (db == null) db = new DbAdapter(mContext);
            db.open();
            product.setPictures(db.getAllPictureByProductId(product.getProductCode()));
            db.close();
        }
        if (BaseActivity.getPrefUnit2Setting(mContext) == BaseActivity.MODE_MeghdarJoz) {
            holder.panelTotalAsset.setVisibility(View.VISIBLE);
            holder.panelTotalCount.setVisibility(View.VISIBLE);
            if (product.getUnitRatio() > 0) {
                holder.btnTotalCount.setText(formatCount(product.getSelectedCount() / product.getUnitRatio()));
            } else {
                holder.btnTotalCount.setText(ServiceTools.formatCount(0));
            }

        } else {
            holder.panelTotalAsset.setVisibility(View.GONE);
            holder.panelTotalCount.setVisibility(View.GONE);
        }

        holder.tvProductName.setText(product.getName());
        holder.tvAsset.setText(formatCount(ServiceTools.getSumCount1(arrayOrginal.get(position).getProductId(), mContext)));

//            tvAsset2.setText(String.valueOf(product.getAsset2()));
        holder.tvInbox.setText(formatCount(product.getUnitRatio()));
        if (productDetails.size() > 0)
            holder.tvPrice.setText(ServiceTools.getPriceFormated(productDetails.get(0).getProductDetailId(), mContext));
        holder.tvUnit.setText(product.getUnitName());
//            tvUnit2.setText(product.getUnitName2());
        if (product.getPictures() != null && product.getPictures().size() > 0) {
//            Glide.with(mContext).load(product.getPictures().get(0).getUrl()).placeholder(R.drawable.image_empty_box).crossFade().listener(ServiceTools.getGlideListener(holder.imgProduct))
//                    .into(holder.imgProduct);
            loadImage(mContext, product.getPictures().get(0).getUrl(), holder.imgProduct);
        } else {
            holder.imgProduct.setImageResource(R.drawable.img_default_product);
            holder.imgProduct.setBackgroundResource(R.drawable.image_empty_box);

        }

        holder.txtCount.setText(formatCount(product.getSelectedCount()));

        /*holder.txtCount.setOnClickListener(getClickListenerCountProduct(product, fragmentManager, false, holder));
        if (product.getUnitRatio() > 0)
            holder.btnTotalCount.setOnClickListener(getClickListenerCountProduct(product, fragmentManager, true, holder));*/

        holder.recyclerImage.setAdapter(getAdapterRecyclerImageProduct(product, holder.imgProduct));
        holder.imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product.getPictures() == null || product.getPictures().size() == 0)
                    return;
                Intent intent = new Intent(mContext, PhotoViewerActivity.class);
                intent.putExtra(ProjectInfo._json_key_user_id, BaseActivity.getPrefUserId());
                intent.putExtra(ProjectInfo._json_key_product_id, product.getProductCode());
                intent.putExtra(ProjectInfo._json_key_index, selectedPhotoindex);
                mContext.startActivity(intent);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCount = (TextView) v.findViewById(R.id.txtCountKol);
                btnPrice = (TextView) v.findViewById(R.id.tvPrice);
                showCountPriceDialog(position, holder.tvPrice.getText().toString(), holder.txtCount.getText().toString());
            }
        });

    }


    void showCountPriceDialog(int position, String price, String count) {
    }


    private DisplayImageOptions getOptionImageLoader() {
        if (mContext instanceof ProductsListActivity) {
            return ProductsListActivity.options;
        } else if (mContext instanceof ProductPickerListActivity) {
            return ProductPickerListActivity.options;
        }
        return null;
    }

    @NonNull
    private RecyclerView.Adapter getAdapterRecyclerImageProduct(final Product product, final ImageView imgProduct) {
        return new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                ImageView view = new ImageView(mContext);
                int size = mContext.getResources().getDimensionPixelSize(R.dimen.product_gallery_other_image_size);
                ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(size, size);
                view.setLayoutParams(params);
                size = ServiceTools.dpToPX(mContext, 5);
                params.setMargins(size / 2, size, size / 2, size);
                view.setPadding(size, size, size, size);
                view.setScaleType(ImageView.ScaleType.FIT_CENTER);
                ViewHolder viewHolder = new ViewHolder(view);

                return viewHolder;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

                ViewHolder mHolder = (ViewHolder) holder;
                final PicturesProduct picturesProduct = product.getPictures().get(position);
//                Glide.with(mContext).load(picturesProduct.getUrl()).placeholder(R.drawable.image_empty_box).crossFade().listener(ServiceTools.getGlideListener(mHolder.imgProduct))
//                        .into(mHolder.imgProduct);
                loadImage(mContext, picturesProduct.getUrl(), mHolder.imgProduct);
                mHolder.imgProduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedPhotoindex = position;
//                        ImageLoader.getInstance().displayImage(product.getPictures().get(position).getUrl(), imgProduct, getOptionImageLoader(), ServiceTools.getImageLoaderListener(R.drawable.img_default_product));
//                        Glide.with(mContext).load(picturesProduct.getUrl()).placeholder(R.drawable.image_empty_box).crossFade()
//                                .listener(ServiceTools.getGlideListener(imgProduct)).into(imgProduct);
                        loadImage(mContext, product.getPictures().get(position).getUrl(), imgProduct);

                    }
                });
            }

            @Override
            public int getItemCount() {
                return product.getPictures() == null ? 0 : product.getPictures().size();
            }
        };
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView imgProduct;

        public ViewHolder(ImageView v) {
            super(v);
            imgProduct = v;
        }
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        RecyclerView recyclerImage;
        TextView tvProductName;
        TextView tvAsset;
        TextView tvAsset2;
        TextView tvInbox;
        TextView tvPrice;
        TextView tvUnit;
        TextView tvUnit2;
        TextView txtCount;
        Button btnTotalCount;
        View panelTotalAsset, panelTotalCount;

        public Holder(View view) {
            super(view);
            imgProduct = (ImageView) view.findViewById(R.id.imgProduct);
            recyclerImage = (RecyclerView) view.findViewById(R.id.recyclerImage);
            tvProductName = (TextView) view.findViewById(R.id.tvName);
            tvAsset = (TextView) view.findViewById(R.id.tvAsset);
            tvAsset2 = (TextView) view.findViewById(R.id.tvAsset2);
            tvInbox = (TextView) view.findViewById(R.id.tvInbox);
            tvPrice = (TextView) view.findViewById(R.id.tvPrice);
            tvUnit = (TextView) view.findViewById(R.id.tvUnit);
            tvUnit2 = (TextView) view.findViewById(R.id.tvUnit2);
            txtCount = (TextView) view.findViewById(R.id.txtCountKol);
            btnTotalCount = (Button) view.findViewById(R.id.txtTotalCount);
            panelTotalAsset = view.findViewById(R.id.panelTotalAsset);
            panelTotalCount = view.findViewById(R.id.panelTotalCount);
        }
    }

    public static void loadImage(Context context, String url, ImageView imageView) {
        if (url != null) {
            int index = url.lastIndexOf("/") + 1;
            String namePicture = url.substring(index);
            try {
//            String fPath = ProjectInfo.DIRECTORY_MAHAKORDER + "/" + ProjectInfo.DIRECTORY_IMAGES + "/" + ProjectInfo.DIRECTORY_PRODUCTS;
                String FilePath = ServiceTools.getKeyFromSharedPreferences(context, BaseActivity.getPrefUserMasterId(context) + "");
                File Directory = new File(FilePath, namePicture);
                if (Directory.exists()) {
                    Glide.with(context).load(Directory).placeholder(R.drawable.image_empty_box).crossFade()
                            .listener(ServiceTools.getGlideFileListener(imageView)).into(imageView);
                    return;
                }
            } catch (Exception e) {
                FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                FirebaseCrashlytics.getInstance().recordException(e);
                e.printStackTrace();
            }
            Glide.with(context).load(url).placeholder(R.drawable.image_empty_box).crossFade()
                    .listener(ServiceTools.getGlideListener(imageView)).into(imageView);
        }

    }

}
