package xxl.content;

import xxl.visitors.Visitor;

public class LiteralInt extends CellContent {
    
    private String _value;

    public LiteralInt(String value) {
        _value = value;
        updateResult();
    }

    @Override
    public String obtainResult() {
        return this.getInt();
    }

    public String getInt(){
        return _value;
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
