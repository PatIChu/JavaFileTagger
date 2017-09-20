package photo_renamer;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
/**
 * The image class which contains the properties of an image which are name, tags,
 * and file path.
 * 
 * @author chupatr2, fongdere
 * @version Final
 */
public class Image implements Serializable{
	/** Determines if a un-serialized file is compatible with this class. */
	private static final long serialVersionUID = -4228058213657423943L;
	/** The extension of the image */
	private String extension;
	/** The name of the image */
	private String name;
	/** The directory of the image */
	private String parent;
	/** The tags of the image */
	private ArrayList<String> tags;
	
	/** 
	 * Create the properties relating to
	 * the the name of an Image.
	 * 
	 * @param image
	 * 			the image file that is represented
	 */
	public Image(File image) {
		String imageName = image.getName();
		this.parent = image.getParent();
		this.tags = getTags(imageName);
		this.name =  imageName.substring(0, imageName.lastIndexOf(".")).split("@")[0].trim();
		this.extension = imageName.substring(imageName.lastIndexOf("."));
	}
	
	/**
	 * Add a tag to an Image if the Image does not have that tag.
	 * 
	 * @param tag the string tag that is added
	 */
	public void addTag(String tag) {
		if (!tags.contains(tag)) {
			this.tags.add(tag);
		}
	}
	
	/**
	 * Return the extension of an Image.
	 * 
	 * @return the extension of an Image.
	 */
	public String getExtension() {
		return extension;
	}
	
	/**
	 * Return the name of an Image.
	 * 
	 * @return return the string name of an Image
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Return the tags of an Image.
	 * 
	 * @return the list of tags of an Image
	 */
	public ArrayList<String> getTags() {
		return tags;
	}
	
	/**
	 * Return tags in an Image name and updates the big list of tags.
	 * <p>
	 * This method returns an empty list and adds no tags if there are no tags.
	 * 
	 * @param imageName the absolute name of an image
	 * @return a list of @ suffix-removed tags of an image
	 */
	private ArrayList<String> getTags(String imageName) {
		ArrayList<String> tags = new ArrayList<String>(Arrays.asList(imageName.substring(0, imageName.lastIndexOf(".")).split(" @")));
		tags.remove(0);
		for (String tag: tags) {
			ImageManager.tagList.addTag(tag.trim());
		}
		return tags;
	}
	
	/**
	 * Remove a tag from an Image if the Image has that tag.
	 * 
	 * @param tag the string tag that is removed
	 */
	public void removeTag(String tag) {
		if (tags.contains(tag)) {
			this.tags.remove(tag);
		}
	}
	
	/**
	 * Set the extension of an Image.
	 * 
	 * @param extension the extension that is set
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}
	
	/**
	 * Set the name of an Image.
	 * 
	 * @param name the name that is set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Set the tags of an Image.
	 * 
	 * @param tags the list of tags that is set
	 */
	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}
	
	/**
	 * Return a file path representation an Image.
	 * <p>
	 * The file may or may not exist as the tags change.
	 * 
	 * @return the file path that an image would have if it were a file
	 */
	public File toFilePath() {
		return new File(this.parent + "//" + this.toString());
	}
	
	/**
	 * Return the full name of an Image.
	 * 
	 * @return the absolute name of an image with tags and extension
	 */
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(name);
		if (!(tags == null)) {
			for (String tag: tags) {
				str.append(" @" + tag);
			}
		}
		str.append(extension);
		return str.toString();
	}

}
