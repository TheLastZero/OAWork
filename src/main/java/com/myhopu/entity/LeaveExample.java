package com.myhopu.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LeaveExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table TB_LEAVE
     *
     * @mbg.generated Tue Nov 13 14:09:01 GMT+08:00 2018
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table TB_LEAVE
     *
     * @mbg.generated Tue Nov 13 14:09:01 GMT+08:00 2018
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table TB_LEAVE
     *
     * @mbg.generated Tue Nov 13 14:09:01 GMT+08:00 2018
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_LEAVE
     *
     * @mbg.generated Tue Nov 13 14:09:01 GMT+08:00 2018
     */
    public LeaveExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_LEAVE
     *
     * @mbg.generated Tue Nov 13 14:09:01 GMT+08:00 2018
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_LEAVE
     *
     * @mbg.generated Tue Nov 13 14:09:01 GMT+08:00 2018
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_LEAVE
     *
     * @mbg.generated Tue Nov 13 14:09:01 GMT+08:00 2018
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_LEAVE
     *
     * @mbg.generated Tue Nov 13 14:09:01 GMT+08:00 2018
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_LEAVE
     *
     * @mbg.generated Tue Nov 13 14:09:01 GMT+08:00 2018
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_LEAVE
     *
     * @mbg.generated Tue Nov 13 14:09:01 GMT+08:00 2018
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_LEAVE
     *
     * @mbg.generated Tue Nov 13 14:09:01 GMT+08:00 2018
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_LEAVE
     *
     * @mbg.generated Tue Nov 13 14:09:01 GMT+08:00 2018
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_LEAVE
     *
     * @mbg.generated Tue Nov 13 14:09:01 GMT+08:00 2018
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_LEAVE
     *
     * @mbg.generated Tue Nov 13 14:09:01 GMT+08:00 2018
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table TB_LEAVE
     *
     * @mbg.generated Tue Nov 13 14:09:01 GMT+08:00 2018
     */
    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andLeaveIdIsNull() {
            addCriterion("LEAVE_ID is null");
            return (Criteria) this;
        }

        public Criteria andLeaveIdIsNotNull() {
            addCriterion("LEAVE_ID is not null");
            return (Criteria) this;
        }

        public Criteria andLeaveIdEqualTo(Long value) {
            addCriterion("LEAVE_ID =", value, "leaveId");
            return (Criteria) this;
        }

        public Criteria andLeaveIdNotEqualTo(Long value) {
            addCriterion("LEAVE_ID <>", value, "leaveId");
            return (Criteria) this;
        }

        public Criteria andLeaveIdGreaterThan(Long value) {
            addCriterion("LEAVE_ID >", value, "leaveId");
            return (Criteria) this;
        }

        public Criteria andLeaveIdGreaterThanOrEqualTo(Long value) {
            addCriterion("LEAVE_ID >=", value, "leaveId");
            return (Criteria) this;
        }

        public Criteria andLeaveIdLessThan(Long value) {
            addCriterion("LEAVE_ID <", value, "leaveId");
            return (Criteria) this;
        }

        public Criteria andLeaveIdLessThanOrEqualTo(Long value) {
            addCriterion("LEAVE_ID <=", value, "leaveId");
            return (Criteria) this;
        }

        public Criteria andLeaveIdIn(List<Long> values) {
            addCriterion("LEAVE_ID in", values, "leaveId");
            return (Criteria) this;
        }

        public Criteria andLeaveIdNotIn(List<Long> values) {
            addCriterion("LEAVE_ID not in", values, "leaveId");
            return (Criteria) this;
        }

        public Criteria andLeaveIdBetween(Long value1, Long value2) {
            addCriterion("LEAVE_ID between", value1, value2, "leaveId");
            return (Criteria) this;
        }

        public Criteria andLeaveIdNotBetween(Long value1, Long value2) {
            addCriterion("LEAVE_ID not between", value1, value2, "leaveId");
            return (Criteria) this;
        }

        public Criteria andLeaveTypeIsNull() {
            addCriterion("LEAVE_TYPE is null");
            return (Criteria) this;
        }

        public Criteria andLeaveTypeIsNotNull() {
            addCriterion("LEAVE_TYPE is not null");
            return (Criteria) this;
        }

        public Criteria andLeaveTypeEqualTo(String value) {
            addCriterion("LEAVE_TYPE =", value, "leaveType");
            return (Criteria) this;
        }

        public Criteria andLeaveTypeNotEqualTo(String value) {
            addCriterion("LEAVE_TYPE <>", value, "leaveType");
            return (Criteria) this;
        }

        public Criteria andLeaveTypeGreaterThan(String value) {
            addCriterion("LEAVE_TYPE >", value, "leaveType");
            return (Criteria) this;
        }

        public Criteria andLeaveTypeGreaterThanOrEqualTo(String value) {
            addCriterion("LEAVE_TYPE >=", value, "leaveType");
            return (Criteria) this;
        }

        public Criteria andLeaveTypeLessThan(String value) {
            addCriterion("LEAVE_TYPE <", value, "leaveType");
            return (Criteria) this;
        }

        public Criteria andLeaveTypeLessThanOrEqualTo(String value) {
            addCriterion("LEAVE_TYPE <=", value, "leaveType");
            return (Criteria) this;
        }

        public Criteria andLeaveTypeLike(String value) {
            addCriterion("LEAVE_TYPE like", value, "leaveType");
            return (Criteria) this;
        }

        public Criteria andLeaveTypeNotLike(String value) {
            addCriterion("LEAVE_TYPE not like", value, "leaveType");
            return (Criteria) this;
        }

        public Criteria andLeaveTypeIn(List<String> values) {
            addCriterion("LEAVE_TYPE in", values, "leaveType");
            return (Criteria) this;
        }

        public Criteria andLeaveTypeNotIn(List<String> values) {
            addCriterion("LEAVE_TYPE not in", values, "leaveType");
            return (Criteria) this;
        }

        public Criteria andLeaveTypeBetween(String value1, String value2) {
            addCriterion("LEAVE_TYPE between", value1, value2, "leaveType");
            return (Criteria) this;
        }

        public Criteria andLeaveTypeNotBetween(String value1, String value2) {
            addCriterion("LEAVE_TYPE not between", value1, value2, "leaveType");
            return (Criteria) this;
        }

        public Criteria andTimeStartIsNull() {
            addCriterion("TIME_START is null");
            return (Criteria) this;
        }

        public Criteria andTimeStartIsNotNull() {
            addCriterion("TIME_START is not null");
            return (Criteria) this;
        }

        public Criteria andTimeStartEqualTo(Date value) {
            addCriterion("TIME_START =", value, "timeStart");
            return (Criteria) this;
        }

        public Criteria andTimeStartNotEqualTo(Date value) {
            addCriterion("TIME_START <>", value, "timeStart");
            return (Criteria) this;
        }

        public Criteria andTimeStartGreaterThan(Date value) {
            addCriterion("TIME_START >", value, "timeStart");
            return (Criteria) this;
        }

        public Criteria andTimeStartGreaterThanOrEqualTo(Date value) {
            addCriterion("TIME_START >=", value, "timeStart");
            return (Criteria) this;
        }

        public Criteria andTimeStartLessThan(Date value) {
            addCriterion("TIME_START <", value, "timeStart");
            return (Criteria) this;
        }

        public Criteria andTimeStartLessThanOrEqualTo(Date value) {
            addCriterion("TIME_START <=", value, "timeStart");
            return (Criteria) this;
        }

        public Criteria andTimeStartIn(List<Date> values) {
            addCriterion("TIME_START in", values, "timeStart");
            return (Criteria) this;
        }

        public Criteria andTimeStartNotIn(List<Date> values) {
            addCriterion("TIME_START not in", values, "timeStart");
            return (Criteria) this;
        }

        public Criteria andTimeStartBetween(Date value1, Date value2) {
            addCriterion("TIME_START between", value1, value2, "timeStart");
            return (Criteria) this;
        }

        public Criteria andTimeStartNotBetween(Date value1, Date value2) {
            addCriterion("TIME_START not between", value1, value2, "timeStart");
            return (Criteria) this;
        }

        public Criteria andTimeEndIsNull() {
            addCriterion("TIME_END is null");
            return (Criteria) this;
        }

        public Criteria andTimeEndIsNotNull() {
            addCriterion("TIME_END is not null");
            return (Criteria) this;
        }

        public Criteria andTimeEndEqualTo(Date value) {
            addCriterion("TIME_END =", value, "timeEnd");
            return (Criteria) this;
        }

        public Criteria andTimeEndNotEqualTo(Date value) {
            addCriterion("TIME_END <>", value, "timeEnd");
            return (Criteria) this;
        }

        public Criteria andTimeEndGreaterThan(Date value) {
            addCriterion("TIME_END >", value, "timeEnd");
            return (Criteria) this;
        }

        public Criteria andTimeEndGreaterThanOrEqualTo(Date value) {
            addCriterion("TIME_END >=", value, "timeEnd");
            return (Criteria) this;
        }

        public Criteria andTimeEndLessThan(Date value) {
            addCriterion("TIME_END <", value, "timeEnd");
            return (Criteria) this;
        }

        public Criteria andTimeEndLessThanOrEqualTo(Date value) {
            addCriterion("TIME_END <=", value, "timeEnd");
            return (Criteria) this;
        }

        public Criteria andTimeEndIn(List<Date> values) {
            addCriterion("TIME_END in", values, "timeEnd");
            return (Criteria) this;
        }

        public Criteria andTimeEndNotIn(List<Date> values) {
            addCriterion("TIME_END not in", values, "timeEnd");
            return (Criteria) this;
        }

        public Criteria andTimeEndBetween(Date value1, Date value2) {
            addCriterion("TIME_END between", value1, value2, "timeEnd");
            return (Criteria) this;
        }

        public Criteria andTimeEndNotBetween(Date value1, Date value2) {
            addCriterion("TIME_END not between", value1, value2, "timeEnd");
            return (Criteria) this;
        }

        public Criteria andTimeUsedIsNull() {
            addCriterion("TIME_USED is null");
            return (Criteria) this;
        }

        public Criteria andTimeUsedIsNotNull() {
            addCriterion("TIME_USED is not null");
            return (Criteria) this;
        }

        public Criteria andTimeUsedEqualTo(BigDecimal value) {
            addCriterion("TIME_USED =", value, "timeUsed");
            return (Criteria) this;
        }

        public Criteria andTimeUsedNotEqualTo(BigDecimal value) {
            addCriterion("TIME_USED <>", value, "timeUsed");
            return (Criteria) this;
        }

        public Criteria andTimeUsedGreaterThan(BigDecimal value) {
            addCriterion("TIME_USED >", value, "timeUsed");
            return (Criteria) this;
        }

        public Criteria andTimeUsedGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("TIME_USED >=", value, "timeUsed");
            return (Criteria) this;
        }

        public Criteria andTimeUsedLessThan(BigDecimal value) {
            addCriterion("TIME_USED <", value, "timeUsed");
            return (Criteria) this;
        }

        public Criteria andTimeUsedLessThanOrEqualTo(BigDecimal value) {
            addCriterion("TIME_USED <=", value, "timeUsed");
            return (Criteria) this;
        }

        public Criteria andTimeUsedIn(List<BigDecimal> values) {
            addCriterion("TIME_USED in", values, "timeUsed");
            return (Criteria) this;
        }

        public Criteria andTimeUsedNotIn(List<BigDecimal> values) {
            addCriterion("TIME_USED not in", values, "timeUsed");
            return (Criteria) this;
        }

        public Criteria andTimeUsedBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("TIME_USED between", value1, value2, "timeUsed");
            return (Criteria) this;
        }

        public Criteria andTimeUsedNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("TIME_USED not between", value1, value2, "timeUsed");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("USER_ID is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("USER_ID is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(Long value) {
            addCriterion("USER_ID =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(Long value) {
            addCriterion("USER_ID <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(Long value) {
            addCriterion("USER_ID >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(Long value) {
            addCriterion("USER_ID >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(Long value) {
            addCriterion("USER_ID <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(Long value) {
            addCriterion("USER_ID <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<Long> values) {
            addCriterion("USER_ID in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<Long> values) {
            addCriterion("USER_ID not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(Long value1, Long value2) {
            addCriterion("USER_ID between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(Long value1, Long value2) {
            addCriterion("USER_ID not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andLeaveStatusIsNull() {
            addCriterion("LEAVE_STATUS is null");
            return (Criteria) this;
        }

        public Criteria andLeaveStatusIsNotNull() {
            addCriterion("LEAVE_STATUS is not null");
            return (Criteria) this;
        }

        public Criteria andLeaveStatusEqualTo(Long value) {
            addCriterion("LEAVE_STATUS =", value, "leaveStatus");
            return (Criteria) this;
        }

        public Criteria andLeaveStatusNotEqualTo(Long value) {
            addCriterion("LEAVE_STATUS <>", value, "leaveStatus");
            return (Criteria) this;
        }

        public Criteria andLeaveStatusGreaterThan(Long value) {
            addCriterion("LEAVE_STATUS >", value, "leaveStatus");
            return (Criteria) this;
        }

        public Criteria andLeaveStatusGreaterThanOrEqualTo(Long value) {
            addCriterion("LEAVE_STATUS >=", value, "leaveStatus");
            return (Criteria) this;
        }

        public Criteria andLeaveStatusLessThan(Long value) {
            addCriterion("LEAVE_STATUS <", value, "leaveStatus");
            return (Criteria) this;
        }

        public Criteria andLeaveStatusLessThanOrEqualTo(Long value) {
            addCriterion("LEAVE_STATUS <=", value, "leaveStatus");
            return (Criteria) this;
        }

        public Criteria andLeaveStatusIn(List<Long> values) {
            addCriterion("LEAVE_STATUS in", values, "leaveStatus");
            return (Criteria) this;
        }

        public Criteria andLeaveStatusNotIn(List<Long> values) {
            addCriterion("LEAVE_STATUS not in", values, "leaveStatus");
            return (Criteria) this;
        }

        public Criteria andLeaveStatusBetween(Long value1, Long value2) {
            addCriterion("LEAVE_STATUS between", value1, value2, "leaveStatus");
            return (Criteria) this;
        }

        public Criteria andLeaveStatusNotBetween(Long value1, Long value2) {
            addCriterion("LEAVE_STATUS not between", value1, value2, "leaveStatus");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table TB_LEAVE
     *
     * @mbg.generated do_not_delete_during_merge Tue Nov 13 14:09:01 GMT+08:00 2018
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table TB_LEAVE
     *
     * @mbg.generated Tue Nov 13 14:09:01 GMT+08:00 2018
     */
    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}