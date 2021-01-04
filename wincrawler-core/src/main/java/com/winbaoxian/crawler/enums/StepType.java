package com.winbaoxian.crawler.enums;

/**
 * 步骤类型
 */
public enum StepType {

    /*------------逻辑判断--------------*/
    /**
     * IF条件(附加数据持久化)
     */
    ifGroup(StepMode.logic),
    /**
     * IF条件 continue, break
     */
    ifSignal(StepMode.logic),
    /**
     * 遍历(附加数据持久化)
     */
    forGroup(StepMode.logic),
    /**
     * 循环(附加数据持久化)
     */
    whileGroup(StepMode.logic),

    /**
     * 设置变量
     */
    setV(StepMode.logic),

    /*-----------selenium操作-----------*/
    /**
     * 点击
     */
    click(StepMode.selenium),
    /**
     * TODO
     * 单选
     */
    radio(StepMode.selenium),

    /**
     * TODO
     * 复选框
     */
    checkbox(StepMode.selenium),
    /**
     * TODO
     * 复选框多选
     */
    multiCheckbox(StepMode.selenium),
    /**
     * 输入文本
     */
    inputText(StepMode.selenium),
    /**
     * 打开网页
     */
    openWebPage(StepMode.selenium),
    /**
     * 验证码校验
     */
    validateCode(StepMode.selenium),
    /**
     * 下拉框选择（扩展，逻辑上）
     */
    selectEx(StepMode.selenium),
    /**
     * 下拉框单选
     */
    select(StepMode.selenium),
    /**
     * 下拉框多选
     */
    multiSelect(StepMode.selenium),
    /**
     * TODO
     * 悬浮
     */
    hove(StepMode.selenium),
    /**
     * 提取数据
     */
    extractData(StepMode.selenium),
    /**
     * 获取cookie
     */
    fetchCookie(StepMode.selenium),
    /**
     * 设置cookie
     */
    setCookie(StepMode.selenium),

    /*-----------ajax操作-----------*/
    /**
     * http请求
     */
    http(StepMode.ajax),
    /**
     * 识别图片验证码
     */
    ocr(StepMode.ajax),
    /**
     * 提取数据http请求
     */
    extractDataHttp(StepMode.ajax),
    ;

    /**
     * @see com.winbaoxian.crawler.enums.StepMode
     */
    private StepMode mode;

    StepType(StepMode mode) {
        this.mode = mode;
    }

    public StepMode getMode() {
        return mode;
    }

    public static StepType getStepType(String type) {
        for (StepType stepType : StepType.values()) {
            if (stepType.name().equalsIgnoreCase(type)) {
                return stepType;
            }
        }
        return null;
    }

}
