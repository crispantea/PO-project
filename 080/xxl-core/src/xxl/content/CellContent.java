package xxl.content;

import java.io.Serializable;
import xxl.visitors.*;

public abstract class CellContent implements Serializable, TypeVisitor {

    private String _result = "";

    public String obtainResult() {
        return null;
    }

    public String getResult() {
        return _result;
    }

    public void updateResult() {
        _result = obtainResult();
    }

    @Override
    public String accept(Visitor visitor) {
        return visitor.visit(this);
    }

}
