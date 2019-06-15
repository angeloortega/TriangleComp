/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;
import Triangle.tools.Triangle.AbstractSyntaxTrees.CaseWhen;
import Triangle.tools.Triangle.CodeGenerator.Frame;

/**
 *
 * @author angelo
 */
public class ChooseCode {
    public Frame frame;
    public int address;
    public CaseWhen ast;
    public ChooseCode(Frame pFrame, Integer pAddress){
        frame = pFrame;
        address = pAddress;
    }
}
