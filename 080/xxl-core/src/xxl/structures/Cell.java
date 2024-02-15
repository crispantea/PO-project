package xxl.structures;

import xxl.content.*;
import xxl.observers.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cell implements Serializable, Subject, Observer {
    
    private CellContent _content = null;
    private List<Observer> _observers = new ArrayList<>();

    public Cell() {  

    }

    public Cell(CellContent content) {
        _content = content;
    }

    public void setCellContent(CellContent cellContent) {
        _content = cellContent;
        update();
    }

    public CellContent getCellContent() {
        return _content;
    }
    
    @Override
    public void addObserver(Observer observer) {
        _observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        _observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : _observers) {
            observer.update();
        }
    }

    @Override
    public void update() {
        if (_content != null) _content.updateResult();
        notifyObservers();
    }
    
}
