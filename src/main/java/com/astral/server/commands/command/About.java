package com.astral.server.commands.command;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.FormattedMessage;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.permissions.HytalePermissions;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;


public final class About extends AbstractPlayerCommand {
    public About(@NonNullDecl String name, @NonNullDecl String description, boolean requiresConfirmation) {
        super(name, description, requiresConfirmation);
        this.requirePermission(HytalePermissions.fromCommand("description.command"));
    }

    @Override
    protected void execute(
            @NonNullDecl CommandContext commandContext,
            @NonNullDecl Store<EntityStore> store,
            @NonNullDecl Ref<EntityStore> ref,
            @NonNullDecl PlayerRef playerRef,
            @NonNullDecl World world
    ) {
        String text = "ASTRAL SERVER";
        FormattedMessage root = new FormattedMessage();
        root.children = new FormattedMessage[text.length()];
        int startColor = 0x00FFFF;
        int endColor   = 0xFF00FF;
        for (int i = 0; i < text.length(); i++) {
            float t = (float) i / (text.length() - 1);
            int r = (int) (((0) * (1 - t)) + ((endColor >> 16 & 0xFF) * t));
            int g = (int) (((startColor >> 8  & 0xFF) * (1 - t)) + ((0) * t));
            int b = (int) (((startColor       & 0xFF) * (1 - t)) + ((endColor & 0xFF) * t));
            FormattedMessage part = new FormattedMessage();
            part.rawText = String.valueOf(text.charAt(i));
            part.color = String.format("#%02X%02X%02X", r, g, b);

            root.children[i] = part;
        }
        playerRef.sendMessage(new Message(root));
    }
}
