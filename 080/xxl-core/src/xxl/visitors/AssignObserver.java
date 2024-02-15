
package xxl.visitors;

import java.util.ArrayList;

import xxl.content.*;
import xxl.content.binaryFunctions.*;
import xxl.content.intervalFunctions.*;
import xxl.structures.*;

public class AssignObserver implements Visitor {

    private Boolean _isNewContent;
    private Cell _workingCell;

    public AssignObserver(Boolean isNewContent, Cell workingCell) {
        _isNewContent = isNewContent;
        _workingCell = workingCell;
    }

    @Override
    public String visit(LiteralInt literalInts) {
        return "";
    }

    @Override
    public String visit(LiteralString literalString) {
        return "";
    }

    @Override
    public String visit(Reference reference) {
        if (_isNewContent)
            reference.getReference().addObserver(_workingCell);
        else 
            reference.getReference().removeObserver(_workingCell);
        return "";
    }

    public String binaryAux(BinaryFunction binaryFunction) {
        binaryFunction.getLeftContent().accept(this);
        binaryFunction.getRightContent().accept(this);
        return "";
    }

    public String intervalAux(IntervalFunction intervalFunction) {
        ArrayList<CellContent> addresses = intervalFunction.getIntervalAddresses();
        for (int i = 0; i < addresses.size(); i++) {
            addresses.get(i).accept(this);
        }
        return "";
    }

    @Override
    public String visit(AddFunction addFunction) {
        binaryAux(addFunction);
        return "";
    }

    @Override
    public String visit(MulFunction mulFunction) {
        binaryAux(mulFunction);
        return "";
    }

    @Override
    public String visit(SubFunction subFunction) {
        binaryAux(subFunction);
        return "";
    }

    @Override
    public String visit(DivFunction divFunction) {
        binaryAux(divFunction);
        return "";
    }

    @Override
    public String visit(AverageFunction averageFunction) {
        intervalAux(averageFunction);
        return "";
    }

    @Override
    public String visit(ProductFunction productFunction) {
        intervalAux(productFunction);
        return "";
    }

    @Override
    public String visit(CoalesceFunction coalesceFunction) {
        intervalAux(coalesceFunction);
        return "";
    }

    @Override
    public String visit(ConcatFunction concatFunction) {
        intervalAux(concatFunction);
        return "";
    }

    @Override
    public String visit(CellContent intervalFunction) {
        return "";
    }
    
}
