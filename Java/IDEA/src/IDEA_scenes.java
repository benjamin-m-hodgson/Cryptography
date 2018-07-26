import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/** 
 * Used to create and return Scene objects to be loaded and displayed on the program stage 
 * to interface with the application.
 *
 * @author Benjamin Hodgson
 * @date 7/23/18
 *
 * 
 */
public class IDEA_scenes {
	private final int DEFAULT_HEIGHT = 600;
    private final int DEFAULT_WIDTH = 320;
    private final IDEA_gui app;
    private TextArea keyArea;
    private TextArea dataArea;
    private Button submit;
    private boolean inputOp;
    private boolean inputKey;
    private boolean inputData;
    
    public IDEA_scenes(IDEA_gui appDriver) {
    	app = appDriver;
    	keyArea = new TextArea();
    	dataArea = new TextArea();
    	submit = new Button();
    }
	
    /**
     * 
     * @return VBox container housing the application interface
     */
	public Scene initialScene() {
		VBox container = makeHeader("International Data Encryption Algorithm (IDEA)");
		HBox btnContainer = new HBox();
		btnContainer.setId("btn-container");
		
		// handle setting up toggle button functionality
		ToggleGroup btnToggleGroup = new ToggleGroup();
		ToggleButton encryptButton = new ToggleButton("Encrypt data");
		encryptButton.setToggleGroup(btnToggleGroup);
		ToggleButton decryptButton = new ToggleButton("Decrypt data");
		decryptButton.setToggleGroup(btnToggleGroup);
		btnToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
		    public void changed(ObservableValue<? extends Toggle> ov, 
		    		Toggle old_toggle, Toggle new_toggle) {
		    		if (new_toggle == encryptButton) {
		    			app.encrypt();
		    			System.out.printf("Logged encrypt %n");
		    			inputOp = true;
		    		}
		    		else if (new_toggle == decryptButton) {
		    			app.decrypt();
		    			System.out.printf("Logged decrypt %n");
		    			inputOp = true;
		    		}
		    		else {
		    			// TODO prevent clicking of submit button and possibly disable input areas
		    			System.out.printf("No action toggled %n");
		    			inputOp = false;
		    		}
		    		updateButtons();
		    	}
		    });
		btnToggleGroup.selectToggle(encryptButton);
		btnContainer.getChildren().addAll(encryptButton, decryptButton);
		
		// create submit button to execute algorithm
		submit = new Button("Submit");
		submit.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
            	System.out.printf("Submit click registered %n");
            	app.process(keyArea, dataArea);
            }
        });
		container.getChildren().addAll(btnContainer, inputKey(), inputData(), submit);
		return new Scene(container, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	public Scene resultScene(String out) {
		VBox container = makeHeader("International Data Encryption Algorithm (IDEA)");
		Label descLabel = new Label("Program result");
		TextArea resultArea = new TextArea(out);
		resultArea.setEditable(false);
		HBox btnContainer = new HBox();
		btnContainer.setId("btn-container");
		Button backBtn = new Button("Back");
		backBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
            	System.out.printf("Back click registered %n");
            	app.displayPastScene();
            }
        });
		Button copyBtn = new Button("Copy result");
		copyBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
            	final Clipboard clipboard = Clipboard.getSystemClipboard();
                final ClipboardContent content = new ClipboardContent();
                content.putString(out);
                clipboard.setContent(content);
            }
        });
		btnContainer.getChildren().addAll(backBtn, copyBtn);
		container.getChildren().addAll(descLabel, resultArea, btnContainer);
		return new Scene(container, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	/**
	 * 
	 * @return VBox container that houses the toggle buttons and input area necessary for key information
	 */
	private VBox inputKey() {
		VBox container = makeTitle("Key Input");
		//Label descLabel = new Label("Select the key format:");
		HBox btnContainer = new HBox();
		btnContainer.setId("btn-container");
		ToggleGroup btnToggleGroup = new ToggleGroup();
		ToggleButton binBtn = new ToggleButton("Binary");
		binBtn.setToggleGroup(btnToggleGroup);
		ToggleButton hexBtn = new ToggleButton("Hex");
		hexBtn.setToggleGroup(btnToggleGroup);
		ToggleButton txtBtn = new ToggleButton("Text");
		txtBtn.setToggleGroup(btnToggleGroup);
		keyArea = new TextArea();
		keyArea.setPromptText("Enter the desired key.");
		btnToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
		    public void changed(ObservableValue<? extends Toggle> ov, 
		    		Toggle old_toggle, Toggle new_toggle) {
		    		if (new_toggle == binBtn) {
		    			app.keyBin();
		    			System.out.printf("Logged binary %n");
		    			inputKey = true;
		    		}
		    		else if (new_toggle == hexBtn) {
		    			app.keyHex();
		    			System.out.printf("Logged hex %n");
		    			inputKey = true;
		    		}
		    		else if (new_toggle == txtBtn) {
		    			app.keyTxt();
		    			System.out.printf("Logged txt %n");
		    			inputKey = true;
		    		}
		    		else {
		    			// TODO prevent clicking of submit button and disable input area
		    			System.out.printf("No buttons toggled %n");
		    			inputKey = false;
		    		}
		    		updateButtons();
		    	}
		    });
		btnToggleGroup.selectToggle(txtBtn);
		//btnContainer.getChildren().addAll(descLabel, binBtn, hexBtn, txtBtn);
		btnContainer.getChildren().addAll(binBtn, hexBtn, txtBtn);
		container.getChildren().addAll(btnContainer, keyArea);
		return container;	
	}
	
	/**
	 * 
	 * @return VBox container that houses the toggle buttons and input area necessary for data information
	 */
	private VBox inputData() {
		VBox container = makeTitle("Data Input");
		//Label descLabel = new Label("Select the data format:");
		HBox btnContainer = new HBox();
		btnContainer.setId("btn-container");
		ToggleGroup btnToggleGroup = new ToggleGroup();
		ToggleButton binBtn = new ToggleButton("Binary");
		binBtn.setToggleGroup(btnToggleGroup);
		ToggleButton hexBtn = new ToggleButton("Hex");
		hexBtn.setToggleGroup(btnToggleGroup);
		ToggleButton txtBtn = new ToggleButton("Text");
		txtBtn.setToggleGroup(btnToggleGroup);
		dataArea = new TextArea();
		dataArea.setPromptText("Enter the desired data.");
		btnToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
		    public void changed(ObservableValue<? extends Toggle> ov, 
		    		Toggle old_toggle, Toggle new_toggle) {
		    		if (new_toggle == binBtn) {
		    			app.dataBin();
		    			System.out.printf("Logged binary %n");
		    			inputData = true;
		    		}
		    		else if (new_toggle == hexBtn) {
		    			app.dataHex();
		    			System.out.printf("Logged hex %n");
		    			inputData = true;
		    		}
		    		else if (new_toggle == txtBtn) {
		    			app.dataTxt();
		    			System.out.printf("Logged txt %n");
		    			inputData = true;
		    		}
		    		else {
		    			// TODO prevent clicking of submit button and disable input area
		    			System.out.printf("No buttons toggled %n");
		    			inputData = false;
		    		}
		    		updateButtons();
		    	}
		    });
		btnToggleGroup.selectToggle(txtBtn);
		//btnContainer.getChildren().addAll(descLabel, binBtn, hexBtn, txtBtn);
		btnContainer.getChildren().addAll(binBtn, hexBtn, txtBtn);
		container.getChildren().addAll(btnContainer, dataArea);
		return container;	
	}
	
	/**
	 * @param heading the page heading text to be displayed
	 * 
	 * @return VBox container to hold other page elements and page title label
	 */
	private VBox makeHeader(String heading) {
		VBox container = new VBox();
		container.setId("header-container");
		Label titleLabel = new Label(heading);
		container.getChildren().add(titleLabel);
		return container;
	}
	
	/**
	 * @param title the page section title to be displayed
	 * 
	 * @return VBox container to hold a section of page content with a title
	 */
	private VBox makeTitle(String title) {
		VBox container = new VBox();
		container.setId("title-container");
		Label titleLabel = new Label(title);
		container.getChildren().add(titleLabel);
		return container;
	}
	
	/**
	 * Enables/disables input areas and buttons from toggle button input
	 */
	private void updateButtons() {
		// disable key input area if no key format is selected
		if (inputKey) {
			keyArea.setDisable(false);
		}
		else {
			keyArea.setDisable(true);
		}
		// disable data input area if no data format is selected
		if (inputData) {
			dataArea.setDisable(false);
		}
		else {
			dataArea.setDisable(true);
		}
		// disable submit button if any formats not specified
		if (!inputOp || !inputKey || !inputData) {
			submit.setDisable(true);
		}
		else {
			submit.setDisable(false);
		}
	}
	
}
