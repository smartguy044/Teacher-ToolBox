package teacherToolBox.components;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Student
{
    private SimpleIntegerProperty studentID = new SimpleIntegerProperty();
    private SimpleStringProperty firstName = new SimpleStringProperty("");
    private SimpleStringProperty lastName = new SimpleStringProperty("");
    private SimpleStringProperty gender = new SimpleStringProperty("");
    private SimpleStringProperty fullName = new SimpleStringProperty("");

    public Student()
    {
        this(0, "", "", "");
    }

    public Student(int studentID, String firstName, String lastName, String gender)
    {
        setStudentID(studentID);
        setFirstName(firstName);
        setLastName(lastName);
        setGender(gender);
        setFullName(firstName + " " + lastName);
    }

    public Student(String name)
    {
        setFullName(name);
    }

    public int getStudentID()
    {
        return studentID.get();
    }

    public void setStudentID(int stuID)
    {
        studentID.set(stuID);
    }

    public String getFirstName()
    {
        return firstName.get();
    }

    public void setFirstName(String fName)
    {
        firstName.set(fName);
    }

    public String getLastName()
    {
        return lastName.get();
    }

    public void setLastName(String fName)
    {
        lastName.set(fName);
    }

    public String getGender()
    {
        return gender.get();
    }

    public void setGender(String gen)
    {
        gender.set(gen);
    }

    public String getFullName()
    {
        return fullName.get();
    }

    public void setFullName(String name)
    {
        fullName.set(name);
    }

    @Override
    public String toString()
    {
        return studentID + " " + firstName + " " + lastName + " " + gender;
    }
}
