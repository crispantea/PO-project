package xxl.content.intervalFunctions;

import java.util.ArrayList;

import xxl.content.CellContent;
import xxl.visitors.Visitor;

public class ConcatFunction extends IntervalFunction {
    
    private String _firstAddress;
    private String _secondAddress;

    public ConcatFunction(ArrayList<CellContent> content, String firstAddress, String secondAddress) {
        super(content);
        _firstAddress = firstAddress;
        _secondAddress = secondAddress;
    }

    @Override
    public String obtainResult() {
        ArrayList<CellContent> values = super.getIntervalAddresses();
        String res = "'";
        Boolean hasString = false;
        for (int i = 0; i < values.size(); i++) {
            values.get(i).updateResult();
            String addressRes = values.get(i).getResult();
            if(!addressRes.equals("")) {
                if (addressRes.charAt(0) == '\'') {
                    res += addressRes.substring(1, addressRes.length());
                    hasString = true;
                }
            }
        }
        if (hasString) return res;
        return "";
    }

    @Override
    public String toString() {
        return "=CONCAT(" + _firstAddress + ":" + _secondAddress + ")";
    }

    @Override
    public String accept(Visitor visitor) {
        return visitor.visit(this);
    }
    
}
