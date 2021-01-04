package com.winbaoxian.crawler.enums;

/**
 * 步骤行为
 */
public enum StepSignal {

    step_next, //执行下一步
    step_break, //跳出当前循环
    step_continue, //执行下一组步骤
    step_throwe, //抛出异常，在whileGroup、forGroup、ifGroup中且needStore=true时，则异常被捕获
    ;
}
