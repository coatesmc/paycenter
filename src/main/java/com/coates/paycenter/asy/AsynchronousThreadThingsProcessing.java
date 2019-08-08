package com.coates.paycenter.asy;

import com.coates.paycenter.enums.PaymentStatus;

/**
 * 
 * <p>Title: AsynchronousThreadThingsProcessing.java </p>
 * <p>Package com.shenpinkj.common.timer </p>
 * <p>Description: TODO(异步线程事物处理)  </p>
 * <p>Company: www.shenpinkj.cn</p> 
 * @author 牟超
 * @date	2017年12月11日上午10:55:32
 * @version 1.0
 */
public class AsynchronousThreadThingsProcessing extends Thread{
		
	private int type;
	private String orderNo;
	
	/**
	 * 
	 * @param type 状态
	 * @param orderNo 订单号
	 */
	public AsynchronousThreadThingsProcessing(int type, String orderNo) {
		this.type = type;
		this.orderNo = orderNo;
	}

	@Override
	public void run(){
		//手动注入AllBean
		// IdataUnifiedProcessingService dataUnifiedProcessingService = (IdataUnifiedProcessingService) AllBean.getBean("dataUnifiedProcessingService");
		//type:1 线程为提现异步处理 剩下的自己处理 配合自己的业务逻辑
		if(PaymentStatus.PAYMENT_CALLBACK.getCode()==type) {

		}
    }
	
	
}
