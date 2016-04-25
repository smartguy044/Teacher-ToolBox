package teacherToolBox.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import teacherToolBox.components.Student;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

/*
 * The AttendanceController class permits the user to mark the students attendance.
 * Specifically, the user needs to manually update the attendance of every student inside the roster.
 * The user must also select the roster which he/she needs to manage the attendance.
 * <p/> Bugs: None
 *
 * @author  Michael Stevens, Josh Torrans, Matthew Fondevilla, Joanna Ho, Tom Warren, and Greg Grimsley
 */

@FXMLController("../fxml/Attendance.fxml")
public class AttendanceController
{
    @FXML
    private JFXComboBox<String> classCB;

    @FXML
    private TableView<Student> rosterView;

    @FXML
    private TableColumn monday, tuesday, wednesday, thursday, friday;

    @FXML
    @ActionTrigger("selectionAction")
    private JFXButton selectionButton;

    @FXML
    @ActionTrigger("submitAction")
    private JFXButton submitButton;

    @FXML private SimpleBooleanProperty mondayChecked = new SimpleBooleanProperty(false);
    @FXML private SimpleBooleanProperty tuesdayChecked = new SimpleBooleanProperty(false);
    @FXML private SimpleBooleanProperty wednesdayChecked = new SimpleBooleanProperty(false);
    @FXML private SimpleBooleanProperty thursdayChecked = new SimpleBooleanProperty(false);
    @FXML private SimpleBooleanProperty fridayChecked = new SimpleBooleanProperty(false);

    private ObservableList<Student> data;

    private Connection connection;
    private Statement statement;

    private String[] days;
    private String selection;
    private ArrayList<Integer> ids;

    @PostConstruct
    public void init()
    {
        Properties properties = new Properties();

        ArrayList<String> classes = new ArrayList<>();

        try
        {
            properties.load(new FileInputStream(".//src//database.properties"));
            String url = properties.getProperty("jdbc.url");
            Class.forName(properties.getProperty("jdbc.driver"));
            connection = DriverManager.getConnection(url, properties.getProperty("jdbc.username"), properties.getProperty("jdbc.password"));

            statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select courseName from courses where userID = " + 1001);

            while (resultSet.next())
            {
                classes.add(resultSet.getString(1));
            }

            classCB.getItems().addAll(classes);
        }
        catch (IOException ioException)
        {
            String msg = ioException.getMessage();
            System.err.printf("problem with properties file: %s\n", msg);
        }
        catch (SQLException sqlException)
        {
            String msg = sqlException.getMessage();
            System.err.printf("problem with db connection: %s\n", msg);
        }
        catch (ClassNotFoundException e)
        {
            String msg = e.getMessage();
            System.err.printf("problem with driver: %s\n", msg);
        }

        Calendar now = Calendar.getInstance();

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

        int delta = -now.get(GregorianCalendar.DAY_OF_WEEK) + 2; //add 2 if your week start on monday
        now.add(Calendar.DAY_OF_MONTH, delta);

        days = new String[5];

        for (int i = 0; i < 5; i++)
        {
            days[i] = format.format(now.getTime());
            now.add(Calendar.DAY_OF_MONTH, 1);
        }

        for (int i = 0; i < days.length; i++)
        {
            if (i == 0)
            {
                monday.setText(days[i]);
            }
            else if (i == 1)
            {
                tuesday.setText(days[i]);
            }
            else if (i == 2)
            {
                wednesday.setText(days[i]);
            }
            else if (i == 3)
            {
                thursday.setText(days[i]);
            }
            else if (i == 4)
            {
                friday.setText(days[i]);
            }
        }
    }

    @ActionMethod("selectionAction")
    public void selectionButton_onAction() throws Exception
    {
        data = rosterView.getItems();
        data.clear();

        ids = new ArrayList<>();
        ArrayList<String> rset = new ArrayList<>();

        selection = classCB.getSelectionModel().getSelectedItem();

        try
        {
            ResultSet resultSet = statement.executeQuery("select studentID from rosters where courseID in (select courseID from courses where courseName = '" + selection + "')");

            while (resultSet.next())
            {
                ids.add(resultSet.getInt(1));
            }

            for (Integer id : ids)
            {
                resultSet = statement.executeQuery("select studentFN, studentLN from students where studentID = " + id);

                while (resultSet.next())
                {
                    String name = resultSet.getString(1) + " " + resultSet.getString(2);
                    data.add(new Student(name));
                }
            }

            String[][] value = new String[ids.size()][5];

            for (String day : days)
            {
                resultSet = statement.executeQuery("select `" + day + "` from " + selection + "Attendance");

                while (resultSet.next())
                {
                    rset.add(resultSet.getString(1));
                }
            }

            int count = 0;

            for(int i = 0; i < days.length; i++)
            {
                for(int j = 0; j < value.length; j++)
                {
                    value[j][i] = rset.get(count);
                    count++;
                }
            }

            for(int i = 0; i < days.length; i++)
            {
                count = 0;

                for (String[] aValue : value)
                {
                    switch(i)
                    {
                        case 0:
                            if(aValue[i].equals("Y"))
                            {
                                data.get(count).setMondayChecked(true);
                            }
                            break;
                        case 1:
                            if(aValue[i].equals("Y"))
                            {
                                data.get(count).setTuesdayChecked(true);
                            }
                            break;
                        case 2:
                            if(aValue[i].equals("Y"))
                            {
                                data.get(count).setWednesdayChecked(true);
                            }
                            break;
                        case 3:
                            if(aValue[i].equals("Y"))
                            {
                                data.get(count).setThursdayChecked(true);
                            }
                            break;
                        case 4:
                            if(aValue[i].equals("Y"))
                            {
                                data.get(count).setFridayChecked(true);
                            }
                            break;
                        default:
                            break;
                    }
                    count++;
                }
            }
        }
        catch (SQLException sqlException)
        {
            String msg = sqlException.getMessage();
            System.err.printf("problem with db connection: %s\n", msg);
        }
    }

    @ActionMethod("submitAction")
    public void submitButton_onAction() throws Exception
    {
        data = rosterView.getItems();

        mondayChecked.addListener((ov, old_val, new_val) ->
        {
            data.get(0).setMondayChecked(true);
        });

        tuesdayChecked.addListener((ov, old_val, new_val) ->
        {
            data.get(0).setTuesdayChecked(true);
        });

        wednesdayChecked.addListener((ov, old_val, new_val) ->
        {
            data.get(0).setWednesdayChecked(true);
        });

        thursdayChecked.addListener((ov, old_val, new_val) ->
        {
            data.get(0).setThursdayChecked(true);
        });

        fridayChecked.addListener((ov, old_val, new_val) ->
        {
            data.get(0).setFridayChecked(true);
        });

        for(int i = 0; i < data.size(); i++)
        {
            char[] check = {'N', 'N', 'N', 'N', 'N'};

            if(data.get(i).getMonChecked())
            {
                check[0] = 'Y';
            }
            if (data.get(i).getTueChecked())
            {
                check[1] = 'Y';
            }
            if (data.get(i).getWedChecked())
            {
                check[2] = 'Y';
            }
            if (data.get(i).getThurChecked())
            {
                check[3] = 'Y';
            }
            if (data.get(i).getFriChecked())
            {
                check[4] = 'Y';
            }

            try
            {
                statement = connection.createStatement();

                for(int j = 0; j < days.length; j++)
                {
                    statement.executeUpdate("UPDATE " + selection + "Attendance set `" + days[j] + "` = '" + check[j] + "' where studentID = " + ids.get(i));
                }
            }
            catch (SQLException sqlException)
            {
                String msg = sqlException.getMessage();
                System.err.printf("problem with db connection: %s\n", msg);
            }
        }
    }
}
