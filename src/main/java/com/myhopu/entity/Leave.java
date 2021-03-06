package com.myhopu.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class Leave {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TB_LEAVE.LEAVE_ID
     *
     * @mbg.generated Tue Nov 13 14:09:01 GMT+08:00 2018
     */
    private Long leaveId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TB_LEAVE.LEAVE_TYPE
     *
     * @mbg.generated Tue Nov 13 14:09:01 GMT+08:00 2018
     */
    private String leaveType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TB_LEAVE.TIME_START
     *
     * @mbg.generated Tue Nov 13 14:09:01 GMT+08:00 2018
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date timeStart;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TB_LEAVE.TIME_END
     *
     * @mbg.generated Tue Nov 13 14:09:01 GMT+08:00 2018
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date timeEnd;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TB_LEAVE.TIME_USED
     *
     * @mbg.generated Tue Nov 13 14:09:01 GMT+08:00 2018
     */
    private Double timeUsed;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TB_LEAVE.USER_ID
     *
     * @mbg.generated Tue Nov 13 14:09:01 GMT+08:00 2018
     */
    private Long userId;

    private SysUser user;
    
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TB_LEAVE.LEAVE_STATUS
     *
     * @mbg.generated Tue Nov 13 14:09:01 GMT+08:00 2018
     */
    private Integer leaveStatus;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TB_LEAVE.LEAVE_ID
     *
     * @return the value of TB_LEAVE.LEAVE_ID
     *
     * @mbg.generated Tue Nov 13 14:09:01 GMT+08:00 2018
     */
    public Long getLeaveId() {
        return leaveId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TB_LEAVE.LEAVE_ID
     *
     * @param leaveId the value for TB_LEAVE.LEAVE_ID
     *
     * @mbg.generated Tue Nov 13 14:09:01 GMT+08:00 2018
     */
    public void setLeaveId(Long leaveId) {
        this.leaveId = leaveId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TB_LEAVE.LEAVE_TYPE
     *
     * @return the value of TB_LEAVE.LEAVE_TYPE
     *
     * @mbg.generated Tue Nov 13 14:09:01 GMT+08:00 2018
     */
    public String getLeaveType() {
        return leaveType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TB_LEAVE.LEAVE_TYPE
     *
     * @param leaveType the value for TB_LEAVE.LEAVE_TYPE
     *
     * @mbg.generated Tue Nov 13 14:09:01 GMT+08:00 2018
     */
    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType == null ? null : leaveType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TB_LEAVE.TIME_START
     *
     * @return the value of TB_LEAVE.TIME_START
     *
     * @mbg.generated Tue Nov 13 14:09:01 GMT+08:00 2018
     */
    public Date getTimeStart() {
        return timeStart;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TB_LEAVE.TIME_START
     *
     * @param timeStart the value for TB_LEAVE.TIME_START
     *
     * @mbg.generated Tue Nov 13 14:09:01 GMT+08:00 2018
     */
    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TB_LEAVE.TIME_END
     *
     * @return the value of TB_LEAVE.TIME_END
     *
     * @mbg.generated Tue Nov 13 14:09:01 GMT+08:00 2018
     */
    public Date getTimeEnd() {
        return timeEnd;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TB_LEAVE.TIME_END
     *
     * @param timeEnd the value for TB_LEAVE.TIME_END
     *
     * @mbg.generated Tue Nov 13 14:09:01 GMT+08:00 2018
     */
    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

    

    public Double getTimeUsed() {
		return timeUsed;
	}

	public void setTimeUsed(Double timeUsed) {
		this.timeUsed = timeUsed;
	}

	/**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TB_LEAVE.USER_ID
     *
     * @return the value of TB_LEAVE.USER_ID
     *
     * @mbg.generated Tue Nov 13 14:09:01 GMT+08:00 2018
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TB_LEAVE.USER_ID
     *
     * @param userId the value for TB_LEAVE.USER_ID
     *
     * @mbg.generated Tue Nov 13 14:09:01 GMT+08:00 2018
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    

	public Integer getLeaveStatus() {
		return leaveStatus;
	}

	public void setLeaveStatus(Integer leaveStatus) {
		this.leaveStatus = leaveStatus;
	}
	
	public SysUser getUser() {
		return user;
	}

	public void setUser(SysUser user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Leave [leaveId=" + leaveId + ", leaveType=" + leaveType + ", timeStart=" + timeStart + ", timeEnd="
				+ timeEnd + ", timeUsed=" + timeUsed + ", userId=" + userId + ", leaveStatus=" + leaveStatus + "]";
	}
    
    
}