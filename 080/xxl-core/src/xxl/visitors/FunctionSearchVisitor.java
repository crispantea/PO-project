package xxl.visitors;

import xxl.content.*;
import xxl.content.binaryFunctions.*;
import xxl.content.intervalFunctions.*;

public class FunctionSearchVisitor implements Visitor {
    
    private String _argument;

    public FunctionSearchVisitor(String argument) {
        _argument = argument;
    }

    public String visit(LiteralInt literalInts) {
        return "";
    }

    public String visit(LiteralString literalString) {
        return "";
    }
    
    public String visit(Reference reference) {
        return "";
    }

    public String compareFunctionName(CellContent cellContent) {
        return cellContent.getResult() + cellContent.toString();
    }

    public String visit(AddFunction addFunction) {
        if(addFunction.toString().contains(_argument)) {
            return compareFunctionName(addFunction);
        }
        return "";
    }

    public String visit(SubFunction subFunction) {
        if(subFunction.toString().contains(_argument)) {
            return compareFunctionName(subFunction);
        }
        return "";
    }

    public String visit(DivFunction divFunction) {
        if(divFunction.toString().contains(_argument)) {
            return compareFunctionName(divFunction);
        }
        return "";
    }

    public String visit(MulFunction mulFunction) {
        if(mulFunction.toString().contains(_argument)) {
            return compareFunctionName(mulFunction);
        }
        return "";
    }

    public String visit(AverageFunction averageFunction) {
        if(averageFunction.toString().contains(_argument)) {
            return compareFunctionName(averageFunction);
        }
        return "";
    }

    public String visit(ProductFunction productFunction) {
        if(productFunction.toString().contains(_argument)) {
            return compareFunctionName(productFunction);
        }
        return "";
    }

    public String visit(CoalesceFunction coalesceFunction) {
        if(coalesceFunction.toString().contains(_argument)) {
            return compareFunctionName(coalesceFunction);
        }
        return "";
    }
    
    public String visit(ConcatFunction concatFunction) {
        if(concatFunction.toString().contains(_argument)) {
            return compareFunctionName(concatFunction);
        }
        return "";
    }

    public String visit(CellContent intervalFunction) {
        return "";
    }

}