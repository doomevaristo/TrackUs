package com.marcosevaristo.tcc001.database;

import android.provider.BaseColumns;

public class SQLiteObjectsHelper {
    private SQLiteObjectsHelper(){}

    public static class TLinhasFavoritas implements BaseColumns, OperacoesComColunas {
        public static String TABLE_NAME = "TB_LINHAS_FAVORITAS";
        public static final String COLUMN_IDFIREBASE = "LIN_IDFIREBASE";
        public static String COLUMN_NUMERO = "LIN_NUMERO";
        public static String COLUMN_TITULO = "LIN_TITULO";
        public static String COLUMN_SUBTITULO = "LIN_SUBTITULO";
        public static String COLUMN_MUNICIPIOID = "LIN_MUNIDFIREBASE";
        public static String COLUMN_MUNICIPIONOME = "LIN_MUNNOME";

        private static TLinhasFavoritas instance;

        public static TLinhasFavoritas getInstance() {
            if(instance == null) {
                instance = new TLinhasFavoritas();
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
            sb.append(",").append(COLUMN_MUNICIPIOID).append(" VARCHAR(255) NOT NULL ");
            sb.append(",").append(COLUMN_MUNICIPIONOME).append(" VARCHAR(255) NOT NULL ");
            sb.append(");");
            return sb.toString();
        }

        @Override
        public String getColunasParaSelect() {
            StringBuilder sb = new StringBuilder();
            sb.append("LIN.").append(_ID).append(", ").append(COLUMN_IDFIREBASE).append(", ").append(COLUMN_NUMERO).append(", ")
                    .append(COLUMN_TITULO).append(", ").append(COLUMN_SUBTITULO).append(", ").append(COLUMN_MUNICIPIOID).append(", ").append(COLUMN_MUNICIPIONOME);
            return sb.toString();
        }
    }

    public static class TMunicipioAtual implements BaseColumns, OperacoesComColunas {
        public static final String TABLE_NAME = "TB_MUNICIPIO_ATUAL";
        public static final String COLUMN_IDFIREBASE = "MUN_IDFIREBASE";
        public static final String COLUMN_MUNNOME = "MUN_MUNNOME";

        private static TMunicipioAtual instance;

        public static TMunicipioAtual getInstance() {
            if(instance == null) {
                instance = new TMunicipioAtual();
            }
            return instance;
        }

        @Override
        public String getColunasParaSelect() {
            StringBuilder sb = new StringBuilder();
            sb.append("MUN.").append(_ID).append(", ").append(COLUMN_IDFIREBASE).append(", ").append(COLUMN_MUNNOME);
            return sb.toString();
        }

        @Override
        public String getCreateEntry() {
            StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE ").append(TABLE_NAME);
            sb.append(" (").append(_ID).append(" INTEGER NOT NULL PRIMARY KEY ");
            sb.append(", ").append(COLUMN_IDFIREBASE).append(" VARCHAR(255) NOT NULL ");
            sb.append(", ").append(COLUMN_MUNNOME).append(" VARCHAR(255) NOT NULL ");
            sb.append(");");
            return sb.toString();
        }
    }
}
