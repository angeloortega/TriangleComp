/*
 * @(#)Token.java                        2.1 2003/10/07
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


final class Token extends Object {

  protected int kind;
  protected String spelling;
  protected SourcePosition position;

  public Token(int kind, String spelling, SourcePosition position) {

    if (kind == Token.IDENTIFIER) {
      int currentKind = firstReservedWord;
      boolean searching = true;

      while (searching) {
        int comparison = tokenTable[currentKind].compareTo(spelling);
        if (comparison == 0) {
          this.kind = currentKind;
          searching = false;
        } else if (comparison > 0 || currentKind == lastReservedWord) {
          this.kind = Token.IDENTIFIER;
          searching = false;
        } else {
          currentKind ++;
        }
      }
    } else
      this.kind = kind;

    this.spelling = spelling;
    this.position = position;

  }

  public static String spell (int kind) {
    return tokenTable[kind];
  }

  public String toString() {
    return "Kind=" + kind + ", spelling=" + spelling +
      ", position=" + position;
  }

  // Token classes...

  /*
  Se anaden los valores para:
	  Palabras reservadas:
			CHOOSE
			FOR
			FROM
			LOOP
			PACKAGE
			PAR
			PASS
			PRIVATE
			RECURSIVE
			TO
			TYPE
			UNTIL
			WHEN
		Puntuaciones:
			PIPE
			SINGLEDECLARATION
			DOLLAR
			DOUBLEDOT
  */
  public static final int

    // literals, identifiers, operators...
    INTLITERAL	= 0,
    CHARLITERAL	= 1,
    IDENTIFIER	= 2,
    OPERATOR	= 3,

    // reserved words - must be in alphabetical order...
    ARRAY		= 4,
    CHOOSE = 5,
    CONST		= 6,
    DO			= 7,
    ELSE		= 8,
    END			= 9,
    FOR = 10,
    FROM = 11,
    FUNC		= 12,
    IF			= 13,
    IN			= 14,
    LET			= 15,
    LOOP = 16,
    OF			= 17,
    PACKAGE = 18,
    PAR = 19,
    PASS = 20,
    PRIVATE = 21,
    PROC		= 22,
    RECORD		= 23,
    RECURSIVE = 24,
    THEN		= 25,
    TO = 26,
    TYPE		= 27,
    UNTIL = 28,
    VAR			= 29,
    WHEN = 30,
    WHILE		= 31,

    // punctuation...
    DOT			= 32,
    COLON		= 33,
    SEMICOLON	= 34,
    COMMA		= 35,
    BECOMES		= 36,
    IS			= 37,
    PIPE = 38,
    SINGLEDECLARATION = 39,
    DOLLAR = 40,
    DOUBLEDOT = 41,

    // brackets...
    LPAREN		= 42,
    RPAREN		= 43,
    LBRACKET	= 44,
    RBRACKET	= 45,
    LCURLY		= 46,
    RCURLY		= 47,
    // special tokens...
    EOT			= 48,
    ERROR		= 49;


    /*
    Se añaden las representaciones a la tabla de los nuevos elementos:
	    Palabras reservadas:
			choose
			for
			from
			loop
			package
			par
			pass
			private
			recursive
			to
			type
			until
			when
		Puntuaciones:
			|
			::=
			$
			..
    */
			
  private static String[] tokenTable = new String[] {
    "<int>",
    "<char>",
    "<identifier>",
    "<operator>",
    "array",
    "choose",
    "const",
    "do",
    "else",
    "end",
    "for",
    "from",
    "func",
    "if",
    "in",
    "let",
    "loop",
    "of",
    "package",
    "par",
    "pass",
    "private",
    "proc",
    "record",
    "recursive",
    "then",
    "to",
    "type",
    "until",
    "var",
    "when",
    "while",
    ".",
    ":",
    ";",
    ",",
    ":=",
    "~",
    "|",
    "::=",
    "$",
    "..",
    "(",
    ")",
    "[",
    "]",
    "{",
    "}",
    "",
    "<error>"
  };

  private final static int	firstReservedWord = Token.ARRAY,
  				lastReservedWord  = Token.WHILE;

}
