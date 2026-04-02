package com.coloryr.allmusic.server.mixin;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CommandSourceStack.class)
public interface IGetCommandOutput {
    @Accessor(value = "source")
    CommandSource getSource();

    @Accessor(value = "silent")
    boolean getSilent();
}

