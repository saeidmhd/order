package com.mahak.order.service;

import android.content.Context;
import android.os.Environment;

import com.mahak.order.BaseActivity;
import com.mahak.order.common.PhotoGallery;
import com.mahak.order.common.PicturesProduct;
import com.mahak.order.common.Product;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.storage.DbAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
            String fileName = file1.getName().toLowerCase();
            if (fileName.contains(".jpg") || file1.getName().toLowerCase().contains(".png") || file1.getName().toLowerCase().contains(".jpeg")) {
                int index = file1.getName().indexOf(".");
                String pictureName = file1.getName().substring(0, index);
                String[] details = pictureName.split("_");
                if (details.length == 2) {
                    String dataBaseId = BaseActivity.getPrefDatabaseId(context);
                    String masterId = details[0];
                    String number = details[1];
                    String hash = ServiceTools.computeMD5Hash(masterId+number);
                    Product product = dba.getProductByMasterIdAndDataBaseId(masterId, dataBaseId);
                    if (product.getProductCode() != 0) {
                        PicturesProduct picturesProduct = dba.getPictureProductByMasterIdAndDataBaseIdAndPictureId(masterId, dataBaseId, fileName);
                        if (picturesProduct.getFileName() == null) {
                            String pictureId = masterId + number + product.getProductId();
                            addPictureProduct(file1, dataBaseId, masterId, product , hash , pictureId);
                            addPhotoGalley(product , pictureId);
                        }
                    }
                }
            }
        }

        dba.close();
    }

    private void addPhotoGalley( Product product , String pictureId) {
        PhotoGallery photoGallery = new PhotoGallery();
        ArrayList<PhotoGallery> photoGalleries = new ArrayList<>();
        photoGallery.setPictureId(ServiceTools.toLong(pictureId));
        photoGallery.setItemCode(product.getProductId());
        photoGalleries.add(photoGallery);
        dba.UpdateOrAddPhotoGallery(photoGalleries,0);
    }

    private void addPictureProduct(File file1, String dataBaseId, String masterId, Product product , String hash  , String pictureId) {
        PicturesProduct picturesProduct;
        picturesProduct = new PicturesProduct();
        picturesProduct.setDataBaseId(dataBaseId);
        picturesProduct.setFileName(file1.getName().toLowerCase());
        picturesProduct.setFileSize(file1.getTotalSpace());
        picturesProduct.setLastUpdate(System.currentTimeMillis());
        picturesProduct.setItemId(product.getProductCode());
        picturesProduct.setMahakId(product.getMahakId());
        picturesProduct.setPictureCode(ServiceTools.toLong(masterId));
        picturesProduct.setPictureId(ServiceTools.toLong(pictureId));
        picturesProduct.setUrl(file1.getPath());
        picturesProduct.setTitle(file1.getName());
        picturesProduct.setProductId(product.getProductId());
        picturesProduct.setUserId(BaseActivity.getPrefUserId(context));
        picturesProduct.setDataHash(hash);
        dba.addPicturesProductOffline(picturesProduct);
    }


}
