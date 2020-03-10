package com.company;

public class Date {

    private int day;
    private int month;
    private int year;

    public Date() {
        this.day = Terminal.TODAYS_DAY;
        this.month = Terminal.TODAYS_MONTH;
        this.year = Terminal.TODAYS_YEAR;
    }

    public Date(int day, int month, int year) {
        this.day = 1;
        this.month = 1;
        this.year = 2020;
        this.setDay(day);
        this.setMonth(month);
        this.setYear(year);
    }

    public int getDay() {return day; }
    public int getMonth() {return month; }
    public int getYear() {return year; }
// Returns the days of this date since 01/01/1970.
    private int daysSince1970() {
        int days = 0;
// previous years
        for (int i = 1970; i < this.year; i++){
            days += this.daysInYear(i);
        }
// previous months in this year
        for (int i = 1; i < this.month; i++) {
            days += this.daysInMonth(i, this.year);
        }
// previous days in this month
        days += this.day - 1;
        return days;
    }
// Returns the number of the days in the specified year.
    private int daysInYear(int year) {
        int days = 0;

        for (int month = 1; month <= 12; month++){
            days += this.daysInMonth(month, year);
        }
        return days;
    }
// Returns the days between the specified date and the date represented by this instance.
    public int getAgeInDaysAt(Date today) {
        return today.daysSince1970() - this.daysSince1970();
    }
// Returns the full years between the specified date and the date represented by this instance.
    public int getAgeInYearsAt(Date today) {
        int age = today.year - this.year;

        if (today.month > this.month) {
            // birthday already happened this year
            return age;
        } else if(today.month < this.month){
            // birthday will happen later this year
            return age - 1;
        } else {
            // birthday is in this month
            if (today.day >= this.day) {
                // bday was earlier this month or today
                return age;
            } else {
                // bday later this month
                return age - 1;
            }
        }
    }

private int daysInMonth(int month, int year) {
        if (month != 2 && month != 11 && month != 12) {

        } else if (month == 12) {
            return 31;
        } else if (month == 11) {
            return 30;
        } else if (month == 2) {
            return daysInFebruary(year);
        } else {

        } return -1;
    }

    private int daysInFebruary(int year) {
        if (year % 4 != 0) {
            return 28;
        }
        if ((year % 100 == 0) && (year % 400 != 0)) {
            return 28;
        }
        return 29;
    }

    public String toString() { return this.day + "/" + this.month + "/" + this.year; }

    public void setDay(int day) {
        if (day < 1) {
            this.day = 1;
        } else if (day > daysInMonth(this.month, this.year)) {
            this.day = daysInMonth(this.month, this.year);
        } else {
            this.day = day;
        }
    }
    public void setMonth(int month) {
        if (month < 1) {
            this.month = 1;
        } else if (month > 12) {
            this.month = 12;
        } else {
            this.month = month;
        }
        this.setDay(this.day);
    }

    public void setYear(int year) {
        if (year < 1970) {
            this.year = 1970;
        } else if (year > 2100){
            this.year = 2100;
        } else {
            this.year = year;
        }
        this.setDay(this.day);
    }

    public boolean equals(Date date) {
        if (this == date) {
            return true;
        }
        if (date == null){
            return false;
        }

        return this.day == date.day && this.month == date.month && this.year == date.year;
    }

}

