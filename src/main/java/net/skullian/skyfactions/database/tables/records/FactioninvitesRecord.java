/*
 * This file is generated by jOOQ.
 */
package net.skullian.skyfactions.database.tables.records;


import net.skullian.skyfactions.database.tables.Factioninvites;

import org.jooq.impl.TableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class FactioninvitesRecord extends TableRecordImpl<FactioninvitesRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>factionInvites.factionName</code>.
     */
    public FactioninvitesRecord setFactionname(String value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>factionInvites.factionName</code>.
     */
    public String getFactionname() {
        return (String) get(0);
    }

    /**
     * Setter for <code>factionInvites.uuid</code>.
     */
    public FactioninvitesRecord setUuid(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>factionInvites.uuid</code>.
     */
    public String getUuid() {
        return (String) get(1);
    }

    /**
     * Setter for <code>factionInvites.inviter</code>.
     */
    public FactioninvitesRecord setInviter(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>factionInvites.inviter</code>.
     */
    public String getInviter() {
        return (String) get(2);
    }

    /**
     * Setter for <code>factionInvites.type</code>.
     */
    public FactioninvitesRecord setType(String value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>factionInvites.type</code>.
     */
    public String getType() {
        return (String) get(3);
    }

    /**
     * Setter for <code>factionInvites.timestamp</code>.
     */
    public FactioninvitesRecord setTimestamp(Long value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>factionInvites.timestamp</code>.
     */
    public Long getTimestamp() {
        return (Long) get(4);
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached FactioninvitesRecord
     */
    public FactioninvitesRecord() {
        super(Factioninvites.FACTIONINVITES);
    }

    /**
     * Create a detached, initialised FactioninvitesRecord
     */
    public FactioninvitesRecord(String factionname, String uuid, String inviter, String type, Long timestamp) {
        super(Factioninvites.FACTIONINVITES);

        setFactionname(factionname);
        setUuid(uuid);
        setInviter(inviter);
        setType(type);
        setTimestamp(timestamp);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised FactioninvitesRecord
     */
    public FactioninvitesRecord(net.skullian.skyfactions.database.tables.pojos.Factioninvites value) {
        super(Factioninvites.FACTIONINVITES);

        if (value != null) {
            setFactionname(value.getFactionname());
            setUuid(value.getUuid());
            setInviter(value.getInviter());
            setType(value.getType());
            setTimestamp(value.getTimestamp());
            resetChangedOnNotNull();
        }
    }
}
