package com.coloryr.allmusic.server.core.command;

import java.util.List;

public interface ICommand {

    void execute(Object sender, String name, String[] args);

    List<String> tab(String name, String[] args, int index);
}
