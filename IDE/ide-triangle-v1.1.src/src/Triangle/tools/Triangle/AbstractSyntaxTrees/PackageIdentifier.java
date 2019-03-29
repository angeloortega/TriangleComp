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
public class PackageIdentifier extends Identifier {
    
    public PackageIdentifier(String theSpelling, SourcePosition thePosition) {
        super(theSpelling, thePosition);
    }
    
}
