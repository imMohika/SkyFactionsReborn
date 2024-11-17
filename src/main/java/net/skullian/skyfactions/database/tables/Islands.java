/*
 * This file is generated by jOOQ.
 */
package net.skullian.skyfactions.database.tables;


import java.util.Collection;

import net.skullian.skyfactions.database.DefaultSchema;
import net.skullian.skyfactions.database.Keys;
import net.skullian.skyfactions.database.tables.records.IslandsRecord;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.PlainSQL;
import org.jooq.QueryPart;
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
public class Islands extends TableImpl<IslandsRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>islands</code>
     */
    public static final Islands ISLANDS = new Islands();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<IslandsRecord> getRecordType() {
        return IslandsRecord.class;
    }

    /**
     * The column <code>islands.id</code>.
     */
    public final TableField<IslandsRecord, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>islands.uuid</code>.
     */
    public final TableField<IslandsRecord, String> UUID = createField(DSL.name("uuid"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>islands.level</code>.
     */
    public final TableField<IslandsRecord, Integer> LEVEL = createField(DSL.name("level"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>islands.gems</code>.
     */
    public final TableField<IslandsRecord, Integer> GEMS = createField(DSL.name("gems"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>islands.runes</code>.
     */
    public final TableField<IslandsRecord, Integer> RUNES = createField(DSL.name("runes"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>islands.defenceCount</code>.
     */
    public final TableField<IslandsRecord, Integer> DEFENCECOUNT = createField(DSL.name("defenceCount"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>islands.last_raided</code>.
     */
    public final TableField<IslandsRecord, Long> LAST_RAIDED = createField(DSL.name("last_raided"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>islands.last_raider</code>.
     */
    public final TableField<IslandsRecord, String> LAST_RAIDER = createField(DSL.name("last_raider"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>islands.created</code>.
     */
    public final TableField<IslandsRecord, Long> CREATED = createField(DSL.name("created"), SQLDataType.BIGINT.nullable(false), this, "");

    private Islands(Name alias, Table<IslandsRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private Islands(Name alias, Table<IslandsRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>islands</code> table reference
     */
    public Islands(String alias) {
        this(DSL.name(alias), ISLANDS);
    }

    /**
     * Create an aliased <code>islands</code> table reference
     */
    public Islands(Name alias) {
        this(alias, ISLANDS);
    }

    /**
     * Create a <code>islands</code> table reference
     */
    public Islands() {
        this(DSL.name("islands"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public UniqueKey<IslandsRecord> getPrimaryKey() {
        return Keys.ISLANDS__PK_ISLANDS;
    }

    @Override
    public Islands as(String alias) {
        return new Islands(DSL.name(alias), this);
    }

    @Override
    public Islands as(Name alias) {
        return new Islands(alias, this);
    }

    @Override
    public Islands as(Table<?> alias) {
        return new Islands(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Islands rename(String name) {
        return new Islands(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Islands rename(Name name) {
        return new Islands(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Islands rename(Table<?> name) {
        return new Islands(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Islands where(Condition condition) {
        return new Islands(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Islands where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Islands where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Islands where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Islands where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Islands where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Islands where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Islands where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Islands whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Islands whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
