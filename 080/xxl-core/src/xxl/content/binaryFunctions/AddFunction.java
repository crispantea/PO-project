package xxl.content.binaryFunctions;

import xxl.content.CellContent;
import xxl.visitors.Visitor;

public class AddFunction extends BinaryFunction {

    private String _firstAddress;
    private String _secondAddress;

    public AddFunction(CellContent leftContent, CellContent rightContent, String firstAddress, String secondAddress) {
        super(leftContent, rightContent);
        _firstAddress = firstAddress;
        _secondAddress = secondAddress;
    }

    @Override
    public String obtainResult() {

        if(super.getLeftContent() == null || super.getRightContent() == null) return "#VALUE";

        getLeftContent().updateResult();
        getRightContent().updateResult();

        String firstValue = getLeftContent().getResult();
        String secondValue = getRightContent().getResult();

        if(firstValue.equals("") || firstValue.contains("'")
        || secondValue.equals("") || secondValue.contains("'")
        || firstValue.equals("#VALUE") || secondValue.equals("#VALUE")) return "#VALUE";

        return "" + (Integer.parseInt(firstValue) + Integer.parseInt(secondValue));
    }

    @Override
    public String toString() {
        return "=ADD(" + _firstAddress + "," + _secondAddress + ")";
    }

    @Override
    public String accept(Visitor visitor) {
        return visitor.visit(this);
    }

}
