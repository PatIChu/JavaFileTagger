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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
/**
 * The ImageLog is a class that saves, reads, and adds to a list of ImageLogItems of a file.
 * 
 * @author chupatr2
 * @version Final
 */
/*
 * ImageLog follows a sort of Observer pattern as it is updated whenever ImageManager calls upon it.
 * It's called whenever a file is changed and addTag is pretty much what update would be for an observer
 * class
 *  
 */
public class ImageLog implements Serializable{
	/** Determines if a un-serialized file is compatible with this class. */
	private static final long serialVersionUID = -4126406550099717792L;
	/** A map containing the associated ImageLogItems to a filename */
	private LinkedHashMap<String, ArrayList<ImageLogItem>> logList = new LinkedHashMap<String, ArrayList<ImageLogItem>>();
	
	/**
	 * Creates a log of an file's old and current full names.
	 * <p>
	 * The log is composed of the short name of the file and the associated ImageLogItems of that file.
	 * The log is saved to a .ser file.
	 * 
	 * @param filepath The file path were the log is stored to.
	 */
	public ImageLog(String filepath) {
		
		File file = new File(filepath);
        if (file.exists() && file.length() != 0) {
			readFromFile(filepath);
        } else {
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println("Could not create file.");
				e.printStackTrace();
			}
        }
	}
	
	/**
	 * Add a new log to the image log.
	 * 
	 * @param image the file that is being logged.
	 */
	public void addLog(File image) {
		ImageLogItem imageLog = new ImageLogItem(image);
		String name = imageLog.getName();
		
		ArrayList<ImageLogItem> imageLogs = logList.get(name);
	    imageLogs.add(imageLog);
	    
	    saveToFile(System.getProperty("user.dir") + "\\log.txt");
	}
	
	/**
	 * Begin a log of a file if it does not exist.
	 * If it does exist, nothing is done.
	 * 
	 * @param image
	 */
	public void beginLog(File image) {
		ImageLogItem imageLog = new ImageLogItem(image);
		String name = imageLog.getName();
		ArrayList<ImageLogItem> imageLogs = logList.get(name);
		
		if(imageLogs == null) {
	    	imageLogs = new ArrayList<ImageLogItem>();
	    	imageLogs.add(imageLog);
	    	logList.put(name, imageLogs);
		}
	}
	
	/**
	 * Return the list of old names associated with a file
	 * 
	 * @param image the file to get the logs of
	 * @return a list of ImageLogItem associated to the file
	 */
	public ArrayList<ImageLogItem> getImageLog(Image image) {
		return logList.get(image.getName());
	}
	
	/**
	 * Return all the logs in ImageLog
	 * 
	 * @return the logList
	 */
	public LinkedHashMap<String, ArrayList<ImageLogItem>> getLogList() {
		return logList;
	}
	
	/**
	 * Read the file that contains the log of an image and populate the logList
	 * 
	 * @param path the string file path of where the log is located
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	@SuppressWarnings("unchecked")
	public void readFromFile(String path) {
        try {
            InputStream file = new FileInputStream(path);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            //Undo the serialize of the Map
            logList = (LinkedHashMap<String, ArrayList<ImageLogItem>>) input.readObject();
            input.close();
        } catch (IOException | ClassNotFoundException ex) {
        	System.out.println("Could not read file.");
        	ex.printStackTrace();
        }  
    }
	
	/**
	 * Save the logList to a file.
	 * 
	 * @param filePath the file path to save the log list to
	 */
	public void saveToFile(String filePath) {
		try {
			OutputStream file = new FileOutputStream(filePath);
			OutputStream buffer = new BufferedOutputStream(file);
			ObjectOutput output = new ObjectOutputStream(buffer);

			// Serialize the Map
			output.writeObject(logList);
			output.close();
		} catch (IOException ex) {
			System.out.println("Could not save to file.");
			ex.printStackTrace();
		}
    }
	
	/**
	 * Set all the logs in ImageLog
	 * 
	 * @param logList the logList to set to.
	 */
	public void setLogList(LinkedHashMap<String, ArrayList<ImageLogItem>> logList) {
		this.logList = logList;
	}
}
