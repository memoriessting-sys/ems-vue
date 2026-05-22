package cqie.edu.ems.comm;

import lombok.Data;

//分页参数
@Data
public class PageParameter {
    private Integer pageIndex = 1;// 页号,起始值1
    private Integer pageSize = 15;// 页面记录数
}
