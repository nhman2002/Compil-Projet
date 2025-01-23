import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Hashtable;

public abstract class predef {
    private static Map<String, Type> predefMap;
    private static List<String> predefList;

    public static void initialisation() {
        predefMap = new Hashtable<String, Type>();
        predefList = new ArrayList<String>();
        ArrayList<Type> args = new ArrayList<Type>();
        args.add(new TInt());
        predefMap.put("print_int", new TFun(args, new TUnit()));
        predefMap.put("float_of_int", new TFun(args, new TFloat()));
        args = new ArrayList<Type>();
        args.add(new TUnit());
        predefMap.put("print_newline", new TFun(args, new TUnit()));
        args = new ArrayList<Type>();
        args.add(new TFloat());
        predefMap.put("sin", new TFun(args, new TFloat()));
        predefMap.put("cos", new TFun(args, new TFloat()));
        predefMap.put("sqrt", new TFun(args, new TFloat()));
        predefMap.put("abs_float", new TFun(args, new TFloat()));
        predefMap.put("int_of_float", new TFun(args, new TInt()));
        predefMap.put("truncate", new TFun(args, new TInt()));
        predefList.add("print_int");
        predefList.add("float_of_int");
        predefList.add("print_newline");
        predefList.add("sin");
        predefList.add("cos");
        predefList.add("sqrt");
        predefList.add("abs_float");
        predefList.add("int_of_float");
        predefList.add("truncate");
    }

    public static Map<String, Type> getPredefMap() {
        return predefMap;
    }

    public static List<String> getPredefList() {
        return predefList;
    }

}
