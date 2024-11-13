package net.skullian.skyfactions.database.tables;

import net.skullian.skyfactions.database.DefaultSchema;
import net.skullian.skyfactions.database.Keys;
import net.skullian.skyfactions.database.tables.records.FactionsRecord;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import java.util.Collection;

@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Factions extends TableImpl<FactionsRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>factions</code>
     */
    public static final Factions FACTIONS = new Factions();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<FactionsRecord> getRecordType() {
        return FactionsRecord.class;
    }

    /**
     * The column <code>factions.name</code>.
     */
    public final TableField<FactionsRecord, String> NAME = createField(DSL.name("name"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>factions.motd</code>.
     */
    public final TableField<FactionsRecord, String> MOTD = createField(DSL.name("motd"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>factions.level</code>.
     */
    public final TableField<FactionsRecord, Integer> LEVEL = createField(DSL.name("level"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>factions.last_raid</code>.
     */
    public final TableField<FactionsRecord, Long> LAST_RAID = createField(DSL.name("last_raid"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>factions.locale</code>.
     */
    public final TableField<FactionsRecord, String> LOCALE = createField(DSL.name("locale"), SQLDataType.CLOB.nullable(false), this, "");

    private Factions(Name alias, Table<FactionsRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private Factions(Name alias, Table<FactionsRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>factions</code> table reference
     */
    public Factions(String alias) {
        this(DSL.name(alias), FACTIONS);
    }

    /**
     * Create an aliased <code>factions</code> table reference
     */
    public Factions(Name alias) {
        this(alias, FACTIONS);
    }

    /**
     * Create a <code>factions</code> table reference
     */
    public Factions() {
        this(DSL.name("factions"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public UniqueKey<FactionsRecord> getPrimaryKey() {
        return Keys.FACTIONS__PK_FACTIONS;
    }

    @Override
    public Factions as(String alias) {
        return new Factions(DSL.name(alias), this);
    }

    @Override
    public Factions as(Name alias) {
        return new Factions(alias, this);
    }

    @Override
    public Factions as(Table<?> alias) {
        return new Factions(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Factions rename(String name) {
        return new Factions(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Factions rename(Name name) {
        return new Factions(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Factions rename(Table<?> name) {
        return new Factions(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Factions where(Condition condition) {
        return new Factions(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Factions where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Factions where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Factions where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Factions where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Factions where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Factions where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Factions where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Factions whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Factions whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
