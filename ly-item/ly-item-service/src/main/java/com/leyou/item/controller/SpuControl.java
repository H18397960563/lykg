package com.leyou.item.controller;

import com.leyou.common.utils.PageResult;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.server.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SpuControl {

    @Autowired
    private SpuService spuService;

    @GetMapping("spu/page")
    public ResponseEntity<PageResult<Spu>> querySpuByPage(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "saleable",required = false) Boolean saleable,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows){
        PageResult<Spu> spuPageResult = spuService.querySpu(key, saleable, page, rows);
        if(spuPageResult!=null&&spuPageResult.getItems()!=null&&spuPageResult.getItems().size()!=0){
            return ResponseEntity.ok(spuPageResult);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("goods")
    public ResponseEntity<Void> addSpuAndSpuDetail(@RequestBody Spu spu){
        spuService.addSpuAndSpuDetail(spu);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PutMapping("goods")
    public ResponseEntity<Void> updateSpuAndSpuDetail(@RequestBody Spu spu){
        spuService.updateSpuAndSpuDetail(spu);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    ///spu/detail/195
    @GetMapping("spu/detail/{spu_id}")
    public ResponseEntity<SpuDetail> querySpuDetailBySpuId(@PathVariable("spu_id") Long spuId){
        SpuDetail spuDetail = spuService.querySpuDetailById(spuId);
        if(spuDetail!=null){
            return ResponseEntity.ok(spuDetail);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //item/sku/list?id=3
    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> querySkuBySpuId(@RequestParam("id") Long id){
        List<Sku> skus = spuService.querySkuBySpuId(id);
        if(skus!=null){
            return ResponseEntity.ok(skus);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("spu/{id}")
    public ResponseEntity<Spu> querySpuById(@PathVariable("id") Long id){
        Spu spu = spuService.querySpuById(id);
        if(spu!=null){
            return ResponseEntity.ok(spu);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    };
}
