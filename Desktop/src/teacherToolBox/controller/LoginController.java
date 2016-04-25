package teacherToolBox.controller;

import com.jfoenix.controls.JFXDecorator;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
import io.datafx.controller.flow.container.DefaultFlowContainer;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Screen;
import javafx.stage.Stage;
import teacherToolBox.Main;
import teacherToolBox.maincontroller.MainAddRosterController;
import teacherToolBox.maincontroller.MainSignUpController;

import java.awt.*;

/*
 * The LoginController class requires the user to enter his/her credentials for verification.	 *
 * The user needs to input his/her username and password in order to access the other TeacherToolbox functionality.
 *
 * <p/> Bugs: None
 *
 * @author  Michael Stevens, Josh Torrans, Matthew Fondevilla, Joanna Ho, Tom Warren, and Greg Grimsley
 */

@FXMLController("../fxml/Login.fxml")
public class LoginController
{
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
        Stage primaryStage = new Stage();
        Flow flow = new Flow(MainAddRosterController.class);
        DefaultFlowContainer container = new DefaultFlowContainer();
        flowContext = new ViewFlowContext();
        flowContext.register("Stage", primaryStage);
        flow.createHandler(flowContext).start(container);

        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();

        Scene scene = new Scene(new JFXDecorator(primaryStage, container.getView()), ((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2), ((primScreenBounds.getHeight() - primaryStage.getHeight()) / 4));

        scene.getStylesheets().add(Main.class.getResource("/resources/css/teacherToolBox-main.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

        Stage st = (Stage) loginButton.getScene().getWindow();
        st.hide();
    }

    @ActionMethod("signUpAction")
    public void signUpButton_onAction() throws Exception
    {
        Stage primaryStage = new Stage();
        Flow flow = new Flow(MainSignUpController.class);
        DefaultFlowContainer container = new DefaultFlowContainer();
        flowContext = new ViewFlowContext();
        flowContext.register("Stage", primaryStage);
        flow.createHandler(flowContext).start(container);

        Scene scene = new Scene(new JFXDecorator(primaryStage, container.getView()), 375, 700);
        scene.getStylesheets().add(Main.class.getResource("/resources/css/teacherToolBox-main.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

        Stage st = (Stage) signUpButton.getScene().getWindow();
        st.hide();
    }
}

