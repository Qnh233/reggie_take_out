package com.hao.dto;

import com.hao.domain.DishFlavor;
import com.hao.domain.Setmeal;
import com.hao.domain.SetmealDish;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 必燃
 * @version 1.0
 * @create 2023-01-15 22:04
 */
@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes = new ArrayList<>();

    private String categoryName;

}
