package teacherToolBox.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import teacherToolBox.components.EditingCell;
import teacherToolBox.components.Student;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

@FXMLController("../fxml/Grades.fxml")
public class GradesController
{
    @FXMLViewFlowContext
    private ViewFlowContext flowContext;

    @FXML
    private JFXComboBox<String> classCB;

    @FXML
    private TableView<Student> rosterView;

    @FXML
    private TableColumn finalGrade;

    @FXML
    @ActionTrigger("selectionAction")
    private JFXButton selectBtn;

    @FXML
    @ActionTrigger("addAssignment")
    private Button addAssignment;

    private String selection;
    private Callback<TableColumn, TableCell> cellFactory;

    private Connection connection;
    private Statement statement;

    @PostConstruct
    public void init()
    {
        Properties properties = new Properties();
        connection = null;
        statement = null;

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

            cellFactory = p -> new EditingCell();
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
    public void selectBtn_onAction() throws Exception
    {
        ObservableList<Student> data = rosterView.getItems();
        data.clear();

        ArrayList<Integer> ids = new ArrayList<>();
        ArrayList<String> columnNames = new ArrayList<>();
        ArrayList<String> grades = new ArrayList<>();
        TableColumn column = null;

        selection = classCB.getSelectionModel().getSelectedItem();

        try {
            statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT COLUMN_NAME\n" +
                    "FROM INFORMATION_SCHEMA.COLUMNS\n" +
                    "WHERE TABLE_NAME = '" + selection + "grades'\n" +
                    "ORDER BY ORDINAL_POSITION;");

            while (resultSet.next())
            {
                columnNames.add(resultSet.getString(1));
            }

            for (int i = 2; i < columnNames.size(); i++)
            {
                column = new TableColumn(columnNames.get(i));

                column.setCellValueFactory(new PropertyValueFactory("grades"));
                column.setCellFactory(cellFactory);
                rosterView.getColumns().add(column);
                rosterView.setEditable(true);

                TableColumn finalColumn = column;
                column.setOnEditCommit(new EventHandler<CellEditEvent<Student, String>>()
                {
                    @Override
                    public void handle(CellEditEvent<Student, String> t)
                    {
                        t.getTableView().getItems().get(t.getTablePosition().getRow()).setGrade(t.getNewValue());

                        updateGrade(finalColumn.getText(), t.getTableView().getItems().get(t.getTablePosition().getRow()).getStudentID(), t.getNewValue());
                    }
                });

                resultSet = statement.executeQuery("select " + columnNames.get(i) + " from " + selection + "grades");

                while (resultSet.next())
                {
                    grades.add(resultSet.getString(1));
                }
            }

            resultSet = statement.executeQuery("select studentID from rosters where courseID in (select courseID from courses where courseName = '" + selection + "')");

            while (resultSet.next())
            {
                ids.add(resultSet.getInt(1));
            }

            for (int i = 0; i < ids.size(); i++)
            {
                Integer id = ids.get(i);
                resultSet = statement.executeQuery("select studentFN, studentLN from students where studentID = " + id);

                while (resultSet.next())
                {
                    String name = resultSet.getString(1) + " " + resultSet.getString(2);
                    Student newStu = new Student(id, name, grades.get(i));
                    data.add(newStu);

                    if(grades.size() > 0)
                    {

                    }
                }
            }
        }
        catch (SQLException sqlException)
        {
            String msg = sqlException.getMessage();
            System.err.printf("problem with db connection: %s\n", msg);
        }
    }

    @ActionMethod("addAssignment")
    public void addAssignment_onAction() throws Exception
    {
        Stage primaryStage = new Stage();

        FXMLLoader loader = new FXMLLoader(EditDeleteStudentController.class.getResource("../fxml/AddAssignmentPrompt.fxml"));
        Pane page = loader.load();
        Stage dialogStage = new Stage();

        dialogStage.setTitle("Edit Person");
        dialogStage.initModality(Modality.WINDOW_MODAL);

        page.setStyle("-fx-background-color: white; -fx-border-color: black;");
        dialogStage.initStyle(StageStyle.UNDECORATED);

        dialogStage.initOwner(primaryStage);

        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        AddAssignmentController controller = loader.getController();
        controller.setDialogStage(dialogStage);

        dialogStage.showAndWait();

        if(controller.isOkClicked())
        {
            TableColumn column = new TableColumn(controller.getName());

            try {
                statement = connection.createStatement();

                statement.executeUpdate("INSERT INTO assignments(assignmentName, assignmentCat, date, gradesName) values('" + controller.getName() + "', '" + controller.getCategory()
                + "', '" + controller.getDate() + "'" + ", '" + selection + "Grades')");

                statement.executeUpdate("ALTER TABLE " + selection + "Grades ADD `" + controller.getName() + "` VARCHAR(35)");
            }
            catch (SQLException sqlException)
            {
                String msg = sqlException.getMessage();
                System.err.printf("problem with db connection: %s\n", msg);
            }

            column.setCellValueFactory(new PropertyValueFactory<Student, String>("grades"));
            column.setCellFactory(cellFactory);

            rosterView.getColumns().add(column);
            rosterView.setEditable(true);

            column.setOnEditCommit(new EventHandler<CellEditEvent<Student, String>>()
            {
                @Override
                public void handle(CellEditEvent<Student, String> t)
                {
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).setGrade(t.getNewValue());
                    updateGrade(column.getText(), t.getTableView().getItems().get(t.getTablePosition().getRow()).getStudentID(), t.getNewValue());
                }
            });
        }
    }

    private void updateGrade(String column, int studentID, String grade)
    {
        try {
            statement = connection.createStatement();

            statement.executeUpdate("UPDATE " + selection + "Grades SET " + column + " = " + Integer.valueOf(grade) + " where studentID = " + studentID);
        }
        catch (SQLException sqlException)
        {
            String msg = sqlException.getMessage();
            System.err.printf("problem with db connection: %s\n", msg);
        }
    }

}
