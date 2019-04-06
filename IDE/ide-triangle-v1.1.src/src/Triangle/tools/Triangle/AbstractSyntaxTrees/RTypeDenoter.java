/*
 * @(#)ArrayTypeDenoter.java                        2.1 2003/10/07
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
//Used to represent when there is a record as a type denoter in an expression

package Triangle.tools.Triangle.AbstractSyntaxTrees;

import Triangle.tools.Triangle.SyntacticAnalyzer.SourcePosition;

public class RTypeDenoter extends TypeDenoter {

  public RTypeDenoter (RecordTypeDenoter rtAST,
                    SourcePosition thePosition) {
    super (thePosition);
    REC = rtAST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitRTypeDenoter(this, o);
  }

  public RecordTypeDenoter REC;

    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
