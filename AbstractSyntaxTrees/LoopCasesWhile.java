
import Triangle.SyntacticAnalyzer.SourcePosition;

public class LoopCasesWhile extends LoopCases {

  public LoopCasesWhile (Expression expAST, Command comAST,
               SourcePosition thePosition) {
    super (thePosition);
    EXP = expAST;
    COM = comAST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitLoopCasesWhile(this, o);
  }

  public Expression EXP;
  public Command COM;
}