package Color_yr.AllMusic.MusicAPI.MusicAPI1.GetProgramInfo;

public class PrInfoOBJ {

    private program program;

    public boolean isOK(){
        return (program != null && program.getMainSong().getName() != null);
    }

    public String getDj() {

        return program.getDj().getBrand();
    }

    public String getId(){

        return String.valueOf(program.getMainSong().getId());
    }

    public String getName(){

        return program.getMainSong().getName();
    }
}

class mainSong{

    private String name;
    private int id;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

class dj{

    private String brand;

    public String getBrand() {
        return brand;
    }
}

class program{

    private mainSong mainSong;
    private dj dj;

     public dj getDj() {
        return dj;
    }

    public mainSong getMainSong() {
        return mainSong;
    }
}