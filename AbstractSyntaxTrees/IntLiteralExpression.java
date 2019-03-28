package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

public class IntLiteralExpression extends Expression {

  public IntLiteralExpression (SecondaryExpression secAST, SourcePosition thePosition) {
    super (thePosition);
    SECEXP = secAST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitIntLiteralExpression(this, o);
  }

  public SecondaryExpression SECEXP;
}