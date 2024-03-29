package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

public class SequentialPackageDeclaration extends PackageDeclaration {

  public SequentialPackageDeclaration (PackageDeclaration d1AST, PackageDeclaration d2AST,
                       SourcePosition thePosition) {
    super (thePosition);
    D1 = d1AST;
    D2 = d2AST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitSequentialPackageDeclaration(this, o);
  }

  public PackageDeclaration D1, D2;
}