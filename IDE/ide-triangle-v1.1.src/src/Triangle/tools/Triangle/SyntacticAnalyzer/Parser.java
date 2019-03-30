/*
 * @(#)Parser.java                        2.1 2003/10/07
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


package Triangle.tools.Triangle.SyntacticAnalyzer;

import Triangle.ErrorReporter;
import Triangle.tools.Triangle.AbstractSyntaxTrees.*;

public class Parser {

  private Scanner lexicalAnalyser;
  private ErrorReporter errorReporter;
  private Token currentToken;
  private SourcePosition previousTokenPosition;

  public Parser(Scanner lexer, ErrorReporter reporter) {
    lexicalAnalyser = lexer;
    errorReporter = reporter;
    previousTokenPosition = new SourcePosition();
  }

// accept checks whether the current token matches tokenExpected.
// If so, fetches the next token.
// If not, reports a syntactic error.

  void accept (int tokenExpected) throws SyntaxError {
    if (currentToken.kind == tokenExpected) {
      previousTokenPosition = currentToken.position;
      currentToken = lexicalAnalyser.scan();
    } else {
      syntacticError("\"%\" expected here", Token.spell(tokenExpected));
    }
  }

  void acceptIt() {
    previousTokenPosition = currentToken.position;
    currentToken = lexicalAnalyser.scan();
  }

// start records the position of the start of a phrase.
// This is defined to be the position of the first
// character of the first token of the phrase.

  void start(SourcePosition position) {
    position.start = currentToken.position.start;
  }

// finish records the position of the end of a phrase.
// This is defined to be the position of the last
// character of the last token of the phrase.

  void finish(SourcePosition position) {
    position.finish = previousTokenPosition.finish;
  }

  void syntacticError(String messageTemplate, String tokenQuoted) throws SyntaxError {
    SourcePosition pos = currentToken.position;
    errorReporter.reportError(messageTemplate, tokenQuoted, pos);
    throw(new SyntaxError());
  }

///////////////////////////////////////////////////////////////////////////////
//
// PROGRAMS
//
///////////////////////////////////////////////////////////////////////////////

  public Program parseProgram() {

    Program programAST = null;

    previousTokenPosition.start = 0;
    previousTokenPosition.finish = 0;
    currentToken = lexicalAnalyser.scan();
    PackageDeclaration packageAST = null;
    
    SourcePosition programPos = new SourcePosition();
    
    start(programPos);
    
    try {
      while(currentToken.kind == Token.PACKAGE){
        PackageDeclaration pckdcl2AST = parsePackageDeclaration();
        finish(programPos);
        packageAST = new SequentialPackageDeclaration(packageAST, pckdcl2AST,programPos);
        accept(Token.SEMICOLON);
      }
      Command cAST = parseCommand();
      programAST = new Program(cAST,packageAST, previousTokenPosition);
      if (currentToken.kind != Token.EOT) {
        syntacticError("\"%\" not expected after end of program",
          currentToken.spelling);
      }
    }
    catch (SyntaxError s) {
        System.out.println(s.getMessage());
        return null; }
    return programAST;
  }

///////////////////////////////////////////////////////////////////////////////
//
// LITERALS
//
///////////////////////////////////////////////////////////////////////////////

// parseIntegerLiteral parses an integer-literal, and constructs
// a leaf AST to represent it.

  IntegerLiteral parseIntegerLiteral() throws SyntaxError {
    IntegerLiteral IL = null;

    if (currentToken.kind == Token.INTLITERAL) {
      previousTokenPosition = currentToken.position;
      String spelling = currentToken.spelling;
      IL = new IntegerLiteral(spelling, previousTokenPosition);
      currentToken = lexicalAnalyser.scan();
    } else {
      IL = null;
      syntacticError("integer literal expected here", "");
    }
    return IL;
  }

// parseCharacterLiteral parses a character-literal, and constructs a leaf
// AST to represent it.

  CharacterLiteral parseCharacterLiteral() throws SyntaxError {
    CharacterLiteral CL = null;

    if (currentToken.kind == Token.CHARLITERAL) {
      previousTokenPosition = currentToken.position;
      String spelling = currentToken.spelling;
      CL = new CharacterLiteral(spelling, previousTokenPosition);
      currentToken = lexicalAnalyser.scan();
    } else {
      CL = null;
      syntacticError("character literal expected here", "");
    }
    return CL;
  }

// parseIdentifier parses an identifier, and constructs a leaf AST to
// represent it.

  Identifier parseIdentifier() throws SyntaxError {
    Identifier I = null;

    if (currentToken.kind == Token.IDENTIFIER) {
      previousTokenPosition = currentToken.position;
      String spelling = currentToken.spelling;
      I = new Identifier(spelling, previousTokenPosition);
      currentToken = lexicalAnalyser.scan();
    } else {
      I = null;
      syntacticError("identifier expected here", "");
    }
    return I;
  }

  LongIdentifier parseLongIdentifier() throws SyntaxError {
    Identifier initAST = parseIdentifier();
    PackageIdentifier pckgAST = null;
    Identifier iAST = null;
    SourcePosition longIdentifierPos = new SourcePosition();
    start(longIdentifierPos);
    if(currentToken.kind == Token.DOLLAR){
      acceptIt();
      pckgAST = (PackageIdentifier) initAST;
      iAST = parseIdentifier();
    }
    else{
      iAST = initAST;
    }
    finish(longIdentifierPos);
    return new LongIdentifier(pckgAST, iAST, longIdentifierPos);
  }
// parseOperator parses an operator, and constructs a leaf AST to
// represent it.

  Operator parseOperator() throws SyntaxError {
    Operator O = null;

    if (currentToken.kind == Token.OPERATOR) {
      previousTokenPosition = currentToken.position;
      String spelling = currentToken.spelling;
      O = new Operator(spelling, previousTokenPosition);
      currentToken = lexicalAnalyser.scan();
    } else {
      O = null;
      syntacticError("operator expected here", "");
    }
    return O;
  }
  
  
//
///////////////////////////////////////////////////////////////////////////////

// parseCommand parses the command, and constructs an AST
// to represent its phrase structure.

  Command parseCommand() throws SyntaxError {
    Command commandAST = null; // in case there's a syntactic error

    SourcePosition commandPos = new SourcePosition();

    start(commandPos);
  
    commandAST = parseSingleCommand();
    while (currentToken.kind == Token.SEMICOLON) {
      acceptIt();
      Command c2AST = parseSingleCommand();    
      finish(commandPos);
      commandAST = new SequentialCommand(commandAST, c2AST, commandPos);
    }
    return commandAST;
  }

  /*
   * (POSSIBLE) V-name ::= Expression  ????
   * */
  Command parseSingleCommand() throws SyntaxError {  
    Command commandAST = null; // in case there's a syntactic error

    SourcePosition commandPos = new SourcePosition();
    start(commandPos);

    switch (currentToken.kind) { //Listo

    case Token.IDENTIFIER:    //Long identifier
      {
        CompoundIdentifier iAST = parseCompoundIdentifier();
        if (currentToken.kind == Token.LPAREN) {
          acceptIt();
          ActualParameterSequence apsAST = parseActualParameterSequence();
          accept(Token.RPAREN);
          finish(commandPos);
          commandAST = new CallCommand(iAST.identifier, apsAST, commandPos);

        } else {
          VName vAST = parseRestOfVname(iAST);
          accept(Token.BECOMES);
          Expression eAST = parseExpression();
          finish(commandPos);
          commandAST = new AssignCommand(vAST, eAST, commandPos);
        }
      }
      break;
        
      case Token.CHOOSE:
      {
        acceptIt();
        Expression eAST = parseExpression();
        accept(Token.FROM);
        Command casesAST = parseCases();
        accept(Token.END);
        finish(commandPos);
        commandAST = new ChooseCommand(eAST, casesAST, commandPos);
      }
      break;
      
       case Token.IF:
      {
        acceptIt();
        Expression eAST = parseExpression();
        accept(Token.THEN);
        Command c1AST = parseCommand();
        accept(Token.ELSE);
        Command c2AST = parseCommand();
        finish(commandPos);
        commandAST = new IfCommand(eAST, c1AST, c2AST, commandPos);
      }
      break;

    case Token.LET:
      {
        acceptIt();
        Declaration dAST = parseDeclaration();
        accept(Token.IN);
        Command cAST = parseCommand();
        accept(Token.END);
        finish(commandPos);
        commandAST = new LetCommand(dAST, cAST, commandPos);
      }
      break;
        
    case Token.LOOP:
    {
      acceptIt();
      LoopCases loopCasesAST = parseLoopCases();
      finish(commandPos);
      commandAST = new CallLoopCases(loopCasesAST, commandPos);
    }
    break;

    case Token.PASS:{
        acceptIt();
         finish(commandPos);
        commandAST = new EmptyCommand(commandPos);
    }break;
        
    case Token.EOT:{
      finish(commandPos);
      commandAST = new EmptyCommand(commandPos);
    }break;

    default:
      syntacticError("\"%\" cannot start a command",
        currentToken.spelling);
      break;

    }

    return commandAST;
  }
  
  LoopCases parseLoopCases() throws SyntaxError{ //Listo
    LoopCases loopCasesAST = null;
    SourcePosition loopCasesPos = new SourcePosition();
    
    
    switch(currentToken.kind){
      case Token.DO:{
        acceptIt();
        Command commandAST = parseCommand();
        DoLoop doLoopAST = parseDoLoop();
        finish(loopCasesPos);
        loopCasesAST = new LoopCasesDo(commandAST,doLoopAST, loopCasesPos);
      }break;
      
      case Token.FOR:{
        acceptIt();
        Identifier identifierAST = parseIdentifier();
        accept(Token.FROM);
        Expression eAST = parseExpression();
        accept(Token.TO);
        Expression e2AST = parseExpression();
        ForLoop forLoopAST = parseForLoop();
        finish(loopCasesPos);
        loopCasesAST = new LoopCasesFOR(identifierAST,eAST,e2AST,forLoopAST,loopCasesPos);
      }break;
      
      case Token.UNTIL:{
        acceptIt();
        Expression eAST = parseExpression();
        accept(Token.DO);
        Command commandAST = parseCommand();
        accept(Token.END);
        finish(loopCasesPos);
        loopCasesAST = new LoopCasesUntil(eAST,commandAST, loopCasesPos);
      } break;
        
      case Token.WHILE:{
        acceptIt();
        Expression eAST = parseExpression();
        accept(Token.DO);
        Command commandAST = parseCommand();
        accept(Token.END);
        finish(loopCasesPos);
        loopCasesAST = new LoopCasesWhile(eAST,commandAST, loopCasesPos);
      }break;

      default:
      syntacticError("\"%\" cannot start a LoopCases",
        currentToken.spelling);
      break;
    }
    return loopCasesAST;
  }
  
  DoLoop parseDoLoop() throws SyntaxError{//Listo
    DoLoop doLoopAST = null;
    SourcePosition doLoopPos = new SourcePosition();
    start(doLoopPos);
    switch (currentToken.kind) { //Listo
      
      case Token.UNTIL:
      {
        acceptIt();
        Expression eAST = parseExpression();
        accept(Token.END);
        finish(doLoopPos);
        doLoopAST = new DoLoopUntil(eAST, doLoopPos);
      }break;
        
      case Token.WHILE:
      {
        acceptIt();
        Expression eAST = parseExpression();
        accept(Token.END);
        finish(doLoopPos);
        doLoopAST = new DoLoopWhile(eAST, doLoopPos);
      } 
      break;
        
      default:
      syntacticError("\"%\" cannot start a do loop",
        currentToken.spelling);
      break;
    }
    finish(doLoopPos);
    return doLoopAST;
  }
  
  ForLoop parseForLoop() throws SyntaxError{ //Listo
    ForLoop forLoopAST = null;
    SourcePosition forLoopPos = new SourcePosition();
    start(forLoopPos);
    switch (currentToken.kind){
      case Token.DO:
      {
        acceptIt();
        Command cmdAST = parseCommand();
        accept(Token.END);
        finish(forLoopPos);
        forLoopAST = new ForLoopDo(cmdAST, forLoopPos);
      }
      break;
        
              
      case Token.UNTIL:
      {
        acceptIt();
        Expression eAST = parseExpression();
        accept(Token.DO);
        Command cmdAST = parseCommand();
        accept(Token.END);
        finish(forLoopPos);
        forLoopAST = new ForLoopUntil(eAST, cmdAST, forLoopPos);        
      }
      break;
        
      case Token.WHILE:
      {
        acceptIt();
        Expression eAST = parseExpression();
        accept(Token.DO);
        Command cmdAST = parseCommand();
        accept(Token.END);
        finish(forLoopPos);
        forLoopAST = new ForLoopWhile(eAST, cmdAST, forLoopPos);
      }
      break;
      
      default:
        syntacticError("\"%\" cannot start a for loop",
          currentToken.spelling);
        break;
    }
    finish(forLoopPos);
    return forLoopAST;
  }

  Cases parseCases() throws SyntaxError{
    Case caseAST = null;
    ElseCase elseCaseAST = null;
    SourcePosition casePos = new SourcePosition();
    start(casePos);
    caseAST = parseCase();
    finish(casePos);
    while(currentToken.kind == Token.WHEN){
      Case case2AST = parseCase();
      finish(casePos);
      caseAST = new SequentialCase(caseAST, case2AST,casePos);
    }
    
    if(currentToken.kind == Token.ELSE){
      elseCaseAST = parseElseCase();
      finish(casePos);
    }
    Cases casesAST = new Cases(caseAST,elseCaseAST,casePos);
    return casesAST;
  }
  
  Case parseCase() throws SyntaxError{
    Case caseAST = null;
    SourcePosition casePos = new SourcePosition();
    start(casePos);
    accept(Token.WHEN);
    CaseLiterals caseLitAST = parseCaseLiterals();
    accept(Token.THEN);
    Command cmdAST = parseCommand();
    finish(casePos);
    caseAST = new CaseWhen(caseLitAST, cmdAST, casePos);
    return caseAST;
  }
  
  ElseCase parseElseCase() throws SyntaxError{
    ElseCase elseCaseAST = null;
    SourcePosition elseCasePos = new SourcePosition();
    start(elseCasePos);
    accept(Token.ELSE);
    Command cmdAST = parseCommand();
    finish(elseCasePos);
    elseCaseAST = new ElseCase(cmdAST, elseCasePos);
    return elseCaseAST;
  }
  
  CaseLiterals parseCaseLiterals() throws SyntaxError{
    CaseLiterals caseLiteralsAST = null;
    SourcePosition caseLiteralsPos = new SourcePosition();
    start(caseLiteralsPos);
    CaseRange caseRangeAST = parseCaseRange();
    finish(caseLiteralsPos);
    while(currentToken.kind == Token.PIPE){
      acceptIt();
      CaseRange caseRange2AST = parseCaseRange();
      finish(caseLiteralsPos);
      caseRangeAST = new SequentialCaseRange(caseRangeAST, caseRange2AST,caseLiteralsPos);
    }
    
    caseLiteralsAST = new CaseLiterals(caseRangeAST,caseLiteralsPos);
    return caseLiteralsAST;
  }
    
    
  CaseRange parseCaseRange() throws SyntaxError{
    CaseLiteral caseLiteral2AST = null;
    
    SourcePosition caseRangePos = new SourcePosition();
    start(caseRangePos);
    CaseLiteral caseLiteralAST = parseCaseLiteral();
    
    if(currentToken.kind == Token.DOUBLEDOT){
      acceptIt();
      caseLiteral2AST = parseCaseLiteral();
    }
    finish(caseRangePos);
    CaseRange caseRangeAST = new CaseRangeCase(caseLiteralAST, caseLiteral2AST ,caseRangePos);
    return caseRangeAST;
  }
  
  CaseLiteral parseCaseLiteral() throws SyntaxError{ // listo
    CaseLiteral caseLiteralAST = null;
    SourcePosition caseLiteralPos = new SourcePosition();
    start(caseLiteralPos);
    
    switch (currentToken.kind) {
        
      case Token.INTLITERAL:
      {
        IntegerLiteral ilAST = parseIntegerLiteral();
        finish(caseLiteralPos);
        caseLiteralAST = new CaseLiteralINT(ilAST, caseLiteralPos);
      } 
      break;
      case Token.CHARLITERAL:
      {
        CharacterLiteral clAST= parseCharacterLiteral();
        finish(caseLiteralPos);
        caseLiteralAST = new CaseLiteralCHAR(clAST, caseLiteralPos);
      }
      break;
      default:
      syntacticError("\"%\" cannot start a case-literal",
        currentToken.spelling);
      break;
    }
    finish(caseLiteralPos);
    return caseLiteralAST;
  }
  
///////////////////////////////////////////////////////////////////////////////
//
// EXPRESSIONS
//
///////////////////////////////////////////////////////////////////////////////

  Expression parseExpression() throws SyntaxError {
    Expression expressionAST = null; // in case there's a syntactic error

    SourcePosition expressionPos = new SourcePosition();

    start (expressionPos);

    switch (currentToken.kind) { //Listo

    case Token.INTLITERAL:
    case Token.CHARLITERAL:
    case Token.IDENTIFIER:
    case Token.OPERATOR:
    case Token.LPAREN:
    case Token.LBRACKET:
    case Token.LCURLY:
      {
        Expression sExprAST = parseSecondaryExpression();
        expressionAST = new SecExpression(sExprAST, expressionPos);
      }
      break;
    
    case Token.IF:
      {
        acceptIt();
        Expression e1AST = parseExpression();
        accept(Token.THEN);
        Expression e2AST = parseExpression();
        accept(Token.ELSE);
        Expression e3AST = parseExpression();
        finish(expressionPos);
        expressionAST = new IfExpression(e1AST, e2AST, e3AST, expressionPos);
      }
      break;
    
    case Token.LET:
      {
        acceptIt();
        Declaration dAST = parseDeclaration();
        accept(Token.IN);
        Expression eAST = parseExpression();
        finish(expressionPos);
        expressionAST = new LetExpression(dAST, eAST, expressionPos);
      }
      break;

    default:
      syntacticError("\"%\" cannot start an expression",
        currentToken.spelling);
      break;
    }
    return expressionAST;
  }

  Expression parseSecondaryExpression() throws SyntaxError {
    Expression expressionAST = null; // in case there's a syntactic error

    SourcePosition expressionPos = new SourcePosition();
    start(expressionPos);

    expressionAST = parsePrimaryExpression();
    while (currentToken.kind == Token.OPERATOR) {
      Operator opAST = parseOperator();
      Expression e2AST = parsePrimaryExpression();
      expressionAST = new BinaryExpression (expressionAST, opAST, e2AST,
        expressionPos);
    }
    return expressionAST;
  }

 Expression parsePrimaryExpression() throws SyntaxError {
    Expression expressionAST = null; // in case there's a syntactic error

    SourcePosition expressionPos = new SourcePosition();
    start(expressionPos);

    switch (currentToken.kind) { //listo

    case Token.INTLITERAL:
      {
        IntegerLiteral ilAST = parseIntegerLiteral();
        finish(expressionPos);
        expressionAST = new IntegerExpression(ilAST, expressionPos);
      }
      break;

    case Token.CHARLITERAL:
      {
        CharacterLiteral clAST= parseCharacterLiteral();
        finish(expressionPos);
        expressionAST = new CharacterExpression(clAST, expressionPos);
      }
      break;
 
    case Token.IDENTIFIER: //Long Identifier or V-Name Case
      {
        CompoundIdentifier ciAST = parseCompoundIdentifier();
        if(currentToken.kind == Token.LPAREN){  //Long Identifier
          LongIdentifier liAST = new LongIdentifier(ciAST.packageIdentifier, ciAST.identifier, ciAST.position);
          acceptIt();
          ActualParameterSequence apsAST = parseActualParameterSequence();
          accept(Token.RPAREN);
          finish(expressionPos);
          expressionAST = new CallExpression(liAST, apsAST, expressionPos);
        }
        else{ //V-Name
          VName vAST = parseRestOfVname(ciAST);
          finish(expressionPos);
          expressionAST = new AssignExpression(vAST, expressionPos);
        }
      }
      break;

    case Token.OPERATOR:
      {
        Operator opAST = parseOperator();
        Expression ex1AST = parsePrimaryExpression();
        finish(expressionPos);
        expressionAST = new OperatorExpression(opAST, ex1AST, expressionPos);
      }
      break;
        
    case Token.LPAREN:
        {
          acceptIt();
          Expression ex1AST = parseExpression();
          accept(Token.RPAREN);
          finish(expressionPos);
          expressionAST = new LParenExpression(ex1AST, expressionPos);
        }
        break;
    case Token.LBRACKET:
      {
        acceptIt();
        ArrayAggregate aaAST = parseArrayAggregate();
        accept(Token.RBRACKET);
        finish(expressionPos);
        expressionAST = new LBracketExpression(aaAST, expressionPos);
      }
      break;
        
    case Token.LCURLY:
      {
        acceptIt();
        RecordAggregate raAST = parseRecordAggregate();
        accept(Token.RCURLY);
        finish(expressionPos);
        expressionAST = new LCurlyExpression(raAST, expressionPos);
      }
      break;

    default:
      syntacticError("\"%\" cannot start an expression",
        currentToken.spelling);
      break;

    }
    return expressionAST;
  }

///////////////////////////////////////////////////////////////////////////////
//
// AGGREGATES
//
///////////////////////////////////////////////////////////////////////////////

  RecordAggregate parseRecordAggregate() throws SyntaxError {
    RecordAggregate aggregateAST = null; // in case there's a syntactic error

    SourcePosition aggregatePos = new SourcePosition();
    start(aggregatePos);

    Identifier iAST = parseIdentifier();
    accept(Token.IS);
    Expression eAST = parseExpression();

    if (currentToken.kind == Token.COMMA) {
      acceptIt();
      RecordAggregate aAST = parseRecordAggregate();
      finish(aggregatePos);
      aggregateAST = new MultipleRecordAggregate(iAST, eAST, aAST, aggregatePos);
    } else {
      finish(aggregatePos);
      aggregateAST = new SingleRecordAggregate(iAST, eAST, aggregatePos);
    }
    return aggregateAST;
  }

  ArrayAggregate parseArrayAggregate() throws SyntaxError {
    ArrayAggregate aggregateAST = null; // in case there's a syntactic error

    SourcePosition aggregatePos = new SourcePosition();
    start(aggregatePos);

    Expression eAST = parseExpression();
    if (currentToken.kind == Token.COMMA) {
      acceptIt();
      ArrayAggregate aAST = parseArrayAggregate();
      finish(aggregatePos);
      aggregateAST = new MultipleArrayAggregate(eAST, aAST, aggregatePos);
    } else {
      finish(aggregatePos);
      aggregateAST = new SingleArrayAggregate(eAST, aggregatePos);
    }
    return aggregateAST;
  }

///////////////////////////////////////////////////////////////////////////////
//
// VALUE-OR-VARIABLE NAMES
//
///////////////////////////////////////////////////////////////////////////////

  CompoundIdentifier parseCompoundIdentifier() throws SyntaxError{
    Identifier initAST = parseIdentifier();
    PackageIdentifier pckgAST = null;
    Identifier iAST = null;
    SourcePosition compoundIdentifierPos = new SourcePosition();
    start(compoundIdentifierPos);
    if(currentToken.kind == Token.DOLLAR){
      acceptIt();
      pckgAST = (PackageIdentifier) initAST;
      iAST = parseIdentifier();
    }
    else{
      iAST = initAST;
    }
    finish(compoundIdentifierPos);
    return new CompoundIdentifier(iAST,pckgAST, compoundIdentifierPos);
  }
  VName parseVname () throws SyntaxError {
    VName vnameAST = null; // in case there's a syntactic errorReporter
    SourcePosition vnamePos = new SourcePosition();
    start(vnamePos);
    Identifier initAST = parseIdentifier();
    PackageIdentifier pckgAST = null;
    Identifier iAST = null;
    if(currentToken.kind == Token.DOLLAR){
      acceptIt();
      pckgAST = (PackageIdentifier) initAST;
      iAST = parseIdentifier();
    }
    else{
      iAST = initAST;
    }
    finish(vnamePos);
    vnameAST = parseRestOfVname(new CompoundIdentifier(iAST,pckgAST,vnamePos));
    return vnameAST;
  }

  VName parseRestOfVname(CompoundIdentifier cmpdIdentifier) throws SyntaxError {
    SourcePosition vnamePos = new SourcePosition();
    vnamePos = cmpdIdentifier.position;
    PackageIdentifier pckgAST = cmpdIdentifier.packageIdentifier;
    Identifier identifierAST = cmpdIdentifier.identifier;
    VName vAST = new SimpleVname(identifierAST, pckgAST, vnamePos); //TODO @giulliano Package was added to the simple Vname

    while (currentToken.kind == Token.DOT ||
           currentToken.kind == Token.LBRACKET) {

      if (currentToken.kind == Token.DOT) {
        acceptIt();
        Identifier iAST = parseIdentifier();
        vAST = new DotVname(vAST, iAST, vnamePos);
      } else {
        acceptIt();
        Expression eAST = parseExpression();
        accept(Token.RBRACKET);
        finish(vnamePos);
        vAST = new SubscriptVname(vAST, eAST, vnamePos);
      }
    }
    return vAST;
  }

///////////////////////////////////////////////////////////////////////////////
//
// DECLARATIONS
//
///////////////////////////////////////////////////////////////////////////////

  Declaration parseDeclaration() throws SyntaxError {
    Declaration declarationAST = null; // in case there's a syntactic error

    SourcePosition declarationPos = new SourcePosition();
    start(declarationPos);
    CompoundDeclaration cdAST = parseCompoundDeclaration();
    while (currentToken.kind == Token.SEMICOLON) {
      acceptIt();
      Declaration cd1AST =(Declaration) parseCompoundDeclaration();
      finish(declarationPos);
      declarationAST = new SequentialDeclaration(cdAST, cd1AST,
        declarationPos);
    }
    return declarationAST;
  }

  
  CompoundDeclaration parseCompoundDeclaration()throws SyntaxError 
  {
    CompoundDeclaration compoundDeclarationAST = null;
    SourcePosition compoundDeclarationPos = new SourcePosition();
    start(compoundDeclarationPos);
    
    switch (currentToken.kind){//listo
  
       case Token.PAR:{
        acceptIt();
        SingleDeclaration sdAST = (SingleDeclaration) parseSingleDeclaration();
        accept(Token.PIPE);
        SingleDeclaration sd2AST = (SingleDeclaration) parseSingleDeclaration();
        compoundDeclarationAST = new SequentialSingleDeclaration(sdAST,sd2AST,compoundDeclarationPos);
        
        while (currentToken.kind == Token.PIPE) {
          acceptIt();
          SingleDeclaration sd3AST = (SingleDeclaration) parseSingleDeclaration(); 
          finish(compoundDeclarationPos);
          compoundDeclarationAST = new SequentialSingleDeclaration(compoundDeclarationAST, sd3AST, compoundDeclarationPos);
        }
        accept(Token.END);
        finish(compoundDeclarationPos);
        
      }break;
      case Token.PRIVATE:{
        acceptIt();
        Declaration dAST = parseDeclaration();
        accept(Token.IN);
        Declaration d2AST = parseDeclaration();
        accept(Token.END);
        finish(compoundDeclarationPos);
        compoundDeclarationAST = new CompoundDeclarationPrivate(dAST,d2AST, compoundDeclarationPos);
      }break;
      
      case Token.RECURSIVE:{
        acceptIt();
        ProcFuncs pfAST = parseProcFuncs();
        accept(Token.END);
        finish(compoundDeclarationPos);
        compoundDeclarationAST = new CompoundDeclarationRecursive(pfAST, compoundDeclarationPos);
      }break;
     
        default:
          try{
            Declaration sdAST = (Declaration) parseSingleDeclaration();
            finish(compoundDeclarationPos);
            compoundDeclarationAST = new CompoundDeclarationSingleDeclaration(sdAST, compoundDeclarationPos); 
          }
          catch(SyntaxError e){
            syntacticError("\"%\" cannot start a CompoundDeclaration",
                           currentToken.spelling);
          }
          break;

    }
    return compoundDeclarationAST;
  }

  Declaration parseSingleDeclaration() throws SyntaxError {
    Declaration declarationAST = null;

    SourcePosition declarationPos = new SourcePosition();
    start(declarationPos);

    switch (currentToken.kind) { //Listo

    case Token.CONST:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.IS);
        Expression eAST = parseExpression();
        finish(declarationPos);
        declarationAST = new ConstDeclaration(iAST, eAST, declarationPos);
      }
      break;
        
    case Token.FUNC:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.LPAREN);
        FormalParameterSequence fpsAST = parseFormalParameterSequence();
        accept(Token.RPAREN);
        accept(Token.COLON);
        TypeDenoter tAST = parseTypeDenoter();
        accept(Token.IS);
        Expression eAST = parseExpression();
        finish(declarationPos);
        declarationAST = new FuncDeclaration(iAST, fpsAST, tAST, eAST,
          declarationPos);
      }
      break;
      
    case Token.PROC:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.LPAREN);
        FormalParameterSequence fpsAST = parseFormalParameterSequence();
        accept(Token.RPAREN);
        accept(Token.IS);
        Command cAST = parseSingleCommand();
        finish(declarationPos);
        declarationAST = new ProcDeclaration(iAST, fpsAST, cAST, declarationPos);
      }
      break;

    case Token.TYPE:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.IS);
        TypeDenoter tAST = parseTypeDenoter();
        finish(declarationPos);
        declarationAST = new TypeDeclaration(iAST, tAST, declarationPos);
      }
      break;

    case Token.VAR:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        VarSingleDeclaration vsdAST = parseVarSingleDeclaration();
        finish(declarationPos);
        declarationAST = new VarDeclaration(iAST, vsdAST, declarationPos);
      }
      break;

    default:
      syntacticError("\"%\" cannot start a declaration",
        currentToken.spelling);
      break;

    }
    return declarationAST;
  }
  
  VarSingleDeclaration parseVarSingleDeclaration() throws SyntaxError {
    VarSingleDeclaration varSingleDAST = null; // in case there's a syntactic error
    SourcePosition varSingleDPos = new SourcePosition();
    start(varSingleDPos);
    if (currentToken.kind == Token.COLON) {
      acceptIt();
      TypeDenoter tAST = parseTypeDenoter();
      finish(varSingleDPos);
      varSingleDAST = new VarSingleDeclarationColon(tAST, varSingleDPos);
    } else if (currentToken.kind == Token.SINGLEDECLARATION){
      acceptIt();
      Expression eAST = parseExpression();
      finish(varSingleDPos);
      varSingleDAST = new VarSingleDeclarationSingleDeclaration(eAST,varSingleDPos);
    }
      else{
      syntacticError("\"%\" is not a valid single declaration starter",
        currentToken.spelling);
      }
    return varSingleDAST;
  }
  
///////////////////////////////////////////////////////////////////////////////
//
// PROCEDURES OR FUNCTIONS
//
///////////////////////////////////////////////////////////////////////////////

  ProcFunc parseProcFunc() throws SyntaxError {
    ProcFunc procFuncAST = null; // in case there's a syntactic error
    SourcePosition procFuncPos = new SourcePosition();
    start(procFuncPos);
    if (currentToken.kind == Token.PROC) {
      acceptIt();
      Identifier iAST = parseIdentifier();
      accept(Token.LPAREN);
      FormalParameterSequence fpsAST = parseFormalParameterSequence();
      accept(Token.RPAREN);
      accept(Token.IS);
      Command cmndAST = parseCommand();
      accept(Token.END);
      finish(procFuncPos);
      procFuncAST = new ProcProcFunc(iAST, fpsAST, cmndAST, procFuncPos);
    } else if (currentToken.kind == Token.FUNC){
      acceptIt();
      Identifier iAST = parseIdentifier();
      accept(Token.LPAREN);
      FormalParameterSequence fpsAST = parseFormalParameterSequence();
      accept(Token.RPAREN);
      accept(Token.COLON);
      TypeDenoter tAST = parseTypeDenoter();
      accept(Token.IS);
      Expression eAST = parseExpression();
      finish(procFuncPos);
      procFuncAST = new FuncProcFunc(iAST,fpsAST,tAST,eAST,procFuncPos);
    }
      else{
      syntacticError("\"%\" is not a valid procedure or function starter",
        currentToken.spelling);
      }
    return procFuncAST;
  }

ProcFuncs parseProcFuncs() throws SyntaxError {
    ProcFuncs procFuncsAST = null; // in case there's a syntactic error
    SourcePosition procFuncsPos = new SourcePosition();
    start(procFuncsPos);
    ProcFunc procFuncAST = parseProcFunc();
    ProcFunc procFunc1AST = parseProcFunc();
    procFuncAST = new ProcFuncs(procFuncAST, procFunc1AST, procFuncsPos);
    while (currentToken.kind == Token.PIPE) {
      acceptIt();
      procFunc1AST = parseProcFunc();
      finish(procFuncsPos);
      procFuncAST = new ProcFuncs(procFuncAST, procFunc1AST, procFuncsPos);
    }
    return procFuncsAST;
  }


///////////////////////////////////////////////////////////////////////////////
//
// PARAMETERS
//
///////////////////////////////////////////////////////////////////////////////

FormalParameterSequence parseFormalParameterSequence() throws SyntaxError {
    FormalParameterSequence formalsAST = null;

    SourcePosition formalsPos = new SourcePosition();
    start(formalsPos);
    if (currentToken.kind == Token.RPAREN) {
      finish(formalsPos);
      formalsAST = new EmptyFormalParameterSequence(formalsPos);

    } else {
      formalsAST = parseProperFormalParameterSequence();
    }
    return formalsAST;
  }

  FormalParameterSequence parseProperFormalParameterSequence() throws SyntaxError {
    FormalParameterSequence formalsAST = null; // in case there's a syntactic error;

    SourcePosition formalsPos = new SourcePosition();
    start(formalsPos);
    FormalParameter fpAST = parseFormalParameter();
    if (currentToken.kind == Token.COMMA) {
      acceptIt();
      FormalParameterSequence fpsAST = parseProperFormalParameterSequence();
      finish(formalsPos);
      formalsAST = new MultipleFormalParameterSequence(fpAST, fpsAST,
        formalsPos);

    } else {
      finish(formalsPos);
      formalsAST = new SingleFormalParameterSequence(fpAST, formalsPos);
    }
    return formalsAST;
  }

  FormalParameter parseFormalParameter() throws SyntaxError {
    FormalParameter formalAST = null; // in case there's a syntactic error;

    SourcePosition formalPos = new SourcePosition();
    start(formalPos);

    switch (currentToken.kind) { //Listo

    case Token.IDENTIFIER:
      {
        Identifier iAST = parseIdentifier();
        accept(Token.COLON);
        TypeDenoter tAST = parseTypeDenoter();
        finish(formalPos);
        formalAST = new ConstFormalParameter(iAST, tAST, formalPos);
      }
      break;
        
		case Token.FUNC:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.LPAREN);
        FormalParameterSequence fpsAST = parseFormalParameterSequence();
        accept(Token.RPAREN);
        accept(Token.COLON);
        TypeDenoter tAST = parseTypeDenoter();
        finish(formalPos);
        formalAST = new FuncFormalParameter(iAST, fpsAST, tAST, formalPos);
      }
      break;
		
    case Token.PROC:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.LPAREN);
        FormalParameterSequence fpsAST = parseFormalParameterSequence();
        accept(Token.RPAREN);
        finish(formalPos);
        formalAST = new ProcFormalParameter(iAST, fpsAST, formalPos);
      }
      break;
        
    case Token.VAR:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.COLON);
        TypeDenoter tAST = parseTypeDenoter();
        finish(formalPos);
        formalAST = new VarFormalParameter(iAST, tAST, formalPos);
      }
      break;

    default:
      syntacticError("\"%\" cannot start a formal parameter",
        currentToken.spelling);
      break;

    }
    return formalAST;
  }

  ActualParameterSequence parseActualParameterSequence() throws SyntaxError {
    ActualParameterSequence actualsAST;

    SourcePosition actualsPos = new SourcePosition();

    start(actualsPos);
    if (currentToken.kind == Token.RPAREN) {
      finish(actualsPos);
      actualsAST = new EmptyActualParameterSequence(actualsPos);

    } else {
      actualsAST = parseProperActualParameterSequence();
    }
    return actualsAST;
  }

  ActualParameterSequence parseProperActualParameterSequence() throws SyntaxError {
    ActualParameterSequence actualsAST = null;

    SourcePosition actualsPos = new SourcePosition();

    start(actualsPos);
    ActualParameter apAST = parseActualParameter();
    if (currentToken.kind == Token.COMMA) {
      acceptIt();
      ActualParameterSequence apsAST = parseProperActualParameterSequence();
      finish(actualsPos);
      actualsAST = new MultipleActualParameterSequence(apAST, apsAST,
        actualsPos);
    } else {
      finish(actualsPos);
      actualsAST = new SingleActualParameterSequence(apAST, actualsPos);
    }
    return actualsAST;
  }

  ActualParameter parseActualParameter() throws SyntaxError {
    ActualParameter actualAST = null; // in case there's a syntactic error
    SourcePosition actualPos = new SourcePosition();
    start(actualPos);
    switch (currentToken.kind) { //Listo
		case Token.FUNC:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        finish(actualPos);
        actualAST = new FuncActualParameter(iAST, actualPos);
      }
      break;
        
    case Token.PROC:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        finish(actualPos);
        actualAST = new ProcActualParameter(iAST, actualPos);
      }
      break;
        
    case Token.VAR:
      {
        acceptIt();
        VName vAST = parseVname();
        finish(actualPos);
        actualAST = new VarActualParameter(vAST, actualPos);
      }
      break;

    default:
         
        try{
        Expression eAST = parseExpression();
        finish(actualPos);
        actualAST = new ConstActualParameter(eAST, actualPos);
        }
        catch(SyntaxError e){
        syntacticError("\"%\" cannot start an actual parameter",
        currentToken.spelling);
        }
      break;
    }
    return actualAST;
  }

///////////////////////////////////////////////////////////////////////////////
//
// TYPE-DENOTERS
//
///////////////////////////////////////////////////////////////////////////////

  TypeDenoter parseTypeDenoter() throws SyntaxError {
    TypeDenoter typeAST = null; // in case there's a syntactic error
    SourcePosition typePos = new SourcePosition();

    start(typePos);

    switch (currentToken.kind) { //Listo

  
    case Token.IDENTIFIER: // Long Identifier
      {
        LongIdentifier iAST = parseLongIdentifier();
        finish(typePos);
        typeAST = new TypeDenoterLongIdentifier(iAST, typePos);
      }break;
        
    case Token.ARRAY:
      {
        acceptIt();
        IntegerLiteral ilAST = parseIntegerLiteral();
        accept(Token.OF);
        TypeDenoter tAST = parseTypeDenoter();
        finish(typePos);
        typeAST = new ArrayTypeDenoter(ilAST, tAST, typePos);
      }
      break;

    case Token.RECORD:
      {
        acceptIt();
        RecordTypeDenoter rtdAST = parseRecordTypeDenoter();
        accept(Token.END);
        finish(typePos);
        typeAST = new RTypeDenoter(rtdAST, typePos);
      }
      break;

    default:
      syntacticError("\"%\" cannot start a type denoter",
        currentToken.spelling);
      break;

    }
    return typeAST;
  }

  RecordTypeDenoter parseRecordTypeDenoter() throws SyntaxError {
    RecordTypeDenoter recordAST = null; // in case there's a syntactic error

    SourcePosition recordPos = new SourcePosition();

    start(recordPos);
    Identifier iAST = parseIdentifier();
    accept(Token.COLON);
    TypeDenoter tAST = parseTypeDenoter();
    if (currentToken.kind == Token.COMMA) {
      acceptIt();
      RecordTypeDenoter rAST = parseRecordTypeDenoter();
      finish(recordPos);
      recordAST = new MultipleRecordTypeDenoter(iAST, tAST, rAST, recordPos);
    } else {
      finish(recordPos);
      recordAST = new SingleRecordTypeDenoter(iAST, tAST, recordPos);
    }
    return recordAST;
  }

  
///////////////////////////////////////////////////////////////////////////////
//
// PACKAGE DECLARATION
//
///////////////////////////////////////////////////////////////////////////////

  PackageDeclaration parsePackageDeclaration() throws SyntaxError {
    PackageDeclaration packageDeclarationAST = null; // in case there's a syntactic error
    SourcePosition packageDeclarationPos = new SourcePosition();
    start(packageDeclarationPos);
    accept(Token.PACKAGE);
    Identifier iAST = (Identifier) parseIdentifier();
    accept(Token.IS);
    Declaration dAST = parseDeclaration();
    accept(Token.END);
    finish(packageDeclarationPos);
    packageDeclarationAST = new PackageDeclaration(iAST, dAST, packageDeclarationPos);
    
    return packageDeclarationAST;
  }
}