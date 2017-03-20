package com.aclass.edx.helloworld.data.models;

import static com.aclass.edx.helloworld.data.contracts.MediaContract.ContentEntry;
import static com.aclass.edx.helloworld.data.contracts.MediaContract.MediaEntry;
import static com.aclass.edx.helloworld.data.contracts.MediaContract.ModuleEntry;

/**
 * Created by tictocproject on 14/03/2017.
 */

public class DBQueries {

    // Make these constants local. These will not be used frequently.

    private static final String INT_PK_AUTOINCREMENT = " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL";
    private static final String TEXT_NOT_NULL = " TEXT NOT NULL";
    private static final String INT_NOT_NULL = " INTEGER NOT NULL";
    private static final String CREATE_TABLE = "CREATE TABLE ";
    private static final String FK_START = "FOREIGN KEY (";
    private static final String FK_REFERENCES = ") REFERENCES ";
    private static final String FK_END = ")";
    private static final String COMMA = ",";

    public static final String CREATE_TABLE_MEDIA = CREATE_TABLE + MediaEntry.TABLE_NAME + " (" +
            MediaEntry._ID + INT_PK_AUTOINCREMENT + COMMA +
            MediaEntry.COLUMN_NAME_TITLE + TEXT_NOT_NULL + COMMA +
            MediaEntry.COLUMN_NAME_FILENAME + TEXT_NOT_NULL + COMMA +
            MediaEntry.COLUMN_NAME_TYPE + INT_NOT_NULL + ");";

    public static final String CREATE_TABLE_MODULE = CREATE_TABLE + ModuleEntry.TABLE_NAME + " (" +
            ModuleEntry._ID + INT_PK_AUTOINCREMENT + COMMA +
            ModuleEntry.COLUMN_NAME_TITLE + TEXT_NOT_NULL + ");";

    public static final String CREATE_TABLE_CONTENT = CREATE_TABLE + ContentEntry.TABLE_NAME + " (" +
            ContentEntry._ID + INT_PK_AUTOINCREMENT + COMMA +
            ContentEntry.COLUMN_NAME_MODULE_ID + INT_NOT_NULL + COMMA +
            ContentEntry.COLUMN_NAME_TYPE + INT_NOT_NULL + COMMA +
            ContentEntry.COLUMN_NAME_CONTENT_ID + INT_NOT_NULL + COMMA +
            FK_START + ContentEntry.COLUMN_NAME_MODULE_ID + FK_REFERENCES +
            ModuleEntry.TABLE_NAME + "(" + ModuleEntry._ID + FK_END +");";

    private static final String DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS %s;\n";

    public static String getDropTablesQuery() {
        String[] tableNames = {MediaEntry.TABLE_NAME, ModuleEntry.TABLE_NAME, ContentEntry.TABLE_NAME};
        StringBuilder query = new StringBuilder();

        for (String tableName : tableNames) {
            String deleteTableQuery = String.format(DROP_TABLE_IF_EXISTS, tableName);
            query.append(deleteTableQuery);
        }

        return query.toString();
    }
}
