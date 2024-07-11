package net.skullian.torrent.skyfactions.faction;

import net.skullian.torrent.skyfactions.config.Messages;

public enum AuditLogType {

    FACTION_CREATE(Messages.AUDIT_FACTION_CREATE_TITLE, Messages.AUDIT_FACTION_CREATE_DESCRIPTION),
    PLAYER_JOIN(Messages.AUDIT_FACTION_JOIN_TITLE, Messages.AUDIT_FACTION_JOIN_DESCRIPTION),
    PLAYER_LEAVE(Messages.AUDIT_FACTION_LEAVE_TITLE, Messages.AUDIT_FACTION_LEAVE_DESCRIPTION),
    MOTD_UPDATE(Messages.AUDIT_FACTION_MOTD_TITLE, Messages.AUDIT_FACTION_MOTD_DESCRIPTION),
    BROADCAST_CREATE(Messages.AUDIT_FACTION_BROADCAST_TITLE, Messages.AUDIT_FACTION_BROADCAST_DESCRIPTION),
    PLAYER_KICK(Messages.AUDIT_FACTION_KICK_TITLE, Messages.AUDIT_FACTION_KICK_DESCRIPTION),
    PLAYER_BAN(Messages.AUDIT_FACTION_BAN_TITLE, Messages.AUDIT_FACTION_BAN_DESCRIPTION);

    private final Messages title;
    private final Messages description;
    AuditLogType(Messages title, Messages description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle(Object... replacements) {
        return title.get(replacements);
    }

    public String getDescription(Object... replacements) {
        return description.get(replacements);
    }


}
