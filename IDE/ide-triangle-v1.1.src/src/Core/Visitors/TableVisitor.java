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
import Triangle.tools.Triangle.AbstractSyntaxTrees.TypeDenoterLongIdentifier;
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
    
    private DefaultTableModel model;
    
    /** Creates a new instance of TableDetails */
   public TableVisitor() {        
    }
   
   
      /**
       * Returns the filled table model.
       */
      public DefaultTableModel getTable(Program ast) {
          model = new DefaultTableModel((new String[] {"Name", "Type", "Size", "Level", "Displacement", "Value"}), 0);
          visitProgram(ast, null);

          return(model);
      }

    // <editor-fold defaultstate="collapsed" desc=" Commands ">
    // Commands
   public Object visitAssignCommand(AssignCommand ast, Object o) { 
        ast.V.visit(this, null);
        ast.E.visit(this, null);
        
        return(null);
    }

    public Object visitCallCommand(CallCommand ast, Object o) { 
        ast.I.visit(this, null);
        ast.APS.visit(this, null);

        return(null);
    }

    public Object visitChooseCommand(ChooseCommand ast, Object o) {
        ast.EXP.visit(this, null);
        ast.COM.visit(this, null);

        return(null);
    }

    public Object visitEmptyCommand(EmptyCommand ast, Object o) { 
        return(null);
    }

    public Object visitIfCommand(IfCommand ast, Object o) { 
        ast.E.visit(this, null);
        ast.C1.visit(this, null);
        ast.C2.visit(this, null);

        return(null);
    }

    public Object visitLetCommand(LetCommand ast, Object o) {     
        ast.D.visit(this, null);
        ast.C.visit(this, null);

        return(null);
    }

    public Object visitSequentialCommand(SequentialCommand ast, Object o) { 
        ast.C1.visit(this, null);
        ast.C2.visit(this, null);

        return(null);
    }

    public Object visitCallLoopCases(CallLoopCases ast, Object o) {
        ast.LOOP.visit(this, null);

        return(null);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" PROCS ">

    @Override
    public Object visitProcProcFunc(ProcProcFunc ast, Object o) {
        ast.ID.visit(this, null);
        ast.FPS.visit(this, null);
        ast.COM.visit(this, null);
        return null;
    }

    @Override
    public Object visitFuncProcFunc(FuncProcFunc ast, Object o) {
        ast.ID.visit(this, null);
        ast.FPS.visit(this, null);
        ast.TD.visit(this, null);
        ast.EXP.visit(this, null);
        return null;
    }

    @Override
    public Object visitProcFuncs(ProcFuncs ast, Object o) {
        ast.PF1.visit(this, null);
        ast.PF2.visit(this, null);
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Loops ">
    @Override
    public Object visitLoopCasesWhile(LoopCasesWhile ast, Object o) {
        ast.EXP.visit(this, null);
        ast.COM.visit(this, null);
        return null;
    }

    @Override
    public Object visitLoopCasesUntil(LoopCasesUntil ast, Object o) {
        ast.EXP.visit(this, null);
        ast.COM.visit(this, null);
        return null;
    }

    @Override
    public Object visitLoopCasesDo(LoopCasesDo ast, Object o) {
        ast.COM.visit(this, null);
        ast.DO.visit(this, null);
        return null;
    }

    @Override
    public Object visitLoopCasesFOR(LoopCasesFOR ast, Object o) {
        ast.DECL.visit(this, null);
        ast.EXP2.visit(this, null);
        ast.FOR.visit(this, null);
        return null;
    }

    @Override
    public Object visitDoLoopUntil(DoLoopUntil ast, Object o) {
        ast.EXP.visit(this, null);
        return null;
    }

    @Override
    public Object visitDoLoopWhile(DoLoopWhile ast, Object o) {
        ast.EXP.visit(this, null);
        return null;
    }

    @Override
    public Object visitForLoopDo(ForLoopDo ast, Object o) {
        ast.COM.visit(this, null);
        return null;
    }

    @Override
    public Object visitForLoopUntil(ForLoopUntil ast, Object o) {
        ast.EXP.visit(this, null);
        ast.COM.visit(this, null);
        return null;
    }

    @Override
    public Object visitForLoopWhile(ForLoopWhile ast, Object o) {
        ast.EXP.visit(this, null);
        ast.COM.visit(this, null);
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Cases ">
    @Override
    public Object visitCases(Cases ast, Object o) {
        if(ast.CASE2 == null){
            ast.CASE1.visit(this, null);
        }
        else{
            ast.CASE1.visit(this, null);
            ast.CASE2.visit(this, null);
        }
        return null;
    }

    @Override
    public Object visitElseCase(ElseCase ast, Object o) {
        ast.COM.visit(this, null);
        return null;
    }

    @Override
    public Object visitSequentialCase(SequentialCase ast, Object o) {
        ast.C1.visit(this, null);
        ast.C2.visit(this, null);
        return null;
    }

    @Override
    public Object visitCaseWhen(CaseWhen ast, Object o) {
        ast.CASELIT.visit(this, null);
        ast.COM.visit(this, null);
        return null;
    }

    @Override
    public Object visitCaseLiterals(CaseLiterals ast, Object o) {
        ast.CASERANGE.visit(this, null);
        return null;
    }

    @Override
    public Object visitCaseRangeCase(CaseRangeCase ast, Object o) {
        if(ast.CASELIT2 == null){
            ast.CASELIT.visit(this, null);
        }
        else{
            ast.CASELIT.visit(this, null);
            ast.CASELIT2.visit(this, null);
        }
        return null;
    }

    @Override
    public Object visitSequentialCaseRange(SequentialCaseRange ast, Object o) {
        ast.C1.visit(this, null);
        ast.C2.visit(this, null);
        return null;
    }

    @Override
    public Object visitCaseLiteralCHAR(CaseLiteralCHAR ast, Object o) {
        ast.CHARLIT.visit(this, null);
        return null;
    }

    @Override
    public Object visitCaseLiteralINT(CaseLiteralINT ast, Object o) {
        ast.INTLIT.visit(this, null);
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Expressions ">
    // Expressions

    public Object visitBinaryExpression(BinaryExpression ast, Object o) { 
        ast.E1.visit(this, null);
        ast.O.visit(this, null);
        ast.E2.visit(this, null);

        return(null);
    }

    public Object visitCallExpression(CallExpression ast, Object o) { 
        ast.I.visit(this, null);
        ast.APS.visit(this, null);

        return(null);
    }

    public Object visitAssignExpression(AssignExpression ast, Object o) {
        ast.V.visit(this, null);
        return null;
    }

    public Object visitCharacterExpression(CharacterExpression ast, Object o) { 
        ast.CL.visit(this, null);

        return(null);
    }

    public Object visitIfExpression(IfExpression ast, Object o) {       
        ast.E1.visit(this, null);
        ast.E2.visit(this, null);
        ast.E3.visit(this, null);

        return(null);
    }

    public Object visitIntegerExpression(IntegerExpression ast, Object o) {
        ast.IL.visit(this, null); //TODO esta linea no veina extrannamente, solo el return null 
        return(null);
    }

    public Object visitLetExpression(LetExpression ast, Object o) { 
        ast.D.visit(this, null);
        ast.E.visit(this, null);

        return(null);
    }

    @Override
    public Object visitSecExpression(SecExpression ast, Object o) {
        ast.secExpression.visit(this, null);
        return(null);
    }
    
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visitan:
    * Operator y Expression con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitOperatorExpression(OperatorExpression ast, Object o) {
        ast.O.visit(this, null);
        ast.E.visit(this, null);
        return(null);
    }
    
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visita:
    * Expression con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitLParenExpression(LParenExpression ast, Object o) {
        ast.E.visit(this, null);
        return(null);
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visita:
    * RecordAggregate con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitLCurlyExpression(LCurlyExpression ast, Object o) {
        ast.RA.visit(this, null);
        return(null);
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se escribe el nombre del ast en el archivo xml, se visita:
    * ArrayAggregate con el fin de seguir el recorrido del AST compelto.
    */
    @Override
    public Object visitLBracketExpression(LBracketExpression ast, Object o) {
        ast.AA.visit(this, null);
        return(null);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Declarations ">
    // Declarations

    public Object visitConstDeclaration(ConstDeclaration ast, Object o) {   
        String name = ast.I.spelling;
        String type = "N/A";
        try {
          int size = (ast.entity!=null?ast.entity.size:0);
          int level = -1;
          int displacement = -1;
          int value = -1;

          if (ast.entity instanceof KnownValue) {
                type = "KnownValue";
                value = ((KnownValue)ast.entity).value;
            }
            else if (ast.entity instanceof UnknownValue) {
                type = "UnknownValue";
                level = ((UnknownValue)ast.entity).address.level;
                displacement = ((UnknownValue)ast.entity).address.displacement;
            }
            addIdentifier(name, type, size, level, displacement, value);
        } catch (NullPointerException e) { }

        ast.I.visit(this, null);
        ast.E.visit(this, null);

        return(null);
    }

    public Object visitFuncDeclaration(FuncDeclaration ast, Object o) {    
        try {
        addIdentifier(ast.I.spelling, 
                "KnownRoutine", 
                (ast.entity!=null?ast.entity.size:0), 
                ((KnownRoutine)ast.entity).address.level, 
                ((KnownRoutine)ast.entity).address.displacement, 
                -1);      
        } catch (NullPointerException e) { }            
        ast.FPS.visit(this, null);
        ast.T.visit(this, null);
        ast.E.visit(this, null);

        return(null);
    }

    public Object visitProcDeclaration(ProcDeclaration ast, Object o) { 
        try {
        addIdentifier(ast.I.spelling, "KnownRoutine", 
                (ast.entity!=null?ast.entity.size:0), 
                ((KnownRoutine)ast.entity).address.level, 
                ((KnownRoutine)ast.entity).address.displacement, 
                -1);
        } catch (NullPointerException e) { }

        ast.FPS.visit(this, null);
        ast.C.visit(this, null);

        return(null);
    }

    public Object visitSequentialDeclaration(SequentialDeclaration ast, Object o) {   
        ast.D1.visit(this, null);
        ast.D2.visit(this, null);

        return(null);
    }

    public Object visitTypeDeclaration(TypeDeclaration ast, Object o) {
        ast.I.visit(this, null);
        ast.T.visit(this, null);

        return(null);
    }

    public Object visitPackageDeclaration(PackageDeclaration ast, Object o) {
        ast.ID.visit(this, null);
        ast.DEC.visit(this, null);
        return (null);
    }

    public Object visitSequentialSingleDeclaration(SequentialSingleDeclaration ast, Object o) {
        if(ast.D2 == null){
            ast.D1.visit(this, null);
        }
        else{
            ast.D1.visit(this, null);
            ast.D2.visit(this, null);
        }
        return (null);
    }

    public Object visitSequentialPackageDeclaration(SequentialPackageDeclaration ast, Object o) {
        if(ast.D2 == null){
            ast.D1.visit(this, null);
        }
        else{
            ast.D1.visit(this, null);
            ast.D2.visit(this, null);
        }
        return (null);
    }

    public Object visitCompoundDeclarationRecursive(CompoundDeclarationRecursive ast, Object o) {
        ast.PF.visit(this, null);
        return (null);
    }

    public Object visitCompoundDeclarationPrivate(CompoundDeclarationPrivate ast, Object o) {
        ast.D1.visit(this, null);
        ast.D2.visit(this, null);
        return (null);
    }

    public Object visitCompoundDeclarationSingleDeclaration(CompoundDeclarationSingleDeclaration ast, Object o) {
        ast.SD.visit(this, null);
        return (null);
    }

    public Object visitVarSingleDeclarationColon(VarSingleDeclarationColon ast, Object o) {
        ast.T.visit(this, null);
        return (null);
    }

    public Object visitVarSingleDeclarationSingleDeclaration(VarSingleDeclarationSingleDeclaration ast, Object o) {
        ast.T.visit(this, null);
        return (null);
    }

    public Object visitVarDeclaration(VarDeclaration ast, Object o) {      
        try {
        addIdentifier(ast.I.spelling, 
                "KnownAddress", 
                (ast.entity!=null?ast.entity.size:0), 
                ((KnownAddress)ast.entity).address.level, 
                ((KnownAddress)ast.entity).address.displacement, 
                -1);
        } catch (NullPointerException e) { }
        ast.V.visit(this, null);
        return(null);
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Aggregates ">
    // Array Aggregates
    public Object visitMultipleArrayAggregate(MultipleArrayAggregate ast, Object o) { 
        ast.E.visit(this, null);
        ast.AA.visit(this, null);

        return(null);
    }

    public Object visitSingleArrayAggregate(SingleArrayAggregate ast, Object o) { 
        ast.E.visit(this, null);

        return(null);
    }

    // Record Aggregates
    public Object visitMultipleRecordAggregate(MultipleRecordAggregate ast, Object o) { 
        ast.I.visit(this, null);
        ast.E.visit(this, null);
        ast.RA.visit(this, null);

        return(null);
    }

    public Object visitSingleRecordAggregate(SingleRecordAggregate ast, Object o) { 
        ast.I.visit(this, null);
        ast.E.visit(this, null);

        return(null);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Parameters ">
    // Formal Parameters
    public Object visitConstFormalParameter(ConstFormalParameter ast, Object o) {       
        try {
        addIdentifier(ast.I.spelling, 
                "UnknownValue", 
                (ast.entity!=null?ast.entity.size:0), 
                ((UnknownValue)ast.entity).address.level, 
                ((UnknownValue)ast.entity).address.displacement, 
                -1);
        } catch (NullPointerException e) { }

        ast.I.visit(this, null);
        ast.T.visit(this, null);

        return(null);
    }

    public Object visitFuncFormalParameter(FuncFormalParameter ast, Object o) {       
        try {
        addIdentifier(ast.I.spelling, 
                "UnknownRoutine",
                (ast.entity!=null?ast.entity.size:0), 
                ((UnknownRoutine)ast.entity).address.level, 
                ((UnknownRoutine)ast.entity).address.displacement,
                -1);
        } catch (NullPointerException e) { }
        ast.FPS.visit(this, null);      
        ast.T.visit(this, null);     

        return(null);
    }

    public Object visitProcFormalParameter(ProcFormalParameter ast, Object o) {       
        try {
        addIdentifier(ast.I.spelling, 
                "UnknownRoutine",
                (ast.entity!=null?ast.entity.size:0), 
                ((UnknownRoutine)ast.entity).address.level, 
                ((UnknownRoutine)ast.entity).address.displacement,
                -1);      
        } catch (NullPointerException e) { }
        ast.FPS.visit(this, null);      

        return(null);
    }

    public Object visitVarFormalParameter(VarFormalParameter ast, Object o) {       
        try {
        addIdentifier(ast.I.spelling, 
                "UnknownAddress",
                ast.T.entity.size,
                ((UnknownAddress)ast.entity).address.level, 
                ((UnknownAddress)ast.entity).address.displacement,
                -1);
        } catch (NullPointerException e) { }
        ast.T.visit(this, null);

        return(null);
    }

    public Object visitEmptyFormalParameterSequence(EmptyFormalParameterSequence ast, Object o) { 
        return(null);
    }

    public Object visitMultipleFormalParameterSequence(MultipleFormalParameterSequence ast, Object o) { 
        ast.FP.visit(this, null);
        ast.FPS.visit(this, null);

        return(null);
    }

    public Object visitSingleFormalParameterSequence(SingleFormalParameterSequence ast, Object o) { 
        ast.FP.visit(this, null);

        return(null);
    }

    // Actual Parameters
    public Object visitConstActualParameter(ConstActualParameter ast, Object o) { 
        ast.E.visit(this, null);

        return(null);
    }

    public Object visitFuncActualParameter(FuncActualParameter ast, Object o) { 
        ast.I.visit(this, null);

        return(null);
    }

    public Object visitProcActualParameter(ProcActualParameter ast, Object o) { 
        ast.I.visit(this, null);

        return(null);
    }

    public Object visitVarActualParameter(VarActualParameter ast, Object o) { 
        ast.V.visit(this, null);

        return(null);
    }

    public Object visitEmptyActualParameterSequence(EmptyActualParameterSequence ast, Object o) {       
        return(null);
    }

    public Object visitMultipleActualParameterSequence(MultipleActualParameterSequence ast, Object o) { 
        ast.AP.visit(this, null);
        ast.APS.visit(this, null);

        return(null);
    }

    public Object visitSingleActualParameterSequence(SingleActualParameterSequence ast, Object o) {   
        ast.AP.visit(this, null);

        return(null);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Type Denoters ">
    // Type Denoters

    public Object visitArrayTypeDenoter(ArrayTypeDenoter ast, Object o) { 
        ast.IL.visit(this, null);
        ast.T.visit(this, null);

        return(null);
    }

    public Object visitMultipleRecordTypeDenoter(MultipleRecordTypeDenoter ast, Object o) {
        ast.ID.visit(this, null);
        ast.TD.visit(this, null);
        ast.RTD.visit(this, null);
        return(null);
    }

    public Object visitSingleRecordTypeDenoter(SingleRecordTypeDenoter ast, Object o) {
        ast.ID.visit(this, null);
        ast.TD.visit(this, null);
        return(null);
    }

    public Object visitTypeDenoterLongIdentifier(TypeDenoterLongIdentifier ast, Object o) {
        ast.longIdentifier.visit(this, null);
        return(null);
    }

    public Object visitRTypeDenoter(RTypeDenoter ast, Object o) {
        ast.REC.visit(this, null);
        return(null);
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Literals, Identifiers and Operators ">
    // Literals, Identifiers and Operators
    public Object visitCharacterLiteral(CharacterLiteral ast, Object o) {   
        return(null);
    }

    public Object visitIdentifier(Identifier ast, Object o) {             
        return(null);
    }

    public Object visitIntegerLiteral(IntegerLiteral ast, Object o) { 
        return(null);
    }

    public Object visitOperator(Operator ast, Object o) { 
        //ast.decl.visit(this, null); decl siempre va a ser null, basado en el AST de Operator

        return(null);
    }

    public Object visitLongIdentifier(LongIdentifier ast, Object o) {
        if(ast.packageIdentifier == null){
            ast.identifier.visit(this, null);
        }
        else{
            ast.packageIdentifier.visit(this, null);
            ast.identifier.visit(this, null);
        }
        return(null);
    }

    public Object visitCompoundIdentifier(CompoundIdentifier ast, Object o) {
        ast.identifier.visit(this, null);
        ast.packageIdentifier.visit(this, null);
        return(null);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Values or Variable Names ">
    // Value-or-variable names
    public Object visitDotVname(DotVname ast, Object o) {
        ast.V.visit(this, null);
        ast.I.visit(this, null);

        return(null);
    }

    public Object visitSimpleVname(SimpleVname ast, Object o) { 
        if(ast.P == null){
            ast.I.visit(this, null);
        }
        else{
            ast.I.visit(this, null);
            ast.P.visit(this, null);
        }

        return(null);
    }

    public Object visitSubscriptVname(SubscriptVname ast, Object o) {
        ast.V.visit(this, null);
        ast.E.visit(this, null);

        return(null);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Table Creation Methods ">
    // Programs
    public Object visitProgram(Program ast, Object o) { 
        if(ast.P == null){
            ast.C.visit(this, null);
        }
        else{
            ast.P.visit(this, null);
            ast.C.visit(this, null);
        }

        return(null);
    }

      /**
       * Adds an identifier to the table.
       */
      private void addIdentifier(String name, String type, int size, int level, int displacement, int value) {
          boolean exists = false;

          for (int i=0;(i<model.getRowCount() && !exists);i++)
              if (((String)model.getValueAt(i, 0)).compareTo(name) == 0)
                  exists = true;

          if (!exists) {
              model.addRow(new String[] {name, 
                      type, 
                      String.valueOf(size), 
                      (level<0?" ":String.valueOf(level)), 
                      (displacement<0?" ":String.valueOf(displacement)), 
                      (value<0?" ":String.valueOf(value))});
          }
      }

    public Object visitBracketSelector(BracketSelector aThis, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Object visitDotSelector(DotSelector aThis, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Object visitIntLiteralExpression(IntLiteralExpression aThis, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Object visitLIdentifierExpression(LIdentifierExpression aThis, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Object visitSingleDeclarationCommand(SingleDeclarationCommand aThis, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitWhileCommand(WhileCommand aThis, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitArrayExpression(ArrayExpression ast, Object o) { 
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitBinaryOperatorDeclaration(BinaryOperatorDeclaration ast, Object o) {        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitUnaryOperatorDeclaration(UnaryOperatorDeclaration ast, Object o) {        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitAnyTypeDenoter(AnyTypeDenoter ast, Object o) {      
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public Object visitVnameExpression(VnameExpression aThis, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public Object visitUnaryExpression(UnaryExpression aThis, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitBoolTypeDenoter(BoolTypeDenoter aThis, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitCharTypeDenoter(CharTypeDenoter aThis, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitEmptyExpression(EmptyExpression aThis, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitIntTypeDenoter(IntTypeDenoter aThis, Object o) {
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
    public Object visitSingleFieldTypeDenoter(SingleFieldTypeDenoter aThis, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
