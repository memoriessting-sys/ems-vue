package cqie.edu.ems.domain.qo;

import lombok.Data;

@Data
public class SysDeptQo {
    private String name;
    private Long manager_user_id;
    private String managerUserName;
    private Long parent_id;
    private Integer sort_code;
    private Integer status;
}
