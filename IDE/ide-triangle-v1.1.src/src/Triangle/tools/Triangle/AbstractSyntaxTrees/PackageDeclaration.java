package Triangle.tools.Triangle.AbstractSyntaxTrees;

import Triangle.tools.Triangle.SyntacticAnalyzer.SourcePosition;

public class PackageDeclaration extends Declaration {

    public PackageDeclaration (SourcePosition thePosition) {
    super (thePosition);
  }
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