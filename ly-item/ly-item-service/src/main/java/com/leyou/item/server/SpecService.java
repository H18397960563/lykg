package com.leyou.item.server;

import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecService {

    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecParamMapper specParamMapper;

    public List<SpecGroup> querySpecGroups(Long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        List<SpecGroup> specGroups = specGroupMapper.select(specGroup);
        //商品详情
        specGroups.forEach(s -> {

            SpecParam paramRecord = new SpecParam();
            //根据规格组的id查询规格参数
            paramRecord.setGroupId(specGroup.getId());
            specGroup.setSpecParams(specParamMapper.select(paramRecord));
        });
        return specGroups;
    }

    public List<SpecParam> querySpecParams(Long gid, Long cid, Boolean searching, Boolean generic) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setSearching(searching);
        specParam.setGeneric(generic);
        List<SpecParam> specParams = specParamMapper.select(specParam);
        return specParams;
    }

    public void addSpecGroup(SpecGroup specGroup) {
            specGroupMapper.insert(specGroup);
    }

    public void addSpecGroupParam(SpecParam specParam) {
        int insert = specParamMapper.insert(specParam);
        System.out.println("是否新增成功："+insert);
    }

    public void updateSpecGroup(SpecGroup specGroup) {
        specGroupMapper.updateByPrimaryKey(specGroup);
    }

    public void deleteSpecGroup(SpecGroup specGroup) {
        specGroupMapper.deleteByPrimaryKey(specGroup);
    }

    public void updateSpecGroupParam(SpecParam specParam) {
        int i = specParamMapper.updateByPrimaryKey(specParam);
        System.out.println("是否修改成功："+i);
    }

    public void deleteSpecParam(Long id) {
        SpecParam specParam = new SpecParam();
        specParam.setId(id);
        specParamMapper.deleteByPrimaryKey(specParam);
    }
}
