/*
 * This file is generated by jOOQ.
 */
package net.skullian.skyfactions.database.tables.records;


import java.time.LocalDateTime;

import net.skullian.skyfactions.database.tables.FactionElections;

import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class FactionElectionsRecord extends UpdatableRecordImpl<FactionElectionsRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>faction_elections.id</code>.
     */
    public FactionElectionsRecord setId(Integer value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>faction_elections.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>faction_elections.faction_name</code>.
     */
    public FactionElectionsRecord setFactionName(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>faction_elections.faction_name</code>.
     */
    public String getFactionName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>faction_elections.end_date</code>.
     */
    public FactionElectionsRecord setEndDate(LocalDateTime value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>faction_elections.end_date</code>.
     */
    public LocalDateTime getEndDate() {
        return (LocalDateTime) get(2);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached FactionElectionsRecord
     */
    public FactionElectionsRecord() {
        super(FactionElections.FACTION_ELECTIONS);
    }

    /**
     * Create a detached, initialised FactionElectionsRecord
     */
    public FactionElectionsRecord(Integer id, String factionName, LocalDateTime endDate) {
        super(FactionElections.FACTION_ELECTIONS);

        setId(id);
        setFactionName(factionName);
        setEndDate(endDate);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised FactionElectionsRecord
     */
    public FactionElectionsRecord(net.skullian.skyfactions.database.tables.pojos.FactionElections value) {
        super(FactionElections.FACTION_ELECTIONS);

        if (value != null) {
            setId(value.getId());
            setFactionName(value.getFactionName());
            setEndDate(value.getEndDate());
            resetChangedOnNotNull();
        }
    }
}
