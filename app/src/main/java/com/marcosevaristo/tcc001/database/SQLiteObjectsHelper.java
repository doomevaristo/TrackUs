package com.marcosevaristo.tcc001.database;

import android.provider.BaseColumns;

public class SQLiteObjectsHelper {
    private SQLiteObjectsHelper(){}

    public static class TLinhas implements BaseColumns, OperacoesComColunas {
        public static String TABLE_NAME = "TB_LINHAS";
        public static final String COLUMN_IDFIREBASE = "LIN_IDFIREBASE";
        public static String COLUMN_NUMERO = "LIN_NUMERO";
        public static String COLUMN_TITULO = "LIN_TITULO";
        public static String COLUMN_SUBTITULO = "LIN_SUBTITULO";
        public static String COLUMN_MUNICIPIO = "LIN_MUNID";
        public static String COLUMN_EHFAVORITA = "LIN_EHFAVORITA";

        private static TLinhas instance;

        public static TLinhas getInstance() {
            if(instance == null) {
                instance = new TLinhas();
            }
            return instance;
        }

        @Override
        public String getCreateEntry(){
            StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE ").append(TABLE_NAME);
            sb.append(" (").append(_ID).append(" INTEGER NOT NULL PRIMARY KEY ");
            sb.append(",").append(COLUMN_IDFIREBASE).append(" VARCHAR(255) NOT NULL ");
            sb.append(",").append(COLUMN_NUMERO).append(" VARCHAR(25) NOT NULL ");
            sb.append(",").append(COLUMN_TITULO).append(" VARCHAR(255) NOT NULL ");
            sb.append(",").append(COLUMN_SUBTITULO).append(" VARCHAR(600) NULL ");
            sb.append(",").append(COLUMN_MUNICIPIO).append(" INTEGER NULL ");
            sb.append(",").append(COLUMN_EHFAVORITA).append(" INTEGER NOT NULL DEFAULT 0 ");
            sb.append(", FOREIGN KEY (").append(COLUMN_MUNICIPIO).append(") REFERENCES ").append(TMunicipios.TABLE_NAME).append("(")
                    .append(TMunicipios._ID).append(") ON DELETE CASCADE");
            sb.append(");");
            return sb.toString();
        }

        @Override
        public String getColunasParaSelect() {
            StringBuilder sb = new StringBuilder();
            sb.append("LIN.").append(_ID).append(", ").append(COLUMN_IDFIREBASE).append(", ").append(COLUMN_NUMERO).append(", ")
                    .append(COLUMN_TITULO).append(", ").append(COLUMN_SUBTITULO).append(", ").append(COLUMN_MUNICIPIO)
                    .append(", ").append(COLUMN_EHFAVORITA);
            return sb.toString();
        }
    }

    public static class TMunicipios implements BaseColumns, OperacoesComColunas {
        public static final String TABLE_NAME = "TB_MUNICIPIOS";
        public static final String COLUMN_IDFIREBASE = "MUN_IDFIREBASE";
        public static final String COLUMN_MUNNOME = "MUN_MUNNOME";
        public static final String COLUMN_EHMUNICIPIOATUAL = "MUN_EHMUNATUAL";

        private static TMunicipios instance;

        public static TMunicipios getInstance() {
            if(instance == null) {
                instance = new TMunicipios();
            }
            return instance;
        }

        @Override
        public String getColunasParaSelect() {
            StringBuilder sb = new StringBuilder();
            sb.append("MUN.").append(_ID).append(", ").append(COLUMN_IDFIREBASE).append(", ").append(COLUMN_MUNNOME).append(", ").append(COLUMN_EHMUNICIPIOATUAL);
            return sb.toString();
        }

        @Override
        public String getCreateEntry() {
            StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE ").append(TABLE_NAME);
            sb.append(" (").append(_ID).append(" INTEGER NOT NULL PRIMARY KEY ");
            sb.append(", ").append(COLUMN_IDFIREBASE).append(" VARCHAR(255) NOT NULL ");
            sb.append(", ").append(COLUMN_MUNNOME).append(" VARCHAR(255) NOT NULL ");
            sb.append(", ").append(COLUMN_EHMUNICIPIOATUAL).append(" INTEGER NOT NULL DEFAULT 0 ");
            sb.append(");");
            return sb.toString();
        }
    }
}
