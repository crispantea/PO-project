package xxl.search;

import xxl.Spreadsheet;

public interface Search {
    
    public String search(String value, int lines, int columns, Spreadsheet spreadsheet);
}