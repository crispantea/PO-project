package xxl.content.intervalFunctions;

import java.util.ArrayList;

import xxl.content.CellContent;
import xxl.visitors.Visitor;

public class CoalesceFunction extends IntervalFunction {
    
    private String _firstAddress;
    private String _secondAddress;

    public CoalesceFunction(ArrayList<CellContent> content, String firstAddress, String secondAddress) {
        super(content);
        _firstAddress = firstAddress;
        _secondAddress = secondAddress;
    }

    @Override
    public String obtainResult() {
        ArrayList<CellContent> values = super.getIntervalAddresses();
        for (int i = 0; i < values.size(); i++) {
            if(values.get(i) != null) {
                values.get(i).updateResult();
                if(values.get(i).getResult().charAt(0) == '\'') return values.get(i).getResult();
            }
        }
        return "";
    }

    @Override
    public String toString() {
        return "=COALESCE(" + _firstAddress + ":" + _secondAddress + ")";
    }

    @Override
    public String accept(Visitor visitor) {
        return visitor.visit(this);
    }
    
}
