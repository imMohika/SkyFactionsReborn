package net.skullian.skyfactions.database.tables;

import net.skullian.skyfactions.database.DefaultSchema;
import net.skullian.skyfactions.database.tables.records.AuditlogsRecord;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import java.util.Collection;

@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Auditlogs extends TableImpl<AuditlogsRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>auditLogs</code>
     */
    public static final Auditlogs AUDITLOGS = new Auditlogs();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<AuditlogsRecord> getRecordType() {
        return AuditlogsRecord.class;
    }

    /**
     * The column <code>auditLogs.factionName</code>.
     */
    public final TableField<AuditlogsRecord, String> FACTIONNAME = createField(DSL.name("factionName"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>auditLogs.type</code>.
     */
    public final TableField<AuditlogsRecord, String> TYPE = createField(DSL.name("type"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>auditLogs.uuid</code>.
     */
    public final TableField<AuditlogsRecord, String> UUID = createField(DSL.name("uuid"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>auditLogs.description</code>.
     */
    public final TableField<AuditlogsRecord, String> REPLACEMENTS = createField(DSL.name("replacements"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>auditLogs.timestamp</code>.
     */
    public final TableField<AuditlogsRecord, Long> TIMESTAMP = createField(DSL.name("timestamp"), SQLDataType.BIGINT.nullable(false), this, "");

    private Auditlogs(Name alias, Table<AuditlogsRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private Auditlogs(Name alias, Table<AuditlogsRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>auditLogs</code> table reference
     */
    public Auditlogs(String alias) {
        this(DSL.name(alias), AUDITLOGS);
    }

    /**
     * Create an aliased <code>auditLogs</code> table reference
     */
    public Auditlogs(Name alias) {
        this(alias, AUDITLOGS);
    }

    /**
     * Create a <code>auditLogs</code> table reference
     */
    public Auditlogs() {
        this(DSL.name("auditLogs"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public Auditlogs as(String alias) {
        return new Auditlogs(DSL.name(alias), this);
    }

    @Override
    public Auditlogs as(Name alias) {
        return new Auditlogs(alias, this);
    }

    @Override
    public Auditlogs as(Table<?> alias) {
        return new Auditlogs(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Auditlogs rename(String name) {
        return new Auditlogs(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Auditlogs rename(Name name) {
        return new Auditlogs(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Auditlogs rename(Table<?> name) {
        return new Auditlogs(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Auditlogs where(Condition condition) {
        return new Auditlogs(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Auditlogs where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Auditlogs where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Auditlogs where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Auditlogs where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Auditlogs where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Auditlogs where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Auditlogs where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Auditlogs whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Auditlogs whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
