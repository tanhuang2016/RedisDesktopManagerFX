package xyz.hashdog.rdm.common.tuple;

/**
 * 元组
 * @author th
 * @version 1.0.0
 * @since 2023/8/1 12:43
 */
public class Tuple2 <T1,T2>{

    private  T1 t1;
    private  T2 t2;

    public Tuple2(T1 t1, T2 t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    public T1 getT1() {
        return t1;
    }

    public T2 getT2() {
        return t2;
    }
}
