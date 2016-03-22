package teacherToolBox.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
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
    @FXMLViewFlowContext
    private ViewFlowContext flowContext;

    @FXML private JFXComboBox<String> classCB;
    @FXML private TableView<Student> rosterView;
    @FXML private JFXTextField studentIdTF;
    @FXML private JFXTextField firstNameTF;
    @FXML private JFXTextField lastNameTF;
    @FXML private JFXTextField genderTF;
    @FXML private JFXDialog studentExistsDialog;
    @FXML private JFXButton acceptButtonSE;
    @FXML private JFXDialog studentIDExistsDialog;
    @FXML private JFXButton acceptButtonID;

    @FXML
    @ActionTrigger("selectionAction")
    private JFXButton selectionButton;

    @FXML
    @ActionTrigger("submitAction")
    private JFXButton submitButton;

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

        acceptButtonSE.setOnMouseClicked((e)->{
            studentExistsDialog.close();
        });

        acceptButtonID.setOnMouseClicked((e)->{
            studentIDExistsDialog.close();
        });

        classCB.setOnAction((event) ->
        {
            if(!classCB.getValue().equals("Select Roster"))
            {
                selectionButton.setDisable(false);
            }
        });

        studentIdTF.focusedProperty().addListener((o, oldVal, newVal) ->
        {
            if (!newVal)
            {
                studentIdTF.validate();
            }

            studentIdTF.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>()
            {
                @Override
                public void handle(KeyEvent arg0)
                {
                    if (!newVal)
                    {
                        studentIdTF.validate();
                    }

                    updateButton();
                }
            });
        });

        firstNameTF.focusedProperty().addListener((o, oldVal, newVal) ->
        {
            if (!newVal)
            {
                firstNameTF.validate();
            }

            firstNameTF.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>()
            {
                @Override
                public void handle(KeyEvent arg0)
                {
                    if (!newVal)
                    {
                        firstNameTF.validate();
                    }

                    updateButton();
                }
            });
        });

        lastNameTF.focusedProperty().addListener((o, oldVal, newVal) ->
        {
            if (!newVal)
            {
                lastNameTF.validate();
            }

            lastNameTF.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>()
            {
                @Override
                public void handle(KeyEvent arg0)
                {
                    if (!newVal)
                    {
                        lastNameTF.validate();
                    }

                    updateButton();
                }
            });
        });

        genderTF.focusedProperty().addListener((o, oldVal, newVal) ->
        {
            if (!newVal)
            {
                genderTF.validate();
            }

            genderTF.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>()
            {
                @Override
                public void handle(KeyEvent arg0)
                {
                    if (!newVal)
                    {
                        genderTF.validate();
                    }

                    updateButton();
                }
            });
        });
    }

    @ActionMethod("selectionAction")
    public void selectionButton_onAction() throws Exception
    {
        studentIdTF.setDisable(false);
        firstNameTF.setDisable(false);
        lastNameTF.setDisable(false);
        genderTF.setDisable(false);

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

    @ActionMethod("submitAction")
    public void submitButton_onAction() throws Exception
    {
        boolean exists = true;

        for (Student aData : data)
        {
            if (aData.getStudentID() == Integer.valueOf(studentIdTF.getText()))
            {
                if(aData.getFirstName().equals(firstNameTF.getText()) && aData.getLastName().equals(lastNameTF.getText()))
                {
                    studentExistsDialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
                    studentExistsDialog.show((Pane) flowContext.getRegisteredObject("ContentPane"));
                }
                else
                {
                    studentIDExistsDialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
                    studentIDExistsDialog.show((Pane) flowContext.getRegisteredObject("ContentPane"));
                }

                break;
            }
            else
            {
                exists = false;
            }
        }

        if(!exists)
        {
            data.add(new Student(Integer.valueOf(studentIdTF.getText()), firstNameTF.getText(), lastNameTF.getText(), genderTF.getText()));

            studentIdTF.setText("");
            firstNameTF.setText("");
            lastNameTF.setText("");
            genderTF.setText("");
            updateButton();
        }
    }

    private void updateButton()
    {
        if (!studentIdTF.getText().equals("") && !firstNameTF.getText().equals("")
                && !lastNameTF.getText().equals("") && !genderTF.getText().equals(""))

        {
            submitButton.setDisable(false);
        }
    }
}
