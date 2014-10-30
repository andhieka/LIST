package list;

import static org.junit.Assert.*;
import list.ICommand.CommandExecutionException;
import list.ITask.TaskStatus;

import org.junit.Before;
import org.junit.Test;

public class UnmarkCommandTest {

	private TaskManager taskManager = TaskManager.getInstance();
	
	@Before
	public void initializeTest() {
		taskManager.clearTasks();
		
		ITask taskOne = new Task();
		taskOne.setTitle("task 1");
		taskOne.setStatus(TaskStatus.DONE);
		
		ITask taskTwo = new Task();
		taskTwo.setTitle("task 2");
		taskTwo.setStatus(TaskStatus.DONE);
		
		taskManager.addTask(taskOne);
		taskManager.addTask(taskTwo);
		
		Controller.refreshUi();
	}
	
	@Test
	public void shouldChangeTaskStatusToPending() throws Exception {
		int taskNumber = 1;
		ITask taskToUnmark = Controller.getTaskWithNumber(taskNumber);
		UnmarkCommand unmarkCommand = new UnmarkCommand(taskToUnmark);
		
		unmarkCommand.execute();
				
		assertEquals(TaskStatus.PENDING, taskToUnmark.getStatus());
	}
	
	@Test
	public void shouldReturnSuccessMessageUponSuccessfulOperation() 
			throws Exception {
		int taskNumber = 1;
		UnmarkCommand unmarkCommand = new UnmarkCommand(Controller.getTaskWithNumber(taskNumber));
		
		String reply = unmarkCommand.execute();
				
		assertEquals("Task is unmarked successfully.", reply);
	}
	
}
