
package Triangle.tools.Triangle.AbstractSyntaxTrees;

import Triangle.tools.Triangle.SyntacticAnalyzer.SourcePosition;

public class SingleDeclarationCommand extends Command {

  public SingleDeclarationCommand (VName vnAST, Expression eAST, SourcePosition thePosition) {
    super (thePosition);
    VN = vnAST;
    EXP = eAST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitSingleDeclarationCommand(this, o);
  }

  public VName VN;
  public Expression EXP;
}