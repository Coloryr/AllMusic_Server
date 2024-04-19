package com.coloryr.allmusic.server.core.command;

import com.coloryr.allmusic.server.core.objs.enums.HudType;

public abstract class AHudCommand extends ACommand {
    protected final HudType type;

    public AHudCommand(HudType type) {
        this.type = type;
    }
}