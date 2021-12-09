package com.jacekg.homefinances.budget;

import java.time.LocalDate;
import java.util.List;

public interface BudgetService {
	
	MonthlyBudget findByUserIdAndDate(Long userId, LocalDate date);
	
	MonthlyBudgetDTO save(MonthlyBudgetDTO monthlyBudgetDTO);

	List<MonthlyBudgetDTO> findAllByUserId(Long userId);
}
