
import Triangle.SyntacticAnalyzer.SourcePosition;

public class DoLoopWhile extends DoLoop {

  public DoLoopWhile (Expression expAST,
               SourcePosition thePosition) {
    super (thePosition);
    EXP = expAST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitDoLoopWhile(this, o);
  }

  public Expression EXP;
}