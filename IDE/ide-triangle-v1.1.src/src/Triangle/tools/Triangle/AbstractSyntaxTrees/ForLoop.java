//Used to represent the AST of all the for type loops

package Triangle.tools.Triangle.AbstractSyntaxTrees;

import Triangle.tools.Triangle.SyntacticAnalyzer.SourcePosition;

public abstract class ForLoop extends AST {

  public ForLoop (SourcePosition thePosition) {
    super (thePosition);
  }
}