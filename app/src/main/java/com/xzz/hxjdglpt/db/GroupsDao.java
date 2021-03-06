package com.xzz.hxjdglpt.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;


// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table GROUPS.
 */
public class GroupsDao extends AbstractDao<Groups, String> {

    public static final String TABLENAME = "GROUPS";

    /**
     * Properties of entity Groups.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property GroupsId = new Property(0, String.class, "id", true,
                "GROUPID");
        public final static Property Name = new Property(1, String.class, "name", false,
                "GROUP_NAME");
        public final static Property GCreator = new Property(2, String.class, "creator", false,
                "GROUP_CREATOR");
        public final static Property PortraitUri = new Property(3, String.class, "portraitUri",
                false, "GROUP_IMAGE_URI");

    }


    public GroupsDao(DaoConfig config) {
        super(config);
    }

    public GroupsDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /**
     * Creates the underlying database table.
     */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists ? "IF NOT EXISTS " : "";
        db.execSQL("CREATE TABLE " + constraint + "'GROUPS' (" + //
                "'GROUPID' TEXT PRIMARY KEY NOT NULL ," + // 0: groupsId
                "'GROUP_NAME' TEXT," + // 1: name
                "'GROUP_CREATOR' TEXT," + // 1: creator
                "'GROUP_IMAGE_URI' TEXT);");
        // Add Indexes
//        db.execSQL("CREATE INDEX " + constraint + "IDX_GROUPS_NAME_NAME_SPELLING ON GROUPS" + " "
//                + "(NAME,NAME_SPELLING);");
    }

    /**
     * Drops the underlying database table.
     */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'GROUPS'";
        db.execSQL(sql);
    }

    /**
     * @inheritdoc
     */
    @Override
    protected void bindValues(SQLiteStatement stmt, Groups entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getId());

        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
        String creator = entity.getCreator();
        if (creator != null) {
            stmt.bindString(3, creator);
        }
        String portraitUri = entity.getPortraitUri();
        if (portraitUri != null) {
            stmt.bindString(4, portraitUri);
        }

    }

    /**
     * @inheritdoc
     */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.getString(offset + 0);
    }

    /**
     * @inheritdoc
     */
    @Override
    public Groups readEntity(Cursor cursor, int offset) {
        Groups entity = new Groups( //
                cursor.getString(offset + 0), // groupsId
                cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
                cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // creator
                cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // portraitUri
        );
        return entity;
    }

    /**
     * @inheritdoc
     */
    @Override
    public void readEntity(Cursor cursor, Groups entity, int offset) {
        entity.setId(cursor.getString(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setCreator(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setPortraitUri(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
    }

    /**
     * @inheritdoc
     */
    @Override
    protected String updateKeyAfterInsert(Groups entity, long rowId) {
        return entity.getId();
    }

    /**
     * @inheritdoc
     */
    @Override
    public String getKey(Groups entity) {
        if (entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /**
     * @inheritdoc
     */
    @Override
    protected boolean isEntityUpdateable() {
        return true;
    }

}
