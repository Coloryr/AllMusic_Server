package coloryr.allmusic.core.objs.message;

public class FunObj {
    public String Rain;

    public boolean check() {
        boolean res = false;
        if (Rain == null) {
            res = true;
        }

        return res;
    }

    public void init() {
        Rain = "§d[AllMusic]§e天空开始变得湿润";
    }

    public static FunObj make() {
        FunObj obj = new FunObj();
        obj.init();

        return obj;
    }
}
