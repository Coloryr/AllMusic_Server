package coloryr.allmusic.sql;

public interface IEconomy {
    boolean check(String name, int cost);

    boolean cost(String name, int cost);
}
