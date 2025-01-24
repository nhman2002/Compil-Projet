import java_cup.runtime.*;
import java.io.*;
import java_cup.runtime.Symbol;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.ComplexSymbolFactory.Location;

public class Main {
  static public void main(String argv[]) {    
    try {
	ComplexSymbolFactory sf=new ComplexSymbolFactory();
      /* Istanzio lo scanner aprendo il file di ingresso argv[0] */
      Yylex l = new Yylex(new FileReader(argv[0]),sf);
      /* Istanzio il parser */
      parser p = new parser(l,sf);
      /* Avvio il parser */
      Object result = p.parse();      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}


