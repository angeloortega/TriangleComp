/*
 * @(#)IdEntry.java                        2.1 2003/10/07
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

package Triangle.tools.Triangle.ContextualAnalyzer;

import Triangle.tools.Triangle.AbstractSyntaxTrees.Declaration;

public class IdEntry {

  protected String id;
  protected Declaration attr;
  protected int level;
  protected IdEntry previous;
  protected IdEntry next;

  IdEntry (String id, Declaration attr, int level, IdEntry previous) {
    this.id = id;
    this.attr = attr;
    this.level = level;
    this.previous = previous;
  }

    public void setNext(IdEntry next) {
        this.next = next;
    }
    
}
