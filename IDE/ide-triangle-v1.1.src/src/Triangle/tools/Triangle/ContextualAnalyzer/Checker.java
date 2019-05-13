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
import Utilities.RecursiveProcFuncData;
import Utilities.FormalParameterData;
import Utilities.ActualParameterData;
import Utilities.LoopCasesFORData;
import java.util.HashMap;

public final class Checker implements Visitor {
    //nuevos jose
    /*
   The method has two parameters which are ast, where, either one or two caselit are. 
    First, it determines the type denoter of both of them and if they match with the 
    typedenoter of the first expression determined in the main part of the choose, 
    they are added to the hash  in choosedata, but if they don't match, an error is reported, 
    if there are two values, that means is a range, therefore, addCharactersValues or  
    addIntegerValues are called, so they analyze the range and add the values of the range.
    */
    @Override
    public Object visitCaseRangeCase(CaseRangeCase ast, Object chooseData) {
        ChooseData valuesData = (ChooseData) chooseData;
        TypeDenoter typeExpression = ((ChooseData) chooseData).getType();
        TypeDenoter typeCaseLit1  = (TypeDenoter) ast.CASELIT.visit(this, null);
        TypeDenoter typeCaseLit2 = null;
        if( ast.CASELIT2 != null )
              typeCaseLit2 = (TypeDenoter) ast.CASELIT2.visit(this, null);
        if( typeExpression.equals(StdEnvironment.charType) ){
            if( ! typeCaseLit1.equals(typeExpression) ){
                 reporter.reportError ("Char expression expected here", "",
				ast.CASELIT.position);
            }
            if( typeCaseLit2 != null && ! typeCaseLit2.equals(typeExpression) ){
                 reporter.reportError ("Char expression expected here", "",
				ast.CASELIT2.position);
            }
        }
        else if( typeExpression.equals(StdEnvironment.integerType) ){
            if( ! typeCaseLit1.equals(typeExpression) ){
                 reporter.reportError ("Integer expression expected here", "",
				ast.CASELIT.position);
            }
            if(typeCaseLit2 != null && ! typeCaseLit2.equals(typeExpression) ){
                 reporter.reportError ("Integer expression expected here", "",
				ast.CASELIT2.position);
            }
        }
            if( ast.CASELIT instanceof CaseLiteralCHAR && ast.CASELIT2 == null ){
                if(!valuesData.exists( ((CharacterLiteral) ( ( (CaseLiteralCHAR) ast.CASELIT).CHARLIT)).spelling)){
                    valuesData.addData( ((CharacterLiteral) ( ( (CaseLiteralCHAR) ast.CASELIT).CHARLIT)).spelling );
                }
                else{
                 reporter.reportError ("Repeated Character Literal in Choose Command", "",
				ast.CASELIT.position);
                }
            }
            else if( ast.CASELIT instanceof CaseLiteralINT && ast.CASELIT2 == null){
                if(!valuesData.exists(((IntegerLiteral) ( ( (CaseLiteralINT) ast.CASELIT).INTLIT)).spelling)){
                     valuesData.addData( ((IntegerLiteral) ( ( (CaseLiteralINT) ast.CASELIT).INTLIT)).spelling );
                }
                 else{
                 reporter.reportError ("Repeated Integer Literal in Choose Command", "",
				ast.CASELIT.position);
                }
            }
             
            if(ast.CASELIT2 !=  null && typeExpression.equals(typeCaseLit2) ){
                if( ast.CASELIT instanceof CaseLiteralCHAR){
                    addCharactersValues(valuesData,
                            ((CharacterLiteral) ( ( (CaseLiteralCHAR) ast.CASELIT).CHARLIT)).spelling,
                            ((CharacterLiteral) ( ( (CaseLiteralCHAR) ast.CASELIT2).CHARLIT)).spelling,
                            ast.CASELIT.position);
                }
                else if(ast.CASELIT instanceof CaseLiteralINT){
                    addIntegerValues(valuesData,
                            ((IntegerLiteral) ( ( (CaseLiteralINT) ast.CASELIT).INTLIT)).spelling,
                            ((IntegerLiteral) ( ( (CaseLiteralINT) ast.CASELIT2).INTLIT)).spelling,
                            ast.CASELIT.position);
                }
            }
        return null;
    }

    /*
    It has the parameter, actual values, which is the reference to the ChooseData where the values are stored, 
    value1 which is the string of the values where the array starts and value2 where it finishes, and source position 
    which is used to indicate where the error is found if there is one. It adds the Integer values to the choosedata
    array.
    */
    private void addIntegerValues(ChooseData actualValues,String value1,String value2,SourcePosition position ){
        int value1Int = Integer.parseInt(value1);
        int value2Int = Integer.parseInt(value2) + 1;
        
        for (int i = value1Int; i < value2Int; i++) { 
            if( actualValues.exists( Integer.toString(i) ) ){
                 reporter.reportError ("Repeated Integer Literal in Choose Command", "", position);
            }
            else{
                actualValues.addData( Integer.toString(i ) );
            }
        }
    }
    
    /*
    It has the parameter, actual values, which is the reference to the ChooseData where the values are stored, 
    value1 which is the string of the values where the array starts and value2 where it finishes, and source position 
    which is used to indicate where the error is found if there is one. It adds the character values to the choosedata
    array.
    */
    private void addCharactersValues(ChooseData actualValues,String value1,String value2,SourcePosition position ){
        char value1Char = value1.charAt(1);
        char value2Char = value2.charAt(1);
        char counter;
        for (int i = value1Char ; i < value2Char + 1; i++) {
            counter = (char)i;
            if(!actualValues.exists( "'" + Character.toString(counter) + "'" ) ){
                actualValues.addData( "'" + Character.toString(counter) + "'" );
            }
            else{
                reporter.reportError ("Repeated Character Literal in Choose Command", "", position);
            }
        }
    }

    @Override
public Object visitMultipleRecordTypeDenoter(MultipleRecordTypeDenoter ast, Object o) {
    ast.TD = (TypeDenoter) ast.TD.visit(this, o);
    ast.RTD.visit(this, o);
    return ast;
}

@Override
public Object visitSingleRecordTypeDenoter(SingleRecordTypeDenoter ast, Object o) {
    ast.TD = (TypeDenoter) ast.TD.visit(this, o);
    return ast;
}

@Override
public Object visitRTypeDenoter(RTypeDenoter ast, Object o) {
    ast.REC = (RecordTypeDenoter) ast.REC.visit(this, o);
    return ast;
}

public Object visitTypeDenoterLongIdentifier(TypeDenoterLongIdentifier ast, Object o) {
  	Declaration binding = (Declaration) ast.longIdentifier.visit(this, o);
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
      
    String packageName = defaultPackage;
        if(ast.packageIdentifier != null){
            packageName = ast.packageIdentifier.spelling;
        }
    Declaration binding = hashIdTables.get(packageName).retrieve(ast.identifier.spelling);
    if (binding != null)
      ast.identifier.decl = binding;
    return binding;
  }
    // Packages
    //Uses the VarDeclaration ast to determine the type of the identifier attached.
    //Retrieves the identification table for the package the ast belongs.
    //Returns null
    @Override
    public Object visitVarDeclaration(VarDeclaration ast, Object o) {
    TypeDenoter binding = (TypeDenoter) ast.V.visit(this, o);
    if (binding != null) {
    	ast.I.type = binding;
    }
    String packageName = defaultPackage;
    if(o instanceof String){
        packageName = (String) o;
    }
    hashIdTables.get(packageName).enter(ast.I.spelling, ast);
    if (ast.duplicated)
      reporter.reportError ("identifier \"%\" already declared",
                            ast.I.spelling, ast.position);

    return null;
  }
    //Visits the Type Denoter and returns the type
    @Override
  public Object visitVarSingleDeclarationColon(VarSingleDeclarationColon ast, Object o) {
    ast.T = (TypeDenoter) ast.T.visit(this, o);
    return ast.T;
  }
  //Visits the Expression in the ast and retrieves the type for that expression
  //and returns the type
    @Override
  public Object visitVarSingleDeclarationSingleDeclaration(VarSingleDeclarationSingleDeclaration ast, Object o) {
    ast.T.type = (TypeDenoter) ast.T.visit(this, o);
      return ast.T.type;
  }
  //Enters the identifier and the ast into the general identification table
  //then checks if there is an package with the same name, if the package does not exist
  //then it creates a new identification table and adds it to the hash. At last it visits the declaration
  //of the package and sends the package name as an object so the visitor know which package it belongs to.
  //Return null
    @Override
  public Object visitPackageDeclaration(PackageDeclaration ast, Object o) {
    String packageName = defaultPackage;
    if(o instanceof String){
        packageName = (String) o;
    }
    hashIdTables.get(defaultPackage).enter(ast.ID.spelling, ast);
    if(hashIdTables.containsKey(ast.ID.spelling)){
      reporter.reportError ("Package identifier \"%\" already declared",
                            ast.ID.spelling, ast.position);
    }
    else{
        IdentificationTable packageTable =  IdentificationTable.copyTable(dummyTable);
        hashIdTables.put(ast.ID.spelling, packageTable);
        ast.DEC.visit(this, ast.ID.spelling); // The package name is sent so the visitor knows which package to declare in
    }
    return null;
  }
  //Visits two package declarations and returns null.
    @Override

  public Object visitSequentialPackageDeclaration(SequentialPackageDeclaration ast, Object o) {
    ast.D1.visit(this, o);
    if(ast.D2 != null)
        ast.D2.visit(this, o);
    return null;
  }
  //Checks the packageIdentifier of the ast for the package to which the vName belongs.
  //It visits the identifier and retrieves the declaration and returns the type of
  //declaration.
    @Override
  public Object visitSimpleVname(SimpleVname ast, Object o) {
    String packageName = defaultPackage;
    ast.variable = false;
    ast.type = StdEnvironment.errorType;
     Declaration binding;
    if(ast.P != null)
        packageName = ast.P.spelling;
    else if (o instanceof String)
        packageName = (String) o;
        
    binding = (Declaration) ast.I.visit(this, packageName);

    if (binding == null)
      reportUndeclared(ast.I);
    else
      if (binding instanceof ConstDeclaration) {
        ast.type = ((ConstDeclaration) binding).E.type;
        ast.variable = false;
      } else if (binding instanceof VarDeclaration) {
        if (((VarDeclaration)binding).V.getClass() == VarSingleDeclarationSingleDeclaration.class){
          ast.type = ((VarSingleDeclarationSingleDeclaration) ((VarDeclaration)binding).V).T.type;
        }
        else{
          ast.type = ((VarSingleDeclarationColon) ((VarDeclaration)binding).V).T;
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
            String packageName = defaultPackage;
            if(o instanceof String){
                packageName = (String) o;
            }
            TypeDenoter eType = (TypeDenoter) ast.E.visit(this, o);
            if(o instanceof LoopCasesFORData){
                packageName = ((LoopCasesFORData) o).getPackageName();
                hashIdTables.get(packageName).openScope();
            }
            hashIdTables.get(packageName).enter(ast.I.spelling, ast);
            if (ast.duplicated)
              reporter.reportError ("identifier \"%\" already declared",
                                    ast.I.spelling, ast.position);
            return eType;
         }

	@Override
    public Object visitCompoundDeclarationRecursive(CompoundDeclarationRecursive ast, Object o) {
        String packageName = defaultPackage;
        if(o instanceof String){
            packageName = (String) o;
        }
        for (int i = 0; i < 2; i++) {
          ast.PF.visit(this, new RecursiveProcFuncData(i,packageName));
        }
        return null;
    }

    @Override 
    public Object visitProcFuncs(ProcFuncs ast, Object o) {
          ast.PF1.visit(this,o);
          ast.PF2.visit(this, o);
        return null;
    }

    @Override
    public Object visitProcProcFunc(ProcProcFunc ast, Object o) {
        String packageName = defaultPackage;
        Integer iteration = 0;
        if(o instanceof RecursiveProcFuncData){
            RecursiveProcFuncData data = (RecursiveProcFuncData) o;
            if(data.getPackageName() != null)
                packageName = data.getPackageName();
            if(data.getIteration() != null)
                iteration = data.getIteration();
        }
        
        if (iteration.equals(new Integer(0))) {
          hashIdTables.get(packageName).enter(ast.ID.spelling, ast); // permits recursion
          ast.FPS.visit(this, packageName);
          if (ast.duplicated)
            reporter.reportError ("identifier \"%\" already declared",
                                  ast.ID.spelling, ast.position);
        }
        else {
          ast.COM.visit(this, packageName);
        }
        return null;
    }

    @Override
    public Object visitFuncProcFunc(FuncProcFunc ast, Object o) {
        String packageName = defaultPackage;
        Integer iteration = 0;
        if(o instanceof RecursiveProcFuncData){
            RecursiveProcFuncData data = (RecursiveProcFuncData) o;
            if(data.getPackageName() != null)
                packageName = data.getPackageName();
            if(data.getIteration() != null)
                iteration = data.getIteration();
        }
        
        if (iteration.equals(0)) {

        hashIdTables.get(packageName).enter(ast.ID.spelling, ast);
        ast.FPS.visit(this, packageName);

          if (ast.duplicated)
            reporter.reportError ("identifier \"%\" already declared",
                                  ast.ID.spelling, ast.position);
          ast.TD = (TypeDenoter) ast.TD.visit(this, packageName);

        }
        else {
          TypeDenoter eType = (TypeDenoter) ast.EXP.visit(this, packageName);
          if (! ast.TD.equals(eType))
            reporter.reportError ("body of function \"%\" has wrong type",
                                  ast.ID.spelling, ast.EXP.position);
        }
        return null;
    }

    @Override
    public Object visitLoopCasesWhile(LoopCasesWhile ast, Object o) {
        TypeDenoter eType = (TypeDenoter) ast.EXP.visit(this, o);
        if (! eType.equals(StdEnvironment.booleanType))
          reporter.reportError ("Boolean expression expected here", "",
				ast.EXP.position);
        ast.COM.visit(this, o);
        return null;
    }

    @Override
    public Object visitLoopCasesUntil(LoopCasesUntil ast, Object o) {
        TypeDenoter eType = (TypeDenoter) ast.EXP.visit(this, o);
        if (! eType.equals(StdEnvironment.booleanType))
          reporter.reportError ("Boolean expression expected here", "",
				ast.EXP.position);
        ast.COM.visit(this, o);
        return null;
    }

    @Override
    public Object visitLoopCasesDo(LoopCasesDo ast, Object o) {
        ast.COM.visit(this, o);
				ast.DO.visit(this, o);
        return null;
    }

    @Override
    public Object visitLoopCasesFOR(LoopCasesFOR ast, Object o) {
         String packageName = defaultPackage;
         if(o instanceof String){
            packageName = (String) o;
        }
        TypeDenoter eType2 = (TypeDenoter) ast.EXP2.visit(this, o);
      	if (! eType2.equals(StdEnvironment.integerType))
          reporter.reportError ("Integer expression expected here", "",
				ast.EXP2.position);
        TypeDenoter eType1 = (TypeDenoter) ast.DECL.visit(this, new LoopCasesFORData(packageName));
      	if (! eType1.equals(StdEnvironment.integerType))
          reporter.reportError ("Integer expression expected here", "",
				ast.DECL.position); //TODO revisar si la posicion esta bien.
        Object output = ast.FOR.visit(this, o);
        if (output != null) {
            TypeDenoter eType3 = (TypeDenoter) output;
            if (! eType3.equals(StdEnvironment.booleanType))
              reporter.reportError ("Boolean expression expected here", "",
            ast.FOR.position); //TODO revisar si la posicion esta bien.
        }
      	hashIdTables.get(packageName).closeScope();
        return null;
    }

    @Override
    public Object visitDoLoopUntil(DoLoopUntil ast, Object o) {
        TypeDenoter eType = (TypeDenoter) ast.EXP.visit(this, o);
        if (! eType.equals(StdEnvironment.booleanType))
          reporter.reportError ("Boolean expression expected here", "",
				ast.EXP.position);
      	return null;
    }

    @Override
    public Object visitDoLoopWhile(DoLoopWhile ast, Object o) {
        TypeDenoter eType = (TypeDenoter) ast.EXP.visit(this, o);
        if (! eType.equals(StdEnvironment.booleanType))
          reporter.reportError ("Boolean expression expected here", "",
				ast.EXP.position);
      	return null;
    }

  	@Override
    public Object visitForLoopDo(ForLoopDo ast, Object o) {
      	ast.COM.visit(this, o);
        return null;
    }

    @Override
    public Object visitForLoopUntil(ForLoopUntil ast, Object o) {
      	TypeDenoter eType = (TypeDenoter) ast.EXP.visit(this, o);
        ast.COM.visit(this, o);
        return eType;
    }

    @Override
    public Object visitForLoopWhile(ForLoopWhile ast, Object o) {
      	TypeDenoter eType = (TypeDenoter) ast.EXP.visit(this, o);
        ast.COM.visit(this, o);
        return eType;
    }
    //endregion


    //region Jose

    /*It returns the integer type of the std enviroment, to determine if it the expression visited, is a integer*/
    @Override
    public Object visitCaseLiteralINT(CaseLiteralINT ast, Object o) {
        return StdEnvironment.integerType;
    }
    
    /*It returns the integer type of the std enviroment, to determine if it the expression visited, is a integer*/
@Override
    public Object visitCaseLiteralCHAR(CaseLiteralCHAR ast, Object o) {
        return StdEnvironment.charType;
    }


 @Override
    /*It visits the ast where there are two or more CaseRange nested*/
    public Object visitSequentialCaseRange(SequentialCaseRange ast, Object typeExpression) {
        ast.C1.visit(this, typeExpression);
        ast.C2.visit(this, typeExpression);
        return null;
    }


@Override
    /*It visitis the expression to determine the value of the expression*/
    public Object visitCaseLiterals(CaseLiterals ast, Object typeExpression) {
        ast.CASERANGE.visit(this, typeExpression);
        return null;
    }

    
@Override
    /*It uses the object to get the type of the main expression and determine if 
its expression matches it, also, visits its command. */
    public Object visitCaseWhen(CaseWhen ast, Object typeExpression) {
        ast.CASELIT.visit(this, typeExpression);
        ast.COM.visit(this, null);
        return null;
    }

@Override
    /*
        It visits the cases, it is used when a choose has two or more cases. 
    */
    public Object visitSequentialCase(SequentialCase ast, Object typeExpression) {
        ast.C1.visit(this, typeExpression);
        ast.C2.visit(this, typeExpression);
        return null;
    }


@Override
    /*
    It visits the else case of the choose, and since it doesn't have an expression, 
    there's no need to check the type of the main expression
    */
    public Object visitElseCase(ElseCase ast, Object o) {
        ast.COM.visit(this, o);
        return null;
    }


 @Override
    /*It visits the cases of the choose and the optional else case*/
    public Object visitCases(Cases ast, Object typeExpression) {
        ast.CASE1.visit(this, typeExpression);
        if(ast.CASE2 != null)
            ast.CASE2.visit(this, typeExpression);
        return null;
    }



    @Override
    /*It determine if the expression of the choose is a integer or a char, and if it isnt it reportes an error, 
    and visits the command of the choose command, and verifies that its is correct, 
    */
    public Object visitChooseCommand(ChooseCommand ast, Object o) {
        TypeDenoter expressionType = (TypeDenoter) ast.EXP.visit(this, o);
        if( ! expressionType.equals( StdEnvironment.charType) && ! expressionType.equals( StdEnvironment.integerType) ){
            reporter.reportError("Integer or Char expression expected here", "", ast.EXP.position);
        }
        ChooseData expressionData = new ChooseData(expressionType);
        ast.COM.visit(this, expressionData);
        return null;
    }


@Override
    /* it visits the declaration, it is for the case where theres only one of them*/
    public Object visitCompoundDeclarationSingleDeclaration(CompoundDeclarationSingleDeclaration ast, Object o) {
        ast.SD.visit(this, o);
        return null;
    }



    @Override
    /*It is for the case in the par declarations where there are two or more Single declarations it 
    first declares the declarations in its private scope to verify that none of them are using each other, 
    then it declares them in the actual scope to maintain the declarations
    */
    public Object visitSequentialSingleDeclaration(SequentialSingleDeclaration ast, Object o) {
        String packageName = defaultPackage;
        if(o instanceof String){
            packageName = (String) o;
        }
       hashIdTables.get(packageName).openScope();
        ast.D1.visit(this, o);
        hashIdTables.get(packageName).closeScope();
        
        hashIdTables.get(packageName).openScope();
        ast.D2.visit(this, o);
        hashIdTables.get(packageName).closeScope();
        
        ast.D1.visit(this, o);
        ast.D2.visit(this, o);
        
        return null;
    }

    @Override
    /*
    The id table is now treated as a  doubled linked list, where each node has access to its previous and its next, 
    and at the beginning of the private declaration, aka before D1 the node where it starts is saved in startPoint 
    and then where D2 starts is saved in inStart, at the end of the visits, D1 is left behind and D2 starts where D1 
    used to start.
    */
    public Object visitCompoundDeclarationPrivate(CompoundDeclarationPrivate ast, Object o) {
        String packageName = defaultPackage;
        if(o instanceof String){
            packageName = (String) o;
        }
        IdEntry startPoint = hashIdTables.get(packageName).getLatest();
        ast.D1.visit(this, o);
        IdEntry inStart = hashIdTables.get(packageName).getLatest();
        ast.D2.visit(this, o);
        inStart.next.previous = startPoint;
        return null;
    }
    //endregion 
    

  public Object visitAssignCommand(AssignCommand ast, Object o) {
    TypeDenoter vType = (TypeDenoter) ast.V.visit(this, o);
    TypeDenoter eType = (TypeDenoter) ast.E.visit(this, o);
    if (!ast.V.variable)
      reporter.reportError ("LHS of assignment is not a variable", "", ast.V.position);
    if (! eType.equals(vType))
      reporter.reportError ("assignment incompatibilty", "", ast.position);
    return null;
  }


  @Override
  public Object visitCallCommand(CallCommand ast, Object o) {
    String packageName = defaultPackage;
    String callerPackage = defaultPackage;
    if (ast.I instanceof CompoundIdentifier){
        CompoundIdentifier ci = (CompoundIdentifier)ast.I;
        if(ci.packageIdentifier != null){
            packageName = ci.packageIdentifier.spelling;  // If a compound identifier it's found, there's a possibility it's a package call.
        }
    }
    if(o instanceof String){
        callerPackage = (String) o;
    }
    Declaration binding = (Declaration) ast.I.visit(this, packageName);
    if (binding == null)
      reportUndeclared(ast.I);
    else if (binding instanceof ProcDeclaration) {
      ast.APS.visit(this, new FormalParameterData((((ProcDeclaration) binding).FPS), packageName, callerPackage));
    }else if (binding instanceof ProcProcFunc) {
      ast.APS.visit(this, new FormalParameterData((((ProcProcFunc) binding).FPS), packageName,callerPackage));
    } else if (binding instanceof ProcFormalParameter) {
      ast.APS.visit(this, new FormalParameterData((((ProcFormalParameter) binding).FPS), packageName,callerPackage));
    } else
      reporter.reportError("\"%\" is not a procedure identifier",
                           ast.I.spelling, ast.I.position);
    return null;
  }

  public Object visitEmptyCommand(EmptyCommand ast, Object o) {
    return null;
  }

  public Object visitIfCommand(IfCommand ast, Object o) {
    TypeDenoter eType = (TypeDenoter) ast.E.visit(this, o);
    if (! eType.equals(StdEnvironment.booleanType))
      reporter.reportError("Boolean expression expected here", "", ast.E.position);
    ast.C1.visit(this, o);
    ast.C2.visit(this, o);
    return null;
  }

  public Object visitLetCommand(LetCommand ast, Object o) {
    String packageName = defaultPackage;
    if(o instanceof String){
        packageName = (String) o;
    }
    hashIdTables.get(packageName).openScope();
    ast.D.visit(this, o);
    ast.C.visit(this, o);
    hashIdTables.get(packageName).closeScope();
    return null;
  }

  public Object visitSequentialCommand(SequentialCommand ast, Object o) {
    ast.C1.visit(this, o);
    ast.C2.visit(this, o);
    return null;
  }

  public Object visitWhileCommand(WhileCommand ast, Object o) {
    TypeDenoter eType = (TypeDenoter) ast.E.visit(this, o);
    if (! eType.equals(StdEnvironment.booleanType))
      reporter.reportError("Boolean expression expected here", "", ast.E.position);
    ast.C.visit(this, o);
    return null;
  }

    // Iterative commands

    @Override
    public Object visitCallLoopCases(CallLoopCases ast, Object o) {
        ast.LOOP.visit(this, o);
        return null;
    }

  // Expressions

  // Returns the TypeDenoter denoting the type of the expression. Does
  // not use the given object.


  public Object visitArrayExpression(ArrayExpression ast, Object o) {
    TypeDenoter elemType = (TypeDenoter) ast.AA.visit(this, o);
    IntegerLiteral il = new IntegerLiteral(new Integer(ast.AA.elemCount).toString(),
                                           ast.position);
    ast.type = new ArrayTypeDenoter(il, elemType, ast.position);
    return ast.type;
  }

  public Object visitBinaryExpression(BinaryExpression ast, Object o) {

    TypeDenoter e1Type = (TypeDenoter) ast.E1.visit(this, o);
    TypeDenoter e2Type = (TypeDenoter) ast.E2.visit(this, o);
    Declaration binding = (Declaration) ast.O.visit(this, o);

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
    String packageName = defaultPackage;
    String callerPackage = defaultPackage;
    if(ast.I.packageIdentifier != null){
        packageName = ast.I.packageIdentifier.spelling;
    }
    if(o instanceof String){
        callerPackage = (String) o;
    }
    Declaration binding = (Declaration) ast.I.visit(this, o);
    if (binding == null) {
      reportUndeclared(ast.I);
      ast.type = StdEnvironment.errorType;
    } else if (binding instanceof FuncDeclaration) {
      ast.APS.visit(this, new FormalParameterData((((FuncDeclaration) binding).FPS), packageName,callerPackage));
      ast.type = ((FuncDeclaration) binding).T;
    } else if (binding instanceof FuncProcFunc) {
      ast.APS.visit(this, new FormalParameterData((((FuncProcFunc) binding).FPS), packageName,callerPackage));
      ast.type = ((FuncProcFunc) binding).TD;
    }else if (binding instanceof FuncFormalParameter) {
      ast.APS.visit(this, new FormalParameterData((((FuncFormalParameter) binding).FPS), packageName,callerPackage));
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
    TypeDenoter e1Type = (TypeDenoter) ast.E1.visit(this, o);
    if (! e1Type.equals(StdEnvironment.booleanType))
      reporter.reportError ("Boolean expression expected here", "",
                            ast.E1.position);
    TypeDenoter e2Type = (TypeDenoter) ast.E2.visit(this, o);
    TypeDenoter e3Type = (TypeDenoter) ast.E3.visit(this, o);
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
    String packageName = defaultPackage;
    if(o instanceof String){
        packageName = (String) o;
    }
    hashIdTables.get(packageName).openScope();
    ast.D.visit(this, o);
    ast.type = (TypeDenoter) ast.E.visit(this, o);
    hashIdTables.get(packageName).closeScope();
    return ast.type;
  }

  public Object visitRecordExpression(RecordExpression ast, Object o) {
    FieldTypeDenoter rType = (FieldTypeDenoter) ast.RA.visit(this, o);
    ast.type = new RecordTypeDenoter(rType, ast.position);
    return ast.type;
  }

  public Object visitUnaryExpression(UnaryExpression ast, Object o) {

    TypeDenoter eType = (TypeDenoter) ast.E.visit(this, o);
    Declaration binding = (Declaration) ast.O.visit(this, o);
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
    ast.type = (TypeDenoter) ast.V.visit(this, o);
    return ast.type;
  }

  @Override
    public Object visitSecExpression(SecExpression ast, Object o) {
        ast.type = (TypeDenoter) ast.secExpression.visit(this, o);
        return ast.type;
    }

    @Override
    public Object visitIntLiteralExpression(IntLiteralExpression ast, Object o) {
        ast.type = StdEnvironment.integerType;
        return ast.type;
    }

    @Override
    public Object visitOperatorExpression(OperatorExpression ast, Object o) { //Used to visit multiple expressions separated by an operator
        ast.O.visit(this, o);
        ast.type = (TypeDenoter) ast.E.visit(this, o);
        return ast.type;

    }

    @Override
    public Object visitLParenExpression(LParenExpression ast, Object o) {
        return ast.E.visit(this, o);
    }

    @Override
    public Object visitLCurlyExpression(LCurlyExpression ast, Object o) {
        FieldTypeDenoter rType = (FieldTypeDenoter) ast.RA.visit(this, o);
        ast.type = new RecordTypeDenoter(rType, ast.position);
        return ast.type;
    }

    @Override
    public Object visitLBracketExpression(LBracketExpression ast, Object o) {
        TypeDenoter elemType = (TypeDenoter) ast.AA.visit(this, o);
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
        if(o instanceof String){
        packageName = (String) o;
        }
        Declaration binding = hashIdTables.get(packageName).retrieve(ast.identifier.spelling);
        if (binding != null)
          ast.identifier.decl = binding;
        return binding;
    }

    @Override
    public Object visitLIdentifierExpression(LIdentifierExpression ast, Object o) { //previous call expression
        Declaration binding = (Declaration) ast.LI.visit(this, o);
            String packageName = defaultPackage;
            String callerPackage = defaultPackage;
        if(ast.LI.packageIdentifier != null){
            packageName = ast.LI.packageIdentifier.spelling;

        }
        if(o instanceof String){
            callerPackage = (String) o;
        }
        if (binding == null) {
            reportUndeclared(ast.LI);
            ast.type = StdEnvironment.errorType;
          } else if (binding instanceof FuncDeclaration) {
            ast.APS.visit(this, new FormalParameterData((((FuncDeclaration) binding).FPS), packageName,callerPackage));
            ast.type = ((FuncDeclaration) binding).T;
          } else if (binding instanceof FuncProcFunc) {
            ast.APS.visit(this, new FormalParameterData((((FuncProcFunc) binding).FPS), packageName,callerPackage));
            ast.type = ((FuncProcFunc) binding).TD;
          }else if (binding instanceof FuncFormalParameter) {
            ast.APS.visit(this, new FormalParameterData((((FuncFormalParameter) binding).FPS), packageName,callerPackage));
            ast.type = ((FuncFormalParameter) binding).T;
          } else
            reporter.reportError("\"%\" is not a function identifier",
                                 ast.LI.spelling, ast.LI.position);

        return ast.type;
    }

    @Override
    public Object visitAssignExpression(AssignExpression ast, Object o) {
        return ast.V.visit(this, o);
    }


  // Declarations

  // Always returns null. Does not use the given object.
  public Object visitBinaryOperatorDeclaration(BinaryOperatorDeclaration ast, Object o) {
    return null;
  }

  public Object visitFuncDeclaration(FuncDeclaration ast, Object o) {
    ast.T = (TypeDenoter) ast.T.visit(this, o);
    String packageName = defaultPackage;
    if(o instanceof String){
        packageName = (String) o;
    }
    hashIdTables.get(packageName).enter (ast.I.spelling, ast); // permits recursion
    if (ast.duplicated)
      reporter.reportError ("identifier \"%\" already declared",
                            ast.I.spelling, ast.position);
    hashIdTables.get(packageName).openScope();
    ast.FPS.visit(this, o);
    TypeDenoter eType = (TypeDenoter) ast.E.visit(this, o);
    hashIdTables.get(packageName).closeScope();
    if (! ast.T.equals(eType))
      reporter.reportError ("body of function \"%\" has wrong type",
                            ast.I.spelling, ast.E.position);
    return null;
  }

  public Object visitProcDeclaration(ProcDeclaration ast, Object o) {
    String packageName = defaultPackage;
    if(o instanceof String){
        packageName = (String) o;
    }
    hashIdTables.get(packageName).enter (ast.I.spelling, ast); // permits recursion
    if (ast.duplicated)
      reporter.reportError ("identifier \"%\" already declared",
                            ast.I.spelling, ast.position);
    hashIdTables.get(packageName).openScope();
    ast.FPS.visit(this, o);
    ast.C.visit(this, o);
    hashIdTables.get(packageName).closeScope();
    return null;
  }

  public Object visitSequentialDeclaration(SequentialDeclaration ast, Object o) {
    ast.D1.visit(this, o);
    ast.D2.visit(this, o);
    return null;
  }

  public Object visitTypeDeclaration(TypeDeclaration ast, Object o) {
    ast.T = (TypeDenoter) ast.T.visit(this, o);
    String packageName = defaultPackage;
    if(o instanceof String){
        packageName = (String) o;
    }
    hashIdTables.get(packageName).enter(ast.I.spelling, ast);
    if (ast.duplicated)
      reporter.reportError ("identifier \"%\" already declared",
                            ast.I.spelling, ast.position);
    return null;
  }

  public Object visitUnaryOperatorDeclaration(UnaryOperatorDeclaration ast, Object o) {
    return null;
  }

  public Object visitMultipleArrayAggregate(MultipleArrayAggregate ast, Object o) {
    TypeDenoter eType = (TypeDenoter) ast.E.visit(this, o);
    TypeDenoter elemType = (TypeDenoter) ast.AA.visit(this, o);
    ast.elemCount = ast.AA.elemCount + 1;
    if (! eType.equals(elemType))
      reporter.reportError ("incompatible array-aggregate element", "", ast.E.position);
    return elemType;
  }

  public Object visitSingleArrayAggregate(SingleArrayAggregate ast, Object o) {
    TypeDenoter elemType = (TypeDenoter) ast.E.visit(this, o);
    ast.elemCount = 1;
    return elemType;
  }

  // Record Aggregates

  // Returns the TypeDenoter for the Record Aggregate. Does not use the
  // given object.

  public Object visitMultipleRecordAggregate(MultipleRecordAggregate ast, Object o) {
    TypeDenoter eType = (TypeDenoter) ast.E.visit(this, o);
    FieldTypeDenoter rType = (FieldTypeDenoter) ast.RA.visit(this, o);
    TypeDenoter fType = checkFieldIdentifier(rType, ast.I);
    if (fType != StdEnvironment.errorType)
      reporter.reportError ("duplicate field \"%\" in record",
                            ast.I.spelling, ast.I.position);
    ast.type = new MultipleFieldTypeDenoter(ast.I, eType, rType, ast.position);
    return ast.type;
  }

  public Object visitSingleRecordAggregate(SingleRecordAggregate ast, Object o) {
    TypeDenoter eType = (TypeDenoter) ast.E.visit(this, o);
    ast.type = new SingleFieldTypeDenoter(ast.I, eType, ast.position);
    return ast.type;
  }

  // Formal Parameters

  // Always returns null. Does not use the given object.

  public Object visitConstFormalParameter(ConstFormalParameter ast, Object o) {
    String packageName = defaultPackage;
    if(o instanceof String){
        packageName = (String) o;
    }
    ast.T = (TypeDenoter) ast.T.visit(this, packageName);
    hashIdTables.get(packageName).enter(ast.I.spelling, ast);
    if (ast.duplicated)
      reporter.reportError ("duplicated formal parameter \"%\"",
                            ast.I.spelling, ast.position);
    return null;
  }

  public Object visitFuncFormalParameter(FuncFormalParameter ast, Object o) {
    String packageName = defaultPackage;
    if(o instanceof String){
        packageName = (String) o;
    }
    hashIdTables.get(packageName).openScope();
    ast.FPS.visit(this, o);
    hashIdTables.get(packageName).closeScope();
    ast.T = (TypeDenoter) ast.T.visit(this, o);

    hashIdTables.get(packageName).enter(ast.I.spelling, ast);
    if (ast.duplicated)
      reporter.reportError ("duplicated formal parameter \"%\"",
                            ast.I.spelling, ast.position);
    return null;
  }

  public Object visitProcFormalParameter(ProcFormalParameter ast, Object o) {
    String packageName = defaultPackage;
    if(o instanceof String){
        packageName = (String) o;
    }
    hashIdTables.get(packageName).openScope();
    ast.FPS.visit(this, o);
    hashIdTables.get(packageName).closeScope();
    if(o instanceof String){
        packageName = (String) o;
    }
    hashIdTables.get(packageName).enter(ast.I.spelling, ast);
    if (ast.duplicated)
      reporter.reportError ("duplicated formal parameter \"%\"",
                            ast.I.spelling, ast.position);
    return null;
  }

  public Object visitVarFormalParameter(VarFormalParameter ast, Object o) {
    String packageName = defaultPackage;
    if(o instanceof String){
        packageName = (String) o;
    }
    ast.T = (TypeDenoter) ast.T.visit(this, o);

    hashIdTables.get(packageName).enter(ast.I.spelling, ast);
    if (ast.duplicated)
      reporter.reportError ("duplicated formal parameter \"%\"",
                            ast.I.spelling, ast.position);
    return null;
  }

  public Object visitEmptyFormalParameterSequence(EmptyFormalParameterSequence ast, Object o) {
    return null;
  }

  public Object visitMultipleFormalParameterSequence(MultipleFormalParameterSequence ast, Object o) {
    ast.FP.visit(this, o);
    ast.FPS.visit(this, o);
    return null;
  }

  public Object visitSingleFormalParameterSequence(SingleFormalParameterSequence ast, Object o) {
    ast.FP.visit(this, o);
    return null;
  }

  // Actual Parameters

  // Always returns null. Uses the given FormalParameter.

  public Object visitConstActualParameter(ConstActualParameter ast, Object o) {
    FormalParameter fp = null;
    String packageName = defaultPackage;
    String callerPackage = defaultPackage;
    if(o instanceof FormalParameter){
        fp = (FormalParameter) o;
    }
    else if(o instanceof ActualParameterData){
        fp = ((ActualParameterData) o).getFP();
        if(((ActualParameterData) o).getPackageName() != null)
            packageName = ((ActualParameterData) o).getPackageName();
        if(((ActualParameterData) o).getCallerPackage() != null)
            callerPackage = ((ActualParameterData) o).getCallerPackage();
        else
            callerPackage = packageName;
    }
    TypeDenoter eType = (TypeDenoter) ast.E.visit(this, callerPackage);

    if (! (fp instanceof ConstFormalParameter))
      reporter.reportError ("const actual parameter not expected here", "",
                            ast.position);
    else if (  eType != null  && !eType.equals(((ConstFormalParameter) fp).T)  )
      reporter.reportError ("wrong type for const actual parameter", "",
                            ast.E.position);
    return null;
  }

  public Object visitFuncActualParameter(FuncActualParameter ast, Object o) {
    FormalParameter fp = null;   String packageName = defaultPackage;
    String callerPackage = defaultPackage;
    if(o instanceof FormalParameter){
        fp = (FormalParameter) o;
    }
    else if(o instanceof ActualParameterData){
        fp = ((ActualParameterData) o).getFP();
        if(((ActualParameterData) o).getPackageName() != null)
            packageName = ((ActualParameterData) o).getPackageName();
        if(((ActualParameterData) o).getCallerPackage() != null)
            callerPackage = ((ActualParameterData) o).getCallerPackage();
        else
            callerPackage = packageName;
    }

    Declaration binding = (Declaration) ast.I.visit(this, callerPackage);
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
    FormalParameter fp = null;
    String packageName = defaultPackage;
    String callerPackage = defaultPackage;
    if(o instanceof FormalParameter){
        fp = (FormalParameter) o;
    }
    else if(o instanceof ActualParameterData){
        fp = ((ActualParameterData) o).getFP();
        if(((ActualParameterData) o).getPackageName() != null)
            packageName = ((ActualParameterData) o).getPackageName();
        if(((ActualParameterData) o).getCallerPackage() != null)
            callerPackage = ((ActualParameterData) o).getCallerPackage();
        else
            callerPackage = packageName;
    }
    
    Declaration binding = (Declaration) ast.I.visit(this, callerPackage);
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
    FormalParameter fp = null;
    String packageName = defaultPackage;
    String callerPackage = defaultPackage;
    if(o instanceof FormalParameter){
        fp = (FormalParameter) o;
    }
    else if(o instanceof ActualParameterData){
        fp = ((ActualParameterData) o).getFP();
        if(((ActualParameterData) o).getPackageName() != null)
            packageName = ((ActualParameterData) o).getPackageName();
        if(((ActualParameterData) o).getCallerPackage() != null)
            callerPackage = ((ActualParameterData) o).getCallerPackage();
        else
            callerPackage = packageName;
    }
    
    TypeDenoter vType = (TypeDenoter) ast.V.visit(this, callerPackage);
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
  
    FormalParameterSequence fps = null;
    if(o instanceof FormalParameterSequence){
        fps = (FormalParameterSequence) o;
    }
    else if(o instanceof FormalParameterData){
        fps = ((FormalParameterData) o).getFPS();
    }
    
    if (! (fps instanceof EmptyFormalParameterSequence))
      reporter.reportError ("too few actual parameters", "", ast.position);
    return null;
  }

  public Object visitMultipleActualParameterSequence(MultipleActualParameterSequence ast, Object o) {
    FormalParameterSequence fps = null;
    String packageName = defaultPackage;
    String callerPackage = defaultPackage;
    if(o instanceof FormalParameterSequence){
        fps = (FormalParameterSequence) o;
    }
    else if(o instanceof FormalParameterData){
        fps = ((FormalParameterData) o).getFPS();
        if(((FormalParameterData) o).getPackageName() != null)
            packageName = ((FormalParameterData) o).getPackageName();
        if(((FormalParameterData) o).getCallerPackage() != null)
            callerPackage = ((FormalParameterData) o).getCallerPackage();
        else
            callerPackage = packageName;
    }
    
    
    if (! (fps instanceof MultipleFormalParameterSequence))
      reporter.reportError ("too many actual parameters", "", ast.position);
    else {
      ast.AP.visit(this, new ActualParameterData((((MultipleFormalParameterSequence) fps).FP),packageName,callerPackage));
      ast.APS.visit(this,new ActualParameterData((((MultipleFormalParameterSequence) fps).FP),packageName,callerPackage));
    }
    return null;
  }

  public Object visitSingleActualParameterSequence(SingleActualParameterSequence ast, Object o) {
    FormalParameterSequence fps = null;
    String packageName = defaultPackage;
    String callerPackage = defaultPackage;
    if(o instanceof FormalParameterSequence){
        fps = (FormalParameterSequence) o;
    }
    else if(o instanceof FormalParameterData){
        fps = ((FormalParameterData) o).getFPS();
        if(((FormalParameterData) o).getPackageName() != null)
            packageName = ((FormalParameterData) o).getPackageName();
        if(((FormalParameterData) o).getCallerPackage() != null)
            callerPackage = ((FormalParameterData) o).getCallerPackage();
        else
            callerPackage = packageName;
    }
    
    if (! (fps instanceof SingleFormalParameterSequence))
      reporter.reportError ("incorrect number of actual parameters", "", ast.position);
    else {
      ast.AP.visit(this,new ActualParameterData((((SingleFormalParameterSequence) fps).FP),packageName,callerPackage));
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
    ast.T = (TypeDenoter) ast.T.visit(this, o);
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
    Declaration binding = (Declaration) ast.I.visit(this, o);
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
    ast.FT = (FieldTypeDenoter) ast.FT.visit(this, o);
    return ast;
  }

  public Object visitMultipleFieldTypeDenoter(MultipleFieldTypeDenoter ast, Object o) {
    ast.T = (TypeDenoter) ast.T.visit(this, o);
    ast.FT.visit(this, o);
    return ast;
  }

  public Object visitSingleFieldTypeDenoter(SingleFieldTypeDenoter ast, Object o) {
    ast.T = (TypeDenoter) ast.T.visit(this, o);
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
    if(I instanceof CompoundIdentifier){
        CompoundIdentifier ci = (CompoundIdentifier) I;
        packageName = ci.packageIdentifier.spelling;
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
    String packageName = defaultPackage;
    if(o instanceof String){
        packageName = (String) o;
    }
    Declaration binding = hashIdTables.get(packageName).retrieve(O.spelling);
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
    TypeDenoter vType = (TypeDenoter) ast.V.visit(this, o);
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


  public Object visitSubscriptVname(SubscriptVname ast, Object o) {
    TypeDenoter vType = (TypeDenoter) ast.V.visit(this, o);
    ast.variable = ast.V.variable;
    TypeDenoter eType = (TypeDenoter) ast.E.visit(this, o);
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
      if(ast.P != null)
          ast.P.visit(this, o);
      ast.C.visit(this, o);
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
      IdentificationTable idTable = new IdentificationTable ();
      this.hashIdTables = new HashMap<String, IdentificationTable>();
      this.hashIdTables.put(defaultPackage, idTable);
      establishStdEnvironment();
      dummyTable = IdentificationTable.copyTable(idTable);
    }

    private HashMap<String,IdentificationTable> hashIdTables;
    private static SourcePosition dummyPos = new SourcePosition();
    private ErrorReporter reporter;
    private final String defaultPackage = "_global_";
    private final IdentificationTable dummyTable;

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
    String packageName = defaultPackage;
    TypeDeclaration binding;

    binding = new TypeDeclaration(new Identifier(id, dummyPos), typedenoter, dummyPos);
    hashIdTables.get(packageName).enter(id, binding);
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
    hashIdTables.get(defaultPackage).enter(id, binding);
    return binding;
  }

  // Creates a small AST to represent the "declaration" of a standard
  // type, and enters it in the identification table.

  private ProcDeclaration declareStdProc (String id, FormalParameterSequence fps) {

    ProcDeclaration binding;

    binding = new ProcDeclaration(new Identifier(id, dummyPos), fps,
                                  new EmptyCommand(dummyPos), dummyPos);
    hashIdTables.get(defaultPackage).enter(id, binding);
    return binding;
  }

  // Creates a small AST to represent the "declaration" of a standard
  // type, and enters it in the identification table.

  private FuncDeclaration declareStdFunc (String id, FormalParameterSequence fps,
                                          TypeDenoter resultType) {

    FuncDeclaration binding;

    binding = new FuncDeclaration(new Identifier(id, dummyPos), fps, resultType,
                                  new EmptyExpression(dummyPos), dummyPos);
    hashIdTables.get(defaultPackage).enter(id, binding);
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
    hashIdTables.get(defaultPackage).enter(op, binding);
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
    hashIdTables.get(defaultPackage).enter(op, binding);
    return binding;
  }

  // Creates small ASTs to represent the standard types.
  // Creates small ASTs to represent "declarations" of standard types,
  // constants, procedures, functions, and operators.
  // Enters these "declarations" in the identification table.

  private final static Identifier dummyI = new Identifier("", dummyPos);

  private void establishStdEnvironment () {

    // hashIdTables.get(packageName).startIdentification();
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
