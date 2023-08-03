package coloryr.allmusic.core.objs.config;

public class FunConfigObj {
    public boolean Rain;
    public int RainRate;

    public void init() {
        Rain = true;
        RainRate = 10;
    }

    public static FunConfigObj make() {
        FunConfigObj obj = new FunConfigObj();
        obj.init();

        return obj;
    }
}
