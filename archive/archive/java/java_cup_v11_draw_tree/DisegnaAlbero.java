import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.awt.*;
import java.net.URL;

@SuppressWarnings({"deprecation","unchecked"})
public class DisegnaAlbero
{
	ArrayList stack;
	double ultimo_inserimento;
	double deltaX;
	double deltaY;
	double raggio;
	sym term;
	Font ff;
	double numE;
	double scale;
	private final String filesizeName="scale.in";
	
	
	String last_nomeP;
	int last_pp;
	boolean savedSymbols;

	
	int reduced_elements;
	
	

	public DisegnaAlbero()
	{

		//This is to have the file size.txt inside the cup folder 
		URL classesRootDir = getClass().getProtectionDomain().getCodeSource().getLocation();
		String path=new String(classesRootDir.toString().replaceAll("^file:",""));
		 

		/*
		 * Read the scale value first from the project folder then from the CUP folder. If we can't, use the default
		 * value (10.0).
		 */
		if(!read_scale_from(filesizeName)) {
			
			read_scale_from(path + filesizeName );
		}

		stack = new ArrayList();
		term = new sym();
		ultimo_inserimento = 0;

		deltaX = .1 / scale;
		deltaY = .22 / scale;
		raggio = .06 / scale;
		StdDraw.setCanvasSize(300 * (int)scale, 200 * (int)scale);
		ff = new Font("DialogInput", Font.BOLD, 9);
		StdDraw.setFont(ff);
		numE = 0;

		savedSymbols=false;


		
	}


	private boolean read_scale_from(String filepath)
	{
		
		FileReader fr = null;
		BufferedReader br = null;
		try {
			//System.out.println(path + "scale.txt");
			fr = new FileReader(filepath);
			br = new BufferedReader(fr);

			String text;
			if ((text = br.readLine()) == null) {
				throw new IOException("Impossible to read the scale");
			}

			scale = Double.parseDouble(text);
			if (scale < 1.0 ) { //setCanvasSize wants an integer > 0 as input 
				throw new IOException("Incorrect scale value");
			}
			//System.out.println("New scale = " + scale);
			return true;

			
		} catch (IOException e) {
			//System.out.println("Using default scale");
			scale = 10.0;
			return false;
		} catch (NumberFormatException nfe) {
			//System.out.println("Using default scale");
			scale = 10.0;
			return false;
		}


	}

	public void push(String string, int pp)
	{
		String nomeP;


		//ComplexSymbolFactory input or reduce input
		if(string.startsWith("Symbol: ")) {
			nomeP= new String(string.substring(8));
			
		
		} else if(string.startsWith("#")) {
		//Symbol input
					
			string = string.substring(1);
			if (pp == 1)
				nomeP = new String(string);
			else
				nomeP = new String(term.getTT(new Integer(string).intValue()));
		} else {
		 //Symbol marker
			
			if (pp == 1) nomeP = new String(string); 	
			else nomeP = new String(term.getTT(new Integer(string).intValue()));
			//else nomeP=new String(string);
		}
		
		
		
		
		//Adding the delay of one symbol
		//Removed the EOF symbol from
		
		if(savedSymbols==true && nomeP.equals("EOF") ) {
		
			printNodo(last_nomeP,last_pp);
			//printNodo(nomeP,pp);
			savedSymbols=false;
			return;

		} else if(savedSymbols==false && nomeP.equals("EOF")) {
			
			//Print the symbol without storing it since it's the end of the file
			//printNodo(nomeP,pp);
			return;

		} 
		if(savedSymbols==false && !nomeP.equals("EOF")) {
			//First valid push
			last_nomeP=new String(nomeP);
			last_pp=pp;
			savedSymbols=true;
		} else if(savedSymbols==true && !nomeP.equals("EOF")) {
			
			if(pp==0) {
				//a normal token was inserted
				printNodo(last_nomeP,last_pp);
				last_nomeP=new String(nomeP);
				last_pp=pp;
				savedSymbols=true;

			} else {
				//a token from epsilon was inserted, i swap it
				printNodo(nomeP,pp);
				
			}

		}
		
		

		
	}
		
	
	public void printNodo(String nomeP,int pp)
	{
		if (numE == 0)
			numE = .1 / scale;
		else
			numE = 0;

		if (pp == 1)
			StdDraw.setPenColor(StdDraw.MAGENTA);
		else
			StdDraw.setPenColor(StdDraw.LIGHT_GRAY);

		if (stack.isEmpty() == true) {
			stack.add(0, new Nodo(nomeP, 0, numE));
			//System.out.println(((Nodo)stack.get(0)).valore);
			StdDraw.filledCircle(0, numE, raggio);
			StdDraw.setPenColor();
			StdDraw.text(0, numE, nomeP);
		} else {
			stack.add(0, new Nodo(nomeP, ultimo_inserimento + deltaX, numE));
			
			StdDraw.filledCircle(ultimo_inserimento + deltaX, numE, raggio);
			StdDraw.setPenColor();
			StdDraw.text(ultimo_inserimento + deltaX, numE, nomeP);//term.getTT(new Integer(string).intValue()));
			ultimo_inserimento += deltaX;
		}

		//System.out.println("PUSH:" + nomeP + " PP: " + pp + " SS : " + savedSymbols);
	}

	public void reduce(int i, String string, int fine)
	{
		//System.out.println("REDUCE:" + i + string );
		if (i == 0) {
			printNodo(string, 1);
		} else {
			
			double maxY = ((Nodo)stack.get(0)).posY, X = ((Nodo)stack.get(0)).posX, posXX = 0;

			//ciclo per prendere posizione Y massima
			for (int j = 0; j < i; j++)
			{
				if (((Nodo)stack.get(j)).posY > maxY)
					maxY = ((Nodo)stack.get(j)).posY;
				posXX += ((Nodo)stack.get(j)).posX;
			}
			posXX = posXX / i;

			//ciclo per disegnare linee di congiunzione
			for (int j = 0; j < i; j++)
			{
				StdDraw.line(((Nodo)stack.get(j)).posX, ((Nodo)stack.get(j)).posY + raggio, posXX, maxY + deltaY - raggio);
			}

			//ciclo per fare pop elementi
			for (int j = 0; j < i; j++)
			{
				stack.remove(0);
			}
			stack.add(0, new Nodo(string, posXX, maxY + deltaY));

			
			
			StdDraw.setPenColor(StdDraw.GREEN);
			

			StdDraw.filledCircle(posXX, maxY + deltaY, raggio);
			StdDraw.setPenColor();
			StdDraw.text(posXX, maxY + deltaY, string);
			
			
		}
	}


}
