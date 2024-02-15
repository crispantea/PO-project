package xxl.content;

import xxl.visitors.Visitor;

public class LiteralString extends CellContent {
    
    private String _value;

    public LiteralString(String value) {
        _value = value;
        updateResult();
    }

    @Override
    public String obtainResult() {
        return this.getString();
    }

    public String getString() {
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
