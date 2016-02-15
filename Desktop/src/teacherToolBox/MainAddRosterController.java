package teacherToolBox;

import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXRippler;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.container.AnimatedFlowContainer;
import io.datafx.controller.flow.container.ContainerAnimations;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.controller.util.VetoException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import javax.annotation.PostConstruct;

@FXMLController("Main2.fxml")

public class MainAddRosterController
{
    @FXMLViewFlowContext
    private ViewFlowContext context;

    @FXML
    private StackPane root;

    @FXML
    private StackPane content;

    @FXML
    private StackPane optionsBurger;

    @FXML
    private JFXRippler optionsRippler;

    @FXML
    private JFXPopup toolbarPopup;

    @FXML
    private Label exit;

    private FlowHandler flowHandler;

    @PostConstruct
    public void init() throws FlowException, VetoException
    {
        // init Popup
        toolbarPopup.setPopupContainer(root);
        toolbarPopup.setSource(optionsRippler);

        optionsBurger.setOnMouseClicked((e) -> toolbarPopup.show(JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT, -12, 15));

        // close application
        exit.setOnMouseClicked((e) -> Platform.exit());

        // create the inner flow and content
        context = new ViewFlowContext();

        // set the default controller
        Flow innerFlow = new Flow(AddRosterController.class);

        flowHandler = innerFlow.createHandler(context);
        context.register("ContentFlowHandler", flowHandler);
        context.register("ContentFlow", innerFlow);
        context.register("ContentPane", content);
        content.getChildren().add(flowHandler.start(new AnimatedFlowContainer(Duration.millis(320), ContainerAnimations.SWIPE_LEFT)));
    }
}
