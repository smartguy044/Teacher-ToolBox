package teacherToolBox.maincontroller;

import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.container.AnimatedFlowContainer;
import io.datafx.controller.flow.container.ContainerAnimations;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.controller.util.VetoException;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import teacherToolBox.controller.AddAssignmentController;
import javax.annotation.PostConstruct;

/*
 * The MainAssignmentController class is responsible for handling the graphical theme for its related controller.
 *
 * <p/> Bugs: None
 *
 * @author  Michael Stevens, Josh Torrans, Matthew Fondevilla, Joanna Ho, Tom Warren, and Greg Grimsley
 */

@FXMLController("../fxml/Main3.fxml")
public class MainAddAssignmentController
{
    @FXMLViewFlowContext
    private ViewFlowContext context;

    @FXML
    private StackPane content;

    @PostConstruct
    public void init() throws FlowException, VetoException
    {
        context = new ViewFlowContext();

        Flow innerFlow = new Flow(AddAssignmentController.class);

        FlowHandler flowHandler = innerFlow.createHandler(context);
        context.register("ContentFlowHandler", flowHandler);
        context.register("ContentFlow", innerFlow);
        context.register("ContentPane", content);
        content.getChildren().add(flowHandler.start(new AnimatedFlowContainer(Duration.millis(320), ContainerAnimations.SWIPE_LEFT)));
    }
}
