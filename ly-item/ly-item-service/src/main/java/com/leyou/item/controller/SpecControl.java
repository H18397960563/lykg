package com.leyou.item.controller;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.server.SpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("spec")
public class SpecControl {

    @Autowired
    private SpecService specService;

    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecGroups(
            @PathVariable("cid") Long cid
    ){
        List<SpecGroup> specGroups = specService.querySpecGroups(cid);
        if(specGroups!=null&&specGroups.size()!=0){
            return ResponseEntity.ok(specGroups);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    ///spec/group
    @PostMapping("group")
    public ResponseEntity<Void> addSpecGroup(@RequestBody SpecGroup specGroup){
        specService.addSpecGroup(specGroup);
               return ResponseEntity.ok().build();
    }
    ///spec/group
    @PutMapping("group")
    public ResponseEntity<Void> updateSpecGroup(@RequestBody SpecGroup specGroup){
        specService.updateSpecGroup(specGroup);
        return ResponseEntity.ok().build();
    }
    ///spec/group/28
    @DeleteMapping("group/{id}")
    public ResponseEntity<Void> deleteSpecGroup(@PathVariable("id") Long id){
        SpecGroup specGroup = new SpecGroup();
        specGroup.setId(id);
        specService.deleteSpecGroup(specGroup);
        return ResponseEntity.ok().build();
    }
    ///params?gid=1
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> querySpecParams(
            @RequestParam(value = "gid",required = false)Long gid,
            @RequestParam(value = "cid",required = false)Long cid,
            @RequestParam(value = "searching",required = false)Boolean searching,
            @RequestParam(value = "generic",required = false)Boolean generic
    ){
        List<SpecParam> specParams = specService.querySpecParams(gid,cid,searching,generic);
        if(specParams!=null&&specParams.size()!=0){
            return ResponseEntity.ok(specParams);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    //spec/param
    @PostMapping("param")
    public ResponseEntity<Void> addSpecGroupParam(@RequestBody SpecParam specParam){
        System.out.println("control层新增一个参数："+specParam);
        specService.addSpecGroupParam(specParam);
        return ResponseEntity.ok().build();
    }

    //spec/param
    @PutMapping("param")
    public ResponseEntity<Void> updateSpecGroupParam(@RequestBody SpecParam specParam){
        System.out.println("control层修改一个参数："+specParam);
        specService.updateSpecGroupParam(specParam);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("param/{id}")
    public ResponseEntity<Void> deleteSpecParam(@PathVariable("id") Long id){
        specService.deleteSpecParam(id);
        return ResponseEntity.ok().build();
    }
}
