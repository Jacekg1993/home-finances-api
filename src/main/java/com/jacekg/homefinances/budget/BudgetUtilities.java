package com.jacekg.homefinances.budget;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.jacekg.homefinances.expenses.model.ConstantExpense;
import com.jacekg.homefinances.expenses.model.Expense;

public class BudgetUtilities {
	
//	public static List<ConstantExpense> removeDuplicatedConstantExpenses(List<ConstantExpense> currentConstantExpenses,
//			List<ConstantExpense> updatedConstantExpenses) {
//
//		ConstantExpense currentConstantExpense;
//		ConstantExpense updatedConstantExpense;
//
//		for (int i = 0; i < updatedConstantExpenses.size(); i++) {
//
//			updatedConstantExpense = updatedConstantExpenses.get(i);
//
//			for (int j = 0; j < currentConstantExpenses.size(); j++) {
//
//				currentConstantExpense = currentConstantExpenses.get(j);
//
//				if (updatedConstantExpense.getId() != (currentConstantExpense.getId())
//						&& updatedConstantExpense.getName().equals(currentConstantExpense.getName())) {
//					updatedConstantExpenses.remove(i);
//				}
//			}
//		}
//
//		return updatedConstantExpenses;
//	}
	
	public static <T> List<T> removeDuplicatedExpenses(List<T> currentExpenses,
			List<T> updatedExpenses) {
		
		System.out.println("currentExpense: " + currentExpenses);
		System.out.println("updatedExpense: " + updatedExpenses);
		
		T currentExpense;
		T updatedExpense;

		for (int i = 0; i < updatedExpenses.size(); i++) {

			updatedExpense = updatedExpenses.get(i);
			System.out.println("updated:" + updatedExpense.toString());
			
			for (int j = 0; j < currentExpenses.size(); j++) {

				currentExpense = currentExpenses.get(j);
				System.out.println("current: " + currentExpense);
				
				if (((Expense) updatedExpense).getId() != (((Expense) currentExpense).getId())
						&& ((Expense) updatedExpense).getName().equals(((Expense) currentExpense).getName())) {
					updatedExpenses.remove(i);
				}
			}
		}

		return updatedExpenses;
	}
	
//	public static List<Long> findConstantExpensesIdToRemove
//		(Collection<ConstantExpense> currentConstantExpenses,
//		Collection<ConstantExpense> updatedConstantExpenses) {
//
//		List<Long> constantExpensesIdToRemove = new ArrayList<>();
//
//		for (ConstantExpense constantExpense : currentConstantExpenses) {
//			
//			ConstantExpense searchedConstantExpense = updatedConstantExpenses
//					.stream()
//					.filter(
//					updatedConstantExpense -> constantExpense.getName().equals(updatedConstantExpense.getName()))
//					.findFirst().orElse(null);
//
//			if (searchedConstantExpense == null) {
//				constantExpensesIdToRemove.add(constantExpense.getId());
//			}
//		}
//
//		return constantExpensesIdToRemove;
//	}
	
	public static <T> List<Long> findExpensesIdsToRemove(
			Collection<T> currentExpenses, Collection<T> updatedExpenses) {
		
//		System.out.println("currentExpense: " + currentExpenses);
//		System.out.println("updatedExpense: " + updatedExpenses);
		
		List<Long> constantExpensesIdToRemove = new ArrayList<>();

		for (T expense : currentExpenses) {

			T searchedExpense = updatedExpenses.stream()
					.filter(updatedExpense -> ((Expense) expense).getName()
							.equals(((Expense) updatedExpense).getName()))
					.findFirst().orElse(null);

			if (searchedExpense == null) {
				constantExpensesIdToRemove.add(((Expense) expense).getId());
			}
		}

		return constantExpensesIdToRemove;
	}
	
}
