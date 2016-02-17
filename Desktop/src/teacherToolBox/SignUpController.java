package teacherToolBox;

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
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javax.annotation.PostConstruct;

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
        });

        emailTF.focusedProperty().addListener((o, oldVal, newVal) ->
        {
            if (!newVal)
            {
                emailTF.validate();
            }
        });

        passwordTF.focusedProperty().addListener((o, oldVal, newVal) ->
        {
            if (!newVal)
            {
                passwordTF.validate();
            }
        });

        confirmPassTF.focusedProperty().addListener((o, oldVal, newVal) ->
        {
            if (!newVal)
            {
                confirmPassTF.validate();
            }
        });

        secAnsTF.focusedProperty().addListener((o, oldVal, newVal) ->
        {
            if (!newVal)
            {
                secAnsTF.validate();
            }
        });
    }
}
