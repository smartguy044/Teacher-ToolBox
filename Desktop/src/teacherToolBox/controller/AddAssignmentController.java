package teacherToolBox.controller;

import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.util.VetoException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import javax.annotation.PostConstruct;

@FXMLController("../fxml/AddAssignmentPrompt.fxml")
public class AddAssignmentController
{
    @FXML
    private TextField yearTF;

    @PostConstruct
    public void init()
    {

    }
}
