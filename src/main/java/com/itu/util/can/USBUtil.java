package com.itu.util.can;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import org.usb4java.BufferUtils;
import org.usb4java.Context;
import org.usb4java.Device;
import org.usb4java.DeviceDescriptor;
import org.usb4java.DeviceHandle;
import org.usb4java.DeviceList;
import org.usb4java.LibUsb;
import org.usb4java.LibUsbException;

public class USBUtil {
	
	public boolean connect() {
		UsbDriver usbDriver = new UsbDriver();
		ControlCAN controlCan = new ControlCAN(usbDriver);
		// Scan device
        Device mUsbDevice = controlCan.VCI_ScanDevice();
        if(mUsbDevice == null){
            System.out.println("No device connected");
            return false;
        }

        // Open device
        int ret = controlCan.VCI_OpenDevice();
        if(ret != ErrorType.ERR_SUCCESS){
            System.out.println("Open device error!\n");
            System.out.println(String.format("Error code: %d\n",ret));
            return false;
        }else{
            System.out.println("Open device success!\n");
        }

        VCI_INIT_CONFIG_EX CAN_InitEx = new VCI_INIT_CONFIG_EX();
        CAN_InitEx.CAN_ABOM = 0;//Automatic bus-off management
        // Loop back
        CAN_InitEx.CAN_Mode = 0;
        //1Mbps
        CAN_InitEx.CAN_BRP = 12;
        CAN_InitEx.CAN_BS1 = 4;
        CAN_InitEx.CAN_BS2 = 1;
        CAN_InitEx.CAN_SJW = 1;
        CAN_InitEx.CAN_NART = 1;//No automatic retransmission
        CAN_InitEx.CAN_RFLM = 0;//Receive FIFO locked mode
        CAN_InitEx.CAN_TXFP = 1;//Transmit FIFO priority
        CAN_InitEx.CAN_RELAY = 0;
        ret = controlCan.VCI_InitCANEx((byte)0, CAN_InitEx);
        if(ret != ErrorType.ERR_SUCCESS){
            System.out.println("Init device failed!\n");
            System.out.println(String.format("Error code: %d\n",ret));
            return false;
        }else{
            System.out.println("Init device success!\n");
        }
        //Set filter
        VCI_FILTER_CONFIG CAN_FilterConfig = new VCI_FILTER_CONFIG();
        CAN_FilterConfig.FilterIndex = 0;
        CAN_FilterConfig.Enable = 1;//Enable
        CAN_FilterConfig.ExtFrame = 1;
        CAN_FilterConfig.FilterMode = 0;
        CAN_FilterConfig.ID_IDE = 0;
        CAN_FilterConfig.ID_RTR = 0;
        CAN_FilterConfig.ID_Std_Ext = 1;
        CAN_FilterConfig.MASK_IDE = 0;
        CAN_FilterConfig.MASK_RTR = 0;
        CAN_FilterConfig.MASK_Std_Ext = 1;
        ret = controlCan.VCI_SetFilter((byte)0, CAN_FilterConfig);
        if(ret != ErrorType.ERR_SUCCESS){
            System.out.println("Set filter failed!\n");
            System.out.println(String.format("Error code: %d\n",ret));
            return false;
        }else{
            System.out.println("Set filter success!\n");
        }

        // Start CAN
        ret = controlCan.VCI_StartCAN((byte)0);
        if(ret != ErrorType.ERR_SUCCESS){
            System.out.println("Start CAN failed!\n");
            System.out.println(String.format("Error code: %d\n",ret));
            return false;
        }else{
            System.out.println("Start CAN success!\n");
        }
        
        
        Thread canReader = new Thread(new CanReader(controlCan));
        canReader.start();
        return true;
//        
//        while (true) {
//        
//        	try {
//                Thread.sleep(5000);
//            }catch (InterruptedException e) {
//                return;
//            }	
//        	
//	        ControlCAN.VCI_CAN_OBJ CAN_SendData[] =  new ControlCAN.VCI_CAN_OBJ[1];
//	        for (int i = 0; i < CAN_SendData.length; i++) {
//	            CAN_SendData[i] = controlCan.new VCI_CAN_OBJ();
//	            CAN_SendData[i].DataLen = 8;
//	            CAN_SendData[i].Data = new byte[8];
//	            for (int j = 0; j < CAN_SendData[i].DataLen; j++) {
//	                CAN_SendData[i].Data[j] = (byte) (8-j);
//	        }
//	        CAN_SendData[i].ExternFlag = 0;
//	        CAN_SendData[i].RemoteFlag = 0;
//	        CAN_SendData[i].ID = 0x45A;
//	        //CAN_SendData[i].SendType = 2;
//	        CAN_SendData[i].SendType = 0;
//	        }
//	        ret = controlCan.VCI_Transmit((byte)0, CAN_SendData, CAN_SendData.length);
//	        if(ret != ErrorType.ERR_SUCCESS){
//	            System.out.println("Send CAN data failed!");
//	            System.out.println(String.format("Error code: %d",ret));
//	            //return;
//	        }else{
//	            System.out.println("Send CAN data success!");
//	        }
//
//        
//        
//        }
        	
        
        
	        
	
       
		 
	}
}
