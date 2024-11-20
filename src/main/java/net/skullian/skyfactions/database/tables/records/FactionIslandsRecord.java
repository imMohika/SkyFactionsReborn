/*
 * This file is generated by jOOQ.
 */
package net.skullian.skyfactions.database.tables.records;


import net.skullian.skyfactions.database.tables.FactionIslands;

import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class FactionIslandsRecord extends UpdatableRecordImpl<FactionIslandsRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>faction_islands.id</code>.
     */
    public FactionIslandsRecord setId(Integer value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>faction_islands.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>faction_islands.factionName</code>.
     */
    public FactionIslandsRecord setFactionname(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>faction_islands.factionName</code>.
     */
    public String getFactionname() {
        return (String) get(1);
    }

    /**
     * Setter for <code>faction_islands.runes</code>.
     */
    public FactionIslandsRecord setRunes(Integer value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>faction_islands.runes</code>.
     */
    public Integer getRunes() {
        return (Integer) get(2);
    }

    /**
     * Setter for <code>faction_islands.defenceCount</code>.
     */
    public FactionIslandsRecord setDefencecount(Integer value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>faction_islands.defenceCount</code>.
     */
    public Integer getDefencecount() {
        return (Integer) get(3);
    }

    /**
     * Setter for <code>faction_islands.gems</code>.
     */
    public FactionIslandsRecord setGems(Integer value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>faction_islands.gems</code>.
     */
    public Integer getGems() {
        return (Integer) get(4);
    }

    /**
     * Setter for <code>faction_islands.last_raided</code>.
     */
    public FactionIslandsRecord setLastRaided(Long value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>faction_islands.last_raided</code>.
     */
    public Long getLastRaided() {
        return (Long) get(5);
    }

    /**
     * Setter for <code>faction_islands.last_raider</code>.
     */
    public FactionIslandsRecord setLastRaider(String value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>faction_islands.last_raider</code>.
     */
    public String getLastRaider() {
        return (String) get(6);
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
     * Create a detached FactionIslandsRecord
     */
    public FactionIslandsRecord() {
        super(FactionIslands.FACTION_ISLANDS);
    }

    /**
     * Create a detached, initialised FactionIslandsRecord
     */
    public FactionIslandsRecord(Integer id, String factionname, Integer runes, Integer defencecount, Integer gems, Long lastRaided, String lastRaider) {
        super(FactionIslands.FACTION_ISLANDS);

        setId(id);
        setFactionname(factionname);
        setRunes(runes);
        setDefencecount(defencecount);
        setGems(gems);
        setLastRaided(lastRaided);
        setLastRaider(lastRaider);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised FactionIslandsRecord
     */
    public FactionIslandsRecord(net.skullian.skyfactions.database.tables.pojos.FactionIslands value) {
        super(FactionIslands.FACTION_ISLANDS);

        if (value != null) {
            setId(value.getId());
            setFactionname(value.getFactionname());
            setRunes(value.getRunes());
            setDefencecount(value.getDefencecount());
            setGems(value.getGems());
            setLastRaided(value.getLastRaided());
            setLastRaider(value.getLastRaider());
            resetChangedOnNotNull();
        }
    }
}
