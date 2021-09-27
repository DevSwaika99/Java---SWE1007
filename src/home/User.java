package home;

public class User {

    private String rejoindate;

    private String email;

    private String leavedate;

    private String gender;

    private String lastname;

    private String firstname;

    public String getId ()
    {
        return rejoindate;
    }

    public void setId (String rejoindate)
    {
        this.rejoindate = rejoindate;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getDob ()
    {
        return leavedate;
    }

    public void setDob (String dob)
    {
        this.leavedate = dob;
    }

    public String getGender ()
    {
        return gender;
    }

    public void setGender (String gender)
    {
        this.gender = gender;
    }

    public String getLastname ()
    {
        return lastname;
    }

    public void setLastname (String lastname)
    {
        this.lastname = lastname;
    }

    public String getFirstname ()
    {
        return firstname;
    }

    public void setFirstname (String firstname)
    {
        this.firstname = firstname;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [rejoindate = "+rejoindate+", leavedate = "+leavedate+", email = "+email+", gender = "+gender+", lastname = "+lastname+", firstname = "+firstname+"]";
    }
}