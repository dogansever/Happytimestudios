package com.sever.main;

import java.util.regex.Pattern;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.Patterns;

public class GenaralUtil {

	public static String getEmail(Context context) {
		String email = "";
		Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
		Account[] accounts = AccountManager.get(context).getAccountsByType("com.google");
		for (Account account : accounts) {
			if (emailPattern.matcher(account.name).matches()) {
				email = account.name;
				break;
			}
		}
		System.out.println("getEmail:" + email);
		return email;
	}
}
