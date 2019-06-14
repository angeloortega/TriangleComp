/*
 * IDE-Triangle v1.0
 * TreeVisitor.java
 */

package Core.Visitors;
import Triangle.tools.Triangle.AbstractSyntaxTrees.*;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Implements the Triangle Visitor interface, which is used to
 * visit an entire AST. 
 *
 * Generates DefaultMutableTreeNodes, used to draw a JTree.
 *
 * @author Luis Leopoldo Prez <luiperpe@ns.isi.ulatina.ac.cr>
 */
public class TreeVisitor implements Visitor {
      
    /**
     * Creates a new instance of TreeVisitor.
     */
    public TreeVisitor() {
    }
    
    // <editor-fold defaultstate="collapsed" desc=" Commands ">    
    // Commands
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: VName y Expression.
    */
    @Override
    public Object visitAssignCommand(AssignCommand ast, Object o) {
        return(createBinary("Assign Command", ast.V, ast.E));
    }
    
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: Identifier y ActualParameterSequence.
    */
    @Override
    public Object visitCallCommand(CallCommand ast, Object o) {
        return(createBinary("Call Command", ast.I, ast.APS));
    }
    
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: Expression y Command.
    */
    @Override
    public Object visitChooseCommand(ChooseCommand ast, Object o) {
        return(createBinary("Call Command", ast.EXP, ast.COM));
    }
    
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual no contiene nada.
    */
    @Override
    public Object visitEmptyCommand(EmptyCommand ast, Object o) {
        return(createNullary("Empty Command"));
    }
    
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: Expression, Command1 y Command2.
    */
    @Override
    public Object visitIfCommand(IfCommand ast, Object obj) {
        return(createTernary("If Command", ast.E, ast.C1, ast.C2));
    }
    
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: Declaration y Command.
    */
    @Override
    public Object visitLetCommand(LetCommand ast, Object obj) {
        return(createBinary("Let Command", ast.D, ast.C));
    }
    
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: Command1 y Command2.
    */
    @Override
    public Object visitSequentialCommand(SequentialCommand ast, Object obj) {
        return(createBinary("Sequential Command", ast.C1, ast.C2));
    }
    
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: LoopCases.
    */
    @Override
    public Object visitCallLoopCases(CallLoopCases ast, Object obj) {
        return(createUnary("Call Loop Cases", ast.LOOP));
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" PROCS & FUNCS ">
    // PROCS
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: Identifier, FormalParameterSequence y Command.
    */
    @Override
    public Object visitProcProcFunc(ProcProcFunc ast, Object o) {
        return(createTernary("Proc Proc Func", ast.ID, ast.FPS, ast.COM));
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: Identifier, FormalParameterSequence, TypeDenoter y Expression.
    */
    @Override
    public Object visitFuncProcFunc(FuncProcFunc ast, Object o) {
        return(createQuaternary("Func Proc Func", ast.ID, ast.FPS, ast.TD, ast.EXP));
    }
    
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: ProcFunc1 y ProcFunc2.
    */
    @Override
    public Object visitProcFuncs(ProcFuncs ast, Object o) {
        return(createBinary("Proc Funcs", ast.PF1, ast.PF2));
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Loops ">
    // Loops
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: Expression y Command.
    */
    @Override
    public Object visitLoopCasesWhile(LoopCasesWhile ast, Object o) {
        return(createBinary("Loop Cases While", ast.EXP, ast.COM));
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: Expression y Expression.
    */
    @Override
    public Object visitLoopCasesUntil(LoopCasesUntil ast, Object o) {
        return(createBinary("Loop Cases Until", ast.EXP, ast.COM));
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: Command y DoLoop.
    */
    @Override
    public Object visitLoopCasesDo(LoopCasesDo ast, Object o) {
        return(createBinary("Loop Cases Do", ast.COM, ast.DO));
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: Identifier, Expression1, Expression2 y ForLoop.
    */
    @Override
    public Object visitLoopCasesFOR(LoopCasesFOR ast, Object o) {
        return(createTernary("Loop Cases FOR", ast.DECL, ast.EXP2, ast.FOR));
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: Expression.
    */
    @Override
    public Object visitDoLoopUntil(DoLoopUntil ast, Object o) {
        return(createUnary("Do Loop Until", ast.EXP));
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: Expression.
    */
    @Override
    public Object visitDoLoopWhile(DoLoopWhile ast, Object o) {
        return(createUnary("Do Loop While", ast.EXP));
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: Command.
    */
    @Override
    public Object visitForLoopDo(ForLoopDo ast, Object o) {
        return(createUnary("For Loop Do", ast.COM));
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: Expression y Command.
    */
    @Override
    public Object visitForLoopUntil(ForLoopUntil ast, Object o) {
        return(createBinary("For Loop Until", ast.EXP, ast.COM));
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: Expression y Command.
    */
    @Override
    public Object visitForLoopWhile(ForLoopWhile ast, Object o) {
        return(createBinary("For Loop While", ast.EXP, ast.COM));
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Cases ">
    // Cases
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: Case y ElseCase.
    */
    @Override
    public Object visitCases(Cases ast, Object o) {
        return(createBinary("Cases", ast.CASE1, ast.CASE2));
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: Command.
    */
    @Override
    public Object visitElseCase(ElseCase ast, Object o) {
        return(createUnary("Else Case", ast.COM));
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: Case1 y Case2.
    */
    @Override
    public Object visitSequentialCase(SequentialCase ast, Object o) {
        return(createBinary("Sequential Case", ast.C1, ast.C2));
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: CaseLiterals y Command.
    */
    @Override
    public Object visitCaseWhen(CaseWhen ast, Object o) {
        return(createBinary("Case When", ast.CASELIT, ast.COM));
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: CaseRange.
    */
    @Override
    public Object visitCaseLiterals(CaseLiterals ast, Object o) {
        return(createUnary("Case Literals", ast.CASERANGE));
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: CaseLiteral1 y CaseLiteral2.
    */
    @Override
    public Object visitCaseRangeCase(CaseRangeCase ast, Object o) {
        return(createBinary("Case Range Case", ast.CASELIT, ast.CASELIT2));
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: CaseRange1 y CaseRange2.
    */
    @Override
    public Object visitSequentialCaseRange(SequentialCaseRange ast, Object o) {
        return(createBinary("Sequential Case Range", ast.C1, ast.C2));
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: CharacterLiteral.
    */
    @Override
    public Object visitCaseLiteralCHAR(CaseLiteralCHAR ast, Object o) {
        return(createUnary("Case Literal CHAR", ast.CHARLIT));
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: IntegerLiteral.
    */
    @Override
    public Object visitCaseLiteralINT(CaseLiteralINT ast, Object o) {
        return(createUnary("Case Literal INT", ast.INTLIT));
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Expressions ">
    // Expressions
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: Expression1, Operator y Expression2.
    */
    @Override
    public Object visitBinaryExpression(BinaryExpression ast, Object obj) {
        return(createTernary("Binary Expression", ast.E1, ast.O, ast.E2));
    }
    
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: LongIdentifier y ActualParameterSequence.
    */
    @Override
    public Object visitCallExpression(CallExpression ast, Object obj) {
        return(createBinary("Call Expression", ast.I, ast.APS));
    }
    
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: VName.
    */
    @Override
    public Object visitAssignExpression(AssignExpression ast, Object o) {
        return(createUnary("Assign Expression", ast.V));
    }
    
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: CharacterLiteral.
    */
    @Override
    public Object visitCharacterExpression(CharacterExpression ast, Object obj) {
        return(createUnary("Character Expression", ast.CL));
    }
    
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: Expression1, Expression2 y Expression3.
    */
    @Override
    public Object visitIfExpression(IfExpression ast, Object obj) {
        return(createTernary("If Expression", ast.E1, ast.E2, ast.E3));
    }
    
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: IntegerLiteral.
    */
    @Override
    public Object visitIntegerExpression(IntegerExpression ast, Object obj) {
        return(createUnary("Integer Expression", ast.IL));
    }
    
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: Declaration y Expression.
    */
    @Override
    public Object visitLetExpression(LetExpression ast, Object obj) {
        return(createBinary("Let Expression", ast.D, ast.E));
    }
    
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: Expression.
    */
    @Override
    public Object visitSecExpression(SecExpression ast, Object o) {
        return(createUnary("Secondary Expression",ast.secExpression));
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: Operator y Expression.
    */
    @Override
    public Object visitOperatorExpression(OperatorExpression ast, Object o) {
        return(createBinary("Operator Expression", ast.O, ast.E));
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: Expression.
    */
    @Override
    public Object visitLParenExpression(LParenExpression ast, Object o) {
        return(createUnary("LParen Expression", ast.E));
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: RecordAggregate.
    */
    @Override
    public Object visitLCurlyExpression(LCurlyExpression ast, Object o) {
        return(createUnary("LCurly Expression", ast.RA));
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: ArrayAggregate.
    */
    @Override
    public Object visitLBracketExpression(LBracketExpression ast, Object o) {
        return(createUnary("LBracket Expression", ast.AA));
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Declarations ">
    // Declarations
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: VName y Expression.
    */
    @Override
    public Object visitConstDeclaration(ConstDeclaration ast, Object obj) {
        return(createBinary("Constant Declaration", ast.I, ast.E));
    }
    
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: Identifier, FormalParameterSequence, TypeDenoter y Expression.
    */
    @Override
    public Object visitFuncDeclaration(FuncDeclaration ast, Object obj) {
        return(createQuaternary("Function Declaration", ast.I, ast.FPS, ast.T, ast.E));
    }
    
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: Identifier, FormalParameterSequence y Command.
    */
    @Override
    public Object visitProcDeclaration(ProcDeclaration ast, Object obj) {
        return(createTernary("Procedure Declaration", ast.I, ast.FPS, ast.C));        
    }
    
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: CompoundDeclaration y Declaration.
    */
    @Override
    public Object visitSequentialDeclaration(SequentialDeclaration ast, Object obj) {
        return(createBinary("Sequential Declaration", ast.D1, ast.D2));
    }
    
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: Identifier y TypeDenoter.
    */
    @Override
    public Object visitTypeDeclaration(TypeDeclaration ast, Object obj) {
        return(createBinary("Type Declaration", ast.I, ast.T));
    }
    
    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: Identifier y Declaration.
    */
    @Override
    public Object visitPackageDeclaration(PackageDeclaration ast, Object o) {
        return(createBinary("Package Declaration", ast.ID, ast.DEC));
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: SingleDeclaration1 y SingleDeclaration2.
    */
    @Override
    public Object visitSequentialSingleDeclaration(SequentialSingleDeclaration ast, Object o) {
        return(createBinary("Sequential Single Declaration", ast.D1, ast.D2));
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: PackageDeclaration1 y PackageDeclaration2.
    */
    @Override
    public Object visitSequentialPackageDeclaration(SequentialPackageDeclaration ast, Object o) {
       return ast.D2 == null ? createUnary("Package Declaration", ast.D1) : createBinary("Sequential Package Declaration", ast.D1, ast.D2);
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: ProcFuncs.
    */
    @Override
    public Object visitCompoundDeclarationRecursive(CompoundDeclarationRecursive ast, Object o) {
        return(createUnary("Compound Declaration Recursive", ast.PF));
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: Declaration1 y Declaration2.
    */
    @Override
    public Object visitCompoundDeclarationPrivate(CompoundDeclarationPrivate ast, Object o) {
        return(createBinary("Compound Declaration Private", ast.D1, ast.D2));
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: Declaration.
    */
    @Override
    public Object visitCompoundDeclarationSingleDeclaration(CompoundDeclarationSingleDeclaration ast, Object o) {
        return(createUnary("Compound Declaration Single Declaration", ast.SD));
    }

    /*
    * Entradas: el ast respectivo que se visita, y un Object
    * Proceso: se crea un nodo con el nombre del ast que se visita,
    * el cual contiene: TypeDenoter.
    */
    @Override
    public Object visitVarSingleDeclarationColon(VarSingleDeclarationColon ast, Object o) {
        return(createUnary("Var Single Declaration Colon", ast.T));
    }

    @Override
    public Object visitVarSingleDeclarationSingleDeclaration(VarSingleDeclarationSingleDeclaration ast, Object o) {
        return(createUnary("Var Single Declaration Single Declaration", ast.T));
    }
    
    @Override
    public Object visitVarDeclaration(VarDeclaration ast, Object o) {
        return(createBinary("Var Declaration", ast.I, ast.V));
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Aggregates ">
    // Array Aggregates
    @Override
    public Object visitMultipleArrayAggregate(MultipleArrayAggregate ast, Object obj) {
        return(createBinary("Multiple Array Aggregate", ast.E, ast.AA));
    }
    
    @Override
    public Object visitSingleArrayAggregate(SingleArrayAggregate ast, Object obj) {
        return(createUnary("Single Array Aggregate", ast.E));
    }
    
    // Record Aggregates
    @Override
    public Object visitMultipleRecordAggregate(MultipleRecordAggregate ast, Object obj) {
        return(createTernary("Multiple Record Aggregate", ast.I, ast.E, ast.RA));
    }
    
    @Override
    public Object visitSingleRecordAggregate(SingleRecordAggregate ast, Object obj) {
        return(createBinary("Single Record Aggregate", ast.I, ast.E));
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Parameters ">
    // Formal Parameters   
    @Override
    public Object visitConstFormalParameter(ConstFormalParameter ast, Object obj) {
        return(createBinary("Constant Formal Parameter", ast.I, ast.T));
    }
    
    @Override
    public Object visitFuncFormalParameter(FuncFormalParameter ast, Object obj) {
        return(createTernary("Function Formal Parameter", ast.I, ast.FPS, ast.T));
    }
    
    @Override
    public Object visitProcFormalParameter(ProcFormalParameter ast, Object obj) {
        return(createBinary("Procedure Formal Parameter", ast.I, ast.FPS));
    }
    
    @Override
    public Object visitVarFormalParameter(VarFormalParameter ast, Object obj) {
        return(createBinary("Variable Formal Parameter", ast.I, ast.T));
    }
    
    @Override
    public Object visitEmptyFormalParameterSequence(EmptyFormalParameterSequence ast, Object obj) {
        return(createNullary("Empty Formal Parameter Sequence"));
    }
    
    @Override
    public Object visitMultipleFormalParameterSequence(MultipleFormalParameterSequence ast, Object obj) {
        return(createBinary("Multiple Formal Parameter Sequence", ast.FP, ast.FPS));
    }
    
    @Override
    public Object visitSingleFormalParameterSequence(SingleFormalParameterSequence ast, Object obj) {
        return(createUnary("Single Formal Parameter Sequence", ast.FP));
    }
    
    // Actual Parameters
    @Override
    public Object visitConstActualParameter(ConstActualParameter ast, Object obj) {
        return(createUnary("Constant Actual Parameter", ast.E));
    }
    
    @Override
    public Object visitFuncActualParameter(FuncActualParameter ast, Object obj) {
        return(createUnary("Function Actual Parameter", ast.I));
    }
    
    @Override
    public Object visitProcActualParameter(ProcActualParameter ast, Object obj) {
        return(createUnary("Procedure Actual Parameter", ast.I));
    }
    
    @Override
    public Object visitVarActualParameter(VarActualParameter ast, Object obj) {
        return(createUnary("Variable Actual Parameter", ast.V));
    }
    
    @Override
    public Object visitEmptyActualParameterSequence(EmptyActualParameterSequence ast, Object obj) {
        return(createNullary("Empty Actual Parameter Sequence"));
    }
    
    @Override
    public Object visitMultipleActualParameterSequence(MultipleActualParameterSequence ast, Object obj) {
        return(createBinary("Multiple Actual Parameter Sequence", ast.AP, ast.APS));
    }
    
    @Override
    public Object visitSingleActualParameterSequence(SingleActualParameterSequence ast, Object obj) {
        return(createUnary("Single Actual Parameter Sequence", ast.AP));
    }
    // </editor-fold>
        
    // <editor-fold defaultstate="collapsed" desc=" Type Denoters ">
    // Type Denoters
 public Object visitAnyTypeDenoter(AnyTypeDenoter ast, Object obj) {
        return(createNullary("any"));
    }
    
    public Object visitArrayTypeDenoter(ArrayTypeDenoter ast, Object obj) {
        return(createBinary("Array Type Denoter", ast.IL, ast.T));
    }
    
    public Object visitBoolTypeDenoter(BoolTypeDenoter ast, Object obj) {
        return(createNullary("bool"));
    }
    
    public Object visitCharTypeDenoter(CharTypeDenoter ast, Object obj) {
        return(createNullary("char"));
    }
    
    public Object visitErrorTypeDenoter(ErrorTypeDenoter ast, Object obj) {
        return(createNullary("error"));
    }
    
    public Object visitSimpleTypeDenoter(SimpleTypeDenoter ast, Object obj) {
        return(createUnary("Simple Type Denoter", ast.I));
    }
    
    public Object visitIntTypeDenoter(IntTypeDenoter ast, Object obj) {
        return(createNullary("int"));
    }
    
    public Object visitRecordTypeDenoter(RecordTypeDenoter ast, Object obj) {
        return(createUnary("Record Type Denoter", ast.FT));
    }
    
    public Object visitMultipleFieldTypeDenoter(MultipleFieldTypeDenoter ast, Object obj) {
        return(createTernary("Multiple Field Type Denoter", ast.I, ast.T, ast.FT));
    }
    
    public Object visitSingleFieldTypeDenoter(SingleFieldTypeDenoter ast, Object obj) {
        return(createBinary("Single Field Type Denoter", ast.I, ast.T));
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Literals, Identifiers and Operators ">
    // Literals, Identifiers and Operators
    @Override
    public Object visitCharacterLiteral(CharacterLiteral ast, Object obj) {
        return(createNullary(ast.spelling));
    }
    
    @Override
    public Object visitIdentifier(Identifier ast, Object obj) {
        return(createNullary(ast.spelling));
    }
    
    @Override
    public Object visitIntegerLiteral(IntegerLiteral ast, Object obj) {
        return(createNullary(ast.spelling));
    }
    
    @Override
    public Object visitOperator(Operator ast, Object obj) {
        return(createNullary(ast.spelling));
    }
    
    @Override
    public Object visitLongIdentifier(LongIdentifier ast, Object o) {
        return ast.packageIdentifier == null ? createUnary("Long Identifier", ast.identifier) : (createBinary("Long Identifier", ast.packageIdentifier, ast.identifier));
    }

    @Override
    public Object visitCompoundIdentifier(CompoundIdentifier ast, Object o) {
        return(createBinary("Compound Identifier", ast.identifier, ast.packageIdentifier));
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Values or Variable Names ">
    // Values or Variable Names
    @Override
    public Object visitDotVname(DotVname ast, Object obj) {
        return(createBinary("Dot Vname", ast.V,ast.I));
    }
    
    @Override
    public Object visitSimpleVname(SimpleVname ast, Object obj) {
        return ast.P == null ? createUnary("Simple Vname", ast.I) : createBinary("Simple Vname", ast.I, ast.P);
    }
    
    @Override
    public Object visitSubscriptVname(SubscriptVname ast, Object obj) {
        return(createBinary("Subscript Vname", ast.V, ast.E));
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Programs ">
    // Programs
    @Override
    public Object visitProgram(Program ast, Object obj) {
        return ast.P == null ? createUnary("Program", ast.C) : createBinary("Program",ast.P, ast.C);

    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Tree Creation Methods ">
    
    /**
     * Creates a nullary tree node (doesn't have any children).
     * @param caption The tree's caption (text to be shown when the tree is drawn).
     * @return The tree node.
     */
    public DefaultMutableTreeNode createNullary(String caption) {
        DefaultMutableTreeNode t = new DefaultMutableTreeNode(caption);
        
        return(t);
    }
    
    /**
     * Creates an unary tree node.
     * @param caption The tree's caption (text to be shown when the tree is drawn).
     * @param child1 The first children node.
     * @return The tree node.
     */
    public DefaultMutableTreeNode createUnary(String caption, AST child1) {
        DefaultMutableTreeNode t = new DefaultMutableTreeNode(caption);
        t.add((DefaultMutableTreeNode)child1.visit(this, null));
        
        return(t);
    }
    
    /**
     * Creates a binary tree node.
     * @param caption The tree's caption (text to be shown when the tree is drawn).
     * @param child1 The first children node.
     * @param child2 The second children node.
     * @return The tree node.
     */
    public DefaultMutableTreeNode createBinary(String caption, AST child1, AST child2) {
        DefaultMutableTreeNode t = new DefaultMutableTreeNode(caption);
        t.add((DefaultMutableTreeNode)child1.visit(this, null));
        if(child2 != null)
            t.add((DefaultMutableTreeNode)child2.visit(this, null));
        return(t);
    }
    
    /**
     * Creates a ternary tree node.
     * @param caption The tree's caption (text to be shown when the tree is drawn).
     * @param child1 The first children node.
     * @param child2 The second children node.
     * @param child3 The third children node.
     * @return The tree node.
     */
    public DefaultMutableTreeNode createTernary(String caption, AST child1, AST child2, AST child3) {
        DefaultMutableTreeNode t = new DefaultMutableTreeNode(caption);
        t.add((DefaultMutableTreeNode)child1.visit(this, null));
        t.add((DefaultMutableTreeNode)child2.visit(this, null));
        t.add((DefaultMutableTreeNode)child3.visit(this, null));
        
        return(t);        
    }
    
    /**
     * Creates a quaternary tree node.
     * @param caption The tree's caption (text to be shown when the tree is drawn).
     * @param child1 The first children node.
     * @param child2 The second children node.
     * @param child3 The third children node.
     * @param child4 The fourth children node.
     * @return The tree node.
     */
    public DefaultMutableTreeNode createQuaternary(String caption, AST child1, AST child2, AST child3, AST child4) {
        DefaultMutableTreeNode t = new DefaultMutableTreeNode(caption);
        t.add((DefaultMutableTreeNode)child1.visit(this, null));
        t.add((DefaultMutableTreeNode)child2.visit(this, null));
        t.add((DefaultMutableTreeNode)child3.visit(this, null));
        t.add((DefaultMutableTreeNode)child4.visit(this, null));
        
        return(t);             
    }
    // </editor-fold>

    @Override
    public Object visitArrayExpression(ArrayExpression ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitBinaryOperatorDeclaration(BinaryOperatorDeclaration ast, Object o) {
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
    public Object visitEmptyExpression(EmptyExpression ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitIntLiteralExpression(IntLiteralExpression ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitLIdentifierExpression(LIdentifierExpression ast, Object o) {
        return(createNullary(ast.LI.spelling));
    }


    @Override
    public Object visitRecordExpression(RecordExpression ast, Object obj) {
        return(createUnary("Record Expression", ast.RA));
    }

    @Override
    public Object visitSingleDeclarationCommand(SingleDeclarationCommand ast, Object o) {
        return(createBinary("Assign Command", ast.VN, ast.EXP));
    }


    @Override
    public Object visitUnaryExpression(UnaryExpression ast, Object obj) {
        return(createBinary("Unary Expression", ast.O, ast.E));
    }

    @Override
    public Object visitUnaryOperatorDeclaration(UnaryOperatorDeclaration ast, Object obj) {
        return(createTernary("Unary Operator Declaration", ast.O, ast.ARG, ast.RES));
    }

    @Override
    public Object visitVnameExpression(VnameExpression ast, Object obj) {
        return(createUnary("Vname Expression", ast.V));
    }

    @Override
    public Object visitWhileCommand(WhileCommand ast, Object obj) {
        return(createBinary("While Command", ast.E, ast.C));
    }

    @Override
    public Object visitTypeDenoterLongIdentifier(TypeDenoterLongIdentifier ast, Object o) {
        return(createNullary(ast.longIdentifier.spelling));    
    }

    @Override
    public Object visitRTypeDenoter(RTypeDenoter ast, Object o) {
        return(createUnary("Record Type Denoter", ast.REC));    
    }

    @Override
    public Object visitMultipleRecordTypeDenoter(MultipleRecordTypeDenoter ast, Object o) {
        return(createTernary("Multiple Record Type Denoter", ast.ID,ast.TD,ast.RTD));    
    }

    @Override
    public Object visitSingleRecordTypeDenoter(SingleRecordTypeDenoter ast, Object o) {
        return(createBinary("Single Record Type Denoter",ast.ID,ast.TD));    
    }
    
}
