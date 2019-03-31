/*
 * IDE-Triangle v1.0
 * TableDetails.java
 */

package Core.Visitors;

import Triangle.tools.Triangle.AbstractSyntaxTrees.AnyTypeDenoter;
import Triangle.tools.Triangle.AbstractSyntaxTrees.ArrayExpression;
import Triangle.tools.Triangle.AbstractSyntaxTrees.ArrayTypeDenoter;
import Triangle.tools.Triangle.AbstractSyntaxTrees.AssignCommand;
import Triangle.tools.Triangle.AbstractSyntaxTrees.AssignExpression;
import Triangle.tools.Triangle.AbstractSyntaxTrees.BinaryExpression;
import Triangle.tools.Triangle.AbstractSyntaxTrees.BinaryOperatorDeclaration;
import Triangle.tools.Triangle.AbstractSyntaxTrees.BoolTypeDenoter;
import Triangle.tools.Triangle.AbstractSyntaxTrees.CallCommand;
import Triangle.tools.Triangle.AbstractSyntaxTrees.CallExpression;
import Triangle.tools.Triangle.AbstractSyntaxTrees.CharTypeDenoter;
import Triangle.tools.Triangle.AbstractSyntaxTrees.CharacterExpression;
import Triangle.tools.Triangle.AbstractSyntaxTrees.CharacterLiteral;
import Triangle.tools.Triangle.AbstractSyntaxTrees.ConstActualParameter;
import Triangle.tools.Triangle.AbstractSyntaxTrees.ConstDeclaration;
import Triangle.tools.Triangle.AbstractSyntaxTrees.ConstFormalParameter;
import Triangle.tools.Triangle.AbstractSyntaxTrees.DotVname;
import Triangle.tools.Triangle.AbstractSyntaxTrees.EmptyActualParameterSequence;
import Triangle.tools.Triangle.AbstractSyntaxTrees.EmptyCommand;
import Triangle.tools.Triangle.AbstractSyntaxTrees.EmptyExpression;
import Triangle.tools.Triangle.AbstractSyntaxTrees.EmptyFormalParameterSequence;
import Triangle.tools.Triangle.AbstractSyntaxTrees.ErrorTypeDenoter;
import Triangle.tools.Triangle.AbstractSyntaxTrees.FuncActualParameter;
import Triangle.tools.Triangle.AbstractSyntaxTrees.FuncDeclaration;
import Triangle.tools.Triangle.AbstractSyntaxTrees.FuncFormalParameter;
import Triangle.tools.Triangle.AbstractSyntaxTrees.Identifier;
import Triangle.tools.Triangle.AbstractSyntaxTrees.IfCommand;
import Triangle.tools.Triangle.AbstractSyntaxTrees.IfExpression;
import Triangle.tools.Triangle.AbstractSyntaxTrees.IntTypeDenoter;
import Triangle.tools.Triangle.AbstractSyntaxTrees.IntegerExpression;
import Triangle.tools.Triangle.AbstractSyntaxTrees.IntegerLiteral;
import Triangle.tools.Triangle.AbstractSyntaxTrees.LetCommand;
import Triangle.tools.Triangle.AbstractSyntaxTrees.LetExpression;
import Triangle.tools.Triangle.AbstractSyntaxTrees.MultipleActualParameterSequence;
import Triangle.tools.Triangle.AbstractSyntaxTrees.MultipleArrayAggregate;
import Triangle.tools.Triangle.AbstractSyntaxTrees.MultipleFieldTypeDenoter;
import Triangle.tools.Triangle.AbstractSyntaxTrees.MultipleFormalParameterSequence;
import Triangle.tools.Triangle.AbstractSyntaxTrees.MultipleRecordAggregate;
import Triangle.tools.Triangle.AbstractSyntaxTrees.Operator;
import Triangle.tools.Triangle.AbstractSyntaxTrees.ProcActualParameter;
import Triangle.tools.Triangle.AbstractSyntaxTrees.ProcDeclaration;
import Triangle.tools.Triangle.AbstractSyntaxTrees.ProcFormalParameter;
import Triangle.tools.Triangle.AbstractSyntaxTrees.Program;
import Triangle.tools.Triangle.AbstractSyntaxTrees.RecordExpression;
import Triangle.tools.Triangle.AbstractSyntaxTrees.RecordTypeDenoter;
import Triangle.tools.Triangle.AbstractSyntaxTrees.SequentialCommand;
import Triangle.tools.Triangle.AbstractSyntaxTrees.SequentialDeclaration;
import Triangle.tools.Triangle.AbstractSyntaxTrees.SimpleTypeDenoter;
import Triangle.tools.Triangle.AbstractSyntaxTrees.SimpleVname;
import Triangle.tools.Triangle.AbstractSyntaxTrees.SingleActualParameterSequence;
import Triangle.tools.Triangle.AbstractSyntaxTrees.SingleArrayAggregate;
import Triangle.tools.Triangle.AbstractSyntaxTrees.SingleFieldTypeDenoter;
import Triangle.tools.Triangle.AbstractSyntaxTrees.SingleFormalParameterSequence;
import Triangle.tools.Triangle.AbstractSyntaxTrees.SingleRecordAggregate;
import Triangle.tools.Triangle.AbstractSyntaxTrees.SubscriptVname;
import Triangle.tools.Triangle.AbstractSyntaxTrees.TypeDeclaration;
import Triangle.tools.Triangle.AbstractSyntaxTrees.UnaryExpression;
import Triangle.tools.Triangle.AbstractSyntaxTrees.UnaryOperatorDeclaration;
import Triangle.tools.Triangle.AbstractSyntaxTrees.VarActualParameter;
import Triangle.tools.Triangle.AbstractSyntaxTrees.VarDeclaration;
import Triangle.tools.Triangle.AbstractSyntaxTrees.VarFormalParameter;
import Triangle.tools.Triangle.AbstractSyntaxTrees.Visitor;
import Triangle.tools.Triangle.AbstractSyntaxTrees.VnameExpression;
import Triangle.tools.Triangle.AbstractSyntaxTrees.WhileCommand;
import Triangle.tools.Triangle.CodeGenerator.Field;
import Triangle.tools.Triangle.CodeGenerator.KnownAddress;
import Triangle.tools.Triangle.CodeGenerator.KnownRoutine;
import Triangle.tools.Triangle.CodeGenerator.KnownValue;
import Triangle.tools.Triangle.CodeGenerator.TypeRepresentation;
import Triangle.tools.Triangle.CodeGenerator.UnknownAddress;
import Triangle.tools.Triangle.CodeGenerator.UnknownRoutine;
import Triangle.tools.Triangle.CodeGenerator.UnknownValue;
import Triangle.tools.Triangle.AbstractSyntaxTrees.BracketSelector;
import Triangle.tools.Triangle.AbstractSyntaxTrees.CallLoopCases;
import Triangle.tools.Triangle.AbstractSyntaxTrees.CaseLiteralCHAR;
import Triangle.tools.Triangle.AbstractSyntaxTrees.CaseLiteralINT;
import Triangle.tools.Triangle.AbstractSyntaxTrees.CaseLiterals;
import Triangle.tools.Triangle.AbstractSyntaxTrees.CaseRangeCase;
import Triangle.tools.Triangle.AbstractSyntaxTrees.CaseWhen;
import Triangle.tools.Triangle.AbstractSyntaxTrees.Cases;
import Triangle.tools.Triangle.AbstractSyntaxTrees.ChooseCommand;
import Triangle.tools.Triangle.AbstractSyntaxTrees.CompoundDeclarationPrivate;
import Triangle.tools.Triangle.AbstractSyntaxTrees.CompoundDeclarationRecursive;
import Triangle.tools.Triangle.AbstractSyntaxTrees.CompoundDeclarationSingleDeclaration;
import Triangle.tools.Triangle.AbstractSyntaxTrees.CompoundIdentifier;
import Triangle.tools.Triangle.AbstractSyntaxTrees.DoLoopUntil;
import Triangle.tools.Triangle.AbstractSyntaxTrees.DoLoopWhile;
import Triangle.tools.Triangle.AbstractSyntaxTrees.DotSelector;
import Triangle.tools.Triangle.AbstractSyntaxTrees.ElseCase;
import Triangle.tools.Triangle.AbstractSyntaxTrees.ForLoopDo;
import Triangle.tools.Triangle.AbstractSyntaxTrees.ForLoopUntil;
import Triangle.tools.Triangle.AbstractSyntaxTrees.ForLoopWhile;
import Triangle.tools.Triangle.AbstractSyntaxTrees.FuncProcFunc;
import Triangle.tools.Triangle.AbstractSyntaxTrees.IntLiteralExpression;
import Triangle.tools.Triangle.AbstractSyntaxTrees.LBracketExpression;
import Triangle.tools.Triangle.AbstractSyntaxTrees.LCurlyExpression;
import Triangle.tools.Triangle.AbstractSyntaxTrees.LIdentifierExpression;
import Triangle.tools.Triangle.AbstractSyntaxTrees.LParenExpression;
import Triangle.tools.Triangle.AbstractSyntaxTrees.LongIdentifier;
import Triangle.tools.Triangle.AbstractSyntaxTrees.LoopCasesDo;
import Triangle.tools.Triangle.AbstractSyntaxTrees.LoopCasesFOR;
import Triangle.tools.Triangle.AbstractSyntaxTrees.LoopCasesUntil;
import Triangle.tools.Triangle.AbstractSyntaxTrees.LoopCasesWhile;
import Triangle.tools.Triangle.AbstractSyntaxTrees.MultipleRecordTypeDenoter;
import Triangle.tools.Triangle.AbstractSyntaxTrees.OperatorExpression;
import Triangle.tools.Triangle.AbstractSyntaxTrees.PackageDeclaration;
import Triangle.tools.Triangle.AbstractSyntaxTrees.ProcFuncs;
import Triangle.tools.Triangle.AbstractSyntaxTrees.ProcProcFunc;
import Triangle.tools.Triangle.AbstractSyntaxTrees.RTypeDenoter;
import Triangle.tools.Triangle.AbstractSyntaxTrees.SecExpression;
import Triangle.tools.Triangle.AbstractSyntaxTrees.SequentialCase;
import Triangle.tools.Triangle.AbstractSyntaxTrees.SequentialCaseRange;
import Triangle.tools.Triangle.AbstractSyntaxTrees.SequentialPackageDeclaration;
import Triangle.tools.Triangle.AbstractSyntaxTrees.SequentialSingleDeclaration;
import Triangle.tools.Triangle.AbstractSyntaxTrees.SingleDeclarationCommand;
import Triangle.tools.Triangle.AbstractSyntaxTrees.SingleRecordTypeDenoter;
import Triangle.tools.Triangle.AbstractSyntaxTrees.VarSingleDeclarationColon;
import Triangle.tools.Triangle.AbstractSyntaxTrees.VarSingleDeclarationSingleDeclaration;
import javax.swing.table.DefaultTableModel;

/**
 * Implements the Triangle Visitor interface, which is used to
 * visit an entire AST. 
 *
 * Generates a DefaultTableModel, used to draw a Jable.
 *
 * @author Luis Leopoldo Pérez <luiperpe@ns.isi.ulatina.ac.cr>
 */
public class TableVisitor implements Visitor {
    
    /** Creates a new instance of TableDetails */
    public TableVisitor() {        
    }

    @Override
    public Object visitAssignCommand(AssignCommand ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitCallCommand(CallCommand ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitChooseCommand(ChooseCommand ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitEmptyCommand(EmptyCommand ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitIfCommand(IfCommand ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitLetCommand(LetCommand ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitSequentialCommand(SequentialCommand ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitWhileCommand(WhileCommand ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitSingleDeclarationCommand(SingleDeclarationCommand ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitProcProcFunc(ProcProcFunc ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitFuncProcFunc(FuncProcFunc ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitLoopCasesWhile(LoopCasesWhile ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitLoopCasesUntil(LoopCasesUntil ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitLoopCasesDo(LoopCasesDo ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitLoopCasesFOR(LoopCasesFOR ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitDoLoopUntil(DoLoopUntil ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitDoLoopWhile(DoLoopWhile ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitCallLoopCases(CallLoopCases ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitForLoopDo(ForLoopDo ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitForLoopUntil(ForLoopUntil ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitForLoopWhile(ForLoopWhile ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitCases(Cases ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitElseCase(ElseCase ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitSequentialCase(SequentialCase ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitCaseWhen(CaseWhen ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitCaseLiterals(CaseLiterals ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitCaseRangeCase(CaseRangeCase ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitSequentialCaseRange(SequentialCaseRange ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitCaseLiteralCHAR(CaseLiteralCHAR ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitCaseLiteralINT(CaseLiteralINT ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitBracketSelector(BracketSelector ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitDotSelector(DotSelector ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitArrayExpression(ArrayExpression ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitAssignExpression(AssignExpression ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitBinaryExpression(BinaryExpression ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitCallExpression(CallExpression ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitCharacterExpression(CharacterExpression ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitEmptyExpression(EmptyExpression ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitSecExpression(SecExpression ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitIfExpression(IfExpression ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitIntegerExpression(IntegerExpression ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitLetExpression(LetExpression ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitRecordExpression(RecordExpression ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitUnaryExpression(UnaryExpression ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitVnameExpression(VnameExpression ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitIntLiteralExpression(IntLiteralExpression ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitOperatorExpression(OperatorExpression ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitLParenExpression(LParenExpression ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitLCurlyExpression(LCurlyExpression ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitLBracketExpression(LBracketExpression ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitLIdentifierExpression(LIdentifierExpression ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitBinaryOperatorDeclaration(BinaryOperatorDeclaration ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitConstDeclaration(ConstDeclaration ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitFuncDeclaration(FuncDeclaration ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitProcDeclaration(ProcDeclaration ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitPackageDeclaration(PackageDeclaration ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitSequentialDeclaration(SequentialDeclaration ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitSequentialSingleDeclaration(SequentialSingleDeclaration ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitSequentialPackageDeclaration(SequentialPackageDeclaration ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitTypeDeclaration(TypeDeclaration ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitUnaryOperatorDeclaration(UnaryOperatorDeclaration ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitVarDeclaration(VarDeclaration ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitCompoundDeclarationRecursive(CompoundDeclarationRecursive ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitCompoundDeclarationPrivate(CompoundDeclarationPrivate ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitCompoundDeclarationSingleDeclaration(CompoundDeclarationSingleDeclaration ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitVarSingleDeclarationColon(VarSingleDeclarationColon ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitVarSingleDeclarationSingleDeclaration(VarSingleDeclarationSingleDeclaration ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitMultipleArrayAggregate(MultipleArrayAggregate ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitSingleArrayAggregate(SingleArrayAggregate ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitMultipleRecordAggregate(MultipleRecordAggregate ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitSingleRecordAggregate(SingleRecordAggregate ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitMultipleRecordTypeDenoter(MultipleRecordTypeDenoter ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitSingleRecordTypeDenoter(SingleRecordTypeDenoter ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitConstFormalParameter(ConstFormalParameter ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitFuncFormalParameter(FuncFormalParameter ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitProcFormalParameter(ProcFormalParameter ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitVarFormalParameter(VarFormalParameter ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitEmptyFormalParameterSequence(EmptyFormalParameterSequence ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitMultipleFormalParameterSequence(MultipleFormalParameterSequence ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitSingleFormalParameterSequence(SingleFormalParameterSequence ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitConstActualParameter(ConstActualParameter ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitFuncActualParameter(FuncActualParameter ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitProcActualParameter(ProcActualParameter ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitVarActualParameter(VarActualParameter ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitEmptyActualParameterSequence(EmptyActualParameterSequence ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitMultipleActualParameterSequence(MultipleActualParameterSequence ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitSingleActualParameterSequence(SingleActualParameterSequence ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitAnyTypeDenoter(AnyTypeDenoter ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitArrayTypeDenoter(ArrayTypeDenoter ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitBoolTypeDenoter(BoolTypeDenoter ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitCharTypeDenoter(CharTypeDenoter ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitErrorTypeDenoter(ErrorTypeDenoter ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitSimpleTypeDenoter(SimpleTypeDenoter ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitIntTypeDenoter(IntTypeDenoter ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitRecordTypeDenoter(RecordTypeDenoter ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitMultipleFieldTypeDenoter(MultipleFieldTypeDenoter ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitSingleFieldTypeDenoter(SingleFieldTypeDenoter ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitCharacterLiteral(CharacterLiteral ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitIdentifier(Identifier ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitIntegerLiteral(IntegerLiteral ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitOperator(Operator ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitLongIdentifier(LongIdentifier ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitCompoundIdentifier(CompoundIdentifier ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitDotVname(DotVname ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitSimpleVname(SimpleVname ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitSubscriptVname(SubscriptVname ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitProgram(Program ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitProcFuncs(ProcFuncs aThis, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitRTypeDenoter(RTypeDenoter aThis, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
