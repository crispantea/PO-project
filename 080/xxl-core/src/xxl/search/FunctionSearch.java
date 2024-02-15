package xxl.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import xxl.Spreadsheet;
import xxl.content.*;
import xxl.visitors.*;

public class FunctionSearch implements Search {

    public String search(String argument, int lines, int columns, Spreadsheet spreadsheet) {

        String address;
        CellContent content;
        ArrayList<String> resultArray= new ArrayList<>();
        String result = "";
        Visitor visitor = new FunctionSearchVisitor(argument);
        
        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < columns; j++) {
                address = i + ";" + j;
                content = spreadsheet.getStorageStructure().getCell(address).getCellContent();
                if (content != null) {
                    String visitResult = content.accept(visitor);
                    if (!visitResult.equals("")) {
                        resultArray.add(address + "|" + visitResult + "\n");
                    }
                }
            }
        }

        if (resultArray.size() == 0) 
            return result;

        Comparator<String> functionNameComparator = new Comparator<String>() {
            @Override
            public int compare(String str1, String str2) {
                String nome1 = str1.split("=")[1];
                String nome2 = str2.split("=")[1];
            
                return nome1.compareTo(nome2);
            }
        };
        
        Collections.sort(resultArray, functionNameComparator);
        for (String str : resultArray)
            result += str;

        return result.substring(0, result.length() - 1);
    }

}
