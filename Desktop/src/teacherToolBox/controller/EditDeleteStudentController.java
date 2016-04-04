package teacherToolBox.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import teacherToolBox.components.Student;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

@FXMLController("../fxml/EditDeleteStudent.fxml")
public class EditDeleteStudentController
{
    @FXMLViewFlowContext
    private ViewFlowContext flowContext;

    @FXML private JFXComboBox<String> classCB;
    @FXML private TableView<Student> rosterView;

    @FXML
    @ActionTrigger("selectionAction")
    private JFXButton selectionButton;

    private ObservableList<Student> data;
    private Statement statement;

    @PostConstruct
    public void init()
    {
        Properties properties = new Properties();
        ArrayList<String> classes = new ArrayList<>();

        try
        {
            data = rosterView.getItems();

            properties.load(new FileInputStream(".//src//database.properties"));
            String url = properties.getProperty("jdbc.url");
            Class.forName(properties.getProperty("jdbc.driver"));
            Connection connection = DriverManager.getConnection(url, properties.getProperty("jdbc.username"),
                    properties.getProperty("jdbc.password"));

            statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select courseName from courses where userID = " + 1001);

            while (resultSet.next())
            {
                classes.add(resultSet.getString(1));
            }

            classCB.getItems().addAll(classes);

            rosterView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
                if(rosterView.getSelectionModel().getSelectedItem() != null)
                {
                    int value = rosterView.getSelectionModel().getSelectedIndex();

                    int studentID = data.get(value).getStudentID();
                    String fname = data.get(value).getFirstName();
                    String lname = data.get(value).getLastName();
                    String gender = data.get(value).getGender();

                    try
                    {
                        Stage primaryStage = new Stage();

                        FXMLLoader loader = new FXMLLoader(EditDeleteStudentController.class.getResource("../fxml/StudentEditDialog.fxml"));
                        AnchorPane page = loader.load();
                        Stage dialogStage = new Stage();
                        dialogStage.setTitle("Edit Person");
                        dialogStage.initModality(Modality.WINDOW_MODAL);
                        dialogStage.initOwner(primaryStage);
                        flowContext = new ViewFlowContext();
                        flowContext.register("Stage", dialogStage);
                        Scene scene = new Scene(page);
                        dialogStage.setScene(scene);

                        StudentEditDialogController controller = loader.getController();
                        controller.setDialogStage(dialogStage);
                        controller.setPerson(data.get(value));

                        dialogStage.showAndWait();

                        if(controller.isOkClicked())
                        {
                            if(studentID != data.get(value).getStudentID() || !fname.equals(data.get(value).getFirstName()) ||
                                    !lname.equals(data.get(value).getLastName()) || !gender.equals(data.get(value).getGender()))
                            {
                                statement.executeUpdate("UPDATE students SET studentID = " + data.get(value).getStudentID() + ", studentFN = '" + data.get(value).getFirstName() +

                                        "', studentLN = '" + data.get(value).getLastName() + "', studentGen = '" + data.get(value).getGender() + "' WHERE studentFN = '" + fname + "'");
                                rosterView.refresh();
                            }
                        }

                        if(controller.isDeleteClicked())
                        {
                            statement.executeUpdate("DELETE FROM rosters WHERE studentID = " + studentID);
                            statement.executeUpdate("DELETE FROM students WHERE studentID = " + studentID);

                            getStudents();

                            rosterView.refresh();
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
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
        getStudents();
    }

    private void getStudents() throws Exception
    {
        data.clear();

        ArrayList<Integer> ids = new ArrayList<>();
        String selection = classCB.getSelectionModel().getSelectedItem();


        ResultSet resultSet = statement.executeQuery("select studentID from rosters where courseID in (select " +
                "courseID from courses where courseName = '" + selection + "')");

        while (resultSet.next())
        {
            ids.add(resultSet.getInt(1));
        }

        for (Integer id : ids)
        {
            resultSet = statement.executeQuery("select studentID, studentFN, studentLN, studentGen from students " +
                    "where studentID = " + id);

            while (resultSet.next())
            {
                data.add(new Student(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet
                        .getString(4)));
            }
        }
    }
}
