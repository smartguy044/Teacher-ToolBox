package teacherToolBox.components;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Student
{
    private SimpleIntegerProperty studentID = new SimpleIntegerProperty();
    private SimpleStringProperty firstName = new SimpleStringProperty("");
    private SimpleStringProperty lastName = new SimpleStringProperty("");
    private SimpleStringProperty gender = new SimpleStringProperty("");
    private SimpleStringProperty fullName = new SimpleStringProperty("");

    private SimpleBooleanProperty mondayChecked = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty tuesdayChecked = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty wednesdayChecked = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty thursdayChecked = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty fridayChecked = new SimpleBooleanProperty(false);

    private SimpleStringProperty grades = new SimpleStringProperty("");
    private SimpleStringProperty activity = new SimpleStringProperty("");
    private SimpleDoubleProperty avgGrade = new SimpleDoubleProperty();

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

    public Student(int studentID, String name, String activity, double avgGrade)
    {
        setStudentID(studentID);
        setFullName(name);
        setActivity(activity);
        setAvgGrade(avgGrade);
    }

    public Student(String name)
    {
        setFullName(name);
        setGrades(name);
    }

    public Student(int studentID, String name)
    {
        setFullName(name);
        setGrades(name);
    }

    public String getGrades() {
        return grades.get();
    }

    public void setGrades(String grade)
    {
        grades.set(grade);
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

    public String getActivity()
    {
        return activity.get();
    }

    private void setActivity(String act)
    {
        activity.set(act);
    }

    public double getAvgGrade()
    {
        return avgGrade.get();
    }

    private void setAvgGrade(double avg)
    {
        avgGrade.set(avg);
    }

    public String getFullName()
    {
        return fullName.get();
    }

    private void setFullName(String name)
    {
        fullName.set(name);
    }

    public SimpleBooleanProperty mondayCheckedProperty()
    {
        return mondayChecked;
    }

    public Boolean getMonChecked()
    {
        return mondayChecked.get();
    }

    public void setMondayChecked(final Boolean checked)
    {
        mondayChecked.set(checked);
    }

    public SimpleBooleanProperty tuesdayCheckedProperty()
    {
        return tuesdayChecked;
    }

    public Boolean getTueChecked()
    {
        return tuesdayChecked.get();
    }

    public void setTuesdayChecked(final Boolean checked)
    {
        tuesdayChecked.set(checked);
    }

    public SimpleBooleanProperty wednesdayCheckedProperty()
    {
        return wednesdayChecked;
    }

    public Boolean getWedChecked()
    {
        return wednesdayChecked.get();
    }

    public void setWednesdayChecked(final Boolean checked)
    {
        wednesdayChecked.set(checked);
    }

    public SimpleBooleanProperty thursdayCheckedProperty()
    {
        return thursdayChecked;
    }

    public Boolean getThurChecked()
    {
        return thursdayChecked.get();
    }

    public void setThursdayChecked(final Boolean checked)
    {
        thursdayChecked.set(checked);
    }

    public SimpleBooleanProperty fridayCheckedProperty()
    {
        return fridayChecked;
    }

    public Boolean getFriChecked()
    {
        return fridayChecked.get();
    }

    public void setFridayChecked(final Boolean checked)
    {
        fridayChecked.set(checked);
    }

    @Override
    public String toString()
    {
        return studentID + " " + firstName + " " + lastName + " " + gender;
    }
}
