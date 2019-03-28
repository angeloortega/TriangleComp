import Triangle.SyntacticAnalyzer.SourcePosition;

public class LoopCasesDo extends LoopCases {

  public LoopCasesDo (Command comAST, doloopAST DoLoop,
               SourcePosition thePosition) {
    super (thePosition);
    DO = doloopAST;
    COM = comAST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitLoopCasesDo(this, o);
  }

  public DoLoop DO;
  public Command COM;
}