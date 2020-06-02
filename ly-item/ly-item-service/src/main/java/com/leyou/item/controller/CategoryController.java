package com.leyou.item.controller;

import com.leyou.item.pojo.Category;
import com.leyou.item.server.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("list")
    public ResponseEntity<List<Category>> queryByParentId(@RequestParam("pid") Long id){
        List<Category> categories = categoryService.queryByParentId(id);
        if(categories!=null&&categories.size()!=0){
            return ResponseEntity.ok(categories);
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("bid/{bid}")
    public ResponseEntity<List<Category>> queryBrandById(@PathVariable("bid") Long bid){
        List<Category> categories = categoryService.queryBrandById(bid);
        if(categories!=null&&categories.size()!=0){
            return ResponseEntity.ok(categories);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @GetMapping("names")
    public List<String> queryNameByIds(@RequestParam("ids") List<Long> ids){
          return categoryService.queryNameByIds(ids);
    }

}
