package com.leyou.item.server;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.leyou.common.utils.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandService {

    @Autowired
    private BrandMapper brandMapper;

    public PageResult<Brand> pageQuery(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        PageHelper.startPage(page,rows);
        Example example = new Example(Brand.class);
        if (StringUtils.isNoneBlank(key)){
            Example.Criteria criteria = example.createCriteria();
            criteria.andLike("name","%"+key+"%");
        }
        if(StringUtils.isNoneBlank(sortBy)){
            example.setOrderByClause(sortBy+(desc?" DESC":" ASC"));
        }
        Page<Brand> brandPage =(Page<Brand>) brandMapper.selectByExample(example);
        return new PageResult<>(brandPage.getTotal(),new Long(brandPage.getPages()),brandPage.getResult());
    }

    @Transactional
    public void addBrand(Brand brand, List<Long> ids) {
        brandMapper.insertSelective(brand);
        ids.forEach(id->brandMapper.insetBrandCategory(id,brand.getId()));
    }

    @Transactional
    public void updateBrand(Brand brand, List<Long> ids) {
        brandMapper.updateByPrimaryKey(brand);
        brandMapper.deleteCategoryBrandByCid(brand.getId());
        ids.forEach(id->brandMapper.insetBrandCategory(id,brand.getId()));
    }

    public List<Brand> queryBrand(Long cid) {
        List<Brand> brands = brandMapper.selectBrand(cid);
        return brands;
    }

    public Brand queryBrandById(Long bid) {
        Brand brand = brandMapper.selectByPrimaryKey(bid);
        return brand;
    }
}
