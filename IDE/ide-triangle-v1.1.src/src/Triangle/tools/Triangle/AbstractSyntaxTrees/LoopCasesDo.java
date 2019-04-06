//Used to represent the command in a do loop

package Triangle.tools.Triangle.AbstractSyntaxTrees;

import Triangle.tools.Triangle.SyntacticAnalyzer.SourcePosition;

public class LoopCasesDo extends LoopCases {

  public LoopCasesDo (Command comAST, DoLoop doloopAST,
               SourcePosition thePosition) {
    super (thePosition);
    DO = doloopAST;
    COM = comAST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitLoopCasesDo(this, o);
  }

  public DoLoop DO;
  public Command COM;
}