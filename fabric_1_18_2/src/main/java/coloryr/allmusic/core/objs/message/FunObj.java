package coloryr.allmusic.core.objs.message;

public class FunObj {
    public String Rain;

    public FunObj()
    {
        Rain = "§d[AllMusic]§e天空开始变得湿润";
    }

    public boolean check(){
        boolean res =false;
        if(Rain == null)
        {
            res = true;
        }

        return res;
    }
}
