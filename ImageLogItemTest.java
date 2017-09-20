package photo_renamer;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Calendar;
import org.junit.BeforeClass;
import org.junit.Test;

public class ImageLogItemTest extends ImageTest {

	static ImageLogItem imageLog;
	static java.util.Date now;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Calendar calendar = Calendar.getInstance();
		now = calendar.getTime();
		imageLog = new ImageLogItem(new File("TestFile.jpg"));
	}
	
	@Test
	public void testGetTimeStamp() {
		assertTrue(imageLog.getTimeStamp().getTime() - now.getTime() <= 2 );
	}
	
}
