
package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

public class SequentialProcFuncs extends ProcFunc {

  public SequentialProcFuncs (ProcFunc pf1AST, ProcFunc pf2AST, SourcePosition thePosition) {
    super (thePosition);
    PF1 = pf1AST;
    PF2 = pf2AST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitSequentialProcFuncs(this, o);
  }

  public ProcFunc PF1, PF2;
}