package photo_renamer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * The TagList is a class that saves, reads, and edits a list of tags.
 * 
 * @author chupatr2, fongdere
 * @version Final
 */
public class TagList {
	/** The file path to the file where the tag list is saved too. */
	private String filepath;
	/** The big list of persistent tags. */
	private ArrayList<String> tagList = new ArrayList<String>();
	
	/**
	 * A TagList class which manages and saves all tags.
	 * 
	 * @param filepath the file path to where the tag list is saved too
	 */
	public TagList(String filepath) {
		this.filepath = filepath;
		
		File tagFile = new File(filepath);
        if (tagFile.exists() && tagFile.length() != 0) {
            readFromFile(filepath);
        } else {
            try {
				tagFile.createNewFile();
			} catch (IOException e) {
				System.out.println("Could not create file.");
	        	e.printStackTrace();
			}
        }
    }
	
	/**
	 * Add a tag to the big list of tags if it is in the list. Otherwise do nothing.
	 * 
	 * @param tag the tag to add
	 */
	public void addTag(String tag) {
		if (!tagList.contains(tag)) {
			tagList.add(tag);
			saveToFile(filepath);
		}
	}
	
	/**
	 * Return the big list of tags.
	 * 
	 * @return the tagList
	 */
	public ArrayList<String> getTagList() {
		return tagList;
	}
	
	/**
	 * Populate the tagList with tags from the file if in a readable format.
	 * 
	 * @param path the file path of the file to read from
	 */
	@SuppressWarnings("unchecked")
	public void readFromFile(String path) {

        try {
            InputStream file = new FileInputStream(path);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            //Undo the serialize of the Map
            tagList = (((ArrayList<String>) input.readObject()));
            input.close();
        } catch (IOException | ClassNotFoundException ex) {
        	System.out.println("Could not read file.");
        	ex.printStackTrace();
        }  
    }
	
	/**
	 * Remove a tag from the big list of tags if it is in the list. Otherwise do nothing.
	 * 
	 * @param tag the tag to remove
	 */
	public void removeTag(String tag) {
		if (tagList.contains(tag)) {
			tagList.remove(tag);
			saveToFile(filepath);
		}
	}
	
	/**
	 * Save the tagList to a file.
	 * 
	 * @param filePath
	 */
	public void saveToFile(String filePath) {
		try {
			OutputStream file = new FileOutputStream(filePath);
			OutputStream buffer = new BufferedOutputStream(file);
			ObjectOutput output = new ObjectOutputStream(buffer);

			// Serialize the Map
			output.writeObject(tagList);
			output.close();
		} catch (IOException ex) {
			System.out.println("Could not save to file.");
        	ex.printStackTrace();
		}
    }
	
}
