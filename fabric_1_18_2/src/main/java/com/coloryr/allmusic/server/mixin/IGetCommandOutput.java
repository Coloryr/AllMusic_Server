package com.coloryr.allmusic.server.mixin;

import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerCommandSource.class)
public interface IGetCommandOutput {
    @Accessor(value = "output")
    CommandOutput getOutput();

    @Accessor(value = "silent")
    boolean getSilent();
}

