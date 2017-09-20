package photo_renamer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
/**
 * The GUI of PhotoRenamer. Add tags to a list, remove tags from a list, add tags to files,
 * remove tags from files, rename files to old names.
 * 
 * @author chupatr2, fongdere
 * @version Final
 */
/*
 * PhotoRenamer follows a MVC model and acts as the controller.
 * TagList and ImageLog are the main two models which are viewed when needed by update methods below.
 * PhotoRenamer itself creates the GUI for input in order to change the models.
 *
 * There are 11 listener classes which follow an observer pattern.
 * They observe whenever a specific action is performed and update the GUI as necessary 
 * by updating the view and / or passing changes to the model.
 */
public class PhotoRenamer {
	/**
	 * Execute lines to run PhotoRenamer.
	 * @param args
	 */
	public static void main(String[] args) {
		PhotoRenamer renamer = new PhotoRenamer();
		renamer.createAndShowGui();
	}
	
	/** The button that adds a tag to the list. */
	private JButton addTagButton;
	
	/** The button that adds tags to a file. */
	private JButton addTagToFileButton;
	/** The Panel that contains the buttons. */
	private JPanel buttonContainer;
	/** A directory containing the image files. */
	private File directory;
	/** The list of all images in directory. */
	private DefaultListModel<Image> fileList;
	/** The panel that contains the list of images and a list of old names. */
	private JPanel filePanel;
	
	/** The button that reverts to an older filename. */
	private JButton goToOldNameButton;
	/** The image icon to be displayed. */
	private ImageIcon icon;
	/** The buffered image to be display. */
	private BufferedImage image;
	/** The panel that contains the image to be displayed. */
	private JPanel imageContainer;
	
	/** The label that contains the image to be displayed. */
	private JLabel imageLabel;
	/** The display of all images in a directory. */
	private JList<Image> images;
	
	/** The scroll box for the list of images. */
	private JScrollPane imageScrollPane;
	/** The JFrame containing the PhotoRenamer GUI. */
	private JFrame jframe;
	/** The list of all old names of a file. */
	private DefaultListModel<ImageLogItem> nameList;
	
	/** The display of all old names of an image. */
	private JList<ImageLogItem> names;
	/** The scroll box for the list of oldNames. */
	private JScrollPane nameScrollPane;
	/** The button that chooses a directory. */
	private JButton openFileButton;
	
	/** The button that removes a tag from the list.*/
	private JButton removeTagButton;
	/** The button that removes tags from a file. */
	private JButton removeTagFromFileButton;
	/** The Panel that contains the buttons for tagging files. */
	private JPanel tagButtonContainer;
	/** The Panel that contains all check boxes for tags. */
	private JPanel tagContainer;
	
	/** The scroll box for the tagContainer. */
	private JScrollPane tagListScrollPane;
	
	/** A text field for entering text for tags. */
	private final JTextField textField;
	
	/* 
	 * The controller. Updates the model with user input. Creates the initial view which is updated with update.
	 * Some models are TagList and ImageLog. 
	 * More precisely, the model is the list of all tags and the list of old names for a file.
	 * The image list is also modeled however we just update the view of the old directory as we go.
	 */
	/**
	 * Creates the JFrame from PhotoRenamer
	 */
	private PhotoRenamer() {
		jframe = new JFrame("Photo Renamer");
		Container content = this.jframe.getContentPane();
		
		/* A text box is created for text input used for creating new tags. */
		textField = new JTextField();
		textField.setBorder(BorderFactory.createTitledBorder ("Type in a tag:"));
		/* Enable the add tag button if there is text. */
		textField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				if (!textField.getText().equals("")) {
					addTagButton.setEnabled(true);
				}
			}
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				if (textField.getText().equals("")) {
					addTagButton.setEnabled(false);
				} else {
					addTagButton.setEnabled(true);
				}
			}
			
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				if (textField.getText().equals("")) {
					addTagButton.setEnabled(false);
				}
			}
		});
		
/* Button controls */
		buttonContainer = new JPanel();
		openFileButton = new JButton("Choose directory");
		/** Creates window to choose a directory when clicked. */
		openFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showFileChooser();
			}
		});
		
		buttonContainer.add(openFileButton, BorderLayout.NORTH);
		
		/* Add a tag from the text box to the tag list when clicked.*/
		addTagButton = new JButton("Add a tag to the list");
		addTagButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ImageManager.tagList.addTag(textField.getText());
				updateTagListView();
			}
		});
		
		/* Remove tag(s) that are checked from the tag list when clicked. */
		removeTagButton = new JButton("Remove a tag from the list");
		removeTagButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				for (Component box: tagContainer.getComponents()) {
					if (box instanceof JCheckBox && ((JCheckBox) box).isSelected()) {
						ImageManager.tagList.removeTag(((JCheckBox)box).getText());
					}
				}
				updateTagListView();
				removeTagButton.setEnabled(false);
			}
		});
		
		addTagButton.setEnabled(false);
		removeTagButton.setEnabled(false);
		buttonContainer.add(addTagButton, BorderLayout.WEST);
		buttonContainer.add(removeTagButton, BorderLayout.EAST);
		
		tagButtonContainer = new JPanel();
		
		/*Add tags to an image when clicked and keeps the selection on the same file. */
		addTagToFileButton = new JButton("Add tags to image");
		addTagToFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ArrayList<String> tags = getSelectedTags();
				int index = images.getSelectedIndex();
				ImageManager.addTagToFile(tags, images.getSelectedValue());
				updateFileListView();
				images.setSelectedIndex(index);
			}
		});
		
		/*Remove tags from an image when clicked and keeps the selection on the same file. */
		removeTagFromFileButton = new JButton("Remove tags from image");
		removeTagFromFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ArrayList<String> tags = getSelectedTags();
				int index = images.getSelectedIndex();
				ImageManager.removeTagFromFile(tags, images.getSelectedValue());
				updateFileListView();
				images.setSelectedIndex(index);
			}
		});
		
		/*Reverts an image to an older name chosen in the old Name list. */
		goToOldNameButton = new JButton("Revert to older name");
		goToOldNameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int index = images.getSelectedIndex();
				ImageManager.renameToOldName(images.getSelectedValue(), names.getSelectedValue());
				updateFileListView();
				images.setSelectedIndex(index);
				goToOldNameButton.setEnabled(false);
			}
		});
		
		addTagToFileButton.setEnabled(false);
		removeTagFromFileButton.setEnabled(false);
		goToOldNameButton.setEnabled(false);
		tagButtonContainer.add(addTagToFileButton, BorderLayout.WEST);
		tagButtonContainer.add(removeTagFromFileButton, BorderLayout.CENTER);
		tagButtonContainer.add(goToOldNameButton,BorderLayout.EAST);
		buttonContainer.add(tagButtonContainer,BorderLayout.SOUTH);	
		
/* Tags */
		tagContainer = new JPanel();
		tagContainer.setLayout(new BoxLayout(tagContainer, BoxLayout.Y_AXIS));
		tagListScrollPane = new JScrollPane(tagContainer);
		tagListScrollPane.setBorder(BorderFactory.createTitledBorder ("Tags"));
		tagListScrollPane.setPreferredSize(new Dimension(200, 300));
		updateTagListView();
		
/* Files */
		fileList = new DefaultListModel<Image>();
		images = new JList<Image>(fileList);
		images.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		/** Updates the image displayed and the old name list when a file is selected */
		images.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (images.getSelectedValue() != null) {
					updateImage();
					updateOldNameListView();
				}
			}
		});
		images.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				updateButtons();
			}
		});
		imageScrollPane = new JScrollPane(images);
		imageScrollPane.setBorder(BorderFactory.createTitledBorder ("Images"));
		imageScrollPane.setPreferredSize(new Dimension(400, 300));
		
		nameList = new DefaultListModel<ImageLogItem>();
		names = new JList<>(nameList);
		names.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		names.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				goToOldNameButton.setEnabled(true);
			}
		});
		nameScrollPane = new JScrollPane(names);
		nameScrollPane.setBorder(BorderFactory.createTitledBorder ("Old Names"));
		nameScrollPane.setPreferredSize(new Dimension(400, 300));
		
		filePanel = new JPanel();
		filePanel.add(imageScrollPane, BorderLayout.WEST);
		filePanel.add(nameScrollPane, BorderLayout.EAST);
		
/* Image Display */
		icon = new ImageIcon();
		imageContainer = new JPanel();
		imageLabel = new JLabel(null, icon, JLabel.CENTER);
		imageContainer.add(imageLabel);
		imageContainer.setPreferredSize(new Dimension(400, 300));
		
		content.add(textField, BorderLayout.NORTH);
		content.add(tagListScrollPane, BorderLayout.WEST);
		content.add(imageContainer, BorderLayout.CENTER);
		content.add(filePanel, BorderLayout.EAST);
		content.add(buttonContainer, BorderLayout.SOUTH);
	}
	
	/**
	 * Create and show the PhotoRenamer GUI.
	 */
	private void createAndShowGui() {
		jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		jframe.pack();
		jframe.setVisible(true);
	}
	
	/**
	 * 
	 * @return
	 */
	private ArrayList<String> getSelectedTags() {
		ArrayList<String> tags = new ArrayList<String>();
		for (Component box: tagContainer.getComponents()) {
			if (box instanceof JCheckBox && ((JCheckBox) box).isSelected()) {
				tags.add(((JCheckBox) box).getText());
			}
		}
		return tags;
	}
	
	/**
	 * Create a box to choose a directory to look for images in
	 */
	private void showFileChooser() {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("."));
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = chooser.showOpenDialog(jframe);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			System.out.println("User opened file " + chooser.getSelectedFile().getAbsolutePath());
			this.directory = chooser.getSelectedFile();
			updateFileListView();
			updateTagListView();
		}
	}
	
	/**
	 * Updates the view of the file list panel to the current image files in the directory
	 */
	private void updateFileListView() {
		fileList.clear();
		for (Image image: ImageManager.getImages(this.directory)) {
			fileList.addElement(image);
		}
	}
	
	/**
	 * Updates the view of the image to the selected file
	 */
	private void updateImage() {
		if (!(images.getSelectedIndex() == -1)) {
	        try {
	        	image = (ImageIO.read(images.getSelectedValue().toFilePath()));
	        	icon = new ImageIcon(image.getScaledInstance(300, 300, 300));
	        	imageLabel.setIcon(icon);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	    }
	}
	/**
	 * Updates the view of the list of logged names of the selected file
	 */
	private  void updateOldNameListView() {
		nameList.clear();
		for (ImageLogItem name: ImageManager.imageLogger.getImageLog(images.getSelectedValue())) {
			nameList.addElement(name);
		}
	}
	/*
	 * These are methods to update the view of the GUI
	 */
	/**
	 * Updates the view of the tag list panel to the current tag list.
	 */
	private void updateTagListView() {
		tagContainer.removeAll();
		for (String tag: ImageManager.tagList.getTagList()) {
			JCheckBox tagbox = new JCheckBox(tag);
			tagbox.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					updateButtons();
				}
			});
			tagContainer.add(tagbox);
		}
		tagContainer.revalidate();
		tagContainer.repaint();
	}
	
	private void updateButtons() {
		addTagToFileButton.setEnabled(false);
		removeTagFromFileButton.setEnabled(false);
		removeTagButton.setEnabled(false);
		for (Component box: tagContainer.getComponents()) {
			if (box instanceof JCheckBox  && ((JCheckBox) box).isSelected()) {
				removeTagButton.setEnabled(true);
				if (images.getSelectedValue() != null) {
					addTagToFileButton.setEnabled(true);
					removeTagFromFileButton.setEnabled(true);
				}
			}
		}
	}
}
