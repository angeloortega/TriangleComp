//Used to represent the AST of a do type loop.

package Triangle.tools.Triangle.AbstractSyntaxTrees;

import Triangle.tools.Triangle.SyntacticAnalyzer.SourcePosition;

public abstract class DoLoop extends AST {

  public DoLoop (SourcePosition thePosition) {
    super (thePosition);
  }
}