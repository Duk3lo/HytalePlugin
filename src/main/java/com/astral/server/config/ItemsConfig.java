package com.astral.server.config;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.ExtraInfo;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.schema.SchemaContext;
import com.hypixel.hytale.codec.schema.config.Schema;
import org.bson.BsonValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class ItemsConfig {
    public static final BuilderCodec<ItemsConfig> CODEC = null;


    public static class ItemCommand {

        public static final BuilderCodec<ItemCommand> CODEC =
                BuilderCodec.builder(ItemCommand.class, ItemCommand::new)
                        .append(new KeyedCodec<>("Items", Codec.STRING),
                                (r, v, _) -> r.id = v,
                                (r, _) -> r.id)
                        .add()
                        .append(new KeyedCodec<>("Lore", null),
                                (r, v, _) -> r.lore = v,
                                (r, _) -> r.lore)
                        .add()
                        .append(new KeyedCodec<>("Command", Codec.STRING),
                                (r, v, _) -> r.command = v,
                                (r, _) -> r.command)
                        .add()
                        .build();

        private String id = "Tool_Watering_Can";
        private List<String> lore = List.of("event", "asd");
        private String command = "astMen";

        public ItemCommand() {}

        public String getId() { return id; }
        public List<String> getLore() { return lore; }
        public String getCommand() { return command; }

        public void setId(String id) { this.id = id; }
        public void setLore(List<String> lore) { this.lore = lore; }
        public void setCommand(String command) { this.command = command; }
    }
}
