/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Triangle.tools.Triangle.AbstractSyntaxTrees;

import Triangle.tools.Triangle.SyntacticAnalyzer.SourcePosition;

/**
 *
 * @author jose pablo
 */
public class TypeDenoterLongIdentifier extends TypeDenoter{
    
  public TypeDenoterLongIdentifier (LongIdentifier p_Identifier, SourcePosition thePosition) {
    super (thePosition);
    longIdentifier = p_Identifier;
  }

  public boolean equals(Object obj){
      //TODO: Salu2 
      return false;
  }
  
  
  public LongIdentifier longIdentifier;

    @Override
    public Object visit(Visitor v, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
