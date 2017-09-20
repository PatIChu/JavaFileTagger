package photo_renamer;

import java.io.File;
import java.util.ArrayList;
/**
 * The ImageManager is a collection of static methods that deal with file editing and finding.
 * 
 * @author chupatr2, fongdere
 * @version
 */
/*
 * Image Manager is "observed" by ImageLog and sends updates when files are changed
 */
public final class ImageManager {
	protected static ImageLog imageLogger = new ImageLog(System.getProperty("user.dir") + "//log.txt");
	protected static TagList tagList = new TagList(System.getProperty("user.dir") + "//tags.txt");
	
	/**
	 * Add tags to a file with a list of tags. If the file already has the tags, nothing is done.
	 * <p>
	 * Add a log of the new change to the image log if the file is changed.
	 * 
	 * @param tags the list of tags to add to a file
	 * @param file the Image representation of a file
	 */
	public static void addTagToFile(ArrayList<String> tags, Image file) {
		File currentFile = file.toFilePath();
		for (String tag: tags) {
			file.addTag(tag);
		}
		File newFileName = file.toFilePath();
		if (!newFileName.exists()) {
			currentFile.renameTo(newFileName);
			imageLogger.addLog(newFileName);
		}
	}
	
	/**
	 * Return the list of images that end with .jpg, .png, .bmp in a directory 
	 * and it's sub directories.
	 * 
	 * @param file the directory that is being looked at
	 * @return
	 */
	public static ArrayList<Image> getImages(File file){
		ArrayList<Image> images = new ArrayList<Image>();
		if(file.isFile()) {
			String extension = new Image(file).getExtension().toString();
			if (extension.equals(".jpg")|| extension.equals(".png") || extension.equals(".bmp")) {
				images.add(new Image(file));
				imageLogger.beginLog(file);
			}
		} else if(file.isDirectory()) {
			File[] fileList = file.listFiles();
			if(fileList != null) {
				for (File insideFile: fileList) {
					images.addAll(getImages(insideFile));
				}
			}
		}
		
		return images;
	}
	
	/**
	 * Remove tags from a file with a list of tags. If the file does not have any of the tags, nothing is done.
	 * <p>
	 * Add a log of the new change to the image log if the file is changed.
	 * 
	 * @param tags the list of tags to add to a file
	 * @param file the Image representation of a file
	 */
	public static void removeTagFromFile(ArrayList<String> tags, Image file) {
		File currentFile = file.toFilePath();
		for (String tag: tags) {
			file.removeTag(tag);
		}
		File newFileName = file.toFilePath();
		if (!newFileName.exists()) {
			currentFile.renameTo(newFileName);
			imageLogger.addLog(newFileName);
		}
	}
	
	/**
	 * Rename a file to a old name that it had. If the file is that name, nothing is done.
	 * <p>
	 * Add a log of the new change to the image log if the file is changed.
	 * 
	 * @param file the Image representation of the file
	 * @param oldName one of the ImageLogItem of the file
	 */
	public static void renameToOldName(Image file, ImageLogItem oldName) {
		File oldFileName = oldName.toFilePath();
		if (!oldFileName.exists()) {
			file.toFilePath().renameTo(oldFileName);
			imageLogger.addLog(oldFileName);
		}
	}
	
}
