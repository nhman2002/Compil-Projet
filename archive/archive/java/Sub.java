import java.util.*;
public class Sub extends Exp {
    final Exp e1;
    final Exp e2;

    Sub(Exp e1, Exp e2) {
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
        return this.e1;
    }

    public Exp getE2() {
        return this.e2;
    }

    public String toString() {
        return "sub";
    }
}
