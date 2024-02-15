package xxl.structures;

public interface StorageStructure {

    public Cell getCell(String address);
    public void putCell(String address, Cell cell);
    public boolean onStorage(String address);
}
