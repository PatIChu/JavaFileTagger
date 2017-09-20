package photo_renamer;

import java.io.File;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
/**
 * The ImageLogItem class which extends Image and adds a time stamp property of when it is created.
 * 
 * @author chupatr2, fongdere
 * @version Final
 */
public class ImageLogItem extends Image implements Serializable{
	/** Determines if a un-serialized file is compatible with this class. */
	private static final long serialVersionUID = -4586377751292073642L;
	/** The time that the log was made */
	private Timestamp timeStamp;
	
	/**
	 * Create a log of an image
	 * 
	 * @param image the file that is being logged
	 */
	public ImageLogItem(File image) {
		super(image);
		Calendar calendar = Calendar.getInstance();
		java.util.Date now = calendar.getTime();
		this.timeStamp = new java.sql.Timestamp(now.getTime());
	}
	
	/**
	 * Return the time the image log was made.
	 * 
	 * @return the time stamp
	 */
	public Timestamp getTimeStamp() {
		return timeStamp;
	}
	
	/**
	 * Set the time the log was made.
	 * 
	 * @param timeStamp the time stamp that is set to
	 */
	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	/**
	 * Return a string representation of an ImageLogItem
	 * in the form of time stamp: (super Image representation)
	 */
	public String toString() {
		return this.timeStamp.toString() + ": " + super.toString();
	}
}
