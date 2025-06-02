package com.alex.myexpenses.entity.expenses;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.alex.myexpenses.entity.user.UserEntity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "expense_list")
@Data
@NoArgsConstructor
public class ExpenseListEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9060366669952128072L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "name", length = 100, nullable = false)
	private String name;
	
	@Column(name = "month", nullable = false)
	private Integer month;
	
	@Column(name = "budget", nullable = false)
	private Double budget;
	
	@Column(name = "total_expense", nullable = false)
	private Double totalExpense;
	
	@ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
	private UserEntity user;
	
	@OneToMany(mappedBy = "expenseList", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<ExpenseEntity> expenses;
}
