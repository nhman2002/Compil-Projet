import java.util.*;
public class Neg extends Exp {
    final Exp e;

    Neg(Exp e) {
        this.e = e;
    }
    // @Override
    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    } 
    void accept(Visitor v) {
        v.visit(this);
    }
    // @Override
    public Exp getE() {
        return e;
    }
    // @Override
    public String toString() {
        return "-" + e;
    }
}