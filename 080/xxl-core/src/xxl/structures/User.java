package xxl.structures;

import xxl.Spreadsheet;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

public class User implements Serializable {

    private String _userName;
    private Map<String, Spreadsheet> _spreadsheets;

    public User(String name) {
        _userName = name;
        _spreadsheets = new TreeMap<String, Spreadsheet>();
    }

    public User() {
        _userName = "root";
        _spreadsheets = new TreeMap<String, Spreadsheet>();
    }

}

