package photo_renamer;

import static org.junit.Assert.*;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ImageLogTest {
	private static String logFile = System.getProperty("user.dir") + "\\TestLogs.txt";
	private static String logFile2 = System.getProperty("user.dir") + "\\Test2Logs.txt";
	static ImageLog log;
	static ImageLog log2;

	@Before
	public void setUp() throws Exception {
		log = new ImageLog(logFile);
		log2 = new ImageLog(logFile2);
		log2.beginLog(new File("True.jpg"));;
		log2.beginLog(new File("Wow.jpg"));;
	}

	@After
	public void tearDown() throws Exception {
		new File(logFile).delete();
		new File(logFile2).delete();
	}

	@Test
	public void testBeginLog() {
		log.beginLog(new File("True.jpg"));
		Timestamp expected = log2.getLogList().get("True").get(0).getTimeStamp();
		log2.beginLog(new File("True.jpg"));
		Timestamp actual = log2.getLogList().get("True").get(0).getTimeStamp();
		
		/** Getters and setters for the log list */
		assertTrue(log.getLogList().containsKey("True"));
		assertTrue(log2.getLogList().containsKey("True"));
		assertTrue(log.getLogList().size() == 1);
		assertTrue(log2.getLogList().size() == 2);
		assertTrue(log.getLogList().get("True").size() == 1);
		
		/** Check log created when log already exists */
		assertTrue(log2.getLogList().get("True").size() == 1);
		
		/** Check if time stamp changed when log already exists */
		assertEquals(expected, actual);
		
		assertTrue(log.getLogList().get("True").get(0).toString().equals("True.jpg"));
		assertTrue(log2.getLogList().get("True").get(0).toString().equals("True.jpg"));
	}
	
	@Test
	public void testAddLog() {
		log.beginLog(new File("Wow.jpg"));
		log.addLog(new File("Wow @Tag1 @Tag2.jpg"));
		log2.addLog(new File("True @Tag1.jpg"));
		
		assertTrue(log.getLogList().get("Wow").size() == 2);
		/** Check if the log at the current index is the same as what was added */
		assertTrue(log.getLogList().get("Wow").get(log.getLogList().size()).toString().equals("Wow @Tag1 @Tag2.jpg"));
		assertTrue(log2.getLogList().get("True").get(log.getLogList().size()).toString().equals("True @Tag1.jpg"));
	}
	
	@Test
	public void testGetImageLog() {
		log.beginLog(new File("Wow.jpg"));
		log.addLog(new File("Wow @Tag1 @Tag2.jpg"));
		log2.addLog(new File("True @Tag1.jpg"));
		
		ArrayList<ImageLogItem> oldLog = log.getImageLog(new Image(new File("Wow.jpg")));
		ArrayList<ImageLogItem> oldLog2 = log2.getImageLog(new Image(new File("True.jpg")));
		assertTrue(oldLog.size() == 2);
		assertTrue(oldLog2.size() == 2);
		assertTrue(!(oldLog.get(0) == oldLog.get(1)));
		assertTrue((oldLog.get(0).toString().equals("Wow.jpg")));
		assertTrue((oldLog.get(1).toString().equals("Wow @Tag1 @Tag2.jpg")));
	}

}
