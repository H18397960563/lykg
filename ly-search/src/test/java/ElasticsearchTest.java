import com.leyou.ElasticsearchApplication;
import com.leyou.client.GoodsClient;
import com.leyou.common.utils.PageResult;
import com.leyou.entity.Goods;
import com.leyou.item.pojo.Spu;
import com.leyou.mapper.GoodsMapper;
import com.leyou.service.IndexService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElasticsearchApplication.class)
public class ElasticsearchTest {

    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private IndexService indexService;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private GoodsClient  goodsClient;

    @Test
    public void createIndexesAndPutMapping(){
        template.createIndex(Goods.class);
        template.putMapping(Goods.class);
    }

    //输入数据到索引库
    @Test
    public void loadData(){
        int page = 1;
        List<Goods> goods = new ArrayList<>();
        while (true){
            PageResult<Spu> spuPageResult = goodsClient.querySpuByPage(null, null, page, 50);
            if(spuPageResult==null) break;
            page++;
            List<Spu> items = spuPageResult.getItems();
            items.forEach(i->{
                Goods goods1 = indexService.buildGoods(i);
                goods.add(goods1);
            });
        }
        goodsMapper.saveAll(goods);
    }
}

