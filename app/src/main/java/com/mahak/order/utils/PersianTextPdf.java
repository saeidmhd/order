package com.mahak.order.utils;

import android.util.Log;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.mahak.order.BaseActivity;
import com.mahak.order.common.ServiceTools;

import java.util.ArrayList;


public class PersianTextPdf {
    ////////Create Persian Pdf
    public static void addText(String str, Paragraph paragraph, float txtSize, BaseColor txtColor) {

        String EnCodeText = MaryamEncodeString(str);
        String InvStrText = Inverse(str, true);

        try {
            for (int i = 0; i < InvStrText.length(); i++) {
                if (!isProbablyArabic(InvStrText.charAt(i)) && InvStrText.charAt(i) != ' ') {
                    Font font = new Font(Font.FontFamily.COURIER);
                    font.setSize(txtSize);
                    font.setColor(txtColor);

                    //
                    Chunk chunk = new Chunk(String.valueOf(InvStrText.charAt(i)));
                    chunk.setFont(font);

                    paragraph.add(chunk);
                } else {
                    BaseFont farsiFont = BaseFont.createFont("assets/fonts/F_yekan.ttf", "", BaseFont.EMBEDDED);
                    Font paraFont = new Font(farsiFont);
                    paraFont.setSize(txtSize);
                    paraFont.setColor(txtColor);

                    //
                    Chunk chunkEncode = new Chunk(String.valueOf(EnCodeText.charAt(i)));
                    chunkEncode.setFont(paraFont);

                    paragraph.add(chunkEncode);
                }
            }

        } catch (Exception e) {
            ServiceTools.logToFireBase(e);
            // TODO: handle exception
        }
    }

    public static void addText(String str, Phrase phrase, float txtSize, BaseColor txtColor) {

        String EnCodeText = MaryamEncodeString(str);
        String InvStrText = Inverse(str, true);
        try {
            for (int i = 0; i < InvStrText.length(); i++) {
                if (!isProbablyArabic(InvStrText.charAt(i)) && InvStrText.charAt(i) != ' ') {
                    Font font = new Font(Font.FontFamily.COURIER);
                    font.setSize(txtSize);
                    font.setColor(txtColor);

                    //
                    Chunk chunk = new Chunk(String.valueOf(InvStrText.charAt(i)));
                    chunk.setFont(font);

                    phrase.add(chunk);
                } else {
                    BaseFont farsiFont = BaseFont.createFont("assets/fonts/F_yekan.ttf", "", BaseFont.EMBEDDED);
                    Font paraFont = new Font(farsiFont);
                    paraFont.setSize(txtSize);
                    paraFont.setColor(txtColor);

                    //
                    Chunk chunkEncode = new Chunk(String.valueOf(EnCodeText.charAt(i)));
                    chunkEncode.setFont(paraFont);

                    phrase.add(chunkEncode);
                }
            }

        } catch (Exception e) {
            ServiceTools.logToFireBase(e);
            e.printStackTrace();
        }
    }
    public static void addText_de(String str, Phrase phrase, float txtSize, BaseColor txtColor) {

        String EnCodeText = MaryamEncodeString(str);
        try {
            for (int i = 0; i < str.length(); i++) {
                if (!isProbablyArabic(str.charAt(i)) && str.charAt(i) != ' ') {
                    Font font = new Font(Font.FontFamily.COURIER);
                    font.setSize(txtSize);
                    font.setColor(txtColor);

                    //
                    Chunk chunk = new Chunk(String.valueOf(str.charAt(i)));
                    chunk.setFont(font);

                    phrase.add(chunk);
                } else {
                    BaseFont farsiFont = BaseFont.createFont("assets/fonts/F_yekan.ttf", "", BaseFont.EMBEDDED);
                    Font paraFont = new Font(farsiFont);
                    paraFont.setSize(txtSize);
                    paraFont.setColor(txtColor);

                    //
                    Chunk chunkEncode = new Chunk(String.valueOf(EnCodeText.charAt(i)));
                    chunkEncode.setFont(paraFont);

                    phrase.add(chunkEncode);
                }
            }

        } catch (Exception e) {
            ServiceTools.logToFireBase(e);
            e.printStackTrace();
        }
    }

    public static String Inverse(String normal_str, boolean en_inverse) {

        String StrMainTemp = "";
        boolean find_first = false;
        ArrayList<PositionEn> indexes = new ArrayList<PositionEn>();

        for (int i = 0; i < normal_str.length(); i++) {
            char c = normal_str.charAt(i);
            StrMainTemp = c + StrMainTemp;

            if (!isProbablyArabic(c) && c != ' ' && !find_first) {
                // add first index
                PositionEn pp = new PositionEn();

                pp.setFirst(i);
                pp.setEnd(-1);

                indexes.add(pp);
                find_first = true;
            }
            if ((isProbablyArabic(c) || c == ' ') && find_first) {
                //add end index
                PositionEn pos = indexes.get(indexes.size() - 1);
                pos.setEnd(i);
                find_first = false;
            }

        }


        //handle if not end index added
        if (indexes.size() > 0)
            if (indexes.get(indexes.size() - 1).getEnd() == -1) {
                indexes.get(indexes.size() - 1).setEnd(normal_str.length());
            }

        //reverse
        if (en_inverse) {

            for (int j = 0; j < indexes.size(); j++) {
                String subStr = normal_str.substring(indexes.get(j).getFirst(), indexes.get(j).getEnd());
                Log.d("@PDF", Inverse(subStr, false));
                if (!subStr.equals("(") && !subStr.equals(")"))
                    StrMainTemp = StrMainTemp.replaceAll(Inverse(subStr, false), subStr);
            }
        }
        return StrMainTemp;
    }

    public static boolean isProbablyArabic(char ss) {
        String s = ss + "";
        for (int i = 0; i < s.length(); ) {
            int c = s.codePointAt(i);
            if (c >= 0x0600 && c <= 0x06FF)
                return true;
            i += Character.charCount(c);
        }
        return false;
    }

    public static boolean IsHaveMidStyle(char ch) {
        return ch != '??' && ch != '??' && ch != '??' && ch != '??' && ch != '??' && ch != '??' && ch != '??' && ch != '??' && ch != '??' && ch != '??' && ch != '??';
    }

    public static char getMaryamEncodeChar(char ch, String style) {
        switch (ch) {
            case '??':
            case '??':
            case '??':
                if (style.equals("Mid")) {
                    return 'I';
                } else if (style.equals("Start")) {
                    return 'H';
                } else if (style.equals("End")) {
                    return 'I';
                } else {
                    return 'H';
                }
            case '??':
                if (style.equals("Mid")) {
                    return 'B';
                } else if (style.equals("Start")) {
                    return 'A';
                } else if (style.equals("End")) {
                    return 'B';
                } else {
                    return 'A';
                }

            case '??':
                if (style.equals("Mid")) {
                    return 'L';
                } else if (style.equals("Start")) {
                    return 'M';
                } else if (style.equals("End")) {
                    return 'K';
                } else {
                    return 'J';
                }
            case '??':
                if (style.equals("Mid")) {
                    return 'P';
                } else if (style.equals("Start")) {
                    return 'Q';
                } else if (style.equals("End")) {
                    return 'O';
                } else {
                    return 'N';
                }
            case '??':
                if (style.equals("Mid")) {
                    return 'T';
                } else if (style.equals("Start")) {
                    return 'U';
                } else if (style.equals("End")) {
                    return 'S';
                } else {
                    return 'R';
                }
            case '??':
                if (style.equals("Mid")) {
                    return 'X';
                } else if (style.equals("Start")) {
                    return 'Y';
                } else if (style.equals("End")) {
                    return 'W';
                } else {
                    return 'V';
                }
            case '??':
                if (style.equals("Mid")) {
                    return '\\';
                } else if (style.equals("Start")) {
                    return ']';
                } else if (style.equals("End")) {
                    return '[';
                } else {
                    return 'Z';
                }
            case '??':
                if (style.equals("Mid")) {
                    return '`';
                } else if (style.equals("Start")) {
                    return 'a';
                } else if (style.equals("End")) {
                    return '_';
                } else {
                    return '^';
                }

            case '??':
                if (style.equals("Mid")) {
                    return 'd';
                } else if (style.equals("Start")) {
                    return 'e';
                } else if (style.equals("End")) {
                    return 'c';
                } else {
                    return 'b';
                }
            case '??':
                if (style.equals("Mid")) {
                    return 'h';
                } else if (style.equals("Start")) {
                    return 'i';
                } else if (style.equals("End")) {
                    return 'g';
                } else {
                    return 'f';
                }
            case '??':
                if (style.equals("Mid")) {
                    return 'k';
                } else if (style.equals("Start")) {
                    return 'j';
                } else if (style.equals("End")) {
                    return 'k';
                } else {
                    return 'j';
                }
            case '??':
                if (style.equals("Mid")) {
                    return 'm';
                } else if (style.equals("Start")) {
                    return 'l';
                } else if (style.equals("End")) {
                    return 'm';
                } else {
                    return 'l';
                }
            case '??':
                if (style.equals("Mid")) {
                    return 'o';
                } else if (style.equals("Start")) {
                    return 'n';
                } else if (style.equals("End")) {
                    return 'o';
                } else {
                    return 'n';
                }
            case '??':
                if (style.equals("Mid")) {
                    return 'p';
                } else if (style.equals("Start")) {
                    return 'q';
                } else if (style.equals("End")) {
                    return 'p';
                } else {
                    return 'q';
                }
            case '??':
                if (style.equals("Mid")) {
                    return 'v';
                } else if (style.equals("Start")) {
                    return 'w';
                } else if (style.equals("End")) {
                    return 'u';
                } else {
                    return 't';
                }
            case '??':
                if (style.equals("Mid")) {
                    return 'z';
                } else if (style.equals("Start")) {
                    return '{';
                } else if (style.equals("End")) {
                    return 'y';
                } else {
                    return 'x';
                }
            case '??':
                if (style.equals("Mid")) {
                    return '~';
                } else if (style.equals("Start")) {
                    return '???';
                } else if (style.equals("End")) {
                    return '}';
                } else {
                    return '|';
                }
            case '??':
                if (style.equals("Mid")) {
                    return '??';
                } else if (style.equals("Start")) {
                    return '??';
                } else if (style.equals("End")) {
                    return '???';
                } else {
                    return '??';
                }
            case '??':
                if (style.equals("Mid")) {
                    return '??';
                } else if (style.equals("Start")) {
                    return '??';
                } else if (style.equals("End")) {
                    return '??';
                } else {
                    return '??';
                }
            case '??':
                if (style.equals("Mid")) {
                    return '???';
                } else if (style.equals("Start")) {
                    return '???';
                } else if (style.equals("End")) {
                    return '???';
                } else {
                    return '???';
                }
            case '??':
                if (style.equals("Mid")) {
                    return '???';
                } else if (style.equals("Start")) {
                    return '??';
                } else if (style.equals("End")) {
                    return '???';
                } else {
                    return '???';
                }
            case '??':
                if (style.equals("Mid")) {
                    return '??';
                } else if (style.equals("Start")) {
                    return '??';
                } else if (style.equals("End")) {
                    return '??';
                } else {
                    return '??';
                }
            case '??':
                if (style.equals("Mid")) {
                    return '??';
                } else if (style.equals("Start")) {
                    return '??';
                } else if (style.equals("End")) {
                    return '??';
                } else {
                    return '??';
                }
            case '??':
                if (style.equals("Mid")) {
                    return '??';
                } else if (style.equals("Start")) {
                    return '??';
                } else if (style.equals("End")) {
                    return '??';
                } else {
                    return '??';
                }
            case '??':
            case '??':
                if (style.equals("Mid")) {
                    return '??';
                } else if (style.equals("Start")) {
                    return '??';
                } else if (style.equals("End")) {
                    return '??';
                } else {
                    return '??';
                }
            case '??':
                if (style.equals("Mid")) {
                    return '??';
                } else if (style.equals("Start")) {
                    return '??';
                } else if (style.equals("End")) {
                    return '??';
                } else {
                    return '??';
                }
            case '??':
                if (style.equals("Mid")) {
                    return '??';
                } else if (style.equals("Start")) {
                    return '??';
                } else if (style.equals("End")) {
                    return '??';
                } else {
                    return '??';
                }
            case '??':
                if (style.equals("Mid")) {
                    return '??';
                } else if (style.equals("Start")) {
                    return '??';
                } else if (style.equals("End")) {
                    return '??';
                } else {
                    return '??';
                }
            case '??':
                if (style.equals("Mid")) {
                    return '??';
                } else if (style.equals("Start")) {
                    return '??';
                } else if (style.equals("End")) {
                    return '??';
                } else {
                    return '??';
                }
            case '??':
            case '??':
                if (style.equals("Mid")) {
                    return '??';
                } else if (style.equals("Start")) {
                    return '??';
                } else if (style.equals("End")) {
                    return '??';
                } else {
                    return '??';
                }
            case '??':
                if (style.equals("Mid")) {
                    return '??';
                } else if (style.equals("Start")) {
                    return '??';
                } else if (style.equals("End")) {
                    return '??';
                } else {
                    return '??';
                }
            case '??':
            case '??':
                if (style.equals("Mid")) {
                    return '??';
                } else if (style.equals("Start")) {
                    return '??';
                } else if (style.equals("End")) {
                    return '??';
                } else {
                    return '??';
                }
        }
        return ch;
    }

    public static String MaryamEncodeString(String normal_str) {

        String temp = "";
        for (int q = 0; q < normal_str.length(); q++) {
            char c = normal_str.charAt(q);
            if (c == ' ' || !isProbablyArabic(c)) {
                temp = ' ' + temp;
                continue;
            }

            boolean next = true;
            boolean prev = true;

            if (q == 0) {
                prev = false;
            }
            if (q == normal_str.length() - 1) {
                next = false;
            }
            if (next) {
                char c1 = normal_str.charAt(q + 1);
                if (c1 == ' ') {
                    next = false;
                }
            }
            if (next) {
                if (!isProbablyArabic(normal_str.charAt(q + 1)))
                    next = false;
            }

            if (prev) {
                char c1 = normal_str.charAt(q - 1);
                if (c1 == ' ') {
                    prev = false;
                }
            }

            if (prev) {
                if (!IsHaveMidStyle(normal_str.charAt(q - 1))) {
                    prev = false;
                }
            }
            if (prev) {
                if (!isProbablyArabic(normal_str.charAt(q - 1)))
                    prev = false;
            }

            if (next && prev) {
                temp = getMaryamEncodeChar(normal_str.charAt(q), "Mid") + temp;
            } else if (next && prev == false) {
                temp = getMaryamEncodeChar(normal_str.charAt(q), "Start") + temp;
            } else if (next == false && prev) {
                temp = getMaryamEncodeChar(normal_str.charAt(q), "End") + temp;
            } else if (next == false && prev == false) {
                temp = getMaryamEncodeChar(normal_str.charAt(q), "null") + temp;
            }
        }
        return temp;
    }

    public static class PositionEn {
        int first, end;

        public void setFirst(int first) {
            this.first = first;
        }

        public int getFirst() {
            return first;
        }

        public void setEnd(int end) {
            this.end = end;
        }

        public int getEnd() {
            return end;
        }
    }
}
