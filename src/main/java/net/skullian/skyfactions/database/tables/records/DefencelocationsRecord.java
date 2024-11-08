package net.skullian.skyfactions.database.tables.records;

import net.skullian.skyfactions.database.tables.Defencelocations;
import org.jooq.impl.TableRecordImpl;

@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class DefencelocationsRecord extends TableRecordImpl<DefencelocationsRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>defenceLocations.uuid</code>.
     */
    public DefencelocationsRecord setUuid(String value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>defenceLocations.uuid</code>.
     */
    public String getUuid() {
        return (String) get(0);
    }

    /**
     * Setter for <code>defenceLocations.type</code>.
     */
    public DefencelocationsRecord setType(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>defenceLocations.type</code>.
     */
    public String getType() {
        return (String) get(1);
    }

    /**
     * Setter for <code>defenceLocations.factionName</code>.
     */
    public DefencelocationsRecord setFactionname(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>defenceLocations.factionName</code>.
     */
    public String getFactionname() {
        return (String) get(2);
    }

    /**
     * Setter for <code>defenceLocations.x</code>.
     */
    public DefencelocationsRecord setX(Integer value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>defenceLocations.x</code>.
     */
    public Integer getX() {
        return (Integer) get(3);
    }

    /**
     * Setter for <code>defenceLocations.y</code>.
     */
    public DefencelocationsRecord setY(Integer value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>defenceLocations.y</code>.
     */
    public Integer getY() {
        return (Integer) get(4);
    }

    /**
     * Setter for <code>defenceLocations.z</code>.
     */
    public DefencelocationsRecord setZ(Integer value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>defenceLocations.z</code>.
     */
    public Integer getZ() {
        return (Integer) get(5);
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached DefencelocationsRecord
     */
    public DefencelocationsRecord() {
        super(Defencelocations.DEFENCELOCATIONS);
    }

    /**
     * Create a detached, initialised DefencelocationsRecord
     */
    public DefencelocationsRecord(String uuid, String type, String factionname, Integer x, Integer y, Integer z) {
        super(Defencelocations.DEFENCELOCATIONS);

        setUuid(uuid);
        setType(type);
        setFactionname(factionname);
        setX(x);
        setY(y);
        setZ(z);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised DefencelocationsRecord
     */
    public DefencelocationsRecord(net.skullian.skyfactions.database.tables.pojos.Defencelocations value) {
        super(Defencelocations.DEFENCELOCATIONS);

        if (value != null) {
            setUuid(value.getUuid());
            setType(value.getType());
            setFactionname(value.getFactionname());
            setX(value.getX());
            setY(value.getY());
            setZ(value.getZ());
            resetChangedOnNotNull();
        }
    }
}