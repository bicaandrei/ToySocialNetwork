package ubb_221.toysocialnetworkgui.domain;

import java.util.Objects;

public class Tuple<Entity1, Entity2>{

    private Entity1 e1;
    private Entity2 e2;

    public Tuple() {}
    public Tuple(Entity1 e1, Entity2 e2){

        this.e1 = e1;
        this.e2 = e2;

    }
    public void set_e1(Entity1 e1) { this.e1 = e1; }
    public void set_e2(Entity2 e2) { this.e2 = e2; }
    public Entity1 get_e1() { return this.e1; }
    public Entity2 get_e2() { return this.e2; }

    @Override
    public String toString() {

        return "(" + e1 + "), (" + e2 + ")";
    }

    @Override
    public boolean equals(Object obj) {

        return (this.e1.equals(((Tuple) obj).e1)) && (this.e2.equals(((Tuple) obj).e2));

    }

    @Override
    public int hashCode() {
        return Objects.hash(e1, e2);
    }


}