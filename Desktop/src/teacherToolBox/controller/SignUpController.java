package teacherToolBox.controller;

import com.jfoenix.controls.*;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
import io.datafx.controller.flow.container.DefaultFlowContainer;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import teacherToolBox.Main;
import teacherToolBox.maincontroller.MainController;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Properties;

/*
 * The SignUpController class is the class that handles the first process of the first time user of TeacherToolbox.
 * The user needs to fill up the sign up window with the following information:
 * >Full Name
 * >Email
 * >Password
 * >Security Question and Answer
 * Note that there are special restrictions in filling up the sign up windows, such as special character requirements, and email format requirements.
 *
 * <p/> Bugs: None
 *
 * @author  Michael Stevens, Josh Torrans, Matthew Fondevilla, Joanna Ho, Tom Warren, and Greg Grimsley
 */

@FXMLController("../fxml/SignUp.fxml")
public class SignUpController
{
    @FXMLViewFlowContext
    private ViewFlowContext flowContext;

    @FXML private JFXTextField nameTF;
    @FXML private JFXTextField emailTF;
    @FXML private JFXPasswordField passwordTF;
    @FXML private JFXPasswordField confirmPassTF;
    @FXML private JFXTextField secAnsTF;
    @FXML private JFXComboBox<String> secCB;
    @FXML private JFXDialog emailDialog;
    @FXML private JFXButton acceptButton;
    @FXML private JFXDialog passwordDialog;
    @FXML private JFXButton acceptButtonPW;
    @FXML private JFXDialog passwordDialog2;
    @FXML private JFXButton acceptButtonPW2;
    @FXML private JFXDialog signUpDialog;
    @FXML private JFXButton acceptButtonSU;
    @FXML private Tooltip toolTip;

    @FXML
    @ActionTrigger("signUpAction")
    private Button signUpButton;

    @FXML
    @ActionTrigger("backAction")
    private Button backButton;

    @ActionMethod("signUpAction")
    public void signUpButton_onAction() throws Exception
    {
        if(!emailTF.getText().matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"))
        {
            emailDialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
            emailDialog.show((Pane) flowContext.getRegisteredObject("ContentPane"));
        }
        else if(!passwordTF.getText().equals(confirmPassTF.getText()))
        {
            passwordDialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
            passwordDialog.show((Pane) flowContext.getRegisteredObject("ContentPane"));
        }
        else if(!passwordTF.getText().matches("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%]).{8,20})"))
        {
            passwordDialog2.setTransitionType(JFXDialog.DialogTransition.CENTER);
            passwordDialog2.show((Pane) flowContext.getRegisteredObject("ContentPane"));
        }
        else
        {
            Properties properties = new Properties();
            Connection connection = null;
            Statement statement = null;

            String fName = nameTF.getText().substring(0, nameTF.getText().indexOf(' '));
            String lName = nameTF.getText().substring(nameTF.getText().indexOf(' ') + 1, nameTF.getText().length());
            String generatedPassword = "";

            boolean match = false;

            try {
                properties.load(new FileInputStream(".//src//database.properties"));
                String url = properties.getProperty("jdbc.url");
                Class.forName(properties.getProperty("jdbc.driver"));
                connection = DriverManager.getConnection(url, properties.getProperty("jdbc.username"), properties.getProperty("jdbc.password"));

                statement = connection.createStatement();

                ResultSet resultSet = statement.executeQuery("select uname from users");

                while (resultSet.next())
                {
                    match = resultSet.getString(1).equals(emailTF.getText());
                }

                if(match)
                {
                    signUpDialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
                    signUpDialog.show((Pane) flowContext.getRegisteredObject("ContentPane"));
                }
                else
                {
                    try
                    {
                        // Create MessageDigest instance for SHA-512
                        MessageDigest md = MessageDigest.getInstance("SHA-512");
                        //Add password bytes to digest
                        md.update(passwordTF.getText().getBytes());
                        //Get the hash's bytes
                        byte[] bytes = md.digest();
                        //This bytes[] has bytes in decimal format;
                        //Convert it to hexadecimal format
                        StringBuilder sb = new StringBuilder();
                        for (byte aByte : bytes)
                        {
                            sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
                        }
                        //Get complete hashed password in hex format
                        generatedPassword = sb.toString();

                        statement.executeUpdate("INSERT INTO users(fname, lname, uname, pword, secques, secans, logins) values('"
                                + fName + "', '" + lName + "', '" + emailTF.getText() + "', '" + generatedPassword + "', '" + secCB.getValue()
                                + "', '" + secAnsTF.getText() + "', " + 0 + ")");
                    }
                    catch (NoSuchAlgorithmException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            catch (IOException ioException)
            {
                String msg = ioException.getMessage();
                System.err.printf("problem with properties file: %s\n", msg);
            }
            catch (SQLException sqlException)
            {
                String msg = sqlException.getMessage();
                System.err.printf("problem with db connection: %s\n", msg);
            }
            catch (ClassNotFoundException e)
            {
                String msg = e.getMessage();
                System.err.printf("problem with driver: %s\n", msg);
            }
        }

        Stage primaryStage = new Stage();
        Flow flow = new Flow(MainController.class);
        DefaultFlowContainer container = new DefaultFlowContainer();
        flowContext = new ViewFlowContext();
        flowContext.register("Stage", primaryStage);
        flow.createHandler(flowContext).start(container);

        Scene scene = new Scene(new JFXDecorator(primaryStage, container.getView()), 375, 550);
        scene.getStylesheets().add(Main.class.getResource("/resources/css/teacherToolBox-main.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

        Stage st = (Stage) backButton.getScene().getWindow();
        st.hide();
    }

    @ActionMethod("backAction")
    public void backButton_onAction() throws Exception
    {
        Stage primaryStage = new Stage();
        Flow flow = new Flow(MainController.class);
        DefaultFlowContainer container = new DefaultFlowContainer();
        flowContext = new ViewFlowContext();
        flowContext.register("Stage", primaryStage);
        flow.createHandler(flowContext).start(container);

        Scene scene = new Scene(new JFXDecorator(primaryStage, container.getView()), 375, 550);
        scene.getStylesheets().add(Main.class.getResource("/resources/css/teacherToolBox-main.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

        Stage st = (Stage) backButton.getScene().getWindow();
        st.hide();
    }

    @PostConstruct
    public void init()
    {
        nameTF.focusedProperty().addListener((o, oldVal, newVal) ->
        {
            if (!newVal)
            {
                nameTF.validate();
            }

            nameTF.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>()
            {
                @Override
                public void handle(KeyEvent arg0)
                {
                    updateButton();
                }
            });
        });

        emailTF.focusedProperty().addListener((o, oldVal, newVal) ->
        {
            if (!newVal)
            {
                emailTF.validate();
            }

            emailTF.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>()
            {
                @Override
                public void handle(KeyEvent arg0)
                {
                    updateButton();
                }
            });
        });

        passwordTF.focusedProperty().addListener((o, oldVal, newVal) ->
        {
            if (!newVal)
            {
                passwordTF.validate();
            }

            passwordTF.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>()
            {
                @Override
                public void handle(KeyEvent arg0)
                {
                    updateButton();
                }
            });
        });

        confirmPassTF.focusedProperty().addListener((o, oldVal, newVal) ->
        {
            if (!newVal || !passwordTF.getText().equals(confirmPassTF.getText()))
            {
                confirmPassTF.validate();
            }

            confirmPassTF.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>()
            {
                @Override
                public void handle(KeyEvent arg0)
                {
                    updateButton();
                }
            });
        });

        secCB.focusedProperty().addListener((o, oldVal, newVal) ->
        {
            secCB.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent arg0)
                {
                    updateButton();
                }
            });
        });

        secAnsTF.focusedProperty().addListener((o, oldVal, newVal) ->
        {
            if (!newVal)
            {
                secAnsTF.validate();
            }

            secAnsTF.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>()
            {
                @Override
                public void handle(KeyEvent arg0)
                {
                    updateButton();
                }
            });
        });

        acceptButton.setOnMouseClicked((e)->{
            emailDialog.close();
        });

        acceptButtonPW.setOnMouseClicked((e)->{
            passwordDialog.close();
        });

        acceptButtonPW2.setOnMouseClicked((e)->{
            passwordDialog2.close();
        });

        acceptButtonSU.setOnMouseClicked((e)->{
            signUpDialog.close();
        });
    }

    public void updateButton()
    {
        if(!nameTF.getText().equals("") && !emailTF.getText().equals("") && !passwordTF.getText().equals("") && !confirmPassTF.getText().equals("")
                && !secAnsTF.getText().equals("") && !secCB.getValue().equals("Select Security Question..."))
        {
            signUpButton.setDisable(false);
        }
        else
        {
            signUpButton.setDisable(true);
        }
    }
}
