package Triangle.tools.Triangle.AbstractSyntaxTrees;

import Triangle.tools.Triangle.SyntacticAnalyzer.SourcePosition;

public class ForLoopUntil extends ForLoop {

  public ForLoopUntil (Expression expAST, Command comAST,
               SourcePosition thePosition) {
    super (thePosition);
    EXP = expAST;
    COM = comAST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitForLoopUntil(this, o);
  }

  public Expression EXP;
  public Command COM;
}