package Triangle.tools.Triangle.AbstractSyntaxTrees;

import Triangle.tools.Triangle.SyntacticAnalyzer.SourcePosition;

public class LoopCasesUntil extends LoopCases {

  public LoopCasesUntil (Expression expAST, Command comAST,
               SourcePosition thePosition) {
    super (thePosition);
    EXP = expAST;
    COM = comAST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitLoopCasesUntil(this, o);
  }

  public Expression EXP;
  public Command COM;
}