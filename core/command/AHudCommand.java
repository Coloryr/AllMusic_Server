package coloryr.allmusic.core.command;

import coloryr.allmusic.core.objs.enums.HudType;

public abstract class AHudCommand extends ACommand {
    protected final HudType type;

    public AHudCommand(HudType type) {
        this.type = type;
    }
}