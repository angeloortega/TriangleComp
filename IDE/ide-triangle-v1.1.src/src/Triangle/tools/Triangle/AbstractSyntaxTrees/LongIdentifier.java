/*
 * @(#)Identifier.java                        2.1 2003/10/07
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

package Triangle.tools.Triangle.AbstractSyntaxTrees;

import Triangle.tools.Triangle.SyntacticAnalyzer.SourcePosition;

public class LongIdentifier extends Identifier {

  public LongIdentifier (PackageIdentifier piAST, Identifier idAST,SourcePosition thePosition) {
    super (piAST.spelling + "$"+ idAST.spelling, thePosition);
    packageIdentifier = piAST;
    identifier = idAST;
  }
  
    public LongIdentifier (Identifier idAST,SourcePosition thePosition) {
    super ( idAST.spelling, thePosition);
    identifier = idAST;
  }

  public PackageIdentifier packageIdentifier;
  public Identifier identifier; // Either a Declaration or a FieldTypeDenoter

  @Override
  public Object visit (Visitor v, Object o) {
    return v.visitLongIdentifier(this, o);
  }

}
