
package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

public class SecExpression extends Expression {

  public SecExpression (SecondaryExpression secAST, SourcePosition thePosition) {
    super (thePosition);
    SECEXP = secAST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitSecExpression(this, o);
  }

  public SecondaryExpression SECEXP;
}