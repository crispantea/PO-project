package xxl.search;

import xxl.Spreadsheet;
import xxl.content.*;
import xxl.visitors.*;

public class ValueSearch implements Search {

    public String search(String argument, int lines, int columns, Spreadsheet spreadsheet) {

        String address;
        CellContent content;
        String result = ""; 
        Visitor visitor = new ValueSearchVisitor(argument);
        
        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < columns; j++) {
                address = i + ";" + j;
                content = spreadsheet.getStorageStructure().getCell(address).getCellContent();
                if (content != null) {
                    String visitResult = content.accept(visitor);
                    if (!visitResult.equals("")) {
                        result += address + "|" + visitResult + "\n";
                    }
                }
            }
        }
        if (result.equals("")) return result;
        return result.substring(0, result.length() - 1);
    }
    
}