package net.skullian.torrent.skyfactions.faction;

import net.skullian.torrent.skyfactions.config.types.Messages;

public enum AuditLogType {

    FACTION_CREATE(Messages.AUDIT_FACTION_CREATE_TITLE, Messages.AUDIT_FACTION_CREATE_DESCRIPTION),
    PLAYER_JOIN(Messages.AUDIT_FACTION_JOIN_TITLE, Messages.AUDIT_FACTION_JOIN_DESCRIPTION),
    PLAYER_LEAVE(Messages.AUDIT_FACTION_LEAVE_TITLE, Messages.AUDIT_FACTION_LEAVE_DESCRIPTION),
    MOTD_UPDATE(Messages.AUDIT_FACTION_MOTD_TITLE, Messages.AUDIT_FACTION_MOTD_DESCRIPTION),
    BROADCAST_CREATE(Messages.AUDIT_FACTION_BROADCAST_TITLE, Messages.AUDIT_FACTION_BROADCAST_DESCRIPTION),
    PLAYER_KICK(Messages.AUDIT_FACTION_KICK_TITLE, Messages.AUDIT_FACTION_KICK_DESCRIPTION),
    PLAYER_BAN(Messages.AUDIT_FACTION_BAN_TITLE, Messages.AUDIT_FACTION_BAN_DESCRIPTION),
    INVITE_CREATE(Messages.AUDIT_FACTION_INVITE_CREATE_TITLE, Messages.AUDIT_FACTION_INVITE_CREATE_DESCRIPTION),
    JOIN_REQUEST_CREATE(Messages.AUDIT_FACTION_JOIN_REQUEST_TITLE, Messages.AUDIT_FACTION_JOIN_REQUEST_DESCRIPTION),
    INVITE_REVOKE(Messages.AUDIT_FACTION_INVITE_REVOKE_TITLE, Messages.AUDIT_FACTION_INVITE_REVOKE_DESCRIPTION),
    JOIN_REQUEST_ACCEPT(Messages.AUDIT_FACTION_JOIN_REQUEST_ACCEPT_TITLE, Messages.AUDIT_FACTION_JOIN_REQUEST_ACCEPT_DESCRIPTION),
    JOIN_REQUEST_REJECT(Messages.AUDIT_FACTION_JOIN_REQUEST_REJECT_TITLE, Messages.AUDIT_FACTION_JOIN_REQUEST_REJECT_DESCRIPTION),
    PLAYER_JOIN_REQUEST_REVOKE(Messages.AUDIT_FACTION_PLAYER_JOIN_REQUEST_REVOKE_TITLE, Messages.AUDIT_FACTION_PLAYER_JOIN_REQUEST_REVOKE_DESCRIPTION),
    PLAYER_JOIN_REQUEST_DENY(Messages.AUDIT_FACTION_PLAYER_JOIN_REQUEST_DENY_TITLE, Messages.AUDIT_FACTION_PLAYER_JOIN_REQUEST_DENY_DESCRIPTION),
    INVITE_ACCEPT(Messages.AUDIT_FACTION_PLAYER_INVITE_ACCEPT_TITLE, Messages.AUDIT_FACTION_PLAYER_INVITE_ACCEPT_DESCRIPTION),
    INVITE_DENY(Messages.AUDIT_FACTION_PLAYER_INVITE_DENY_TITLE, Messages.AUDIT_FACTION_PLAYER_INVITE_DENY_DESCRIPTION);

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
