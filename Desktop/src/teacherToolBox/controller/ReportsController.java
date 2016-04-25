package teacherToolBox.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import teacherToolBox.components.Student;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javax.annotation.PostConstruct;
import javax.jnlp.FileContents;
import javax.jnlp.FileSaveService;
import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private JFXComboBox<String> classCB3;

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
    @ActionTrigger("uploadSubmitAction")
    private JFXButton uploadSubmitButton;

    @FXML
    @ActionTrigger("uploadSubmitAction")
    private JFXButton uploadSubmitButton2;

    @FXML
    @ActionTrigger("finishAction")
    private JFXButton finishButton;

    @FXML
    private JFXTextField filePath;

    @FXML
    private DatePicker startDate, endDate;

    private Properties properties;
    private Connection connection;
    private Statement statement;

    private ObservableList<Student> data;

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
            classCB3.getItems().addAll(classes);

            activityCB.getItems().add(0, "no");
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

        // Validations for 1st tab in Reports.fxml
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

        filePath.focusedProperty().addListener((o, oldVal, newVal) -> //////////////////needs work//////////
        {
            if (!newVal)
            {
                filePath.validate();
            }

            filePath.addEventFilter(KeyEvent.KEY_RELEASED, arg0 ->
            {
                if (!newVal)
                {
                    filePath.validate();
                }

                updateButton();
            });
        });

        // Validations for 2nd tab in Reports.fxml

    }

    @ActionMethod("selectionAction")
    public void selectBtn_onAction() throws Exception
    {
        ObservableList<Student> data = rosterView.getItems();
        data.clear();

        ArrayList<Integer> ids = new ArrayList<>();

        String selection = classCB.getSelectionModel().getSelectedItem();

        try {
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

                while (resultSet.next()) {
                    data.add(new Student(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet
                            .getString(4)));
                }
            }
        }
        catch (SQLException sqlException)
        {
            String msg = sqlException.getMessage();
            System.err.printf("problem with db connection: %s\n", msg);
        }
    }

    /*
    @ActionMethod("browseAction")
    public void browseButton_onAction() throws Exception
    {
        Stage st = (Stage) browseButton.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Roster File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel Documents", "*.xlsx", "*.csv"));

        File selectedFile = fileChooser.showOpenDialog(st);

        if (selectedFile != null)
        {
            filePath.setText(selectedFile.getAbsolutePath());
        }
    }*/

    @ActionMethod("browseAction")
    public void browseButton_onAction() throws Exception
    {
        Stage st = (Stage) browseButton.getScene().getWindow();

        browseButton.setOnAction(event ->
        {
            FileChooser fileChooser = new FileChooser();

            //Set extension filter
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel Documents", "*.xlsx", "*.csv");
            fileChooser.getExtensionFilters().add(extFilter);

            //Show save file dialog
            File file = fileChooser.showSaveDialog(st);

            if(file != null)
            {
                SaveFile(data.toString(), file);
            }
        });
    }

    private void SaveFile(String content, File file)
    {
        try
        {
            FileWriter fileWriter = null;

            fileWriter = new FileWriter(file);
            for (Student std : data)
            {
                String txt = std.getFirstName() + ",";
            }
            fileWriter.write(content);
            fileWriter.close();
        }
        catch (IOException ex)
        {
            Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @ActionMethod("uploadSubmitAction")
    public void uploadSubmitButton_onAction() throws Exception
    {
        String csvFile = filePath.getText();
        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";
        String extension = csvFile.substring(csvFile.lastIndexOf('.'), csvFile.length());

        try {
            if (extension.equals(".csv"))
            {
                br = new BufferedReader(new FileReader(csvFile));

                while ((line = br.readLine()) != null)
                {
                    // use comma as separator
                    String[] student = line.split(csvSplitBy);

                    data = rosterView.getItems();
                    data.add(new Student(Integer.valueOf(student[0]), student[1], student[2], student[3]));
                }
            }
            else if (extension.equals(".xlsx"))
            {
                try {
                    FileInputStream fs = new FileInputStream(csvFile);
                    XSSFWorkbook wb = new XSSFWorkbook(fs);
                    XSSFSheet sheet = wb.getSheetAt(0);
                    XSSFRow row;
                    XSSFCell cell;

                    Student student = new Student();

                    int rows;
                    rows = sheet.getPhysicalNumberOfRows();

                    int cols = 0;
                    int temp = 0;

                    for (int i = 0; i < 10 || i < rows; i++)
                    {
                        row = sheet.getRow(i);

                        if (row != null)
                        {
                            temp = sheet.getRow(i).getPhysicalNumberOfCells();

                            if (temp > cols)
                            {
                                cols = temp;
                            }
                        }
                    }

                    for (int r = 0; r < rows; r++)
                    {
                        row = sheet.getRow(r);

                        if (row != null)
                        {
                            for (int i = 0; i < cols; i++)
                            {
                                cell = row.getCell((short) i);

                                if (cell != null)
                                {
                                    if (cell.getColumnIndex() == 0)
                                    {
                                        student.setStudentID((int) cell.getNumericCellValue());
                                    }
                                    else if (cell.getColumnIndex() == 1)
                                    {
                                        student.setFirstName(String.valueOf(cell.getStringCellValue()));
                                    }
                                    else if (cell.getColumnIndex() == 2)
                                    {
                                        student.setLastName(String.valueOf(cell.getStringCellValue()));
                                    }
                                    else if (cell.getColumnIndex() == 3)
                                    {
                                        student.setGender(String.valueOf(cell.getStringCellValue()));
                                    }
                                    if (i % 3 == 0 && i != 0)
                                    {
                                        data = rosterView.getItems();
                                        data.add(new Student(student.getStudentID(), student.getFirstName(), student.getLastName(), student.getGender()));
                                    }
                                }
                            }
                        }
                    }
                }
                catch (Exception ioe)
                {
                    ioe.printStackTrace();
                }
            }

            finishButton.setDisable(false);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (br != null)
            {
                try {
                    br.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private void updateButton()
    {
        if (!classCB.getValue().equals("Roster"))
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


        }/*
        else if(radioButton2.isSelected())
        {
            if (!className.getText().equals("") && !studentIdTF.getText().equals("") && !firstNameTF.getText().equals("") && !lastNameTF.getText().equals("") && !genderTF.getText().equals(""))

            {
                manualSubmitButton.setDisable(false);
            }
            else
            {
                manualSubmitButton.setDisable(true);
            }
        }*/
    }
}
