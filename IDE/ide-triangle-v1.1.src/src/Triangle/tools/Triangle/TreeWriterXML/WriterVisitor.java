package Triangle.tools.Triangle.TreeWriterXML;
import Triangle.tools.Triangle.AbstractSyntaxTrees.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
* La clase WriterVisitor permite recorrer los diferentes ASTs generados por el Parser,
* de modo que recorran en orden y se escriba el respectivo texto XML de los ASTs que
* se visitan. Como resultado se va a lograr observar en el archivo XML el AST completo
* que generó el Parser. El constructor recibe por parámetro el FileWriter que permite
* escribir en el archivo. Note que la clase implementa la interface Visitor, de modo
* que se garantice la implementación de todos los visitors necesarios que permiten
* visitar cada uno de los ASTs generados por el Parser.
*/
public class WriterVisitor implements Visitor {

    private FileWriter fileWriter;

    public WriterVisitor() {
    }
    
    public WriterVisitor(FileWriter fileWriter) {
        this.fileWriter = fileWriter;
    }

    public void setFileWriter(FileWriter fileWriter){
        this.fileWriter = fileWriter;
    }
     
    public FileWriter geFileWriter(){
        return fileWriter;
    }
    
    // <editor-fold defaultstate="collapsed" desc=" Commands ">
    // Commands
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * VName y Expression con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitAssignCommand(AssignCommand ast, Object obj) {
        writeLineXML("<AssignCommand>");
        ast.V.visit(this, null);
        ast.E.visit(this, null);
        writeLineXML("</AssignCommand>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * Identifier y ActualParameterSequence con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitCallCommand(CallCommand ast, Object obj) {
        writeLineXML("<CallCommand>");
        ast.I.visit(this, null);
        ast.APS.visit(this, null);
        writeLineXML("</CallCommand>");
        return null;
    }
    
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * Expression y Command con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitChooseCommand(ChooseCommand ast, Object o) {
        writeLineXML("<ChoooseCommand>");
        ast.EXP.visit(this, null);
        ast.COM.visit(this, null);
        writeLineXML("</ChoooseCommand>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml.
    */
    @Override
    public Object visitEmptyCommand(EmptyCommand ast, Object obj) {
        writeLineXML("<EmptyCommand/>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * Expression, Command1 y Command2 con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitIfCommand(IfCommand ast, Object obj) {
        writeLineXML("<IfCommand>");
        ast.E.visit(this, null);
        ast.C1.visit(this, null);
        ast.C2.visit(this, null);
        writeLineXML("</IfCommand>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * Declaration y Command con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitLetCommand(LetCommand ast, Object obj) {
        writeLineXML("<LetCommand>");
        ast.D.visit(this, null);
        ast.C.visit(this, null);
        writeLineXML("</LetCommand>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * Command1 y Command2 con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitSequentialCommand(SequentialCommand ast, Object obj) {
        writeLineXML("<SequentialCommand>");
        ast.C1.visit(this, null);
        ast.C2.visit(this, null);
        writeLineXML("</SequentialCommand>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visita:
    * LoopCases con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitCallLoopCases(CallLoopCases ast, Object o) {
        writeLineXML("<CallLoopCases>");
        ast.LOOP.visit(this, null);
        writeLineXML("</CallLoopCases>");
        return null;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" PROCS ">
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * Identifier, FormalParameterSequence y Command con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitProcProcFunc(ProcProcFunc ast, Object o) {
        writeLineXML("<ProcProcFunc>");
        ast.ID.visit(this, null);
        ast.FPS.visit(this, null);
        ast.COM.visit(this, null);
        writeLineXML("</ProcProcFunc>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * Identifier, FormalParameterSequence, TypeDenoter y Expression con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitFuncProcFunc(FuncProcFunc ast, Object o) {
        writeLineXML("<FuncProcFunc>");
        ast.ID.visit(this, null);
        ast.FPS.visit(this, null);
        ast.TD.visit(this, null);
        ast.EXP.visit(this, null);
        writeLineXML("</FuncProcFunc>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * ProcFunc y ProcFunc con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitProcFuncs(ProcFuncs ast, Object o) {
        writeLineXML("<ProcFuncs>");
        ast.PF1.visit(this, null);
        ast.PF2.visit(this, null);
        writeLineXML("</ProcFuncs>");
        return null;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Loops ">
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * Expression y Command con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitLoopCasesWhile(LoopCasesWhile ast, Object o) {
        writeLineXML("<LoopCasesWhile>");
        ast.EXP.visit(this, null);
        ast.COM.visit(this, null);
        writeLineXML("</LoopCasesWhile>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * Expression y Expression con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitLoopCasesUntil(LoopCasesUntil ast, Object o) {
        writeLineXML("<LoopCasesUntil>");
        ast.EXP.visit(this, null);
        ast.COM.visit(this, null);
        writeLineXML("</LoopCasesUntil>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * Command y DoLoop con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitLoopCasesDo(LoopCasesDo ast, Object o) {
        writeLineXML("<LoopCasesDo>");
        ast.COM.visit(this, null);
        ast.DO.visit(this, null);
        writeLineXML("</LoopCasesDo>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * Identifier, Expression1, Expression2 y ForLoop con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitLoopCasesFOR(LoopCasesFOR ast, Object o) {
        writeLineXML("<LoopCasesFOR>");
        ast.ID.visit(this, null);
        ast.EXP.visit(this, null);
        ast.EXP2.visit(this, null);
        ast.FOR.visit(this, null);
        writeLineXML("</LoopCasesFOR>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visita:
    * Expression con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitDoLoopUntil(DoLoopUntil ast, Object o) {
        writeLineXML("<DoLoopUntil>");
        ast.EXP.visit(this, null);
        writeLineXML("</DoLoopUntil>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visita:
    * Expression con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitDoLoopWhile(DoLoopWhile ast, Object o) {
        writeLineXML("<DoLoopWhile>");
        ast.EXP.visit(this, null);
        writeLineXML("</DoLoopWhile>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visita:
    * Command con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitForLoopDo(ForLoopDo ast, Object o) {
        writeLineXML("<ForLoopDo>");
        ast.COM.visit(this, null);
        writeLineXML("</ForLoopDo>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * Expression y Command con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitForLoopUntil(ForLoopUntil ast, Object o) {
        writeLineXML("<ForLoopUntil>");
        ast.EXP.visit(this, null);
        ast.COM.visit(this, null);
        writeLineXML("</ForLoopUntil>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * Expression y Command con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitForLoopWhile(ForLoopWhile ast, Object o) {
        writeLineXML("<ForLoopWhile>");
        ast.EXP.visit(this, null);
        ast.COM.visit(this, null);
        writeLineXML("</ForLoopWhile>");
        return null;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Cases ">
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * Case y ElseCase, si ElseCase es diferente a null. De lo contrario
    * se visita solo Case. Esto con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitCases(Cases ast, Object o) {
        writeLineXML("<Cases>");
        if(ast.CASE2 == null){
            ast.CASE1.visit(this, null);
        }
        else{
            ast.CASE1.visit(this, null);
            ast.CASE2.visit(this, null);
        }
        writeLineXML("</Cases>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visita:
    * Command con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitElseCase(ElseCase ast, Object o) {
        writeLineXML("<ElseCase>");
        ast.COM.visit(this, null);
        writeLineXML("</ElseCase>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * Case1 y Case2 con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitSequentialCase(SequentialCase ast, Object o) {
        writeLineXML("<SequentialCase>");
        ast.C1.visit(this, null);
        ast.C2.visit(this, null);
        writeLineXML("</SequentialCase>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * CaseLiterals y Command con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitCaseWhen(CaseWhen ast, Object o) {
        writeLineXML("<CaseWhen>");
        ast.CASELIT.visit(this, null);
        ast.COM.visit(this, null);
        writeLineXML("</CaseWhen>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visita:
    * CaseRange con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitCaseLiterals(CaseLiterals ast, Object o) {
        writeLineXML("<CaseLiterals>");
        ast.CASERANGE.visit(this, null);
        writeLineXML("</CaseLiterals>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * CaseLiteral1 y CaseLiteral2, si CaseLiteral2 es diferente a null.
    * De lo contrario solo CaseLiteral1. Esto con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitCaseRangeCase(CaseRangeCase ast, Object o) {
        writeLineXML("<CaseRangeCase>");
        if(ast.CASELIT2 == null){
            ast.CASELIT.visit(this, null);
        }
        else{
            ast.CASELIT.visit(this, null);
            ast.CASELIT2.visit(this, null);
        }
        writeLineXML("</CaseRangeCase>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * CaseRange1 y CaseRange2 con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitSequentialCaseRange(SequentialCaseRange ast, Object o) {
        writeLineXML("<SequentialCaseRange>");
        ast.C1.visit(this, null);
        ast.C2.visit(this, null);
        writeLineXML("</SequentialCaseRange>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visita:
    * CharacterLiteral con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitCaseLiteralCHAR(CaseLiteralCHAR ast, Object o) {
        writeLineXML("<CaseLiteralCHAR>");
        ast.CHARLIT.visit(this, null);
        writeLineXML("</CaseLiteralCHAR>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visita:
    * IntegerLiteral con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitCaseLiteralINT(CaseLiteralINT ast, Object o) {
        writeLineXML("<CaseLiteralINT>");
        ast.INTLIT.visit(this, null);
        writeLineXML("</CaseLiteralINT>");
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Expressions ">
    // Expressions
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * Expression1, Operator y Expression2 con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitBinaryExpression(BinaryExpression ast, Object obj) {
        writeLineXML("<BinaryExpression>");
        ast.E1.visit(this, null);
        ast.O.visit(this, null);
        ast.E2.visit(this, null);
        writeLineXML("</BinaryExpression>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * LongIdentifier y ActualParameterSequence con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitCallExpression(CallExpression ast, Object obj) {
        writeLineXML("<CallExpression>");
        ast.I.visit(this, null);
        ast.APS.visit(this, null);
        writeLineXML("</CallExpression>");
        return null;
    }
    
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visita:
    * VName con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitAssignExpression(AssignExpression ast, Object o) {
        writeLineXML("<AssignExpression>");
        ast.V.visit(this, null);
        writeLineXML("</AssignExpression>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visita:
    * CharacterLiteral con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitCharacterExpression(CharacterExpression ast, Object obj) {
        writeLineXML("<CharacterExpression>");
        ast.CL.visit(this, null);
        writeLineXML("</CharacterExpression>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * Expression1, Expression2 y Expression3 con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitIfExpression(IfExpression ast, Object obj) {
        writeLineXML("<IfExpression>");
        ast.E1.visit(this, null);
        ast.E2.visit(this, null);
        ast.E3.visit(this, null);
        writeLineXML("</IfExpression>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visita:
    * IntegerLiteral con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitIntegerExpression(IntegerExpression ast, Object obj) {
        writeLineXML("<IntegerExpression>");
        ast.IL.visit(this, null);
        writeLineXML("</IntegerExpression>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * Declaration y Expression con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitLetExpression(LetExpression ast, Object obj) {
        writeLineXML("<LetExpression>");
        ast.D.visit(this, null);
        ast.E.visit(this, null);
        writeLineXML("</LetExpression>");
        return null;
    }
    
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visita:
    * Expression con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitSecExpression(SecExpression ast, Object o) {
        writeLineXML("<SecExpression>");
        ast.secExpression.visit(this, null);
        writeLineXML("</SecExpression>");
        return null;
    }
    
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * Operator y Expression con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitOperatorExpression(OperatorExpression ast, Object o) {
        writeLineXML("<OperatorExpression>");
        ast.O.visit(this, null);
        ast.E.visit(this, null);
        writeLineXML("</OperatorExpression>");
        return null;
    }
    
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visita:
    * Expression con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitLParenExpression(LParenExpression ast, Object o) {
        writeLineXML("<LParenExpression>");
        ast.E.visit(this, null);
        writeLineXML("</LParenExpression>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visita:
    * RecordAggregate con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitLCurlyExpression(LCurlyExpression ast, Object o) {
        writeLineXML("<LCurlyExpression>");
        ast.RA.visit(this, null);
        writeLineXML("</LCurlyExpression>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visita:
    * ArrayAggregate con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitLBracketExpression(LBracketExpression ast, Object o) {
        writeLineXML("<LBracketExpression>");
        ast.AA.visit(this, null);
        writeLineXML("</LBracketExpression>");
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Declarations ">
    // Declarations
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * VName y Expression con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitConstDeclaration(ConstDeclaration ast, Object obj) {
        writeLineXML("<ConstDeclaration>");
        ast.I.visit(this, null);
        ast.E.visit(this, null);
        writeLineXML("</ConstDeclaration>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * Identifier, FormalParameterSequence, TypeDenoter y Expression con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitFuncDeclaration(FuncDeclaration ast, Object obj) {
        writeLineXML("<FuncDeclaration>");
        ast.I.visit(this, null);
        ast.FPS.visit(this, null);
        ast.T.visit(this, null);
        ast.E.visit(this, null);
        writeLineXML("</FuncDeclaration>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * Identifier, FormalParameterSequence y Command con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitProcDeclaration(ProcDeclaration ast, Object obj) {
        writeLineXML("<ProcDeclaration>");
        ast.I.visit(this, null);
        ast.FPS.visit(this, null);
        ast.C.visit(this, null);
        writeLineXML("</ProcDeclaration>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * CompoundDeclaration y Declaration con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitSequentialDeclaration(SequentialDeclaration ast, Object obj) {
        writeLineXML("<SequentialDeclaration>");
        ast.D1.visit(this, null);
        ast.D2.visit(this, null);
        writeLineXML("</SequentialDeclaration>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * Identifier y TypeDenoter con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitTypeDeclaration(TypeDeclaration ast, Object obj) {
        writeLineXML("<TypeDeclaration>");
        ast.I.visit(this, null);
        ast.T.visit(this, null);
        writeLineXML("</TypeDeclaration>");
        return null;
    }
    
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * Identifier y Declaration con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitPackageDeclaration(PackageDeclaration ast, Object o) {
        writeLineXML("<PackageDeclaration>");
        ast.ID.visit(this, null);
        ast.DEC.visit(this, null);
        writeLineXML("</PackageDeclaration>");
        return null;
    }
    
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * SingleDeclaration1 y SingleDeclaration2, si SingleDeclaration 2 es diferente a null.
    * De lo contrario solo SingleDeclaration1. Esto con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitSequentialSingleDeclaration(SequentialSingleDeclaration ast, Object o) {
        writeLineXML("<SequentialSingleDeclaration>");
        if(ast.D2 == null){
            ast.D1.visit(this, null);
        }
        else{
            ast.D1.visit(this, null);
            ast.D2.visit(this, null);
        }
        writeLineXML("</SequentialSingleDeclaration>");
        return null;
    }
    
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * PackageDeclaration1 y PackageDeclaration2, si PackageDeclaration2 es diferente a null.
    * De lo contrario solo PackageDeclaration1. Esto con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitSequentialPackageDeclaration(SequentialPackageDeclaration ast, Object o) {
        writeLineXML("<SequentialPackageDeclaration>");
        if(ast.D2 == null){
            ast.D1.visit(this, null);
        }
        else{
            ast.D1.visit(this, null);
            ast.D2.visit(this, null);
        }
        writeLineXML("</SequentialPackageDeclaration>");
        return null;
    }
    
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visita:
    * ProcFuncs con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitCompoundDeclarationRecursive(CompoundDeclarationRecursive ast, Object o) {
        writeLineXML("<CompoundDeclarationRecursive>");
        ast.PF.visit(this, null);
        writeLineXML("</CompoundDeclarationRecursive>");
        return null;
    }
    
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * Declaration1 y Declaration2 con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitCompoundDeclarationPrivate(CompoundDeclarationPrivate ast, Object o) {
        writeLineXML("<CompoundDeclarationPrivate>");
        ast.D1.visit(this, null);
        ast.D2.visit(this, null);
        writeLineXML("</CompoundDeclarationPrivate>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visita:
    * Declaration con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitCompoundDeclarationSingleDeclaration(CompoundDeclarationSingleDeclaration ast, Object o) {
        writeLineXML("<CompoundDeclarationSingleDeclaration>");
        ast.SD.visit(this, null);
        writeLineXML("</CompoundDeclarationSingleDeclaration>");
        return null;
    }
    
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visita:
    * TypeDenoter con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitVarSingleDeclarationColon(VarSingleDeclarationColon ast, Object o) {
        writeLineXML("<VarSingleDeclarationColon>");
        ast.T.visit(this, null);
        writeLineXML("</VarSingleDeclarationColon>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visita:
    * Expression con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitVarSingleDeclarationSingleDeclaration(VarSingleDeclarationSingleDeclaration ast, Object o) {
        writeLineXML("<VarSingleDeclarationSingleDeclaration>");
        ast.T.visit(this, null);
        writeLineXML("</VarSingleDeclarationSingleDeclaration>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * Identifier y VarSingleDeclaration con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitVarDeclaration(VarDeclaration ast, Object obj) {
        writeLineXML("<VarDeclaration>");
        ast.I.visit(this, null);
        ast.V.visit(this, null);
        writeLineXML("</VarDeclaration>");
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Aggregates ">
    // Array Aggregates
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * Expression y ArrayAggregate con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitMultipleArrayAggregate(MultipleArrayAggregate ast, Object obj) {
        writeLineXML("<MultipleArrayAggregate>");
        ast.E.visit(this, null);
        ast.AA.visit(this, null);
        writeLineXML("</MultipleArrayAggregate>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visita:
    * Expression con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitSingleArrayAggregate(SingleArrayAggregate ast, Object obj) {
        writeLineXML("<SingleArrayAggregate>");
        ast.E.visit(this, null);
        writeLineXML("</SingleArrayAggregate>");
        return null;
    }


    // Record Aggregates
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * Identifier, Expression y RecordAggregate con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitMultipleRecordAggregate(MultipleRecordAggregate ast, Object obj) {
        writeLineXML("<MultipleRecordAggregate>");
        ast.I.visit(this, null);
        ast.E.visit(this, null);
        ast.RA.visit(this, null);
        writeLineXML("</MultipleRecordAggregate>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * Identifier y Expression con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitSingleRecordAggregate(SingleRecordAggregate ast, Object obj) {
        writeLineXML("<SingleRecordAggregate>");
        ast.I.visit(this, null);
        ast.E.visit(this, null);
        writeLineXML("</SingleRecordAggregate>");
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Parameters ">
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * Identifier y TypeDenoter con el fin de seguir el recorrido del AST compelto.
    */
    // Formal Parameters
    @Override
    public Object visitConstFormalParameter(ConstFormalParameter ast, Object obj) {
        writeLineXML("<ConstFormalParameter>");
        ast.I.visit(this, null);
        ast.T.visit(this, null);
        writeLineXML("</ConstFormalParameter>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * Identifier, FormalParameterSequence y TypeDenoter con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitFuncFormalParameter(FuncFormalParameter ast, Object obj) {
        writeLineXML("<FuncFormalParameter>");
        ast.I.visit(this, null);
        ast.FPS.visit(this, null);
        ast.T.visit(this, null);
        writeLineXML("</FuncFormalParameter>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * Identifier y FormalParameterSequence con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitProcFormalParameter(ProcFormalParameter ast, Object obj) {
        writeLineXML("<ProcFormalParameter>");
        ast.I.visit(this, null);
        ast.FPS.visit(this, null);
        writeLineXML("</ProcFormalParameter>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * Identifier y TypeDenoter con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitVarFormalParameter(VarFormalParameter ast, Object obj) {
        writeLineXML("<VarFormalParameter>");
        ast.I.visit(this, null);
        ast.T.visit(this, null);
        writeLineXML("</VarFormalParameter>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml.
    */
    @Override
    public Object visitEmptyFormalParameterSequence(EmptyFormalParameterSequence ast, Object obj) {
        writeLineXML("<EmptyFormalParameterSequence/>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * FormalParameter y FormalParameterSequence con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitMultipleFormalParameterSequence(MultipleFormalParameterSequence ast, Object obj) {
        writeLineXML("<MultipleFormalParameterSequence>");
        ast.FP.visit(this, null);
        ast.FPS.visit(this, null);
        writeLineXML("</MultipleFormalParameterSequence>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visita:
    * FormalParameter con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitSingleFormalParameterSequence(SingleFormalParameterSequence ast, Object obj) {
        writeLineXML("<SingleFormalParameterSequence>");
        ast.FP.visit(this, null);
        writeLineXML("</SingleFormalParameterSequence>");
        return null;
    }


    // Actual Parameters
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visita
    * ActualParameter con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitConstActualParameter(ConstActualParameter ast, Object obj) {
        writeLineXML("<ConstActualParameter>");
        ast.E.visit(this, null);
        writeLineXML("</ConstActualParameter>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * Identifier con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitFuncActualParameter(FuncActualParameter ast, Object obj) {
        writeLineXML("<FuncActualParameter>");
        ast.I.visit(this, null);
        writeLineXML("</FuncActualParameter>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visita:
    * Identifier con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitProcActualParameter(ProcActualParameter ast, Object obj) {
        writeLineXML("<ProcActualParameter>");
        ast.I.visit(this, null);
        writeLineXML("</ProcActualParameter>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visita:
    * VName con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitVarActualParameter(VarActualParameter ast, Object obj) {
        writeLineXML("<VarActualParameter>");
        ast.V.visit(this, null);
        writeLineXML("</VarActualParameter>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml
    */
    @Override
    public Object visitEmptyActualParameterSequence(EmptyActualParameterSequence ast, Object obj) {
        writeLineXML("<EmptyActualParameterSequence/>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * ActualParameter y ActualParameterSequence con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitMultipleActualParameterSequence(MultipleActualParameterSequence ast, Object obj) {
        writeLineXML("<MultipleActualParameterSequence>");
        ast.AP.visit(this, null);
        ast.APS.visit(this, null);
        writeLineXML("</MultipleActualParameterSequence>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * ActualParameter con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitSingleActualParameterSequence(SingleActualParameterSequence ast, Object obj) {
        writeLineXML("<SingleActualParameterSequence>");
        ast.AP.visit(this, null);
        writeLineXML("</SingleActualParameterSequence>");
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Type Denoters ">
    // Type Denoters
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * IntegerLiteral y TypeDenoter con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitArrayTypeDenoter(ArrayTypeDenoter ast, Object obj) {
        writeLineXML("<ArrayTypeDenoter>");
        ast.IL.visit(this, null);
        ast.T.visit(this, null);
        writeLineXML("</ArrayTypeDenoter>");
        return null;
    }
    
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * Identifier, TypeDenoter y RecordTypeDenoter con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitMultipleRecordTypeDenoter(MultipleRecordTypeDenoter ast, Object o) {
        writeLineXML("<MultipleRecordTypeDenoter>");
        ast.ID.visit(this, null);
        ast.TD.visit(this, null);
        ast.RTD.visit(this, null);
        writeLineXML("</MultipleRecordTypeDenoter>");
        return null;
    }
    
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * Identifier y TypeDenoter con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitSingleRecordTypeDenoter(SingleRecordTypeDenoter ast, Object o) {
        writeLineXML("<SingleRecordTypeDenoter>");
        ast.ID.visit(this, null);
        ast.TD.visit(this, null);
        writeLineXML("</SingleRecordTypeDenoter>");
        return null;
    }
    
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visita:
    * LongIdentifier con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitTypeDenoterLongIdentifier(TypeDenoterLongIdentifier ast, Object o) {
        writeLineXML("<TypeDenoterLongIdentifier>");
        ast.longIdentifier.visit(this, null);
        writeLineXML("</TypeDenoterLongIdentifier>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visita:
    * RecordTypeDenoter con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitRTypeDenoter(RTypeDenoter ast, Object o) {
        writeLineXML("<RTypeDenoter>");
        ast.REC.visit(this, null);
        writeLineXML("</RTypeDenoter>");
        return null;
    }
    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Literals, Identifiers and Operators ">
    // Literals, Identifiers and Operators
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, y se escribe
    * el spelling.
    */
    @Override
    public Object visitCharacterLiteral(CharacterLiteral ast, Object obj) {
        writeLineXML("<CharacterLiteral value=\"" + ast.spelling + "\"/>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, y se escribe
    * el spelling.
    */
    @Override
    public Object visitIdentifier(Identifier ast, Object obj) {
        writeLineXML("<Identifier value=\"" + ast.spelling + "\"/>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, y se escribe
    * el spelling.
    */
    @Override
    public Object visitIntegerLiteral(IntegerLiteral ast, Object obj) {
        writeLineXML("<IntegerLiteral value=\"" + ast.spelling + "\"/>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, y se escribe
    * el spelling.
    */
    @Override
    public Object visitOperator(Operator ast, Object obj) {
        writeLineXML("<Operator value=\"" + transformOperator(ast.spelling) + "\"/>");
        return null;
    }
    
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * PackageIdentifier y Identifier, si packageIdentifier es diferente a null,
    * de lo contrario solo Identifier. Esto con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitLongIdentifier(LongIdentifier ast, Object o) {
        writeLineXML("<LongIdentifier>");
        if(ast.packageIdentifier == null){
            ast.identifier.visit(this, null);
        }
        else{
            ast.packageIdentifier.visit(this, null);
            ast.identifier.visit(this, null);
        }
        writeLineXML("</LongIdentifier>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * Identifier y PackageIdentifier con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitCompoundIdentifier(CompoundIdentifier ast, Object o) {
        writeLineXML("<CompoundIdentifier>");
        ast.identifier.visit(this, null);
        ast.packageIdentifier.visit(this, null);
        writeLineXML("</CompoundIdentifier>");
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Value-or-variable names ">
    // Value-or-variable names
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * VName y Identifier con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitDotVname(DotVname ast, Object obj) {
        writeLineXML("<DotVname>");
        ast.V.visit(this, null);
        ast.I.visit(this, null);
        writeLineXML("</DotVname>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * VName y PackageIdenfier, si PackageIdentifier es diferente a null.
    * De lo contrario, solo Identifier. Esto con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitSimpleVname(SimpleVname ast, Object obj) {
        writeLineXML("<SimpleVname>");
        if(ast.P == null){
            ast.I.visit(this, null);
        }
        else{
            ast.I.visit(this, null);
            ast.P.visit(this, null);
        }
        writeLineXML("</SimpleVname>");
        return null;
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * VName y Expression con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitSubscriptVname(SubscriptVname ast, Object obj) {
        writeLineXML("<SubscriptVname>");
        ast.V.visit(this, null);
        ast.E.visit(this, null);
        writeLineXML("</SubscriptVname>");
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Programs ">
    // Programs
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * Command y PackageDeclaration, si PackageDeclaration es diferente a null.
    * De lo contrario, solo Command. A partir de estos ast que se visitan, se
    * realiza el recorrido de todos los ast, y luego se cierra el fileWriter,
    * lo que indica que ya se terminaron de escribir todos los ast en el archivo XML.
    */
    @Override
    public Object visitProgram(Program ast, Object obj) {
        writeLineXML("<Program>");
        if(ast.P == null){
            ast.C.visit(this, null);
        }
        else{
            ast.P.visit(this, null);
            ast.C.visit(this, null);
        }
        writeLineXML("</Program>");
        try {
            fileWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(WriterVisitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    // </editor-fold>

    /*
    * Entradas: el string a escribir en el archivo XML
    * Proceso: escribir el string en una linea del archivo, y
    * realizar un salto de linea con el fin de escribir el
    * siguiente string que se necesitaria.
    * Salidas: ninguna
    */
    private void writeLineXML(String line) {
        try {
            fileWriter.write(line);
            fileWriter.write('\n');
        } catch (IOException e) {
            System.err.println("Error while writing file for print the AST");
            e.printStackTrace();
        }
    }

    /*
    * Convert the characters "<" & "<=" to their equivalents in XML
    */
    private String transformOperator(String operator) {
        if (operator.compareTo("<") == 0)
            return "&lt;";
        else if (operator.compareTo("<=") == 0)
            return "&lt;=";
        else
            return operator;
    }
    
    @Override
    public Object visitAnyTypeDenoter(AnyTypeDenoter aThis, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitArrayExpression(ArrayExpression aThis, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitBinaryOperatorDeclaration(BinaryOperatorDeclaration aThis, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitBoolTypeDenoter(BoolTypeDenoter aThis, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitBracketSelector(BracketSelector aThis, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitCharTypeDenoter(CharTypeDenoter aThis, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitDotSelector(DotSelector aThis, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitEmptyExpression(EmptyExpression aThis, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitIntLiteralExpression(IntLiteralExpression aThis, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitIntTypeDenoter(IntTypeDenoter aThis, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitLIdentifierExpression(LIdentifierExpression aThis, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitMultipleFieldTypeDenoter(MultipleFieldTypeDenoter aThis, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitRecordExpression(RecordExpression aThis, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitRecordTypeDenoter(RecordTypeDenoter aThis, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitErrorTypeDenoter(ErrorTypeDenoter aThis, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitSimpleTypeDenoter(SimpleTypeDenoter aThis, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitSingleDeclarationCommand(SingleDeclarationCommand aThis, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitSingleFieldTypeDenoter(SingleFieldTypeDenoter aThis, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitUnaryExpression(UnaryExpression aThis, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitUnaryOperatorDeclaration(UnaryOperatorDeclaration aThis, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitVnameExpression(VnameExpression aThis, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitWhileCommand(WhileCommand aThis, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}