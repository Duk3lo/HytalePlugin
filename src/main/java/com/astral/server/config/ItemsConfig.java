package com.astral.server.config;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.map.MapCodec;
import java.util.HashMap;
import java.util.Map;

public final class ItemsConfig {
    public static final BuilderCodec<ItemsConfig> CODEC =
            BuilderCodec.builder(ItemsConfig.class, ItemsConfig::new)
                    .append(new KeyedCodec<>("Items", new MapCodec<>(ItemCommand.CODEC, HashMap::new)),
                            (m, v, _) -> m.itemsMap = v,
                            (m, _) -> m.itemsMap)
                    .add()
                    .build();

    private Map<String, ItemCommand> itemsMap = new HashMap<>();

        public ItemsConfig() { itemsMap.put("ItemMenu", new ItemCommand("Menu", (short) 36)); }

    public Map<String, ItemCommand> getItemsMap() { return itemsMap; }

    public static class ItemCommand {
        public static final BuilderCodec<ItemCommand> CODEC =
                BuilderCodec.builder(ItemCommand.class, ItemCommand::new)
                        .append(new KeyedCodec<>("ID", Codec.STRING),
                                (r, v, _) -> r.id = v,
                                (r, _) -> r.id)
                        .add()
                        .append(new KeyedCodec<>("Slot", Codec.SHORT),
                                (r, v, _) -> r.slot = v,
                                (r, _) -> r.slot)
                        .add()
                        .build();
        private String id = "";
        private short slot = 0;

        public ItemCommand() {}
        public ItemCommand(String id, short slot) {
            this.id = id;
            this.slot = slot;
        }
        public String getId() {return id;}
        public short getSlot() {return slot;}
    }
}
