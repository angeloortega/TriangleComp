import Triangle.SyntacticAnalyzer.SourcePosition;

public class ForLoopDo extends ForLoop {

  public ForLoopDo (Command comAST,
               SourcePosition thePosition) {
    super (thePosition);
    COM = comAST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitForLoopDo(this, o);
  }

  public Command COM;
}