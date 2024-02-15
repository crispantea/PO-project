package xxl.visitors;

import xxl.content.*;
import xxl.content.binaryFunctions.*;
import xxl.content.intervalFunctions.*;


public interface Visitor {

    public String visit(LiteralInt literalInts);
    public String visit(LiteralString literalString);
    public String visit(Reference reference);

    public String visit(AddFunction addFunction);
    public String visit(SubFunction subFunction);
    public String visit(DivFunction divFunction);
    public String visit(MulFunction mulFunction);

    public String visit(AverageFunction averageFunction);
    public String visit(ProductFunction productFunction);
    public String visit(CoalesceFunction coalesceFunction);
    public String visit(ConcatFunction concatFunction);

    public String visit(CellContent intervalFunction);

}
