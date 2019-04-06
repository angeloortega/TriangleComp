//Used to represent the expression in a do loop with while termination

package Triangle.tools.Triangle.AbstractSyntaxTrees;

import Triangle.tools.Triangle.SyntacticAnalyzer.SourcePosition;

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