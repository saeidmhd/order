package com.mahak.order.service;

import android.content.Context;
import android.os.Environment;

import com.mahak.order.BaseActivity;
import com.mahak.order.common.PicturesProduct;
import com.mahak.order.common.Product;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.storage.DbAdapter;

import java.io.File;
import java.util.List;

/**
 * Created by admin on 11/24/16.
 */
public class ReadOfflinePicturesProducts {

    Context context;
    DbAdapter dba;

    public ReadOfflinePicturesProducts(Context context) {
        this.context = context;
        dba = new DbAdapter(context);
    }

    public void readAllImages() {

        String filePath = ServiceTools.getKeyFromSharedPreferences(context, BaseActivity.getPrefUserMasterId(context) + "");
        if (filePath.equals(""))
            return;

        File file = new File(filePath);
        File[] files = file.listFiles();
        if (files == null)
            return;

        dba.open();

        for (File file1 : files) {
            if (file1.getName().toLowerCase().contains(".jpg") || file1.getName().toLowerCase().contains(".png") || file1.getName().toLowerCase().contains(".jpeg")) {
                int index = file1.getName().indexOf(".");
                String pictureName = file1.getName().substring(0, index);
                String[] details = pictureName.split("_");
                if (details.length == 2) {
                    String dataBaseId = BaseActivity.getPrefDatabaseId(context);
                    String masterId = details[0];
                    String number = details[1];
                    Product product = dba.getProductByMasterIdAndDataBaseId(masterId, dataBaseId);
                    if (product.getProductCode() != 0) {
                        PicturesProduct picturesProduct = dba.getPictureProductByMasterIdAndDataBaseIdAndPictureId(masterId, dataBaseId, number);
                        if (picturesProduct.getFileName() == null) {
                            picturesProduct = new PicturesProduct();
                            picturesProduct.setDataBaseId(dataBaseId);
                            picturesProduct.setFileName(file1.getName());
                            picturesProduct.setFileSize(file1.getTotalSpace());
                            picturesProduct.setLastUpdate(System.currentTimeMillis());
                            picturesProduct.setItemId(product.getProductCode());
                            picturesProduct.setMahakId(product.getMahakId());
                            picturesProduct.setPictureCode(ServiceTools.toLong(masterId));
                            picturesProduct.setPictureId(ServiceTools.toLong(number));
                            picturesProduct.setUrl(file1.getPath());
                            picturesProduct.setTitle(file1.getName());
                            picturesProduct.setProductId(ServiceTools.toLong(masterId));
                            picturesProduct.setUserId(BaseActivity.getPrefUserId(context));
                            dba.addPicturesProductOffline(picturesProduct);
                        }
                    }
                }
            }
        }

        dba.close();
    }


}
