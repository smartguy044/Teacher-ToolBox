package teacherToolBox.maincontroller;

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
import teacherToolBox.controller.SignUpController;
import javax.annotation.PostConstruct;

/*
 * The MainSignUpController class is responsible for handling the graphical theme for its related controller.
 *
 * <p/> Bugs: None
 *
 * @author  Michael Stevens, Josh Torrans, Matthew Fondevilla, Joanna Ho, Tom Warren, and Greg Grimsley
 */

@FXMLController("../fxml/Main.fxml")
public class MainSignUpController
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
        toolbarPopup.setPopupContainer(root);
        toolbarPopup.setSource(optionsRippler);

        optionsBurger.setOnMouseClicked((e) -> toolbarPopup.show(JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT, -12, 15));

        exit.setOnMouseClicked((e) -> Platform.exit());

        context = new ViewFlowContext();

        Flow innerFlow = new Flow(SignUpController.class);

        flowHandler = innerFlow.createHandler(context);
        context.register("ContentFlowHandler", flowHandler);
        context.register("ContentFlow", innerFlow);
        context.register("ContentPane", content);
        content.getChildren().add(flowHandler.start(new AnimatedFlowContainer(Duration.millis(320), ContainerAnimations.SWIPE_LEFT)));
    }
}