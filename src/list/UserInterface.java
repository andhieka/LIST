// import java swings from the library
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import java.awt.Container;
import java.awt.Font;

public class UserInterface {
	private static JFrame mainFrame = new JFrame("List");
	private final static int MAINFRAMEWIDTH = 700;
	private final static int MAINFRAMEHEIGHT = 700;
	private final static int NUMBEROFLINESALLOWED = 10;
	private static int CONSOLEWIDTH = MAINFRAMEWIDTH;
	private static int CONSOLEHEIGHT = MAINFRAMEHEIGHT * 2 / 7;
	private static int LISTWIDTH = MAINFRAMEWIDTH;
	private static int LISTHEIGHT = MAINFRAMEHEIGHT - CONSOLEHEIGHT;
	private static int LABELWIDTH = MAINFRAMEWIDTH;
	private static int LABELHEIGHT = LISTHEIGHT / NUMBEROFLINESALLOWED;
	private static Font fontForDate = new Font("Arial", Font.BOLD, 36);
	private static Font fontForTask = new Font("Arial", Font.PLAIN, 36);
	private static Date previousDate = new Date();
	private static boolean isFull = false;
	private static int numberOfLines = 0;

	public UserInterface(){

		// set the size of the frame
		mainFrame.setSize(MAINFRAMEWIDTH, MAINFRAMEHEIGHT);

		// set the window location to the center of the screen
		mainFrame.setLocationRelativeTo(null);

		// set that BorderLayout cannot be used in the window
		mainFrame.setLayout(null);

		// set that the application quit when the window closed
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// make the window visible to the user
		mainFrame.setVisible(true);
	}

	public boolean displayNewTaskOrDate(ITask task) {

		// return if the lines are full
		if (numberOfLines == NUMBEROFLINESALLOWED - 1) return isFull;

		// check whether the date is the same with the date of the previous task
		// if it's not the same, diplay the new date
		// if it's the same, display the task
		if (!checkDateIsAppeared(task)) {
			displayNewDate(task);
		} else {
			displayNewTask(task);
		}

		return isFull;
	}

	public void displayTaskDetail(ITask task) {

		//**********display the title and category**********//
		// display the title of the task
		displayNewLine("Title: " + task.getTitle() + " (" + task.getCategory() + ")", fontForDate);

		//**********display the place**********//
		// display the place of the task
		displayNewLine("Place: " + task.getPlace(), fontForTask);

		//**********display the start time**********//
		// string for the start time
		String stringForStartTime = "Start time: ";
		
		// get the start date to be displayed
		Date startDateToDisplay = task.getStartTime();

		// set the dates to the string to be displayed
		stringForStartTime += startDateToDisplay.getDay();
		stringForStartTime += "/";
		stringForStartTime += startDateToDisplay.getMonth();
		stringForStartTime += "/";
		stringForStartTime += startDateToDisplay.getYear();

		// display the start time of the task
		displayNewLine(stringForStartTime, fontForTask);

		//**********display the end time**********//
		// string for the end time
		String stringForEndTime = "End time: ";
		
		// get the end date to be displayed
		Date endDateToDisplay = task.getEndTime();

		// set the dates to the string to be displayed
		stringForEndTime += endDateToDisplay.getDay();
		stringForEndTime += "/";
		stringForEndTime += endDateToDisplay.getMonth();
		stringForEndTime += "/";
		stringForEndTime += endDateToDisplay.getYear();

		// display the end time of the task
		displayNewLine(stringForEndTime, fontForTask);

		//**********display the notes**********//
		// display the notes of the task
		displayNewLine("Notes: " + task.getNotes(), fontForTask);
	}

	public boolean checkDateIsAppeared(ITask task) {

		// return whether the date held was the same with the date of the task
		return previousDate.equals(task.getEndTime());
	}

	public void displayNewDate(ITask task) {

		// get the date to be displayed
		Date dateToDisplay = task.getEndTime();

		String stringOfDateToDisplay = "";

		// set the dates to the string to be displayed
		stringOfDateToDisplay += dateToDisplay.getDay();
		stringOfDateToDisplay += "/";
		stringOfDateToDisplay += dateToDisplay.getMonth();
		stringOfDateToDisplay += "/";
		stringOfDateToDisplay += dateToDisplay.getYear();

		// display the contents to the window
		displayNewLine(stringOfDateToDisplay, fontForDate);
	}

	public void displayNewTask(ITask task) {
		
		// get the title of the task to be displayed
		String stringOfTaskToDisplay = task.getTitle();

		// display the contents to the window
		displayNewLine(stringOfTaskToDisplay, fontForTask);
	}

	public static void displayNewLine(String stringToDisplay, Font fontForLabel) {

		// make the instance of the JPanel to set the label on that
		JPanel panelOfLine = new JPanel();

		// make new label that hold the string to be displayed
		JLabel labelOfString = new JLabel(stringToDisplay);

		// set the size of the label
		labelOfString.setBounds(0, numberOfLines * LABELHEIGHT, LABELWIDTH, LABELHEIGHT);

		// set the font of the Label
		labelOfString.setFont(fontForLabel);

		// add the label on the panel
		panelOfLine.add(labelOfString);

		// get the area that I can use for the panel
		Container containerForLabel = mainFrame.getContentPane();

		// add the label to the container
		containerForLabel.add(labelOfString);

		// update the 
		SwingUtilities.updateComponentTreeUI(mainFrame);

		// increment the number of the lines already displayed, and if it is maximum, isFull = true
		numberOfLines++;
		if (numberOfLines == NUMBEROFLINESALLOWED - 1) isFull = true;
	}
}