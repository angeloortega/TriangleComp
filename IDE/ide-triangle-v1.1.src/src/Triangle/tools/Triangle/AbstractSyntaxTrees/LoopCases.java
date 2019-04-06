//Used to represent the AST of a loop statement

package Triangle.tools.Triangle.AbstractSyntaxTrees;

import Triangle.tools.Triangle.SyntacticAnalyzer.SourcePosition;

public abstract class LoopCases extends AST {

  public LoopCases (SourcePosition thePosition) {
    super (thePosition);
  }
}