package cqie.edu.ems.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述: 系统功能菜单实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysMenu {
    /**
     * 主键
     */
    private Integer id;
    /**
     * 菜单名称
     */
    private String name;
    /**
     * 路由地址
     */
    private String url;
    /**
     * 顺序号
     */
    private Integer sortCode;
    /**
     * 上级菜单Id
     */
    private Integer parentId;
}
