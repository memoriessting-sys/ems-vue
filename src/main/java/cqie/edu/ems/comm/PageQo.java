package cqie.edu.ems.comm;

import lombok.Data;
import lombok.ToString;

//分页查询条件
@Data
@ToString(callSuper = true)
public class PageQo<Qo> extends PageParameter {
    private Qo filters;//查询条件
}


