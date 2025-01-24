import java.util.*;

public class TTuple extends Type {
    private List<Type> ele;

    public List<Type> getEle() {
        return ele;
    }

    public TTuple(List<Type> ele) {
        this.ele = ele;
    }

    public TTuple(TTuple tt) {
        this.ele = tt.ele;
    }

    @Override
    public String toString() {
        int i = 1;
        String result = "(";
        result = result + ele.get(0);
        while (i < ele.size()) {
            result = result + "," + ele.get(i);
            i++;
        }
        result = result + ")";
        return result;
    }

    @Override
    public Type clone() {
        List<Type> new_ele = new ArrayList<Type>();
        for (Type t : this.ele)
            new_ele.add(t.clone());
        return new TTuple(new_ele);
    }

    public boolean equals(Object o) {
        if (!(o instanceof TTuple))
            return false;
        else
            return this.ele.equals(((TTuple) o).ele);
    }

    public boolean isDefined() {
        for (Type t : this.ele) {
            if (t instanceof TVar)
                return false;
        }
        return true;
    }
}
