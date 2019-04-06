//Used to represent the expression in a do loop with until termination

package Triangle.tools.Triangle.AbstractSyntaxTrees;

import Triangle.tools.Triangle.SyntacticAnalyzer.SourcePosition;

public class DoLoopUntil extends DoLoop {

  public DoLoopUntil (Expression expAST,
               SourcePosition thePosition) {
    super (thePosition);
    EXP = expAST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitDoLoopUntil(this, o);
  }

  public Expression EXP;
}