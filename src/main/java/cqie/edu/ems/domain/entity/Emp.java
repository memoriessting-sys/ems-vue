package cqie.edu.ems.domain.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 员工信息表(Emp)实体类
 */
@Data
public class Emp implements Serializable {
    private static final long serialVersionUID = -62485820550867755L;
    /**
     * 员工Id
     */
    private Long id;
    /**
     * 姓名
     */
    private String name;
    /**
     * 工号
     */
    private String code;
    /**
     * 电子邮箱
     */
    private String email;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 身份证号
     */
    private String idCard;
    /**
     * 出生日期
     */
    private Date birthday;
    /**
     * 性别(0-未知 1-男 2-女)
     */
    private Integer sex;
    /**
     * 民族
     */
    private String nationCode;
    /**
     * 政治面貌
     */
    private String politicalCode;
    /**
     * 籍贯
     */
    private String nativePlace;
    /**
     * 毕业院校
     */
    private String graduateSchool;
    /**
     * 专业
     */
    private String majorCode;
    /**
     * 最高学历
     */
    private String highestEducationCode;
    /**
     * 最高学位
     */
    private String highestDegreeCode;
    /**
     * 现居住地
     */
    private String habitation;
    /**
     * 婚姻状态(0-未知 1-未婚 2-已婚 3-离异 )
     */
    private Integer maritalStatus;
    /**
     * 入职日期
     */
    private Date entryDate;
    /**
     * 离职日期
     */
    private Date leaveDate;
    /**
     * 任职部门Id
     */
    private Long deptId;
    /**
     * 岗位岗级Id
     */
    private Long postLevelId;
    /**
     * 绑定用户Id
     */
    private Long userId;
    /**
     * 角色类型
     */
    private Integer roleType;
    /**
     * 状态(0-在职 1-离职)
     */
    private Integer status;

}
