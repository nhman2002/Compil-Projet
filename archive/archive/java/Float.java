import java.util.*;
public class Float extends Exp {
        float f;
    
        Float(float f) {
            this.f = f;
        }
    
        <E> E accept(ObjVisitor<E> v) {
            return v.visit(this);
        }
    
        void accept(Visitor v) {
            v.visit(this);
        } 
    }