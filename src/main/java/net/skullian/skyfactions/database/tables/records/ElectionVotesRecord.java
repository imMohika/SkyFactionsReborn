/*
 * This file is generated by jOOQ.
 */
package net.skullian.skyfactions.database.tables.records;


import net.skullian.skyfactions.database.tables.Electionvotes;

import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class ElectionvotesRecord extends UpdatableRecordImpl<ElectionvotesRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>electionVotes.election</code>.
     */
    public ElectionvotesRecord setElection(Integer value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>electionVotes.election</code>.
     */
    public Integer getElection() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>electionVotes.uuid</code>.
     */
    public ElectionvotesRecord setUuid(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>electionVotes.uuid</code>.
     */
    public String getUuid() {
        return (String) get(1);
    }

    /**
     * Setter for <code>electionVotes.votedFor</code>.
     */
    public ElectionvotesRecord setVotedfor(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>electionVotes.votedFor</code>.
     */
    public String getVotedfor() {
        return (String) get(2);
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
     * Create a detached ElectionvotesRecord
     */
    public ElectionvotesRecord() {
        super(Electionvotes.ELECTIONVOTES);
    }

    /**
     * Create a detached, initialised ElectionvotesRecord
     */
    public ElectionvotesRecord(Integer election, String uuid, String votedfor) {
        super(Electionvotes.ELECTIONVOTES);

        setElection(election);
        setUuid(uuid);
        setVotedfor(votedfor);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised ElectionvotesRecord
     */
    public ElectionvotesRecord(net.skullian.skyfactions.database.tables.pojos.Electionvotes value) {
        super(Electionvotes.ELECTIONVOTES);

        if (value != null) {
            setElection(value.getElection());
            setUuid(value.getUuid());
            setVotedfor(value.getVotedfor());
            resetChangedOnNotNull();
        }
    }
}
