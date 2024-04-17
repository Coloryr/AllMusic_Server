package coloryr.allmusic.core.objs.config;

public class FunConfigObj {
    public boolean rain;
    public int rainRate;

    public void init() {
        rain = true;
        rainRate = 10;
    }

    public static FunConfigObj make() {
        FunConfigObj obj = new FunConfigObj();
        obj.init();

        return obj;
    }
}
