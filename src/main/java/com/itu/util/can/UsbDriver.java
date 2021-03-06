package com.itu.util.can;

/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  android.app.PendingIntent
 *  android.hardware.usb.UsbDevice
 *  android.hardware.usb.UsbDeviceConnection
 *  android.hardware.usb.UsbEndpoint
 *  android.hardware.usb.UsbInterface
 *  android.hardware.usb.UsbManager
 *  android.util.Log
 */

import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Collection;
import java.util.HashMap;

import org.usb4java.BufferUtils;
import org.usb4java.Context;
import org.usb4java.Device;
import org.usb4java.DeviceDescriptor;
import org.usb4java.DeviceHandle;
import org.usb4java.DeviceList;
import org.usb4java.LibUsb;
import org.usb4java.LibUsbException;

public class UsbDriver {
//    private UsbManager usbManager;
    private Device usbDevice;
//    private UsbInterface usbInterface;
//    private UsbEndpoint BulkInEndpoint;
//    private UsbEndpoint BulkOutEndpoint;
//    private UsbDeviceConnection connection;
//    private PendingIntent pendingIntent;
	private Context context = null;
    private UserType UserType = new UserType();
    private short vendorId = 1155;
    private short productId = 1638;
    private DeviceHandle handle;
    /** The input endpoint of the USB-CAN Interface. */
    private static final byte IN_ENDPOINT = (byte) 0x82;
    /** The output endpoint of the USB-CAN Interface. */
    private static final byte OUT_ENDPOINT =  (byte) 0x02;
    private static final int TIMEOUT = 5000;
    
    public UsbDriver() {
        this.context = new Context();
		int result = LibUsb.init(context);
		if (result != LibUsb.SUCCESS) throw new LibUsbException("Unable to initialize libusb.", result);
        this.usbDevice = this.ScanDevices();
//        OpenDevice();
    }

    public Device ScanDevices() {
    	// Read the USB device list
	    DeviceList list = new DeviceList();
	    int result = LibUsb.getDeviceList(null, list);
	    if (result < 0) throw new LibUsbException("Unable to get device list", result);

	    try
	    {
	        // Iterate over all devices and scan for the right one
	        for (Device device: list)
	        {
	            DeviceDescriptor descriptor = new DeviceDescriptor();
	            result = LibUsb.getDeviceDescriptor(device, descriptor);
	            if (result != LibUsb.SUCCESS) throw new LibUsbException("Unable to read device descriptor", result);
	            if ( descriptor.idVendor() == vendorId && descriptor.idProduct() == productId) { 
	            	System.out.println("Found can bus");
	            	return device;
	            }
	        }
	    }
	    finally
	    {
	        // Ensure the allocated device list is freed
	    	//System.out.println("Exception");
	        //LibUsb.freeDeviceList(list, true);
	    }
	    // Device not found
	    return null;
    }

    public int OpenDevice() {
    	this.handle = new DeviceHandle();
		int result = LibUsb.open(this.usbDevice, handle);
		if (result != LibUsb.SUCCESS) throw new LibUsbException("Unable to open USB device", result);
		System.out.println("USB device opened ");
	    // Use device handle here
		result = LibUsb.claimInterface(handle , 0);
		if (result != LibUsb.SUCCESS) throw new LibUsbException("Unable to claim interface", result);
		System.out.println("USB Interface claimed ");
		return result;
    }

    public synchronized int USBWriteData(byte[] writebuffer, int length, int timeout) {
    	ByteBuffer buffer = BufferUtils.allocateByteBuffer(length);
        buffer.put(writebuffer,0,length);
        IntBuffer transferred = BufferUtils.allocateIntBuffer();
        int result = LibUsb.bulkTransfer(handle, OUT_ENDPOINT, buffer,
            transferred, timeout);
        if (result != LibUsb.SUCCESS)
        {
            throw new LibUsbException("Unable to send data", result);
        }
        int res = 0;
        res = transferred.get();
        //System.out.println(( res = transferred.get() )+ " bytes sent to device");
        return res;
    }

    public synchronized int USBReadData(byte[] readbuffer, int length, int timeout) {
    	ByteBuffer buffer = BufferUtils.allocateByteBuffer(length).order(
                ByteOrder.LITTLE_ENDIAN);
        IntBuffer transferred = BufferUtils.allocateIntBuffer();
        int result = LibUsb.bulkTransfer(handle, IN_ENDPOINT, buffer,
            transferred, timeout);
        if (result != LibUsb.SUCCESS)
        {
            throw new LibUsbException("Unable to read data", result);
        	//return 0;
        }
        int res = 0;
        res = transferred.get();
        //System.out.println((res = transferred.get()) + " bytes read from device");
  
        // transfer bytes from this buffer into the given destination array
        buffer.get(readbuffer, 0, length);
        buffer.clear();
        return res;
    }

    public int USB_SendMsg(S_MSG msg) {
        int ret;
        byte[] SendBuff = new byte[10240];
        int i = 0;
        SendBuff[i++] = (byte)(msg.message >> 24);
        SendBuff[i++] = (byte)(msg.message >> 16);
        SendBuff[i++] = (byte)(msg.message >> 8);
        SendBuff[i++] = (byte)(msg.message >> 0);
        SendBuff[i++] = msg.PackNum;
        SendBuff[i++] = (byte)(msg.size >> 8);
        SendBuff[i++] = (byte)(msg.size >> 0);
        if (msg.size > 0) {
            int j = 0;
            while (j < msg.size) {
                SendBuff[i++] = msg.buffer[j];
                ++j;
            }
        }
        if ((ret = this.USBWriteData(SendBuff, i, 5000)) != i) {
            return -5;
        }
        return 0;
    }

    public int USB_GetMsg(UserType.PS_MSG msg) {
        byte[] GetBuff = new byte[10240];
        int i = 0;
        int ret = this.USBReadData(GetBuff, 512, TIMEOUT);
        if (ret >= 7) {
            msg.value.message = GetBuff[0] << 24 | GetBuff[1] << 16 | GetBuff[2] << 8 | GetBuff[3] << 0;
            msg.value.status = GetBuff[4];
            int temp = GetBuff[5] * 256 & 65280;
            msg.value.size = (short)(temp += GetBuff[6] & 255);
            if (msg.value.size > 0) {
                i = 0;
                while (i < msg.value.size) {
                    msg.value.buffer[i] = GetBuff[7 + i];
                    ++i;
                }
            }
            return 0;
        }
        return -6;
    }

    public int USB_GetMsgWithSize(UserType.PS_MSG msg) {
        byte[] GetBuff = new byte[msg.value.size + 7];
        int i = 0;
        int ret = this.USBReadData(GetBuff, msg.value.size + 7, TIMEOUT);
        if (ret >= 7) {
            msg.value.message = GetBuff[0] << 24 | GetBuff[1] << 16 | GetBuff[2] << 8 | GetBuff[3] << 0;
            msg.value.status = GetBuff[4];
            int temp = GetBuff[5] * 256 & 65280;
            msg.value.size = (short)(temp += GetBuff[6] & 255);
            if (msg.value.size > 0) {
                i = 0;
                while (i < msg.value.size) {
                    msg.value.buffer[i] = GetBuff[7 + i];
                    ++i;
                }
            }
            return 0;
        }
        return -6;
    }

    public int USB_GetMsgEx(UserType.PS_MSG msg) {
        byte[] GetBuff = new byte[10240];
        int i = 0;
        int ret = this.USBReadData(GetBuff, 7, TIMEOUT);
        if (ret >= 7) {
            msg.value.message = GetBuff[0] << 24 | GetBuff[1] << 16 | GetBuff[2] << 8 | GetBuff[3] << 0;
            msg.value.status = GetBuff[4];
            int temp = GetBuff[5] * 256 & 65280;
            msg.value.size = (short)(temp += GetBuff[6] & 255);
            if (msg.value.size > 0) {
                ret = this.USBReadData(GetBuff, msg.value.size, TIMEOUT);
                i = 0;
                while (i < ret) {
                    msg.value.buffer[i] = GetBuff[i];
                    ++i;
                }
            }
            return 0;
        }
        System.out.printf("USB_GetMsgEx %d\n", ret);
        return -6;
    }

    public synchronized byte USB_GetStatus() {
        byte[] GetBuff = new byte[1];
        int ret = this.USBReadData(GetBuff, 1, TIMEOUT);
        if (ret != 1) {
            return -6;
        }
        return GetBuff[0];
    }
}

