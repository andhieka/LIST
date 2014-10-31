package list;

import java.io.IOException;
import java.util.List;
import java.util.Stack;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import list.ICommand.CommandExecutionException;
import list.IParser.ParseException;
import list.view.MainController;

import org.json.JSONException;

public class Controller extends Application {
	private static final String APPLICATION_NAME = "LIST";	
	private Stage primaryStage;
	private StackPane mainLayout;
	
    private static final String TITLE_FLOATING_TASKS = "Floating Tasks";
    private static final String TITLE_TODAY_TASKS = "Today's Tasks";
    private static final String TITLE_ALL_TASKS = "All Tasks";

    public static enum DisplayMode {
        TODAY, FLOATING, ALL, CUSTOM
    }
    
	private static final String MESSAGE_UNKNOWN_ERROR = "Unknown error!";
    private static final String MESSAGE_ERROR_PARSING_COMMAND = "Error parsing command.";
    private static final String MESSAGE_ERROR_LOADING = "Error loading data";
	private static final String MESSAGE_ERROR_INVALID_JSON_FORMAT = "Data is not in a valid JSON format ." + 
																	"Please ensure the JSON format is " + 
																	"valid and relaunch the program.";
	private static final String MESSAGE_ERROR_SAVING = "Error saving data";
	
	private static final DisplayMode DEFAULT_DISPLAY_MODE = DisplayMode.ALL;
    
	private static IUserInterface userInterface = null;
	private static IParser parser = new FlexiCommandParser();
	private static TaskManager taskManager = TaskManager.getInstance();
	private static DisplayMode displayMode = DEFAULT_DISPLAY_MODE;
	private static List<ITask> displayedTasks = null;
	private static ITask displayedTaskDetail = null;
	
	private static Stack<ICommand> undoStack = new Stack<ICommand>();
	private static Stack<ICommand> redoStack = new Stack<ICommand>();
	private static boolean isUndoRedoOperation = false;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle(APPLICATION_NAME);
	
		initializeMainLayout();
		
		loadInitialData();
		
		refreshUi();
		
	}
		
	@Override
	public void stop() {
		System.out.println("Exiting...");
		return;
	}
	
	private void initializeMainLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApplication.class.getResource("view/asdf.fxml"));
			
            mainLayout = (StackPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(mainLayout);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
		} 
	}
	
	public static String processUserInput(String userInput) {
	    String reply;
        try {
            isUndoRedoOperation = false;
            ICommand commandMadeByParser = parser.parse(userInput);
            reply = commandMadeByParser.execute();
            ICommand inverseCommand = commandMadeByParser.getInverseCommand();
            if (inverseCommand != null) {
                undoStack.add(inverseCommand);
            }
            if (!isUndoRedoOperation) {
                redoStack.clear();
            }
        } catch (ParseException e) {
            reply = MESSAGE_ERROR_PARSING_COMMAND;
        } catch (CommandExecutionException e) {
            reply = e.getMessage();
        } catch (IOException e) {
        	reply = MESSAGE_ERROR_SAVING;
        } catch (Exception e) {
            reply = MESSAGE_UNKNOWN_ERROR;
            e.printStackTrace();
        }
		return reply;
	}

	public static ITask getTaskWithNumber(int taskNumber) {
	    int taskId = taskNumber - 1;
		return displayedTasks.get(taskId);
	}
	
	public static boolean hasTaskWithNumber(int taskNumber) {
		return (taskNumber > 0 && taskNumber <= displayedTasks.size());		
	}
	
	public static ITask getLastDisplayedTaskDetail() {
	    return displayedTaskDetail;
	}
	
	//UI FUNCTIONS
    public static void setDisplayMode(DisplayMode displayMode) {
        Controller.displayMode = displayMode; 
    }
    
	public static void displayTaskDetail(ITask selectedTask) {
	    Controller.displayedTaskDetail = selectedTask;
		userInterface.displayTaskDetail(selectedTask);
	}
	
	public static void refreshUi() {
		List<ITask> tasksToDisplay = null;
		if (userInterface == null) {
		    userInterface = MainController.getInstance();
		}
	    switch (Controller.displayMode) {
		    case ALL:
		    	tasksToDisplay = taskManager.getAllTasks();
		        userInterface.display(TITLE_ALL_TASKS, tasksToDisplay);
		        break;
		    case TODAY:
		    	tasksToDisplay = taskManager.getTodayTasks();
		        userInterface.display(TITLE_TODAY_TASKS, tasksToDisplay);
		        break;
		    case FLOATING:
		    	tasksToDisplay = taskManager.getFloatingTasks();
		        userInterface.display(TITLE_FLOATING_TASKS, tasksToDisplay);
		        break;
		    case CUSTOM:
		        //do something, or possibly do nothing
	    }
	    Controller.displayedTasks = tasksToDisplay;
	}

	public static void updateUiWithTaskDetail(ITask selectedTask) {
		userInterface.displayTaskDetail(selectedTask);
	}
	
	static Stack<ICommand> getUndoStack() {
	    return undoStack;
	}
	
	static Stack<ICommand> getRedoStack() {
	    return redoStack;
	}
	
	static void reportUndoRedoOperation() {
	    isUndoRedoOperation = true;
	}
	
	//TODO: Error with UI when loading
	public static void loadInitialData() {
		try {
			taskManager.loadTasks();
		} catch (IOException e) {
			//userInterface.displayMessageToUser(MESSAGE_ERROR_LOADING);
		} catch (JSONException e) {
			//userInterface.displayMessageToUser(MESSAGE_ERROR_INVALID_JSON_FORMAT);
			System.exit(1);
		}
	}

}
