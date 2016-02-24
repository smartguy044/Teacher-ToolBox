package teacherToolBox.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import teacherToolBox.components.Student;

import javax.annotation.PostConstruct;
import java.io.*;

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

    @FXML private JFXTextField filePath;
    @FXML private JFXTextField studentIdTF;
    @FXML private JFXTextField firstNameTF;
    @FXML private JFXTextField lastNameTF;
    @FXML private JFXTextField genderTF;
    @FXML private TableView<Student> rosterView;

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
        filePath.focusedProperty().addListener((o, oldVal, newVal) ->
        {
            filePath.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>()
            {
                @Override
                public void handle(KeyEvent arg0)
                {
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
                    updateButton();
                }
            });
        });
    }

    public void updateButton()
    {
        if(radioButton1.isSelected())
        {
            if(!filePath.getText().equals(""))
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
            if (!studentIdTF.getText().equals("") && !firstNameTF.getText().equals("") && !lastNameTF.getText().equals("") && !genderTF.getText().equals(""))

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

                    ObservableList<Student> data = rosterView.getItems();
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
                                        ObservableList<Student> data = rosterView.getItems();
                                        data.add(new Student(student.getStudentID(), student.getFirstName(), student.getLastName(), student.getGender()));
                                    }
                                }
                            }
                        }
                    }
                } catch(Exception ioe)
                {
                    ioe.printStackTrace();
                }
            }
        } catch (FileNotFoundException e)
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
        ObservableList<Student> data = rosterView.getItems();
        data.add(new Student(Integer.valueOf(studentIdTF.getText()), firstNameTF.getText(), lastNameTF.getText(), genderTF.getText()));

        studentIdTF.setText("");
        firstNameTF.setText("");
        lastNameTF.setText("");
        genderTF.setText("");
    }
}
