package teacherToolBox;

import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXRippler;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
import io.datafx.controller.flow.container.DefaultFlowContainer;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.annotation.PostConstruct;

@FXMLController("Login.fxml")

public class LoginController
{
    @FXMLViewFlowContext
    private ViewFlowContext context;

    @FXMLViewFlowContext
    private ViewFlowContext flowContext;

    @FXML
    @ActionTrigger("loginAction")
    private Button loginButton;

    @FXML
    @ActionTrigger("signUpAction")
    private Button signUpButton;

    @ActionMethod("loginAction")
    public void loginButton_onAction() throws Exception
    {
        loginButton.setOnMouseClicked(event -> ((Node) (event.getSource())).getScene().getWindow().hide());

        Stage primaryStage = new Stage();
        Flow flow = new Flow(MainAddRosterController.class);
        DefaultFlowContainer container = new DefaultFlowContainer();
        flowContext = new ViewFlowContext();
        flowContext.register("Stage", primaryStage);
        flow.createHandler(flowContext).start(container);

        Scene scene = new Scene(new JFXDecorator(primaryStage, container.getView()), 750, 800);
        scene.getStylesheets().add(Main.class.getResource("/resources/css/teacherToolBox-main.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @ActionMethod("signUpAction")
    public void signUpButton_onAction() throws Exception
    {
        loginButton.setOnMouseClicked(event -> ((Node) (event.getSource())).getScene().getWindow().hide());

        Stage primaryStage = new Stage();
        Flow flow = new Flow(MainSignUpController.class);
        DefaultFlowContainer container = new DefaultFlowContainer();
        flowContext = new ViewFlowContext();
        flowContext.register("Stage", primaryStage);
        flow.createHandler(flowContext).start(container);

        Scene scene = new Scene(new JFXDecorator(primaryStage, container.getView()), 375, 550);
        scene.getStylesheets().add(Main.class.getResource("/resources/css/teacherToolBox-main.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

