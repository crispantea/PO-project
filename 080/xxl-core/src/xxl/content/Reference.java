package xxl.content;

import xxl.structures.Cell;
import xxl.visitors.Visitor;

public class Reference extends CellContent {
    
    private Cell _reference;
    private String _referencedString;

    public Reference(Cell reference, String referencedString) {
        _reference = reference;
        _referencedString = referencedString;
        updateResult();
    }

    @Override
    public String obtainResult() {
        if(_reference.getCellContent() == null) return "#VALUE";
        return _reference.getCellContent().obtainResult();
    }

    public Cell getReference() {
        return _reference;
    }

    @Override
    public String toString() {
        return "=" + _referencedString;
    }

    @Override
    public String accept(Visitor visitor){
        return visitor.visit(this);
    }

}
