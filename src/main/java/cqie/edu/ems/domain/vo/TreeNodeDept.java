package cqie.edu.ems.domain.vo;

import lombok.Data;

import java.util.List;

//部门树节点
@Data
public class TreeNodeDept {
    //部门Id
    private String id;
    //部门名称
    private String text;
    //上级部门Id
    private String pid;
    //子部门列表
    private List<TreeNodeDept> children;

}
