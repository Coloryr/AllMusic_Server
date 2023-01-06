package coloryr.allmusic.hud.obj;

public class PosOBJ {
    public int x;
    public int y;

    public PosOBJ() {

    }

    public PosOBJ(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public PosOBJ copy() {
        return new PosOBJ(this.x, this.y);
    }
}
