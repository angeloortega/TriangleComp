package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

public class PackageDeclaration extends Declaration {

  public PackageDeclaration (Identifier idAST, Declaration decAST,
                       SourcePosition thePosition) {
    super (thePosition);
    ID = idAST;
    DEC = decAST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitPackDeclaration(this, o);
  }

  public Declaration DEC;
  public Identifier ID;
}