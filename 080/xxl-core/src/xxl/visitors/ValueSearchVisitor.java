package xxl.visitors;

import xxl.content.*;
import xxl.content.binaryFunctions.*;
import xxl.content.intervalFunctions.*;

public class ValueSearchVisitor implements Visitor {
    
    private String _argument;

    public ValueSearchVisitor(String argument) {
        _argument = argument;
    }

    public String compareResult(CellContent cellContent) {
        if (cellContent.getResult().equals(_argument)) {
            return cellContent.getResult() + cellContent.toString();
        }
        return "";
    }

    public String visit(LiteralInt literalInts) {
        return compareResult(literalInts);
    }

    public String visit(LiteralString literalString) {
        return compareResult(literalString);
    }

    public String visit(Reference reference) {
        return compareResult(reference);
    }

    public String visit(AddFunction addFunction) {
        return compareResult(addFunction);
    }

    public String visit(SubFunction subFunction) {
        return compareResult(subFunction);
    }

    public String visit(DivFunction divFunction) {
        return compareResult(divFunction);
    }

    public String visit(MulFunction mulFunction) {
        return compareResult(mulFunction);
    }

    public String visit(AverageFunction averageFunction) {
        return compareResult(averageFunction);
    }

    public String visit(ProductFunction productFunction) {
        return compareResult(productFunction);
    }

    public String visit(CoalesceFunction coalesceFunction) {
        return compareResult(coalesceFunction);
    }
    
    public String visit(ConcatFunction concatFunction) {
        return compareResult(concatFunction);
    }

    public String visit(CellContent intervalFunction) {
        return "";
    }
    
}