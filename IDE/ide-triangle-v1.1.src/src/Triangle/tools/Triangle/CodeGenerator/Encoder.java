/*
 * @(#)Encoder.java                        2.1 2003/10/07
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

package Triangle.tools.Triangle.CodeGenerator;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import Triangle.tools.TAM.Instruction;
import Triangle.tools.TAM.Machine;
import Triangle.tools.Triangle.ErrorReporter;
import Triangle.StdEnvironment;
import Triangle.tools.Triangle.AbstractSyntaxTrees.*;
import Utilities.ChooseCode;


public final class Encoder implements Visitor {
    

  // Commands
  public Object visitAssignCommand(AssignCommand ast, Object o) {
        Frame frame = (Frame) o;
    Integer valSize = (Integer) ast.E.visit(this, frame);
    encodeStore(ast.V, new Frame (frame, valSize.intValue()),
		valSize.intValue());
    return null;
  }

  public Object visitCallCommand(CallCommand ast, Object o) {
    Frame frame = (Frame) o;
    Integer argsSize = (Integer) ast.APS.visit(this, frame);
    ast.I.visit(this, new Frame(frame.level, argsSize));
    return null;
  }

  public Object visitEmptyCommand(EmptyCommand ast, Object o) {
    return null;
  }

  public Object visitIfCommand(IfCommand ast, Object o) {
    Frame frame = (Frame) o;
    int jumpifAddr, jumpAddr;
    Integer valSize = (Integer) ast.E.visit(this, frame);
    jumpifAddr = nextInstrAddr;
    emit(Machine.JUMPIFop, Machine.falseRep, Machine.CBr, 0);
    ast.C1.visit(this, frame);
    jumpAddr = nextInstrAddr;
    emit(Machine.JUMPop, 0, Machine.CBr, 0);
    patch(jumpifAddr, nextInstrAddr);
    ast.C2.visit(this, frame);
    patch(jumpAddr, nextInstrAddr);
    return null;
  }

  public Object visitLetCommand(LetCommand ast, Object o) {
    Frame frame = (Frame) o;
    int extraSize = (Integer) ast.D.visit(this, frame);
    ast.C.visit(this, new Frame(frame, extraSize));
    if (extraSize > 0)
      emit(Machine.POPop, 0, 0, extraSize);
    return null;
  }

  public Object visitSequentialCommand(SequentialCommand ast, Object o) {
    ast.C1.visit(this, o);
    ast.C2.visit(this, o);
    return null;
  }

  //loop cases

  //Visits a loop case, returns null, passes the received parameter forward.
    @Override
    public Object visitCallLoopCases(CallLoopCases ast, Object o) {
        ast.LOOP.visit(this, o);
        return null;
    }
     @Override
    public Object visitLoopCasesWhile(LoopCasesWhile ast, Object o) {
         Frame frame = (Frame) o;
         int jumpAddr, loopAddr;
         jumpAddr = nextInstrAddr;
         emit(Machine.JUMPop, 0, Machine.CBr, 0);
         loopAddr = nextInstrAddr;
         ast.COM.visit(this, frame);
         patch(jumpAddr, nextInstrAddr);
         ast.EXP.visit(this, frame);
         emit(Machine.JUMPIFop, Machine.trueRep, Machine.CBr, loopAddr);
         return null;
    }

    /**
     * The method visits the Expression contained in the ast, and checks
     * if the type returned by the visit of the Expression is a boolean.
     * Then, the Command of the ast is visited.
     * @param ast
     * @param o
     * @return 
     */
    @Override
    public Object visitLoopCasesUntil(LoopCasesUntil ast, Object o) {
        Frame frame = (Frame) o;
         int jumpAddr, loopAddr;
         jumpAddr = nextInstrAddr;
         emit(Machine.JUMPop, 0, Machine.CBr, 0);
         loopAddr = nextInstrAddr;
         ast.COM.visit(this, frame);
         patch(jumpAddr, nextInstrAddr);
         ast.EXP.visit(this, frame);
         emit(Machine.JUMPIFop, Machine.falseRep, Machine.CBr, loopAddr);
         return null;
    }

    /**
     * The Command and DoLoop of the ast are visited.
     * @param ast
     * @param o
     * @return 
     */
    @Override
    public Object visitLoopCasesDo(LoopCasesDo ast, Object o) {
        Frame frame = (Frame) o;
         int  loopAddr;
         loopAddr = nextInstrAddr;
         ast.COM.visit(this, frame);
         ast.DO.visit(this, frame);
        if(ast.DO instanceof DoLoopWhile)
            emit(Machine.JUMPIFop, Machine.trueRep, Machine.CBr, loopAddr); 
        else
            emit(Machine.JUMPIFop, Machine.falseRep, Machine.CBr, loopAddr);
        return null;
    }
       /**
     * The method visits the Expression contained in the ast, and
     * checks if the returned type from the visit of the Expression
     * is a boolean.
     * @param ast
     * @param o
     * @return 
     */
    @Override
    public Object visitDoLoopUntil(DoLoopUntil ast, Object o) {
         Frame frame = (Frame) o;
         ast.EXP.visit(this, frame);
         return null;
    }

    /**
     * The method visits the Expression contained in the ast, and
     * checks if the returned type from the visit of the Expression
     * is a boolean.
     * @param ast
     * @param o
     * @return 
     */
    @Override
    public Object visitDoLoopWhile(DoLoopWhile ast, Object o) {
         Frame frame = (Frame) o;
         ast.EXP.visit(this, frame);
         return null;
    }
    
    /**
     * The method first gets the packageName from the defaultPackage or from the parameter.
     * Then it visits the Expression contained in the ast, and check if the returned type
     * is an integer. After that, the ConstDeclaration of the ast is visited, and it
     * returns the type of the Expression contained in the declaration. This type is
     * checked to see if its an integer. So, the ForLoop of the ast is visited, and
     * if it returns a type, it is checked to see if its a boolean. Finally, the scope
     * ; that was opened in the visitConstDeclaration, is closed.
     * @param ast
     * @param o
     * @return 
     */
    @Override
    public Object visitLoopCasesFOR(LoopCasesFOR ast, Object o) { 
         Frame frame = (Frame) o;
         int repeat, evalCond,exit, expSize;
         expSize = (Integer)ast.EXP2.visit(this, frame);
         ast.DECL.visit(this, new Frame(frame.level, frame.size + expSize));
         evalCond = nextInstrAddr;
         emit(Machine.JUMPop, 0, Machine.CBr, 0);
         repeat = nextInstrAddr;
         ast.FOR.visit(this, frame);
         emit(Machine.CALLop, frame.level, Machine.PBr, Machine.succDisplacement);
         patch(evalCond, nextInstrAddr);
         emit(Machine.LOADop,2, Machine.STr,-2);
         emit(Machine.CALLop, frame.level, Machine.PBr, Machine.geDisplacement);
         emit(Machine.JUMPIFop, Machine.trueRep, Machine.CBr, repeat);
         emit(Machine.POPop, 0, 0, 2);
         return null;
    }

    
    /**
     * The method visits the Command contained in the ast.
     * @param ast
     * @param o
     * @return 
     */
    @Override
    public Object visitForLoopDo(ForLoopDo ast, Object o) {//Todo
        ast.COM.visit(this, o);
        return null;
    }

    /**
     * The method visits the Expression contained in the ast, and
     * the returned type of this visit is assigned to the variable eType.
     * Then the Command of the ast is visited, and the variable eType is
     * returned, so it can be used from where the method is called.
     * @param ast
     * @param o
     * @return 
     */
    @Override
    public Object visitForLoopUntil(ForLoopUntil ast, Object o) {//Todo
         Frame frame = (Frame) o;
         int jumpAddr, loopAddr;
         jumpAddr = nextInstrAddr;
         emit(Machine.JUMPop, 0, Machine.CBr, 0);
         loopAddr = nextInstrAddr;
         ast.COM.visit(this, frame);
         patch(jumpAddr, nextInstrAddr);
         ast.EXP.visit(this, frame);
         emit(Machine.JUMPIFop, Machine.falseRep, Machine.CBr, loopAddr);
         return null;
    }

    /**
     * The method visits the Expression contained in the ast, and
     * the returned type of this visit is assigned to the variable eType.
     * Then the Command of the ast is visited, and the variable eType is
     * returned, so it can be used from where the method is called.
     * @param ast
     * @param o
     * @return 
     */
    @Override
    public Object visitForLoopWhile(ForLoopWhile ast, Object o) { //Todo
         Frame frame = (Frame) o;
         int jumpAddr, loopAddr;
         jumpAddr = nextInstrAddr;
         emit(Machine.JUMPop, 0, Machine.CBr, 0);
         loopAddr = nextInstrAddr;
         ast.COM.visit(this, frame);
         patch(jumpAddr, nextInstrAddr);
         ast.EXP.visit(this, frame);
         emit(Machine.JUMPIFop, Machine.trueRep, Machine.CBr, loopAddr);
         return null;
    }


  public Object visitWhileCommand(WhileCommand ast, Object o) {
    Frame frame = (Frame) o;
    int jumpAddr, loopAddr;

    jumpAddr = nextInstrAddr;
    emit(Machine.JUMPop, 0, Machine.CBr, 0);
    loopAddr = nextInstrAddr;
    ast.C.visit(this, frame);
    patch(jumpAddr, nextInstrAddr);
    ast.E.visit(this, frame);
    emit(Machine.JUMPIFop, Machine.trueRep, Machine.CBr, loopAddr);
    return null;
  }


  // Expressions
  public Object visitArrayExpression(ArrayExpression ast, Object o) {
    ast.type.visit(this, o);
    return ast.AA.visit(this, o);
  }

  public Object visitBinaryExpression(BinaryExpression ast, Object o) {
    Frame frame = (Frame) o;
    Integer valSize = (Integer) ast.type.visit(this, o);
    int valSize1 = ((Integer) ast.E1.visit(this, frame)).intValue();
    Frame frame1 = new Frame(frame, valSize1);
    int valSize2 = ((Integer) ast.E2.visit(this, frame1)).intValue();
    Frame frame2 = new Frame(frame.level, valSize1 + valSize2);
    ast.O.visit(this, frame2);
    return valSize;
  }

  public Object visitCallExpression(CallExpression ast, Object o) {
    Frame frame = (Frame) o;
    Integer valSize = (Integer) ast.type.visit(this, o);
    Integer argsSize = (Integer) ast.APS.visit(this, frame);
    ast.I.visit(this, new Frame(frame.level, argsSize));
    return valSize;
  }

  public Object visitCharacterExpression(CharacterExpression ast,
						Object o) {
    Frame frame = (Frame) o;
    Integer valSize = (Integer) ast.type.visit(this, o);
    emit(Machine.LOADLop, 0, 0, ast.CL.spelling.charAt(1));
    return valSize;
  }

  public Object visitEmptyExpression(EmptyExpression ast, Object o) {
    return new Integer(0);
  }

  public Object visitIfExpression(IfExpression ast, Object o) {
    Frame frame = (Frame) o;
    Integer valSize;
    int jumpifAddr, jumpAddr;

    ast.type.visit(this, o);
    ast.E1.visit(this, frame);
    jumpifAddr = nextInstrAddr;
    emit(Machine.JUMPIFop, Machine.falseRep, Machine.CBr, 0);
    valSize = (Integer) ast.E2.visit(this, frame);
    jumpAddr = nextInstrAddr;
    emit(Machine.JUMPop, 0, Machine.CBr, 0);
    patch(jumpifAddr, nextInstrAddr);
    valSize = (Integer) ast.E3.visit(this, frame);
    patch(jumpAddr, nextInstrAddr);
    return valSize;
  }

  public Object visitIntegerExpression(IntegerExpression ast, Object o) {
    Frame frame = (Frame) o;
    Integer valSize = (Integer) ast.type.visit(this, o);
    emit(Machine.LOADLop, 0, 0, Integer.parseInt(ast.IL.spelling));
    return valSize;
  }

  public Object visitLetExpression(LetExpression ast, Object o) {
    Frame frame = (Frame) o;
    ast.type.visit(this, o);
    int extraSize = ((Integer) ast.D.visit(this, frame)).intValue();
    Frame frame1 = new Frame(frame, extraSize);
    Integer valSize = (Integer) ast.E.visit(this, frame1);
    if (extraSize > 0)
      emit(Machine.POPop, valSize.intValue(), 0, extraSize);
    return valSize;
  }

  public Object visitRecordExpression(RecordExpression ast, Object o){
    ast.type.visit(this, o);
    return ast.RA.visit(this, o);
  }

  public Object visitUnaryExpression(UnaryExpression ast, Object o) {
    Frame frame = (Frame) o;
    Integer valSize = (Integer) ast.type.visit(this, o);
    ast.E.visit(this, frame);
    ast.O.visit(this, new Frame(frame.level, valSize.intValue()));
    return valSize;
  }

  public Object visitVnameExpression(VnameExpression ast, Object o) {
    Frame frame = (Frame) o;
    Integer valSize = (Integer) ast.type.visit(this, o);
    encodeFetch(ast.V, frame, valSize.intValue());
    return valSize;
  }


  // Declarations
  public Object visitBinaryOperatorDeclaration(BinaryOperatorDeclaration ast,
					       Object o){
    return new Integer(0);
  }

  public Object visitConstDeclaration(ConstDeclaration ast, Object o) {
    Frame frame = (Frame) o;
    int extraSize = 0;
 if (ast.E instanceof CharacterExpression) {
        CharacterLiteral CL = ((CharacterExpression) ast.E).CL;
        ast.entity = new KnownValue(Machine.characterSize,
                                 characterValuation(CL.spelling));
    } else if (ast.E instanceof IntegerExpression) {
        IntegerLiteral IL = ((IntegerExpression) ast.E).IL;
        ast.entity = new KnownValue(Machine.integerSize,
				 Integer.parseInt(IL.spelling));
    } else {
      int valSize = ((Integer) ast.E.visit(this, frame)).intValue();
      ast.entity = new UnknownValue(valSize, frame.level, frame.size);
      extraSize = valSize;
    }
    writeTableDetails(ast);
    return new Integer(extraSize);
  }
  
  //Recursive procedures and functions
  
  
    @Override
    public Object visitCompoundDeclarationRecursive(CompoundDeclarationRecursive ast, Object o) {
        return  ast.PF.visit(this,o);
    }
  
    @Override
    public Object visitProcProcFunc(ProcProcFunc ast, Object o) {
         Frame frame = (Frame) o;
        int jumpAddr = nextInstrAddr;
        int argsSize = 0;
        /*
        emit(Machine.JUMPop, 0, Machine.CBr, 0);
        ast.entity = new KnownRoutine (Machine.closureSize, frame.level,
                                 nextInstrAddr);
        writeTableDetails(ast);
        */
        if (frame.level == Machine.maxRoutineLevel)
          reporter.reportRestriction("can't nest routines so deeply");
        else {
          Frame frame1 = new Frame(frame.level + 1, 0);
          argsSize = ((Integer) ast.FPS.visit(this, frame1)).intValue();
          Frame frame2 = new Frame(frame.level + 1, Machine.linkDataSize);
          ast.COM.visit(this, frame2);
        }
        emit(Machine.RETURNop, 0, 0, argsSize);
        //patch(jumpAddr, nextInstrAddr);
        return new Integer(0);
    }

    @Override
    public Object visitFuncProcFunc(FuncProcFunc ast, Object o) {
         Frame frame = (Frame) o;
        int jumpAddr = nextInstrAddr;
        int argsSize = 0, valSize = 0;
        /*
        emit(Machine.JUMPop, 0, Machine.CBr, 0);
        ast.entity = new KnownRoutine(Machine.closureSize, frame.level, nextInstrAddr);
        writeTableDetails(ast);
                */
        if (frame.level == Machine.maxRoutineLevel)
          reporter.reportRestriction("can't nest routines more than 7 deep");
        else {
          Frame frame1 = new Frame(frame.level + 1, 0);
          argsSize = ((Integer) ast.FPS.visit(this, frame1)).intValue();
          Frame frame2 = new Frame(frame.level + 1, Machine.linkDataSize);
          valSize = ((Integer) ast.EXP.visit(this, frame2)).intValue();
        }
        emit(Machine.RETURNop, valSize, 0, argsSize);
       // patch(jumpAddr, nextInstrAddr);
        return new Integer(0);
    }

    @Override
    public Object visitProcFuncs(ProcFuncs ast, Object o) {
        Frame frame = (Frame) o;
        int jumpAddress1,jumpAddress2;
        int temp = 0;
        jumpAddress2 = nextInstrAddr;
        if(ast.PF2 != null){
        emit(Machine.JUMPop, 0, Machine.CBr, 0);
        ast.PF2.entity = new KnownRoutine(Machine.closureSize, frame.level, nextInstrAddr);
        writeTableDetails(ast.PF2);
        }
        else{
        emit(Machine.JUMPop, 0, Machine.CBr, 0);
        ast.PF1.entity = new KnownRoutine(Machine.closureSize, frame.level, nextInstrAddr);
        writeTableDetails(ast.PF1);
        }
        int val = (Integer)ast.PF1.visit(this,o);
        Frame frame1 = new Frame (frame.level, frame.size + val);
        if(ast.PF2 != null){
            jumpAddress1 = nextInstrAddr;
            emit(Machine.JUMPop, 0, Machine.CBr, 0);
            ast.PF1.entity = new KnownRoutine(Machine.closureSize, frame.level, nextInstrAddr);
            writeTableDetails(ast.PF1);
              val += (Integer)ast.PF2.visit(this, frame1);
              patch(jumpAddress1, nextInstrAddr);
              patch(jumpAddress2,jumpAddress1);
              KnownRoutine routine = (KnownRoutine)ast.PF1.entity;
              ast.PF1.entity = ast.PF2.entity;
              ast.PF2.entity = routine;
              patchRecursive(nextInstrAddr,jumpAddress1+1, jumpAddress2 + 1);
            }
        else{
            ast.PF1.entity = ast.PF2.entity;
        }
        return val;
    }
  
  //Procedures and functions

  public Object visitFuncDeclaration(FuncDeclaration ast, Object o) {
    Frame frame = (Frame) o;
    int jumpAddr = nextInstrAddr;
    int argsSize = 0, valSize = 0;

    emit(Machine.JUMPop, 0, Machine.CBr, 0);
    ast.entity = new KnownRoutine(Machine.closureSize, frame.level, nextInstrAddr);
    writeTableDetails(ast);
    if (frame.level == Machine.maxRoutineLevel)
      reporter.reportRestriction("can't nest routines more than 7 deep");
    else {
      Frame frame1 = new Frame(frame.level + 1, 0);
      argsSize = ((Integer) ast.FPS.visit(this, frame1)).intValue();
      Frame frame2 = new Frame(frame.level + 1, Machine.linkDataSize);
      valSize = ((Integer) ast.E.visit(this, frame2)).intValue();
    }
    emit(Machine.RETURNop, valSize, 0, argsSize);
    patch(jumpAddr, nextInstrAddr);
    return new Integer(0);
  }

  public Object visitProcDeclaration(ProcDeclaration ast, Object o) {
    Frame frame = (Frame) o;
    int jumpAddr = nextInstrAddr;
    int argsSize = 0;

    emit(Machine.JUMPop, 0, Machine.CBr, 0);
    ast.entity = new KnownRoutine (Machine.closureSize, frame.level,
                                nextInstrAddr);
    writeTableDetails(ast);
    if (frame.level == Machine.maxRoutineLevel)
      reporter.reportRestriction("can't nest routines so deeply");
    else {
      Frame frame1 = new Frame(frame.level + 1, 0);
      argsSize = ((Integer) ast.FPS.visit(this, frame1)).intValue();
      Frame frame2 = new Frame(frame.level + 1, Machine.linkDataSize);
      ast.C.visit(this, frame2);
    }
    emit(Machine.RETURNop, 0, 0, argsSize);
    patch(jumpAddr, nextInstrAddr);
    return new Integer(0);
  }

  public Object visitSequentialDeclaration(SequentialDeclaration ast, Object o) {
    Frame frame = (Frame) o;
    int extraSize1, extraSize2;

    extraSize1 = (Integer)ast.D1.visit(this, frame);
    Frame frame1 = new Frame (frame, extraSize1);
    extraSize2 = ((Integer) ast.D2.visit(this, frame1)).intValue();
    return new Integer(extraSize1 + extraSize2);
  }

  public Object visitTypeDeclaration(TypeDeclaration ast, Object o) {
    // just to ensure the type's representation is decided
    ast.T.visit(this, o);
    return new Integer(0);
  }

  public Object visitUnaryOperatorDeclaration(UnaryOperatorDeclaration ast,
					      Object o) {
    return new Integer(0);
  }

  public Object visitVarDeclaration(VarDeclaration ast, Object o) {
    Frame frame = (Frame) o;
    int extraSize;
    extraSize = (Integer) ast.V.visit(this, o);
    //emit(Machine.PUSHop, 0, 0, extraSize);
    ast.entity = new KnownAddress(Machine.addressSize, frame.level, frame.size);
    writeTableDetails(ast);
    return extraSize;
  }


  // Array Aggregates
  public Object visitMultipleArrayAggregate(MultipleArrayAggregate ast,
					    Object o) {
    Frame frame = (Frame) o;
    int elemSize = ((Integer) ast.E.visit(this, frame)).intValue();
    Frame frame1 = new Frame(frame, elemSize);
    int arraySize = ((Integer) ast.AA.visit(this, frame1)).intValue();
    return new Integer(elemSize + arraySize);
  }

  public Object visitSingleArrayAggregate(SingleArrayAggregate ast, Object o) {
    return ast.E.visit(this, o);
  }


  // Record Aggregates
  public Object visitMultipleRecordAggregate(MultipleRecordAggregate ast,
					     Object o) {
    Frame frame = (Frame) o;
    int fieldSize = ((Integer) ast.E.visit(this, frame)).intValue();
    Frame frame1 = new Frame (frame, fieldSize);
    int recordSize = ((Integer) ast.RA.visit(this, frame1)).intValue();
    return new Integer(fieldSize + recordSize);
  }

  public Object visitSingleRecordAggregate(SingleRecordAggregate ast,
					   Object o) {
    return ast.E.visit(this, o);
  }


  // Formal Parameters
  public Object visitConstFormalParameter(ConstFormalParameter ast, Object o) {
    Frame frame = (Frame) o;
    int valSize = ((Integer) ast.T.visit(this, o)).intValue();
    ast.entity = new UnknownValue (valSize, frame.level, -frame.size - valSize);
    writeTableDetails(ast);
    return new Integer(valSize);
  }

  public Object visitFuncFormalParameter(FuncFormalParameter ast, Object o) {
    Frame frame = (Frame) o;
    int argsSize = Machine.closureSize;
    ast.entity = new UnknownRoutine (Machine.closureSize, frame.level,
				  -frame.size - argsSize);
    writeTableDetails(ast);
    return new Integer(argsSize);
  }

  public Object visitProcFormalParameter(ProcFormalParameter ast, Object o) {
    Frame frame = (Frame) o;
    int argsSize = Machine.closureSize;
    ast.entity = new UnknownRoutine (Machine.closureSize, frame.level,
				  -frame.size - argsSize);
    writeTableDetails(ast);
    return new Integer(argsSize);
  }

  public Object visitVarFormalParameter(VarFormalParameter ast, Object o) {
    Frame frame = (Frame) o;
    ast.T.visit(this, o);
    ast.entity = new UnknownAddress (Machine.addressSize, frame.level,
				  -frame.size - Machine.addressSize);
    writeTableDetails(ast);
    return new Integer(Machine.addressSize);
  }


  public Object visitEmptyFormalParameterSequence(
	 EmptyFormalParameterSequence ast, Object o) {
    return new Integer(0);
  }

  public Object visitMultipleFormalParameterSequence(
 	 MultipleFormalParameterSequence ast, Object o) {
    Frame frame = (Frame) o;
    int argsSize1 = ((Integer) ast.FPS.visit(this, frame)).intValue();
    Frame frame1 = new Frame(frame, argsSize1);
    int argsSize2 = ((Integer) ast.FP.visit(this, frame1)).intValue();
    return new Integer(argsSize1 + argsSize2);
  }

  public Object visitSingleFormalParameterSequence(
	 SingleFormalParameterSequence ast, Object o) {
    return ast.FP.visit (this, o);
  }


  // Actual Parameters
  public Object visitConstActualParameter(ConstActualParameter ast, Object o) {
    return ast.E.visit (this, o);
  }

  public Object visitFuncActualParameter(FuncActualParameter ast, Object o) {
    Frame frame = (Frame) o;
    if (ast.I.decl.entity instanceof KnownRoutine) {
      ObjectAddress address = ((KnownRoutine) ast.I.decl.entity).address;
      // static link, code address
      emit(Machine.LOADAop, 0, displayRegister(frame.level, address.level), 0);
      emit(Machine.LOADAop, 0, Machine.CBr, address.displacement);
    } else if (ast.I.decl.entity instanceof UnknownRoutine) {
      ObjectAddress address = ((UnknownRoutine) ast.I.decl.entity).address;
      emit(Machine.LOADop, Machine.closureSize, displayRegister(frame.level,
           address.level), address.displacement);
    } else if (ast.I.decl.entity instanceof PrimitiveRoutine) {
      int displacement = ((PrimitiveRoutine) ast.I.decl.entity).displacement;
      // static link, code address
      emit(Machine.LOADAop, 0, Machine.SBr, 0);
      emit(Machine.LOADAop, 0, Machine.PBr, displacement);
    }
    return new Integer(Machine.closureSize);
  }

  public Object visitProcActualParameter(ProcActualParameter ast, Object o) {
    Frame frame = (Frame) o;
    if (ast.I.decl.entity instanceof KnownRoutine) {
      ObjectAddress address = ((KnownRoutine) ast.I.decl.entity).address;
      // static link, code address
      emit(Machine.LOADAop, 0, displayRegister(frame.level, address.level), 0);
      emit(Machine.LOADAop, 0, Machine.CBr, address.displacement);
    } else if (ast.I.decl.entity instanceof UnknownRoutine) {
      ObjectAddress address = ((UnknownRoutine) ast.I.decl.entity).address;
      emit(Machine.LOADop, Machine.closureSize, displayRegister(frame.level,
           address.level), address.displacement);
    } else if (ast.I.decl.entity instanceof PrimitiveRoutine) {
      int displacement = ((PrimitiveRoutine) ast.I.decl.entity).displacement;
      // static link, code address
      emit(Machine.LOADAop, 0, Machine.SBr, 0);
      emit(Machine.LOADAop, 0, Machine.PBr, displacement);
    }
    return new Integer(Machine.closureSize);
  }

  public Object visitVarActualParameter(VarActualParameter ast, Object o) {
    encodeFetchAddress(ast.V, (Frame) o);
    return new Integer(Machine.addressSize);
  }


  public Object visitEmptyActualParameterSequence(
	 EmptyActualParameterSequence ast, Object o) {
    return new Integer(0);
  }

  public Object visitMultipleActualParameterSequence(
	 MultipleActualParameterSequence ast, Object o) {
    Frame frame = (Frame) o;
    int argsSize1 = ((Integer) ast.AP.visit(this, frame)).intValue();
    Frame frame1 = new Frame (frame, argsSize1);
    int argsSize2 = ((Integer) ast.APS.visit(this, frame1)).intValue();
    return new Integer(argsSize1 + argsSize2);
  }

  public Object visitSingleActualParameterSequence(
	 SingleActualParameterSequence ast, Object o) {
    return ast.AP.visit (this, o);
  }


  // Type Denoters
  public Object visitAnyTypeDenoter(AnyTypeDenoter ast, Object o) {
    return new Integer(0);
  }

  public Object visitArrayTypeDenoter(ArrayTypeDenoter ast, Object o) {
    int typeSize;
    if (ast.entity == null) {
      int elemSize = ((Integer) ast.T.visit(this, o)).intValue();
      typeSize = Integer.parseInt(ast.IL.spelling) * elemSize;
      ast.entity = new TypeRepresentation(typeSize);
      writeTableDetails(ast);
    } else
      typeSize = ast.entity.size;
    return new Integer(typeSize);
  }

  public Object visitBoolTypeDenoter(BoolTypeDenoter ast, Object o) {
    if (ast.entity == null) {
      ast.entity = new TypeRepresentation(Machine.booleanSize);
      writeTableDetails(ast);
    }
    return new Integer(Machine.booleanSize);
  }

  public Object visitCharTypeDenoter(CharTypeDenoter ast, Object o) {
    if (ast.entity == null) {
      ast.entity = new TypeRepresentation(Machine.characterSize);
      writeTableDetails(ast);
    }
    return new Integer(Machine.characterSize);
  }

  public Object visitErrorTypeDenoter(ErrorTypeDenoter ast, Object o) {
    return new Integer(0);
  }

  public Object visitSimpleTypeDenoter(SimpleTypeDenoter ast,
					   Object o) {
    return new Integer(0);
  }

  public Object visitIntTypeDenoter(IntTypeDenoter ast, Object o) {
    if (ast.entity == null) {
      ast.entity = new TypeRepresentation(Machine.integerSize);
      writeTableDetails(ast);
    }
    return new Integer(Machine.integerSize);
  }

  public Object visitRecordTypeDenoter(RecordTypeDenoter ast, Object o) {
    int typeSize;
    if (ast.entity == null) {
      typeSize = ((Integer) ast.FT.visit(this, new Integer(0))).intValue();
      ast.entity = new TypeRepresentation(typeSize);
      writeTableDetails(ast);
    } else
      typeSize = ast.entity.size;
    return new Integer(typeSize);
  }


  public Object visitMultipleFieldTypeDenoter(MultipleFieldTypeDenoter ast,
					      Object o) {
    int offset = ((Integer) o).intValue();
    int fieldSize;

    if (ast.entity == null) {
      fieldSize = ((Integer) ast.T.visit(this, o)).intValue();
      ast.entity = new Field (fieldSize, offset);
      writeTableDetails(ast);
    } else
      fieldSize = ast.entity.size;

    Integer offset1 = new Integer(offset + fieldSize);
    int recSize = ((Integer) ast.FT.visit(this, offset1)).intValue();
    return new Integer(fieldSize + recSize);
  }

  public Object visitSingleFieldTypeDenoter(SingleFieldTypeDenoter ast,
					    Object o) {
    int offset = ((Integer) o).intValue();
    int fieldSize;

    if (ast.entity == null) {
      fieldSize = ((Integer) ast.T.visit(this, o)).intValue();
      ast.entity = new Field (fieldSize, offset);
      writeTableDetails(ast);
    } else
      fieldSize = ast.entity.size;

    return new Integer(fieldSize);
  }
  
  //Cases
        @Override
    public Object visitCaseRangeCase(CaseRangeCase ast, Object o) {
        return null;
    }
    @Override
    public Object visitChooseCommand(ChooseCommand ast, Object o) {
        ast.EXP.visit(this, o);
        ast.COM.visit(this,o);
        emit(Machine.POPop, 0, 0, 1);

        return null;
    }

  @Override
    public Object visitCases(Cases ast, Object o) {
        int jumpOutside = nextInstrAddr;
        ChooseCode cc = new ChooseCode((Frame)o, jumpOutside);
        ast.CASE1.visit(this, cc);
        if(ast.CASE2 != null){
            ast.CASE2.visit(this, o);
        }
        patchJumps(jumpOutside, nextInstrAddr);
        return null;
    }

    @Override
    public Object visitElseCase(ElseCase ast, Object o) {
        return ast.COM.visit(this, o);
    }

    @Override
    public Object visitSequentialCase(SequentialCase ast, Object o) {
        ast.C1.visit(this,o);
        ast.C2.visit(this, o);
        return null;
    }

    @Override
    public Object visitCaseWhen(CaseWhen ast, Object choosecode) { //TODO Aqui es donde estan los comandos
        Frame o = ((ChooseCode) choosecode).frame;
        int jmpOutside =((ChooseCode) choosecode).address;
        ast.CASELIT.visit(this, o);
        CharacterLiteral character1,character2;
        IntegerLiteral integer1,integer2;
        CaseRangeCase cases = ((CaseRangeCase)ast.CASELIT.CASERANGE);
        if(cases.CASELIT2 != null ){
                if( cases.CASELIT instanceof CaseLiteralCHAR){
                    
                    character1  =   (CharacterLiteral) ( ( (CaseLiteralCHAR) cases.CASELIT).CHARLIT);
                    character2  =   (CharacterLiteral) ( ( (CaseLiteralCHAR) cases.CASELIT2).CHARLIT);
                    emit(Machine.LOADLop, 0, 0, (int)character1.spelling.charAt(1));
                            Frame frame = (Frame) o;
                            int jumpifAddr,jumpifAddr2;
                            jumpifAddr = nextInstrAddr;
                            emit(Machine.CASEop, 0, Machine.CBr, 0);
                            emit(Machine.POPop, 0, 0, 1);
                            emit(Machine.LOADLop, 0, 0, (int)character1.spelling.charAt(1));
                            jumpifAddr2 = nextInstrAddr;
                            emit(Machine.CASEop, 0, Machine.CBr, 0);
                            emit(Machine.POPop, 0, 0, 1);
                            ast.COM.visit(this, frame);
                            emit(Machine.JUMPop, 0, Machine.CBr, jmpOutside);
                            patch(jumpifAddr, nextInstrAddr);
                            patch(jumpifAddr2, nextInstrAddr);
                            emit(Machine.POPop, 0, 0, 2);
                    character2  =   (CharacterLiteral) ( ( (CaseLiteralCHAR) cases.CASELIT2).CHARLIT);
                }
                else if(cases.CASELIT instanceof CaseLiteralINT){
                           integer1 = (IntegerLiteral) ( ( (CaseLiteralINT) cases.CASELIT).INTLIT);
                           integer2 = (IntegerLiteral) ( ( (CaseLiteralINT) cases.CASELIT2).INTLIT);
                }
        }
        else{
                if( cases.CASELIT instanceof CaseLiteralCHAR){
                    
                    character1  =   (CharacterLiteral) ( ( (CaseLiteralCHAR) cases.CASELIT).CHARLIT);
                    emit(Machine.LOADLop, 0, 0, (int)character1.spelling.charAt(1));
                            Frame frame = (Frame) o;
                            int jumpifAddr;
                            jumpifAddr = nextInstrAddr;
                            emit(Machine.CASEop, 0, Machine.CBr, 0);
                            emit(Machine.POPop, 0, 0, 1);
                            ast.COM.visit(this, frame);
                            emit(Machine.JUMPop, 0, Machine.CBr, jmpOutside);
                            patch(jumpifAddr, nextInstrAddr);
                            emit(Machine.POPop, 0, 0, 1);
                }
                else if(cases.CASELIT instanceof CaseLiteralINT){
                           integer1 = (IntegerLiteral) ( ( (CaseLiteralINT) cases.CASELIT).INTLIT);
                           emit(Machine.LOADLop, 0, 0, Integer.parseInt(integer1.spelling));
                            Frame frame = (Frame) o;
                            int jumpifAddr;
                            jumpifAddr = nextInstrAddr;
                            emit(Machine.CASEop, 0, Machine.CBr, 0);
                            emit(Machine.POPop, 0, 0, 1);
                            ast.COM.visit(this, frame);
                            emit(Machine.JUMPop, 0, Machine.CBr, jmpOutside);
                            patch(jumpifAddr, nextInstrAddr);
                            emit(Machine.POPop, 0, 0, 1);
                }
        }
        return null;
    }

    @Override
    public Object visitCaseLiterals(CaseLiterals ast, Object o) {
        return ast.CASERANGE.visit(this, o);
    }
   
    @Override
    public Object visitSequentialCaseRange(SequentialCaseRange ast, Object o) {
        return ast.C1.visit(this, o);
    }

    @Override
    public Object visitCaseLiteralCHAR(CaseLiteralCHAR ast, Object o) {
        return ast.CHARLIT.visit(this, o);
    }

    @Override
    public Object visitCaseLiteralINT(CaseLiteralINT ast, Object o) {
        return ast.INTLIT.visit(this, o);
    }


  // Literals, Identifiers and Operators
  public Object visitCharacterLiteral(CharacterLiteral ast, Object o) {
    return null;
  }

  public Object visitIdentifier(Identifier ast, Object o) {
    Frame frame = (Frame) o;
    if(ast.decl != null){
    if (ast.decl.entity instanceof KnownRoutine) {
      ObjectAddress address = ((KnownRoutine) ast.decl.entity).address;
      emit(Machine.CALLop, displayRegister(frame.level, address.level),
	   Machine.CBr, address.displacement);
    } else if (ast.decl.entity instanceof UnknownRoutine) {
      ObjectAddress address = ((UnknownRoutine) ast.decl.entity).address;
      emit(Machine.LOADop, Machine.closureSize, displayRegister(frame.level,
           address.level), address.displacement);
      emit(Machine.CALLIop, 0, 0, 0);
    } else if (ast.decl.entity instanceof PrimitiveRoutine) {
      int displacement = ((PrimitiveRoutine) ast.decl.entity).displacement;
      if (displacement != Machine.idDisplacement)
        emit(Machine.CALLop, Machine.SBr, Machine.PBr, displacement);
    } else if (ast.decl.entity instanceof EqualityRoutine) { // "=" or "\="
      int displacement = ((EqualityRoutine) ast.decl.entity).displacement;
      emit(Machine.LOADLop, 0, 0, frame.size / 2);
      emit(Machine.CALLop, Machine.SBr, Machine.PBr, displacement);
    }
    }
    return null;
  }

  public Object visitIntegerLiteral(IntegerLiteral ast, Object o) {
    return null;
  }

  public Object visitOperator(Operator ast, Object o) {
    Frame frame = (Frame) o;
    if (ast.decl.entity instanceof KnownRoutine) {
      ObjectAddress address = ((KnownRoutine) ast.decl.entity).address;
      emit(Machine.CALLop, displayRegister (frame.level, address.level),
	   Machine.CBr, address.displacement);
    } else if (ast.decl.entity instanceof UnknownRoutine) {
      ObjectAddress address = ((UnknownRoutine) ast.decl.entity).address;
      emit(Machine.LOADop, Machine.closureSize, displayRegister(frame.level,
           address.level), address.displacement);
      emit(Machine.CALLIop, 0, 0, 0);
    } else if (ast.decl.entity instanceof PrimitiveRoutine) {
      int displacement = ((PrimitiveRoutine) ast.decl.entity).displacement;
      if (displacement != Machine.idDisplacement)
        emit(Machine.CALLop, Machine.SBr, Machine.PBr, displacement);
    } else if (ast.decl.entity instanceof EqualityRoutine) { // "=" or "\="
      int displacement = ((EqualityRoutine) ast.decl.entity).displacement;
      emit(Machine.LOADLop, 0, 0, frame.size / 2);
      emit(Machine.CALLop, Machine.SBr, Machine.PBr, displacement);
    }
    return null;
  }


  // Value-or-variable names
  public Object visitDotVname(DotVname ast, Object o) {
    Frame frame = (Frame) o;
    RuntimeEntity baseObject = (RuntimeEntity) ast.V.visit(this, frame);
    ast.offset = ast.V.offset + ((Field) ast.I.decl.entity).fieldOffset;
                   // I.decl points to the appropriate record field
    ast.indexed = ast.V.indexed;
    return baseObject;
  }

  public Object visitSimpleVname(SimpleVname ast, Object o) {
    ast.offset = 0;
    ast.indexed = false;
    return ast.I.decl.entity;
  }

  public Object visitSubscriptVname(SubscriptVname ast, Object o) {
    Frame frame = (Frame) o;
    RuntimeEntity baseObject;
    int elemSize, indexSize;

    baseObject = (RuntimeEntity) ast.V.visit(this, frame);
    ast.offset = ast.V.offset;
    ast.indexed = ast.V.indexed;
    elemSize = ((Integer) ast.type.visit(this, o)).intValue();
    if (ast.E instanceof IntegerExpression) {
      IntegerLiteral IL = ((IntegerExpression) ast.E).IL;
      ast.offset = ast.offset + Integer.parseInt(IL.spelling) * elemSize;
    } else {
      // v-name is indexed by a proper expression, not a literal
      if (ast.indexed)
        frame.size = frame.size + Machine.integerSize;
      indexSize = ((Integer) ast.E.visit(this, frame)).intValue();
      if (elemSize != 1) {
        emit(Machine.LOADLop, 0, 0, elemSize);
        emit(Machine.CALLop, Machine.SBr, Machine.PBr,
             Machine.multDisplacement);
      }
      if (ast.indexed)
        emit(Machine.CALLop, Machine.SBr, Machine.PBr, Machine.addDisplacement);
      else
        ast.indexed = true;
    }
    return baseObject;
  }


  // Programs
  public Object visitProgram(Program ast, Object o) {
      if(ast.P != null)
          ast.P.visit(this, o);
    return ast.C.visit(this, new Frame(0,0));
  }

  public Encoder (ErrorReporter reporter) {
    this.reporter = reporter;
    nextInstrAddr = Machine.CB;
    elaborateStdEnvironment();
  }

  private ErrorReporter reporter;

  // Generates code to run a program.
  // showingTable is true iff entity description details
  // are to be displayed.
  public final void encodeRun (Program theAST, boolean showingTable) {
    tableDetailsReqd = showingTable;
    //startCodeGeneration();
    theAST.visit(this, new Frame (0, 0));
    emit(Machine.HALTop, 0, 0, 0);
  }

  // Decides run-time representation of a standard constant.
  private final void elaborateStdConst (Declaration constDeclaration,
					int value) {

    if (constDeclaration instanceof ConstDeclaration) {
      ConstDeclaration decl = (ConstDeclaration) constDeclaration;
      int typeSize = ((Integer) decl.E.type.visit(this,null)).intValue();
      decl.entity = new KnownValue(typeSize, value);
      writeTableDetails(constDeclaration);
    }
  }

  // Decides run-time representation of a standard routine.
  private final void elaborateStdPrimRoutine (Declaration routineDeclaration,
                                          int routineOffset) {
    routineDeclaration.entity = new PrimitiveRoutine (Machine.closureSize, routineOffset);
    writeTableDetails(routineDeclaration);
  }

  private final void elaborateStdEqRoutine (Declaration routineDeclaration,
                                          int routineOffset) {
    routineDeclaration.entity = new EqualityRoutine (Machine.closureSize, routineOffset);
    writeTableDetails(routineDeclaration);
  }

  private final void elaborateStdRoutine (Declaration routineDeclaration,
                                          int routineOffset) {
    routineDeclaration.entity = new KnownRoutine (Machine.closureSize, 0, routineOffset);
    writeTableDetails(routineDeclaration);
  }

  private final void elaborateStdEnvironment() {
    tableDetailsReqd = false;
    elaborateStdConst(StdEnvironment.falseDecl, Machine.falseRep);
    elaborateStdConst(StdEnvironment.trueDecl, Machine.trueRep);
    elaborateStdPrimRoutine(StdEnvironment.notDecl, Machine.notDisplacement);
    elaborateStdPrimRoutine(StdEnvironment.andDecl, Machine.andDisplacement);
    elaborateStdPrimRoutine(StdEnvironment.orDecl, Machine.orDisplacement);
    elaborateStdConst(StdEnvironment.maxintDecl, Machine.maxintRep);
    elaborateStdPrimRoutine(StdEnvironment.addDecl, Machine.addDisplacement);
    elaborateStdPrimRoutine(StdEnvironment.subtractDecl, Machine.subDisplacement);
    elaborateStdPrimRoutine(StdEnvironment.multiplyDecl, Machine.multDisplacement);
    elaborateStdPrimRoutine(StdEnvironment.divideDecl, Machine.divDisplacement);
    elaborateStdPrimRoutine(StdEnvironment.moduloDecl, Machine.modDisplacement);
    elaborateStdPrimRoutine(StdEnvironment.lessDecl, Machine.ltDisplacement);
    elaborateStdPrimRoutine(StdEnvironment.notgreaterDecl, Machine.leDisplacement);
    elaborateStdPrimRoutine(StdEnvironment.greaterDecl, Machine.gtDisplacement);
    elaborateStdPrimRoutine(StdEnvironment.notlessDecl, Machine.geDisplacement);
    elaborateStdPrimRoutine(StdEnvironment.chrDecl, Machine.idDisplacement);
    elaborateStdPrimRoutine(StdEnvironment.ordDecl, Machine.idDisplacement);
    elaborateStdPrimRoutine(StdEnvironment.eolDecl, Machine.eolDisplacement);
    elaborateStdPrimRoutine(StdEnvironment.eofDecl, Machine.eofDisplacement);
    elaborateStdPrimRoutine(StdEnvironment.getDecl, Machine.getDisplacement);
    elaborateStdPrimRoutine(StdEnvironment.putDecl, Machine.putDisplacement);
    elaborateStdPrimRoutine(StdEnvironment.getintDecl, Machine.getintDisplacement);
    elaborateStdPrimRoutine(StdEnvironment.putintDecl, Machine.putintDisplacement);
    elaborateStdPrimRoutine(StdEnvironment.geteolDecl, Machine.geteolDisplacement);
    elaborateStdPrimRoutine(StdEnvironment.puteolDecl, Machine.puteolDisplacement);
    elaborateStdEqRoutine(StdEnvironment.equalDecl, Machine.eqDisplacement);
    elaborateStdEqRoutine(StdEnvironment.unequalDecl, Machine.neDisplacement);
  }

  // Saves the object program in the named file.

  public void saveObjectProgram(String objectName) {
    FileOutputStream objectFile = null;
    DataOutputStream objectStream = null;

    int addr;

    try {
      objectFile = new FileOutputStream (objectName);
      objectStream = new DataOutputStream (objectFile);

      addr = Machine.CB;
      for (addr = Machine.CB; addr < nextInstrAddr; addr++)
        Machine.code[addr].write(objectStream);
      objectFile.close();
    } catch (FileNotFoundException s) {
      System.err.println ("Error opening object file: " + s);
    } catch (IOException s) {
      System.err.println ("Error writing object file: " + s);
    }
  }

  boolean tableDetailsReqd;

  public static void writeTableDetails(AST ast) {
  }

  // OBJECT CODE

  // Implementation notes:
  // Object code is generated directly into the TAM Code Store, starting at CB.
  // The address of the next instruction is held in nextInstrAddr.

  private int nextInstrAddr;

  // Appends an instruction, with the given fields, to the object code.
  private void emit (int op, int n, int r, int d) {
    Instruction nextInstr = new Instruction();
    if (n > 255) {
        reporter.reportRestriction("length of operand can't exceed 255 words");
        n = 255; // to allow code generation to continue
    }
    nextInstr.op = op;
    nextInstr.n = n;
    nextInstr.r = r;
    nextInstr.d = d;
    if (nextInstrAddr == Machine.PB)
      reporter.reportRestriction("too many instructions for code segment");
    else {
        Machine.code[nextInstrAddr] = nextInstr;
        nextInstrAddr = nextInstrAddr + 1;
    }
  }

  // Patches the d-field of the instruction at address addr.
  private void patch (int addr, int d) {
    Machine.code[addr].d = d;
  }
  
  private void patchJumps (int d1, int d2) {
      int i = 0;
    while(i < nextInstrAddr){
        if(Machine.code[i].d == d1 && Machine.code[i].op == Machine.JUMPop){
            Machine.code[i].d = d2;
        }
        i++;
    }
  }
  
  private void patchRecursive(int addr,int d1, int d2){
  int i = 0;
    while(i < addr){
        if(Machine.code[i].d == d1 && Machine.code[i].op == Machine.CALLop){
            Machine.code[i].d = d2;
        }
        else if(Machine.code[i].d == d2 && Machine.code[i].op == Machine.CALLop){
            Machine.code[i].d = d1;
        }
        i++;
    }
    }
  
  // DATA REPRESENTATION

  public int characterValuation (String spelling) {
  // Returns the machine representation of the given character literal.
    return spelling.charAt(1);
      // since the character literal is of the form 'x'}
  }

  // REGISTERS

  // Returns the register number appropriate for object code at currentLevel
  // to address a data object at objectLevel.
  private int displayRegister (int currentLevel, int objectLevel) {
    if (objectLevel == 0)
      return Machine.SBr;
    else if (currentLevel - objectLevel <= 6)
      return Machine.LBr + currentLevel - objectLevel; // LBr|L1r|...|L6r
    else {
      reporter.reportRestriction("can't access data more than 6 levels out");
      return Machine.L6r;  // to allow code generation to continue
    }
  }

  // Generates code to fetch the value of a named constant or variable
  // and push it on to the stack.
  // currentLevel is the routine level where the vname occurs.
  // frameSize is the anticipated size of the local stack frame when
  // the constant or variable is fetched at run-time.
  // valSize is the size of the constant or variable's value.

  private void encodeStore(VName V, Frame frame, int valSize) {

    RuntimeEntity baseObject = (RuntimeEntity) V.visit(this, frame);
    // If indexed = true, code will have been generated to load an index value.
    if (valSize > 255) {
      reporter.reportRestriction("can't store values larger than 255 words");
      valSize = 255; // to allow code generation to continue
    }
    if (baseObject instanceof KnownAddress) {
      ObjectAddress address = ((KnownAddress) baseObject).address;
      if (V.indexed) {
        emit(Machine.LOADAop, 0, displayRegister(frame.level, address.level),
             address.displacement + V.offset);
        emit(Machine.CALLop, Machine.SBr, Machine.PBr, Machine.addDisplacement);
        emit(Machine.STOREIop, valSize, 0, 0);
      } else {
        emit(Machine.STOREop, valSize, displayRegister(frame.level,
	     address.level), address.displacement + V.offset);
      }
    } else if (baseObject instanceof UnknownAddress) {
      ObjectAddress address = ((UnknownAddress) baseObject).address;
      emit(Machine.LOADop, Machine.addressSize, displayRegister(frame.level,
           address.level), address.displacement);
      if (V.indexed)
        emit(Machine.CALLop, Machine.SBr, Machine.PBr, Machine.addDisplacement);
      if (V.offset != 0) {
        emit(Machine.LOADLop, 0, 0, V.offset);
        emit(Machine.CALLop, Machine.SBr, Machine.PBr, Machine.addDisplacement);
      }
      emit(Machine.STOREIop, valSize, 0, 0);
    }
  }

  // Generates code to fetch the value of a named constant or variable
  // and push it on to the stack.
  // currentLevel is the routine level where the vname occurs.
  // frameSize is the anticipated size of the local stack frame when
  // the constant or variable is fetched at run-time.
  // valSize is the size of the constant or variable's value.

  private void encodeFetch(VName V, Frame frame, int valSize) {

    RuntimeEntity baseObject = (RuntimeEntity) V.visit(this, frame);
    // If indexed = true, code will have been generated to load an index value.
    if (valSize > 255) {
      reporter.reportRestriction("can't load values larger than 255 words");
      valSize = 255; // to allow code generation to continue
    }
    if (baseObject instanceof KnownValue) {
      // presumably offset = 0 and indexed = false
      int value = ((KnownValue) baseObject).value;
      emit(Machine.LOADLop, 0, 0, value);
    } else if ((baseObject instanceof UnknownValue) ||
               (baseObject instanceof KnownAddress)) {
      ObjectAddress address = (baseObject instanceof UnknownValue) ?
                              ((UnknownValue) baseObject).address :
                              ((KnownAddress) baseObject).address;
      if (V.indexed) {
        emit(Machine.LOADAop, 0, displayRegister(frame.level, address.level),
             address.displacement + V.offset);
        emit(Machine.CALLop, Machine.SBr, Machine.PBr, Machine.addDisplacement);
        emit(Machine.LOADIop, valSize, 0, 0);
      } else
        emit(Machine.LOADop, valSize, displayRegister(frame.level,
	     address.level), address.displacement + V.offset);
    } else if (baseObject instanceof UnknownAddress) {
      ObjectAddress address = ((UnknownAddress) baseObject).address;
      emit(Machine.LOADop, Machine.addressSize, displayRegister(frame.level,
           address.level), address.displacement);
      if (V.indexed)
        emit(Machine.CALLop, Machine.SBr, Machine.PBr, Machine.addDisplacement);
      if (V.offset != 0) {
        emit(Machine.LOADLop, 0, 0, V.offset);
        emit(Machine.CALLop, Machine.SBr, Machine.PBr, Machine.addDisplacement);
      }
      emit(Machine.LOADIop, valSize, 0, 0);
    }
  }

  // Generates code to compute and push the address of a named variable.
  // vname is the program phrase that names this variable.
  // currentLevel is the routine level where the vname occurs.
  // frameSize is the anticipated size of the local stack frame when
  // the variable is addressed at run-time.

  private void encodeFetchAddress (VName V, Frame frame) {

    RuntimeEntity baseObject = (RuntimeEntity) V.visit(this, frame);
    // If indexed = true, code will have been generated to load an index value.
    if (baseObject instanceof KnownAddress) {
      ObjectAddress address = ((KnownAddress) baseObject).address;
      emit(Machine.LOADAop, 0, displayRegister(frame.level, address.level),
           address.displacement + V.offset);
      if (V.indexed)
        emit(Machine.CALLop, Machine.SBr, Machine.PBr, Machine.addDisplacement);
    } else if (baseObject instanceof UnknownAddress) {
      ObjectAddress address = ((UnknownAddress) baseObject).address;
      emit(Machine.LOADop, Machine.addressSize,displayRegister(frame.level,
           address.level), address.displacement);
      if (V.indexed)
        emit(Machine.CALLop, Machine.SBr, Machine.PBr, Machine.addDisplacement);
      if (V.offset != 0) {
        emit(Machine.LOADLop, 0, 0, V.offset);
        emit(Machine.CALLop, Machine.SBr, Machine.PBr, Machine.addDisplacement);
      }
    }
  }


    @Override
    public Object visitAssignExpression(AssignExpression ast, Object o) {
        Frame frame = (Frame) o;
        Integer valSize = (Integer) ast.type.visit(this, null);
        encodeFetch(ast.V, frame, valSize.intValue());
        return valSize;
    }

    @Override
    public Object visitSecExpression(SecExpression ast, Object o) {
        return ast.secExpression.visit(this, o);
    }

    @Override
    public Object visitOperatorExpression(OperatorExpression ast, Object o) {
        Frame frame = (Frame) o;
        Integer valSize = (Integer) ast.type.visit(this, null);
        int valSize1 = ((Integer) ast.E.visit(this, frame)).intValue();
        Frame frame1 = new Frame(frame.level, valSize1);
        ast.O.visit(this, frame1);
        return valSize;
    }

    @Override
    public Object visitLParenExpression(LParenExpression ast, Object o) {
        return ast.E;
    }

    @Override
    public Object visitLCurlyExpression(LCurlyExpression ast, Object o) {
        ast.type.visit(this, o);
        return ast.RA;
    }

    @Override
    public Object visitLBracketExpression(LBracketExpression ast, Object o) {
        ast.type.visit(this, o);
        return ast.AA;
    }

    @Override
    public Object visitSequentialSingleDeclaration(SequentialSingleDeclaration ast, Object o) {
        Frame frame = (Frame) o;
        Integer val = 0;
        val = (Integer) ast.D1.visit(this,o);
         Frame frame1 = new Frame (frame, val);
         if(ast.D2 != null)
        val = val +  ((Integer) ast.D2.visit(this, frame1)).intValue();
        return val;
    }

    @Override
    public Object visitCompoundDeclarationPrivate(CompoundDeclarationPrivate ast, Object o) {
        Frame frame = (Frame) o;
        Integer val = 0;
        val = (Integer) ast.D1.visit(this,o);
       Frame frame1 = new Frame (frame, val);
         if(ast.D2 != null)
        val = val +  ((Integer) ast.D2.visit(this, frame1)).intValue();
        return val;
    }


    @Override
    public Object visitCompoundDeclarationSingleDeclaration(CompoundDeclarationSingleDeclaration ast, Object o) {
        Frame frame = (Frame) o;
        int extraSize;
        extraSize = (Integer) ast.SD.visit(this, o);
       // emit(Machine.PUSHop, 0, 0, extraSize);
        ast.entity = new KnownAddress(Machine.addressSize, frame.level, frame.size);
        writeTableDetails(ast);
        return extraSize;    
    }

    @Override
    public Object visitVarSingleDeclarationColon(VarSingleDeclarationColon ast, Object o) {
        Frame frame = (Frame) o;
        int extraSize;
        Integer temporal = ((Integer) ast.T.visit(this, o));
        extraSize = temporal.intValue();
        ast.entity = new KnownAddress(Machine.addressSize, frame.level, frame.size);
        writeTableDetails(ast);
        return extraSize;
    }

    @Override
    public Object visitVarSingleDeclarationSingleDeclaration(VarSingleDeclarationSingleDeclaration ast, Object o) {
        Frame frame = (Frame) o;
        int extraSize;
        Integer temporal = ((Integer) ast.T.visit(this, o));
        extraSize = temporal.intValue();
        ast.entity = new KnownAddress(Machine.addressSize, frame.level, frame.size);
        writeTableDetails(ast);
        return extraSize;
    }

    @Override
    public Object visitPackageDeclaration(PackageDeclaration ast, Object o) {
        ast.DEC.visit(this, o);
        return ast.ID.visit(this, o); 
    }

    @Override
    public Object visitSequentialPackageDeclaration(SequentialPackageDeclaration ast, Object o) {
        ast.D1.visit(this, o);
        if(ast.D2 != null)
            ast.D2.visit(this, o);
        return null;
    }

    @Override
    public Object visitTypeDenoterLongIdentifier(TypeDenoterLongIdentifier ast, Object o) {
        return new Integer(0);
    }

    @Override
    public Object visitRTypeDenoter(RTypeDenoter ast, Object o) {
        return ast.REC.visit(this, o);
    }

    @Override
    public Object visitMultipleRecordTypeDenoter(MultipleRecordTypeDenoter ast, Object o) {
        int typeSize;
        if (ast.entity == null) {
          typeSize = ((Integer) ast.FT.visit(this, new Integer(0))).intValue();
          ast.entity = new TypeRepresentation(typeSize);
          writeTableDetails(ast);
        } else
          typeSize = ast.entity.size;
        return new Integer(typeSize + ((Integer)ast.RTD.visit(this,o)));
    }

    @Override
    public Object visitSingleRecordTypeDenoter(SingleRecordTypeDenoter ast, Object o) {
         int typeSize;
        if (ast.entity == null) {
          typeSize = ((Integer) ast.FT.visit(this, new Integer(0))).intValue();
          ast.entity = new TypeRepresentation(typeSize);
          writeTableDetails(ast);
        } else
          typeSize = ast.entity.size;
        return new Integer(typeSize);
    }

    @Override
    public Object visitLongIdentifier(LongIdentifier ast, Object o) {
        ast.identifier.visit(this,o);
        return null;
    }

    @Override
    public Object visitCompoundIdentifier(CompoundIdentifier ast, Object o) {
        ast.identifier.visit(this, o);
        return null;
    }

    @Override
    public Object visitIntLiteralExpression(IntLiteralExpression aThis, Object o) {
        return null;
    }

    @Override
    public Object visitLIdentifierExpression(LIdentifierExpression ast, Object o) {
           Frame frame = (Frame) o;
            Integer valSize = (Integer) ast.type.visit(this, null);
            Integer argsSize = (Integer) ast.APS.visit(this, frame);
            ast.LI.visit(this, new Frame(frame.level, argsSize));
            return valSize;
    }

    @Override
    public Object visitSingleDeclarationCommand(SingleDeclarationCommand ast, Object o) {
         Frame frame = (Frame) o;
        Integer valSize = (Integer) ast.EXP.visit(this, frame);
        encodeStore(ast.VN, new Frame (frame, valSize.intValue()),
                    valSize.intValue());
        return null;
        }
    
    @Override
    public Object visitBracketSelector(BracketSelector aThis, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visitDotSelector(DotSelector ast, Object o) {
          throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
