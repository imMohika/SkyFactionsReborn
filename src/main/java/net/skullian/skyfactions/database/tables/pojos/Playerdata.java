package net.skullian.skyfactions.database.tables.pojos;


import java.io.Serializable;


@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Playerdata implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String uuid;
    private final String faction;
    private final String discordId;
    private final Long lastRaid;
    private final String locale;

    public Playerdata(Playerdata value) {
        this.uuid = value.uuid;
        this.faction = value.faction;
        this.discordId = value.discordId;
        this.lastRaid = value.lastRaid;
        this.locale = value.locale;
    }

    public Playerdata(
        String uuid,
        String faction,
        String discordId,
        Long lastRaid,
        String locale
    ) {
        this.uuid = uuid;
        this.faction = faction;
        this.discordId = discordId;
        this.lastRaid = lastRaid;
        this.locale = locale;
    }

    /**
     * Getter for <code>playerData.uuid</code>.
     */
    public String getUuid() {
        return this.uuid;
    }

    /**
     * Getter for <code>playerData.faction</code>.
     */
    public String getFaction() {
        return this.faction;
    }

    /**
     * Getter for <code>playerData.discord_id</code>.
     */
    public String getDiscordId() {
        return this.discordId;
    }

    /**
     * Getter for <code>playerData.last_raid</code>.
     */
    public Long getLastRaid() {
        return this.lastRaid;
    }

    /**
     * Getter for <code>playerData.locale</code>.
     */
    public String getLocale() {
        return this.locale;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Playerdata other = (Playerdata) obj;
        if (this.uuid == null) {
            if (other.uuid != null)
                return false;
        }
        else if (!this.uuid.equals(other.uuid))
            return false;
        if (this.faction == null) {
            if (other.faction != null)
                return false;
        }
        else if (!this.faction.equals(other.faction))
            return false;
        if (this.discordId == null) {
            if (other.discordId != null)
                return false;
        }
        else if (!this.discordId.equals(other.discordId))
            return false;
        if (this.lastRaid == null) {
            if (other.lastRaid != null)
                return false;
        }
        else if (!this.lastRaid.equals(other.lastRaid))
            return false;
        if (this.locale == null) {
            if (other.locale != null)
                return false;
        }
        else if (!this.locale.equals(other.locale))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.uuid == null) ? 0 : this.uuid.hashCode());
        result = prime * result + ((this.faction == null) ? 0 : this.faction.hashCode());
        result = prime * result + ((this.discordId == null) ? 0 : this.discordId.hashCode());
        result = prime * result + ((this.lastRaid == null) ? 0 : this.lastRaid.hashCode());
        result = prime * result + ((this.locale == null) ? 0 : this.locale.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Playerdata (");

        sb.append(uuid);
        sb.append(", ").append(faction);
        sb.append(", ").append(discordId);
        sb.append(", ").append(lastRaid);
        sb.append(", ").append(locale);

        sb.append(")");
        return sb.toString();
    }
}
