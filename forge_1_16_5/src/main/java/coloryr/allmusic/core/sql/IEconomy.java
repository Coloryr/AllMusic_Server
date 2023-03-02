package coloryr.allmusic.core.sql;

public interface IEconomy {
    boolean check(String name, int cost);

    boolean cost(String name, int cost);
}
