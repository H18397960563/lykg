package com.leyou.item.server;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.leyou.common.utils.PageResult;
import com.leyou.item.mapper.*;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.pojo.Stock;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SpuService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private SpudDetailMapper spudDetailMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    public PageResult<Spu> querySpu(String key, Boolean saleable, Integer page, Integer rows) {
       PageHelper.startPage(page,rows);
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNoneBlank(key)){
            criteria.andLike("title","%"+key+"%");
        }
        if (null!=saleable){
            criteria.andEqualTo("saleable",saleable);
        }
        Page<Spu> spuPage = (Page<Spu>) spuMapper.selectByExample(example);
        List<Spu> result = spuPage.getResult();
        result.forEach(r->r.setCname(StringUtils.join(categoryService.queryNameByIds(Arrays.asList(r.getCid1(),r.getCid2(),r.getCid3())),"/")));
        result.forEach(r->r.setBname(
                brandMapper.selectByPrimaryKey(r.getBrandId()).getName()
        ));
        return new PageResult<Spu>(spuPage.getTotal(),new Long(spuPage.getPages()),spuPage.getResult());
    }

    public void addSpuAndSpuDetail(Spu spu) {
        SpuDetail spuDetail = spu.getSpuDetail();
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(new Date());
        spu.setValid(true);//是否有效
        spu.setSaleable(true);//是否上架
        spuMapper.insertSelective(spu);
        spuDetail.setSpuId(new Long(spu.getId()));
        spudDetailMapper.insert(spuDetail);
        List<Sku> skuList = spu.getSkus();
        skuList.forEach(sku -> addSkuAndStock(spu,sku));
        sendMessage("insert",spu.getId());
    }

    public void addSkuAndStock(Spu spu , Sku sku){
               sku.setSpuId(spu.getId());
               sku.setCreateTime(new Date());
               sku.setLastUpdateTime(new Date());
               skuMapper.insertSelective(sku);
               Stock stock = new Stock();
               stock.setStock(sku.getStock());
               stock.setSkuId(sku.getId());
               stockMapper.insert(stock);
    }

    public SpuDetail querySpuDetailById(Long spuId) {
       return spudDetailMapper.selectByPrimaryKey(spuId);
    }

    public List<Sku> querySkuBySpuId(Long id) {
        Sku sku = new Sku();
        sku.setSpuId(id);
        List<Sku> skus = skuMapper.select(sku);
        skus.forEach(sku1 ->sku1.setStock(stockMapper.selectByPrimaryKey(sku1.getId()).getStock()));
        return skus;
    }

    public void updateSpuAndSpuDetail(Spu spu) {
        SpuDetail spuDetail = spu.getSpuDetail();
        spu.setLastUpdateTime(new Date());
        spuMapper.updateByPrimaryKeySelective(spu);
        spudDetailMapper.updateByPrimaryKey(spuDetail);
         //使用for循环删除stock
      /*  Sku sku = new Sku();
        sku.setSpuId(spu.getId());
        List<Sku> selectList = skuMapper.select(sku);
        selectList.forEach(selectlist -> {
            stockMapper.deleteByPrimaryKey(selectlist.getId());
        });*/

       //使用list删除stock
      /*  Sku sku = new Sku();
        sku.setSpuId(spu.getId());
        List<Sku> selectList = skuMapper.select(sku);
        List<Long> ids = new ArrayList<>();
        selectList.forEach(selectlist -> {
            ids.add(selectlist.getId());
        });*/
       //使用list删除stock
        Sku sku = new Sku();
        sku.setSpuId(spu.getId());
        List<Sku> selectList = skuMapper.select(sku);
        if(!CollectionUtils.isEmpty(selectList)){
            List<Long> ids = selectList.stream().map(Sku::getId).collect(Collectors.toList());
            stockMapper.deleteByIdList(ids);
        }

        //使用新特性删除，list

        skuMapper.delete(sku);
        List<Sku> skuList = spu.getSkus();
        skuList.forEach(skulist -> addSkuAndStock(spu,skulist));
        sendMessage("update",spu.getId());
    }

    private void sendMessage(String type, Long id) {
        amqpTemplate.convertAndSend("item."+type,id);
    }

    public Spu querySpuById(Long spuId) {
        return spuMapper.selectByPrimaryKey(spuId);
    }

    // private void deleteMessage(String type, Long id) {
    //        amqpTemplate.convertAndSend("item."+type,id);
    //    }
}
