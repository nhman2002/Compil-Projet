import java_cup.runtime.Symbol;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.ComplexSymbolFactory.Location;
import java_cup.runtime.*;

%%

%unicode
%cup
%line
%column

/*New builder for the parser*/
%{
    ComplexSymbolFactory sf;	
    StringBuffer string = new StringBuffer();
    public Yylex(java.io.Reader in, ComplexSymbolFactory sf){
	this(in);
	this.sf = sf;
    }
   


  private void error(String message) {
    System.out.println("Error at line "+(yyline+1)+", column "+(yycolumn+1)+" : "+message);
  }
%}

/*The EOF has to be a ComplexSymbol too to avoid a ClassCastException at the end*/
%eofval{
     return sf.newSymbol("EOF", sym.EOF);
%eofval}



number		=	[0-9]+
word 		=	[a-zA-Z]+
comment		=	"//".*
ident  		=	[_a-zA-Z][_a-zA-Z0-9]*


%%

"->"    	{return sf.newSymbol("ARROW",sym.ARROW); }
"-"		{return sf.newSymbol("MINUS",sym.MINUS); }
"+"		{return sf.newSymbol("PLUS",sym.PLUS); }
"/"		{return sf.newSymbol("DIV",sym.DIV); }
"*"		{return sf.newSymbol("STAR",sym.STAR); }
"("		{return sf.newSymbol("OB",sym.OB); }
")"		{return sf.newSymbol("CB",sym.CB); }
";"		{return sf.newSymbol("SC",sym.SC); }
","		{return sf.newSymbol("C",sym.C); }
"."		{return sf.newSymbol("D",sym.D); }
":"		{return sf.newSymbol("DD",sym.DD); }
"="		{return sf.newSymbol("EQ",sym.EQ); }

{comment} 	{;}
{number}	{return sf.newSymbol("NUMBER",sym.NUMBER, new Integer(yytext())); }
{word}		{return sf.newSymbol("WORD",sym.WORD, new String(yytext())); }
{ident} 	{return sf.newSymbol("ID",sym.ID, new String(yytext())); }

\n|\r|\r\n 	{;}
[ \t]		{;}

