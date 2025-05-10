package com.alex.myexpenses.entity.expenses;

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
@Table(name = "categories")
@Data
@NoArgsConstructor
public class CategoryEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "name", length = 100, nullable = false)
	private String name;
	
	@Column(name = "color", length = 50)
	private String color;
	
	@Column(name = "is_default", nullable = false)
	private Boolean isDefault;
	
	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<ExpenseEntity> expenses;
	
	@ManyToOne
	@JoinColumn(name = "id_user", nullable = true)
	private UserEntity user;

}
