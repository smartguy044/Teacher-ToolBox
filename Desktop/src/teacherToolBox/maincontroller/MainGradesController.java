package teacherToolBox.maincontroller;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
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
import javax.annotation.PostConstruct;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import teacherToolBox.controller.GradesController;
import teacherToolBox.controller.SideMenuController;

/*
 * The MainGradesController class is responsible for handling the graphical theme for its related controller.
 *
 * <p/> Bugs: None
 *
 * @author  Michael Stevens, Josh Torrans, Matthew Fondevilla, Joanna Ho, Tom Warren, and Greg Grimsley
 */

@FXMLController("../fxml/Main2.fxml")
public class MainGradesController
{
    @FXMLViewFlowContext
    private ViewFlowContext context;

    @FXML
    private StackPane root;
    @FXML
    private StackPane content;
    @FXML
    private StackPane sideContent;

    @FXML
    private StackPane titleBurgerContainer;

    @FXML
    private JFXHamburger titleBurger;

    @FXML
    private StackPane optionsBurger;
    @FXML
    private JFXRippler optionsRippler;

    @FXML
    private JFXDrawer drawer;

    @FXML
    private JFXPopup toolbarPopup;

    @FXML
    private Label exit;

    private FlowHandler flowHandler;
    private FlowHandler sideMenuFlowHandler;

    private int counter = 0;

    @PostConstruct
    public void init() throws FlowException, VetoException
    {
        drawer.setOnDrawingAction((e) -> {
            titleBurger.getAnimation().setRate(1);
            titleBurger.getAnimation().setOnFinished((event) -> counter = 1);
            titleBurger.getAnimation().play();
        });
        drawer.setOnHidingAction((e) -> {
            titleBurger.getAnimation().setRate(-1);
            titleBurger.getAnimation().setOnFinished((event) -> counter = 0);
            titleBurger.getAnimation().play();
        });
        titleBurgerContainer.setOnMouseClicked((e) -> {
            if (counter == 0)
            {
                drawer.draw();
            }
            else if (counter == 1)
            {
                drawer.hide();
            }
            counter = -1;
        });

        toolbarPopup.setPopupContainer(root);
        toolbarPopup.setSource(optionsRippler);

        optionsBurger.setOnMouseClicked((e) -> toolbarPopup.show(JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT, -12, 15));

        exit.setOnMouseClicked((e) -> Platform.exit());

        context = new ViewFlowContext();

        Flow innerFlow = new Flow(GradesController.class);

        flowHandler = innerFlow.createHandler(context);
        context.register("ContentFlowHandler", flowHandler);
        context.register("ContentFlow", innerFlow);
        context.register("ContentPane", content);
        content.getChildren().add(flowHandler.start(new AnimatedFlowContainer(Duration.millis(320), ContainerAnimations.SWIPE_LEFT)));

        Flow sideMenuFlow = new Flow(SideMenuController.class);
        sideMenuFlowHandler = sideMenuFlow.createHandler(context);
        sideContent.getChildren().add(sideMenuFlowHandler.start(new AnimatedFlowContainer(Duration.millis(320), ContainerAnimations.SWIPE_LEFT)));
    }
}
