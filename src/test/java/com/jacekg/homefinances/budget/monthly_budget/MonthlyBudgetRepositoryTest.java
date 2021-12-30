package com.jacekg.homefinances.budget.monthly_budget;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.jacekg.homefinances.user.User;

@DataJpaTest
class MonthlyBudgetRepositoryTest {
	
	@Autowired
	MonthlyBudgetRepository monthlyBudgetRepository;
	
	@Test
	void findByUserIdAndDate_ShouldReturnMonthlyBudget() {
		
		Long userId = 1L;
		
		User user = new User();
		user.setId(1L);
		
		LocalDate date = LocalDate.now().withDayOfMonth(1);
		
		MonthlyBudget monthlyBudget = new MonthlyBudget
				(1L, date, 0, 0, user, null, null);
		
		monthlyBudgetRepository.save(monthlyBudget);
		
		MonthlyBudget returnedBudget 
			= monthlyBudgetRepository.findByUserIdAndDate(userId, date);
		
		assertEquals(1L, returnedBudget.getId());
		assertEquals(1L, returnedBudget.getUser().getId());
		assertEquals(date, returnedBudget.getDate());
	}

}
