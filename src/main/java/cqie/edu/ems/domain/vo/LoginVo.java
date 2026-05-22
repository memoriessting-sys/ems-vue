package cqie.edu.ems.domain.vo;

import lombok.Data;
import java.util.List;

@Data
public class LoginVo {
    private String token;
    private Long userId;
    private String account;
    private String name;
    private Integer roleType;
    private List<String> permissions;
}
