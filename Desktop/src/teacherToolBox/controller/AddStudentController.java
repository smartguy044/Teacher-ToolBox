package teacherToolBox.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import teacherToolBox.components.Student;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

@FXMLController("../fxml/AddStudent.fxml")

public class AddStudentController
{
    @FXML private JFXComboBox<String> classCB;
    @FXML private TableView<Student> rosterView;

    @FXML
    @ActionTrigger("selectionAction")
    private JFXButton selectionButton;

    private ObservableList<Student> data;
    private Connection connection;
    private Statement statement;

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
    }

    @ActionMethod("selectionAction")
    public void selectionButton_onAction() throws Exception
    {
        data = rosterView.getItems();
        data.clear();

        ArrayList<Integer> ids = new ArrayList<>();
        String selection = classCB.getSelectionModel().getSelectedItem();

        try
        {
            statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select studentID from rosters where courseID in (select courseID from courses where courseName = '" + selection + "')");

            while (resultSet.next())
            {
                ids.add(resultSet.getInt(1));
            }

            for (Integer id : ids)
            {
                resultSet = statement.executeQuery("select studentID, studentFN, studentLN, studentGen from students where studentID = " + id);

                while (resultSet.next())
                {
                    data.add(new Student(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4)));
                }
            }
        }
        catch (SQLException sqlException)
        {
            String msg = sqlException.getMessage();
            System.err.printf("problem with db connection: %s\n", msg);
        }
    }
}
