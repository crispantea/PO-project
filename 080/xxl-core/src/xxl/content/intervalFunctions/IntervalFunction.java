package xxl.content.intervalFunctions;

import java.util.ArrayList;

import xxl.content.CellContent;
import xxl.visitors.Visitor;

public abstract class IntervalFunction extends CellContent {

    private ArrayList<CellContent> _intervalAddresses;


    public IntervalFunction(ArrayList<CellContent> content) {
        _intervalAddresses = content;
        updateResult();
    }

    public ArrayList<CellContent> getIntervalAddresses(){
        return _intervalAddresses;
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
