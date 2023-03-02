package coloryr.allmusic.core.objs.hud;

public class PosObj {
    public int x;
    public int y;

    public PosObj() {

    }

    public PosObj(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public PosObj copy() {
        return new PosObj(this.x, this.y);
    }
}
