package com.mahak.order.oscar_printer;

import android.content.Intent;

import com.android.print.sdk.PrinterInstance;

public interface IPrinterOpertion {
    void open(Intent data);

    void close();

    void chooseDevice();

    PrinterInstance getPrinter();
}
