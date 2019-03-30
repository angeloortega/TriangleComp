package Triangle.tools.Triangle.AbstractSyntaxTrees;

import Triangle.tools.Triangle.SyntacticAnalyzer.SourcePosition;

public class IntLiteralExpression extends Expression {

  public IntLiteralExpression ( BinaryExpression secAST, SourcePosition thePosition) {
    super (thePosition);
    SECEXP = secAST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitIntLiteralExpression(this, o);
  }

  public BinaryExpression SECEXP;
}