package net.skullian.skyfactions.database.tables.records;

import net.skullian.skyfactions.database.tables.Islands;
import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;

@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class IslandsRecord extends UpdatableRecordImpl<IslandsRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>islands.id</code>.
     */
    public IslandsRecord setId(Integer value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>islands.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>islands.uuid</code>.
     */
    public IslandsRecord setUuid(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>islands.uuid</code>.
     */
    public String getUuid() {
        return (String) get(1);
    }

    /**
     * Setter for <code>islands.level</code>.
     */
    public IslandsRecord setLevel(Integer value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>islands.level</code>.
     */
    public Integer getLevel() {
        return (Integer) get(2);
    }

    /**
     * Setter for <code>islands.gems</code>.
     */
    public IslandsRecord setGems(Integer value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>islands.gems</code>.
     */
    public Integer getGems() {
        return (Integer) get(3);
    }

    /**
     * Setter for <code>islands.runes</code>.
     */
    public IslandsRecord setRunes(Integer value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>islands.runes</code>.
     */
    public Integer getRunes() {
        return (Integer) get(4);
    }

    /**
     * Setter for <code>islands.defenceCount</code>.
     */
    public IslandsRecord setDefencecount(Integer value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>islands.defenceCount</code>.
     */
    public Integer getDefencecount() {
        return (Integer) get(5);
    }

    /**
     * Setter for <code>islands.last_raided</code>.
     */
    public IslandsRecord setLastRaided(Long value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>islands.last_raided</code>.
     */
    public Long getLastRaided() {
        return (Long) get(6);
    }

    /**
     * Setter for <code>islands.last_raider</code>.
     */
    public IslandsRecord setLastRaider(String value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>islands.last_raider</code>.
     */
    public String getLastRaider() {
        return (String) get(7);
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
     * Create a detached IslandsRecord
     */
    public IslandsRecord() {
        super(Islands.ISLANDS);
    }

    /**
     * Create a detached, initialised IslandsRecord
     */
    public IslandsRecord(Integer id, String uuid, Integer level, Integer gems, Integer runes, Integer defencecount, Long lastRaided, String lastRaider) {
        super(Islands.ISLANDS);

        setId(id);
        setUuid(uuid);
        setLevel(level);
        setGems(gems);
        setRunes(runes);
        setDefencecount(defencecount);
        setLastRaided(lastRaided);
        setLastRaider(lastRaider);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised IslandsRecord
     */
    public IslandsRecord(net.skullian.skyfactions.database.tables.pojos.Islands value) {
        super(Islands.ISLANDS);

        if (value != null) {
            setId(value.getId());
            setUuid(value.getUuid());
            setLevel(value.getLevel());
            setGems(value.getGems());
            setRunes(value.getRunes());
            setDefencecount(value.getDefencecount());
            setLastRaided(value.getLastRaided());
            setLastRaider(value.getLastRaider());
            resetChangedOnNotNull();
        }
    }
}