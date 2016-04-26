package teacherToolBox.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import teacherToolBox.components.Student;

/*
 * The StudentEditDialogController class is responsible for presenting a dialog window for the selected student that needs to be edited.
 * Specifically, just editing a student from a class roster.
 *
 * <p/> Bugs: None
 *
 * @author  Michael Stevens, Josh Torrans, Matthew Fondevilla, Joanna Ho, Tom Warren, and Greg Grimsley
 */

public class StudentEditDialogController
{

    @FXML
    private TextField studentIDField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField genderField;

    private Stage dialogStage;
    private Student student;
    private boolean okClicked = false;
    private boolean deleteClicked = false;

    @FXML
    private void initialize()
    {

    }

    void setDialogStage(Stage dialogStage)
    {
        this.dialogStage = dialogStage;
    }

    void setPerson(Student student)
    {
        this.student = student;

        studentIDField.setText(student.getStudentID() + "");
        firstNameField.setText(student.getFirstName());
        lastNameField.setText(student.getLastName());
        genderField.setText(student.getGender());
    }

    boolean isOkClicked()
    {
        return okClicked;
    }

    @FXML
    private void handleOk()
    {
        student.setFirstName(firstNameField.getText());
        student.setLastName(lastNameField.getText());
        student.setGender(genderField.getText());

        okClicked = true;
        dialogStage.close();
    }

    @FXML
    private void handleCancel()
    {
        dialogStage.close();
    }

    boolean isDeleteClicked()
    {
        return deleteClicked;
    }

    @FXML
    private void handleDelete() throws Exception
    {
        deleteClicked = true;

        dialogStage.close();
    }
}
