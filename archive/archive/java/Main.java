import java_cup.runtime.*;
import java.io.*;
import java.util.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

public class Main {
  static public void main(String argv[]) {
    // try {
    // Parser p = new Parser(new Lexer(new FileReader(argv[0])));
    // Exp expression = (Exp) p.parse().value;
    // assert (expression != null);

    // System.out.println("------ AST ------");
    // expression.accept(new PrintVisitor());
    // System.out.println();

    // System.out.println("------ Height of the AST ----");
    // int height = Height.computeHeight(expression);
    // System.out.println("using Height.computeHeight: " + height);

    // ObjVisitor<Integer> v = new HeightVisitor();
    // height = expression.accept(v);
    // System.out.println("using HeightVisitor: " + height);

    // } catch (Exception e) {
    // e.toString();
    // }
    int c;
    String arg;
    LongOpt[] longOpts = new LongOpt[5];
    // 1.help
    // 2.input
    // 3.output
    // 4.version
    // 5.asml

    // options
    boolean typeCheckOnly = false;
    boolean parseOnly = false;
    boolean genArmFromAsml = false;
    boolean genAsmlFlag = false;
    boolean bOutputFile = false;

    // String file
    String inputFile = null;
    String outputFile = null;

    StringBuffer sb = new StringBuffer(); // buffer input
    StringBuffer sb2 = new StringBuffer(); // buffer output

    longOpts[0] = new LongOpt("help", LongOpt.NO_ARGUMENT, sb, 'h');
    longOpts[1] = new LongOpt("input", LongOpt.REQUIRED_ARGUMENT, sb, 'i');
    longOpts[2] = new LongOpt("output", LongOpt.REQUIRED_ARGUMENT, sb2, 'o');
    longOpts[3] = new LongOpt("version", LongOpt.NO_ARGUMENT, null, 'v');
    longOpts[4] = new LongOpt("asml", LongOpt.NO_ARGUMENT, null, 'a');

    Getopt g = new Getopt("testCompiler", argv, "ho:i:vtpSa", longOpts);
    // 1st param: program name
    // 2nd param: arguments array
    // 3rd param: Option Specification String 
    // o: → Option o requires an argument (indicated by the colon :).
    // i: → Option i requires an argument.
    // 4th param: Long Options 

    g.setOpterr(false);

    while ((c = g.getopt()) != -1) {
      switch (c) {
        case 'h':
          // help = true;
          System.out.println("MinCaml compiler help:");
          System.out.println("Make sure you parse to correct file");
          System.out.println("  -h, --help           Print this help message");
          System.out.println("  -i, --input <file>   Input file");
          System.out.println("  -o, --output <file>  Output file");
          System.out.println("  -v, --version        Print version information");
          System.out.println("  -a, --asml           Generate ASML file");
          System.out.println("  -t,                  typecheck only");
          System.out.println("  -p,                  parse only");
          System.out.println("  -S,                  generate asm from asml file");
          System.exit(0);
          break;
        case 'i':
          arg = g.getOptarg();
          inputFile = arg;
          break;
        case 'o':
          arg = g.getOptarg();
          outputFile = arg;
          bOutputFile = true;
          break;
        case 'v':
          System.out.println("Version 1.0");
          System.exit(0);
        case 't':
          typeCheckOnly = true;
          break;
        case 'a':
          genAsmlFlag = true;
          break;
        case 'p':
          parseOnly = true;
          break;
        case 'S':
          genArmFromAsml = true;
          break;
        case '?':
          System.err.println("Unknown option: -" + (char) g.getOptopt());
          System.exit(1);
        default:
          System.err.println("Programming error, getopt returned incorrect option character code.");
          System.exit(1);
      }

      // errorMessage
      if (inputFile == null) {
        if (argv.length == 0) {
          System.out.println("No input file");
          System.exit(1);
        } else {
          inputFile = argv[0];
        }
      }

      if (typeCheckOnly) {
        try {
          if (argv.length == 1) {
            System.out.println("No input file specified");
            System.exit(1);
          }
          inputFile = argv[1];
          predef.initialisation();

          Parser p = new Parser(new Lexer(new FileReader(inputFile)));
          Exp expression = (Exp) p.parse().value;

          assert (expression != null);
          typeChecker checker = new typeChecker();
          System.out.println("Type checking...");
          // typeChecker checker = new typeChecker();
          // result = checker.check(expression);

          boolean result = false;
          result = checker.check(expression);
          System.out.println(result);

          if (!result) {
            System.out.println("Code is not well typed");
          } else {
            System.out.println("Code is well typed");
          }

        } catch (Exception e) {
          System.out.println("Error at TypeCheck");
          System.out.println(e);
          System.exit(1);
        }
      } else if (parseOnly) {
        try {
          if (argv.length == 1) {
            System.out.println("No input file specified");
            System.exit(1);
          }
          inputFile = argv[1];
          System.out.println("Parse only");
          Parser p = new Parser(new Lexer(new FileReader(inputFile)));
          Exp expression = (Exp) p.parse().value;
          assert (expression != null);
          predef.initialisation();

          // try {
          // Parser p = new Parser(new Lexer(new FileReader(argv[0])));
          // Exp expression = (Exp) p.parse().value;
          // assert (expression != null);

          System.out.println("------ AST ------");
          expression.accept(new PrintVisitor());
          System.out.println();

          System.out.println("------ Height of the AST ----");
          int height = Height.computeHeight(expression);
          System.out.println("using Height.computeHeight: " + height);

          ObjVisitor<Integer> v = new HeightVisitor();
          height = expression.accept(v);
          System.out.println("using HeightVisitor: " + height);

          // } catch (Exception e) {
          // e.toString();
          // }
        } catch (Exception e) {
        }
      } else if (bOutputFile) {
        try {
          PrintStream o = new PrintStream(new FileOutputStream(outputFile));
          PrintStream console = System.out;
          System.setOut(o);

          Parser p = new Parser(new Lexer(new FileReader(inputFile)));
          Exp expression = (Exp) p.parse().value;
          assert (expression != null);

          predef.initialisation();

          System.out.println("------ AST ------");
          expression.accept(new PrintVisitor());
          System.out.println();

          System.out.println("\n\n------ Height of the AST ----");
          int height = Height.computeHeight(expression);
          System.out.println("using Height.computeHeight: " + height);

          ObjVisitor<Integer> v = new HeightVisitor();
          height = expression.accept(v);
          System.out.println("using HeightVisitor: " + height);

          System.out.println("\n\n------ Type Check ------");
          typeChecker checker = new typeChecker();
          boolean result = false;
          result = checker.check(expression);
          if (result) {
            System.out.println("Well typed code");
          } else {
            System.out.println("Code is not well typed");
          }

          System.out.println("\n\n----Var Computation-----");
          Vars varss = new Vars();
          ArrayList<String> varList = varss.computeVars(expression);
          System.out.println(varList);

          System.out.println("\n\n---- Duplicated AST ----");
          ObjVisitor<Exp> x = new DuplicateVisitor();
          Exp duplicated_expression = expression.accept(x);
          duplicated_expression.accept(new PrintVisitor());
          System.out.println();

          System.out.println("\n\n---- Set of variables ----");
          ObjVisitor<String> w = new VariableVisitor();
          String vars = expression.accept(w);
          System.out.println("Set of Variables in AST using visitor: " + vars);

          System.out.println("\n\n------ AST Knormalized ------");
          Knorm knorms = new Knorm();
          Exp expression3 = knorms.computeKnorm(expression);
          expression3.accept(new PrintVisitor());
          System.out.println();

          System.out.println("\n\n------ Alpha Convertion------");
          alphaCon ac = new alphaCon();
          Exp alpha = expression3.accept(ac);
          alpha.accept(new PrintVisitor());
          System.out.println();

          System.out.println("\n\n------ Nested Let Reduction ------");
          nestedLetReduc nlr = new nestedLetReduc();
          Exp e_nlr = expression3.accept(nlr);
          e_nlr.accept(new PrintVisitor());
          System.out.println();

          Path path = Paths.get(inputFile);
          String fileName = path.getFileName().toString();
          String data[] = fileName.split(".ml");
          String fileNameWithoutExt = data[0];

          System.out.println("\n\n------ ASML------");
          asmlGen ag = new asmlGen();
          String asml = e_nlr.accept(ag);

          asml = asmlGen.declarationFloat + asmlGen.declaration + asmlGen.entryPoint + asml;
          String asmlFile = path.getParent() + "/" + fileNameWithoutExt + ".asml";
          PrintWriter wr = new PrintWriter(new BufferedWriter(new FileWriter(asmlFile)));
          wr.print(asml);
          wr.close();
          System.out.println(asml);
          System.out.println();

          String asmFile = path.getParent() + "/" + fileNameWithoutExt + ".s";
          Process process;
          try {
            String cmd = "python3 ../ASML2ASM/main.py -fo " + asmlFile + " " + asmFile;
            // System.out.println(cmd);
            process = Runtime.getRuntime().exec(cmd);
          } catch (Exception e) {
            System.out.println("Exception Raised" + e.toString());
          }

        } catch (Exception e) {
          System.out.println("Writing output to file error");
        }
      }

      else if (genArmFromAsml) {
        try {
          if (argv.length == 1) {
            System.out.println("input file must be defined!");
            System.exit(1);
          }
          inputFile = argv[1];

          Parser p = new Parser(new Lexer(new FileReader(inputFile)));
          Exp expression = (Exp) p.parse().value;
          assert (expression != null);

          predef.initialisation();

          System.out.println("------ AST ------");
          expression.accept(new PrintVisitor());
          System.out.println();

          System.out.println("\n\n------ Height of the AST ----");
          int height = Height.computeHeight(expression);
          System.out.println("using Height.computeHeight: " + height);

          ObjVisitor<Integer> v = new HeightVisitor();
          height = expression.accept(v);
          System.out.println("using HeightVisitor: " + height);

          System.out.println("\n\n------ Type Check ------");
          typeChecker checker = new typeChecker();
          boolean result = false;
          result = checker.check(expression);
          if (result) {
            System.out.println("Well typed code");
          } else {
            System.out.println("Code is not well typed");
          }

          System.out.println("\n\n----Var Computation-----");
          Vars varss = new Vars();
          ArrayList<String> varList = varss.computeVars(expression);
          System.out.println(varList);

          System.out.println("\n\n---- Duplicated AST ----");
          ObjVisitor<Exp> x = new DuplicateVisitor();
          Exp duplicated_expression = expression.accept(x);
          duplicated_expression.accept(new PrintVisitor());
          System.out.println();

          System.out.println("\n\n---- Set of variables ----");
          ObjVisitor<String> w = new VariableVisitor();
          String vars = expression.accept(w);
          System.out.println("Set of Variables in AST using visitor: " + vars);

          System.out.println("\n\n------ AST Knormalized ------");
          Knorm knorms = new Knorm();
          Exp expression3 = knorms.computeKnorm(expression);
          expression3.accept(new PrintVisitor());
          System.out.println();

          System.out.println("\n\n------ Alpha Convertion------");
          alphaCon ac = new alphaCon();
          Exp alpha = expression.accept(ac);
          alpha.accept(new PrintVisitor());
          System.out.println();

          System.out.println("\n\n------ Nested Let Reduction ------");
          nestedLetReduc nlr = new nestedLetReduc();
          Exp e_nlr = expression3.accept(nlr);
          e_nlr.accept(new PrintVisitor());
          System.out.println();

          Path path = Paths.get(inputFile);
          String fileName = path.getFileName().toString();
          String data[] = fileName.split(".ml");
          String fileNameWithoutExt = data[0];

          System.out.println("\n\n------ ASML------");
          asmlGen ag = new asmlGen();
          String asml = e_nlr.accept(ag);

          asml = asmlGen.declarationFloat + asmlGen.declaration + asmlGen.entryPoint + asml;
          String asmlFile = path.getParent() + "/" + fileNameWithoutExt + ".asml";
          PrintWriter wr = new PrintWriter(new BufferedWriter(new FileWriter(asmlFile)));
          wr.print(asml);
          wr.close();
          System.out.println(asml);
          System.out.println();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

    }

  }
}
