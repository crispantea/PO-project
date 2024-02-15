package xxl.structures;

import java.io.Serializable;
import java.util.TreeMap;

public class TreeMapStorage implements Serializable,StorageStructure {
 
    private TreeMap<String, Cell> _storage = new TreeMap<String, Cell>();

    public Cell getCell(String address) {
        return _storage.get(address);
    }

    public void putCell(String address, Cell cell) {
        if (!onStorage(address)) {
            _storage.put(address, cell);
            return;
        }
        Cell storedCell = this.getCell(address);
        storedCell.setCellContent(cell.getCellContent());
    }

    public boolean onStorage(String address) {
        return _storage.containsKey(address);
    }
    
}
