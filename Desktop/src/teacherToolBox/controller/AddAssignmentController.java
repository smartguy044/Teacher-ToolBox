package teacherToolBox.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.annotation.PostConstruct;

public class AddAssignmentController
{
    @FXML
    private TextField assignmentName;
    @FXML
    private JFXComboBox<String> assignmentCB;
    @FXML
    private JFXComboBox<String> monthCB;
    @FXML
    private JFXComboBox<String> dayCB;
    @FXML
    private TextField yearTF;

    private Stage dialogStage;
    private boolean okClicked = false;
    private String name;
    private String category;
    private String date;

    private static final String[] MONTHS = {"January", "February", "March", "April", "May", "June",
                                            "July", "August", "September", "October", "November",
                                            "December"};

    private static final String[] DAYS = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11",
                                          "12", "13", "14", "15", "16", "17", "18", "19", "20",
                                          "21", "22", "23", "24", "25", "26", "27", "28", "29",
                                          "30", "31"};

    private static final String[] CATEGORIES = {"Daily Assignment", "Homework", "Test"};

    @FXML
    private void initialize()
    {
        monthCB.getItems().addAll(MONTHS);
        assignmentCB.getItems().addAll(CATEGORIES);
        dayCB.getItems().addAll(DAYS);
    }

    @FXML
    private void submitButton()
    {
        setName(assignmentName.getText());
        setCategory(assignmentCB.getSelectionModel().getSelectedItem() + "");
        setDate((monthCB.getSelectionModel().getSelectedIndex() + 1) + "/" + dayCB.getSelectionModel().getSelectedItem()
            + "/" + yearTF.getText());

        okClicked = true;
        dialogStage.close();
    }

    @FXML
    private void cancelButton()
    {
        dialogStage.close();
    }

    @FXML
    private void resetButton()
    {
        assignmentName.clear();
        assignmentName.setPromptText("Assignment Name");
        assignmentCB.getSelectionModel().clearSelection();
        monthCB.getSelectionModel().clearSelection();
        dayCB.getSelectionModel().clearSelection();
        yearTF.clear();
        yearTF.setPromptText("Year");
    }

    void setDialogStage(Stage dialogStage)
    {
        this.dialogStage = dialogStage;
    }

    boolean isOkClicked()
    {
        return okClicked;
    }

    private void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    private void setCategory(String category)
    {
        this.category = category;
    }

    public String getCategory()
    {
        return category;
    }

    private void setDate(String date)
    {
        this.date = date;
    }

    public String getDate()
    {
        return date;
    }
}
