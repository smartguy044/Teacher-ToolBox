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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.input.KeyEvent;
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
    @FXML private JFXButton uploadSubmitButton;
    @FXML private JFXTextField studentIdTF;
    @FXML private JFXTextField firstNameTF;
    @FXML private JFXTextField lastNameTF;
    @FXML private JFXTextField genderTF;
    @FXML private JFXButton manualSubmitButton;

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

    @FXMLViewFlowContext
    private ViewFlowContext context;

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
}
