/*
 * This file is generated by jOOQ.
 */
package net.skullian.skyfactions.database.tables;


import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.skullian.skyfactions.database.DefaultSchema;
import net.skullian.skyfactions.database.Keys;
import net.skullian.skyfactions.database.tables.Factionelections.FactionelectionsPath;
import net.skullian.skyfactions.database.tables.records.ElectionvotesRecord;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.InverseForeignKey;
import org.jooq.Name;
import org.jooq.Path;
import org.jooq.PlainSQL;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.SQL;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.Stringly;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Electionvotes extends TableImpl<ElectionvotesRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>electionVotes</code>
     */
    public static final Electionvotes ELECTIONVOTES = new Electionvotes();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ElectionvotesRecord> getRecordType() {
        return ElectionvotesRecord.class;
    }

    /**
     * The column <code>electionVotes.election</code>.
     */
    public final TableField<ElectionvotesRecord, Integer> ELECTION = createField(DSL.name("election"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>electionVotes.uuid</code>.
     */
    public final TableField<ElectionvotesRecord, String> UUID = createField(DSL.name("uuid"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>electionVotes.votedFor</code>.
     */
    public final TableField<ElectionvotesRecord, String> VOTEDFOR = createField(DSL.name("votedFor"), SQLDataType.CLOB, this, "");

    private Electionvotes(Name alias, Table<ElectionvotesRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private Electionvotes(Name alias, Table<ElectionvotesRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>electionVotes</code> table reference
     */
    public Electionvotes(String alias) {
        this(DSL.name(alias), ELECTIONVOTES);
    }

    /**
     * Create an aliased <code>electionVotes</code> table reference
     */
    public Electionvotes(Name alias) {
        this(alias, ELECTIONVOTES);
    }

    /**
     * Create a <code>electionVotes</code> table reference
     */
    public Electionvotes() {
        this(DSL.name("electionVotes"), null);
    }

    public <O extends Record> Electionvotes(Table<O> path, ForeignKey<O, ElectionvotesRecord> childPath, InverseForeignKey<O, ElectionvotesRecord> parentPath) {
        super(path, childPath, parentPath, ELECTIONVOTES);
    }

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    public static class ElectionvotesPath extends Electionvotes implements Path<ElectionvotesRecord> {

        private static final long serialVersionUID = 1L;
        public <O extends Record> ElectionvotesPath(Table<O> path, ForeignKey<O, ElectionvotesRecord> childPath, InverseForeignKey<O, ElectionvotesRecord> parentPath) {
            super(path, childPath, parentPath);
        }
        private ElectionvotesPath(Name alias, Table<ElectionvotesRecord> aliased) {
            super(alias, aliased);
        }

        @Override
        public ElectionvotesPath as(String alias) {
            return new ElectionvotesPath(DSL.name(alias), this);
        }

        @Override
        public ElectionvotesPath as(Name alias) {
            return new ElectionvotesPath(alias, this);
        }

        @Override
        public ElectionvotesPath as(Table<?> alias) {
            return new ElectionvotesPath(alias.getQualifiedName(), this);
        }
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public UniqueKey<ElectionvotesRecord> getPrimaryKey() {
        return Keys.ELECTIONVOTES__PK_ELECTIONVOTES;
    }

    @Override
    public List<ForeignKey<ElectionvotesRecord, ?>> getReferences() {
        return Arrays.asList(Keys.ELECTIONVOTES__FK_ELECTIONVOTES_PK_FACTIONELECTIONS);
    }

    private transient FactionelectionsPath _factionelections;

    /**
     * Get the implicit join path to the <code>factionElections</code> table.
     */
    public FactionelectionsPath factionelections() {
        if (_factionelections == null)
            _factionelections = new FactionelectionsPath(this, Keys.ELECTIONVOTES__FK_ELECTIONVOTES_PK_FACTIONELECTIONS, null);

        return _factionelections;
    }

    @Override
    public Electionvotes as(String alias) {
        return new Electionvotes(DSL.name(alias), this);
    }

    @Override
    public Electionvotes as(Name alias) {
        return new Electionvotes(alias, this);
    }

    @Override
    public Electionvotes as(Table<?> alias) {
        return new Electionvotes(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Electionvotes rename(String name) {
        return new Electionvotes(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Electionvotes rename(Name name) {
        return new Electionvotes(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Electionvotes rename(Table<?> name) {
        return new Electionvotes(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Electionvotes where(Condition condition) {
        return new Electionvotes(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Electionvotes where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Electionvotes where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Electionvotes where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Electionvotes where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Electionvotes where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Electionvotes where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Electionvotes where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Electionvotes whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Electionvotes whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
