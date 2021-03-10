package com.mahak.order.utils;

import android.content.Context;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.mahak.order.R;

public class HeaderFooterPageEvent extends PdfPageEventHelper {
    Context context;

    public HeaderFooterPageEvent(Context mContext) {
        context = mContext;
    }

    public void onEndPage(PdfWriter writer, Document document) {
        Phrase ph1 = new Phrase();
        PersianTextPdf.addText(context.getString(R.string.page) + document.getPageNumber(), ph1, 14, BaseColor.BLACK);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, ph1, 550, 30, 0);
    }
}
