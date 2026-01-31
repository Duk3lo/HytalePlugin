package com.astral.server.config;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.map.MapCodec;

import java.util.HashMap;
import java.util.Map;

public final class PluginConfig {

    /* ================= ROOT ================= */

    public static final BuilderCodec<PluginConfig> CODEC =
            BuilderCodec.builder(PluginConfig.class, PluginConfig::new)
                    .append(new KeyedCodec<>("Join", Join.CODEC),
                            (cfg, v, _) -> cfg.join = v,
                            (cfg, _) -> cfg.join)
                    .add()
                    .append(new KeyedCodec<>("Menu_Lobby", MenuLobby.CODEC),
                            (cfg, v, _) -> cfg.menuLobby = v,
                            (cfg, _) -> cfg.menuLobby)
                    .add()
                    .build();

    private Join join = new Join();
    private MenuLobby menuLobby = new MenuLobby();

    public Join getJoin() { return join; }
    public MenuLobby getMenuLobby() { return menuLobby; }

    /* ================= JOIN ================= */

    public static class Join {

        public static final BuilderCodec<Join> CODEC =
                BuilderCodec.builder(Join.class, Join::new)
                        .append(new KeyedCodec<>("Enable", Codec.BOOLEAN),
                                (j, v, _) -> j.enable = v,
                                (j, _) -> j.enable)
                        .add()
                        .append(new KeyedCodec<>("TopMessage", Codec.STRING),
                                (j, v, _) -> j.topMessage = v,
                                (j, _) -> j.topMessage)
                        .add()
                        .append(new KeyedCodec<>("BottomMessage", Codec.STRING),
                                (j, v, _) -> j.bottomMessage = v,
                                (j, _) -> j.bottomMessage)
                        .add()
                        .build();

        private boolean enable = true;
        private String topMessage = "Bienvenido al servidor";
        private String bottomMessage = "Astral Network";

        public boolean isEnable() { return enable; }
        public String getTopMessage() { return topMessage; }
        public String getBottomMessage() { return bottomMessage; }
    }

    /* ================= MENU LOBBY ================= */

    public static class MenuLobby {

        public static final BuilderCodec<MenuLobby> CODEC =
                BuilderCodec.builder(MenuLobby.class, MenuLobby::new)
                        .append(new KeyedCodec<>("Redis", Redis.CODEC),
                                (m, v, _) -> m.redis = v,
                                (m, _) -> m.redis)
                        .add()
                        .append(new KeyedCodec<>("Status", Status.CODEC),
                                (m, v, _) -> m.status = v,
                                (m, _) -> m.status)
                        .add()
                        .append(new KeyedCodec<>("Servers",
                                        new MapCodec<>(ServerInfo.CODEC, HashMap::new)),
                                (m, v, _) -> m.servers = v,
                                (m, _) -> m.servers)
                        .add()
                        .build();

        private Redis redis = new Redis();
        private Status status = new Status();
        private Map<String, ServerInfo> servers = new HashMap<>();

        public MenuLobby() {
            servers.put("Vanilla", new ServerInfo("Vanilla"));
        }

        public Redis getRedis() { return redis; }
        public Status getStatus() { return status; }
        public Map<String, ServerInfo> getServers() { return servers; }
    }

    /* ================= STATUS ================= */

    public static class Status {

        public static final BuilderCodec<Status> CODEC =
                BuilderCodec.builder(Status.class, Status::new)
                        .append(new KeyedCodec<>("Online", StatusInfo.CODEC),
                                (s, v, _) -> s.online = v,
                                (s, _) -> s.online)
                        .add()
                        .append(new KeyedCodec<>("Offline", StatusInfo.CODEC),
                                (s, v, _) -> s.offline = v,
                                (s, _) -> s.offline)
                        .add()
                        .build();

        private StatusInfo online = new StatusInfo(
                "#00FF00", "Activo", "#FFFFFF", "$actual / $max"
        );

        private StatusInfo offline = new StatusInfo(
                "#FF0000", "Inactivo", "#777777", "-0-"
        );

        public StatusInfo getOnline() { return online; }
        public StatusInfo getOffline() { return offline; }
    }

    /* ================= REDIS ================= */

    public static class Redis {

        public static final BuilderCodec<Redis> CODEC =
                BuilderCodec.builder(Redis.class, Redis::new)
                        .append(new KeyedCodec<>("Enabled", Codec.BOOLEAN),
                                (r, v, _) -> r.enabled = v,
                                (r, _) -> r.enabled)
                        .add()
                        .build();

        private boolean enabled = false;

        public boolean isEnabled() { return enabled; }
    }

    /* ================= SERVER INFO ================= */

    public static class ServerInfo {

        public static final BuilderCodec<ServerInfo> CODEC =
                BuilderCodec.builder(ServerInfo.class, ServerInfo::new)
                        .append(new KeyedCodec<>("Name", Codec.STRING),
                                (s, v, _) -> s.name = v,
                                (s, _) -> s.name)
                        .add()
                        .build();

        private String name = "Server";

        public ServerInfo() {}
        public ServerInfo(String name) { this.name = name; }

        //public String getName() { return name; }
    }

    /* ================= STATUS INFO ================= */

    public static class StatusInfo {

        public static final BuilderCodec<StatusInfo> CODEC =
                BuilderCodec.builder(StatusInfo.class, StatusInfo::new)
                        .append(new KeyedCodec<>("Text", Codec.STRING),
                                (st, v, _) -> st.text = v,
                                (st, _) -> st.text)
                        .add()
                        .append(new KeyedCodec<>("Color", Codec.STRING),
                                (st, v, _) -> st.color = v,
                                (st, _) -> st.color)
                        .add()
                        .append(new KeyedCodec<>("Count_color", Codec.STRING),
                                (st, v, _) -> st.countColor = v,
                                (st, _) -> st.countColor)
                        .add()
                        .append(new KeyedCodec<>("Format_count", Codec.STRING),
                                (st, v, _) -> st.formatCount = v,
                                (st, _) -> st.formatCount)
                        .add()
                        .build();

        private String text = "Offline";
        private String color = "#FFFFFF";
        private String countColor = "#777777";
        private String formatCount = "-0-";

        public StatusInfo() {}

        public StatusInfo(String color, String text, String countColor, String formatCount) {
            this.color = color;
            this.text = text;
            this.countColor = countColor;
            this.formatCount = formatCount;
        }

        public String getText() { return text; }
        public String getColor() { return color; }
        public String getCountColor() { return countColor; }
        public String getFormatCount() { return formatCount; }
    }
}