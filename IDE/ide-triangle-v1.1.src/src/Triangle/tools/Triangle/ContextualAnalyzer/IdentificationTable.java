/*
 * @(#)IdentificationTable.java                2.1 2003/10/07
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
import java.util.ArrayList;
import java.util.HashMap;

public final class IdentificationTable {

  private int level;
  private IdEntry latest;
  private boolean parallel = false;
  private HashMap<String, Declaration> declarations;
  public IdentificationTable () {
    level = 0;
    latest = null;
  }
  
  public static IdentificationTable copyTable(IdentificationTable id){
      IdentificationTable idTable = new IdentificationTable();
      boolean searching = true;
      IdEntry entry = id.getLatest();
      while (searching) {
      if (entry == null)
        searching = false;
      else{
       idTable.enter(entry.id, entry.attr); 
       entry = entry.previous;
      }
    }
    return idTable;
  }

  // Opens a new level in the identification table, 1 higher than the
  // current topmost level.

  public void openScope () {

    level ++;
  }

  // Closes the topmost level in the identification table, discarding
  // all entries belonging to that level.

  public void closeScope () {

    IdEntry entry, local;

    // Presumably, idTable.level > 0.
    entry = this.latest;
    while (entry.level == this.level) {
      local = entry;
      entry = local.previous;
    }
    this.level--;
    this.latest = entry;
  }

  // Makes a new entry in the identification table for the given identifier
  // and attribute. The new entry belongs to the current level.
  // duplicated is set to to true iff there is already an entry for the
  // same identifier at the current level.
  public void openParallel(){
      declarations = new HashMap<>();
      parallel = true;
  }
  public void closeParallel(){
      IdEntry entry = this.latest;
      for(String  id : declarations.keySet()){
        entry = new IdEntry(id,declarations.get(id), this.level, this.latest);
        this.latest = entry;
        if(entry.previous != null)
            entry.previous.next = entry;
      }
      parallel = false;
  }
  public void enter (String id, Declaration attr) {

    IdEntry entry = this.latest;
    boolean present = false, searching = true;

    // Check for duplicate entry ...
    while (searching) {
      if (entry == null || entry.level < this.level)
        searching = false;
      else if (entry.id.equals(id)) {
        present = true;
        searching = false;
       } else
       entry = entry.previous;
      
    }

    attr.duplicated = present;
    //In case its a parallel
    if(parallel){
        declarations.put(id,attr);
        return;
    }
    // Add new entry ...
    entry = new IdEntry(id, attr, this.level, this.latest);
    this.latest = entry;
    if(entry.previous != null)
        entry.previous.next = entry;
  }

  // Finds an entry for the given identifier in the identification table,
  // if any. If there are several entries for that identifier, finds the
  // entry at the highest level, in accordance with the scope rules.
  // Returns null iff no entry is found.
  // otherwise returns the attribute field of the entry found.

  public Declaration retrieve (String id) {

    IdEntry entry;
    Declaration attr = null;
    boolean present = false, searching = true;

    entry = this.latest;
    while (searching) {
      if (entry == null)
        searching = false;
      else if (entry.id.equals(id)) {
        present = true;
        searching = false;
        attr = entry.attr;
      } else
        entry = entry.previous;
    }

    return attr;
  }

    public IdEntry getLatest() {
        return latest;
    }

    public void setLatest(IdEntry latest) {
        this.latest = latest;
    }

}
