
package Triangle.tools.Triangle.AbstractSyntaxTrees;

import Triangle.tools.Triangle.SyntacticAnalyzer.SourcePosition;

public class ProcFuncs extends ProcFunc {

  public ProcFuncs (ProcFunc pf1AST, ProcFunc pf2AST, SourcePosition thePosition) {
    super (thePosition);
    PF1 = pf1AST;
    PF2 = pf2AST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitProcFuncs(this, o);
  }

  public ProcFunc PF1, PF2;
}