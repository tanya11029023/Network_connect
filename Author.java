public class Author {
private String firstName;
private String lastName;
private Date birthday;
private String residence; 
private String email;

public Author(String firstname, String lastName, Date birthday, String residence, String email){
this.setFirstName(firstName);
this.setLastName(lastName);
this.setResidence(residence);
this.setEmail(email);
this.birthday = birthday;
}

public String getFirstName() {return firstName; }
public String getLastName() {return lastName; }
public Date getBirthday() { return birthday; }
public String getResidence() { return residence; }
public String getEmail() { return email; }
public String toString() {return this.firstName + "" + this.lastName; }

public String getContactInformation() {
return this.firstName + "" + this.lastName + Terminal.NEWLINE + "<" + 
this.email + ">" + Termianl.NEWLINE + this.residence; 
}

public int getAgeAt(Date today) {return this.birthday.getAgeInYearsAt(today); }

public void setFirstName(String firstName) {
  if (firstName == null) {
  this.firstName = "";
  } else {
    this.firstName = firstName; 
}}

public void setLastName(String lastName) {
    if (lastName == null) {
      this.lastName = "";
    } else {
      this.lastName = lastName;
    }
  }

public void setResidence(String residence) {
    if (residence == null) {
      this.residence = "";
    } else {
      this.residence = residence;
    }
  }

public void setEmail(String email) {
    if (email == null || !email.contains("@")) {
      this.email = "invalid@blabla.com";
    } else {
      this.email = email;
    }
  }

// compare if authors are the same
  public boolean equals (Author author){
// boolean for result
  boolean result = true;
// if author is not empty
  if (author != null){
  if (this.firstName.equals(author.firstName) && this.lastName.equals(author.lastName) &&
  this.birthday.equals(author.birthday) && this.residence.equals(author.residence) && this.email.equals(author.email)){
      result = true;
    } else {
      result = false;
    }} else {
      System.out.println("author wasn't initialized");
      result = false;
    }
    return result;
  }
  
  public static void main (String [] args ){
    Date birthday = new Date (1, 1, 1990);
    Date date2 = new Date(1, 1, 1990);
    Author author1 = new Author("Tsoy", "Tatiana", birthday, "Munich", "tanya11@com");
    Author author2 = new Author("Tsoy", "Tatiana", date2, "Munich", "tanya11@de");
    System.out.println(author2.equals(author1));
  }
}
  
