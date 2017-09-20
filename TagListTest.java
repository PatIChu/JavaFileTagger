package photo_renamer;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TagListTest {
	private static String tagFile = System.getProperty("user.dir") + "\\TestTags.txt";
	private static String tagFile2 = System.getProperty("user.dir") + "\\Test2Tags.txt";
	static TagList list;
	static TagList list2;

	@Before
	public void setUp() throws Exception {
		list = new TagList(tagFile);
		list2 = new TagList(tagFile2);
		list2.addTag("One");
	}

	@After
	public void tearDown() throws Exception {
		new File(tagFile).delete();
		new File(tagFile2).delete();
	}

	@Test
	public void testAdd() {
		list.addTag("One");
		list.addTag("Two");
		list.addTag("One");
		ArrayList<String> expected = new ArrayList<String>(Arrays.asList("One", "Two"));
		ArrayList<String> actual = list.getTagList();
		assertEquals(expected, actual);
	}
	
	@Test
	public void testDelete() {
		list2.addTag("Two");
		list2.addTag("Three");
		list2.removeTag("One");
		list2.removeTag("Three");
		ArrayList<String> expected = new ArrayList<String>(Arrays.asList("Two"));
		ArrayList<String> actual = list2.getTagList();
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetTag() {
		ArrayList<String> expected = new ArrayList<String>(Arrays.asList("One"));
		ArrayList<String> actual = list2.getTagList();
		assertEquals(expected, actual);
	}

	@Test
	public void testWrite() {
		list.saveToFile(tagFile2);
		list2.readFromFile(tagFile2);
		ArrayList<String> expected = new ArrayList<String>();
		ArrayList<String> actual = list2.getTagList();
		assertEquals(expected, actual);
	}
	
	@Test
	public void testRead() {
		list.readFromFile(tagFile2);
		ArrayList<String> expected = new ArrayList<String>(Arrays.asList("One"));
		ArrayList<String> actual = list.getTagList();
		assertEquals(expected, actual);
	}
}
