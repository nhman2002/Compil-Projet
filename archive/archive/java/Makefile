
SRC= Lexer.java Parser.java Main.java

all: ${SRC} 
	javac -Xlint -cp java-cup-11b-runtime.jar:java-getopt-1.0.13.jar *.java	

Lexer.java: Lexer.flex
	jflex Lexer.flex 

Parser.java: Parser.cup
	java -jar java-cup-11b.jar -nonterms -expect 150 -interface -parser Parser Parser.cup

clean:
	rm -rf *.class rm sym.java Lexer.java Parser.java *~

test:
	../scripts/mincamlc-test-typecheck.sh
# javac -cp .:java-cup-11b-runtime.jar:gnu.getopt.jar *.jav