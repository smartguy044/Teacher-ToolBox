package teacherToolBox.controller;

import com.jfoenix.controls.JFXButton;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import teacherToolBox.components.Student;

import javax.annotation.PostConstruct;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@FXMLController("../fxml/AddRoster.fxml")

public class AddRosterController
{
    @FXMLViewFlowContext
    private ViewFlowContext context;

    @FXML
    @ActionTrigger("rbAction1")
    private RadioButton radioButton1;

    @FXML
    @ActionTrigger("rbAction2")
    private RadioButton radioButton2;

    @FXML
    @ActionTrigger("browseAction")
    private JFXButton browseButton;

    @FXML
    @ActionTrigger("uploadSubmitAction")
    private JFXButton uploadSubmitButton;

    @FXML
    @ActionTrigger("manualAction")
    private JFXButton manualSubmitButton;

    @FXML
    @ActionTrigger("finishAction")
    private JFXButton finishButton;

    @FXML
    @ActionTrigger("addRosterAction")
    private JFXButton addRosterButton;

    @FXML private JFXTextField className;
    @FXML private DatePicker startDate;
    @FXML private DatePicker endDate;
    @FXML private JFXTextField filePath;
    @FXML private JFXTextField studentIdTF;
    @FXML private JFXTextField firstNameTF;
    @FXML private JFXTextField lastNameTF;
    @FXML private JFXTextField genderTF;
    @FXML private TableView<Student> rosterView;

    @FXML private JFXDialog dateDialog;
    @FXML private JFXButton acceptButton;

    ObservableList<Student> data;

    @ActionMethod("rbAction1")
    public void radioButton1_onAction() throws Exception
    {
        filePath.setDisable(false);
        browseButton.setDisable(false);
        studentIdTF.setDisable(true);
        firstNameTF.setDisable(true);
        lastNameTF.setDisable(true);
        genderTF.setDisable(true);
        manualSubmitButton.setDisable(true);
        updateButton();
    }

    @ActionMethod("rbAction2")
    public void radioButton2_onAction() throws Exception
    {
        filePath.setDisable(true);
        browseButton.setDisable(true);
        studentIdTF.setDisable(false);
        firstNameTF.setDisable(false);
        lastNameTF.setDisable(false);
        genderTF.setDisable(false);
        uploadSubmitButton.setDisable(true);
        updateButton();
    }

    @PostConstruct
    public void init()
    {
        className.focusedProperty().addListener((o, oldVal, newVal) ->
        {
            if (!newVal)
            {
                className.validate();
            }

            className.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>()
            {
                @Override
                public void handle(KeyEvent arg0)
                {
                    if (!newVal)
                    {
                        className.validate();
                    }

                    updateButton();
                }
            });
        });

        filePath.focusedProperty().addListener((o, oldVal, newVal) ->
        {
            if (!newVal)
            {
                filePath.validate();
            }

            filePath.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>()
            {
                @Override
                public void handle(KeyEvent arg0)
                {
                    if (!newVal)
                    {
                        filePath.validate();
                    }

                    updateButton();
                }
            });
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

        acceptButton.setOnMouseClicked((e)->{
            dateDialog.close();
        });
    }

    private void updateButton()
    {
        if(radioButton1.isSelected())
        {
            if(!filePath.getText().equals("") && !className.getText().equals(""))
            {
                uploadSubmitButton.setDisable(false);
            }
            else
            {
                uploadSubmitButton.setDisable(true);
            }
        }
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
        }
    }

    @ActionMethod("browseAction")
    public void browseButton_onAction() throws Exception
    {
        Stage st = (Stage) browseButton.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Roster File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel Documents", "*.xlsx", "*.csv"));

        File selectedFile = fileChooser.showOpenDialog(st);

        if(selectedFile != null)
        {
            filePath.setText(selectedFile.getAbsolutePath());
            updateButton();
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

        try
        {
            if(extension.equals(".csv"))
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
            else if(extension.equals(".xlsx"))
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

                    // This trick ensures that we get the data properly even if it doesn't start from first few rows
                    for(int i = 0; i < 10 || i < rows; i++)
                    {
                        row = sheet.getRow(i);

                        if(row != null)
                        {
                            temp = sheet.getRow(i).getPhysicalNumberOfCells();

                            if(temp > cols)
                            {
                                cols = temp;
                            }
                        }
                    }

                    for(int r = 0; r < rows; r++)
                    {
                        row = sheet.getRow(r);

                        if(row != null)
                        {
                            for(int i = 0; i < cols; i++)
                            {
                                cell = row.getCell((short)i);

                                if(cell != null)
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
                                    if(i % 3 == 0 && i != 0)
                                    {
                                        data = rosterView.getItems();
                                        data.add(new Student(student.getStudentID(), student.getFirstName(), student.getLastName(), student.getGender()));
                                    }
                                }
                            }
                        }
                    }
                }
                catch(Exception ioe)
                {
                    ioe.printStackTrace();
                }
            }

            finishButton.setDisable(false);
            addRosterButton.setDisable(false);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (br != null)
            {
                try
                {
                    br.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    @ActionMethod("manualAction")
    public void manualSubmitButton_onAction() throws Exception
    {
        data = rosterView.getItems();
        data.add(new Student(Integer.valueOf(studentIdTF.getText()), firstNameTF.getText(), lastNameTF.getText(), genderTF.getText()));

        studentIdTF.setText("");
        firstNameTF.setText("");
        lastNameTF.setText("");
        genderTF.setText("");

        updateButton();

        finishButton.setDisable(false);
        addRosterButton.setDisable(false);
    }

    @ActionMethod("finishAction")
    public void finishButton_onAction() throws Exception
    {
        if(startDate.getValue() == null || endDate.getValue() == null)
        {
            dateDialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
            dateDialog.show((Pane) context.getRegisteredObject("ContentPane"));
        }
        else
        {
            data = rosterView.getItems();
            Properties properties = new Properties();
            Connection connection = null;
            Statement statement = null;

            int courseID = 0;

            try
            {
                properties.load(new FileInputStream(".//src//database.properties"));
                String url = properties.getProperty("jdbc.url");
                Class.forName(properties.getProperty("jdbc.driver"));
                connection = DriverManager.getConnection(url, properties.getProperty("jdbc.username"), properties.getProperty("jdbc.password"));

                statement = connection.createStatement();

                statement.executeUpdate("INSERT INTO courses(courseName, userID) values ('" + className.getText() + "', " + 1001 + ")");

                ResultSet resultSet = statement.executeQuery("select courseID from courses where courseName = '" + className.getText() + "'");

                while (resultSet.next())
                {
                    courseID = resultSet.getInt(1);
                }

                for (Student aData : data)
                {
                    statement.executeUpdate("INSERT INTO students(studentID, studentFN, StudentLN, studentGen) values("
                            + aData.getStudentID() + ", '" + aData.getFirstName() + "', '" + aData.getLastName() +
                            "', '"
                            + aData.getGender() + "')");

                    statement.executeUpdate("INSERT INTO rosters(courseID, studentID) values (" + courseID + ", " + aData.getStudentID() + ")");
                }

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                String start = startDate.getValue().format(formatter);

                statement.executeUpdate("CREATE TABLE " + className.getText() + "Attendance (`" + start + "` varchar(1))");

                List<String> dates = datesBetween(startDate.getValue(), endDate.getValue());

                for(int i = 1; i < dates.size(); i++)
                {
                    statement.executeUpdate("ALTER TABLE " + className.getText() + "Attendance ADD `" + dates.get(i) + "` VARCHAR(1)");
                }

                clearForm();
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

    @ActionMethod("addRosterAction")
    public void addRosterButton_onAction() throws Exception
    {
        clearForm();
    }

    private static List<String> datesBetween(LocalDate start, LocalDate end)
    {
        List<String> ret = new ArrayList<>();
        List<String> days = Arrays.asList("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1))
        {
            if(days.contains(date.getDayOfWeek().toString()))
            {
                String date1 = date.format(formatter);
                ret.add(date1);
            }
        }

        return ret;
    }

    private void clearForm()
    {
        data.clear();

        className.setText("");

        filePath.setText("");
        uploadSubmitButton.setDisable(true);

        studentIdTF.setText("");
        firstNameTF.setText("");
        lastNameTF.setText("");
        genderTF.setText("");

        filePath.setDisable(false);
        browseButton.setDisable(false);
        studentIdTF.setDisable(true);
        firstNameTF.setDisable(true);
        lastNameTF.setDisable(true);
        genderTF.setDisable(true);
        manualSubmitButton.setDisable(true);

        radioButton1.setSelected(true);

        finishButton.setDisable(true);
        addRosterButton.setDisable(true);

        startDate.setValue(null);
        startDate.setPromptText("Start Date");
        endDate.setValue(null);
        endDate.setPromptText("End Date");
    }
}
