//Used to represent the identifier and two expression of a for loop


package Triangle.tools.Triangle.AbstractSyntaxTrees;
import Triangle.tools.Triangle.SyntacticAnalyzer.SourcePosition;

public class LoopCasesFOR extends LoopCases {

  public LoopCasesFOR (ConstDeclaration dclAST, Expression exp2AST, ForLoop forAST,
               SourcePosition thePosition) {
    super (thePosition);
    DECL = dclAST;
    EXP2 = exp2AST;
    FOR = forAST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitLoopCasesFOR(this, o);
  }
  public ConstDeclaration DECL;
  public Expression EXP2;
  public ForLoop FOR;
}