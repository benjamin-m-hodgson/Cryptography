import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/** 
 * Use the driver JavaFX program to start the IDEA application
 *
 * @author Benjamin Hodgson
 * @date 7/23/18
 *
 * 
 */
public class IDEA_gui extends Application {      
    // initialize the scene object to build and return application display
    private final IDEA_scenes scenes = new IDEA_scenes(this);    
    // initialize the logic class
    private final IDEA_logic logic = new IDEA_logic();
    private final String styling = 
    	    IDEA_gui.class.getClassLoader().getResource("default.css").toExternalForm();
    private final String programTitle = "IDEA";
    private Stage appStage;
    private Scene pastScene;

    /**
     * Initialize the program and begin the animation loop 
     * 
     * @param stage: Primary stage to attach all scenes
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
    	appStage = primaryStage;
    	appStage.setTitle(programTitle);
    	initialize();
		appStage.show();
    }

    public void initialize() {
    	Scene initialView = scenes.initialScene();
    	initialView.getStylesheets().add(styling);
    	appStage.setScene(initialView);
    }
    
    public void displayPastScene() {
    	if (pastScene != null) {
    		appStage.setScene(pastScene);
    	}
    }
    
    public void encrypt() {
    	logic.setEncrypt();
    }
    
    public void decrypt() {
    	logic.setDecrypt();
    }
    
    public void keyBin() {
    	logic.setKeyBin();
    }
    
    public void keyHex() {
    	logic.setKeyHex();
    }
    
    public void keyTxt() {
    	logic.setKeyTxt();
    }
    
    public void dataBin() {
    	logic.setDataBin();
    }
    
    public void dataHex() {
    	logic.setDataHex();
    }
    
    public void dataTxt() {
    	logic.setDataTxt();
    }
    
    public void process(TextArea keyArea, TextArea dataArea) {
    	String inputKey = keyArea.getText().trim();
    	String inputData = dataArea.getText().trim();
    	if (!logic.processKey(inputKey)) {
    		// TODO handle displaying key input error
    		System.out.printf("Error processing key input %n");
    	}
    	if (!logic.processData(inputData)) {
    		// TODO handle displaying key input error
    		System.out.printf("Error processing data input %n");
    	}
    	displayResult(logic.execute());
    }
    
    private void displayResult(String output) {
    	System.out.printf("Program out: %s%n", output);
    	
    	// save the pastScene to go back to later
    	pastScene = appStage.getScene();
    	
    	Scene resultView = scenes.resultScene(output);
    	resultView.getStylesheets().add(styling);
    	appStage.setScene(resultView);
    }
    
    /**
     * Start the program
     */
    public static void main (String[] args) {
    	launch(args);
    }
}