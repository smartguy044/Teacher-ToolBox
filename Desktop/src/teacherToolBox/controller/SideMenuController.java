package teacherToolBox.controller;

import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.controls.JFXListView;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
import io.datafx.controller.flow.container.DefaultFlowContainer;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.controller.util.VetoException;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import teacherToolBox.Main;

import javax.annotation.PostConstruct;

@FXMLController("../fxml/SideMenu.fxml")

public class SideMenuController
{
	@FXMLViewFlowContext
	private ViewFlowContext context;

	@FXML
	@ActionTrigger("addRoster")
	private Label addRoster;

	@FXML
	@ActionTrigger("attendance")
	private Label attendance;

	@FXML
	@ActionTrigger("rosterMenu")
	private Label rosterMenu;

	@FXML
	private JFXListView<?> sideList;

	@PostConstruct
	public void init() throws FlowException, VetoException
	{
		//sideList.propagateMouseEventsToParent();
		FlowHandler contentFlowHandler = (FlowHandler) context.getRegisteredObject("ContentFlowHandler");
		Flow contentFlow = (Flow) context.getRegisteredObject("ContentFlow");
		bindNodeToController(addRoster, AddRosterController.class, contentFlow, contentFlowHandler);
		bindNodeToController(attendance, AttendanceController.class, contentFlow, contentFlowHandler);
		bindNodeToController(rosterMenu, RosterMenuController.class, contentFlow, contentFlowHandler);
	}

	private void bindNodeToController(Node node, Class<?> controllerClass, Flow flow, FlowHandler flowHandler)
	{
		flow.withGlobalLink(node.getId(), controllerClass);

		node.setOnMouseClicked((e) ->
		{
			try
			{
				flowHandler.handle(node.getId());				
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
			}
		});
	}
}
