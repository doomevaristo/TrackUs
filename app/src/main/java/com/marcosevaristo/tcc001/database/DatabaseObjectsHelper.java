package com.marcosevaristo.tcc001.database;

import android.provider.BaseColumns;

public class DatabaseObjectsHelper {
    private DatabaseObjectsHelper(){}

    public static class TFavoritos implements BaseColumns {
        public static String TABLE_NAME = "TB_FAVORITOS";
        public static String COLUMN_LINHA = "FAV_NROLINHA";

        public static String getCreateEntry(){
            StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE ").append(TABLE_NAME);
            sb.append(" (").append(TFavoritos._ID).append("INTEGER NOT NULL PRIMARY KEY ");
            sb.append(",").append(COLUMN_LINHA).append("VARCHAR(25) NOT NULL ");
            sb.append(", FOREIGN KEY (LINHA) REFERENCES ").append(TABLE_NAME).append("(").append(COLUMN_LINHA).append(")");
            sb.append(")");
            return sb.toString();
        }
    }

    public static class TLinhas implements BaseColumns {
        public static String TABLE_NAME = "TB_LINHAS";
        public static String COLUMN_NUMERO = "LIN_NUMERO";
        public static String COLUMN_TITULO = "LIN_TITULO";
        public static String COLUMN_SUBTITULO = "LIN_SUBTITULO";
        public static String COLUMN_CIDADE = "LIN_CIDADEID";

        public static String getCreateEntry(){
            StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE ").append(TABLE_NAME);
            sb.append(" (").append(TLinhas._ID).append("INTEGER NOT NULL PRIMARY KEY ");
            sb.append(",").append(COLUMN_NUMERO).append("VARCHAR(25) NOT NULL ");
            sb.append(",").append(COLUMN_TITULO).append("VARCHAR(255) NOT NULL ");
            sb.append(",").append(COLUMN_SUBTITULO).append("VARCHAR(600) NULL ");
            sb.append(",").append(COLUMN_CIDADE).append("INTEGER NOT NULL ");
            sb.append(")");
            return sb.toString();
        }

        public static String getColunasParaSelect() {
            StringBuilder sb = new StringBuilder();
            sb.append(COLUMN_NUMERO).append(COLUMN_TITULO).append(COLUMN_SUBTITULO).append(COLUMN_CIDADE);
            return sb.toString();
        }
    }
}
