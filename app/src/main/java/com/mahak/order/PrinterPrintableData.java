package com.mahak.order;


import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.kishcore.sdk.hybrid.api.PrintableData;


public class PrinterPrintableData implements PrintableData {

    Bitmap mBitmap;


    public PrinterPrintableData(Bitmap bPrint) {

        mBitmap = bPrint;
    }

    @Override
    public View toView(Context context) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.print_layout, null);
        ImageView imageView = (ImageView) root.findViewById(R.id.imgPrint);
        imageView.setImageBitmap(Bitmap.createScaledBitmap(mBitmap, 384, mBitmap.getHeight(), false));
        return root;
    }

    @Override
    public int getHeight() {
        return 0;
    }

}
