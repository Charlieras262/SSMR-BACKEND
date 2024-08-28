package com.g5.ssmr.utils;

public class Catalog {
    public static final class UserStatus {
        public static final int ACTIVE = 1;
        public static final int INACTIVE = 2;
        public static final int FIRST_LOGIN = 3;
        public static final int DELETED = 4;
    }

    public static final class UserRole {
        public static final int ROOT = 5;
        public static final int ADMIN = 6;
        public static final int AUDITOR = 7;
        public static final int INSTITUTION = 8;
    }

    public static final class AuditStatus {
        public static final int CREATED = 9;
        public static final int IN_PROCESS = 10;
        public static final int FINISHED = 11;
    }

    public static final class ResultStatus {
        public static final int SUCCEED = 12;
        public static final int FAIL = 13;
    }
}
