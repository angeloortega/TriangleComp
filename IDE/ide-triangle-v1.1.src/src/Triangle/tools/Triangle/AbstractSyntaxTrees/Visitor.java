/*
 * @(#)Visitor.java                        2.1 2003/10/07
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

public interface Visitor {

  // Commands
  public abstract Object visitAssignCommand(AssignCommand ast, Object o);
  public abstract Object visitCallCommand(CallCommand ast, Object o);
  public abstract Object visitChooseCommand(ChooseCommand ast, Object o);  
  public abstract Object visitEmptyCommand(EmptyCommand ast, Object o);
  public abstract Object visitIfCommand(IfCommand ast, Object o);
  public abstract Object visitLetCommand(LetCommand ast, Object o);
  public abstract Object visitSequentialCommand(SequentialCommand ast, Object o);
  public abstract Object visitCallLoopCases(CallLoopCases ast, Object o);

  //PROCS & FUNCS
  public abstract Object visitProcProcFunc(ProcProcFunc ast, Object o);
  public abstract Object visitFuncProcFunc(FuncProcFunc ast, Object o);
  public abstract Object visitProcFuncs(ProcFuncs aThis, Object o);

  //Loops
  public abstract Object visitLoopCasesWhile(LoopCasesWhile ast, Object o);
  public abstract Object visitLoopCasesUntil(LoopCasesUntil ast, Object o);
  public abstract Object visitLoopCasesDo(LoopCasesDo ast, Object o);
  public abstract Object visitLoopCasesFOR(LoopCasesFOR ast, Object o);
  public abstract Object visitDoLoopUntil(DoLoopUntil ast, Object o);
  public abstract Object visitDoLoopWhile(DoLoopWhile ast, Object o);
  public abstract Object visitForLoopDo(ForLoopDo ast, Object o);
  public abstract Object visitForLoopUntil(ForLoopUntil ast, Object o);
  public abstract Object visitForLoopWhile(ForLoopWhile ast, Object o);

  // Cases
  public abstract Object visitCases(Cases ast, Object o);
  public abstract Object visitElseCase(ElseCase ast, Object o);
  public abstract Object visitSequentialCase(SequentialCase ast, Object o);
  public abstract Object visitCaseWhen(CaseWhen ast, Object o);
  public abstract Object visitCaseLiterals(CaseLiterals ast, Object o);
  public abstract Object visitCaseRangeCase(CaseRangeCase ast, Object o);
  public abstract Object visitSequentialCaseRange(SequentialCaseRange ast, Object o);
  public abstract Object visitCaseLiteralCHAR(CaseLiteralCHAR ast, Object o);
  public abstract Object visitCaseLiteralINT(CaseLiteralINT ast, Object o);

  // Expressions
  public abstract Object visitAssignExpression(AssignExpression ast, Object o);
  public abstract Object visitBinaryExpression(BinaryExpression ast, Object o);
  public abstract Object visitCallExpression(CallExpression ast, Object o);
  public abstract Object visitCharacterExpression(CharacterExpression ast, Object o);
  public abstract Object visitSecExpression(SecExpression ast, Object o);
  public abstract Object visitIfExpression(IfExpression ast, Object o);
  public abstract Object visitIntegerExpression(IntegerExpression ast, Object o);
  public abstract Object visitLetExpression(LetExpression ast, Object o);
  public abstract Object visitOperatorExpression(OperatorExpression ast, Object o);
  public abstract Object visitLParenExpression(LParenExpression ast, Object o);
  public abstract Object visitLCurlyExpression(LCurlyExpression ast, Object o);
  public abstract Object visitLBracketExpression(LBracketExpression ast, Object o);


  // Declarations
  public abstract Object visitSequentialDeclaration(SequentialDeclaration ast, Object o);
  public abstract Object visitSequentialSingleDeclaration(SequentialSingleDeclaration ast, Object o);
  public abstract Object visitCompoundDeclarationPrivate(CompoundDeclarationPrivate ast, Object o);
  public abstract Object visitCompoundDeclarationRecursive(CompoundDeclarationRecursive ast, Object o);
  public abstract Object visitCompoundDeclarationSingleDeclaration(CompoundDeclarationSingleDeclaration ast, Object o);
  public abstract Object visitConstDeclaration(ConstDeclaration ast, Object o);
  public abstract Object visitFuncDeclaration(FuncDeclaration ast, Object o);
  public abstract Object visitProcDeclaration(ProcDeclaration ast, Object o);
  public abstract Object visitTypeDeclaration(TypeDeclaration ast, Object o);
  public abstract Object visitVarDeclaration(VarDeclaration ast, Object o);
  public abstract Object visitVarSingleDeclarationColon(VarSingleDeclarationColon ast, Object o);
  public abstract Object visitVarSingleDeclarationSingleDeclaration(VarSingleDeclarationSingleDeclaration ast, Object o);
  public abstract Object visitPackageDeclaration(PackageDeclaration ast, Object o);
  public abstract Object visitSequentialPackageDeclaration(SequentialPackageDeclaration ast, Object o);


  // Array Aggregates
  public abstract Object visitMultipleArrayAggregate(MultipleArrayAggregate ast, Object o);
  public abstract Object visitSingleArrayAggregate(SingleArrayAggregate ast, Object o);

  // Record Aggregates
  public abstract Object visitMultipleRecordAggregate(MultipleRecordAggregate ast, Object o);
  public abstract Object visitSingleRecordAggregate(SingleRecordAggregate ast, Object o);

  // Formal Parameters
  public abstract Object visitConstFormalParameter(ConstFormalParameter ast, Object o);
  public abstract Object visitFuncFormalParameter(FuncFormalParameter ast, Object o);
  public abstract Object visitProcFormalParameter(ProcFormalParameter ast, Object o);
  public abstract Object visitVarFormalParameter(VarFormalParameter ast, Object o);
  public abstract Object visitEmptyFormalParameterSequence(EmptyFormalParameterSequence ast, Object o);
  public abstract Object visitMultipleFormalParameterSequence(MultipleFormalParameterSequence ast, Object o);
  public abstract Object visitSingleFormalParameterSequence(SingleFormalParameterSequence ast, Object o);

  // Actual Parameters
  public abstract Object visitConstActualParameter(ConstActualParameter ast, Object o);
  public abstract Object visitFuncActualParameter(FuncActualParameter ast, Object o);
  public abstract Object visitProcActualParameter(ProcActualParameter ast, Object o);
  public abstract Object visitVarActualParameter(VarActualParameter ast, Object o);
  public abstract Object visitEmptyActualParameterSequence(EmptyActualParameterSequence ast, Object o);
  public abstract Object visitMultipleActualParameterSequence(MultipleActualParameterSequence ast, Object o);
  public abstract Object visitSingleActualParameterSequence(SingleActualParameterSequence ast, Object o);

  // Type Denoters
  public abstract Object visitTypeDenoterLongIdentifier(TypeDenoterLongIdentifier ast, Object o);
  public abstract Object visitArrayTypeDenoter(ArrayTypeDenoter ast, Object o);
  public abstract Object visitRTypeDenoter(RTypeDenoter aThis, Object o);
  public abstract Object visitMultipleRecordTypeDenoter(MultipleRecordTypeDenoter ast, Object o);
  public abstract Object visitSingleRecordTypeDenoter(SingleRecordTypeDenoter ast, Object o);



  // Literals, Identifiers and Operators
  public abstract Object visitCharacterLiteral(CharacterLiteral ast, Object o);
  public abstract Object visitIdentifier(Identifier ast, Object o);
  public abstract Object visitIntegerLiteral(IntegerLiteral ast, Object o);
  public abstract Object visitOperator(Operator ast, Object o);
  public abstract Object visitLongIdentifier(LongIdentifier ast, Object o);
  public abstract Object visitCompoundIdentifier(CompoundIdentifier ast, Object o);

  // Value-or-variable names
  public abstract Object visitDotVname(DotVname ast, Object o);
  public abstract Object visitSimpleVname(SimpleVname ast, Object o);
  public abstract Object visitSubscriptVname(SubscriptVname ast, Object o);

  // Programs
  public abstract Object visitProgram(Program ast, Object o);

}