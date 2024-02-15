package xxl;

import xxl.exceptions.UnrecognizedEntryException;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import xxl.structures.*;
import xxl.visitors.*;
import xxl.content.*;
import xxl.content.binaryFunctions.*;
import xxl.content.intervalFunctions.*;
import xxl.search.*;

/**
 * Class representing a spreadsheet.
 */
public class Spreadsheet implements Serializable, Cloneable {

    @Serial
    private static final long serialVersionUID = 202308312359L;

    private StorageStructure _spreadsheetStorage = null;
    private boolean _changed = false;
    private int _lines;
    private int _columns;

    private Map<String, User> _spreadsheetUsers;
    private User _user;

    private ArrayList<String> _cutBufferResults = new ArrayList<String>();
    private ArrayList<Cell> _cutBufferCells = new ArrayList<Cell>();
    private String _cutBufferAddress = "";

    /**
     * Creates a new spreadsheet with specified number of lines and columns, inicializing the storage structure.
     */
    public Spreadsheet(int lines, int columns) {
        _lines = lines;
        _columns = columns;
        inicializeStorage(lines, columns);

        _spreadsheetUsers = new TreeMap<String, User>();
        _user = new User();
    }

    /**
     * Inicializes all the Spreadsheet
     */
    public void inicializeStorage(int lines, int columns) {
        _spreadsheetStorage = new TreeMapStorage();
        String address;

        for (int j = 0; j <= lines; j++) {
            for (int i = 0; i <= columns; i++) {
                address = j + ";" + i;
                Cell cell = new Cell();
                _spreadsheetStorage.putCell(address, cell);
            }
        }
    }

    public void setChanged(boolean changed) {
		_changed = changed;
	}

    public boolean hasChanged() {
		return _changed;
	}

    public StorageStructure getStorageStructure() {
        return _spreadsheetStorage;
    }
    
    /**
     * Returns true if the address is on the spreadsheet.
     */
    public boolean onSpreadsheet(String address) {
        String parts[] = address.split(";");
        return (Integer.parseInt(parts[0]) <= _lines && Integer.parseInt(parts[1]) <= _columns &&
               (Integer.parseInt(parts[0]) > 0 && Integer.parseInt(parts[1]) > 0 ));
    }

    /**
     * Returns true if a gamma is well built (if it is a line or a column).
     */
    public boolean wellBuiltGamma(String firstAddress, String secondAddress) {
        return (firstAddress.charAt(0) == secondAddress.charAt(0)) || (firstAddress.charAt(2) == secondAddress.charAt(2));
    }

    /**
     * Returns true if the gamma is well defined (on the spreadsheet and well built).
     */
    public boolean wellDefinedGamma(String gamma) {
        /* If gamma contains more than one cell (ex: 1;1:1;2) */
        if (gamma.contains(":")) {
            String[] parts = gamma.split(":");
            return onSpreadsheet(parts[0]) && onSpreadsheet(parts[1]) && wellBuiltGamma(parts[0], parts[1]);
        }
        /* If gamma is a single cell */
        return onSpreadsheet(gamma);
    }

    public boolean isDigit(String string) {
        int length = string.length();
        /* If a single caracter of the string is not a digit, return false */
        for (int i = 0; i < length; i++) {
            char res = string.charAt(i);
            if (!Character.isDigit(res)) return false;
        }
        return true;
    }

    public boolean areEqualSize(String gamma1, String gamma2) {
        int sizeGamma1 = getGammaAddresses(gamma1).split("\n").length;
        int sizeGamma2 = getGammaAddresses(gamma2).split("\n").length;

        return sizeGamma1 == sizeGamma2;
    }

    /**
     * Returns true if the interval is a line on the spreadsheet.
     */
    public boolean intervalIsLine(String gamma) {
        if (!gamma.contains(":")) return true;
        else {
            String[] adresses = gamma.split(":");
            String[] coordinatesFirstAdress = adresses[0].split(";");
            String[] coordinatesSecondtAdress = adresses[1].split(";");

            return Integer.parseInt(coordinatesFirstAdress[0]) - Integer.parseInt(coordinatesSecondtAdress[0]) == 0;
        }
    }

    /**
     * Insert specified content in the specified address.
     *
     * @param rangeSpecification is the address of the content.
     * @param contentSpecification is the content itself.
     * @throws UnrecognizedEntryException if the content is not recognized.
     */
    public void insertContents(String rangeSpecification, String contentSpecification) throws UnrecognizedEntryException {
        
        String firstLetter = "";
        CellContent content = null;
        
        /* Check if we can access the first character and get it, if possible. */
        if (contentSpecification != "")
            firstLetter = String.valueOf(contentSpecification.charAt(0));

        /* If we cannot access the first character, the content is null. */
        if (firstLetter.equals("")) content = null;

        /* If the first character is a ', it is a string. */
        else if (firstLetter.equals("'")) {
            content = new LiteralString(contentSpecification);
        }

        /* If the first character is a '=', it is either a function or a reference. */
        else if (firstLetter.equals("=")) {
            /* If the content contains a '(', it is a function. */
            if (contentSpecification.contains("(")) {

                String functionName = contentSpecification.substring(1, contentSpecification.indexOf("("));
                String functionContent = contentSpecification.substring(contentSpecification.indexOf("(") + 1, contentSpecification.indexOf(")"));
                
                CellContent binaryReferenceLeft = null;
                CellContent binaryReferenceRight = null;

                String firstAddressContent = "";
                String secondAddressContent = "";
                ArrayList<CellContent> intervalAddresses = new ArrayList<CellContent>();

                /* If the function content contains a ':', it is an interval function. */
                if(functionContent.contains(":")) {
                    String[] parts = functionContent.split(":");
                    firstAddressContent = parts[0];
                    secondAddressContent = parts[1];

                    String[] addresses = getGammaAddresses(functionContent).split("\n");
                    for (int i = 0; i < addresses.length; i++) {
                        CellContent cellContent = new Reference(_spreadsheetStorage.getCell(addresses[i]), addresses[i]);
                        intervalAddresses.add(cellContent);
                    }
                }
                /* If the function content does not contain a ':', it is a binary function. */
                else {
                    String[] parts = functionContent.split(",");
                    firstAddressContent = parts[0];
                    secondAddressContent = parts[1];
                    
                    /* If the address is not a digit, it is a reference. */
                    if(!isDigit(firstAddressContent)) {
                        binaryReferenceLeft = new Reference(_spreadsheetStorage.getCell(firstAddressContent), firstAddressContent);
                    }
                    else
                        binaryReferenceLeft = new LiteralInt(firstAddressContent);

                    /* If the address is not a digit, it is a reference. */
                    if(!isDigit(secondAddressContent)) 
                        binaryReferenceRight = new Reference(_spreadsheetStorage.getCell(secondAddressContent), secondAddressContent);
                    else
                        binaryReferenceRight = new LiteralInt(secondAddressContent);
                }
                
                /* Identifies the function name in the switch, sending the values/addresses to the right constructor. */
                switch(functionName) {
                    case "ADD":
                        content = new AddFunction(binaryReferenceLeft, binaryReferenceRight, firstAddressContent, secondAddressContent);
                        break;
                    case "SUB":
                        content = new SubFunction(binaryReferenceLeft, binaryReferenceRight, firstAddressContent, secondAddressContent);
                        break;
                    case "MUL":
                        content = new MulFunction(binaryReferenceLeft, binaryReferenceRight, firstAddressContent, secondAddressContent);
                        break;
                    case "DIV":
                        content = new DivFunction(binaryReferenceLeft, binaryReferenceRight, firstAddressContent, secondAddressContent);
                        break;
                    case "AVERAGE":
                        content = new AverageFunction(intervalAddresses, firstAddressContent, secondAddressContent);
                        break;
                    case "PRODUCT":
                        content = new ProductFunction(intervalAddresses, firstAddressContent, secondAddressContent);
                        break;
                    case "COALESCE":
                        content = new CoalesceFunction(intervalAddresses, firstAddressContent, secondAddressContent);
                        break;
                    case "CONCAT":
                        content = new ConcatFunction(intervalAddresses, firstAddressContent, secondAddressContent);
                        break;
                    default:
                        throw new UnrecognizedEntryException(functionName);
                }
            }
            /* If it does not contain a '(', it is a reference. */
            else {
                String referencedAddress = contentSpecification.substring(1, contentSpecification.length());
                content = new Reference(_spreadsheetStorage.getCell(referencedAddress), referencedAddress);
            }
        }
        /* Excluding the previous options, we know it is a number. */
        else {
            content = new LiteralInt(contentSpecification);
        }

        Cell workignCell = _spreadsheetStorage.getCell(rangeSpecification);

        Visitor visitOldContent = new AssignObserver(false, workignCell);
        Visitor visitNewContent = new AssignObserver(true, workignCell);

        if (workignCell.getCellContent() != null)
            workignCell.getCellContent().accept(visitOldContent);
        if (content != null)
            content.accept(visitNewContent);

        /* Creates and inserts the cell with the content in the spreadsheet memory. */
        Cell cell = new Cell(content);
        _spreadsheetStorage.putCell(rangeSpecification, cell);
    }

    /**
     * Returns the content of the specified gamma.
     *
     * @param gamma is the address(es) of the content.
     * @throws UnrecognizedEntryException if the address is not recognized.
     */
    public String show(String gamma) throws UnrecognizedEntryException {

        /* If gamma is not well defined, throw an exception. */
        if(!wellDefinedGamma(gamma)) throw new UnrecognizedEntryException(gamma);

        String finalString = "";
        String[] adresses = getGammaAddresses(gamma).split("\n");
        for (int i = 0; i < adresses.length; i++) {
            finalString += (adresses[i]) + "|" + (showOneAddress(adresses[i])) + "\n";
        }
        return finalString.substring(0, finalString.length() - 1);
    }

    /**
     * Returns the content of the specified address.
     */
    public String showOneAddress(String gamma) {

        Cell cell = _spreadsheetStorage.getCell(gamma);

        if (cell == null) return "";
        CellContent content = cell.getCellContent();
        if (content == null) return "";
        
        return obtainValue(gamma) + content.toString();
    }

    /**
     * Returns only the value of the specified address.
     */
    public String obtainValue(String gamma) {

        Cell cell = _spreadsheetStorage.getCell(gamma);

        if (cell == null) return "";
        CellContent content = cell.getCellContent();
        if (content == null) return "";

        return content.getResult();

    }


    public String searchFunctions(String functionName) {
        Search search = new FunctionSearch();
        return search.search(functionName, _lines, _columns, this);
    }


    public String searchValues(String value) {
        Search search = new ValueSearch();
        return search.search(value, _lines, _columns, this);
    }

    
    public void paste(String gamma) throws UnrecognizedEntryException {
        if (!wellDefinedGamma(gamma)) throw new UnrecognizedEntryException(gamma);

        /* If there is nothing to paste do nothing */
        if (_cutBufferAddress.equals("")) return;

        if (!hasChanged()) setChanged(true);
        
        /* If the gamma that is being paste is an interval and it´s not the same
        * size as the interval on the cutBuffer do nothing */
        if (gamma.contains(":") && !areEqualSize(gamma, _cutBufferAddress)) return;

        /* If the gamma is only one adress, paste until the spreadsheet reach
        * its max range or until the cutBuffer ends */
        if(!gamma.contains(":")) {
            Boolean isLine = intervalIsLine(_cutBufferAddress);

            String[] addresses = getGammaAddresses(_cutBufferAddress).split("\n");

            String address;
            int line = Integer.parseInt(gamma.split(";")[0]);
            int column = Integer.parseInt(gamma.split(";")[1]);

            for (int i = 0; i < addresses.length; i++) {
                if (isLine) address = line + ";" + (column + i);
                else address = (line + i) + ";" + column;
                if (!onSpreadsheet(address)) return;
                Cell cell = _cutBufferCells.get(i);
                _spreadsheetStorage.putCell(address, cell);
            }
        }
        /* If it is an interval of the same size as the cutBuffer */
        else {
            /* Verify if both are the same geometry type */
            if (intervalIsLine(gamma) == intervalIsLine(_cutBufferAddress)) {
                String[] adressesGamma = getGammaAddresses(gamma).split("\n");
                String[] addressesCutBuffer = getGammaAddresses(_cutBufferAddress).split("\n");

                for (int i = 0; i < addressesCutBuffer.length; i++) {
                    Cell cell = _cutBufferCells.get(i);
                    _spreadsheetStorage.putCell(adressesGamma[i], cell);
                }
            }
        }
    }


    public String showCutBuffer() {
        if (_cutBufferAddress.equals("")) return "";

        String res = "";
        String[] addresses = getGammaAddresses(_cutBufferAddress).split("\n");
        String firstAddressLine = "";
        String secondAddressLine = "";
        String contentStr;

        if (addresses.length > 1) {
            firstAddressLine = _cutBufferAddress.split(":")[0].split(";")[0];
            secondAddressLine = _cutBufferAddress.split(":")[1].split(";")[0];
        }

        Boolean equalLine = firstAddressLine.equals(secondAddressLine);

        for (int i = 0; i < addresses.length; i++) {

            if(equalLine) res += "1;" + (i+1) + "|";
            else res += (i+1) + ";1" + "|";
            if(_cutBufferCells.get(i) == null) contentStr = "";
            else if(_cutBufferCells.get(i).getCellContent() == null) contentStr = "";
            else contentStr = _cutBufferCells.get(i).getCellContent().toString();

            res += _cutBufferResults.get(i) + contentStr + "\n";
        }
        return res.substring(0, res.length() - 1);
    }


    public void cut(String gamma) throws UnrecognizedEntryException {
        if (!wellDefinedGamma(gamma)) throw new UnrecognizedEntryException(gamma);

        this.copy(gamma);
        this.delete(gamma);
    }


    public void copy(String gamma) throws UnrecognizedEntryException {
        if (!wellDefinedGamma(gamma)) throw new UnrecognizedEntryException(gamma);
        if (gamma.equals("")) return;

        String[] addresses = getGammaAddresses(gamma).split("\n");
        for (int i = 0; i < addresses.length; i++) {

            Cell cell = _spreadsheetStorage.getCell(addresses[i]);
            CellContent cellContent = cell.getCellContent();
            Cell independentCell = new Cell(cellContent);

            _cutBufferCells.add(independentCell);
            _cutBufferResults.add(obtainValue(addresses[i]));
        }
        _cutBufferAddress = gamma;
    }


    public void insert(String gamma, String content) throws UnrecognizedEntryException {
        if(!wellDefinedGamma(gamma)) throw new UnrecognizedEntryException(gamma);
        if (!hasChanged()) setChanged(true);

        if(!gamma.contains(":")) insertContents(gamma, content);

        else {
            String[] adresses = getGammaAddresses(gamma).split("\n");
            for (int i = 0; i < adresses.length; i++)
                try {
                    insertContents(adresses[i], content);
                } catch (UnrecognizedEntryException e) {
                    throw new UnrecognizedEntryException(e.getEntrySpecification());
                }
        }
    }


    public void delete(String gamma) throws UnrecognizedEntryException {
        if (!wellDefinedGamma(gamma)) throw new UnrecognizedEntryException(gamma);
        if (gamma.equals("")) return;

        if (!hasChanged()) setChanged(true);

        Cell cell = new Cell();
        if(!gamma.contains(":")) _spreadsheetStorage.putCell(gamma, cell);

        else {
            String[] adresses = getGammaAddresses(gamma).split("\n");
            for(int i = 0; i < adresses.length; i++) 
                _spreadsheetStorage.putCell(adresses[i], cell);
        }
    }

    /**
     * Returns a string with all the addresses on the specified gamma, sorted
     *  by line and column, separated by a new line.
     */
    public String getGammaAddresses(String gamma) {

        if(!gamma.contains(":")) return gamma;

        String[] parts = gamma.split(":");
        String[] firstAddressCoordinates = parts[0].split(";");
        String[] secondAddressCoordinates = parts[1].split(";");
        int aux;
        String res = "";
        int lineFirstAddress = Integer.parseInt(firstAddressCoordinates[0]);
        int columnFirstAddress = Integer.parseInt(firstAddressCoordinates[1]);

        int lineSecondAddress = Integer.parseInt(secondAddressCoordinates[0]);
        int columnSecondAddress = Integer.parseInt(secondAddressCoordinates[1]);

        //If the line of the first address is bigger than the line of the second address, swap them.
        if(lineFirstAddress > lineSecondAddress) {
            aux = lineSecondAddress;
            lineSecondAddress = lineFirstAddress;
            lineFirstAddress = aux;
        }

        //If the column of the first address is bigger than the column of the second address, swap them.
        if(columnFirstAddress > columnSecondAddress) {
            aux = columnSecondAddress;
            columnSecondAddress = columnFirstAddress;
            columnFirstAddress = aux;
        }

        for (int i = lineFirstAddress; i <= lineSecondAddress; i++) {
            for (int j = columnFirstAddress; j <= columnSecondAddress; j++) {
                res += i + ";" + j + "\n";
            }
        }
        return res.substring(0, res.length() - 1);
    }
    
}