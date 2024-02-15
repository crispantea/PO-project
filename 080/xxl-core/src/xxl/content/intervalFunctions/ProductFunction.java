package xxl.content.intervalFunctions;

import java.util.ArrayList;

import xxl.content.CellContent;
import xxl.visitors.Visitor;

public class ProductFunction extends IntervalFunction {

    private String _firstAddress;
    private String _secondAddress;

    public ProductFunction(ArrayList<CellContent> content, String firstAddress, String secondAddress) {
        super(content);
        _firstAddress = firstAddress;
        _secondAddress = secondAddress;
    }

    @Override
    public String obtainResult() {
        ArrayList<CellContent> values = super.getIntervalAddresses();
        int result = 1;
        for (int i = 0; i < values.size(); i++) {
            if(values.get(i) == null) 
                return "#VALUE";
                
            values.get(i).updateResult();
            String addressRes = values.get(i).getResult();
            if(addressRes.equals("") || addressRes.contains("'") || addressRes.equals("#VALUE")) 
                return "#VALUE";
            result *= Integer.parseInt(addressRes);
        }
        return "" + result;
    }

    @Override
    public String toString() {
        return "=PRODUCT(" + _firstAddress + ":" + _secondAddress + ")";
    }

    @Override
    public String accept(Visitor visitor) {
        return visitor.visit(this);
    }

}
