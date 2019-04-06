//Used to represent a two literals in a range statement that
//used a double dot to set the range.


package Triangle.tools.Triangle.AbstractSyntaxTrees;

import Triangle.tools.Triangle.SyntacticAnalyzer.SourcePosition;

public class CaseRangeCase extends CaseRange {

  public CaseRangeCase (CaseLiteral caselitAST, CaseLiteral caselit2AST, SourcePosition thePosition) {
    super (thePosition);
    CASELIT = caselitAST;
    CASELIT2 = caselit2AST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitCaseRangeCase(this, o);
  }

  public CaseLiteral CASELIT, CASELIT2;
}