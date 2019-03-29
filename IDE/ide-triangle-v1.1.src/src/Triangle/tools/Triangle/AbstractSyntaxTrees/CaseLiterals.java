package Triangle.tools.Triangle.AbstractSyntaxTrees;

import Triangle.tools.Triangle.SyntacticAnalyzer.SourcePosition;

public class CaseLiterals extends CaseLiteral {

  public CaseLiterals (CaseRange caseranAST, SourcePosition thePosition) {
    super (thePosition);
    CASERANGE = caseranAST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitCaseLiterals(this, o);
  }

  public CaseRange CASERANGE;
}