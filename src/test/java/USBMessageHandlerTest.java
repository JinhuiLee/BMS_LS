

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.itu.system.handler.USBMassageHandler;

public class USBMessageHandlerTest {

	public static void main(String[] args) {
		byte[] data = new byte[8];
		data[0]= 4;
		data[1]= 2;
		for(int i=2; i<data.length;i++){
			data[i] = 77;
		}
		
		ApplicationContext aContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		USBMassageHandler uSBMassageHandler = (USBMassageHandler) aContext.getBean("USBMassageHandler");
		uSBMassageHandler.handleMessage(data);
		System.out.println("!!!!!!!!!insert data!!!!!!!!!!");
	}
}
