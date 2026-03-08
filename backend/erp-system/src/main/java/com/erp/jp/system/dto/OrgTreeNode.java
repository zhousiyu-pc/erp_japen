package com.erp.jp.system.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 组织树节点 - 用于前端展示
 */
public class OrgTreeNode {
    private Long id;
    private String label;
    private String orgCode;
    private Long parentId;
    private Integer status;
    private List<OrgTreeNode> children = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public String getOrgCode() { return orgCode; }
    public void setOrgCode(String orgCode) { this.orgCode = orgCode; }
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public List<OrgTreeNode> getChildren() { return children; }
    public void setChildren(List<OrgTreeNode> children) { this.children = children; }
}
