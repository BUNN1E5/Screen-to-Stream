import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;


public class ScreenCap {

	public Robot bot;
	
	public ScreenCap()
	{
		try {
			bot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public byte[] getSendData(Point location, Dimension screen)
	{
		byte[] returnData;
		ByteArrayOutputStream sendBaos = new ByteArrayOutputStream ();
		BufferedImage img = bot.createScreenCapture(new Rectangle(location.x, location.y, screen.width, screen.height));
		try {
			ImageIO.write(img, "jpg", sendBaos);
			sendBaos.flush();
			returnData = sendBaos.toByteArray();
			sendBaos.close();
		} catch (Exception e) {
			returnData = new byte[]{};
		}
		return returnData;
	}
	
	public BufferedImage screenCap(Point location, Dimension screen)
	{
		BufferedImage img = bot.createScreenCapture(new Rectangle(location.x, location.y, screen.width, screen.height));
		return img;
	}
	
	public BufferedImage displayImage(byte[] bytes)
	{
		BufferedImage bImageFromConvert = new BufferedImage(1, 1, 1);
		try{
			InputStream in = new ByteArrayInputStream(bytes);
			bImageFromConvert = ImageIO.read(in);
		}
		catch(Exception e)
		{
			
		}
		return bImageFromConvert;
	}
}
