package com.jacekg.homefinances.irregular_expenses_budget;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jacekg.homefinances.expenses.IrregularExpenseRepository;
import com.jacekg.homefinances.expenses.model.IrregularExpense;
import com.jacekg.homefinances.monthly_budget.BudgetUtilities;
import com.jacekg.homefinances.user.User;
import com.jacekg.homefinances.user.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class IrregularExpensesBudgetServiceImpl implements IrregularExpensesBudgetService {
	
	private UserRepository userRepository;
	
	private IrregularExpensesBudgetRepository irregularExpensesBudgetRepository;
	
	private IrregularExpenseRepository irregularExpenseRepository;
	
	private ModelMapper modelMapper;
	
	@Override
	@Transactional
	public IrregularExpensesBudgetDTO findByUserIdAndDate(Long userId, LocalDate date) {
		
		IrregularExpensesBudget irregularExpensesBudget 
			= irregularExpensesBudgetRepository.findByUserIdAndDate(userId, date);
		
		if (irregularExpensesBudget == null) {
			return null;
		} else {
			return modelMapper.map(irregularExpensesBudget, IrregularExpensesBudgetDTO.class);
		}
	}
	
	@Override
	@Transactional
	public IrregularExpensesBudgetDTO save(IrregularExpensesBudgetDTO irregularExpensesBudgetDTO) {
		
		if (findByUserIdAndDate(irregularExpensesBudgetDTO.getUserId(), 
				irregularExpensesBudgetDTO.getDate()) != null) {
			throw new IrregularExpensesBudgetAlreadyExistsException
				("Budżet wydatków nieregularnych na dany rok istnieje!");
		}
		
		List<IrregularExpense> irregularExpenses = new ArrayList<IrregularExpense>();
		
		IrregularExpensesBudget irregularExpensesBudget 
			= modelMapper.map(irregularExpensesBudgetDTO, IrregularExpensesBudget.class);
		
		User user = userRepository.findByUserId(irregularExpensesBudgetDTO.getUserId());
		
		irregularExpensesBudget.setUser(user);
		irregularExpensesBudget.setIrregularExpenses(irregularExpenses);
		
		return modelMapper
				.map(irregularExpensesBudgetRepository.save(irregularExpensesBudget),
						IrregularExpensesBudgetDTO.class);
	}

	@Override
	@Transactional
	public List<IrregularExpensesBudgetDTO> findAllByUserId(Long userId) {
		
		return irregularExpensesBudgetRepository.findAllByUserId(userId)
				.stream()
				.map(budget -> modelMapper.map(budget, IrregularExpensesBudgetDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public IrregularExpensesBudgetDTO update(IrregularExpensesBudgetDTO irregularExpensesBudgetDTO) {

		IrregularExpensesBudget irregularExpensesBudget = modelMapper.map(irregularExpensesBudgetDTO,
				IrregularExpensesBudget.class);

		User user = userRepository.findByUserId(irregularExpensesBudgetDTO.getUserId());
		irregularExpensesBudget.setUser(user);

		List<IrregularExpense> currentIrregularExpenses = irregularExpenseRepository
				.findAllByIrregularExpensesBudgetId(irregularExpensesBudget.getId());
		List<IrregularExpense> updatedIrregularExpenses = irregularExpensesBudget.getIrregularExpenses();

		irregularExpensesBudget.setIrregularExpenses(
				BudgetUtilities.removeDuplicatedExpenses(currentIrregularExpenses, updatedIrregularExpenses));

		return modelMapper.map(irregularExpensesBudgetRepository.save(irregularExpensesBudget),
				IrregularExpensesBudgetDTO.class);
	}
}







