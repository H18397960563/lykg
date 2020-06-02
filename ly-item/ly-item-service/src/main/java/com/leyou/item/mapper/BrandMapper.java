package com.leyou.item.mapper;

import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends Mapper<Brand> {

    @Insert("insert into tb_category_brand values(#{cid},#{bid})")
     void insetBrandCategory(Long cid,Long bid);

    @Delete("delete from tb_category_brand where brand_id=#{id}")
    void deleteCategoryBrandByCid(Long id);

    @Select("select tb.* from tb_category_brand tcb left join tb_brand tb on  tcb.brand_id=tb.id where tcb.category_id=#{cid}")
    List<Brand> selectBrand(Long cid);
}
