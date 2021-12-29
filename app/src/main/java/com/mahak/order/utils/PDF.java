package com.mahak.order.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.mahak.order.BaseActivity;
import com.mahak.order.common.Customer;
import com.mahak.order.common.GroupedTax;
import com.mahak.order.common.Order;
import com.mahak.order.common.OrderDetail;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.SharedPreferencesHelper;
import com.mahak.order.storage.DbAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.mahak.order.common.ServiceTools.getDateAndTimeForLong;


public class PDF {

    public static final String FileNamePDF = "Pdf";
    //public static final File DATABASE_DIRECTORY = new File(Environment.getExternalStorageDirectory(), BaseActivity.MAHAK_BACKUP);
    public static final File DATABASE_DIRECTORY = new File(Environment.getExternalStorageDirectory(), ProjectInfo.DIRECTORY_MAHAKORDER + "/" + ProjectInfo.DIRECTORY_BACKUPS);
    public static File DIRECTORY_PDF = new File(DATABASE_DIRECTORY + "/" + FileNamePDF);

    private DbAdapter dba;
    private final String code;
    private List<OrderDetail> AccArray;
    private final Context mContext;
    private final int CountRowTable = 22;
    private final int txtSize_small = 10;
    private final int txtSize = 11;
    private final int txtTitleSize = 14;
    private PdfWriter writer;

    private long startDate;
    private long endDate;

    private double TotalPrice, FinalPrice, Discount, Payable;
    private double TotalCount = 0;
    private int PackageCount = 0;
    private double TotalItems = 0;
    double TotalOff = 0, TotalReceipt = 0, RemainigOrder = 0;
    double RemainedCustomer = 0, FinalRemainedCustomer = 0;
    double TotalCharge = 0;
    double TotalTax = 0;
    double Tax, Charge, off;
    double ChargePercent, TaxPercent;
    double offValue;
    private double TotalTaxAndCharge;
    private Order order;
    private ArrayList<GroupedTax> groupedTaxes;
    private Customer customer;
    public static int OrderType = 0;
    private ArrayList<OrderDetail> orderDetails;

    public PDF(Context context, String code) {
        this.code = code;
        this.mContext = context;
    }

    public boolean exportOrderDetailToPdf() {
        try {
            if (dba == null)
                dba = new DbAdapter(mContext);
            dba.open();
            order = dba.GetOrder(code);

            if (order.getPersonId() == ProjectInfo.CUSTOMERID_GUEST) {
                customer = dba.getCustomerWithPersonClientId(order.getPersonClientId());
            } else if (order.getPersonId() != ProjectInfo.CUSTOMERID_GUEST) {
                customer = dba.getCustomerWithPersonId(order.getPersonId());
            }

            CalculateTotal();
            Document document = new Document(PageSize.A4);
            File PDFDir = DIRECTORY_PDF;
            if (!PDFDir.exists()) {
                PDFDir.mkdirs();
            }

            File pdfFile = new File(PDFDir, code + ".pdf");
            writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            HeaderFooterPageEvent event = new HeaderFooterPageEvent(mContext);
            writer.setPageEvent(event);
            document.open();
            CreateTable(document, 0);
            document.close();
            dba.close();
            return true;

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
            return false;
        }
    }


    private void FillView() {
        if (dba == null)
            dba = new DbAdapter(mContext);
        dba.open();

        if (SharedPreferencesHelper.getCurrentLanguage(mContext).equals("de_DE")) {
            groupedTaxes = dba.getGroupedTaxCharge(order.getOrderId());
        }

        orderDetails = new ArrayList<>();

        // order = dba.GetOrderWithOrderId(OrderId);


        if (order.getPersonId() == ProjectInfo.CUSTOMERID_GUEST) {
            customer = dba.getCustomerWithPersonClientId(order.getPersonClientId());
        } else if (order.getPersonId() != ProjectInfo.CUSTOMERID_GUEST) {
            customer = dba.getCustomerWithPersonId(order.getPersonId());
        }

        if (OrderType == ProjectInfo.TYPE_RETURN_OF_SALE) {

            //            Discount = (order.getDiscount() == null) ? 0 :  ServiceTools.toLong(order.getDiscount());
            //Read TotalReceipt___________________________________________________________
            TotalReceipt = dba.getTotalReceiptFromOrder(order.getCode());

            //Read From Database ProductInOrder and Computing FinalPrice____________________
            orderDetails = dba.getAllProductWithOrderDetail(order.getId());
            //Parse Json Description _______________________________________________________
            String Description = order.getDescription();

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(order.getOrderDate());
            TotalItems = orderDetails.size();
            String date = getDateAndTimeForLong(order.getOrderDate());

        } else {
            CalculateTotal();
            TotalItems = orderDetails.size();
            //Parse Json Description _______________________________________________________
            String Description = order.getDescription();
            //Fill TextView_________________________________________________________________
            {
                customer.getName();
                customer.getOrganization();
            }
        }
    }

    private void CalculateTotal() {
        TotalPrice = 0;
        TotalCharge = 0;
        TotalTax = 0;
        TotalOff = 0;
        FinalPrice = 0;
        Discount = 0;
        Payable = 0;
        //Read Order From Database_____________________________________________________
        Discount = order.getDiscount();
        //Read TotalReceipt___________________________________________________________
        TotalReceipt = dba.getTotalReceiptFromOrder(order.getCode());
        //Read From Database ProductInOrder and Computing FinalPrice____________________
        AccArray = dba.getAllProductWithOrderDetail(order.getId());
        TotalItems = AccArray.size();
        for (OrderDetail item : AccArray) {
            double Price = (item.getPrice() * item.getSumCountBaJoz());
            offValue = item.getDiscount();
            TaxPercent = item.getTaxPercent();
            ChargePercent = item.getChargePercent();
            TotalCount += item.getSumCountBaJoz();
            PackageCount += item.getCount2();
            TotalPrice += Price;
            off = (offValue * 1);
            Price = Price - off;
            Tax = ((Price * TaxPercent) / 100);
            Charge = ((Price * ChargePercent) / 100);
            TotalCharge += Charge;
            TotalTax += Tax;
            TotalOff += off;
        }
        TotalOff += Discount;
        TotalTaxAndCharge = TotalTax + TotalCharge;
        FinalPrice = (TotalPrice - TotalOff) + (TotalTaxAndCharge);
        RemainigOrder = FinalPrice - TotalReceipt;
    }

    private void CreateTable(Document document, int index) throws DocumentException {

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        //table.setWidths(new float[]{1.4f,1.4f,4.0f,1.0f,3.5f,1.8f});
        table.setWidths(new float[]{6.0f, 6.0f, 6.0f, 2.5f, 12.0f});

        int CountMain = AccArray.size();
        int CountPage = getCountIndexArray(CountMain);
        int k = 0;

        //addImage(document);

        addTitlePage(document, index);

        //Color
        BaseColor LightGraycolor = new BaseColor(224, 224, 224);
        BaseColor LightGreenColor = new BaseColor(187, 254, 188);
        BaseColor whiteColor = new BaseColor(255, 255, 255);

        //Paragraph
        //1
        Phrase ph1 = new Phrase();
        PersianTextPdf.addText("مبلغ نهایی", ph1, txtSize_small, BaseColor.BLACK);
        PdfPCell c1 = new PdfPCell(ph1);
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setVerticalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(LightGraycolor);
        c1.setFixedHeight(30f);
        table.addCell(c1);

        //3
        ph1 = new Phrase();
        PersianTextPdf.addText("مبلغ کل", ph1, txtSize_small, BaseColor.BLACK);
        c1 = new PdfPCell(ph1);
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setVerticalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(LightGraycolor);
        c1.setFixedHeight(30f);
        table.addCell(c1);

        //4
        ph1 = new Phrase();
        PersianTextPdf.addText("فی", ph1, txtSize_small, BaseColor.BLACK);
        c1 = new PdfPCell(ph1);
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setVerticalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(LightGraycolor);
        c1.setFixedHeight(30f);
        table.addCell(c1);

        //6
        ph1 = new Phrase();
        PersianTextPdf.addText("مقدار", ph1, txtSize_small, BaseColor.BLACK);
        c1 = new PdfPCell(ph1);
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setVerticalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(LightGraycolor);
        c1.setFixedHeight(30f);
        table.addCell(c1);
        //7
        ph1 = new Phrase();
        PersianTextPdf.addText("مشخصات کالا", ph1, txtSize_small, BaseColor.BLACK);
        c1 = new PdfPCell(ph1);
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setVerticalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(LightGraycolor);
        c1.setFixedHeight(30f);
        table.addCell(c1);

        table.setHeaderRows(1);

        for (int i = 0; i < AccArray.size(); i++) {

            //1- mablaghe nahaee
            Phrase phrase = new Phrase();
            PdfPCell c2 = new PdfPCell(phrase);
            PersianTextPdf.addText(ServiceTools.formatPriceIgnorLocal(getFinalPrice(AccArray.get(i))), phrase, txtSize, BaseColor.BLACK);
            c2.setHorizontalAlignment(Element.ALIGN_CENTER);
            c2.setVerticalAlignment(Element.ALIGN_CENTER);
            c2.setFixedHeight(25f);
            if (isOdd(i))
                c2.setBackgroundColor(LightGreenColor);
            table.addCell(c2);

            //3- mablaghe kol
            phrase = new Phrase();
            PersianTextPdf.addText(ServiceTools.formatPriceIgnorLocal(getTotalPrice(AccArray.get(i))), phrase, txtSize, BaseColor.BLACK);
            c2 = new PdfPCell(phrase);
            c2.setHorizontalAlignment(Element.ALIGN_CENTER);
            c2.setVerticalAlignment(Element.ALIGN_CENTER);
            c2.setFixedHeight(25f);
            if (isOdd(i))
                c2.setBackgroundColor(LightGreenColor);
            table.addCell(c2);

            //4- fi
            phrase = new Phrase();
            PersianTextPdf.addText(ServiceTools.formatPriceIgnorLocal(AccArray.get(i).getPrice()), phrase, txtSize, BaseColor.BLACK);
            c2 = new PdfPCell(phrase);
            c2.setHorizontalAlignment(Element.ALIGN_CENTER);
            c2.setVerticalAlignment(Element.ALIGN_CENTER);
            c2.setFixedHeight(25f);
            if (isOdd(i))
                c2.setBackgroundColor(LightGreenColor);
            table.addCell(c2);


            //6 -meghdar
            //String strAmount = String.valueOf(String.format(Locale.US,"%,d",AccArray.get(index).getLstTran().get(k).getAmount()));
            phrase = new Phrase();
            PersianTextPdf.addText(ServiceTools.formatPriceIgnorLocal(AccArray.get(i).getSumCountBaJoz()), phrase, txtSize, BaseColor.BLACK);
            c2 = new PdfPCell(phrase);
            c2.setHorizontalAlignment(Element.ALIGN_CENTER);
            c2.setVerticalAlignment(Element.ALIGN_CENTER);
            c2.setFixedHeight(25f);
            if (isOdd(i))
                c2.setBackgroundColor(LightGreenColor);
            table.addCell(c2);

            //7 -product name
            phrase = new Phrase();
            PersianTextPdf.addText(AccArray.get(i).getProductName(), phrase, txtSize, BaseColor.BLACK);
            c2 = new PdfPCell(phrase);
            c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            c2.setVerticalAlignment(Element.ALIGN_CENTER);
            c2.setPaddingRight(5f);
            c2.setFixedHeight(25f);
            if (isOdd(i))
                c2.setBackgroundColor(LightGreenColor);
            table.addCell(c2);
        }

        //1- mablaghe nahaee
        Phrase elements = new Phrase();
        PdfPCell c2 = new PdfPCell(elements);
        PersianTextPdf.addText(ServiceTools.formatPriceIgnorLocal(FinalPrice), elements, txtSize, BaseColor.BLACK);
        c2.setHorizontalAlignment(Element.ALIGN_CENTER);
        c2.setVerticalAlignment(Element.ALIGN_CENTER);
        c2.setFixedHeight(25f);
        c2.setBackgroundColor(LightGraycolor);
        table.addCell(c2);

        //3- mablaghe kol
        elements = new Phrase();
        PersianTextPdf.addText(ServiceTools.formatPriceIgnorLocal(TotalPrice), elements, txtSize, BaseColor.BLACK);
        c2 = new PdfPCell(elements);
        c2.setHorizontalAlignment(Element.ALIGN_CENTER);
        c2.setVerticalAlignment(Element.ALIGN_CENTER);
        c2.setFixedHeight(25f);
        c2.setBackgroundColor(LightGraycolor);
        table.addCell(c2);

        //4- fi
        elements = new Phrase();
        PersianTextPdf.addText("", elements, txtSize, BaseColor.BLACK);
        c2 = new PdfPCell(elements);
        c2.setHorizontalAlignment(Element.ALIGN_CENTER);
        c2.setVerticalAlignment(Element.ALIGN_CENTER);
        c2.setFixedHeight(25f);
        c2.setBackgroundColor(LightGraycolor);
        table.addCell(c2);

        //6 -meghdar
        //String strAmount = String.valueOf(String.format(Locale.US,"%,d",AccArray.get(index).getLstTran().get(k).getAmount()));
        elements = new Phrase();
        PersianTextPdf.addText(ServiceTools.formatPriceIgnorLocal(TotalCount), elements, txtSize, BaseColor.BLACK);
        c2 = new PdfPCell(elements);
        c2.setHorizontalAlignment(Element.ALIGN_CENTER);
        c2.setVerticalAlignment(Element.ALIGN_CENTER);
        c2.setFixedHeight(25f);
        c2.setBackgroundColor(LightGraycolor);
        table.addCell(c2);

        //7 -product name
        elements = new Phrase();
        PersianTextPdf.addText("تعداد: " + ServiceTools.formatPriceIgnorLocal(TotalItems), elements, txtSize, BaseColor.BLACK);
        c2 = new PdfPCell(elements);
        c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c2.setVerticalAlignment(Element.ALIGN_RIGHT);
        c2.setFixedHeight(25f);
        c2.setBackgroundColor(LightGraycolor);
        table.addCell(c2);

        document.add(table);

        /////////////////////////////////////////////////////////////

        addSpaceDocument(document);


        table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1f, 1f});
        //3- mablaghe kol
        Phrase mablagheKolElements = new Phrase();
        PersianTextPdf.addText(ServiceTools.formatPriceIgnorLocal(TotalPrice), mablagheKolElements, txtSize, BaseColor.BLACK);
        PdfPCell c4 = new PdfPCell(mablagheKolElements);
        c4.setHorizontalAlignment(Element.ALIGN_CENTER);
        c4.setVerticalAlignment(Element.ALIGN_CENTER);
        c4.setFixedHeight(25f);
        c4.setBackgroundColor(LightGraycolor);
        table.addCell(c4);

        mablagheKolElements = new Phrase();
        c4 = new PdfPCell(mablagheKolElements);
        PersianTextPdf.addText("جمع مبلغ کل", mablagheKolElements, txtSize, BaseColor.BLACK);
        c4.setHorizontalAlignment(Element.ALIGN_CENTER);
        c4.setVerticalAlignment(Element.ALIGN_CENTER);
        c4.setFixedHeight(25f);
        c4.setBackgroundColor(LightGraycolor);
        table.addCell(c4);

        document.add(table);

        table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1f, 1f});
        //3- mablaghe kol
        Phrase takhfifSatriKolElements = new Phrase();
        PersianTextPdf.addText(ServiceTools.formatPriceIgnorLocal(TotalOff), takhfifSatriKolElements, txtSize, BaseColor.BLACK);
        PdfPCell c5 = new PdfPCell(takhfifSatriKolElements);
        c5.setHorizontalAlignment(Element.ALIGN_CENTER);
        c5.setVerticalAlignment(Element.ALIGN_CENTER);
        c5.setFixedHeight(25f);
        c5.setBackgroundColor(LightGraycolor);
        table.addCell(c5);

        takhfifSatriKolElements = new Phrase();
        c5 = new PdfPCell(takhfifSatriKolElements);
        PersianTextPdf.addText("کسر می شود : جمع تخفیفات سطری و زیر فاکتور", takhfifSatriKolElements, txtSize, BaseColor.BLACK);
        c5.setHorizontalAlignment(Element.ALIGN_CENTER);
        c5.setVerticalAlignment(Element.ALIGN_CENTER);
        c5.setFixedHeight(25f);
        c5.setBackgroundColor(LightGraycolor);
        table.addCell(c5);

        document.add(table);


        table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1f, 1f});
        //3- mablaghe kol
        Phrase taxChargeElements = new Phrase();
        PersianTextPdf.addText(ServiceTools.formatPriceIgnorLocal(TotalTaxAndCharge), taxChargeElements, txtSize, BaseColor.BLACK);
        PdfPCell c6 = new PdfPCell(taxChargeElements);
        c6.setHorizontalAlignment(Element.ALIGN_CENTER);
        c6.setVerticalAlignment(Element.ALIGN_CENTER);
        c6.setFixedHeight(25f);
        c6.setBackgroundColor(LightGraycolor);
        table.addCell(c6);

        taxChargeElements = new Phrase();
        c6 = new PdfPCell(taxChargeElements);
        PersianTextPdf.addText("اضافه می گردد : جمع عوارض و مالیات ", taxChargeElements, txtSize, BaseColor.BLACK);
        c6.setHorizontalAlignment(Element.ALIGN_CENTER);
        c6.setVerticalAlignment(Element.ALIGN_CENTER);
        c6.setFixedHeight(25f);
        c6.setBackgroundColor(LightGraycolor);
        table.addCell(c6);

        document.add(table);


        table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1f, 1f});

        //3- mablaghe kol
        Phrase footerElements = new Phrase();
        PersianTextPdf.addText(ServiceTools.formatPriceIgnorLocal(FinalPrice), footerElements, txtSize, BaseColor.BLACK);
        PdfPCell c3 = new PdfPCell(footerElements);
        c3.setHorizontalAlignment(Element.ALIGN_CENTER);
        c3.setVerticalAlignment(Element.ALIGN_CENTER);
        c3.setFixedHeight(25f);
        c3.setBackgroundColor(LightGraycolor);
        table.addCell(c3);

        footerElements = new Phrase();
        c3 = new PdfPCell(footerElements);
        PersianTextPdf.addText("قابل پرداخت", footerElements, txtSize, BaseColor.BLACK);
        c3.setHorizontalAlignment(Element.ALIGN_CENTER);
        c3.setVerticalAlignment(Element.ALIGN_CENTER);
        c3.setFixedHeight(25f);
        c3.setBackgroundColor(LightGraycolor);
        table.addCell(c3);
        document.add(table);

        addSpaceDocument(document);

        addFooterPage(document, Element.ALIGN_RIGHT, "امضا خریدار");

        addImage(document);

    }

    private boolean isOdd(int i) {
        return i % 2 != 0;

    }

    private void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    private void addLineSeparator(Paragraph paragraph) {
        LineSeparator lineSeparator = new LineSeparator();
        // Adding Horizontal Line...
        paragraph.add(lineSeparator);

    }

    private double getTotalPrice(OrderDetail orderDetail) {
        double Price = orderDetail.getPrice();
        return (Price * orderDetail.getSumCountBaJoz());

    }

    private double getFinalPrice(OrderDetail orderDetail) {

        double TotalPrice = getTotalPrice(orderDetail);
        double TaxAndCharge = 0;
        double off = 0;
        double Tax, Charge;
        double offValue = orderDetail.getDiscount();
        double TaxPercent = orderDetail.getTaxPercent();
        double ChargePercent = orderDetail.getChargePercent();

        off = (offValue * 1);
        TotalPrice = TotalPrice - off;
        Tax = ((TotalPrice * TaxPercent) / 100);
        Charge = ((TotalPrice * ChargePercent) / 100);
        TaxAndCharge = Tax + Charge;

        return TotalPrice + TaxAndCharge;
    }

    private double getTaxAndCharge(OrderDetail orderDetail) {
        double Price = orderDetail.getPrice();
        double TotalPrice = 0;

        double off = 0;
        double Tax, Charge;
        double offValue = orderDetail.getDiscount();
        double TaxPercent = orderDetail.getTaxPercent();
        double ChargePercent = orderDetail.getChargePercent();

        TotalPrice = (Price * orderDetail.getSumCountBaJoz());

        off = (offValue * 1);
        TotalPrice = TotalPrice - off;
        Tax = ((TotalPrice * TaxPercent) / 100);
        Charge = ((TotalPrice * ChargePercent) / 100);
        return Tax + Charge;

    }


    private void addSpaceDocument(Document document) throws DocumentException {

        try {

            Paragraph TitleParagraph = new Paragraph();
            addEmptyLine(TitleParagraph, 1);

            document.add(TitleParagraph);
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            // TODO: handle exception
        }
    }

    private void addTitlePage(Document document, int index) throws DocumentException {

        try {

            Paragraph TitleParagraph = new Paragraph();

            Paragraph orderTypeParagraph = new Paragraph();
            String orderTypeTitle = ServiceTools.getOrderTitle(order.getOrderType(), mContext);
            PersianTextPdf.addText(orderTypeTitle, orderTypeParagraph, txtTitleSize, BaseColor.BLACK);
            orderTypeParagraph.setAlignment(Element.ALIGN_CENTER);
            TitleParagraph.add(orderTypeParagraph);
            addLineSeparator(TitleParagraph);
            addEmptyLine(TitleParagraph, 1);

            Paragraph userParagraph = new Paragraph();
            String user = BaseActivity.getUserProfile().getName();
            PersianTextPdf.addText(user, userParagraph, txtTitleSize, BaseColor.BLACK);
            PersianTextPdf.addText("کاربر : ", userParagraph, txtTitleSize, BaseColor.BLACK);
            userParagraph.setAlignment(Element.ALIGN_RIGHT);
            TitleParagraph.add(userParagraph);
            addEmptyLine(TitleParagraph, 1);


            Paragraph dateParag = new Paragraph();
            String date = getDateAndTimeForLong(order.getOrderDate());
            PersianTextPdf.addText(date, dateParag, txtTitleSize, BaseColor.BLACK);
            PersianTextPdf.addText("تاریخ و ساعت : ", dateParag, txtTitleSize, BaseColor.BLACK);
            dateParag.setAlignment(Element.ALIGN_RIGHT);
            TitleParagraph.add(dateParag);
            addLineSeparator(TitleParagraph);
            addEmptyLine(TitleParagraph, 1);


            Paragraph customerParagraph = new Paragraph();
            String customerName = customer.getName();
            PersianTextPdf.addText(customerName, customerParagraph, txtTitleSize, BaseColor.BLACK);
            PersianTextPdf.addText("نام مشتری : ", customerParagraph, txtTitleSize, BaseColor.BLACK);
            customerParagraph.setAlignment(Element.ALIGN_RIGHT);
            TitleParagraph.add(customerParagraph);
            addEmptyLine(TitleParagraph, 1);


            Paragraph trackingCodeParagraph = new Paragraph();
            String trackingCode = order.getCode();
            PersianTextPdf.addText(trackingCode, trackingCodeParagraph, txtTitleSize, BaseColor.BLACK);
            PersianTextPdf.addText("کد پیگیری : ", trackingCodeParagraph, txtTitleSize, BaseColor.BLACK);
            trackingCodeParagraph.setAlignment(Element.ALIGN_RIGHT);
            TitleParagraph.add(trackingCodeParagraph);
            addEmptyLine(TitleParagraph, 1);


            document.add(TitleParagraph);
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            // TODO: handle exception
        }
    }

    private void addFooterPage(Document document, int alignment, String strFooter) throws DocumentException {

        try {

            Paragraph TitleParagraph = new Paragraph();
            Paragraph orderTypeParagraph = new Paragraph();

            PersianTextPdf.addText(strFooter, orderTypeParagraph, txtTitleSize, BaseColor.BLACK);
            orderTypeParagraph.setAlignment(alignment);

            TitleParagraph.add(orderTypeParagraph);


            document.add(TitleParagraph);
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            // TODO: handle exception
        }
    }

        /*@NonNull
        private PersianDate getPersianDate(long startDate) {
            Date dt = new Date();
            dt.setTime(startDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dt);
            CivilDate cdt = new CivilDate();
            cdt.setCalendar(cal);
            return DateConverter.civilToPersian(cdt);
        }*/

    private void addImage(Document document) throws DocumentException {

        try {


            String FileName = String.format("Signature_%s.png", code);
            Bitmap bitmapSign = ServiceTools.getSign(FileName);
            bitmapSign = Bitmap.createScaledBitmap(bitmapSign, 150, 150, false);
            //InputStream ims = mContext.getAssets().open("logo/logo.png");
            //Bitmap bmp = BitmapFactory.decodeStream(ims);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmapSign.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());
            image.setAlignment(Image.RIGHT);

            document.add(image);

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("Exception ", e.getMessage());
        }
    }

    private int getCountIndexArray(int Count) {
        int CountPage = Count / CountRowTable;
        return CountPage + 1;
    }

    private String escape_string(String str) {

        String repStr = "";
        repStr = str.replaceAll("[-+.^:,()]", "");


        return repStr;
    }

    public static boolean SdIsPresent() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static boolean deletePDF(String PdfFileName) {
        if (!SdIsPresent()) return false;

        File DeletePdfDir = new File(DATABASE_DIRECTORY + "/" + FileNamePDF + "/" + PdfFileName);
        if (!DeletePdfDir.exists()) {
            Log.d("Pdf", "File does not exist");
            return false;
        }
        try {
            DeletePdfDir.delete();
            return true;
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            // TODO: handle exception
            return false;
        }
    }
}
