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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import teacherToolBox.Main;
import teacherToolBox.maincontroller.MainAddAssignmentController;

@FXMLController("../fxml/Grades.fxml")
public class GradesController
{

    @FXMLViewFlowContext
    private ViewFlowContext flowContext;

    @FXML
    @ActionTrigger("addAssignment")
    private Button addAssignment;

    @ActionMethod("addAssignment")
    public void addAssignment_onAction() throws Exception
    {
        Stage primaryStage = new Stage();
        Flow flow = new Flow(MainAddAssignmentController.class);
        DefaultFlowContainer container = new DefaultFlowContainer();
        flowContext = new ViewFlowContext();
        flowContext.register("Stage", primaryStage);
        flow.createHandler(flowContext).start(container);

        Scene scene = new Scene(new JFXDecorator(primaryStage, container.getView()), 590, 500);
        scene.getStylesheets().add(Main.class.getResource("/resources/css/teacherToolBox-main.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

        Stage st = (Stage) addAssignment.getScene().getWindow();
        st.hide();
    }

}
