package teacherToolBox.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import teacherToolBox.components.Student;

import javax.annotation.PostConstruct;
import javax.swing.text.TabableView;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

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

    @PostConstruct
    public void init()
    {
        Properties properties = new Properties();
        Connection connection = null;
        Statement statement = null;

        ArrayList<String> classes = new ArrayList<>();

        try {
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

        try {
            int logins = 0;

            properties.load(new FileInputStream(".//src//database.properties"));
            String url = properties.getProperty("jdbc.url");
            Class.forName(properties.getProperty("jdbc.driver"));
            connection = DriverManager.getConnection(url, properties.getProperty("jdbc.username"), properties.getProperty("jdbc.password"));

            statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select logins from users where userID = " + 1001);

            while (resultSet.next())
            {
                logins = resultSet.getInt(1);
            }

            Calendar now = Calendar.getInstance();

            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

            String[] days = new String[5];
            int delta = -now.get(GregorianCalendar.DAY_OF_WEEK) + 2; //add 2 if your week start on monday
            now.add(Calendar.DAY_OF_MONTH, delta);

            for (int i = 0; i < 5; i++)
            {
                days[i] = format.format(now.getTime());
                now.add(Calendar.DAY_OF_MONTH, 1);
            }

            for(int i = 0; i < days.length; i++)
            {
                if(i == 0)
                {
                    monday.setText(days[i]);
                }
                else if(i == 1)
                {
                    tuesday.setText(days[i]);
                }
                else if(i == 2)
                {
                    wednesday.setText(days[i]);
                }
                else if(i == 3)
                {
                    thursday.setText(days[i]);
                }
                else if(i == 4)
                {
                    friday.setText(days[i]);
                }
            }

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
    }

    @ActionMethod("selectionAction")
    public void selectionButton_onAction() throws Exception
    {
        ObservableList<Student> data = rosterView.getItems();
        data.clear();
        Properties properties = new Properties();
        Connection connection = null;
        Statement statement = null;

        ArrayList<Integer> ids = new ArrayList<>();

        String selection = classCB.getSelectionModel().getSelectedItem();

        try {
            properties.load(new FileInputStream(".//src//database.properties"));
            String url = properties.getProperty("jdbc.url");
            Class.forName(properties.getProperty("jdbc.driver"));
            connection = DriverManager.getConnection(url, properties.getProperty("jdbc.username"), properties.getProperty("jdbc.password"));

            statement = connection.createStatement();

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
                    String name = resultSet.getString(1);
                    data.add(new Student(name));
                }
            }
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
    }
}
