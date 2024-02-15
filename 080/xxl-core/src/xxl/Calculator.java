package xxl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;

import xxl.exceptions.FileNotSavedException;
import xxl.exceptions.ImportFileException;
import xxl.exceptions.MissingFileAssociationException;
import xxl.exceptions.UnavailableFileException;
import xxl.exceptions.UnrecognizedEntryException;


/**
 * Class representing a spreadsheet application.
 */
public class Calculator {

    private Spreadsheet _spreadsheet = null;
    private String _filename = "";


    public void newSpreadsheet(int lines, int columns) {
        _spreadsheet = new Spreadsheet(lines, columns);
    }

    /**
     * Returns the spreadsheet.
     */
    public Spreadsheet getSpreadsheet() {
        return _spreadsheet;
    }

    /**
     * Saves the spreadsheet into the file associated.
     *
     * @throws FileNotFoundException if for some reason the file cannot be found. 
     * @throws MissingFileAssociationException if for some reason the file cannot be associated.
     * @throws IOException if for some reason there is an error while serializing.
     */
    public void save() throws FileNotFoundException, MissingFileAssociationException, IOException {
        /* If the spreadsheet has not changed, there is no point in saving again. */
		if(!_spreadsheet.hasChanged()) {
			return;
		}

        /* If the spreadsheet has no name, it cannot be saved. */
		if (_filename == "") {
			throw new MissingFileAssociationException();
		}

        /* Saves, and sets the spreadsheet as unchanged. */
		try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(_filename)))) {
			oos.writeObject(_spreadsheet);
			_spreadsheet.setChanged(false);
		}

	}

    /**
     * Saves the spreadsheet, naming the file.
     *
     * @param filename the name of the file.
     * @throws FileNotFoundException if for some reason the file cannot be found. 
     * @throws MissingFileAssociationException if for some reason the file cannot be associated.
     * @throws IOException if for some reason there is an error while serializing.
     */
    public void saveAs(String filename) throws FileNotFoundException, MissingFileAssociationException, IOException {
		_filename = filename;
		save();
	}

    /**
     * Loads the spreadsheet from the file associated, if it exists.
     * 
     * @param filename name of the file containing the serialized application's state
     *        to load.
     * @throws UnavailableFileException if the specified file does not exist or there is
     *         an error while processing this file.
     */
    public void load(String filename) throws UnavailableFileException {
		
		try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
			_filename = filename;
			_spreadsheet = (Spreadsheet) ois.readObject();
			_spreadsheet.setChanged(false);
		} catch (IOException | ClassNotFoundException e) {
			throw new UnavailableFileException(filename);
		}

	}

    /**
     * Imports the contents of a text file into the spreadsheet.
     *
     * @param filename name of the text input file.
     * @throws ImportFileException if there is an error while importing the file.
     * 
     */
    public void importFile(String filename) throws ImportFileException {

        try {
            String currentLine;
            int lines;
            int columns;
            
            /* Makes a BufferedReader to read the text file. */
            BufferedReader reader = new BufferedReader(new FileReader(filename));

            /* Gets the number of lines and columns of the spreadhseet. */
            lines = Integer.parseInt(reader.readLine().split("=")[1]);
            columns = Integer.parseInt(reader.readLine().split("=")[1]);

            /* Creates a new spreadsheet with the given number of lines and columns. */
            _spreadsheet = new Spreadsheet(lines, columns);

            /* Reads the file line by line, and inserts the contents into the spreadsheet. */
            while ((currentLine = reader.readLine()) != null) {
                String[] splitLine = currentLine.split("\\|");

                String address = splitLine[0];
                String content = "";

                /* If the splitline has more than one element, it means that the second is the content (and is not empty). */
                if (splitLine.length >= 2) content = splitLine[1];
                
                /* If the spreadsheet was unchanged and the content is not empty, it means that the spreadsheet has now changed. */
                if (!_spreadsheet.hasChanged() && !content.equals("")) _spreadsheet.setChanged(true);
            
                /* During the import file, it is not expected to have invalid function names,
                 * therefore, the exception will never be thrown.
                 */
                try {
	                _spreadsheet.insertContents(address, content);
                } catch (UnrecognizedEntryException e) {}
            }
            reader.close();
        } catch (IOException e) {
            throw new ImportFileException(filename, e);
        }
    }

    /* Throws a FileNotSavedException if the file was modified but not saved. */
    public void isValidSpreadsheet() throws FileNotSavedException {
        if (this.getSpreadsheet() != null) {
            if (this.getSpreadsheet().hasChanged()) {
                throw new FileNotSavedException();
            }
        }
    }

}
