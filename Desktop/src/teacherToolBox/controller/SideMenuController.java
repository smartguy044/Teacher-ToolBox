package teacherToolBox.controller;

import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.controls.JFXListView;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
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
import teacherToolBox.maincontroller.*;

import javax.annotation.PostConstruct;

/*
 * The SideMenuController class is the main menu of the TeacherToolbox
 * This menu can be access in the upper left of the user interface of TeacherToolBox.
 * This menu includes: Add Roster, Roster Menu, Attendance, Grades, and Reports functionality.
 *
 * <p/> Bugs: None
 *
 * @author  Michael Stevens, Josh Torrans, Matthew Fondevilla, Joanna Ho, Tom Warren, and Greg Grimsley
 */

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
	@ActionTrigger("addStudent")
	private Label addStudent;

	@FXML
	@ActionTrigger("editDeleteStudent")
	private Label editDeleteStudent;

	@FXML
	@ActionTrigger("grades")
	private Label grades;

	@FXML
	@ActionTrigger("reports")
	private Label reports;

	@FXML
	private JFXListView<?> sideList;

	@PostConstruct
	public void init() throws FlowException, VetoException, Exception
	{
		sideList.propagateMouseEventsToParent();
		FlowHandler contentFlowHandler = (FlowHandler) context.getRegisteredObject("ContentFlowHandler");
		Flow contentFlow = (Flow) context.getRegisteredObject("ContentFlow");
		bindNodeToController(addRoster, MainAddRosterController.class, contentFlow, contentFlowHandler);
		bindNodeToController(addStudent, MainAddStudentController.class, contentFlow, contentFlowHandler);
		bindNodeToController(editDeleteStudent, MainEditDeleteStudentController.class, contentFlow, contentFlowHandler);
		bindNodeToController(attendance, MainAttendanceController.class, contentFlow, contentFlowHandler);
		bindNodeToController(grades, MainGradesController.class, contentFlow, contentFlowHandler);
		bindNodeToController(reports, MainReportsController.class, contentFlow, contentFlowHandler);
	}

	private void bindNodeToController(Node node, Class<?> controllerClass, Flow flow, FlowHandler flowHandler) throws Exception
	{

		flow.withGlobalLink(node.getId(), controllerClass);

		node.setOnMouseClicked((e) ->
		{
			try
			{
				int height = 0, width = 0;

				Stage st = (Stage) node.getScene().getWindow();

				Stage primaryStage = new Stage();
				Flow flow1 = new Flow(controllerClass);
				DefaultFlowContainer container = new DefaultFlowContainer();
				context = new ViewFlowContext();
				context.register("Stage", primaryStage);
				flow1.createHandler(context).start(container);

				switch(node.toString().substring(node.toString().indexOf('\'') + 1, node.toString().lastIndexOf('\'')))
				{
					case "Add Roster" :
						height = 1000;
						width = 750;
						break;
					case "Add Student" :
						height = 900;
						width = 762;
						break;
					case "Edit/Delete Student" :
						height = 850;
						width = 650;
						break;
					case "Attendance" :
						height = 660;
						width = 650;
						break;
					case "Grades" :
						height = 864;
						width = 1120;
						break;
					case "Reports" :
						height = 1012;
						width = 665;
						break;
					default :
						height = 1000;
						width = 750;
				}

				Scene scene = new Scene(new JFXDecorator(primaryStage, container.getView()), width, height);
				scene.getStylesheets().add(Main.class.getResource("/resources/css/teacherToolBox-main.css").toExternalForm());
				primaryStage.setScene(scene);
				primaryStage.setFullScreen(st.isFullScreen());
				primaryStage.setMaximized(st.isMaximized());
				primaryStage.show();

				st.close();

			}
			catch (Exception e1)
			{
				e1.printStackTrace();
			}
		});
	}
}
