package xxl.content.binaryFunctions;

import xxl.content.CellContent;
import xxl.visitors.Visitor;

public abstract class BinaryFunction extends CellContent {

    private CellContent _leftContent;
    private CellContent _rightContent;

    public BinaryFunction(CellContent leftContent, CellContent rightContent) {
        _leftContent = leftContent;
        _rightContent = rightContent;
        updateResult();
    }

    public String obtainResult(){return null;}

    public CellContent getLeftContent() {
        return _leftContent;
    }

    public CellContent getRightContent() {
        return _rightContent;
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    public String accept(Visitor visitor){
        return visitor.visit(this);
    }

}
