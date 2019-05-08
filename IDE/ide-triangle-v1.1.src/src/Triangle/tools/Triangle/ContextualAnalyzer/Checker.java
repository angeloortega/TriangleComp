/*
 * @(#)Checker.java                        2.1 2003/10/07
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

import Triangle.ErrorReporter;
import Triangle.StdEnvironment;
import Triangle.tools.Triangle.AbstractSyntaxTrees.*;
import Triangle.tools.Triangle.SyntacticAnalyzer.SourcePosition;
import Utilities.ChooseData;
import java.util.HashMap;

public final class Checker implements Visitor {
    //Nuevas cosas Richie-Giulla
 @Override
public Object visitMultipleRecordTypeDenoter(MultipleRecordTypeDenoter ast, Object o) {
    ast.TD = (TypeDenoter) ast.TD.visit(this, null);
    ast.RTD.visit(this, null);
    return ast;
}

@Override
public Object visitSingleRecordTypeDenoter(SingleRecordTypeDenoter ast, Object o) {
    ast.TD = (TypeDenoter) ast.TD.visit(this, null);
    return ast;
}

@Override
public Object visitRTypeDenoter(RTypeDenoter ast, Object o) {
    ast.REC = (RecordTypeDenoter) ast.REC.visit(this, o);
    return ast;
}

public Object visitTypeDenoterLongIdentifier(TypeDenoterLongIdentifier ast, Object o) {
  	Declaration binding = (Declaration) ast.longIdentifier.visit(this, null);
    if (binding == null) {
      reportUndeclared (ast.longIdentifier);
      return StdEnvironment.errorType;
    } else if (! (binding instanceof TypeDeclaration)) {
      reporter.reportError ("\"%\" is not a type longIdentifier",
                            ast.longIdentifier.spelling, ast.longIdentifier.position);
      return StdEnvironment.errorType;
    }
    return ((TypeDeclaration) binding).T;
  }

  public Object visitCompoundIdentifier(CompoundIdentifier ast, Object o) {
    IdentificationTable idTableTemp = hashIdTables.get(ast.packageIdentifier.spelling);
    Declaration binding = idTableTemp.retrieve(ast.identifier.spelling);
    if (binding != null)
      ast.identifier.decl = binding;
    return binding;
  }
    // Packages
    //region Giulla
    @Override
    public Object visitVarDeclaration(VarDeclaration ast, Object o) {
    VarSingleDeclarationSingleDeclaration binding = (VarSingleDeclarationSingleDeclaration) ast.V.visit(this, null);
    if (binding != null) {
        Expression expression = binding.T;
    	ast.I.type = (TypeDenoter) expression.visit(this, null);
    }
    idTable.enter (ast.I.spelling, ast);
    if (ast.duplicated)
      reporter.reportError ("identifier \"%\" already declared",
                            ast.I.spelling, ast.position);

    return null;
  }

    @Override
  public Object visitVarSingleDeclarationColon(VarSingleDeclarationColon ast, Object o) {
    ast.T = (TypeDenoter) ast.T.visit(this, null);
    return ast.T;
  }

    @Override
  public Object visitVarSingleDeclarationSingleDeclaration(VarSingleDeclarationSingleDeclaration ast, Object o) {
    ast.T.type = (TypeDenoter) ast.T.visit(this, null);
      return ast.T.type;
  }
    @Override

  public Object visitPackageDeclaration(PackageDeclaration ast, Object o) {
    idTable.enter(ast.ID.spelling, ast);
    if(hashIdTables.containsKey(ast.ID.spelling)){
      reporter.reportError ("Package identifier \"%\" already declared",
                            ast.ID.spelling, ast.position);
    }
    IdentificationTable packageTable = new IdentificationTable();
    hashIdTables.put(ast.ID.spelling, packageTable);
    ast.DEC.visit(this, ast.ID.spelling); // The package name is sent so the visitor knows which package to declare in
    return null;
  }
    @Override

  public Object visitSequentialPackageDeclaration(SequentialPackageDeclaration ast, Object o) {
    ast.D1.visit(this, null);
    ast.D2.visit(this, null);
    return null;
  }
    @Override
  public Object visitSimpleVname(SimpleVname ast, Object o) {
    ast.variable = false;
    ast.type = StdEnvironment.errorType;
    Declaration binding = (Declaration) ast.I.visit(this, null);
    if (binding == null)
      reportUndeclared(ast.I);
    else
      if (binding instanceof ConstDeclaration) {
        ast.type = ((ConstDeclaration) binding).E.type;
        ast.variable = false;
      } else if (binding instanceof VarDeclaration) {
        if (binding.getClass() == VarSingleDeclarationSingleDeclaration.class){
          ast.type = ((VarSingleDeclarationSingleDeclaration) binding).T.type;
        }
        else{
          ast.type = ((VarSingleDeclarationColon) binding).T;
        }
        ast.variable = true;
      } else if (binding instanceof ConstFormalParameter) {
        ast.type = ((ConstFormalParameter) binding).T;
        ast.variable = false;
      } else if (binding instanceof VarFormalParameter) {
        ast.type = ((VarFormalParameter) binding).T;
        ast.variable = true;
      } else
        reporter.reportError ("\"%\" is not a const or var identifier",
                              ast.I.spelling, ast.I.position);
    return ast.type;
  }
    //endregion
    // Commands

    // Always returns null. Does not use the given object.


  //region Richie

        @Override
	public Object visitConstDeclaration(ConstDeclaration ast, Object o) {
	    TypeDenoter eType = (TypeDenoter) ast.E.visit(this, null);
	    idTable.enter(ast.I.spelling, ast);
	    if (ast.duplicated)
	      reporter.reportError ("identifier \"%\" already declared",
	                            ast.I.spelling, ast.position);
	    return eType;
	 }

	@Override
    public Object visitCompoundDeclarationRecursive(CompoundDeclarationRecursive ast, Object o) {
        ast.PF.visit(this, null);
        return null;
    }

    @Override
    public Object visitProcFuncs(ProcFuncs ast, Object o) {
        for (int i = 0; i < 2; i++) {
          ast.PF1.visit(this, i);
          ast.PF2.visit(this, i);
        }
        return null;
    }

    @Override
    public Object visitProcProcFunc(ProcProcFunc ast, Object o) {
        Integer iteration = (Integer) o;
        if (iteration.equals(new Integer(0))) {
          idTable.enter (ast.ID.spelling, ast); // permits recursion
          if (ast.duplicated)
            reporter.reportError ("identifier \"%\" already declared",
                                  ast.ID.spelling, ast.position);
        }
        else {
          idTable.openScope();
          ast.FPS.visit(this, null);
          ast.COM.visit(this, null);
          idTable.closeScope();
        }
        return null;
    }

    @Override
    public Object visitFuncProcFunc(FuncProcFunc ast, Object o) {
        Integer iteration = (Integer) o;
        if (iteration.equals(0)) {
          idTable.enter (ast.ID.spelling, ast); // permits recursion
          if (ast.duplicated)
            reporter.reportError ("identifier \"%\" already declared",
                                  ast.ID.spelling, ast.position);
        }
        else {
          idTable.openScope();
          ast.FPS.visit(this, null);
          ast.TD = (TypeDenoter) ast.TD.visit(this, null);
          TypeDenoter eType = (TypeDenoter) ast.EXP.visit(this, null);
          idTable.closeScope();
          if (! ast.TD.equals(eType))
            reporter.reportError ("body of function \"%\" has wrong type",
                                  ast.ID.spelling, ast.EXP.position);
        }
        return null;
    }

    @Override
    public Object visitLoopCasesWhile(LoopCasesWhile ast, Object o) {
        TypeDenoter eType = (TypeDenoter) ast.EXP.visit(this, null);
        if (! eType.equals(StdEnvironment.booleanType))
          reporter.reportError ("Boolean expression expected here", "",
				ast.EXP.position);
        ast.COM.visit(this, null);
        return null;
    }

    @Override
    public Object visitLoopCasesUntil(LoopCasesUntil ast, Object o) {
        TypeDenoter eType = (TypeDenoter) ast.EXP.visit(this, null);
        if (! eType.equals(StdEnvironment.booleanType))
          reporter.reportError ("Boolean expression expected here", "",
				ast.EXP.position);
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
		TypeDenoter eType1 = (TypeDenoter) ast.DECL.visit(this, null);
      	if (! eType1.equals(StdEnvironment.integerType))
          reporter.reportError ("Integer expression expected here", "",
				ast.DECL.position); //TODO revisar si la posicion esta bien.
        TypeDenoter eType2 = (TypeDenoter) ast.EXP2.visit(this, null);
      	if (! eType2.equals(StdEnvironment.integerType))
          reporter.reportError ("Integer expression expected here", "",
				ast.EXP2.position);
        idTable.openScope(); //TODO los exp no deben accesar el identifier
        Object output = ast.FOR.visit(this, null);
        if (output != null) {
            TypeDenoter eType3 = (TypeDenoter) output;
            if (! eType3.equals(StdEnvironment.booleanType))
              reporter.reportError ("Boolean expression expected here", "",
            ast.FOR.position); //TODO revisar si la posicion esta bien.
        }
      	idTable.closeScope();
        return null;
    }

    @Override
    public Object visitDoLoopUntil(DoLoopUntil ast, Object o) {
        TypeDenoter eType = (TypeDenoter) ast.EXP.visit(this, null);
        if (! eType.equals(StdEnvironment.booleanType))
          reporter.reportError ("Boolean expression expected here", "",
				ast.EXP.position);
      	return null;
    }

    @Override
    public Object visitDoLoopWhile(DoLoopWhile ast, Object o) {
        TypeDenoter eType = (TypeDenoter) ast.EXP.visit(this, null);
        if (! eType.equals(StdEnvironment.booleanType))
          reporter.reportError ("Boolean expression expected here", "",
				ast.EXP.position);
      	return null;
    }

  	@Override
    public Object visitForLoopDo(ForLoopDo ast, Object o) {
      	ast.COM.visit(this, null);
        return null;
    }

    @Override
    public Object visitForLoopUntil(ForLoopUntil ast, Object o) {
      	TypeDenoter eType = (TypeDenoter) ast.EXP.visit(this, null);
        ast.COM.visit(this, null);
        return eType;
    }

    @Override
    public Object visitForLoopWhile(ForLoopWhile ast, Object o) {
      	TypeDenoter eType = (TypeDenoter) ast.EXP.visit(this, null);
        ast.COM.visit(this, null);
        return eType;
    }
    //endregion


    //region Jose
    @Override
    public Object visitCaseLiteralINT(CaseLiteralINT ast, Object o) {
        return StdEnvironment.integerType;
    }

@Override
    public Object visitCaseLiteralCHAR(CaseLiteralCHAR ast, Object o) {
        return StdEnvironment.charType;
    }


 @Override
    public Object visitSequentialCaseRange(SequentialCaseRange ast, Object typeExpression) {
        ast.C1.visit(this, typeExpression);
        ast.C2.visit(this, typeExpression);
        return null;
    }

@Override
    public Object visitCaseRangeCase(CaseRangeCase ast, Object chooseData) {
        ChooseData valuesData = (ChooseData) chooseData;
        TypeDenoter typeExpression = ((ChooseData) chooseData).getType();
        TypeDenoter typeCaseLit1  = (TypeDenoter) ast.CASELIT.visit(this, null);
        TypeDenoter typeCaseLit2  = (TypeDenoter) ast.CASELIT2.visit(this, null);
        if( typeExpression.equals(StdEnvironment.charType) ){
            if( ! typeCaseLit1.equals(typeExpression) ){
                 reporter.reportError ("Char expression expected here", "",
				ast.CASELIT.position);
            }
            if( ! typeCaseLit2.equals(typeExpression) ){
                 reporter.reportError ("Char expression expected here", "",
				ast.CASELIT2.position);
            }
        }
        else if( typeExpression.equals(StdEnvironment.integerType) ){
            if( ! typeCaseLit1.equals(typeExpression) ){
                 reporter.reportError ("Integer expression expected here", "",
				ast.CASELIT.position);
            }
            if( ! typeCaseLit2.equals(typeExpression) ){
                 reporter.reportError ("Integer expression expected here", "",
				ast.CASELIT2.position);
            }
        }
            if( ast.CASELIT instanceof CaseLiteralCHAR ){
                if(!valuesData.exists( ((CharacterLiteral) ( ( (CaseLiteralCHAR) ast.CASELIT).CHARLIT)).spelling)){
                    valuesData.addData( ((CharacterLiteral) ( ( (CaseLiteralCHAR) ast.CASELIT).CHARLIT)).spelling );
                }
                else{
                 reporter.reportError ("Repeated Character Literal in Choose Command", "",
				ast.CASELIT2.position);
                }
            }
            else if( ast.CASELIT instanceof CaseLiteralINT){
                if(!valuesData.exists(((IntegerLiteral) ( ( (CaseLiteralINT) ast.CASELIT).INTLIT)).spelling)){
                     valuesData.addData( ((IntegerLiteral) ( ( (CaseLiteralINT) ast.CASELIT).INTLIT)).spelling );
                }
                 else{
                 reporter.reportError ("Repeated Character Literal in Choose Command", "",
				ast.CASELIT2.position);
                }
            }

             if( ast.CASELIT2 instanceof CaseLiteralCHAR ){
                if(!valuesData.exists( ((CharacterLiteral) ( ( (CaseLiteralCHAR) ast.CASELIT2).CHARLIT)).spelling)){
                    valuesData.addData( ((CharacterLiteral) ( ( (CaseLiteralCHAR) ast.CASELIT2).CHARLIT)).spelling );
                }
                 else{
                 reporter.reportError ("Repeated Character Literal in Choose Command", "",
				ast.CASELIT2.position);
                }
            }
            else if( ast.CASELIT2 instanceof CaseLiteralINT){
                if(!valuesData.exists(((IntegerLiteral) ( ( (CaseLiteralINT) ast.CASELIT2).INTLIT)).spelling)){
                     valuesData.addData( ((IntegerLiteral) ( ( (CaseLiteralINT) ast.CASELIT2).INTLIT)).spelling );
                }
                 else{
                 reporter.reportError ("Repeated Character Literal in Choose Command", "",
				ast.CASELIT2.position);
                }
            }

        return null;
    }

@Override
    public Object visitCaseLiterals(CaseLiterals ast, Object typeExpression) {
        ast.CASERANGE.visit(this, typeExpression);
        return null;
    }



@Override
    public Object visitCaseWhen(CaseWhen ast, Object typeExpression) {
        ast.CASELIT.visit(this, typeExpression);
        ast.COM.visit(this, null);
        return null;
    }

@Override
    public Object visitSequentialCase(SequentialCase ast, Object typeExpression) {
        ast.C1.visit(this, typeExpression);
        ast.C2.visit(this, typeExpression);
        return null;
    }


@Override
    public Object visitElseCase(ElseCase ast, Object o) {
        ast.COM.visit(this, o);
        return null;
    }


 @Override
    public Object visitCases(Cases ast, Object typeExpression) {
        ast.CASE1.visit(this, typeExpression);
        ast.CASE2.visit(this, typeExpression);
        return null;
    }



    @Override
    public Object visitChooseCommand(ChooseCommand ast, Object o) {
        TypeDenoter expressionType = (TypeDenoter) ast.EXP.visit(this, o);
        if( ! expressionType.equals( StdEnvironment.charType) || ! expressionType.equals( StdEnvironment.integerType) ){
            reporter.reportError("Integer or Char expression expected here", "", ast.EXP.position);
        }
        ChooseData expressionData = new ChooseData(expressionType);
        ast.COM.visit(this, expressionData);
        return null;
    }


@Override
    public Object visitCompoundDeclarationSingleDeclaration(CompoundDeclarationSingleDeclaration ast, Object o) {
        ast.SD.visit(this, o);
        return null;
    }



    @Override
    public Object visitSequentialSingleDeclaration(SequentialSingleDeclaration ast, Object o) {
        ast.D1.visit(this, o);
        ast.D2.visit(this, o);
        return null;
    }


    @Override
    public Object visitCompoundDeclarationPrivate(CompoundDeclarationPrivate ast, Object o) {
        idTable.openScope();
        IdEntry startPoint = idTable.getLatest();
        ast.D1.visit(this, o);
        IdEntry inStart = idTable.getLatest();
        ast.D2.visit(this, o);
        inStart.previous = startPoint;
        idTable.closeScope();
        return null;
    }
    //endregion

  public Object visitAssignCommand(AssignCommand ast, Object o) {
    TypeDenoter vType = (TypeDenoter) ast.V.visit(this, null);
    TypeDenoter eType = (TypeDenoter) ast.E.visit(this, null);
    if (!ast.V.variable)
      reporter.reportError ("LHS of assignment is not a variable", "", ast.V.position);
    if (! eType.equals(vType))
      reporter.reportError ("assignment incompatibilty", "", ast.position);
    return null;
  }


  @Override
  public Object visitCallCommand(CallCommand ast, Object o) {
    String packageName = defaultPackage;
    if (ast.I instanceof CompoundIdentifier){
        CompoundIdentifier ci = (CompoundIdentifier)ast.I;
        if(ci.packageIdentifier != null){
            packageName = ci.packageIdentifier.spelling;  // If a compound identifier it's found, there's a possibility it's a package call.
        }
    }
    Declaration binding = (Declaration) ast.I.visit(this, packageName);
    if (binding == null)
      reportUndeclared(ast.I);
    else if (binding instanceof ProcDeclaration) {
      ast.APS.visit(this, ((ProcDeclaration) binding).FPS);
    } else if (binding instanceof ProcFormalParameter) {
      ast.APS.visit(this, ((ProcFormalParameter) binding).FPS);
    } else
      reporter.reportError("\"%\" is not a procedure identifier",
                           ast.I.spelling, ast.I.position);
    return null;
  }

  public Object visitEmptyCommand(EmptyCommand ast, Object o) {
    return null;
  }

  public Object visitIfCommand(IfCommand ast, Object o) {
    TypeDenoter eType = (TypeDenoter) ast.E.visit(this, null);
    if (! eType.equals(StdEnvironment.booleanType))
      reporter.reportError("Boolean expression expected here", "", ast.E.position);
    ast.C1.visit(this, null);
    ast.C2.visit(this, null);
    return null;
  }

  public Object visitLetCommand(LetCommand ast, Object o) {
    idTable.openScope();
    ast.D.visit(this, null);
    ast.C.visit(this, null);
    idTable.closeScope();
    return null;
  }

  public Object visitSequentialCommand(SequentialCommand ast, Object o) {
    ast.C1.visit(this, null);
    ast.C2.visit(this, null);
    return null;
  }

  public Object visitWhileCommand(WhileCommand ast, Object o) {
    TypeDenoter eType = (TypeDenoter) ast.E.visit(this, null);
    if (! eType.equals(StdEnvironment.booleanType))
      reporter.reportError("Boolean expression expected here", "", ast.E.position);
    ast.C.visit(this, null);
    return null;
  }

    // Iterative commands

    // Always returns null. Does not use the given object.

    /* Si lo de richie no sirve, descomente esta linea
   @Override
    public Object visitLoopCasesWhile(LoopCasesWhile ast, Object o) {
        TypeDenoter eType = (TypeDenoter) ast.EXP.visit(this, null);
        if (! eType.equals(StdEnvironment.booleanType))
          reporter.reportError("Boolean expression expected here", "", ast.EXP.position);
        ast.COM.visit(this, null);
        return null;
    }

    @Override
    public Object visitLoopCasesUntil(LoopCasesUntil ast, Object o) {
        TypeDenoter eType = (TypeDenoter) ast.EXP.visit(this, null);
        if (! eType.equals(StdEnvironment.booleanType))
          reporter.reportError("Boolean expression expected here", "", ast.EXP.position);
        ast.COM.visit(this, null);
        return null;
    }

    @Override
    public Object visitLoopCasesDo(LoopCasesDo ast, Object o) {
        ast.COM.visit(this, null);
        ast.DO.visit(this, o);
        return null;

       }

    @Override
    public Object visitDoLoopUntil(DoLoopUntil ast, Object o) {
        TypeDenoter eType = (TypeDenoter) ast.EXP.visit(this, null);
        if (! eType.equals(StdEnvironment.booleanType))
          reporter.reportError("Boolean expression expected here", "", ast.EXP.position);
        return null;
    }

    @Override
    public Object visitDoLoopWhile(DoLoopWhile ast, Object o) {
        TypeDenoter eType = (TypeDenoter) ast.EXP.visit(this, null);
        if (! eType.equals(StdEnvironment.booleanType))
          reporter.reportError("Boolean expression expected here", "", ast.EXP.position);
        return null;
    }
    */

    @Override
    public Object visitCallLoopCases(CallLoopCases ast, Object o) {
        ast.LOOP.visit(this, null);
        return null;
    }



  // Expressions

  // Returns the TypeDenoter denoting the type of the expression. Does
  // not use the given object.


  public Object visitArrayExpression(ArrayExpression ast, Object o) {
    TypeDenoter elemType = (TypeDenoter) ast.AA.visit(this, null);
    IntegerLiteral il = new IntegerLiteral(new Integer(ast.AA.elemCount).toString(),
                                           ast.position);
    ast.type = new ArrayTypeDenoter(il, elemType, ast.position);
    return ast.type;
  }

  public Object visitBinaryExpression(BinaryExpression ast, Object o) {

    TypeDenoter e1Type = (TypeDenoter) ast.E1.visit(this, null);
    TypeDenoter e2Type = (TypeDenoter) ast.E2.visit(this, null);
    Declaration binding = (Declaration) ast.O.visit(this, null);

    if (binding == null)
      reportUndeclared(ast.O);
    else {
      if (! (binding instanceof BinaryOperatorDeclaration))
        reporter.reportError ("\"%\" is not a binary operator",
                              ast.O.spelling, ast.O.position);
      BinaryOperatorDeclaration bbinding = (BinaryOperatorDeclaration) binding;
      if (bbinding.ARG1 == StdEnvironment.anyType) {
        // this operator must be "=" or "\="
        if (! e1Type.equals(e2Type))
          reporter.reportError ("incompatible argument types for \"%\"",
                                ast.O.spelling, ast.position);
      } else if (! e1Type.equals(bbinding.ARG1))
          reporter.reportError ("wrong argument type for \"%\"",
                                ast.O.spelling, ast.E1.position);
      else if (! e2Type.equals(bbinding.ARG2))
          reporter.reportError ("wrong argument type for \"%\"",
                                ast.O.spelling, ast.E2.position);
      ast.type = bbinding.RES;
    }
    return ast.type;
  }

  public Object visitCallExpression(CallExpression ast, Object o) {
    Declaration binding = (Declaration) ast.I.visit(this, null);
    if (binding == null) {
      reportUndeclared(ast.I);
      ast.type = StdEnvironment.errorType;
    } else if (binding instanceof FuncDeclaration) {
      ast.APS.visit(this, ((FuncDeclaration) binding).FPS);
      ast.type = ((FuncDeclaration) binding).T;
    } else if (binding instanceof FuncFormalParameter) {
      ast.APS.visit(this, ((FuncFormalParameter) binding).FPS);
      ast.type = ((FuncFormalParameter) binding).T;
    } else
      reporter.reportError("\"%\" is not a function identifier",
                           ast.I.spelling, ast.I.position);
    return ast.type;
  }

  public Object visitCharacterExpression(CharacterExpression ast, Object o) {
    ast.type = StdEnvironment.charType;
    return ast.type;
  }

  public Object visitEmptyExpression(EmptyExpression ast, Object o) {
    ast.type = null;
    return ast.type;
  }

  public Object visitIfExpression(IfExpression ast, Object o) {
    TypeDenoter e1Type = (TypeDenoter) ast.E1.visit(this, null);
    if (! e1Type.equals(StdEnvironment.booleanType))
      reporter.reportError ("Boolean expression expected here", "",
                            ast.E1.position);
    TypeDenoter e2Type = (TypeDenoter) ast.E2.visit(this, null);
    TypeDenoter e3Type = (TypeDenoter) ast.E3.visit(this, null);
    if (! e2Type.equals(e3Type))
      reporter.reportError ("incompatible limbs in if-expression", "", ast.position);
    ast.type = e2Type;
    return ast.type;
  }

  public Object visitIntegerExpression(IntegerExpression ast, Object o) {
    ast.type = StdEnvironment.integerType;
    return ast.type;
  }

  public Object visitLetExpression(LetExpression ast, Object o) {
    idTable.openScope();
    ast.D.visit(this, null);
    ast.type = (TypeDenoter) ast.E.visit(this, null);
    idTable.closeScope();
    return ast.type;
  }

  public Object visitRecordExpression(RecordExpression ast, Object o) {
    FieldTypeDenoter rType = (FieldTypeDenoter) ast.RA.visit(this, null);
    ast.type = new RecordTypeDenoter(rType, ast.position);
    return ast.type;
  }

  public Object visitUnaryExpression(UnaryExpression ast, Object o) {

    TypeDenoter eType = (TypeDenoter) ast.E.visit(this, null);
    Declaration binding = (Declaration) ast.O.visit(this, null);
    if (binding == null) {
      reportUndeclared(ast.O);
      ast.type = StdEnvironment.errorType;
    } else if (! (binding instanceof UnaryOperatorDeclaration))
        reporter.reportError ("\"%\" is not a unary operator",
                              ast.O.spelling, ast.O.position);
    else {
      UnaryOperatorDeclaration ubinding = (UnaryOperatorDeclaration) binding;
      if (! eType.equals(ubinding.ARG))
        reporter.reportError ("wrong argument type for \"%\"",
                              ast.O.spelling, ast.O.position);
      ast.type = ubinding.RES;
    }
    return ast.type;
  }

  public Object visitVnameExpression(VnameExpression ast, Object o) {
    ast.type = (TypeDenoter) ast.V.visit(this, null);
    return ast.type;
  }

  @Override
    public Object visitSecExpression(SecExpression ast, Object o) {
        ast.type = (TypeDenoter) ast.secExpression.visit(this, null);
        return ast.type;
    }

    @Override
    public Object visitIntLiteralExpression(IntLiteralExpression ast, Object o) {
        ast.type = StdEnvironment.integerType;
        return ast.type;
    }

    @Override
    public Object visitOperatorExpression(OperatorExpression ast, Object o) { //Used to visit multiple expressions separated by an operator
        ast.O.visit(this, null);
        ast.type = (TypeDenoter) ast.E.visit(this, null);
        return ast.type;

    }

    @Override
    public Object visitLParenExpression(LParenExpression ast, Object o) {
        return ast.E.visit(this, null);
    }

    @Override
    public Object visitLCurlyExpression(LCurlyExpression ast, Object o) {
        FieldTypeDenoter rType = (FieldTypeDenoter) ast.RA.visit(this, null);
        ast.type = new RecordTypeDenoter(rType, ast.position);
        return ast.type;
    }

    @Override
    public Object visitLBracketExpression(LBracketExpression ast, Object o) {
        TypeDenoter elemType = (TypeDenoter) ast.AA.visit(this, null);
        IntegerLiteral il = new IntegerLiteral(new Integer(ast.AA.elemCount).toString(),
                                               ast.position);
        ast.type = new ArrayTypeDenoter(il, elemType, ast.position);
        return ast.type;
    }

    @Override
    public Object visitLongIdentifier(LongIdentifier ast, Object o) {
        String packageName = defaultPackage;
        if(ast.packageIdentifier != null){
            packageName = ast.packageIdentifier.spelling;
        }
        Declaration binding = hashIdTables.get(packageName).retrieve(ast.identifier.spelling);
        if (binding != null)
          ast.identifier.decl = binding;
        return binding;
    }

    @Override
    public Object visitLIdentifierExpression(LIdentifierExpression ast, Object o) { //previous call expression
        Declaration binding = (Declaration) ast.LI.visit(this, null);
        if (binding == null) {
          reportUndeclared(ast.LI);
          ast.type = StdEnvironment.errorType;
        } else if (binding instanceof FuncDeclaration) {
          ast.APS.visit(this, ((FuncDeclaration) binding).FPS);
          ast.type = ((FuncDeclaration) binding).T;
        } else if (binding instanceof FuncFormalParameter) {
          ast.APS.visit(this, ((FuncFormalParameter) binding).FPS);
          ast.type = ((FuncFormalParameter) binding).T;
        } else
          reporter.reportError("\"%\" is not a function identifier",
                               ast.LI.spelling, ast.LI.position);
        return ast.type;
    }

    @Override
    public Object visitAssignExpression(AssignExpression ast, Object o) {
        return ast.V.visit(this, null);
    }


  // Declarations

  // Always returns null. Does not use the given object.
  public Object visitBinaryOperatorDeclaration(BinaryOperatorDeclaration ast, Object o) {
    return null;
  }

  public Object visitFuncDeclaration(FuncDeclaration ast, Object o) {
    ast.T = (TypeDenoter) ast.T.visit(this, null);
    idTable.enter (ast.I.spelling, ast); // permits recursion
    if (ast.duplicated)
      reporter.reportError ("identifier \"%\" already declared",
                            ast.I.spelling, ast.position);
    idTable.openScope();
    ast.FPS.visit(this, null);
    TypeDenoter eType = (TypeDenoter) ast.E.visit(this, null);
    idTable.closeScope();
    if (! ast.T.equals(eType))
      reporter.reportError ("body of function \"%\" has wrong type",
                            ast.I.spelling, ast.E.position);
    return null;
  }

  public Object visitProcDeclaration(ProcDeclaration ast, Object o) {
    idTable.enter (ast.I.spelling, ast); // permits recursion
    if (ast.duplicated)
      reporter.reportError ("identifier \"%\" already declared",
                            ast.I.spelling, ast.position);
    idTable.openScope();
    ast.FPS.visit(this, null);
    ast.C.visit(this, null);
    idTable.closeScope();
    return null;
  }

  public Object visitSequentialDeclaration(SequentialDeclaration ast, Object o) {
    ast.D1.visit(this, null);
    ast.D2.visit(this, null);
    return null;
  }

  public Object visitTypeDeclaration(TypeDeclaration ast, Object o) {
    ast.T = (TypeDenoter) ast.T.visit(this, null);
    idTable.enter (ast.I.spelling, ast);
    if (ast.duplicated)
      reporter.reportError ("identifier \"%\" already declared",
                            ast.I.spelling, ast.position);
    return null;
  }

  public Object visitUnaryOperatorDeclaration(UnaryOperatorDeclaration ast, Object o) {
    return null;
  }

  /*
  TODO: Revisar visitVarDeclarationNuevo
  public Object visitVarDeclaration(VarDeclaration ast, Object o) {
     //TODO Visit type denoter or expression.
      idTable.enter (ast.I.spelling, ast);
    if (ast.duplicated)
      reporter.reportError ("identifier \"%\" already declared",
                            ast.I.spelling, ast.position);

    return null;
  }
    */
  // Array Aggregates

  // Returns the TypeDenoter for the Array Aggregate. Does not use the
  // given object.

  public Object visitMultipleArrayAggregate(MultipleArrayAggregate ast, Object o) {
    TypeDenoter eType = (TypeDenoter) ast.E.visit(this, null);
    TypeDenoter elemType = (TypeDenoter) ast.AA.visit(this, null);
    ast.elemCount = ast.AA.elemCount + 1;
    if (! eType.equals(elemType))
      reporter.reportError ("incompatible array-aggregate element", "", ast.E.position);
    return elemType;
  }

  public Object visitSingleArrayAggregate(SingleArrayAggregate ast, Object o) {
    TypeDenoter elemType = (TypeDenoter) ast.E.visit(this, null);
    ast.elemCount = 1;
    return elemType;
  }

  // Record Aggregates

  // Returns the TypeDenoter for the Record Aggregate. Does not use the
  // given object.

  public Object visitMultipleRecordAggregate(MultipleRecordAggregate ast, Object o) {
    TypeDenoter eType = (TypeDenoter) ast.E.visit(this, null);
    FieldTypeDenoter rType = (FieldTypeDenoter) ast.RA.visit(this, null);
    TypeDenoter fType = checkFieldIdentifier(rType, ast.I);
    if (fType != StdEnvironment.errorType)
      reporter.reportError ("duplicate field \"%\" in record",
                            ast.I.spelling, ast.I.position);
    ast.type = new MultipleFieldTypeDenoter(ast.I, eType, rType, ast.position);
    return ast.type;
  }

  public Object visitSingleRecordAggregate(SingleRecordAggregate ast, Object o) {
    TypeDenoter eType = (TypeDenoter) ast.E.visit(this, null);
    ast.type = new SingleFieldTypeDenoter(ast.I, eType, ast.position);
    return ast.type;
  }

  // Formal Parameters

  // Always returns null. Does not use the given object.

  public Object visitConstFormalParameter(ConstFormalParameter ast, Object o) {
    ast.T = (TypeDenoter) ast.T.visit(this, null);
    idTable.enter(ast.I.spelling, ast);
    if (ast.duplicated)
      reporter.reportError ("duplicated formal parameter \"%\"",
                            ast.I.spelling, ast.position);
    return null;
  }

  public Object visitFuncFormalParameter(FuncFormalParameter ast, Object o) {
    idTable.openScope();
    ast.FPS.visit(this, null);
    idTable.closeScope();
    ast.T = (TypeDenoter) ast.T.visit(this, null);
    idTable.enter (ast.I.spelling, ast);
    if (ast.duplicated)
      reporter.reportError ("duplicated formal parameter \"%\"",
                            ast.I.spelling, ast.position);
    return null;
  }

  public Object visitProcFormalParameter(ProcFormalParameter ast, Object o) {
    idTable.openScope();
    ast.FPS.visit(this, null);
    idTable.closeScope();
    idTable.enter (ast.I.spelling, ast);
    if (ast.duplicated)
      reporter.reportError ("duplicated formal parameter \"%\"",
                            ast.I.spelling, ast.position);
    return null;
  }

  public Object visitVarFormalParameter(VarFormalParameter ast, Object o) {
    ast.T = (TypeDenoter) ast.T.visit(this, null);
    idTable.enter (ast.I.spelling, ast);
    if (ast.duplicated)
      reporter.reportError ("duplicated formal parameter \"%\"",
                            ast.I.spelling, ast.position);
    return null;
  }

  public Object visitEmptyFormalParameterSequence(EmptyFormalParameterSequence ast, Object o) {
    return null;
  }

  public Object visitMultipleFormalParameterSequence(MultipleFormalParameterSequence ast, Object o) {
    ast.FP.visit(this, null);
    ast.FPS.visit(this, null);
    return null;
  }

  public Object visitSingleFormalParameterSequence(SingleFormalParameterSequence ast, Object o) {
    ast.FP.visit(this, null);
    return null;
  }

  // Actual Parameters

  // Always returns null. Uses the given FormalParameter.

  public Object visitConstActualParameter(ConstActualParameter ast, Object o) {
    FormalParameter fp = (FormalParameter) o;
    TypeDenoter eType = (TypeDenoter) ast.E.visit(this, null);

    if (! (fp instanceof ConstFormalParameter))
      reporter.reportError ("const actual parameter not expected here", "",
                            ast.position);
    else if (! eType.equals(((ConstFormalParameter) fp).T))
      reporter.reportError ("wrong type for const actual parameter", "",
                            ast.E.position);
    return null;
  }

  public Object visitFuncActualParameter(FuncActualParameter ast, Object o) {
    FormalParameter fp = (FormalParameter) o;

    Declaration binding = (Declaration) ast.I.visit(this, null);
    if (binding == null)
      reportUndeclared (ast.I);
    else if (! (binding instanceof FuncDeclaration ||
                binding instanceof FuncFormalParameter))
      reporter.reportError ("\"%\" is not a function identifier",
                            ast.I.spelling, ast.I.position);
    else if (! (fp instanceof FuncFormalParameter))
      reporter.reportError ("func actual parameter not expected here", "",
                            ast.position);
    else {
      FormalParameterSequence FPS = null;
      TypeDenoter T = null;
      if (binding instanceof FuncDeclaration) {
        FPS = ((FuncDeclaration) binding).FPS;
        T = ((FuncDeclaration) binding).T;
      } else {
        FPS = ((FuncFormalParameter) binding).FPS;
        T = ((FuncFormalParameter) binding).T;
      }
      if (! FPS.equals(((FuncFormalParameter) fp).FPS))
        reporter.reportError ("wrong signature for function \"%\"",
                              ast.I.spelling, ast.I.position);
      else if (! T.equals(((FuncFormalParameter) fp).T))
        reporter.reportError ("wrong type for function \"%\"",
                              ast.I.spelling, ast.I.position);
    }
    return null;
  }

  public Object visitProcActualParameter(ProcActualParameter ast, Object o) {
    FormalParameter fp = (FormalParameter) o;

    Declaration binding = (Declaration) ast.I.visit(this, null);
    if (binding == null)
      reportUndeclared (ast.I);
    else if (! (binding instanceof ProcDeclaration ||
                binding instanceof ProcFormalParameter))
      reporter.reportError ("\"%\" is not a procedure identifier",
                            ast.I.spelling, ast.I.position);
    else if (! (fp instanceof ProcFormalParameter))
      reporter.reportError ("proc actual parameter not expected here", "",
                            ast.position);
    else {
      FormalParameterSequence FPS = null;
      if (binding instanceof ProcDeclaration)
        FPS = ((ProcDeclaration) binding).FPS;
      else
        FPS = ((ProcFormalParameter) binding).FPS;
      if (! FPS.equals(((ProcFormalParameter) fp).FPS))
        reporter.reportError ("wrong signature for procedure \"%\"",
                              ast.I.spelling, ast.I.position);
    }
    return null;
  }

  public Object visitVarActualParameter(VarActualParameter ast, Object o) {
    FormalParameter fp = (FormalParameter) o;

    TypeDenoter vType = (TypeDenoter) ast.V.visit(this, null);
    if (! ast.V.variable)
      reporter.reportError ("actual parameter is not a variable", "",
                            ast.V.position);
    else if (! (fp instanceof VarFormalParameter))
      reporter.reportError ("var actual parameter not expected here", "",
                            ast.V.position);
    else if (! vType.equals(((VarFormalParameter) fp).T))
      reporter.reportError ("wrong type for var actual parameter", "",
                            ast.V.position);
    return null;
  }

  public Object visitEmptyActualParameterSequence(EmptyActualParameterSequence ast, Object o) {
    FormalParameterSequence fps = (FormalParameterSequence) o;
    if (! (fps instanceof EmptyFormalParameterSequence))
      reporter.reportError ("too few actual parameters", "", ast.position);
    return null;
  }

  public Object visitMultipleActualParameterSequence(MultipleActualParameterSequence ast, Object o) {
    FormalParameterSequence fps = (FormalParameterSequence) o;
    if (! (fps instanceof MultipleFormalParameterSequence))
      reporter.reportError ("too many actual parameters", "", ast.position);
    else {
      ast.AP.visit(this, ((MultipleFormalParameterSequence) fps).FP);
      ast.APS.visit(this, ((MultipleFormalParameterSequence) fps).FPS);
    }
    return null;
  }

  public Object visitSingleActualParameterSequence(SingleActualParameterSequence ast, Object o) {
    FormalParameterSequence fps = (FormalParameterSequence) o;
    if (! (fps instanceof SingleFormalParameterSequence))
      reporter.reportError ("incorrect number of actual parameters", "", ast.position);
    else {
      ast.AP.visit(this, ((SingleFormalParameterSequence) fps).FP);
    }
    return null;
  }

  // Type Denoters

  // Returns the expanded version of the TypeDenoter. Does not
  // use the given object.

  public Object visitAnyTypeDenoter(AnyTypeDenoter ast, Object o) {
    return StdEnvironment.anyType;
  }

  public Object visitArrayTypeDenoter(ArrayTypeDenoter ast, Object o) {
    ast.T = (TypeDenoter) ast.T.visit(this, null);
    if ((Integer.valueOf(ast.IL.spelling).intValue()) == 0)
      reporter.reportError ("arrays must not be empty", "", ast.IL.position);
    return ast;
  }

  public Object visitBoolTypeDenoter(BoolTypeDenoter ast, Object o) {
    return StdEnvironment.booleanType;
  }

  public Object visitCharTypeDenoter(CharTypeDenoter ast, Object o) {
    return StdEnvironment.charType;
  }

  public Object visitErrorTypeDenoter(ErrorTypeDenoter ast, Object o) {
    return StdEnvironment.errorType;
  }

  public Object visitSimpleTypeDenoter(SimpleTypeDenoter ast, Object o) {
    Declaration binding = (Declaration) ast.I.visit(this, null);
    if (binding == null) {
      reportUndeclared (ast.I);
      return StdEnvironment.errorType;
    } else if (! (binding instanceof TypeDeclaration)) {
      reporter.reportError ("\"%\" is not a type identifier",
                            ast.I.spelling, ast.I.position);
      return StdEnvironment.errorType;
    }
    return ((TypeDeclaration) binding).T;
  }

  public Object visitIntTypeDenoter(IntTypeDenoter ast, Object o) {
    return StdEnvironment.integerType;
  }

  public Object visitRecordTypeDenoter(RecordTypeDenoter ast, Object o) {
    ast.FT = (FieldTypeDenoter) ast.FT.visit(this, null);
    return ast;
  }

  public Object visitMultipleFieldTypeDenoter(MultipleFieldTypeDenoter ast, Object o) {
    ast.T = (TypeDenoter) ast.T.visit(this, null);
    ast.FT.visit(this, null);
    return ast;
  }

  public Object visitSingleFieldTypeDenoter(SingleFieldTypeDenoter ast, Object o) {
    ast.T = (TypeDenoter) ast.T.visit(this, null);
    return ast;
  }

  // Literals, Identifiers and Operators
  public Object visitCharacterLiteral(CharacterLiteral CL, Object o) {
    return StdEnvironment.charType;
  }

  //Change: a package is now received
  public Object visitIdentifier(Identifier I, Object o) {
    String packageName = defaultPackage;
    if(o != null){
        if(o instanceof String){
            packageName = (String) o;
        }
    }

    Declaration binding = hashIdTables.get(packageName).retrieve(I.spelling);
    if (binding != null)
      I.decl = binding;
    return binding;
  }

  public Object visitIntegerLiteral(IntegerLiteral IL, Object o) {
    return StdEnvironment.integerType;
  }

  public Object visitOperator(Operator O, Object o) {
    Declaration binding = idTable.retrieve(O.spelling);
    if (binding != null)
      O.decl = binding;
    return binding;
  }

  // Value-or-variable names

  // Determines the address of a named object (constant or variable).
  // This consists of a base object, to which 0 or more field-selection
  // or array-indexing operations may be applied (if it is a record or
  // array).  As much as possible of the address computation is done at
  // compile-time. Code is generated only when necessary to evaluate
  // index expressions at run-time.
  // currentLevel is the routine level where the v-name occurs.
  // frameSize is the anticipated size of the local stack frame when
  // the object is addressed at run-time.
  // It returns the description of the base object.
  // offset is set to the total of any field offsets (plus any offsets
  // due to index expressions that happen to be literals).
  // indexed is set to true iff there are any index expressions (other
  // than literals). In that case code is generated to compute the
  // offset due to these indexing operations at run-time.

  // Returns the TypeDenoter of the Vname. Does not use the
  // given object.

  public Object visitDotVname(DotVname ast, Object o) {
    ast.type = null;
    TypeDenoter vType = (TypeDenoter) ast.V.visit(this, null);
    ast.variable = ast.V.variable;
    if (! (vType instanceof RecordTypeDenoter))
      reporter.reportError ("record expected here", "", ast.V.position);
    else {
      ast.type = checkFieldIdentifier(((RecordTypeDenoter) vType).FT, ast.I);
      if (ast.type == StdEnvironment.errorType)
        reporter.reportError ("no field \"%\" in this record type",
                              ast.I.spelling, ast.I.position);
    }
    return ast.type;
  }

  /*
  TODO revisar visitSimpleVname nuevo
  public Object visitSimpleVname(SimpleVname ast, Object o) {
    ast.variable = false;
    ast.type = StdEnvironment.errorType;
    Declaration binding = (Declaration) ast.I.visit(this, null);
    if (binding == null)
      reportUndeclared(ast.I);
    else
      if (binding instanceof ConstDeclaration) {
        ast.type = ((ConstDeclaration) binding).E.type;
        ast.variable = false;
      } else if (binding instanceof VarDeclaration) {
        //TODO binding for TypeDenoter or Expression
        //ast.type = ((VarDeclaration) binding).T;
        ast.variable = true;
      } else if (binding instanceof ConstFormalParameter) {
        ast.type = ((ConstFormalParameter) binding).T;
        ast.variable = false;
      } else if (binding instanceof VarFormalParameter) {
        ast.type = ((VarFormalParameter) binding).T;
        ast.variable = true;
      } else
        reporter.reportError ("\"%\" is not a const or var identifier",
                              ast.I.spelling, ast.I.position);
    return ast.type;
  }
    */
  public Object visitSubscriptVname(SubscriptVname ast, Object o) {
    TypeDenoter vType = (TypeDenoter) ast.V.visit(this, null);
    ast.variable = ast.V.variable;
    TypeDenoter eType = (TypeDenoter) ast.E.visit(this, null);
    if (vType != StdEnvironment.errorType) {
      if (! (vType instanceof ArrayTypeDenoter))
        reporter.reportError ("array expected here", "", ast.V.position);
      else {
        if (! eType.equals(StdEnvironment.integerType))
          reporter.reportError ("Integer expression expected here", "",
				ast.E.position);
        ast.type = ((ArrayTypeDenoter) vType).T;
      }
    }
    return ast.type;
  }

  // Programs

  public Object visitProgram(Program ast, Object o) {
    ast.C.visit(this, null);
    return null;
  }

  // Checks whether the source program, represented by its AST, satisfies the
  // language's scope rules and type rules.
  // Also decorates the AST as follows:
  //  (a) Each applied occurrence of an identifier or operator is linked to
  //      the corresponding declaration of that identifier or operator.
  //  (b) Each expression and value-or-variable-name is decorated by its type.
  //  (c) Each type identifier is replaced by the type it denotes.
  // Types are represented by small ASTs.

  public void check(Program ast) {
    ast.visit(this, null);
  }

  /////////////////////////////////////////////////////////////////////////////

 // Checker Constructor
    public Checker (ErrorReporter reporter) {
      this.reporter = reporter;
      this.idTable = new IdentificationTable ();
      this.hashIdTables = new HashMap<String, IdentificationTable>();
      this.hashIdTables.put(defaultPackage, idTable);
      establishStdEnvironment();
    }

    private IdentificationTable idTable;
    private HashMap<String,IdentificationTable> hashIdTables;
    private static SourcePosition dummyPos = new SourcePosition();
    private ErrorReporter reporter;
    private final String defaultPackage = "_global_";

  // Reports that the identifier or operator used at a leaf of the AST
  // has not been declared.

  private void reportUndeclared (Terminal leaf) {
    reporter.reportError("\"%\" is not declared", leaf.spelling, leaf.position);
  }


  private static TypeDenoter checkFieldIdentifier(FieldTypeDenoter ast, Identifier I) {
    if (ast instanceof MultipleFieldTypeDenoter) {
      MultipleFieldTypeDenoter ft = (MultipleFieldTypeDenoter) ast;
      if (ft.I.spelling.compareTo(I.spelling) == 0) {
        I.decl = ast;
        return ft.T;
      } else {
        return checkFieldIdentifier (ft.FT, I);
      }
    } else if (ast instanceof SingleFieldTypeDenoter) {
      SingleFieldTypeDenoter ft = (SingleFieldTypeDenoter) ast;
      if (ft.I.spelling.compareTo(I.spelling) == 0) {
        I.decl = ast;
        return ft.T;
      }
    }
    return StdEnvironment.errorType;
  }


  // Creates a small AST to represent the "declaration" of a standard
  // type, and enters it in the identification table.

  private TypeDeclaration declareStdType (String id, TypeDenoter typedenoter) {

    TypeDeclaration binding;

    binding = new TypeDeclaration(new Identifier(id, dummyPos), typedenoter, dummyPos);
    idTable.enter(id, binding);
    return binding;
  }

  // Creates a small AST to represent the "declaration" of a standard
  // type, and enters it in the identification table.

  private ConstDeclaration declareStdConst (String id, TypeDenoter constType) {

    IntegerExpression constExpr;
    ConstDeclaration binding;

    // constExpr used only as a placeholder for constType
    constExpr = new IntegerExpression(null, dummyPos);
    constExpr.type = constType;
    binding = new ConstDeclaration(new Identifier(id, dummyPos), constExpr, dummyPos);
    idTable.enter(id, binding);
    return binding;
  }

  // Creates a small AST to represent the "declaration" of a standard
  // type, and enters it in the identification table.

  private ProcDeclaration declareStdProc (String id, FormalParameterSequence fps) {

    ProcDeclaration binding;

    binding = new ProcDeclaration(new Identifier(id, dummyPos), fps,
                                  new EmptyCommand(dummyPos), dummyPos);
    idTable.enter(id, binding);
    return binding;
  }

  // Creates a small AST to represent the "declaration" of a standard
  // type, and enters it in the identification table.

  private FuncDeclaration declareStdFunc (String id, FormalParameterSequence fps,
                                          TypeDenoter resultType) {

    FuncDeclaration binding;

    binding = new FuncDeclaration(new Identifier(id, dummyPos), fps, resultType,
                                  new EmptyExpression(dummyPos), dummyPos);
    idTable.enter(id, binding);
    return binding;
  }

  // Creates a small AST to represent the "declaration" of a
  // unary operator, and enters it in the identification table.
  // This "declaration" summarises the operator's type info.

  private UnaryOperatorDeclaration declareStdUnaryOp
    (String op, TypeDenoter argType, TypeDenoter resultType) {

    UnaryOperatorDeclaration binding;

    binding = new UnaryOperatorDeclaration (new Operator(op, dummyPos),
                                            argType, resultType, dummyPos);
    idTable.enter(op, binding);
    return binding;
  }

  // Creates a small AST to represent the "declaration" of a
  // binary operator, and enters it in the identification table.
  // This "declaration" summarises the operator's type info.

  private BinaryOperatorDeclaration declareStdBinaryOp
    (String op, TypeDenoter arg1Type, TypeDenoter arg2type, TypeDenoter resultType) {

    BinaryOperatorDeclaration binding;

    binding = new BinaryOperatorDeclaration (new Operator(op, dummyPos),
                                             arg1Type, arg2type, resultType, dummyPos);
    idTable.enter(op, binding);
    return binding;
  }

  // Creates small ASTs to represent the standard types.
  // Creates small ASTs to represent "declarations" of standard types,
  // constants, procedures, functions, and operators.
  // Enters these "declarations" in the identification table.

  private final static Identifier dummyI = new Identifier("", dummyPos);

  private void establishStdEnvironment () {

    // idTable.startIdentification();
    StdEnvironment.booleanType = new BoolTypeDenoter(dummyPos);
    StdEnvironment.integerType = new IntTypeDenoter(dummyPos);
    StdEnvironment.charType = new CharTypeDenoter(dummyPos);
    StdEnvironment.anyType = new AnyTypeDenoter(dummyPos);
    StdEnvironment.errorType = new ErrorTypeDenoter(dummyPos);

    StdEnvironment.booleanDecl = declareStdType("Boolean", StdEnvironment.booleanType);
    StdEnvironment.falseDecl = declareStdConst("false", StdEnvironment.booleanType);
    StdEnvironment.trueDecl = declareStdConst("true", StdEnvironment.booleanType);
    StdEnvironment.notDecl = declareStdUnaryOp("\\", StdEnvironment.booleanType, StdEnvironment.booleanType);
    StdEnvironment.andDecl = declareStdBinaryOp("/\\", StdEnvironment.booleanType, StdEnvironment.booleanType, StdEnvironment.booleanType);
    StdEnvironment.orDecl = declareStdBinaryOp("\\/", StdEnvironment.booleanType, StdEnvironment.booleanType, StdEnvironment.booleanType);

    StdEnvironment.integerDecl = declareStdType("Integer", StdEnvironment.integerType);
    StdEnvironment.maxintDecl = declareStdConst("maxint", StdEnvironment.integerType);
    StdEnvironment.addDecl = declareStdBinaryOp("+", StdEnvironment.integerType, StdEnvironment.integerType, StdEnvironment.integerType);
    StdEnvironment.subtractDecl = declareStdBinaryOp("-", StdEnvironment.integerType, StdEnvironment.integerType, StdEnvironment.integerType);
    StdEnvironment.multiplyDecl = declareStdBinaryOp("*", StdEnvironment.integerType, StdEnvironment.integerType, StdEnvironment.integerType);
    StdEnvironment.divideDecl = declareStdBinaryOp("/", StdEnvironment.integerType, StdEnvironment.integerType, StdEnvironment.integerType);
    StdEnvironment.moduloDecl = declareStdBinaryOp("//", StdEnvironment.integerType, StdEnvironment.integerType, StdEnvironment.integerType);
    StdEnvironment.lessDecl = declareStdBinaryOp("<", StdEnvironment.integerType, StdEnvironment.integerType, StdEnvironment.booleanType);
    StdEnvironment.notgreaterDecl = declareStdBinaryOp("<=", StdEnvironment.integerType, StdEnvironment.integerType, StdEnvironment.booleanType);
    StdEnvironment.greaterDecl = declareStdBinaryOp(">", StdEnvironment.integerType, StdEnvironment.integerType, StdEnvironment.booleanType);
    StdEnvironment.notlessDecl = declareStdBinaryOp(">=", StdEnvironment.integerType, StdEnvironment.integerType, StdEnvironment.booleanType);

    StdEnvironment.charDecl = declareStdType("Char", StdEnvironment.charType);
    StdEnvironment.chrDecl = declareStdFunc("chr", new SingleFormalParameterSequence(
                                      new ConstFormalParameter(dummyI, StdEnvironment.integerType, dummyPos), dummyPos), StdEnvironment.charType);
    StdEnvironment.ordDecl = declareStdFunc("ord", new SingleFormalParameterSequence(
                                      new ConstFormalParameter(dummyI, StdEnvironment.charType, dummyPos), dummyPos), StdEnvironment.integerType);
    StdEnvironment.eofDecl = declareStdFunc("eof", new EmptyFormalParameterSequence(dummyPos), StdEnvironment.booleanType);
    StdEnvironment.eolDecl = declareStdFunc("eol", new EmptyFormalParameterSequence(dummyPos), StdEnvironment.booleanType);
    StdEnvironment.getDecl = declareStdProc("get", new SingleFormalParameterSequence(
                                      new VarFormalParameter(dummyI, StdEnvironment.charType, dummyPos), dummyPos));
    StdEnvironment.putDecl = declareStdProc("put", new SingleFormalParameterSequence(
                                      new ConstFormalParameter(dummyI, StdEnvironment.charType, dummyPos), dummyPos));
    StdEnvironment.getintDecl = declareStdProc("getint", new SingleFormalParameterSequence(
                                            new VarFormalParameter(dummyI, StdEnvironment.integerType, dummyPos), dummyPos));
    StdEnvironment.putintDecl = declareStdProc("putint", new SingleFormalParameterSequence(
                                            new ConstFormalParameter(dummyI, StdEnvironment.integerType, dummyPos), dummyPos));
    StdEnvironment.geteolDecl = declareStdProc("geteol", new EmptyFormalParameterSequence(dummyPos));
    StdEnvironment.puteolDecl = declareStdProc("puteol", new EmptyFormalParameterSequence(dummyPos));
    StdEnvironment.equalDecl = declareStdBinaryOp("=", StdEnvironment.anyType, StdEnvironment.anyType, StdEnvironment.booleanType);
    StdEnvironment.unequalDecl = declareStdBinaryOp("\\=", StdEnvironment.anyType, StdEnvironment.anyType, StdEnvironment.booleanType);

  }

    @Override
    public Object visitSingleDeclarationCommand(SingleDeclarationCommand ast, Object o) {
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
}
