package com.leyou.item.server;

import com.leyou.item.pojo.Category;
import com.leyou.item.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> queryByParentId(Long id) {
        Category category = new Category();
        category.setParentId(id);
        List<Category> categories = categoryMapper.select(category);
        return categories;
    }

    public List<Category> queryBrandById(Long bid) {
       return categoryMapper.queryBrandById(bid);
    }

    public List<String> queryNameByIds(List<Long> ids) {
        List<Category> categories = categoryMapper.selectByIdList(ids);
        List<String> names = new ArrayList<>();
        categories.forEach(category -> names.add(category.getName()));
        return names;
    }
}
