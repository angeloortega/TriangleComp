/*
 * @(#)VarDeclaration.java                        2.1 2003/10/07
 *
 * Copyright (C) 1999, 2003 D.A. Watt and D.F. Brown
 * Dept. of Computing Science, University of Glasgow, Glasgow G12 8QQ Scotland
 * and School of Computer and Math Sciences, The Robert Gordon University,
 * St. Andrew Street, Aberdeen AB25 1HG, Scotland.
 * All rights reserved.
 *
 * This software is provided free for educational use only. It may
 * not be used for commercial purposes without the prior written permission
 * of the authors.
 */

package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

public class ProcProcFunc extends ProcFunc {

  public ProcProcFunc (Identifier idAST, FormalParameterSequence fpsAST, Command comAST,
                         SourcePosition thePosition) {
    super (thePosition);
    ID = idAST;
    FPS = fpsAST;
    COM = comAST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitProcProcFunc(this, o);
  }

  public Identifier T;
  public FormalParameterSequence FPS;
  public Command COM;
}
