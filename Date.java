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


}
