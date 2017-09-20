package photo_renamer;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class ImageTest {

	static Image noTagImage;
	static Image taggedImage;

	@Before
	public void setUp() throws Exception {
		File file = new File("C:\\Users\\Public\\Pictures\\Sample Pictures\\animal-eye2.jpg");
		noTagImage = new Image(file);
		File file2 = new File("C:\\Users\\Public\\Pictures\\Sample Pictures\\animal.eye2 @Wow @You.png");
		taggedImage = new Image(file2);
	}

	@Test
	public void testGetNameNoTag() {
		String expected = "animal-eye2";
		String actual = noTagImage.getName();
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetNameTagged() {
		String expected = "animal.eye2";
		String actual = taggedImage.getName();
		assertEquals(expected, actual);
	}
	
	
	@Test
	public void testAddTagsToNoTags(){
		noTagImage.addTag("hi");
		noTagImage.addTag("huehue");
		noTagImage.addTag("hi");
		ArrayList<String> expected = new ArrayList<String>(Arrays.asList("hi", "huehue"));
		ArrayList<String> actual = noTagImage.getTags();
		assertEquals("No extra tag added.", actual, expected);
	}
	
	@Test
	public void testGetTagsFromNoTags(){
		ArrayList<String> expected = new ArrayList<>();
		ArrayList<String> actual = noTagImage.getTags();
		assertEquals("Empty list of tags returned.", actual, expected);
	}
	
	@Test
	public void testGetExtensionFromNoTags() {
		String expected = ".jpg";
		String actual = noTagImage.getExtension();
		assertEquals(expected, actual);
	}
	
	@Test
	public void removeTagFromNoTags() {
		noTagImage.removeTag("Wow");
		ArrayList<String> expected = new ArrayList<>();
		ArrayList<String> actual = noTagImage.getTags();
		assertEquals("Empty list of tags returned.", actual, expected);
	}
	
	@Test
	public void testNoTagsToString() {
		String expected = "animal-eye2.jpg";
		String actual = noTagImage.toString();
		assertEquals(actual, expected);
	}
	
	@Test
	public void testAddTagsToTaggedImage(){
		taggedImage.addTag("hi");
		taggedImage.addTag("huehue");
		taggedImage.addTag("hi");
		ArrayList<String> expected = new ArrayList<String>(Arrays.asList("Wow", "You", "hi", "huehue"));
		ArrayList<String> actual = taggedImage.getTags();
		assertEquals("No extra tag added.", actual, expected);
	}
	
	@Test
	public void testGetTagsFromTaggedImage(){
		ArrayList<String> expected = new ArrayList<>(Arrays.asList("Wow", "You"));
		ArrayList<String> actual = taggedImage.getTags();
		assertEquals("Empty list of tags returned.", actual, expected);
	}
	
	@Test
	public void testGetExtensionFromTaggedImage() {
		String expected = ".png";
		String actual = taggedImage.getExtension();
		assertEquals(expected, actual);
	}
	
	@Test 
	public void testremoveTagsFromTaggedImage() {
		taggedImage.removeTag("Wow");;
		taggedImage.removeTag("Wow");
		ArrayList<String> expected = new ArrayList<>(Arrays.asList("You"));
		ArrayList<String> actual = taggedImage.getTags();
		assertEquals("No problems removing multiple times.", expected, actual);
	}
	
	@Test
	public void testTaggedImageToString() {
		String expected = "animal.eye2 @Wow @You.png";
		String actual = taggedImage.toString();
		assertEquals(actual, expected);
	}
}
