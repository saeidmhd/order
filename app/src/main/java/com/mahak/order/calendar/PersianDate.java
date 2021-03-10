package com.mahak.order.calendar;

/**
 * @author Amir
 * @author ebraminio (implementing isLeapYear)
 */
public class PersianDate extends AbstractDate {

    private static final String[] persianMonthName = {"", "فروردین",
            "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور", "مهر", "آبان",
            "آذر", "دی", "بهمن", "اسفند"};

    private static final String[] persianMonthNameEn = {"", "Farvardin",
            "Ordibehesht", "Khordad", "Tir", "Mordad", "Shahrivar", "Mehr", "Aban",
            "Azar", "Dey", "Bahman", "Esfand"};

    public static final String[] WeekDayName = {"", "یکشنبه", "دوشنبه", "سه شنبه", "چهارشنبه", "پنج شنبه", "جمعه", "شنبه"};

    /**
     * Months Names in Dari, needed for special Dari Version. Provided by:
     * Mohammad Hamid Majidee
     */
    private static final String[] dariMonthName = {"", "حمل", "ثور", "جوزا",
            "سرطان", "اسد", "سنبله", "میزان", "عقرب", "قوس", "جدی", "دلو",
            "حوت"};

    public static final int[] daysInMonth = {31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29};

    private boolean isDari = false;

    public String[] getMonthsList() {
        return isDari ? dariMonthName : persianMonthName;
    }

    public void setDari(boolean isDari) {
        this.isDari = isDari;
    }

    private int year;
    private int month;
    private int day;

    public PersianDate(int year, int month, int day) {
        setYear(year);
        // Initialize day, so that we get no exceptions when setting month
        this.day = 1;
        setMonth(month);
        setDayOfMonth(day);
    }

    public PersianDate clone() {
        return new PersianDate(getYear(), getMonth(), getDayOfMonth());
    }

    public int getDayOfMonth() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public String getMonthName() {
        return getMonthsList()[month];
    }

    public String getMonthNameEn() {
        return persianMonthNameEn[month];
    }

    public int getWeekOfYear() {
        throw new RuntimeException("not implemented yet!");
    }

    public int getYear() {
        return year;
    }

    public void rollDay(int amount, boolean up) {
        throw new RuntimeException("not implemented yet!");
    }

    public void rollMonth(int amount, boolean up) {
        throw new RuntimeException("not implemented yet!");
    }

    public void rollYear(int amount, boolean up) {
        throw new RuntimeException("not implemented yet!");
    }

    public void setDayOfMonth(int day) {
        if (day < 1)
            throw new DayOutOfRangeException("day " + day + " is out of range!");

        if (month <= 6 && day > 31)
            throw new DayOutOfRangeException("day " + day + " is out of range!");

        if (month > 6 && month <= 12 && day > 30)
            throw new DayOutOfRangeException("day " + day + " is out of range!");

        if (isLeapYear() && month == 12 && day > 30)
            throw new DayOutOfRangeException("day " + day + " is out of range!");

        if ((!isLeapYear()) && month == 12 && day > 29)
            throw new DayOutOfRangeException("day " + day + " is out of range!");

        this.day = day;
    }

    public void setMonth(int month) {
        if (month < 1 || month > 12)
            throw new MonthOutOfRangeException("month " + month
                    + " is out of range!");

        // Set the day again, so that exceptions are thrown if the
        // day is out of range
        setDayOfMonth(day);

        this.month = month;
    }

    public void setYear(int year) {
        if (year == 0)
            throw new YearOutOfRangeException("Year 0 is invalid!");

        this.year = year;
    }

    public String getEvent() {
        throw new RuntimeException("not implemented yet!");
    }

    public int getDayOfWeek() {
        throw new RuntimeException("not implemented yet!");
    }

    public int getDayOfYear() {
        throw new RuntimeException("not implemented yet!");
    }

    public int getWeekOfMonth() {
        throw new RuntimeException("not implemented yet!");
    }

    public boolean isLeapYear() {
        int y;
        if (year > 0)
            y = year - 474;
        else
            y = 473;
        return (((((y % 2820) + 474) + 38) * 682) % 2816) < 682;
    }

    public boolean equals(PersianDate persianDate) {
        return this.getDayOfMonth() == persianDate.getDayOfMonth()
                && this.getMonth() == persianDate.getMonth()
                && this.getYear() == persianDate.getYear();
    }

    public void addDay() {
        if (day == daysInMonth[month - 1]) {
            if (month == 12) {
                if (isLeapYear()) {
                    day++;
                } else {
                    day = 1;
                    month = 1;
                    year++;
                }
            } else {
                day = 1;
                month++;
            }
        } else {
            if (isLeapYear() && day == 30 && month == 12) {
                day = 1;
                month = 1;
                year++;
            } else {
                day++;
            }
        }
    }

    public void minusDay() {
        if (day == 1) {
            if (month == 1) {
                PersianDate pDate = new PersianDate(year - 1, 1, 1);
                if (pDate.isLeapYear()) {
                    day = 30;
                    month = 12;
                    year--;
                } else {
                    day = 29;
                    month = 12;
                    year--;
                }
            } else {
                day = daysInMonth[month - 2];
                month--;
            }
        } else {
            day--;
        }
    }

    public void addWeek() {
        for (int i = 0; i < 7; i++) {
            addDay();
        }
    }

    public void minusWeek() {
        for (int i = 0; i < 7; i++) {
            minusDay();
        }
    }

    public void addMonth() {
        if (day == daysInMonth[month - 1]) {
            if (month == 6) {
                day = 30;
            } else if (month == 11) {
                if (!isLeapYear()) {
                    day = 29;
                }
            } else if (month == 12) {
                day = 31;
            }
        }
        if (month == 12) {
            if (day == 30) {
                day = 31;
            }
            month = 1;
            year++;
        } else {
            month++;
        }
    }

    public void minusMonth() {
        if (day == daysInMonth[month - 1]) {
            if (month == 7) {
                day = 31;
            } else if (month == 12) {
                if (!isLeapYear()) {
                    day = 30;
                }
            } else if (month == 1) {
                PersianDate pDate = new PersianDate(year - 1, 1, 1);
                if (pDate.isLeapYear()) {
                    day = 30;
                } else {
                    day = 29;
                }
            }
        }
        if (month == 1) {
            month = 12;
            year--;
        } else {
            month--;
        }
    }

    public void addYear() {
        if (month == 12) {
            if (day == 30) {
                day = 29;
            }
        }
        year++;
    }

    public void minusYear() {
        if (month == 12) {
            if (day == 30) {
                day = 29;
            }
        }
        year--;
    }

}
