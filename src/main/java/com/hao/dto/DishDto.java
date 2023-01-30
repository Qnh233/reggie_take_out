package com.hao.dto;

import com.hao.domain.Dish;
import com.hao.domain.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 必燃
 * @version 1.0
 * @create 2023-01-14 22:18
 */
@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
