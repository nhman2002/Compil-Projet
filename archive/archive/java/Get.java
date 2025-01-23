import java.util.*;

public class Get extends Exp {
    final Exp e1;
    final Exp e2;

    Get(Exp e1, Exp e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }

    void accept(Visitor v) {
        v.visit(this);
    }

    public Exp getE1() {
        return e1;
    }

    public Exp getE2() {
        return e2;
    }

    public String toString() {
        return "get";
    }
}