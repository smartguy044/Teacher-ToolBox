package teacherToolBox.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import teacherToolBox.components.Student;

import javax.annotation.PostConstruct;
import java.io.*;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.*;

/*
 * The ReportsController class allows the user to generate various reports depending on what columns or rows the user selected.
 * The user will also be able to select the class, enter the report name, filter the dates, and customize the output.
 * The reports can be generate in an excel, png, or jpeg format.
 *
 * <p/> Bugs: None
 *
 * @author  Michael Stevens, Josh Torrans, Matthew Fondevilla, Joanna Ho, Tom Warren, and Greg Grimsley
 */

@FXMLController("../fxml/Reports.fxml")
public class ReportsController
{
    @FXMLViewFlowContext
    private ViewFlowContext flowContext;

    @FXML
    private JFXComboBox<String> classCB;

    @FXML
    private JFXComboBox<String> activityCB;

    @FXML
    private JFXComboBox<String> classCB2;

    @FXML
    private JFXRadioButton topRB;

    @FXML
    private JFXRadioButton bottomRB;

    @FXML
    private JFXRadioButton allRB;

    @FXML
    private JFXRadioButton finalRB;

    @FXML
    private TableView<Student> rosterView;

    @FXML
    @ActionTrigger("selectionAction")
    private JFXButton selectBtn;

    @FXML
    @ActionTrigger("browseAction")
    private JFXButton browseButton;

    @FXML
    @ActionTrigger("browseAction")
    private JFXButton browseButton2;

    @FXML
    @ActionTrigger("resetAction")
    private JFXButton resetButton;

    @FXML
    @ActionTrigger("resetAction")
    private JFXButton resetButton2;

    @FXML
    private DatePicker startDate, endDate;

    private Properties properties;
    private Connection connection;
    private Statement statement;

    private ObservableList<Student> data;

    private static final String[] CATEGORIES = {"Daily Assignment", "Homework", "Test"};

    @PostConstruct
    public void init()
    {
        properties = new Properties();
        connection = null;
        statement = null;

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
            classCB2.getItems().addAll(classes);

            activityCB.getItems().addAll(CATEGORIES);
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

        classCB.valueProperty().addListener((ov, t, t1) ->
        {
            updateButton();
        });

        activityCB.valueProperty().addListener((ov, t, t1) ->
        {
            updateButton();
        });

        startDate.valueProperty().addListener((ov, o, n) ->
        {
            updateButton();
        });

        endDate.valueProperty().addListener((ov, o, n) ->
        {
            updateButton();
        });

        resetButton.setOnAction(event -> clearForm());
        resetButton2.setOnAction(event -> clearForm());
    }

    @ActionMethod("selectionAction")
    public void selectBtn_onAction() throws Exception
    {
        ObservableList<Student> data = rosterView.getItems();
        data.clear();

        ArrayList<Integer> ids = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        ArrayList<Double> grades = new ArrayList<>();
        ArrayList<Double> avg = new ArrayList<>();

        String selection = classCB.getSelectionModel().getSelectedItem();

        try
        {
            ResultSet resultSet = statement.executeQuery("select studentID from rosters where courseID in (select " +
                    "courseID from courses where courseName = '" + selection + "')");

            while (resultSet.next())
            {
                ids.add(resultSet.getInt(1));
            }

            resultSet = statement.executeQuery("select assignmentName from assignments where assignmentCat = '"
                    + activityCB.getSelectionModel().getSelectedItem() + "' " +
                    "and gradesName = '" + selection + "grades' and assignmentDate " +
                    "between '" + startDate.getValue() + "' and '" + endDate.getValue() + "'");

            while (resultSet.next())
            {
                names.add(resultSet.getString(1));
            }

            for (String name1 : names)
            {
                resultSet = statement.executeQuery("select `" + name1 + "` from " + selection + "grades");

                while (resultSet.next())
                {
                    grades.add(resultSet.getDouble(1));
                }
            }

            int j = 0;

            for(int i = 0; i < grades.size(); i++)
            {
                if(i >= ids.size())
                {
                    if(j < ids.size())
                    {
                        avg.set(j, grades.get(i) + avg.get(j));
                    }
                    else
                    {
                        j = 0;
                        avg.set(j, grades.get(i) + avg.get(j));
                    }

                    j++;
                }
                else
                {
                    avg.add(grades.get(i));
                }
            }

            DecimalFormat oneDeci = new DecimalFormat("###.#");

            for(int i = 0; i < avg.size(); i++)
            {
                avg.set(i, Double.valueOf(oneDeci.format(avg.get(i) / names.size())));
            }

            for (int i = 0; i < ids.size(); i++)
            {
                resultSet = statement.executeQuery("select studentFN, studentLN from students where studentID = " + ids.get(i));

                while (resultSet.next())
                {
                    String name = resultSet.getString(1) + " " + resultSet.getString(2);
                    data.add(new Student(ids.get(i), name, activityCB.getSelectionModel().getSelectedItem(), avg.get(i)));
                }
            }
        }
        catch (SQLException sqlException)
        {
            String msg = sqlException.getMessage();
            System.err.printf("problem with db connection: %s\n", msg);
        }
    }

    @ActionMethod("browseAction")
    public void browseButton_onAction() throws Exception
    {
        Stage st = (Stage) browseButton.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel Document", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(st);

        data = rosterView.getItems();

        if(file != null)
        {
            SaveFile(file);
        }
    }

    @ActionMethod("browseAction")
    public void browseButton2_onAction() throws Exception
    {
        Stage st = (Stage) browseButton2.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel Documents", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(st);

        data = rosterView.getItems();

        if(file != null)
        {
            SaveFile(file);
        }
    }

    private void SaveFile(File file)
    {
        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFSheet sheet = workbook.createSheet("Report Data");

        data = rosterView.getItems();

        Map<String, Object[]> data1 = new TreeMap<>();

        data1.put(0 + "", new Object[]{"Student ID", "Student Name", "Activity", "Average Grade"});

        for(int i = 0; i < data.size(); i++)
        {
            data1.put(i + 1 + "", new Object[]{data.get(i).getStudentID(), data.get(i).getFullName(), data.get(i).getActivity(), data.get(i).getAvgGrade()});
        }

        Set<String> keySet = data1.keySet();

        int rowNum = 0;
        for (String key : keySet)
        {
            XSSFRow row = sheet.createRow(rowNum++);

            Object[] objArr = data1.get(key);

            int cellNum = 0;

            for (Object obj : objArr)
            {
                XSSFCell cell = row.createCell(cellNum++);
                if (obj instanceof String)
                {
                    cell.setCellValue((String) obj);
                }
                else if (obj instanceof Integer)
                {
                    cell.setCellValue((Integer) obj);
                }
                else if (obj instanceof Double)
                {
                    cell.setCellValue((Double) obj);
                }
            }
        }
        try
        {
            FileOutputStream out = new FileOutputStream(file);
            workbook.write(out);
            out.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void updateButton()
    {
        if (!classCB.getValue().equals("Roster")) /////////////////throws errors but still works///////////////
        {
            if (!activityCB.getValue().equals("Activity"))
            {
                if (startDate.getValue() != null)
                {
                    if (endDate.getValue() != null)
                    {
                        selectBtn.setDisable(false);
                    }
                }
            }
            else
            {
                selectBtn.setDisable(true);
            }
        }
    }

    private void clearForm()
    {
        classCB.getSelectionModel().clearSelection();
        classCB.setPromptText("Roster");
        activityCB.getSelectionModel().clearSelection();
        activityCB.setPromptText("Activity");
        startDate.setValue(null);
        startDate.setPromptText("Start Date");
        endDate.setValue(null);
        endDate.setPromptText("End Date");

        classCB2.getSelectionModel().clearSelection();
        allRB.selectedProperty().setValue(false);
        finalRB.selectedProperty().setValue(false);
        topRB.selectedProperty().setValue(false);
        bottomRB.selectedProperty().setValue(false);
    }
}
