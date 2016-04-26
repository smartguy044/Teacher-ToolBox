package teacherToolBox.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
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
    private TableView rosterView;

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
        ObservableList<ObservableList> data = rosterView.getItems();
        data.clear();

        ArrayList<Integer> ids = new ArrayList<>();
        ArrayList<String> columnNames = new ArrayList<>();
        ArrayList<String> grades = new ArrayList<>();
        TableColumn column = null;

        selection = classCB.getSelectionModel().getSelectedItem();

        try
        {
            statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT COLUMN_NAME\n" +
                    "FROM INFORMATION_SCHEMA.COLUMNS\n" +
                    "WHERE TABLE_NAME = '" + selection + "grades'\n" +
                    "ORDER BY ORDINAL_POSITION;");

            while (resultSet.next())
            {
                columnNames.add("`" + resultSet.getString(1) + "`");
            }

            String SQL = "SELECT * from " + selection + "grades";

            resultSet = connection.createStatement().executeQuery(SQL);

            for (int i = 0; i < columnNames.size(); i++)
            {
                column = new TableColumn(columnNames.get(i).replace("`", ""));

                final int j = i;

                column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>()

                {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param)
                    {
                        return new SimpleStringProperty((String) param.getValue().get(j));
                    }
                });

                column.setCellFactory(cellFactory);
                rosterView.getColumns().add(column);
                rosterView.setEditable(true);

                TableColumn finalColumn = column;
                column.setOnEditCommit(new EventHandler<CellEditEvent<ObservableList, String>>()
                {
                    @Override
                    public void handle(CellEditEvent<ObservableList, String> t)
                    {
                        t.getTableView().getItems().get(t.getTablePosition().getRow()).add(t.getNewValue());

                        updateGrade(finalColumn.getText(), Integer.valueOf(t.getTableView().getItems().get(t.getTablePosition().getRow()).get(0).toString()), t.getNewValue());
                    }
                });
            }

            while (resultSet.next())
            {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++)
                {
                    row.add(resultSet.getString(i));
                }
                data.add(row);
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Error on Building Data");
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

                statement.executeUpdate("INSERT INTO assignments(assignmentName, assignmentCat, assignmentDate, gradesName) values('" + controller.getName() + "', '" + controller.getCategory()
                + "', '" + controller.getDate() + "'" + ", '" + selection + "Grades')");

                statement.executeUpdate("ALTER TABLE " + selection + "Grades ADD `" + controller.getName() + "` VARCHAR(35)");
            }
            catch (SQLException sqlException)
            {
                String msg = sqlException.getMessage();
                System.err.printf("problem with db connection: %s\n", msg);
            }

            column.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>,
                    ObservableValue<String>>) param -> new SimpleStringProperty("0"));

            column.setCellFactory(cellFactory);

            rosterView.getColumns().add(column);

            TableColumn finalColumn = column;
            column.setOnEditCommit(new EventHandler<CellEditEvent<ObservableList, String>>()
            {
                @Override
                public void handle(CellEditEvent<ObservableList, String> t)
                {
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).add(t.getNewValue());

                    updateGrade(finalColumn.getText(), Integer.valueOf(t.getTableView().getItems().get(t.getTablePosition().getRow()).get(0).toString()), t.getNewValue());
                }
            });
        }
    }

    private void updateGrade(String column, int studentID, String grade)
    {
        try
        {
            statement.executeUpdate("UPDATE " + selection + "Grades SET `" + column + "` = " + Integer.valueOf(grade) + " where studentID = " + studentID);
        }
        catch (SQLException sqlException)
        {
            String msg = sqlException.getMessage();
            System.err.printf("problem with db connection: %s\n", msg);
        }
    }

}
