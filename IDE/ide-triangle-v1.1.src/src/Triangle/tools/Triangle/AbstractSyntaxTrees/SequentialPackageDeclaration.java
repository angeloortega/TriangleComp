//Used to represent two package declarations when they are in the same expression

package Triangle.tools.Triangle.AbstractSyntaxTrees;

import Triangle.tools.Triangle.SyntacticAnalyzer.SourcePosition;

public class SequentialPackageDeclaration extends PackageDeclaration {

  public SequentialPackageDeclaration (PackageDeclaration d1AST, PackageDeclaration d2AST,
                       SourcePosition thePosition) {
     super(thePosition);
    D1 = d1AST;
    D2 = d2AST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitSequentialPackageDeclaration(this, o);
  }

  public PackageDeclaration D1, D2;
}