package photo_renamer;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ImageManagerTest {
	
	private static String testFolder = System.getProperty("user.dir") + "\\TestFolder";
	private static File imageFile;
	private static File imageFile2;
	private static File imageFile3;
	private static ImageManager manager;
	
	/**Setting up image logger so that it makes test logs and test lists*/
	@SuppressWarnings("static-access")
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		manager = new ImageManager();
		new File(testFolder).mkdir();
		new File(testFolder + "\\InsideFolder").mkdir();
		manager.imageLogger = new ImageLog(System.getProperty("user.dir") + "\\TestLog.txt");
		manager.tagList = new TagList(System.getProperty("user.dir") + "\\TestList.txt");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		new File(System.getProperty("user.dir") + "\\TestList.txt").delete();
		new File(System.getProperty("user.dir") + "\\TestLog.txt").delete();
		new File(testFolder).delete();
		new File(testFolder + "\\InsideFolder").delete();
	}

	@Before
	public void setUp() throws Exception {
		imageFile = new File(testFolder + "\\animal.jpg");
		imageFile2 = new File(testFolder + "\\animal2 @hi @bye.jpg");
		imageFile3 = new File(testFolder + "\\InsideFolder\\sneakyimage.jpg");
		imageFile.createNewFile();
		imageFile2.createNewFile();
		imageFile3.createNewFile();
	}

	@After
	public void tearDown() throws Exception {
		for (File testFile: new File(testFolder).listFiles()) {
			if (testFile.isDirectory()) {
				for (File innerFile: testFile.listFiles()) {
					innerFile.delete();
				}
			} else {
				testFile.delete();
			}
		}
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void getImages() {
		ArrayList<Image> images= manager.getImages(new File(testFolder));
		System.out.println(images);
		assertTrue(images.size() == 3);
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void testAddTagToFile(){
		ArrayList<String> tags = new ArrayList<>();
		tags.addAll(Arrays.asList("hi", "bye"));
		manager.getImages(new File(testFolder));
		manager.addTagToFile(tags, new Image(imageFile));
		manager.addTagToFile(tags, new Image(imageFile2));
		manager.addTagToFile(tags, new Image(imageFile3));
		assertTrue(new File(testFolder + "\\animal @hi @bye.jpg").exists());
		assertTrue(new File(testFolder + "\\animal2 @hi @bye.jpg").exists());
		assertTrue(new File(testFolder + "\\InsideFolder\\sneakyimage @hi @bye.jpg").exists());
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void testRemoveTagFromFile(){
		ArrayList<String> tagsToRemove = new ArrayList<>();
		tagsToRemove.addAll(Arrays.asList("hi", "bye"));
		manager.getImages(imageFile);
		manager.removeTagFromFile(tagsToRemove, new Image(imageFile));
		manager.removeTagFromFile(tagsToRemove, new Image(imageFile2));
		manager.removeTagFromFile(tagsToRemove, new Image(imageFile3));
		assertTrue(new File(testFolder + "\\animal.jpg").exists());
		assertTrue(new File(testFolder + "\\animal2.jpg").exists());
		assertTrue(new File(testFolder + "\\InsideFolder\\sneakyimage.jpg").exists());
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void testRenameToOldName(){
		ArrayList<String> tags = new ArrayList<>();
		tags.add("bye");
		manager.addTagToFile(tags, new Image(imageFile));
		tags.add("cya");
		manager.addTagToFile(tags, new Image(imageFile));
		ArrayList<ImageLogItem> oldName = manager.imageLogger.getImageLog(new Image(imageFile));
		manager.renameToOldName(new Image(imageFile), oldName.get(oldName.size() - 1));
		assertTrue(new File(testFolder + "\\animal @bye.jpg").exists());
	}
	
}
