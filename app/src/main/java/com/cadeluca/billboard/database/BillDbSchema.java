package com.cadeluca.billboard.database;

public class BillDbSchema {
    public static final class BillTable {
        public static final String NAME = "bills";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String AMOUNT = "amount";
            public static final String PAID = "paid";
        }
    }
}
