//Used to represent the command in an else statement

package Triangle.tools.Triangle.AbstractSyntaxTrees;

import Triangle.tools.Triangle.SyntacticAnalyzer.SourcePosition;

public class ElseCase extends Case {

  public ElseCase (Command comAST,
               SourcePosition thePosition) {
    super (thePosition);
    COM = comAST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitElseCase(this, o);
  }

  public Command COM;
}