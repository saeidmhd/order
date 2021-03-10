package com.mahak.order.scan;

import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.mahak.order.R;

public class SmallCaptureActivity extends CaptureActivity {

    @Override
    protected DecoratedBarcodeView initializeContent() {
        setContentView(R.layout.activity_small_capture);
        return (DecoratedBarcodeView) findViewById(R.id.zxing_barcode_scanner);
    }
}
