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

//Used to represent one Type denoter in the same expression.

package Triangle.tools.Triangle.AbstractSyntaxTrees;

import Triangle.tools.Triangle.SyntacticAnalyzer.SourcePosition;

public class SingleRecordTypeDenoter extends RecordTypeDenoter {

  public SingleRecordTypeDenoter (Identifier idAST, TypeDenoter tdAST,
                    SourcePosition thePosition) {
    super (thePosition);
    ID = idAST;
    TD = tdAST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitSingleRecordTypeDenoter(this, o);
  }

  public boolean equals (Object obj) {
    if (obj != null && obj instanceof ErrorTypeDenoter)
      return true;
    else if (obj != null && obj instanceof SingleRecordTypeDenoter)
      return this.ID.equals(((SingleRecordTypeDenoter) obj).ID) &&
        this.TD.equals(((SingleRecordTypeDenoter) obj).TD);
    else
      return false;
  }

  public Identifier ID;
  public TypeDenoter TD;

}
