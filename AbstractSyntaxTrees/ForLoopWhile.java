
import Triangle.SyntacticAnalyzer.SourcePosition;

public class ForLoopWhile extends ForLoop {

  public ForLoopWhile (Expression expAST, Command comAST,
               SourcePosition thePosition) {
    super (thePosition);
    EXP = expAST;
    COM = comAST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitForLoopWhile(this, o);
  }

  public Expression EXP;
  public Command COM;
}