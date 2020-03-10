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
// calculate month how many days in month
    int calcMonth = daysInMonth(1, this.year);
    // current month - 1
    int counterM = this.month - 1;
    // calculate days of month
    for (int j = 1 ; j < counterM; j++){
      calcMonth += daysInMonth(this.month-1, this.year);
      this.month = this.month - 1;
    }
    // counter how many years since 1970
    int counter = this.year - 1970;
    // days in years
    int calcYear = 0;
    // start with 1970 and calculate till indicated year
    for (int i = 1970; i < 1970 + counter; i++){
      // add amount of days in year to calcYear
      calcYear += daysInYear(this.year-1);
      // go back from this year to 1970
      this.year = this.year-1;
    }
    // how many days (years result) + days of month till current month + current day (-1)
    return calcYear + calcMonth + this.day - 1;
  }

// Returns the number of the days in the specified year.
    private int daysInYear(int year) {
       int result = 0;
// if not leap year
    if (year % 4 != 0 ) {
      result= 365; }
    else if ((year % 400 != 0) && (year % 100 != 0)){
      result = 366;
    }
    else if ((year%400 != 0) && (year % 100 == 0)) {
    result = 365; }
      //if leap year
    else {
      result = 366;
  } return result; }
    
    private double average (Date today){
    double average = 0;
    int counter = today.year - this.year;
    for (int i = this.year; i < this.year + (today.year - this.year); i++) {
      average += daysInYear(this.year);
      this.year++;
    }
    average = average / counter;
return average;
  }
    
// Returns the days between the specified date and the date represented by this instance.
    public int getAgeInDaysAt(Date today) {
        return today.daysSince1970() - this.daysSince1970();
    }
// Returns the full years between the specified date and the date represented by this instance.
    public int getAgeInYearsAt(Date today) {
    int ageInDays = this.getAgeInDaysAt(today);
    // set today again to actual date --> pls use contructor if apply another date
    today = new Date();
    double ageInYears = (double) ageInDays /average(today);
    int res = (int) ageInYears;
    return res;
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

    public boolean equals (Date date2){
      boolean result = true;
      // check if date in parameters isn't empty
      if (date2 != null){
        // the same day, month, year result equal to true
      if (this.day == date2.day && this.month == date2.month && this.year == date2.year){
          result = true;
      } else result = false;}
      // if date is empty return error message
      else {
        System.out.println("Second date wasn't initialized");
        result = false;
      }
      return result;
  }
    public static void main (String [] args ){
        Date date1 = new Date(11, 2, 1972);
        Date date2 = new Date(11, 2, 1973);
        Date date3 = new Date (11, 4, 1980);
        Date date4 = new Date (5, 9, 1990);
        Date date5 = new Date (25, 11, 2015);
        Date today = new Date(26, 11, 2018);
        //System.out.println(date1.equals(date2));
        //System.out.println(daysInYear(2400));
        //System.out.println(date1.daysSince1970());
        /*System.out.println(daysInYear(1970));
        System.out.println(daysInYear(1971));
        System.out.println(daysInYear(1973));
        System.out.println(daysInYear(1974));
        System.out.println(daysInYear(1972));
        System.out.println(daysInYear(1976));
        System.out.println(daysInYear(1980)); */
        /*System.out.println(date2.daysSince1970());
        System.out.println(date3.daysSince1970());
        System.out.println(date4.daysSince1970());
        System.out.println(date5.daysSince1970()); */
        //System.out.println(date5.average(today));
        System.out.println(date5.getAgeInYearsAt(today));
        //System.out.println(date5.getAgeInDaysAt(today));
}

