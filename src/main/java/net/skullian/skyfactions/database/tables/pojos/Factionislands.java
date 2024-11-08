package net.skullian.skyfactions.database.tables.pojos;


import java.io.Serializable;


@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Factionislands implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Integer id;
    private final String factionname;
    private final Integer runes;
    private final Integer defencecount;
    private final Integer gems;
    private final Long lastRaided;
    private final String lastRaider;

    public Factionislands(Factionislands value) {
        this.id = value.id;
        this.factionname = value.factionname;
        this.runes = value.runes;
        this.defencecount = value.defencecount;
        this.gems = value.gems;
        this.lastRaided = value.lastRaided;
        this.lastRaider = value.lastRaider;
    }

    public Factionislands(
        Integer id,
        String factionname,
        Integer runes,
        Integer defencecount,
        Integer gems,
        Long lastRaided,
        String lastRaider
    ) {
        this.id = id;
        this.factionname = factionname;
        this.runes = runes;
        this.defencecount = defencecount;
        this.gems = gems;
        this.lastRaided = lastRaided;
        this.lastRaider = lastRaider;
    }

    /**
     * Getter for <code>factionIslands.id</code>.
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * Getter for <code>factionIslands.factionName</code>.
     */
    public String getFactionname() {
        return this.factionname;
    }

    /**
     * Getter for <code>factionIslands.runes</code>.
     */
    public Integer getRunes() {
        return this.runes;
    }

    /**
     * Getter for <code>factionIslands.defenceCount</code>.
     */
    public Integer getDefencecount() {
        return this.defencecount;
    }

    /**
     * Getter for <code>factionIslands.gems</code>.
     */
    public Integer getGems() {
        return this.gems;
    }

    /**
     * Getter for <code>factionIslands.last_raided</code>.
     */
    public Long getLastRaided() {
        return this.lastRaided;
    }

    /**
     * Getter for <code>factionIslands.last_raider</code>.
     */
    public String getLastRaider() {
        return this.lastRaider;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Factionislands other = (Factionislands) obj;
        if (this.id == null) {
            if (other.id != null)
                return false;
        }
        else if (!this.id.equals(other.id))
            return false;
        if (this.factionname == null) {
            if (other.factionname != null)
                return false;
        }
        else if (!this.factionname.equals(other.factionname))
            return false;
        if (this.runes == null) {
            if (other.runes != null)
                return false;
        }
        else if (!this.runes.equals(other.runes))
            return false;
        if (this.defencecount == null) {
            if (other.defencecount != null)
                return false;
        }
        else if (!this.defencecount.equals(other.defencecount))
            return false;
        if (this.gems == null) {
            if (other.gems != null)
                return false;
        }
        else if (!this.gems.equals(other.gems))
            return false;
        if (this.lastRaided == null) {
            if (other.lastRaided != null)
                return false;
        }
        else if (!this.lastRaided.equals(other.lastRaided))
            return false;
        if (this.lastRaider == null) {
            if (other.lastRaider != null)
                return false;
        }
        else if (!this.lastRaider.equals(other.lastRaider))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.factionname == null) ? 0 : this.factionname.hashCode());
        result = prime * result + ((this.runes == null) ? 0 : this.runes.hashCode());
        result = prime * result + ((this.defencecount == null) ? 0 : this.defencecount.hashCode());
        result = prime * result + ((this.gems == null) ? 0 : this.gems.hashCode());
        result = prime * result + ((this.lastRaided == null) ? 0 : this.lastRaided.hashCode());
        result = prime * result + ((this.lastRaider == null) ? 0 : this.lastRaider.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Factionislands (");

        sb.append(id);
        sb.append(", ").append(factionname);
        sb.append(", ").append(runes);
        sb.append(", ").append(defencecount);
        sb.append(", ").append(gems);
        sb.append(", ").append(lastRaided);
        sb.append(", ").append(lastRaider);

        sb.append(")");
        return sb.toString();
    }
}
