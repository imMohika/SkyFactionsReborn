package net.skullian.skyfactions.database.tables.pojos;

import java.io.Serializable;

@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Defencelocations implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String uuid;
    private final String type;
    private final String factionname;
    private final Integer x;
    private final Integer y;
    private final Integer z;

    public Defencelocations(Defencelocations value) {
        this.uuid = value.uuid;
        this.type = value.type;
        this.factionname = value.factionname;
        this.x = value.x;
        this.y = value.y;
        this.z = value.z;
    }

    public Defencelocations(
        String uuid,
        String type,
        String factionname,
        Integer x,
        Integer y,
        Integer z
    ) {
        this.uuid = uuid;
        this.type = type;
        this.factionname = factionname;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Getter for <code>defenceLocations.uuid</code>.
     */
    public String getUuid() {
        return this.uuid;
    }

    /**
     * Getter for <code>defenceLocations.type</code>.
     */
    public String getType() {
        return this.type;
    }

    /**
     * Getter for <code>defenceLocations.factionName</code>.
     */
    public String getFactionname() {
        return this.factionname;
    }

    /**
     * Getter for <code>defenceLocations.x</code>.
     */
    public Integer getX() {
        return this.x;
    }

    /**
     * Getter for <code>defenceLocations.y</code>.
     */
    public Integer getY() {
        return this.y;
    }

    /**
     * Getter for <code>defenceLocations.z</code>.
     */
    public Integer getZ() {
        return this.z;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Defencelocations other = (Defencelocations) obj;
        if (this.uuid == null) {
            if (other.uuid != null)
                return false;
        }
        else if (!this.uuid.equals(other.uuid))
            return false;
        if (this.type == null) {
            if (other.type != null)
                return false;
        }
        else if (!this.type.equals(other.type))
            return false;
        if (this.factionname == null) {
            if (other.factionname != null)
                return false;
        }
        else if (!this.factionname.equals(other.factionname))
            return false;
        if (this.x == null) {
            if (other.x != null)
                return false;
        }
        else if (!this.x.equals(other.x))
            return false;
        if (this.y == null) {
            if (other.y != null)
                return false;
        }
        else if (!this.y.equals(other.y))
            return false;
        if (this.z == null) {
            if (other.z != null)
                return false;
        }
        else if (!this.z.equals(other.z))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.uuid == null) ? 0 : this.uuid.hashCode());
        result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
        result = prime * result + ((this.factionname == null) ? 0 : this.factionname.hashCode());
        result = prime * result + ((this.x == null) ? 0 : this.x.hashCode());
        result = prime * result + ((this.y == null) ? 0 : this.y.hashCode());
        result = prime * result + ((this.z == null) ? 0 : this.z.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Defencelocations (");

        sb.append(uuid);
        sb.append(", ").append(type);
        sb.append(", ").append(factionname);
        sb.append(", ").append(x);
        sb.append(", ").append(y);
        sb.append(", ").append(z);

        sb.append(")");
        return sb.toString();
    }
}
