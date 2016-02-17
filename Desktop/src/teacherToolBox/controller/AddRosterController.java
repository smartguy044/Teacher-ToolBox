package teacherToolBox.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.controller.util.VetoException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.Pane;

import javax.annotation.PostConstruct;

@FXMLController("../fxml/AddRoster.fxml")

public class AddRosterController
{
    @FXML
    @ActionTrigger("rbAction1")
    private RadioButton radioButton1;

    @FXML
    @ActionTrigger("rbAction2")
    private RadioButton radioButton2;

    @FXML private JFXTextField filePath;
    @FXML private JFXButton browseButton;
    @FXML private JFXTextField studentIdTF;
    @FXML private JFXTextField firstNameTF;
    @FXML private JFXTextField lastNameTF;
    @FXML private JFXTextField genderTF;

    @ActionMethod("rbAction1")
    public void radioButton1_onAction() throws Exception
    {
        filePath.setDisable(false);
        browseButton.setDisable(false);
        studentIdTF.setDisable(true);
        firstNameTF.setDisable(true);
        lastNameTF.setDisable(true);
        genderTF.setDisable(true);
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
    }

    @FXMLViewFlowContext
    private ViewFlowContext context;

    @PostConstruct
    public void init()
    {
       studentIdTF.focusedProperty().addListener((o, oldVal, newVal) ->
        {
            if (!newVal)
            {
                studentIdTF.validate();
            }
        });

        firstNameTF.focusedProperty().addListener((o, oldVal, newVal) ->
        {
            if (!newVal)
            {
                firstNameTF.validate();
            }
        });

        lastNameTF.focusedProperty().addListener((o, oldVal, newVal) ->
        {
            if (!newVal)
            {
                lastNameTF.validate();
            }
        });

        genderTF.focusedProperty().addListener((o, oldVal, newVal) ->
        {
            if (!newVal)
            {
                genderTF.validate();
            }
        });
    }
}
