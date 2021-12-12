package com.jacekg.homefinances.budget;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.jacekg.homefinances.expenses.ConstantExpenseRepository;
import com.jacekg.homefinances.expenses.model.ConstantExpense;
import com.jacekg.homefinances.expenses.model.ConstantExpenseDTO;
import com.jacekg.homefinances.expenses.model.OneTimeExpense;
import com.jacekg.homefinances.expenses.model.UserPreferenceConstantExpense;
import com.jacekg.homefinances.user.User;
import com.jacekg.homefinances.user.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BudgetServiceImpl implements BudgetService {

	private MonthlyBudgetRepository monthlyBudgetRepository;

	private UserRepository userRepository;
	
	private ConstantExpenseRepository constantExpenseRepository;

	private ModelMapper modelMapper;

	@Override
	@Transactional
	public MonthlyBudgetDTO findByUserIdAndDate(Long userId, LocalDate date) {
		
		System.out.println("get budget");
		MonthlyBudget monthlyBudget = monthlyBudgetRepository.findByUserIdAndDate(userId, date);
		if (monthlyBudget == null) {
			return null;
		} else {
			return modelMapper.map(monthlyBudget, MonthlyBudgetDTO.class);
		}
	}

	@Override
	@Transactional
	public MonthlyBudgetDTO save(MonthlyBudgetDTO monthlyBudgetDTO) {

		MonthlyBudget monthlyBudget = modelMapper.map(monthlyBudgetDTO, MonthlyBudget.class);

		User user = userRepository.findByUserId(monthlyBudgetDTO.getUserId());

		List<ConstantExpense> constantExpenses = new ArrayList<ConstantExpense>();
		List<OneTimeExpense> oneTimeExpenses = new ArrayList<OneTimeExpense>();

		for (UserPreferenceConstantExpense preferencedConstantExpense : user.getUserPreferenceConstantExpenses()) {

			ConstantExpense constantExpense = new ConstantExpense(preferencedConstantExpense.getName(), 0, 0);

			constantExpenses.add(constantExpense);
			System.out.println("const expense: " + constantExpense);
		}

		monthlyBudget.setConstantExpenses(constantExpenses);
		monthlyBudget.setOneTimeExpenses(oneTimeExpenses); 
		monthlyBudget.setUser(user);

		return modelMapper.map(monthlyBudgetRepository.save(monthlyBudget), MonthlyBudgetDTO.class);
	}

	@Override
	@Transactional
	public List<MonthlyBudgetDTO> findAllByUserId(Long userId) {
		
		return monthlyBudgetRepository.findAllByUserId(userId)
				.stream()
				.map(budget -> modelMapper.map(budget, MonthlyBudgetDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public MonthlyBudgetDTO update(MonthlyBudgetDTO monthlyBudgetDTO) {
		
		MonthlyBudget monthlyBudget = modelMapper.map(monthlyBudgetDTO, MonthlyBudget.class);
		
		User user = userRepository.findByUserId(monthlyBudgetDTO.getUserId());
		monthlyBudget.setUser(user);
		
		List<ConstantExpense> currentConstantExpenses = constantExpenseRepository.findAllByMonthlyBudgetId(monthlyBudget.getId());
		List<ConstantExpense> updatedConstantExpenses = monthlyBudget.getConstantExpenses();
		
		System.out.println("current: " + currentConstantExpenses);
		System.out.println("updated: " + updatedConstantExpenses);
		
		boolean constantExpenseAlreadyExists = false;
		
		for (int i = 0; i < updatedConstantExpenses.size(); i++) {
			for (int j = 0; j < currentConstantExpenses.size(); j++) {
				if (updatedConstantExpenses.get(i).getId()
						!= (currentConstantExpenses.get(j).getId())
						&& updatedConstantExpenses.get(i).getName()
						.equals(currentConstantExpenses.get(j).getName())) {
					updatedConstantExpenses.remove(i);
					monthlyBudget.getConstantExpenses().remove(i);
				}
			}
		}
		
		System.out.println("modified expenses: ");
		for (ConstantExpense constantExpense : monthlyBudget.getConstantExpenses()) {
			System.out.println(constantExpense);
		}
		
//		Set<UserPreferenceConstantExpense> userPreferenceConstantExpenses = 
//				user.getUserPreferenceConstantExpenses();
//		
//		if (userPreferenceConstantExpenses.get().getName() == null) {
//			System.out.println("list null");
//		}
//		
//		System.out.println("--------------------------");
//		for (ConstantExpense constantExpense : monthlyBudget.getConstantExpenses()) {
//			
//			for (UserPreferenceConstantExpense userConstantExepnse : userPreferenceConstantExpenses) {
//				if (!(userPreferenceConstantExpenses.getName().equalsIgnoreCase(constantExpense.getName())
//						|| user)) {
//					userPreferenceConstantExpenses
//						.add(new UserPreferenceConstantExpense(0L, constantExpense.getName()));
//					continue;
//				}
//			}
//		}
//		
//		for (UserPreferenceConstantExpense expense : userPreferenceConstantExpenses) {
//			System.out.println("expense: " + expense);
//		}
//			
//		user.setUserPreferenceConstantExpenses(userPreferenceConstantExpenses);
//		userRepository.save(user);
//		
//		System.out.println("--------------------------");
		
		return modelMapper.map(monthlyBudgetRepository.save(monthlyBudget), MonthlyBudgetDTO.class);
	}
}










