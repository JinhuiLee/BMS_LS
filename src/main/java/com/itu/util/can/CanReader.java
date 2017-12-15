package com.itu.util.can;

import java.util.Arrays;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.itu.system.handler.USBMassageHandler;
import com.itu.util.can.ControlCAN.VCI_CAN_OBJ;

public class CanReader extends Thread {
	public ControlCAN.VCI_CAN_OBJ CAN_ReceiveData[] =  new ControlCAN.VCI_CAN_OBJ[1];
	public ControlCAN controlCan;
	
	
	public CanReader(ControlCAN controlCan) {
		this.controlCan = controlCan;
		for (int i = 0; i < CAN_ReceiveData.length; i++)
        {
            CAN_ReceiveData[i] = controlCan.new VCI_CAN_OBJ();
            CAN_ReceiveData[i].Data = new byte[8];
        }
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		System.out.println("Creating VCI_CanReader");
		ApplicationContext aContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		USBMassageHandler uSBMassageHandler = (USBMassageHandler)aContext.getBean("USBMassageHandler");
		while (!controlCan.CanReaderExitFlag) {
			
			try {
                Thread.sleep(20);
            }
            catch (InterruptedException e2) {
                e2.printStackTrace();
            }
			
			
			// CAN read data
			for (int i = 0; i < CAN_ReceiveData.length; i++)
	        {
				Arrays.fill(CAN_ReceiveData[i].Data,(byte)0);
	        }
			
	        int ReadDataNum;
	        int DataNum = controlCan.VCI_GetReceiveNum((byte)0);
	        if(DataNum > 0)
	        {
	            ReadDataNum = controlCan.VCI_Receive((byte)0, CAN_ReceiveData, CAN_ReceiveData.length);
	            for(int i = 0; i < ReadDataNum; i++)
	            {
	                System.out.println("");
	                System.out.println("--CAN_ReceiveData.RemoteFlag = "
	                        + String.format("%d", CAN_ReceiveData[i].RemoteFlag) + "");
	                System.out.println("--CAN_ReceiveData.ExternFlag = "
	                        + String.format("%d", CAN_ReceiveData[i].ExternFlag) + "");
	                System.out.println("--CAN_ReceiveData.ID = 0x"
	                        + String.format("%x", CAN_ReceiveData[i].ID) + "");
	                System.out.println("--CAN_ReceiveData.DataLen = "
	                        + String.format("%d", CAN_ReceiveData[i].DataLen) + "");
	                System.out.println("--CAN_ReceiveData.Data:");
	                
	                int[] data_int = new int[CAN_ReceiveData[i].Data.length];
	                for(int j = 0; j < CAN_ReceiveData[i].DataLen; j++){
	                    System.out.print(String.format("%x",CAN_ReceiveData[i].Data[j]) + " ");
	                    data_int[j] = CAN_ReceiveData[i].Data[j];
	                }
	                System.out.println("\n");
	                System.out.println("--CAN_ReceiveData.TimeStamp = "+ String.format("%d", CAN_ReceiveData[i].TimeStamp));
	                
	                
	        		uSBMassageHandler.handleMessage(CAN_ReceiveData[i].Data);
	        		System.out.println("!!!!!!!!!insert data!!!!!!!!!!");
	            }
	        }
		}
	}

}
