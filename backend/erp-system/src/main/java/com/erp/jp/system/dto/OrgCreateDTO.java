package com.erp.jp.system.dto;

/**
 * 组织创建/更新 DTO
 */
public class OrgCreateDTO {
    private String orgNameJa;
    private String orgNameEn;
    private Long parentOrgId;
    private String orgCode;
    private Long leaderId;
    private Integer sortOrder;
    private Integer status;

    public String getOrgNameJa() { return orgNameJa; }
    public void setOrgNameJa(String orgNameJa) { this.orgNameJa = orgNameJa; }
    public String getOrgNameEn() { return orgNameEn; }
    public void setOrgNameEn(String orgNameEn) { this.orgNameEn = orgNameEn; }
    public Long getParentOrgId() { return parentOrgId; }
    public void setParentOrgId(Long parentOrgId) { this.parentOrgId = parentOrgId; }
    public String getOrgCode() { return orgCode; }
    public void setOrgCode(String orgCode) { this.orgCode = orgCode; }
    public Long getLeaderId() { return leaderId; }
    public void setLeaderId(Long leaderId) { this.leaderId = leaderId; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}
