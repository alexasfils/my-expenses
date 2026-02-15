package com.alex.myexpenses.utility;

public final class AppConstants {
	
	private AppConstants() {}
	
	public static final class Entity {
        public static final String EXPENSE = "Expense ";
        public static final String EXPENSE_LIST = "ExpenseList ";
        public static final String CATEGORY = "Category ";
        public static final String USER = "User ";
    }
	
	public static final class Field {
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String EMAIL = "email";
    }
	
	public static final class Defaults {
        public static final String CATEGORY_OTHERS = "Others";
    }
	
	public static final class ExceptionMessage {
		public static final String INVALID_CREDENTIALS = "Email or password is not valid.";
		public static final String DEMO_LIMIT_REACHED = "You can create only one ExpenseList in demo mode";
    }

}
