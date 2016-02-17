package teacherToolBox;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
import io.datafx.controller.flow.container.DefaultFlowContainer;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.annotation.PostConstruct;
import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

@FXMLController("SignUp.fxml")

public class SignUpController
{
    @FXMLViewFlowContext
    private ViewFlowContext flowContext;

    @FXML private JFXTextField nameTF;
    @FXML private JFXTextField emailTF;
    @FXML private JFXPasswordField passwordTF;
    @FXML private JFXPasswordField confirmPassTF;
    @FXML private JFXTextField secAnsTF;
    @FXML private JFXComboBox<String> secCB;

    @FXML
    @ActionTrigger("signUpAction")
    private Button signUpButton;

    @FXML
    @ActionTrigger("backAction")
    private Button backButton;

    @ActionMethod("backAction")
    public void backButton_onAction() throws Exception
    {
        Stage primaryStage = new Stage();
        Flow flow = new Flow(MainController.class);
        DefaultFlowContainer container = new DefaultFlowContainer();
        flowContext = new ViewFlowContext();
        flowContext.register("Stage", primaryStage);
        flow.createHandler(flowContext).start(container);

        Scene scene = new Scene(new JFXDecorator(primaryStage, container.getView()), 375, 550);
        scene.getStylesheets().add(Main.class.getResource("/resources/css/teacherToolBox-main.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

        Stage st = (Stage) backButton.getScene().getWindow();
        st.hide();
    }

    @PostConstruct
    public void init()
    {
        nameTF.focusedProperty().addListener((o, oldVal, newVal) ->
        {
            if (!newVal)
            {
                nameTF.validate();
            }

            nameTF.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>()
            {
                @Override
                public void handle(KeyEvent arg0)
                {
                    updateButton();
                }
            });
        });

        emailTF.focusedProperty().addListener((o, oldVal, newVal) ->
        {
            if (!newVal)
            {
                emailTF.validate();
            }

            emailTF.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>()
            {
                @Override
                public void handle(KeyEvent arg0)
                {
                    updateButton();
                }
            });
        });

        passwordTF.focusedProperty().addListener((o, oldVal, newVal) ->
        {
            if (!newVal)
            {
                passwordTF.validate();
            }

            passwordTF.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>()
            {
                @Override
                public void handle(KeyEvent arg0)
                {
                    updateButton();
                }
            });
        });

        confirmPassTF.focusedProperty().addListener((o, oldVal, newVal) ->
        {
            if (!newVal)
            {
                confirmPassTF.validate();
            }

            confirmPassTF.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>()
            {
                @Override
                public void handle(KeyEvent arg0)
                {
                    updateButton();
                }
            });
        });

        secCB.focusedProperty().addListener((o, oldVal, newVal) ->
        {
            secCB.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent arg0)
                {
                    updateButton();
                }
            });
        });

        secAnsTF.focusedProperty().addListener((o, oldVal, newVal) ->
        {
            if (!newVal)
            {
                secAnsTF.validate();
            }

            secAnsTF.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>()
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
        if(!nameTF.getText().equals("") && !emailTF.getText().equals("") && !passwordTF.getText().equals("") && !confirmPassTF.getText().equals("")
                && !secAnsTF.getText().equals("") && !secCB.getValue().equals("Select Security Question..."))
        {
            signUpButton.setDisable(false);
        }
        else
        {
            signUpButton.setDisable(true);
        }
    }
}
